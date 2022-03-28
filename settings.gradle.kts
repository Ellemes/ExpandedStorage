pluginManagement {
    repositories {
        maven {
            name = "Fabric Maven"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Architectury Maven"
            url = uri("https://maven.architectury.dev/")
        }
        maven {
            name = "MinecraftForge Maven"
            url = uri("https://maven.minecraftforge.net/")
        }
        maven {
            name = "Quilt Snapshot Maven"
            url = uri("https://maven.quiltmc.org/repository/snapshot/")
        }
        gradlePluginPortal()
        mavenLocal()
    }
}

rootProject.name = "expandedstorage"

include("common")
include("fabric")
include("forge")
include("quilt")

