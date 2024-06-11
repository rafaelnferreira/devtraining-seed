
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
        dependency("global.genesis:auth:${extra.properties["authVersion"]}")

        if (extra.properties["reportingVersion"] != null) {
            dependency("global.genesis:reporting:${extra.properties["reportingVersion"]}")
        }

        if (extra.properties["notifyVersion"] != null) {
            dependency("global.genesis:genesis-notify:${extra.properties["notifyVersion"]}")
        }

        if (extra.properties["file-serverVersion"] != null) {
            dependency("global.genesis:file-server:${extra.properties["file-serverVersion"]}")
        }
    }

    plugins {
        genesisDeploy.enabled = true
    }

}

include("alpha-app")
