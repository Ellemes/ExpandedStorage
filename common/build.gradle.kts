repositories {
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroup("maven.modrinth")
        }
    }
    mavenLocal()
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:${rootProject.properties["fabric_loader_version"]}")

    modImplementation("ninjaphenix:container_library:1.3.2+1.18.2:fabric") {
        exclude("net.fabricmc")
        exclude("net.fabricmc.fabric-api")
    }

    //modImplementation("maven.modrinth:ninjaphenix-container-library:1.3.0+1.18-fabric") {
    //    exclude("net.fabricmc")
    //    exclude("net.fabricmc.fabric-api")
    //}
}

architectury {
    common()
    injectInjectables = false
}

loom {
    accessWidenerPath.set(file("src/main/resources/expandedstorage.accessWidener"))
}
