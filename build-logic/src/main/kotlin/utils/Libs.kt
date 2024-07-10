package utils

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency

internal fun Project.lib(lib: String): Provider<MinimalExternalModuleDependency> {
    return libs().findLibrary(lib).get()
}

internal fun Project.plugin(plugin: String): Provider<PluginDependency> {
    return libs().findPlugin(plugin).get()
}

internal fun Project.pluginId(plugin: String): String {
    return libs().findPlugin(plugin).get().get().pluginId
}

internal fun Project.libVersion(lib: String): String {
    return libs().findVersion(lib).get().toString()
}

private fun Project.libs(): VersionCatalog {
    return extensions.getByType<VersionCatalogsExtension>().named("libs")
}
