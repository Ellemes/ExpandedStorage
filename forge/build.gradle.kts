import com.gitlab.ninjaphenix.gradle.api.task.MinifyJsonTask
import org.gradle.jvm.tasks.Jar

plugins {
    id("ninjaphenix.gradle-utils")
    id("net.minecraftforge.gradle")
    id("org.spongepowered.mixin")
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
    mavenLocal()
}

dependencies {
    //implementation(fg.deobf("curse.maven:ninjaphenixs-container-library-530668:3549171"))
    implementation(fg.deobf("ninjaphenix:container_library:1.3.0+1.18:forge"))

    //implementation(fg.deobf("mezz.jei:jei-${properties["jei_minecraft_version"]}:${properties["jei_version"]}"))
}

val jarTask = tasks.getByName<Jar>("jar") {
    archiveClassifier.set("fat")

    this.finalizedBy("reobfJar")
}

val minifyJarTask = tasks.register<MinifyJsonTask>("minJar") {
    input.set(jarTask.outputs.files.singleFile)
    archiveClassifier.set("")

    manifest.attributes(mapOf(
            "Automatic-Module-Name" to "ninjaphenix.expandedstorage",
            "MixinConfigs" to "expandedstorage.mixins.json"
    ))

    from(rootDir.resolve("LICENSE"))
    dependsOn(jarTask)
}

tasks.getByName("build") {
    dependsOn(minifyJarTask)
}
