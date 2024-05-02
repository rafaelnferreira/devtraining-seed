
rootProject.name = "genesisproduct-alpha"

pluginManagement {
    val genesisVersion: String by settings

    plugins {
        id("global.genesis.settings") version genesisVersion
    }

    repositories {
        if (extra.properties["USE_MVN_LOCAL"] == "true") {
            mavenLocal()
        }
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
    projectType = APPLICATION

    dependencies {
        dependency("global.genesis:reporting:${extra.properties["reportingVersion"]}")
        dependency("global.genesis:auth:${extra.properties["authVersion"]}")
        dependency("global.genesis:genesis-notify:${extra.properties["notifyVersion"]}")
        dependency("global.genesis:file-server:${extra.properties["file-serverVersion"]}")

    }

    plugins {
        genesisDeploy.enabled = true
    }

}

include("alpha-app")
