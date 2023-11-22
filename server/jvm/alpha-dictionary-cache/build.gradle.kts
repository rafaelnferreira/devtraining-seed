
// Add your genesis config dependencies here
dependencies {
    implementation("global.genesis:auth-config:${properties["authVersion"]}")
    implementation("global.genesis:genesis-notify-config:${properties["notifyVersion"]}")
}

description = "alpha-dictionary-cache"
