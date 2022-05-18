import ellemes.gradle.mod.api.task.MinifyJsonTask

plugins {
    id("ellemes.gradle.mod").apply(false)
}

loom {
    forge {
        convertAccessWideners.set(true)
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
        mixinConfig("expandedstorage-common.mixins.json")
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
}

dependencies {
    modImplementation("ellemes:container_library:1.3.4+1.18.2-forge")
    //implementation(fg.deobf("ninjaphenix:container_library:1.3.0+1.18:forge"))

    //implementation(fg.deobf("mezz.jei:jei-${properties["jei_minecraft_version"]}:${properties["jei_version"]}"))
}

tasks.getByName<MinifyJsonTask>("minJar") {
    manifest.attributes(mapOf(
            "Automatic-Module-Name" to "ellemes.expandedstorage",
            "MixinConfigs" to "expandedstorage.mixins.json"
    ))
}
