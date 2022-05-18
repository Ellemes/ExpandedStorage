dependencies {
    modImplementation("ellemes:container_library:${properties["container_library_version"]}-fabric") {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
    }

    //modImplementation("maven.modrinth:ninjaphenix-container-library:1.3.0+1.18-fabric") {
    //    exclude("net.fabricmc")
    //    exclude("net.fabricmc.fabric-api")
    //}
}

