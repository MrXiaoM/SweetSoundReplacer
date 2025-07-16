allprojects {
    apply(plugin="java")
    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://jitpack.io")
        maven("https://repo.rosewooddev.io/repository/public/")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
    dependencies {
        add("compileOnly", "org.jetbrains:annotations:24.0.0")
        add("compileOnly", "com.github.dmulloy2:ProtocolLib:5.3.0")
        if (project.name.startsWith("v1_")) {
            add("compileOnly", project(":nms:shared"))
        }
    }
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        if (JavaVersion.current().isJava10Compatible) {
            options.release.set(8)
        }
    }
}
dependencies {
    add("compileOnly", "org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
    add("compileOnly", project(":nms:shared"))
}
