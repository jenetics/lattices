/*
 * Java Colt Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 2.0
 * @version 3.0
 */
plugins {
	base
	id("me.champeau.jmh") version "0.7.1" apply false
}

rootProject.version = Lattices.VERSION

tasks.named<Wrapper>("wrapper") {
	version = "8.3"
	distributionType = Wrapper.DistributionType.ALL
}

/**
 * Project configuration *before* the projects has been evaluated.
 */
allprojects {
	group =  Lattices.GROUP
	version = Lattices.VERSION

	repositories {
		flatDir {
			dirs("${rootDir}/buildSrc/lib")
		}
		mavenLocal()
		mavenCentral()
	}

    configurations.all {
        resolutionStrategy.preferProjectModules()
    }
}

/**
 * Project configuration *after* the projects has been evaluated.
 */
gradle.projectsEvaluated {
	subprojects {
		val project = this

        tasks.withType<Test> {
            useTestNG()
        }

		plugins.withType<JavaPlugin> {
			configure<JavaPluginExtension> {
                modularity.inferModulePath.set(true)
				sourceCompatibility = JavaVersion.VERSION_17
				targetCompatibility = JavaVersion.VERSION_17
			}

			setupJava(project)
			setupTestReporting(project)
			setupJavadoc(project)
		}

        tasks.withType<JavaCompile> {
            modularity.inferModulePath.set(true)
            options.compilerArgs.add("-Xlint:" + xlint())
        }

		if (plugins.hasPlugin("maven-publish")) {
            setupPublishing(project)
		}
	}

}

/**
 * Some common Java setup.
 */
fun setupJava(project: Project) {
	val attr = mutableMapOf(
		"Implementation-Title" to project.name,
		"Implementation-Version" to Lattices.VERSION,
		"Implementation-URL" to Lattices.URL,
		"Implementation-Vendor" to Lattices.NAME,
		"ProjectName" to Lattices.NAME,
		"Version" to Lattices.VERSION,
		"Maintainer" to Lattices.AUTHOR,
		"Project" to project.name,
		"Project-Version" to project.version,

		"Created-With" to "Gradle ${gradle.gradleVersion}",
		"Built-By" to Env.BUILD_BY,
		"Build-Date" to Env.BUILD_DATE,
		"Build-JDK" to Env.BUILD_JDK,
		"Build-OS-Name" to Env.BUILD_OS_NAME,
		"Build-OS-Arch" to Env.BUILD_OS_ARCH,
		"Build-OS-Version" to Env.BUILD_OS_VERSION
	)
	if (project.extra.has("moduleName")) {
		attr["Automatic-Module-Name"] = project.extra["moduleName"].toString()
	}

	project.tasks.withType<Jar> {
		manifest {
			attributes(attr)
		}
	}
}

/**
 * Setup of the Java test-environment and reporting.
 */
fun setupTestReporting(project: Project) {
	project.apply(plugin = "jacoco")

	project.configure<JacocoPluginExtension> {
		toolVersion = "0.8.10"
	}

	project.tasks {
		named<JacocoReport>("jacocoTestReport") {
			dependsOn("test")

			reports {
				html.required.set(true)
				xml.required.set(true)
				csv.required.set(true)
			}
		}

		named<Test>("test") {
			useTestNG()
			finalizedBy("jacocoTestReport")
		}
	}
}

/**
 * Setup of the projects Javadoc.
 */
fun setupJavadoc(project: Project) {
	project.tasks.withType<Javadoc> {
        modularity.inferModulePath.set(true)

		val doclet = options as StandardJavadocDocletOptions
		doclet.addBooleanOption("Xdoclint:accessibility,html,reference,syntax", true)

		exclude("**/internal/**")

		doclet.memberLevel = JavadocMemberLevel.PROTECTED
		doclet.version(true)
		doclet.docEncoding = "UTF-8"
		doclet.charSet = "UTF-8"
		doclet.linkSource(true)
		doclet.linksOffline(
			"https://docs.oracle.com/javase/17/docs/api",
			"${project.rootDir}/buildSrc/resources/javadoc/java.se"
		)
		doclet.windowTitle = "Lattices ${project.version}"
		doclet.docTitle = "<h1>Lattices ${project.version}</h1>"
		doclet.bottom = "&copy; ${Env.COPYRIGHT_YEAR} Franz Wilhelmst&ouml;tter  &nbsp;<i>(${Env.BUILD_DATE})</i>"
		doclet.stylesheetFile = project.file("${project.rootDir}/buildSrc/resources/javadoc/stylesheet.css")

		doclet.tags = listOf(
			"apiNote:a:API Note:",
			"implSpec:a:Implementation Requirements:",
			"implNote:a:Implementation Note:"
		)

        doLast {
            val dir = if (project.extra.has("moduleName")) {
                project.extra["moduleName"].toString()
            } else {
                ""
            }

            project.copy {
                from("src/main/java") {
                    include("io/**/doc-files/*.*")
                }
                includeEmptyDirs = false
                into(destinationDir!!.resolve(dir))
            }
        }
	}

	val javadoc = project.tasks.findByName("javadoc") as Javadoc?
	if (javadoc != null) {
		project.tasks.register<io.jenetics.gradle.ColorizerTask>("colorizer") {
			directory = javadoc.destinationDir!!
		}

		project.tasks.register("java2html") {
			doLast {
				project.javaexec {
					mainClass.set("de.java2html.Java2Html")
					args = listOf(
						"-srcdir", "src/main/java",
						"-targetdir", "${javadoc.destinationDir}/src-html/${project.extra["moduleName"]}"
					)
					classpath = files("${project.rootDir}/buildSrc/lib/java2html.jar")
				}
			}
		}

		javadoc.doLast {
			val colorizer = project.tasks.findByName("colorizer")
			colorizer?.actions?.forEach {
				it.execute(colorizer)
			}

			val java2html = project.tasks.findByName("java2html")
			java2html?.actions?.forEach {
				it.execute(java2html)
			}
		}
	}
}

/**
 * The Java compiler XLint flags.
 */
fun xlint(): String {
	// See https://docs.oracle.com/en/java/javase/17/docs/specs/man/javac.html#extra-options
	return listOf<String>(
		"auxiliaryclass",
		"cast",
		"classfile",
		"dep-ann",
		"deprecation",
		"divzero",
		"empty",
		"exports",
		"finally",
		"module",
		"opens",
		"overrides",
		"rawtypes",
		"removal",
		"serial",
		"static",
		"try",
		"unchecked",
		"varargs"
	).joinToString(separator = ",")
}

val identifier = "${Lattices.ID}-${Lattices.VERSION}"

/**
 * Setup of the Maven publishing.
 */
fun setupPublishing(project: Project) {
	project.configure<JavaPluginExtension> {
		withJavadocJar()
		withSourcesJar()
	}

	project.tasks.named<Jar>("sourcesJar") {
		filter(
			org.apache.tools.ant.filters.ReplaceTokens::class, "tokens" to mapOf(
				"__identifier__" to identifier,
				"__year__" to Env.COPYRIGHT_YEAR
			)
		)
	}

	project.tasks.named<Jar>("javadocJar") {
		filter(
			org.apache.tools.ant.filters.ReplaceTokens::class, "tokens" to mapOf(
				"__identifier__" to identifier,
				"__year__" to Env.COPYRIGHT_YEAR
			)
		)
	}

	project.configure<PublishingExtension> {
		publications {
			create<MavenPublication>("mavenJava") {
				artifactId = Lattices.ID
				from(project.components["java"])
				versionMapping {
					usage("java-api") {
						fromResolutionOf("runtimeClasspath")
					}
					usage("java-runtime") {
						fromResolutionResult()
					}
				}
				pom {
					name.set(Lattices.ID)
					description.set(project.description)
					url.set(Lattices.URL)
					inceptionYear.set("2022")

					licenses {
						license {
							name.set("The Apache License, Version 2.0")
							url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
							distribution.set("repo")
						}
					}
					developers {
						developer {
							id.set(Lattices.ID)
							name.set(Lattices.AUTHOR)
							email.set(Lattices.EMAIL)
						}
					}
					scm {
						connection.set(Maven.SCM_CONNECTION)
						developerConnection.set(Maven.DEVELOPER_CONNECTION)
						url.set(Maven.SCM_URL)
					}
				}
			}
		}
		repositories {
			maven {
				url = if (version.toString().endsWith("SNAPSHOT")) {
					uri(Maven.SNAPSHOT_URL)
				} else {
					uri(Maven.RELEASE_URL)
				}

				credentials {
					username = if (extra.properties["nexus_username"] != null) {
						extra.properties["nexus_username"] as String
					} else {
						"nexus_username"
					}
					password = if (extra.properties["nexus_password"] != null) {
						extra.properties["nexus_password"] as String
					} else {
						"nexus_password"
					}
				}
			}
		}
	}

	project.apply(plugin = "signing")

	project.configure<SigningExtension> {
		sign(project.the<PublishingExtension>().publications["mavenJava"])
	}

}


