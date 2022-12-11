/*
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
 * @version 2.0
 */
plugins {
    `java-library`
    `java-test-fixtures`
    idea
    `maven-publish`
    id("me.champeau.jmh")
}

extra["moduleName"] = "io.jenetics.linealgebra"

dependencies {
    implementation(project(":colt"))

    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("org.apache.commons:commons-math3:3.6.1")
    testImplementation("nl.jqno.equalsverifier:equalsverifier:3.7.2")
    testImplementation("org.testng:testng:7.6.1")
    testImplementation("colt:colt:1.2.0")

    //testFixturesApi("colt:colt:1.2.0")
    testFixturesApi(project(":colt"))
    testFixturesApi("org.assertj:assertj-core:3.20.2")

    jmh(testFixtures(project(":linealgebra")))
}

tasks.test { dependsOn(tasks.compileJmhJava) }

jmh {
    includes.add(".*DenseDoubleMatrix2dPerf.*")
}
