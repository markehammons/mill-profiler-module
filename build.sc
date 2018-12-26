import mill._
import mill.scalalib._
import publish._
import ammonite.ops._
import mill.modules.Jvm.createJar

trait MillProfilerPublishModule extends PublishModule{
  def artifactName = T {"mill-profiler"}
  def publishVersion = "0.0.1"

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

object `mill-profiler` extends ScalaModule with MillProfilerPublishModule {
  def scalaVersion = T {"2.12.8"}

  val millVersion = "0.3.5"

  def compileIvyDeps = Agg(
    ivy"com.lihaoyi::mill-scalalib:$millVersion"
  )
}
