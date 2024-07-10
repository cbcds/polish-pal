import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import utils.Java
import utils.configureKotlinAndroid
import utils.libVersion
import utils.pluginId

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.pluginManager.apply {
            apply(target.pluginId("android-library"))
            apply(target.pluginId("kotlin-multiplatform"))
        }

        target.configureKotlinAndroid()

        target.extensions.configure<LibraryExtension> {
            compileSdk = target.libVersion("android-compileSdk").toInt()

            sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
            sourceSets["main"].res.srcDirs("src/androidMain/res")
            sourceSets["main"].resources.srcDirs("src/commonMain/resources")

            defaultConfig {
                minSdk = target.libVersion("android-minSdk").toInt()
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
