dependencies {
    implementation("global.genesis:genesis-pal-consolidator")
    implementation(project(":alpha-messages"))
    api("global.genesis:genesis-db")
    compileOnly(project(":alpha-config"))
    compileOnly(project(path = ":alpha-dictionary-cache", configuration = "codeGen"))
    testImplementation("global.genesis:genesis-dbtest")
    testImplementation("global.genesis:genesis-testsupport")
    testImplementation(project(path = ":alpha-dictionary-cache", configuration = "codeGen"))
}

description = "alpha-consolidator"

tasks {
    copyDependencies {
	    enabled = false
    }
}