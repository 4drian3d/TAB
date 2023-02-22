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

repositories {
    maven("https://repo.kryptonmc.org/releases")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://repo.viaversion.com/")
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
}

tasks.compileJava {
    options.release.set(17)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))
