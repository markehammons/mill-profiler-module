package com.markehammons.mill_profiler

import mill.T
import mill.eval.Result

trait OSModule extends mill.Module {
  def operatingSystem = T {
    System.getProperty("os.name") match {
      case name if name.toLowerCase().contains("linux") =>
        Result.Success("linux")
      case name if name.toLowerCase().contains("mac") => Result.Success("mac")
      case name if name.toLowerCase().contains("windows") =>
        Result.Success("windows")
      case name => Result.Failure(s"Could not determine OS type: $name")
    }
  }

  def dataModel = T {
    System.getProperty("os.arch") match {
      case "amd64" => Result.Success("64")
      case "x86"   => Result.Success("32")
      case arch    => Result.Failure(s"Could not determine architecture: $arch")
    }
  }
}
