val vs = versions()

dependencies {

    // https://mvnrepository.com/artifact/io.github.vincenzobaz/spark-scala3

    api("org.apache.spark:spark-core_2.13:3.5.4")
    api("org.apache.spark:spark-mllib_2.13:3.5.4")

//    implementation("io.github.vincenzobaz:spark-scala3_3:0.2.1")
    api(project(":six:spark"))
    testFixturesApi(testFixtures(project(":six:spark")))

}

