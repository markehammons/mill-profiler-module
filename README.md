# mill-profiler-module
A mill module for launching applications with java profilers like yourkit or visualvm

Import ```$ivy.`com.markehammons::mill-profiler-module:0.0.1-SNAPSHOT``` in your build.sc
 and extend either `VisualVMModule` and/or `YourKitModule`. You need to also set an environment variable
 like `VISUAL_VM_HOME` or `YOURKIT_HOME`. Finally, after that setup you can run your profiler.
 
## YourKitModule

### Settings:
* `yourKitHome`: You can set this to the path of your YourKit installation, or use the default behavior where the 
environment is checked for the `YOURKIT_HOME` variable.
* `yourKitAgentLocation`: You can override this to point at the YourKit agent library you want to use for profiling. Its default uses yourKitHome to choose the appropriate library to load.
* `disableJavaVersionCheck`: Set this to true to tell YourKit to not check your java version. This is helpful when running with unsupported VMs.

### Commands:
* `ykProfile <args>`: Runs the application with the yourkit agent attached, ready to be profiled by your yourkit application. `args` are passed to your application's main.

## VisualVMModule

This is meant to be used with VisualVM's Startup Profiler plugin. Go to Applications > Profile Startup and configure
the profiling run the way you want it to go, and finally click to start profiling. Once it's done, use the `vVMProfile` command as seen below

### Settings:
* `visualVMHome`: You can set this to the path of your VisualVM installation, or use the default behavior where
the environment is checked for the `VISUAL_VM_HOME` variable.
* `visualVMAgentLocation`: You can override this to point at the VisualVM agent library you want to use for profiling.
 Its default uses `visualVMHome` to choose the appropriate library to load.

### Commands:
* `vVMProfile <port> <args>`: Runs the application with the visualvm agent attached, ready for your visual vm client to attach to it.
