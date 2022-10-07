rootProject.name = "genesisproduct-alpha"

pluginManagement {
    repositories {
        mavenLocal {
            // VERY IMPORTANT!!! EXCLUDE AGRONA AS IT IS A POM DEPENDENCY AND DOES NOT PLAY NICELY WITH MAVEN LOCAL!
            content {
                excludeGroup("org.agrona")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://genesisglobal.jfrog.io/genesisglobal/libs-release-client")
            credentials {
                username = extra.properties["genesisArtifactoryUser"].toString()
                password = extra.properties["genesisArtifactoryPassword"].toString()
            }
        }
    }
}



include("alpha-config")
include("alpha-messages")
include("alpha-eventhandler")
include("alpha-script-config")
include("alpha-distribution")
include("alpha-dictionary-cache")
include("alpha-dictionary-cache:genesis-generated-sysdef")
include("alpha-dictionary-cache:genesis-generated-fields")
include("alpha-dictionary-cache:genesis-generated-dao")
include("alpha-dictionary-cache:genesis-generated-hft")
include("alpha-dictionary-cache:genesis-generated-view")
include("alpha-deploy")
include("alpha-site-specific")
