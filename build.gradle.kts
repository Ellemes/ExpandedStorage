plugins {
    java
}

subprojects {
    apply(plugin = "java")

    group = properties["maven_group"] as String
    version = properties["mod_version"] as String
    base.archivesName.set(properties["archives_base_name"] as String)
    buildDir = rootDir.resolve("build/${project.name}")

    java {
        sourceCompatibility = JavaVersion.toVersion(properties["mod_java_version"] as String)
        targetCompatibility = JavaVersion.toVersion(properties["mod_java_version"] as String)
    }

    repositories {
        flatDir { // Cannot use exclusive content as forge does not change the artifact group like fabric does.
            name = "Local Dependencies"
            dir(rootDir.resolve("local_dependencies"))
        }

    }

    sourceSets {
        main {
            java {
                setSrcDirs(listOf(
                        "src/barrel/java",
                        "src/base/java",
                        "src/chest/java",
                        "src/old_chest/java",
                        rootDir.resolve("common/${project.name}Src/barrel/java"),
                        rootDir.resolve("common/${project.name}Src/base/java"),
                        rootDir.resolve("common/${project.name}Src/chest/java"),
                        rootDir.resolve("common/${project.name}Src/old_chest/java")
                ))
            }
            resources {
                setSrcDirs(listOf(
                        "src/barrel/resources",
                        "src/base/resources",
                        "src/chest/resources",
                        "src/old_chest/resources",
                        "src/common/resources",
                        rootDir.resolve("common/${project.name}Src/barrel/resources"),
                        rootDir.resolve("common/${project.name}Src/base/resources"),
                        rootDir.resolve("common/${project.name}Src/chest/resources"),
                        rootDir.resolve("common/${project.name}Src/old_chest/resources"),
                        rootDir.resolve("common/${project.name}Src/common/resources"),
                        rootDir.resolve("common/${project.name}Src/chest_compat/resources")
                ))
            }
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
}

tasks.register("buildMod") {
    subprojects.forEach {
        dependsOn(it.tasks["build"])
    }
}
