plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("io.appium:java-client:11.0.0")
    testImplementation("org.seleniumhq.selenium:selenium-remote-driver:4.50.0")
    testImplementation("org.seleniumhq.selenium:selenium-support:4.50.0")
}




tasks.test {
    useJUnitPlatform()

    systemProperty(
        "alarma.hora",
        providers.gradleProperty("hora").orElse("06").get()
    )

    systemProperty(
        "alarma.minutos",
        providers.gradleProperty("minutos").orElse("15").get()
    )

    systemProperty(
        "alarma.periodo",
        providers.gradleProperty("periodo").orElse("AM").get()
    )


    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = true
    }

}
