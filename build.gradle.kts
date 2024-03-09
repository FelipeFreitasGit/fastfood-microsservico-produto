import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.sonarqube") version "4.4.1.3373"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    kotlin("plugin.jpa") version "1.8.21"
    id("org.flywaydb.flyway") version "9.19.4"
}

group = "br.com.fiap.postech.produto"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
    maven {
        url = uri("https://repo.spring.io/snapshot")
    }
    maven {
        url = uri("https://repo.spring.io/milestone")
    }
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.postgresql:postgresql:42.2.1")

    implementation("org.flywaydb:flyway-core:9.19.4")

    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:4.4.1.3373")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testImplementation("org.assertj:assertj-core:3.25.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")

    testImplementation("io.cucumber:cucumber-java:6.10.4")
    testImplementation("org.junit.vintage:junit-vintage-engine:5.9.3")
    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("io.rest-assured:spring-mock-mvc:5.3.0")
    testImplementation("io.rest-assured:json-schema-validator:5.3.1")
    testImplementation("io.rest-assured:rest-assured:5.3.0") {
        exclude(group = "org.codehaus.groovy", module = "groovy")
        exclude(group = "org.codehaus.groovy", module = "groovy-xml")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.9"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

tasks.withType<JacocoCoverageVerification> {
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it).apply {
                exclude(
                    "**/config/**",
                    "**/exception/**",
                    "**/ControllerAdvice.*",
                    "**/FastFoodApplication.*"
                )
            }
        }))
    }
}

tasks.withType<JacocoReport> {
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.map {
            fileTree(it).apply {
                exclude(
                    "**/config/**",
                    "**/exception/**",
                    "**/ControllerAdvice.*",
                    "**/FastFoodApplication.*"
                )
            }
        }))
    }
}

val cucumberRuntime by configurations.creating {
    extendsFrom(configurations["testImplementation"])
}

task("cucumber") {
    dependsOn("assemble", "compileTestJava")
    doLast {
        javaexec {
            mainClass.set("io.cucumber.core.cli.Main")
            classpath = cucumberRuntime + sourceSets.main.get().output + sourceSets.test.get().output
            args = listOf(
                    "--plugin", "pretty",
                    "--plugin", "html:target/cucumber-report.html",
                    "--glue", "br.com.fiap.postech.fastfood.produto.bdd",
                    "src/test/resources"
            )
            environment("CUCUMBER_PUBLISH_QUIET", true)
        }
    }
}

apply(plugin = "org.sonarqube")

val coverageExclusions = listOf(
        "**/config/**",
        "**/exception/**",
        "**/ControllerAdvice.*",
        "**/FastFoodApplication.*"
)

sonar {
    properties {
        property("sonar.projectKey", "FelipeFreitasGit_fastfood-microsservico-produto")
        property("sonar.organization", "felipefreitasgit")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.coverage.jacoco.xmlReportPaths", layout.buildDirectory.dir("/reports/jacoco/test/*.xml"))
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.coverage.exclusions",coverageExclusions)
        property("sonar.exclusions", coverageExclusions)

    }
}

flyway {
    url = "jdbc:postgresql://localhost:5432/fast-food"
    user = "postgres"
    password = "Postgres2023!"
}
