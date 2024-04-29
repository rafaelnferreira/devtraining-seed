
// Add your genesis config dependencies here
dependencies {
    implementation("global.genesis:auth-config:${properties["authVersion"]}")
    implementation("global.genesis:file-server-config:${properties["fileServerVersion"]}")
}

description = "alpha-dictionary-cache"
