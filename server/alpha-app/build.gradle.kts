dependencies {
    compileOnly(genesis("script-dependencies"))
    genesisGeneratedCode(withTestDependency = true)

    testImplementation("global.genesis:genesis-testsupport")
    testImplementation("global.genesis:genesis-dbtest")
}

description = "alpha-app"

sourceSets {
    main {
        resources {
            srcDirs("src/main/resources", "src/main/genesis", "src/main/charlie")
        }
    }
}

tasks {
    copyDependencies {
        enabled = false
    }
}
