dependencies {
    modImplementation("ellemes:${properties["container_library_artifact"]}-fabric:${properties["container_library_version"]}") {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
    }
}

