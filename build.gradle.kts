val settings = object : TxniTemplateSettings {

	// -------------------- Dependencies ---------------------- //
	override val depsHandler: DependencyHandler get() = object : DependencyHandler {
		override fun addGlobal(deps: DependencyHandlerScope) {
			if (mod.mcVersion != "26.1.2") {
				deps.add("modImplementation", "toni.txnilib:${mod.loader}-${mod.mcVersion}:1.0.20")
				deps.add("modImplementation", "toni.sodiumdynamiclights:${mod.loader}-${mod.mcVersion}:1.0.8") { isTransitive = false }
			}

			deps.runtimeOnly("org.anarres:jcpp:1.4.14") // required for iris
			deps.runtimeOnly("org.antlr:antlr4-runtime:4.13.1") // required for iris
			deps.runtimeOnly("io.github.douira:glsl-transformer:2.0.1") // required for iris
		}

		override fun addFabric(deps: DependencyHandlerScope) {
			if (mod.mcVersion == "26.1.2") {
				deps.implementation("eu.pb4:trinkets:${property("deps.trinkets")}")
				deps.implementation(modrinth("lambdynamiclights", "4.10.2+26.1.2"))
				deps.implementation("com.terraformersmc:modmenu:18.0.0")
			}
			else if (mod.mcVersion == "1.21.1")
			{
				deps.add("modImplementation", "io.wispforest:accessories-fabric:1.0.0-beta.35+1.21")
				deps.add("modImplementation", modrinth("iris", "1.8.0-beta.4+1.21-fabric"))
			}
			else {
				deps.add("modImplementation", "io.wispforest:accessories-fabric:1.0.0-beta.38+1.20.1")
				deps.add("modImplementation", modrinth("iris", "1.7.5+1.20.1"))
			}
		}

		override fun addForge(deps: DependencyHandlerScope) {
			deps.add("modImplementation", "io.wispforest:accessories-neoforge:1.0.0-beta.38+1.20.1") { isTransitive = false }
			deps.minecraftRuntimeLibraries("io.wispforest:endec:0.1.8")
			deps.minecraftRuntimeLibraries("io.wispforest.endec:gson:0.1.5")
			deps.minecraftRuntimeLibraries("io.wispforest.endec:netty:0.1.4")

			deps.add("modImplementation", modrinth("cloth-config", "11.1.136+forge"))
			deps.add("modRuntimeOnly", "dev.su5ed.sinytra.fabric-api:fabric-api:0.92.2+1.11.8+1.20.1")

			deps.compileOnly(deps.annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")!!)
			deps.include(deps.implementation("io.github.llamalad7:mixinextras-forge:0.3.5")!!)

			deps.add("modImplementation", modrinth("embeddium", "0.3.31+mc1.20.1"))
			deps.add("modImplementation", modrinth("oculus", "1.20.1-1.7.0"))
			deps.minecraftRuntimeLibraries("org.anarres:jcpp:1.4.14") { isTransitive = false } // required for iris

			deps.add("modCompileOnly", modrinth("curios", "5.11.0+1.20.1"))
		}

		override fun addNeo(deps: DependencyHandlerScope) {
			deps.add("modImplementation", "io.wispforest:accessories-neoforge:1.0.0-beta.35+1.21")
			deps.minecraftRuntimeLibraries("io.wispforest:endec:0.1.8")
			deps.minecraftRuntimeLibraries("io.wispforest.endec:gson:0.1.5")
			deps.minecraftRuntimeLibraries("io.wispforest.endec:netty:0.1.4")

			deps.add("modCompileOnly", modrinth("iris", "1.8.0-beta.4+1.21-neoforge"))

			deps.add("modCompileOnly", "curse.maven:adorned-1036809:5740650")
		}
	}


	// ---------- Curseforge/Modrinth Configuration ----------- //
	// For configuring the dependecies that will show up on your mod page.
	override val publishHandler: PublishDependencyHandler get() = object : PublishDependencyHandler {
		override fun addShared(deps: DependencyContainer) {
			if (mod.mcVersion == "26.1.2") {
				deps.requires("trinkets")
				deps.requires("lambdynamiclights")
			} else {
				deps.requires("txnilib")
				deps.requires("accessories")
			}

			if (mod.isFabric) {
				deps.requires("fabric-api")
			}
		}

		override fun addCurseForge(deps: DependencyContainer) {
			deps.requires("dynamiclights-reforged")
		}

		override fun addModrinth(deps: DependencyContainer) {
			deps.requires("sodium-dynamic-lights")
		}
	}
}


// ---------------TxniTemplate Build Script---------------- //
//   (only edit below this if you know what you're doing)
// -------------------------------------------------------- //

plugins {
	`maven-publish`
	txnitemplate
	application
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("dev.kikugie.j52j") version "1.0" apply false
	id("dev.architectury.loom") apply false
	id("me.modmuss50.mod-publish-plugin")
	id("systems.manifold.manifold-gradle-plugin")
}

// The manifold Gradle plugin version. Update this if you update your IntelliJ Plugin!
manifold { manifoldVersion = "2026.1.8" }

txnitemplate {
	sc = stonecutter
	init()
}

val mod = txnitemplate.mod



// Dependencies
repositories {
	exclusiveMaven("https://www.cursemaven.com", "curse.maven")
	exclusiveMaven("https://api.modrinth.com/maven", "maven.modrinth")
	exclusiveMaven("https://thedarkcolour.github.io/KotlinForForge/", "thedarkcolour")
	maven("https://maven.kikugie.dev/releases")
	maven("https://jitpack.io")
	maven("https://maven.neoforged.net/releases/")
	maven("https://maven.terraformersmc.com/releases/")
	maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
	maven("https://maven.parchmentmc.org")
	exclusiveMaven("https://maven.txni.dev/releases", "toni.txnilib", "toni.sodiumdynamiclights")
	maven("https://maven.su5ed.dev/releases")
	maven("https://maven.wispforest.io/releases")
	maven("https://maven.fabricmc.net")
	maven("https://maven.shedaniel.me/")
	maven("https://maven.nucleoid.xyz/releases")
}

dependencies {
	// apply the Manifold processor, do not remove this unless you want to swap back to Stonecutter preprocessor
	if (mod.mcVersion != "26.1.2")
		implementation(annotationProcessor("systems.manifold:manifold-preprocessor:${manifold.manifoldVersion.get()}")!!)

	if (mod.mcVersion != "26.1.2") {
		compileOnly("org.projectlombok:lombok:1.18.34")
		annotationProcessor("org.projectlombok:lombok:1.18.34")
	}

	// Minecraft 26.1+ ships unobfuscated, so Loom must use the game names
	// directly and no mappings dependency may be declared.
	if (mod.mcVersion != "26.1.2") {
		@Suppress("UnstableApiUsage")
		add("mappings", loom.layered {
			officialMojangMappings()
			val parchmentVersion = when (mod.mcVersion) {
				"1.18.2" -> "1.18.2:2022.11.06"
				"1.19.2" -> "1.19.2:2022.11.27"
				"1.20.1" -> "1.20.1:2023.09.03"
				"1.21.1" -> "1.21:2024.07.28"
				else -> null
			}
			if (parchmentVersion != null) {
				parchment("org.parchmentmc.data:parchment-$parchmentVersion@zip")
			}
		})
	}

	settings.depsHandler.addGlobal(this)

	if (mod.isFabric) {
		if (mod.mcVersion != "26.1.2")
			add("modImplementation", settings.depsHandler.modrinth("modmenu", property("deps.modmenu")))

		settings.depsHandler.addFabric(this)
		add(if (mod.mcVersion == "26.1.2") "implementation" else "modImplementation", "net.fabricmc.fabric-api:fabric-api:${property("deps.fapi")}")
		add(if (mod.mcVersion == "26.1.2") "implementation" else "modImplementation", "net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")

		if (setting("runtime.sodium"))
			add("modRuntimeOnly", settings.depsHandler.modrinth("sodium", when (mod.mcVersion) {
				"1.21.1" -> "mc1.21-0.6.0-beta.2-fabric"
				"1.20.1" -> "mc1.20.1-0.5.11"
				else -> null
			}))
	}

	if (mod.isForge) {
		settings.depsHandler.addForge(this)
		"forge"("net.minecraftforge:forge:${mod.mcVersion}-${property("deps.fml")}")
	}

	if (mod.isNeo) {
		settings.depsHandler.addNeo(this)
		"neoForge"("net.neoforged:neoforge:${property("deps.fml")}")

		if (setting("runtime.sodium"))
			runtimeOnly(settings.depsHandler.modrinth("sodium", "mc1.21-0.6.0-beta.2-neoforge"))
	}

	vineflowerDecompilerClasspath("org.vineflower:vineflower:1.10.1")
}

fun setting(prop : String) : Boolean = property(prop) == "true"

// Loom config
loom {
	val awFile = if (mod.mcVersion == "26.1.2")
		project.file("${mod.id}.classtweaker")
	else
		rootProject.file("src/main/resources/${mod.id}.accesswidener")
	if (awFile.exists())
		accessWidenerPath.set(awFile)

	if (mod.loader == "forge") forge {
		convertAccessWideners.set(true)
		mixinConfigs("mixins.${mod.id}.json")
	}

	if (mod.isActive) {
		runConfigs.all {
			ideConfigGenerated(true)
			vmArgs("-Dmixin.debug.export=true", "-Dsodium.checks.issue2561=false")
			// Mom look I'm in the codebase!
			//programArgs("--username=${mod.clientuser}", "--uuid=${mod.clientuuid}")
			runDir = "../../run/${stonecutter.current.project}/"
		}
	}

	decompilers {
		get("vineflower").apply {
			options.put("mark-corresponding-synthetics", "1")
		}
	}

	runs {
		register("datagen") {
			client()
			name("DataGen Client")
			vmArg("-Dfabric-api.datagen")
			vmArg("-Dfabric-api.datagen.output-dir=" + getRootDir().toPath().resolve("src/main/generated"))
			vmArg("-Dfabric-api.datagen.modid=${mod.id}")
			ideConfigGenerated(false)
			runDir("build/datagen")
		}
	}
}

sourceSets {
	main {
		if (mod.mcVersion == "26.1.2") {
			java.setSrcDirs(listOf(project.file("src/main/java")))
		}
		resources {
			srcDir("src/main/generated")
			exclude(".cache/")
		}
	}
}

// Tasks
tasks {
	if (mod.isNeo) {
		named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
			atAccessWideners.add("${mod.id}.accesswidener")
		}
	}
}

tasks.withType<Tar>() {
	duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.withType<Zip>() {
	duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.compileJava {
	options.encoding = "UTF-8"
	if (mod.mcVersion == "26.1.2") {
		setSource(fileTree(project.file("src/main/java")) { include("**/*.java") })
		doFirst {
			options.compilerArgs.removeAll { it == "-Xplugin:Manifold" }
		}
	} else {
		options.compilerArgs.add("-Xplugin:Manifold")
	}
	// modify the JavaCompile task and inject our auto-generated Manifold symbols
	if(mod.mcVersion != "26.1.2" && !this.name.startsWith("_")) { // check the name, so we don't inject into Forge internal compilation
		ManifoldMC.setupPreprocessor(options.compilerArgs, mod.loader, projectDir, mod.mcVersion, stonecutter.active.project == stonecutter.current.project, false)
	}
}

project.tasks.register("setupManifoldPreprocessors") {
	group = "build"
	ManifoldMC.setupPreprocessor(ArrayList(), mod.loader, projectDir, mod.mcVersion, stonecutter.active.project == stonecutter.current.project, true)
}

tasks.setupChiseledBuild { finalizedBy("setupManifoldPreprocessors") }


val unobfuscatedSourcesJar = if (mod.mcVersion == "26.1.2") {
	tasks.register<Jar>("sourcesJar") {
		archiveClassifier.set("sources")
		from(sourceSets.main.get().allSource)
	}
} else null

val distributableJar = if (mod.mcVersion == "26.1.2")
	tasks.named<Jar>("jar").flatMap { it.archiveFile }
else
	tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar").flatMap { it.archiveFile }

val distributableSourcesJar = if (mod.mcVersion == "26.1.2")
	unobfuscatedSourcesJar!!.flatMap { it.archiveFile }
else
	tasks.named<net.fabricmc.loom.task.RemapSourcesJarTask>("remapSourcesJar").flatMap { it.archiveFile }

val buildAndCollect = tasks.register<Copy>("buildAndCollect") {
	group = "build"
	from(distributableJar)
	into(rootProject.layout.buildDirectory.file("libs/${mod.version}"))
	dependsOn("build")
}

if (stonecutter.current.isActive) {
	rootProject.tasks.register("buildActive") {
		group = "project"
		dependsOn(buildAndCollect)
	}

	rootProject.tasks.register("runActive") {
		group = "project"
		dependsOn(tasks.named("runClient"))
	}
}

stonecutter {
	val j21 = eval(mod.mcVersion, ">=1.20.6")
	val j25 = eval(mod.mcVersion, ">=26.1")
	java {
		withSourcesJar()
		sourceCompatibility = if (j25) JavaVersion.VERSION_25 else if (j21) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
		targetCompatibility = if (j25) JavaVersion.VERSION_25 else if (j21) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
	}

	kotlin {
		// Kotlin targets Java 25 for Minecraft 26.1, but can run its compiler
		// from the installed JDK 26 (the Kotlin plugin currently falls back to
		// JVM_25 when hosted on JDK 26).
		jvmToolchain(if (j25) 26 else if (j21) 21 else 17)
	}
}

tasks.processResources {
	if (mod.mcVersion == "26.1.2") {
		eachFile {
			val legacyResources = rootProject.file("src/main/resources").absolutePath
			val legacyGeneratedResources = rootProject.file("src/main/generated").absolutePath
			if (file.absolutePath.startsWith(legacyResources) &&
				(path == "${mod.id}.accesswidener" || path == "fabric.mod.json" || path == "mixins.${mod.id}.json" ||
				 path.startsWith("data/accessories/") || path.startsWith("data/curios/"))) {
				exclude()
			}
			if (file.absolutePath.startsWith(legacyGeneratedResources) &&
				path == "assets/${mod.id}/lang/en_us.json") {
				exclude()
			}
		}
		from(project.file("${mod.id}.classtweaker")) {
			rename { "${mod.id}.accesswidener" }
		}
	}

	val map = mapOf(
		"modversion" to mod.version,
		"mc" to mod.mcDep,
		"id" to mod.id,
		"group" to mod.group,
		"author" to mod.author,
		"namespace" to mod.namespace,
		"description" to mod.description,
		"discord" to mod.discord,
		"name" to mod.name,
		"license" to mod.license,
		"github" to mod.github,
		"display_name" to mod.displayName,
		"fml" to if (mod.loader == "neoforge") "1" else "45",
		"mnd" to if (mod.loader == "neoforge") "" else "mandatory = true"
	)

	filesMatching("fabric.mod.json") { expand(map) }
	filesMatching("META-INF/mods.toml") { expand(map) }
	filesMatching("META-INF/neoforge.mods.toml") { expand(map) }
}

if (mod.mcVersion == "26.1.2") {
	tasks.matching { it.name.startsWith("j52j") }.configureEach {
		enabled = false
	}
}

// Publishing
publishMods {
	file = distributableJar
	additionalFiles.from(distributableSourcesJar)
	displayName = "${mod.name} ${mod.loader.replaceFirstChar { it.uppercase() }} ${mod.version} for ${property("mod.mc_title")}"
	version = mod.version
	changelog = rootProject.file("CHANGELOG.md").readText()
	type = STABLE
	modLoaders.add(mod.loader)

	val targets = property("mod.mc_targets").toString().split(' ')

	dryRun = providers.environmentVariable("MODRINTH_TOKEN").getOrNull() == null ||
			providers.environmentVariable("CURSEFORGE_TOKEN").getOrNull() == null

	modrinth {
		projectId = property("publish.modrinth").toString()
		accessToken = providers.environmentVariable("MODRINTH_TOKEN")
		targets.forEach(minecraftVersions::add)
		val deps = DependencyContainer(null, this)
 		settings.publishHandler.addModrinth(deps)
		settings.publishHandler.addShared(deps)
	}

	curseforge {
		projectId = property("publish.curseforge").toString()
		accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
		targets.forEach(minecraftVersions::add)
		val deps = DependencyContainer(this, null)
		settings.publishHandler.addCurseForge(deps)
		settings.publishHandler.addShared(deps)
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			groupId = "${property("mod.group")}.${mod.id}"
			version = mod.version
			artifactId = "${mod.loader}-${mod.mcVersion}" //base.archivesName.get()

			from(components["java"])
		}
	}

	repositories {
		val username = "MAVEN_USERNAME".let { System.getenv(it) ?: findProperty(it) }?.toString()
		val password = "MAVEN_PASSWORD".let { System.getenv(it) ?: findProperty(it) }?.toString()

		if (username == null || password == null) {
			println("No maven credentials found.")
            return@repositories;
		}

		val mavenURI = if (properties["publish.use_snapshot_maven"] == "true") "snapshots" else "releases"
		maven {
			name = "${mod.author}_$mavenURI"
			url = uri("https://${property("publish.maven_url").toString()}/$mavenURI")
			credentials {
				this.username = System.getenv("MAVEN_USERNAME")
				this.password = System.getenv("MAVEN_PASSWORD")
			}
		}
	}
}
