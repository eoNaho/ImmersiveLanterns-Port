import dev.kikugie.stonecutter.StonecutterBuild
import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.kotlin.dsl.getByType
import txnitemplate.ModData

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class TxniTemplateBuild internal constructor(val project: Project)  {
    lateinit var loader : String
    lateinit var sc : StonecutterBuild
    lateinit var mod : ModData

    fun setting(prop : String) : Boolean = project.properties[prop] == "true"
    fun property(prop : String) : Any? = project.properties[prop]

    fun init() {
        loader = project.extensions.getByType<LoomGradleExtensionAPI>().platform.get().name.lowercase()
        mod = ModData.from(this)

        project.run {
            version = "${mod.version}-${mod.mcVersion}"
            group = mod.group

            extensions.getByType<BasePluginExtension>().archivesName.set("${mod.id}-${mod.loader}")
        }

        project.dependencies.apply(dependencies())
    }

    private fun dependencies(): (DependencyHandler).() -> Unit = {
        add("minecraft", "com.mojang:minecraft:${mod.mcVersion}")

        if (mod.isFabric) {
            // JarJar Forge Config API
            if (setting("options.forgeconfig") && property("deps.forgeconfigapi") != "none")
                add("include", add("modApi", when (mod.mcVersion) {
                    "1.19.2" -> "net.minecraftforge:forgeconfigapiport-fabric:${property("deps.forgeconfigapi")}"
                    else -> "fuzs.forgeconfigapiport:forgeconfigapiport-fabric:${property("deps.forgeconfigapi")}"
                })!!)
        }
    }

}
