import com.gitlab.ninjaphenix.gradle.api.task.MinifyJsonTask
import com.gitlab.ninjaphenix.gradle.api.task.ParamLocalObfuscatorTask
import org.gradle.jvm.tasks.Jar

plugins {
    java
    id("dev.architectury.loom").version("0.10.0-SNAPSHOT").apply(false)
    id("com.gitlab.ninjaphenix.gradle-utils").version("0.1.0-beta.2")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "dev.architectury.loom")

    group = properties["maven_group"] as String
    version = properties["mod_version"] as String
    base.archivesName.set(properties["archives_base_name"] as String)
    buildDir = rootDir.resolve("build/${project.name}")

    java {
        sourceCompatibility = JavaVersion.toVersion(properties["mod_java_version"] as String)
        targetCompatibility = JavaVersion.toVersion(properties["mod_java_version"] as String)
    }

    repositories {
        maven {
            url  = uri("https://cursemaven.com")
            content {
                includeGroup("curse.maven")
            }
        }
    }

    sourceSets {
        main {
            java {
                setSrcDirs(listOf(
                        "src/main/java",
                        "commonSrc/java"
                ))
            }
            resources {
                setSrcDirs(listOf(
                        "src/main/resources",
                        "commonSrc/resources"
                ))
            }
        }
    }

    dependencies {
        annotationProcessor("net.java.dev.jna:jna:5.9.0")
        annotationProcessor("net.java.dev.jna:jna-platform:5.9.0")
        annotationProcessor("com.github.bsideup.jabel:jabel-javac-plugin:0.4.1")
        compileOnly("com.github.bsideup.jabel:jabel-javac-plugin:0.4.1")
    }

    val minecraft_java_version : String by project
    val isNotIdeaSync = System.getProperties().containsKey("idea.sync.active").not()

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        if (isNotIdeaSync) {
            options.release.set(minecraft_java_version.toInt())
        }
    }

    tasks.withType<JavaExec>().configureEach {
        javaLauncher.set(javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(minecraft_java_version.toInt()))
        })
    }

    val remapJarTask : Jar = tasks.getByName<Jar>("remapJar") {
        archiveFileName.set("${properties["archivesBaseName"]}-${properties["mod_version"]}+${properties["minecraft_version"]}-fat.jar")
    }

    tasks.getByName<Jar>("jar") {
        archiveFileName.set("${properties["archivesBaseName"]}-${properties["mod_version"]}+${properties["minecraft_version"]}-dev.jar")
        from(rootDir.resolve("LICENSE"))
    }

    val minifyJarTask = tasks.register<MinifyJsonTask>("minJar") {
        input.set(remapJarTask.outputs.files.singleFile)
        archiveFileName.set("${properties["archivesBaseName"]}-${properties["mod_version"]}+${properties["minecraft_version"]}-min.jar")
        dependsOn(remapJarTask)
    }

    val releaseJarTask = tasks.register<ParamLocalObfuscatorTask>("releaseJar") {
        input.set(minifyJarTask.get().outputs.files.singleFile)
        archiveFileName.set("${properties["archivesBaseName"]}-${properties["mod_version"]}+${properties["minecraft_version"]}.jar")
        dependsOn(minifyJarTask)
    }

    tasks.getByName("build") {
        dependsOn(releaseJarTask)
    }
}

tasks.register("buildMod") {
    subprojects.forEach {
        dependsOn(it.tasks["build"])
    }
}
