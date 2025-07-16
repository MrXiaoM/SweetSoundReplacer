rootProject.name = "SweetSoundReplacer"

include(":nms")
file("nms").listFiles()?.forEach {
    if (File(it, "build.gradle.kts").exists()) {
        include(":nms:${it.name}")
    }
}
