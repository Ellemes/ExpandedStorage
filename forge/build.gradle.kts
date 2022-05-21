import ellemes.gradle.mod.api.task.MinifyJsonTask
import org.gradle.configurationcache.extensions.capitalized

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

val releaseModTask = tasks.getByName("releaseMod")
val modVersion = properties["mod_version"] as String
val modReleaseType = if ("alpha" in modVersion) "alpha" else if ("beta" in modVersion) "beta" else "release"
val modChangelog = rootDir.resolve("changelog.md").readText(Charsets.UTF_8)
val modTargetVersions = mutableListOf(properties["minecraft_version"] as String)
val modUploadDebug = System.getProperty("MOD_UPLOAD_DEBUG", "false") == "true" // -DMOD_UPLOAD_DEBUG=true

(properties["extra_game_versions"] as String).split(",").forEach {
    if (it != "") {
        modTargetVersions.add(it)
    }
}

curseforge {
    options(closureOf<me.hypherionmc.cursegradle.Options> {
        debug = modUploadDebug
        javaVersionAutoDetect = false
        javaIntegration = false
        forgeGradleIntegration = false
        fabricIntegration = false
        detectFabricApi = false
    })

    project(closureOf<me.hypherionmc.cursegradle.CurseProject> {
        apiKey = System.getenv("CURSEFORGE_TOKEN")
        id = properties["curseforge_project_id"]
        releaseType = modReleaseType
        mainArtifact(tasks.getByName("minJar"), closureOf<me.hypherionmc.cursegradle.CurseArtifact> {
            displayName = project.name.capitalized() + " " + modVersion
            artifact = tasks.getByName("minJar")
        })
        relations(closureOf<me.hypherionmc.cursegradle.CurseRelation> {
            requiredDependency("ellemes-container-library")
        })
        changelogType = "markdown"
        changelog = modChangelog
        gameVersionStrings = listOf(project.name.capitalized(), "Java " + java.targetCompatibility.majorVersion) + modTargetVersions
    })
}

modrinth {
    debugMode.set(modUploadDebug)
    detectLoaders.set(false)

    projectId.set(properties["modrinth_project_id"] as String)
    versionType.set(modReleaseType)
    versionNumber.set(modVersion  + "+" + project.name)
    versionName.set(project.name.capitalized() + " " + modVersion)
    uploadFile.set(tasks.getByName("minJar"))
    dependencies {
        required.project("TIhTvPdy") // ellemes-container-library
    }
    changelog.set(modChangelog)
    gameVersions.set(modTargetVersions)
    loaders.set(listOf(project.name))
}

afterEvaluate {
    releaseModTask.dependsOn(tasks.getByName("curseforge" + properties["curseforge_project_id"]))
    releaseModTask.dependsOn(tasks.getByName("modrinth"))
}
