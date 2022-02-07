pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.toString() == "net.minecraftforge.gradle") {
                useModule("net.minecraftforge.gradle:ForgeGradle:${requested.version}")
            } else if (requested.id.toString() == "org.spongepowered.mixin") {
                useModule("org.spongepowered:mixingradle:${requested.version}")
            } else if (requested.id.toString() == "ninjaphenix.gradle-utils") {
                useModule("com.gitlab.NinjaPhenix.gradle-utils:gradle-utils:${requested.version}")
            }
        }
    }
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "SpongePowered"
            url = uri("https://repo.spongepowered.org/repository/maven-public/")
        }
        maven {
            name = "MinecraftForge"
            url = uri("https://maven.minecraftforge.net/")
        }
        exclusiveContent {
            forRepository {
                maven {
                    url = uri("https://jitpack.io")
                    name = "JitPack"
                }
            }
            filter {
                includeGroup("com.gitlab.NinjaPhenix")
                includeGroup("com.gitlab.NinjaPhenix.gradle-utils")
            }
        }
        gradlePluginPortal()
        mavenLocal()
    }
}

rootProject.name = "expandedstorage"

include("common")
include("fabric")
include("forge")
