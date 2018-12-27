import mill._
import mill.scalalib._
import mill.scalalib.scalafmt.ScalafmtModule
import publish._

trait MillProfilerPublishModule extends PublishModule {
  def publishVersion = T {"0.0.1-SNAPSHOT"}

  def pomSettings = PomSettings(
    description = artifactName(),
    organization = "com.markehammons",
    url = "https://github.com/markehammons/mill-profiler-module",
    licenses = Seq(
      License("LGPL-3.0", "https://opensource.org/licenses/lgpl-3.0.html")
    ),
    scm = SCM(
      "git://github.com/markehammons/mill-profiler-module.git",
      "scm:https://github.com/markehammons/mill-profiler-module.git"
    ),
    developers = Seq(
      Developer("markehammons", "Mark Hammons", "https://github.com/markehammons")
    )
  )
}

object `mill-profiler-module` extends ScalaModule with MillProfilerPublishModule with ScalafmtModule {
  def scalaVersion = "2.12.8"

  val millVersion = "0.3.5"

  override def compileIvyDeps = Agg(
    ivy"com.lihaoyi::mill-scalalib:$millVersion"
  )
}
