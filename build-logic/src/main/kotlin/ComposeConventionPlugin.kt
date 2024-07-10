import org.gradle.api.Plugin
import org.gradle.api.Project
import utils.pluginId

class ComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply {
            apply(target.pluginId("jetbrains-compose"))
            apply(target.pluginId("compose-compiler"))
        }
    }
}
