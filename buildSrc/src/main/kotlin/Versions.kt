import org.gradle.api.Project

class Versions(self: Project) {

    val projectVMajor = "0.1.0"
    val projectV = projectVMajor + "-SNAPSHOT"

    val scalaV: String = self.properties.get("scalaVersion").toString()

    protected val scalaVParts = scalaV.split('.')

    val scalaBinaryV: String = scalaVParts.subList(0, 2).joinToString(".")
    val scalaMinorV: String = scalaVParts[2]
}
