plugins {
    id("global.genesis.deploy")
}

description = "alpha-deploy"

dependencies {
    api(project(":alpha-eventhandler"))
    api(project(":alpha-messages"))
    
    /* dependencies required for building the docker image from the Gradle task */
    genesisServer(
        group = "global.genesis",
        name = "genesis-distribution",
        version = properties["genesisVersion"].toString(),
        classifier = "bin",
        ext = "zip"
    )
    genesisServer(
        group = "global.genesis",
        name = "auth-distribution",
        version = properties["authVersion"].toString(),
        classifier = "bin",
        ext = "zip"
    )
    // causes 0.1.6 plugin to crash
    genesisServer(
        group = "global.genesis",
        name = "genesis-notify-distribution",
        version = properties["notifyVersion"].toString(),
        classifier = "bin",
        ext = "zip"
    )
    genesisServer(
        group = "global.genesis",
        name = "file-server-distribution",
        version = properties["fileServerVersion"].toString(),
        classifier = "bin",
        ext = "zip"
    )
    genesisServer(project(":alpha-distribution", "distribution"))
    genesisServer(project(":alpha-site-specific", "distribution"))
    genesisWeb("client:web")
    /* --- */

    /* Add additional dependencies on other external distributions here */
}
tasks {
    copyDependencies {
        from(configurations.getByName("genesisServer"))
    }
 }