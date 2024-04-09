ext.set("localDaogenVersion", "ALPHA")

plugins {
    `maven-publish`
}

subprojects {
    apply(plugin = "org.gradle.maven-publish")

    tasks {
        withType<Copy> {
            duplicatesStrategy = DuplicatesStrategy.WARN
        }
        withType<Jar> {
            duplicatesStrategy = DuplicatesStrategy.WARN
        }
        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
                jvmTarget = "17"
            }
        }
        test {
            useJUnitPlatform()
            systemProperty("DbHost", "jdbc:h2:~/h2-test-db;DB_CLOSE_DELAY=-1;NON_KEYWORDS=VALUE,KEY")
            systemProperty("DbLayer", "SQL")
        }
    }
}

tasks {
    assemble {
        for (subproject in subprojects) {
            dependsOn(subproject.tasks.named("assemble"))
        }
    }
    build {
        for (subproject in subprojects) {
            dependsOn(subproject.tasks.named("build"))
        }
    }
    clean {
        for (subproject in subprojects) {
            dependsOn(subproject.tasks.named("clean"))
        }
    }
    withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.WARN
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.WARN
    }

    this.dependencies {
        for (subproject in subprojects) {
            dependsOn(subproject.tasks.named("dependencies"))
        }
    }
}

allprojects {

    group = "global.genesis"
    
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    repositories {
        if (properties["USE_MVN_LOCAL"] == "true") {
            mavenLocal()
        }
        mavenCentral()
        maven {
            val repoUrl = if(properties["clientSpecific"] == "true") {
                "https://genesisglobal.jfrog.io/genesisglobal/libs-release-client"
            } else {
                "https://genesisglobal.jfrog.io/genesisglobal/dev-repo"
            }
            url = uri(repoUrl)
            credentials {
                username = properties["genesisArtifactoryUser"].toString()
                password = properties["genesisArtifactoryPassword"].toString()
            }
        }
    }

    publishing {
        publications.create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
