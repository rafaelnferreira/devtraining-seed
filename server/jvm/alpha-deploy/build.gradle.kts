plugins {
    id("global.genesis.deploy")
}

description = "alpha-deploy"

dependencies {
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

    api(project(":alpha-eventhandler"))
    api(project(":alpha-messages"))
    // Add additional dependencies on other external distributions here
}