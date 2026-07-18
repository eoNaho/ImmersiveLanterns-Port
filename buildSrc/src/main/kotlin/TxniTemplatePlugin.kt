import org.gradle.api.Plugin
import org.gradle.api.Project


class TxniTemplatePlugin : Plugin<Project> {


    override fun apply(target: Project) {
        // Apply Loom from a convention plugin so the central Kotlin DSL script
        // retains its generated Loom accessors while each Stonecutter project
        // can select the appropriate implementation.
        target.pluginManager.apply(
            if (target.name == "26.1.2-fabric") {
                "dev.architectury.loom-no-remap"
            } else {
                "dev.architectury.loom"
            }
        )

		if (target.name != "26.1.2-fabric") {
			target.pluginManager.apply("dev.kikugie.j52j")
		}

        target.extensions.create("txnitemplate", TxniTemplateBuild::class.java, target)
    }
}
