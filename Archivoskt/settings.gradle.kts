pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Archivoskt"

val exerciseModules = mapOf(
    ":ud2-ejercicio1" to "ejercicios/Ud2/Ejercicio1/app",
    ":ud3-ejercicio1" to "ejercicios/Ud3/Ejercicio1/app",
    ":ud3-ejercicio2" to "ejercicios/Ud3/Ejercicio2/app",
    ":ud3-ejercicio3" to "ejercicios/Ud3/Ejercicio3/app",
    ":ud4-ejercicio1" to "ejercicios/Ud4/Ejercicio1/app",
    ":ud4-ejercicio2" to "ejercicios/Ud4/Ejercicio2/app",
    ":ud4-ejercicio3" to "ejercicios/Ud4/Ejercicio3/app",
    ":ud4-ejercicio4" to "ejercicios/Ud4/Ejercicio4/app",
    ":ud4-ejercicio5" to "ejercicios/Ud4/Ejercicio5/app",
    ":ud4-ejercicio6" to "ejercicios/Ud4/Ejercicio6/app",
    ":examen-personas" to "ejercicios/ProyectosIndependientes/examen-personas/app",
    ":sorteos" to "ejercicios/ProyectosIndependientes/sorteos/app"
)

exerciseModules.forEach { (modulePath, moduleDirectory) ->
    include(modulePath)
    project(modulePath).projectDir = file(moduleDirectory)
}
