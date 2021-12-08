import com.gitlab.ninjaphenix.gradle.api.task.MinifyJsonTask
import net.fabricmc.loom.task.RemapJarTask

plugins {
    alias(libs.plugins.gradle.utils)
    alias(libs.plugins.gradle.fabric)
}

sourceSets {
    main {
        resources {
            srcDir("src/main/generated")
        }
    }

    create("datagen") {
        compileClasspath += sourceSets.main.get().compileClasspath
        runtimeClasspath += sourceSets.main.get().runtimeClasspath
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

loom {
    runs {
        named("client") {
            ideConfigGenerated(false)
        }
        named("server") {
            ideConfigGenerated(false)
            serverWithGui()
        }
        create("datagen") {
            client()
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.datagen.modid=expandedstorage")
            runDir("build/fabric-datagen")
            source(sourceSets.getByName("datagen"))
        }
    }

    mixin {
        useLegacyMixinAp.set(false)
    }

    accessWidenerPath.set(file("src/main/resources/expandedstorage.accessWidener"))
}

repositories {
    maven {
        url  = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    maven {
        name = "Ladysnake maven"
        url = uri("https://ladysnake.jfrog.io/artifactory/mods")
        content {
            includeGroup("io.github.onyxstudios.Cardinal-Components-API")
        }
    }
    maven {
        name = "Devan maven"
        url = uri("https://raw.githubusercontent.com/Devan-Kerman/Devan-Repo/master/")
        content {
            includeGroup("net.devtech")
        }
    }
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }

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

    mavenLocal()
}

val excludeFabric: (ModuleDependency) -> Unit = {
    it.exclude("net.fabricmc")
    it.exclude("net.fabricmc.fabric-api")
}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = properties["minecraft_version"] as String)
    mappings(group = "net.fabricmc", name = "yarn", version = "${properties["minecraft_version"]}+build.${properties["yarn_version"]}", classifier = "v2")

    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = properties["fabric_loader_version"] as String)
    implementation(group = "org.jetbrains", name = "annotations", version = properties["jetbrains_annotations_version"] as String)
    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = properties["fabric_api_version"] as String)
    modImplementation(group = "curse.maven", name = "ninjaphenixs-container-library-530668", version = "3531691", dependencyConfiguration = excludeFabric)

    // For chest module
    modCompileOnly(group = "curse.maven", name = "statement-340604", version = "3423826", dependencyConfiguration = excludeFabric)
    modCompileOnly(group = "curse.maven", name = "towelette-309338", version = "3398761", dependencyConfiguration = excludeFabric)
    modImplementation(group = "curse.maven", name = "carrier-409184", version = "3504375", dependencyConfiguration = excludeFabric)
    modImplementation(group = "io.github.onyxstudios.Cardinal-Components-API", name = "cardinal-components-base", version = properties["cardinal_version"] as String)
    modImplementation(group = "io.github.onyxstudios.Cardinal-Components-API", name = "cardinal-components-entity", version = properties["cardinal_version"] as String)
    modRuntimeOnly(group = "net.devtech", name = "arrp", version = "0.4.2")

    modRuntimeOnly("me.lucko:fabric-permissions-api:0.1-SNAPSHOT")
    modImplementation(group = "local", name = "htm", version = "dda0f76870e3a424af53416603ff489b1c733b3d")
}

tasks.withType<ProcessResources> {
    val props = mutableMapOf("version" to properties["mod_version"]) // Needs to be mutable
    inputs.properties(props)
    filesMatching("fabric.mod.json") {
        expand(props)
    }
    exclude(".cache/*")
}

val mappingsMcVersion = if (hasProperty("mc")) findProperty("mc") else properties["minecraft_version"]

if (hasProperty("yv")) {
    val updateCommonSources = tasks.register<net.fabricmc.loom.task.MigrateMappingsTask>("updateCommonSources") {
        setInputDir(rootDir.toPath().resolve("fabric/commonSrc/main/java").toString())
        setOutputDir(rootDir.toPath().resolve("fabric/commonSrc/main/java").toString())
        setMappings("net.fabricmc:yarn:${mappingsMcVersion}+build.${findProperty("yv")}")
    }

    tasks.register<net.fabricmc.loom.task.MigrateMappingsTask>("updateFabricSources") {
        dependsOn(updateCommonSources)

        setInputDir(file("src/main/java").toString())
        setOutputDir(file("src/main/java").toString())
        setMappings("net.fabricmc:yarn:${mappingsMcVersion}+build.${findProperty("yv")}")
    }
}

tasks.register<net.fabricmc.loom.task.MigrateMappingsTask>("updateForgeSources") {
    setInputDir(rootDir.toPath().resolve("fabric/commonSrc/main/java").toString())
    setOutputDir(rootDir.toPath().resolve("forge/commonSrc/main/java").toString())
    setMappings("net.minecraft:mappings:${mappingsMcVersion}")
}

configurations {
    create("dev")
}

tasks.jar {
    archiveClassifier.set("dev")
}

artifacts {
    add("dev", tasks.jar.get().archiveFile) {
        builtBy(tasks.jar)
    }
}

val remapJarTask: RemapJarTask = tasks.getByName<RemapJarTask>("remapJar") {
    archiveClassifier.set("fat")
    dependsOn(tasks.jar)
}

val minifyJarTask = tasks.register<MinifyJsonTask>("minJar") {
    input.set(remapJarTask.outputs.files.singleFile)
    archiveClassifier.set("")
    from(rootDir.resolve("LICENSE"))
    dependsOn(remapJarTask)
}

tasks.getByName("build") {
    dependsOn(minifyJarTask)
}
