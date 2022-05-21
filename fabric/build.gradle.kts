import com.modrinth.minotaur.dependencies.DependencyType
import com.modrinth.minotaur.dependencies.ModDependency
import me.hypherionmc.cursegradle.CurseProject
import me.hypherionmc.cursegradle.Options
import org.gradle.configurationcache.extensions.capitalized

plugins {
    id("ellemes.gradle.mod").apply(false)
}

repositories {
    maven {
        name = "Ladysnake maven"
        url = uri("https://ladysnake.jfrog.io/artifactory/mods")
        content {
            includeGroup("io.github.onyxstudios.Cardinal-Components-API")
        }
    }
    maven {
        name = "Devan maven"
        url = uri("https://raw.githubusercontent.com/Devan-Kerman/Devan-Repo/master/")
        content {
            includeGroup("net.devtech")
        }
    }

    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
}

val excludeFabric: (ModuleDependency) -> Unit = {
    it.exclude("net.fabricmc")
    it.exclude("net.fabricmc.fabric-api")
}

mod {
    fabricApi(
            "fabric-api-base",
            "fabric-data-generation-api-v1",
            "fabric-blockrenderlayer-v1",
            "fabric-item-groups-v0",
            "fabric-rendering-v1",
            "fabric-textures-v0",
            "fabric-lifecycle-events-v1",
            "fabric-transfer-api-v1"
    )
}

dependencies {
    modImplementation("ellemes:${properties["container_library_artifact"]}-fabric:${properties["container_library_version"]}")
    //modImplementation(group = "maven.modrinth", name = "ninjaphenix-container-library", version = "1.3.0+1.18-fabric", dependencyConfiguration = excludeFabric)
    //modImplementation(group = "ninjaphenix", name = "container_library", version = "1.3.0+1.18", classifier = "fabric", dependencyConfiguration = excludeFabric)

    // For chest module
    modCompileOnly(group = "curse.maven", name = "statement-340604", version = "3423826", dependencyConfiguration = excludeFabric)
    modCompileOnly(group = "curse.maven", name = "towelette-309338", version = "3398761", dependencyConfiguration = excludeFabric)
    modCompileOnly(group = "curse.maven", name = "carrier-409184", version = "3504375", dependencyConfiguration = excludeFabric)
    modCompileOnly(group = "io.github.onyxstudios.Cardinal-Components-API", name = "cardinal-components-base", version = "${properties["cardinal_version"]}", dependencyConfiguration = excludeFabric)
    modCompileOnly(group = "io.github.onyxstudios.Cardinal-Components-API", name = "cardinal-components-entity", version = "${properties["cardinal_version"]}", dependencyConfiguration = excludeFabric)
    //modRuntimeOnly(group = "net.devtech", name = "arrp", version = "0.4.2")

    //modRuntimeOnly("me.lucko:fabric-permissions-api:0.1-SNAPSHOT")
    modCompileOnly(group = "curse.maven", name = "htm-462534", version = "3539120", dependencyConfiguration = excludeFabric)

    //modRuntimeOnly("maven.modrinth:modmenu:3.1.0")
    //modRuntimeOnly(fabricApi.module("fabric-screen-api-v1", properties["fabric_api_version"] as String))
}

//
//configurations {
//    create("dev")
//}
//
//tasks.jar {
//    archiveClassifier.set("dev")
//}
//
//artifacts {
//    add("dev", tasks.jar.get().archiveFile) {
//        builtBy(tasks.jar)
//    }
//}

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
    options(closureOf<Options> {
        debug = modUploadDebug
        javaVersionAutoDetect = false
        javaIntegration = false
        forgeGradleIntegration = false
        fabricIntegration = false
        detectFabricApi = false
    })

    project(closureOf<CurseProject> {
        apiKey = System.getenv("CURSEFORGE_TOKEN")
        id = properties["curseforge_fabric_project_id"]
        releaseType = modReleaseType
        mainArtifact(tasks.getByName("minJar"), closureOf<me.hypherionmc.cursegradle.CurseArtifact> {
            displayName = project.name.capitalized() + " " + modVersion
            artifact = tasks.getByName("minJar")
        })
        relations(closureOf<me.hypherionmc.cursegradle.CurseRelation> {
            requiredDependency("fabric-api")
            requiredDependency("ellemes-container-library")
            optionalDependency("htm")
            optionalDependency("carrier")
            optionalDependency("towelette")
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
    dependencies.set(listOf(
        ModDependency("P7dR8mSH", DependencyType.REQUIRED), // fabric-api
        ModDependency("TIhTvPdy", DependencyType.REQUIRED), // ellemes-container-library
        ModDependency("IEPAK5x6", DependencyType.OPTIONAL), // htm
        //ModDependency("carrier", DependencyType.OPTIONAL), // carrier (not on modrinth)
        ModDependency("bnesqDoc", DependencyType.OPTIONAL) // towelette
    ))
    changelog.set(modChangelog)
    gameVersions.set(modTargetVersions)
    loaders.set(listOf(project.name))
}

afterEvaluate {
    releaseModTask.dependsOn(tasks.getByName("curseforge" + properties["curseforge_fabric_project_id"]))
    releaseModTask.dependsOn(tasks.getByName("modrinth"))
}
