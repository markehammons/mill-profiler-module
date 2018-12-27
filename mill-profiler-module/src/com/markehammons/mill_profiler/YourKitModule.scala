package com.markehammons.mill_profiler

import mill._
import mill.define.Task
import mill.eval.Result
import mill.modules.Jvm
import mill.scalalib.JavaModule
import os.Path

trait YourKitModule extends OSModule { context: JavaModule =>
  def yourKitHome: Task[Path] = T {
    try Result.Success(os.Path(T.ctx.env("YOURKIT_HOME")))
    catch {
      case e: Exception =>
        Result.Failure(
          s"Could not determine YourKit's location from the environment: ${e.getMessage}." +
            s" Try setting YOURKIT_HOME or manually setting the path to YourKit in your build.sc")
    }
  }

  def yourKitAgentLocation = T {
    yourKitHome() / "bin" / yourKitArchDirectory() / yourKitAgentLibrary()
  }

  private def yourKitAgentLibrary = T {
    operatingSystem() match {
      case "linux"   => "libyjpagent.so"
      case "mac"     => "libyjpagent.jnilib"
      case "windows" => "yjpagent.dll"
    }
  }

  private def yourKitArchDirectory = T {
    operatingSystem() match {
      case "windows" => s"win${dataModel()}"
      case "mac"     => "mac"
      case "linux"   => s"linux-x86-${dataModel()}"
    }
  }

  def disableJavaVersionCheck = T {
    false
  }

  def yourKitAgentArguments = T {
    if (disableJavaVersionCheck()) {
      Agg("_no_java_version_check")
    } else {
      Agg.empty[String]
    }
  }

  private def yourKitAgentJVMArgument = T {
    val args = yourKitAgentArguments().mkString(",")
    s"-agentpath:${yourKitAgentLocation()}" + (if (args.isEmpty) {
                                                 ""
                                               } else {
                                                 s"=$args"
                                               })
  }

  def ykProfile(args: String*) = T.command {
    try Result.Success(
      Jvm.callSubprocess(
        finalMainClass(),
        runClasspath().map(_.path),
        forkArgs() :+ yourKitAgentJVMArgument(),
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
