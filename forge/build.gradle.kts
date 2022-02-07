import com.gitlab.ninjaphenix.gradle.api.task.MinifyJsonTask
import org.gradle.jvm.tasks.Jar

plugins {
    id("ninjaphenix.gradle-utils")
    id("net.minecraftforge.gradle")
    id("org.spongepowered.mixin")
}

minecraft {
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

        create("data") {
            workingDirectory(rootProject.file("run"))
            mods {
                create("expandedstorage") {
                    sources(sourceSets.main.get(), sourceSets.getByName("datagen"))
                }
            }
            args("--mod", "expandedstorage", "--all",
                    "--output", file("src/main/generated"),
                    "--existing", file("src/main/resources"),
                    "--existing", rootDir.resolve("common/src/main/resources"))
        }
    }
}

repositories {
    maven {
        url  = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
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
    mavenLocal()
}

dependencies {
    //implementation(fg.deobf("curse.maven:ninjaphenixs-container-library-530668:3549171"))
    implementation(fg.deobf("ninjaphenix:container_library:1.3.0+1.18:forge"))

    //implementation(fg.deobf("mezz.jei:jei-${properties["jei_minecraft_version"]}:${properties["jei_version"]}"))
}

val jarTask = tasks.getByName<Jar>("jar") {
    archiveClassifier.set("fat")

    this.finalizedBy("reobfJar")
}

val minifyJarTask = tasks.register<MinifyJsonTask>("minJar") {
    input.set(jarTask.outputs.files.singleFile)
    archiveClassifier.set("")

    manifest.attributes(mapOf(
            "Automatic-Module-Name" to "ninjaphenix.expandedstorage",
            "MixinConfigs" to "expandedstorage.mixins.json"
    ))

    from(rootDir.resolve("LICENSE"))
    dependsOn(jarTask)
}

tasks.getByName("build") {
    dependsOn(minifyJarTask)
}
