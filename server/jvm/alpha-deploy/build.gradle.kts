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
    genesisServer(project(":alpha-distribution", "distribution"))
    genesisServer(project(":alpha-site-specific", "distribution"))
    genesisWeb("client:web")
    /* --- */

    /* dependencies required for building the docker image from local docker compose */
    implementation(
        group = "global.genesis",
        name = "genesis-distribution",
        version = properties["genesisVersion"].toString(),
        classifier = "bin",
        ext = "zip"
    )
    implementation(
        group = "global.genesis",
        name = "auth-distribution",
        version = properties["authVersion"].toString(),
        classifier = "bin",
        ext = "zip"
    )
    /* --- */

    /* Add additional dependencies on other external distributions here */
}