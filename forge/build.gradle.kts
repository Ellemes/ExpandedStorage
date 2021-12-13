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
        // JEI maven
        name = "Progwml6 maven"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    maven {
        // JEI maven - fallback
        name = "ModMaven"
        url = uri("https://modmaven.k-4u.nl")
    }
}

dependencies {
    forge("net.minecraftforge:forge:${properties["minecraft_version"]}-${properties["forge_version"]}")

    modImplementation(group = "curse.maven", name = "ninjaphenixs-container-library-530668", version = "3483412")
    modRuntime("mezz.jei:jei-${properties["minecraft_version"]}:${properties["jei_version"]}")
}

tasks.withType<ProcessResources>() {
    val props = mutableMapOf("version" to properties["mod_version"]) // Needs to be mutable
    inputs.properties(props)
    filesMatching("META-INF/mods.toml") {
        expand(props)
    }
}
