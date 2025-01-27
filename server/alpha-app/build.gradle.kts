dependencies {
    compileOnly(genesis("script-dependencies"))
    genesisGeneratedCode(withTestDependency = true)

    testImplementation("global.genesis:genesis-testsupport")
    testImplementation("global.genesis:genesis-dbtest")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")

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
