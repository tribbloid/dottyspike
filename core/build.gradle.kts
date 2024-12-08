val vs = versions()

dependencies {

    api(project(":six:typetag"))
    testFixturesApi(testFixtures(project(":six:typetag")))
    
    // https://mvnrepository.com/artifact/com.lihaoyi/sourcecode
    api("com.lihaoyi:sourcecode_3:0.4.3-M1")

}

