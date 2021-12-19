
plugins {
	`java-library`
	id("me.champeau.jmh") version "0.6.6"
}

group = "io.jenetics"
version = "2.0.0-SNAPSHOT"

tasks.named<Wrapper>("wrapper") {
	gradleVersion = "7.3.2"
	distributionType = Wrapper.DistributionType.ALL
}

sourceCompatibility = 11
targetCompatibility = 11

repositories {
	mavenCentral()
}

dependencies {
}
