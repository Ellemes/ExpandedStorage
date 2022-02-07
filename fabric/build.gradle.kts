import com.gitlab.ninjaphenix.gradle.api.task.MinifyJsonTask
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("fabric-loom")
    id("ninjaphenix.gradle-utils")
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
    //modImplementation(group = "curse.maven", name = "ninjaphenixs-container-library-530668", version = "3549144", dependencyConfiguration = excludeFabric)
    modImplementation(group = "ninjaphenix", name = "container_library", version = "1.3.0+1.18", classifier = "fabric", dependencyConfiguration = excludeFabric)

    // For chest module
    modCompileOnly(group = "curse.maven", name = "statement-340604", version = "3423826", dependencyConfiguration = excludeFabric)
    modCompileOnly(group = "curse.maven", name = "towelette-309338", version = "3398761", dependencyConfiguration = excludeFabric)
    modCompileOnly(group = "curse.maven", name = "carrier-409184", version = "3504375", dependencyConfiguration = excludeFabric)
    modCompileOnly(group = "io.github.onyxstudios.Cardinal-Components-API", name = "cardinal-components-base", version = properties["cardinal_version"] as String)
    modCompileOnly(group = "io.github.onyxstudios.Cardinal-Components-API", name = "cardinal-components-entity", version = properties["cardinal_version"] as String)
    //modRuntimeOnly(group = "net.devtech", name = "arrp", version = "0.4.2")

    //modRuntimeOnly("me.lucko:fabric-permissions-api:0.1-SNAPSHOT")
    modCompileOnly(group = "curse.maven", name = "htm-462534", version = "3539120")
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

val remapJarTask: RemapJarTask = tasks.getByName<RemapJarTask>("remapJar") {
    archiveClassifier.set("fat")
    dependsOn(tasks.jar)
}

val minifyJarTask = tasks.register<MinifyJsonTask>("minJar") {
    input.set(remapJarTask.outputs.files.singleFile)
    archiveClassifier.set("")
    from(rootDir.resolve("LICENSE"))
    dependsOn(remapJarTask)
}

tasks.getByName("build") {
    dependsOn(minifyJarTask)
}
