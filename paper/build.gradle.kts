dependencies {
    implementation(projects.shared)
    implementation(libs.bstats.bukkit)
    compileOnly(libs.purpur)
    compileOnly(libs.placeholderapi)
    compileOnly(libs.vault) {
        exclude("org.bukkit", "bukkit")
    }
    compileOnly(libs.authlib)
}

tasks.compileJava {
    options.release.set(17)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))
