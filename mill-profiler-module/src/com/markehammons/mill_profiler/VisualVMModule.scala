package com.markehammons.mill_profiler

import mill.scalalib.JavaModule
import mill._
import mill.eval.Result
import mill.modules.Jvm

trait VisualVMModule extends OSModule { context: JavaModule =>

  def visualVMHome = T {
    try Result.Success(os.Path(T.ctx.env("VISUAL_VM_HOME")))
    catch {
      case e: Exception =>
        Result.Failure(
          s"Could not find home for visualVM: ${e.getMessage}; Make sure the VISUAL_VM_HOME" +
            " environmental variable is set or set visualVMHome manually in build.sc")
    }
  }

  def visualVMAgentLocation = T {
    visualVMHome() / "profiler" / "lib" / "deployed" / "jdk16" / visualVMArchDirectory() / visualVMAgentFile()
  }

  private def visualVMArchDirectory = T {
    operatingSystem() match {
      case "linux" if dataModel() == "64"   => Result.Success("linux-amd64")
      case "linux"                          => Result.Success("linux")
      case "mac"                            => Result.Success("mac")
      case "windows" if dataModel() == "64" => Result.Success("windows-amd64")
      case "windows"                        => Result.Success("windows")
      case _ =>
        Result.Failure(
          "Unrecognized operating system/architecture configuration")
    }
  }

  private def visualVMAgentFile = T {
    operatingSystem() match {
      case "linux"   => "libprofilerinterface.so"
      case "mac"     => "libprofilerinterface.jnilib"
      case "windows" => "profilerinterface.dll"
    }
  }

  private def visualVMAgentJVMArgument(port: Int) = T.command {
    s"-agentpath:${visualVMAgentLocation()}=${visualVMHome() / "profiler" / "lib"},$port"
  }

  def vVMProfile(port: Int, args: String*) = T.command {
    try Result.Success(
      Jvm.callSubprocess(
        finalMainClass(),
        runClasspath().map(_.path),
        forkArgs() ++ Seq(visualVMAgentJVMArgument(port)(), "-Xverify:none"),
        forkEnv(),
        args,
        workingDir = forkWorkingDir()
      ))
    catch {
      case e: Exception =>
        Result.Failure(s"Profiling failed: ${e.getMessage}")
    }
  }

}
