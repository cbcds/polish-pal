import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import utils.Java
import utils.configureKotlinAndroid
import utils.libVersion
import utils.pluginId

class AndroidAppConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply {
            apply(target.pluginId("android-application"))
            apply(target.pluginId("kotlin-multiplatform"))
        }

        target.configureKotlinAndroid()

        target.extensions.configure<ApplicationExtension> {
            compileSdk = target.libVersion("android-compileSdk").toInt()

            sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

            defaultConfig {
                minSdk = target.libVersion("android-minSdk").toInt()
                targetSdk = target.libVersion("android-targetSdk").toInt()
            }

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }

            compileOptions {
                sourceCompatibility = Java.sourceCompatibility
                targetCompatibility = Java.targetCompatibility
            }
        }
    }
}
