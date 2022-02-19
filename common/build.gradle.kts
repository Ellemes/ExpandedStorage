plugins {
    id("fabric-loom")
}

val excludeFabric: (ModuleDependency) -> Unit = {
    it.exclude("net.fabricmc")
    it.exclude("net.fabricmc.fabric-api")
}

repositories {
    mavenLocal()
}

dependencies {
    modImplementation(group = "maven.modrinth", name = "ninjaphenix-container-library", version = "1.3.0+1.18-fabric", dependencyConfiguration = excludeFabric)
    //modCompileOnly(group = "ninjaphenix", name = "container_library", version = "1.3.0+1.18", classifier = "fabric", dependencyConfiguration = excludeFabric)
}
