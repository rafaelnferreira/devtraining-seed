import global.genesis.gradle.plugin.simple.ProjectType

rootProject.name = "genesisproduct-alpha"

pluginManagement {
    val genesisVersion: String by settings

    plugins {
        id("global.genesis.settings") version genesisVersion
    }

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://genesisglobal.jfrog.io/genesisglobal/dev-repo")
            credentials {
                username = extra.properties["genesisArtifactoryUser"].toString()
                password = extra.properties["genesisArtifactoryPassword"].toString()
            }
        }
    }
}

plugins {
    id("global.genesis.settings")
}

genesis {
    projectType = ProjectType.APPLICATION

    dependencies {
        dependency("global.genesis:auth:${extra.properties["authVersion"]}")

    }

    plugins {
        genesisDeploy.enabled = true
    }

}

include("alpha-app")
