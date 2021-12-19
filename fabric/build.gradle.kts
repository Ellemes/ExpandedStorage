plugins {
    id("dev.architectury.loom")
}

loom {
    silentMojangMappingsLicense()
    runs {
        named("client") {
            vmArgs("-XX:+IgnoreUnrecognizedVMOptions")
            ideConfigGenerated(false)
        }
        named("server") {
            vmArgs("-XX:+IgnoreUnrecognizedVMOptions")
            ideConfigGenerated(false)
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${properties["minecraft_version"]}")
    mappings(loom.officialMojangMappings())
}

repositories {
    maven {
        name = "Shedaniel"
        url = uri("https://maven.shedaniel.me/")
    }
    exclusiveContent {
        forRepository {
            maven {
                name = "TerraformersMC"
                url = uri("https://maven.terraformersmc.com/")
            }
        }
        filter {
            includeGroup("com.terraformersmc")
            includeGroup("io.github.prospector")
        }
    }
    maven {
        name = "JitPack"
        url = uri("https://jitpack.io/")
        content {
            includeGroup("com.github.Virtuoel")
        }
    }
    maven {
        name = "Siphalor's Maven"
        url = uri("https://maven.siphalor.de/")
    }
}

val excludeFabric: (ExternalModuleDependency) -> Unit = {
    it.exclude("net.fabricmc")
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${properties["fabric_loader_version"]}")
    modApi("net.fabricmc.fabric-api:fabric-api:${properties["fabric_api_version"]}")

    // For chest module
    modCompileOnly("com.github.Virtuoel:Statement:31a2c3f", excludeFabric)
    modCompileOnly("com.github.Virtuoel:Towelette:e5e39eb", excludeFabric)

    modImplementation(group = "curse.maven", name = "ninjaphenixs-container-library-530668", version = "3569522")

    modRuntimeOnly("me.shedaniel:RoughlyEnoughItems:${properties["rei_version"]}")
    //modRuntimeOnly("io.github.prospector:modmenu:${properties["modmenu_version"]}")
}

tasks.withType<ProcessResources>() {
    val props = mutableMapOf("version" to properties["mod_version"]) // Needs to be mutable
    inputs.properties(props)
    filesMatching("fabric.mod.json") {
        expand(props)
    }
}
