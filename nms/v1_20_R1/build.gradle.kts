dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.20")
    compileOnly("com.mojang:datafixerupper:6.0.8")
}
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (JavaVersion.current().isJava10Compatible) {
        options.release.set(17)
    }
}
