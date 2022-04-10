import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("com.github.johnrengelman.shadow")
    id("org.quiltmc.loom")
    id("ninjaphenix.gradle.mod").apply(false)
}

configurations {
    create("common")
    create("shadowCommon") // Don't use shadow from the shadow plugin because we don't want IDEA to index this.
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    named("developmentFabric").get().extendsFrom(configurations["common"])
}

loom {
    accessWidenerPath.set(file("src/main/resources/expandedstorage.accessWidener"))
}

repositories {
    maven {
        name = "Quilt Release Maven"
        url = uri("https://maven.quiltmc.org/repository/release/")
    }
    maven {
        name = "Quilt Snapshot Maven"
        url = uri("https://maven.quiltmc.org/repository/snapshot/")
    }
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

mod {
    fabricApiModules(
            "fabric-api-base",
            "fabric-data-generation-api-v1",
            "fabric-rendering-v1",
            "fabric-textures-v0",
            "fabric-transfer-api-v1",
            // Mod Menu
            //"fabric-screen-api-v1",
            // NCL
            "fabric-screen-handler-api-v1",
            "fabric-key-binding-api-v1",
    )
    qslModules(
            "block/block_extensions",
            "core/networking",
            "core/resource_loader",
            "item/item_group",
            "gui/tooltip",
    )
    //qslModules("all")
}

dependencies {
    "common"(project(path = ":common", configuration = "namedElements")) {
        isTransitive = false
    }
    "shadowCommon"(project(path = ":common", configuration = "transformProductionFabric")) {
        isTransitive = false
    }

    modImplementation("ninjaphenix:container_library:1.3.4+1.18.2-fabric", dependencyConfiguration = excludeFabric)

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
}

val shadowJar = tasks.getByName<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar")

shadowJar.apply {
    exclude("architectury.common.json")
    configurations = listOf(project.configurations["shadowCommon"])
    archiveClassifier.set("dev-shadow")
}

tasks.getByName<RemapJarTask>("remapJar") {
    injectAccessWidener.set(true)
    inputFile.set(shadowJar.archiveFile)
    dependsOn(shadowJar)
    archiveClassifier.set("fat")
}

tasks.jar {
    archiveClassifier.set("dev")
}

val minifyJarTask = tasks.register<ninjaphenix.gradle.mod.api.task.MinifyJsonTask>("minJar") {
    input.set(tasks.getByName("remapJar").outputs.files.singleFile)
    archiveClassifier.set(project.name)
    from(rootDir.resolve("LICENSE"))
    dependsOn(tasks.getByName("remapJar"))
}

tasks.build {
    dependsOn(minifyJarTask)
}

(components.findByName("java") as AdhocComponentWithVariants).withVariantsFromConfiguration(project.configurations.getByName("shadowRuntimeElements")) {
    skip()
}
