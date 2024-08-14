
import java.util.*

plugins {
	id("java")
	id("org.springframework.boot") version "2.7.8"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("application")
}

group = "unibo"
version = "3.0.1"

java {
	sourceCompatibility = JavaVersion.VERSION_11
}

repositories {
	mavenCentral()
	flatDir {
		dirs("./unibolibs")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	//Added for WebSocket
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	//JSON
	implementation("com.googlecode.json-simple:json-simple:1.1.1")
	//CUSTOM unibo
	implementation("unibo:unibo.basicomm23-1.0:1.0")


	/* COAP **************************************************************************************************************** */
	// https://mvnrepository.com/artifact/org.eclipse.californium/californium-core
	implementation("org.eclipse.californium:californium-core:3.5.0")
	// https://mvnrepository.com/artifact/org.eclipse.californium/californium-proxy2
	implementation("org.eclipse.californium:californium-proxy2:3.5.0")

}

val springProps = Properties()

properties["activeProfile"]?.let {
    println("Loading properties from application-$it.properties")
    springProps.load(file("src/main/resources/application-$it.properties").inputStream())
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    systemProperty("spring.profiles.active", properties["activeProfile"] ?: "dev")
}

tasks.register<Copy>("propcopy") {
    dependsOn("processResources")
    group = "help"
    description = "Copy properties file to resources"
    val activeProfile = properties["activeProfile"] ?: "dev"
    from("src/main/resources/application-$activeProfile.properties")
    into("src/main/resources/")
    rename("application-$activeProfile.properties", "application.properties")
}

tasks.test {
	useJUnitPlatform()
}
