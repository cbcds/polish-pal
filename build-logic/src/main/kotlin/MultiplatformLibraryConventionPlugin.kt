import org.gradle.api.Plugin
import org.gradle.api.Project
import utils.addKotlinUtilsDependencyIfNeeded
import utils.configureKotlinAndroid
import utils.pluginId

class MultiplatformLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply {
            apply("com.cbcds.polishpal.android.lib")
            apply(target.pluginId("kotlin-multiplatform"))
        }

        target.configureKotlinAndroid()

        // For now, iOS builds are not supported
        // target.configureKotlinIos()

        target.addKotlinUtilsDependencyIfNeeded()
    }
}
