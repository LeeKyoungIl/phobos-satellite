plugins {
    id("java")
}

group = "me.phoboslabs.phobos"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveBaseName.set("phobos-satellite")
    archiveVersion.set("1.0.0")
    archiveClassifier.set("")
    archiveExtension.set("jar")
    from(sourceSets.main.get().allSource)
}