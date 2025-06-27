import java.io.ByteArrayOutputStream
import kotlin.toString

plugins {
    kotlin("jvm") version "2.1.0"
    id("maven-publish")
}

group = "no.iktdev.streamit.library"
version = "1.0"
val named = "streamit-library-db"

repositories {
    mavenCentral()
    mavenLocal()
}

val exposedVersion = "0.61.0"
dependencies {
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("mysql:mysql-connector-java:8.0.29")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("io.kotlintest:kotlintest-assertions:3.3.2")
}

tasks.test {
    useJUnitPlatform()
}



val reposiliteUrl = if (version.toString().endsWith("SNAPSHOT")) {
    "https://reposilite.iktdev.no/snapshots"
} else {
    "https://reposilite.iktdev.no/releases"
}

publishing {
    publications {
        create<MavenPublication>("reposilite") {
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set(named)
                version = project.version.toString()
                url.set(reposiliteUrl)
            }
            from(components["kotlin"])
        }
    }
    repositories {
        mavenLocal()
        maven {
            name = named
            url = uri(reposiliteUrl)
            credentials {
                username = System.getenv("reposiliteUsername")
                password = System.getenv("reposilitePassword")
            }
        }
    }
}

fun findLatestTag(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "describe", "--tags", "--abbrev=0")
        standardOutput = stdout
        isIgnoreExitValue = true
    }
    return stdout.toString().trim().removePrefix("v")
}

fun isSnapshotBuild(): Boolean {
    // Use environment variable or branch name to detect snapshot
    val ref = System.getenv("GITHUB_REF") ?: ""
    return ref.endsWith("/master") || ref.endsWith("/main")
}

fun getCommitsSinceTag(tag: String): Int {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "rev-list", "$tag..HEAD", "--count")
        standardOutput = stdout
        isIgnoreExitValue = true
    }
    return stdout.toString().trim().toIntOrNull() ?: 0
}

val latestTag = findLatestTag()
val versionString = if (isSnapshotBuild()) {
    val parts = latestTag.split(".")
    val patch = parts.lastOrNull()?.toIntOrNull()?.plus(1) ?: 1
    val base = if (parts.size >= 2) "${parts[0]}.${parts[1]}" else latestTag
    val buildNumber = getCommitsSinceTag("v$latestTag")
    "$base.$patch-SNAPSHOT-$buildNumber"
} else {
    latestTag
}

version = versionString