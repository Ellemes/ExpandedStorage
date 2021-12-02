import com.matthewprenger.cursegradle.CurseArtifact
import com.modrinth.minotaur.request.VersionType

buildscript {
    dependencies {
        classpath(group = "com.google.guava", name = "guava", version = "30.1.1-jre") {
            because("Required by loom, conflicts with curse gradle.")
        }
        classpath(group = "com.google.code.gson", name = "gson", version = "2.8.8") {
            because("Required by loom, conflicts with curse gradle")
        }
    }
}

plugins {
    java
    alias(libs.plugins.gradle.modrinth)
    alias(libs.plugins.gradle.curseforge)
}

fun isMainSubProject(name: String): Boolean {
    return name == "fabric"
}

subprojects {
    apply(plugin = "java")

    group = properties["maven_group"] as String
    version = "${properties["mod_version"]}+${properties["minecraft_version"]}"
    base.archivesName.set(properties["archives_base_name"] as String)
    buildDir = rootDir.resolve("build/${project.name}")

    java {
        sourceCompatibility = JavaVersion.toVersion(properties["mod_java_version"] as String)
        targetCompatibility = JavaVersion.toVersion(properties["mod_java_version"] as String)
    }

    repositories {
        exclusiveContent {
            forRepository {
                flatDir {
                    name = "Local Dependencies"
                    dir(rootDir.resolve("local_dependencies"))
                }
            }
            filter {
                includeGroup("local")
            }
        }

    }

    sourceSets {
        main {
            java {
                setSrcDirs(listOf(
                        "src/main/java",
                        "commonSrc/main/java"
                ))
            }
            resources {
                setSrcDirs(listOf(
                        "src/main/resources",
                        rootDir.resolve("common/src/main/resources")
                ))
            }
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
}

tasks.register("buildMod") {
    subprojects.forEach {
        if (isMainSubProject(it.name)) {
            dependsOn(it.tasks["build"])
        }
    }
}

val fabricProject = findProject(":fabric")

var modrinthFabricTask : TaskProvider<com.modrinth.minotaur.TaskModrinthUpload>? = null

var curseforgeFabricTask : TaskProvider<com.matthewprenger.cursegradle.CurseUploadTask>? = null

val realChangelog = rootDir.resolve("changelog.md").readText(Charsets.UTF_8)
val modrinthToken: String? = System.getenv("MODRINTH_TOKEN")
val curseforgeToken: String? = System.getenv("CURSEFORGE_TOKEN")

if (modrinthToken != null) {
    if (fabricProject != null) {
        modrinthFabricTask = tasks.register<com.modrinth.minotaur.TaskModrinthUpload>("publishModrinthFabric") {
            val releaseJarTask = fabricProject.tasks.getByName("minJar")
            dependsOn(releaseJarTask)

            detectLoaders = false
            changelog = realChangelog
            token = modrinthToken
            projectId = properties["modrinth_project_id"] as String
            versionName = "Fabric ${properties["mod_version"]}+${properties["minecraft_version"]}"
            versionNumber = "${properties["mod_version"]}+${properties["minecraft_version"]}-fabric"
            versionType = VersionType.RELEASE
            uploadFile = releaseJarTask
            addGameVersion(properties["minecraft_version"] as String)
            addLoader("fabric")
        }
    }
}

if (curseforgeToken != null) {
    var gameVersion = properties["minecraft_version"] as String
    if ("w" in gameVersion || "rc" in gameVersion) {
        gameVersion = "1.18-Snapshot"
    }

    if (fabricProject != null) {
        curseforgeFabricTask = tasks.register<com.matthewprenger.cursegradle.CurseUploadTask>("publishCurseforgeFabric") {
            val releaseJarTask = fabricProject.tasks.getByName("minJar")
            dependsOn(releaseJarTask)

            apiKey = curseforgeToken
            projectId = properties["curseforge_fabric_project_id"] as String
            mainArtifact = CurseArtifact().apply {
                artifact = releaseJarTask
                changelogType = "markdown"
                changelog = realChangelog
                displayName = "[${properties["minecraft_version"]}] ${properties["mod_version"]}"
                releaseType = "release"
                gameVersionStrings = listOf(gameVersion, "Fabric", "Java ${properties["mod_java_version"]}")
            }
            additionalArtifacts = listOf()
        }
    }
}

val publishTask = tasks.create("publish") {
    listOf(modrinthFabricTask, curseforgeFabricTask).forEach {
        if (it != null) {
            this.dependsOn(it)
            this.mustRunAfter(it)
        }
    }
}
