dependencies {
    modImplementation("ellemes:${properties["container_library_artifact"]}-fabric:${properties["container_library_version"]}") {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
    }

    //modImplementation("maven.modrinth:ninjaphenix-container-library:1.3.0+1.18-fabric") {
    //    exclude("net.fabricmc")
    //    exclude("net.fabricmc.fabric-api")
    //}
}

