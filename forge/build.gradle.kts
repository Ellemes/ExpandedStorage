import ellemes.gradle.mod.api.task.MinifyJsonTask

plugins {
    id("ellemes.gradle.mod").apply(false)
}

loom {
    forge {
        convertAccessWideners.set(true)
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
        mixinConfig("expandedstorage-common.mixin.json")
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
    modImplementation("ellemes:${properties["container_library_artifact"]}-forge:${properties["container_library_version"]}")
    //implementation(fg.deobf("ninjaphenix:container_library:1.3.0+1.18:forge"))

    //implementation(fg.deobf("mezz.jei:jei-${properties["jei_minecraft_version"]}:${properties["jei_version"]}"))
}

tasks.getByName<MinifyJsonTask>("minJar") {
    manifest.attributes(mapOf(
            "Automatic-Module-Name" to "ellemes.expandedstorage",
            "MixinConfigs" to "expandedstorage-common.mixin.json"
    ))
}

val u = ellemes.gradle.mod.api.publishing.UploadProperties(project, "https://github.com/Ellemes/ExpandedStorage")

u.configureCurseForge {
    relations(closureOf<me.hypherionmc.cursegradle.CurseRelation> {
        requiredDependency("ellemes-container-library")
    })
}
