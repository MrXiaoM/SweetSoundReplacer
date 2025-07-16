dependencies {
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.17.1")
    compileOnly("com.mojang:datafixerupper:4.0.26")
}
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (JavaVersion.current().isJava10Compatible) {
        options.release.set(17)
    }
}
