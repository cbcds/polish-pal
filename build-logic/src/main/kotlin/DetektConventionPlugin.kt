@file:Suppress("NoUnusedImports")

import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register
import utils.lib

class DetektConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.tasks.register<Detekt>("detektAll") {
            description = "Runs Detekt on all submodules"
            parallel = true
            ignoreFailures = false
            buildUponDefaultConfig = true
            autoCorrect = true
            setSource(target.projectDir)
            include("**/*.kt", "**/*.kts")
            exclude("**/resources/**", "**/build/**")
            config.setFrom(project.file("detekt.yml"))

            reports {
                md.required = true
                html.required = false
                sarif.required = false
                txt.required = false
                xml.required = false
            }
        }

        target.dependencies {
            "detektPlugins"(target.lib("detekt-formatting"))
        }
    }
}
