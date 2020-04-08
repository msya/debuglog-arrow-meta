# Debug Log Arrow Meta

This compiler plugin was inspired by [DebugLog](https://github.com/kevinmost/debuglog). It is an implementation of this plugin
using [Arrow Meta](https://github.com/arrow-kt/arrow-meta). Arrow Meta is a library that provides functional API to build
compiler plugins. I wrote an article that explains the plugin [Writing a Kotlin Compiler Plugin with Arrow Meta](https://medium.com/@heyitsmohit/writing-kotlin-compiler-plugin-with-arrow-meta-cf7b3689aa3e).

There are two modules in this project. 

## create-plugin

This module contains the actual plugin. `DebugLogMetaPlugin` is the entry point. `DebugLogTransformation` contains the acutal
logic of using the replace transformation of Arrow Meta. 


## use-plugin

This module uses the jar file build from the `create-plugin` module. It demonstrates the usage in the `DebugLogExample` file.
This module also contains a test for the plugin. It uses the `testing-plugin` module from Arrow Meta. 

