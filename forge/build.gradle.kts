import org.gradle.jvm.tasks.Jar

plugins {
    id("net.minecraftforge.gradle").version("5.1.+")
}

minecraft {
    mappings("official", "1.17.1")

    accessTransformer(file("src/common/resources/META-INF/accesstransformer.cfg")) // Currently, this location cannot be changed from the default.

    runs {
        create("client") {
            workingDirectory(rootProject.file("run"))
            mods {
                create("expandedstorage") {
                    source(sourceSets.main.get())
                }
            }
            // "SCAN": For mods scan.
            // "REGISTRIES": For firing of registry events.
            // "REGISTRYDUMP": For getting the contents of all registries.
            //property("forge.logging.markers", "REGISTRIES")

            // Recommended logging level for the console
            // You can set various levels here.
            // Please read: https://stackoverflow.com/questions/2031163/when-to-use-the-different-log-levels
            //property("forge.logging.console.level", "debug")
        }

        create("server") {
            workingDirectory(rootProject.file("run"))
            mods {
                create("expandedstorage") {
                    source(sourceSets.main.get())
                }
            }
            // "SCAN": For mods scan.
            // "REGISTRIES": For firing of registry events.
            // "REGISTRYDUMP": For getting the contents of all registries.
            //property("forge.logging.markers", "REGISTRIES")

            // Recommended logging level for the console
            // You can set various levels here.
            // Please read: https://stackoverflow.com/questions/2031163/when-to-use-the-different-log-levels
            //property("forge.logging.console.level", "debug")
        }
    }
}

repositories {
    maven {
        // JEI maven
        name = "Progwml6 maven"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    maven {
        // JEI maven - fallback
        name = "ModMaven"
        url = uri("https://modmaven.k-4u.nl")
    }
    mavenCentral()
}

dependencies {
    minecraft("net.minecraftforge:forge:${properties["minecraft_version"]}-${properties["forge_version"]}")
    //compileOnly fg.deobf("mezz.jei:jei-1.16.5:${jei_version}:api")
    implementation("org.jetbrains:annotations:21.0.1")
}

tasks.withType<ProcessResources>() {
    val props = mutableMapOf("version" to properties["mod_version"]) // Needs to be mutable
    inputs.properties(props)
    filesMatching("META-INF/mods.toml") {
        expand(props)
    }
}

val jarTask = tasks.getByName<Jar>("jar") {
    archiveFileName.set("${properties["archivesBaseName"]}-${properties["mod_version"]}+${properties["minecraft_version"]}-fat.jar")
}

jarTask.finalizedBy("reobfJar")

val minifyJarTask = tasks.register<com.gitlab.ninjaphenix.gradle.task.MinifyJsonTask>("minJar") {
    parent.set(jarTask.outputs.files.singleFile)
    filePatterns.set(listOf("**/*.json", "**/*.mcmeta"))
    archiveFileName.set("${properties["archivesBaseName"]}-${properties["mod_version"]}+${properties["minecraft_version"]}.jar")
    dependsOn(jarTask)
}

tasks.getByName("build") {
    dependsOn(minifyJarTask)
}
