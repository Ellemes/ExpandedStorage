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
    mavenLocal()
}

val excludeFabric: (ModuleDependency) -> Unit = {
    it.exclude("net.fabricmc")
    it.exclude("net.fabricmc.fabric-api")
}

dependencies {
    val qsl = mod.qsl()
    modImplementation(qsl.module("block", "block_extensions"))
    modImplementation(qsl.module("core", "networking"))
    modImplementation(qsl.module("core", "registry"))
    modImplementation(qsl.module("core", "resource_loader"))
    modImplementation(qsl.module("item", "item_group"))
    modImplementation(qsl.module("gui", "tooltip"))

    listOf(
            "fabric-api-base",
            "fabric-data-generation-api-v1",
            "fabric-rendering-v1",
            "fabric-textures-v0",
            "fabric-transfer-api-v1",
    ).forEach {
        modImplementation(mod.fabricApi().module(it))
    }

    // Required by ECL
    listOf(
            "fabric-screen-handler-api-v1",
            "fabric-key-binding-api-v1",
            "fabric-transitive-access-wideners-v1",
    ).forEach {
        modRuntimeOnly(mod.fabricApi().module(it))
    }

    modImplementation("ellemes:${properties["container_library_artifact"]}-quilt:${properties["container_library_version"]}", dependencyConfiguration = excludeFabric)

    // For chest module
    modCompileOnly(group = "curse.maven", name = "statement-340604", version = "3423826", dependencyConfiguration = excludeFabric)
    modCompileOnly(group = "curse.maven", name = "towelette-309338", version = "3398761", dependencyConfiguration = excludeFabric)
    modCompileOnly(group = "curse.maven", name = "carrier-409184", version = "3504375", dependencyConfiguration = excludeFabric)
    modCompileOnly(group = "io.github.onyxstudios.Cardinal-Components-API", name = "cardinal-components-base", version = "${properties["cardinal_version"]}", dependencyConfiguration = excludeFabric)
    modCompileOnly(group = "io.github.onyxstudios.Cardinal-Components-API", name = "cardinal-components-entity", version = "${properties["cardinal_version"]}", dependencyConfiguration = excludeFabric)
    //modRuntimeOnly(group = "net.devtech", name = "arrp", version = "0.4.2")

    //modRuntimeOnly("me.lucko:fabric-permissions-api:0.1-SNAPSHOT")
    modCompileOnly(group = "curse.maven", name = "htm-462534", version = "3539120", dependencyConfiguration = excludeFabric)
}

val u = ellemes.gradle.mod.api.publishing.UploadProperties(project, "https://github.com/Ellemes/ExpandedStorage")

u.configureCurseForge {
    relations(closureOf<me.hypherionmc.cursegradle.CurseRelation> {
        requiredDependency("qsl")
        requiredDependency("ellemes-container-library")
        optionalDependency("htm")
        optionalDependency("carrier")
        optionalDependency("towelette")
    })
}

u.configureModrinth {
    dependencies {
        required.project("qsl") // qvIfYCYJ
        required.project("ellemes-container-library") // KV8SBboh
        optional.project("htm") // IEPAK5x6
        // optional.project("carrier") // carrier (not on Modrinth)
        optional.project("towelette") // bnesqDoc
    }
}
