package cn.taskeren

import cn.taskeren.version.ForgedVersionChecker
import org.junit.Test
import java.io.File

class ForgedVersionCheckerTest {

	private val testManifestWithLat = File("./src/test/resources/testManifest-plat-1.0-lat.json").readText()
	private val testManifestWithRecAndLat = File("./src/test/resources/testManifest-plat-1.0-rec-lat.json").readText()
	private val testManifestChannels = File("./src/test/resources/testManifest-channels.json").readText()

	@Test
	fun testForgedVersionWithLatOnly() {
		println("Manifest[pf=1.0, lat=1.0.5]")
		println(testManifestWithLat)
		println()
		println("====> [cur=1.0.0] <====")
		ForgedVersionChecker.check("1.0", testManifestWithLat, "1.0.0").apply(ForgedVersionChecker.Result::printForgeLog).apply(::println)
		println("Expected status: BETA_OUTDATED Target: 1.0.5")
		println()
		println("====> [cur=1.0.3] <====")
		ForgedVersionChecker.check("1.0", testManifestWithLat, "1.0.3").apply(ForgedVersionChecker.Result::printForgeLog).apply(::println)
		println("Expected status: BETA_OUTDATED Target: 1.0.5")
		println()
		println("====> [cur=1.0.4] <====")
		ForgedVersionChecker.check("1.0", testManifestWithLat, "1.0.4").apply(ForgedVersionChecker.Result::printForgeLog).apply(::println)
		println("Expected status: BETA_OUTDATED Target: 1.0.5")
		println()
		println("====> [cur=1.0.5] <====")
		ForgedVersionChecker.check("1.0", testManifestWithLat, "1.0.5").apply(ForgedVersionChecker.Result::printForgeLog).apply(::println)
		println("Expected status: BETA Target: null")
		println()
	}

	@Test
	fun testForgedVersionWithRecAndLat() {
		println("Manifest[pf=1.0, rec=1.0.3, lat=1.0.5]")
		println(testManifestWithRecAndLat)
		println()
		println("====> [cur=1.0.0] <====")
		ForgedVersionChecker.check("1.0", testManifestWithRecAndLat, "1.0.0").apply(ForgedVersionChecker.Result::printForgeLog).apply(::println)
		println("Expected status: OUTDATED Target: 1.0.3")
		println()
		println("====> [cur=1.0.3] <====")
		ForgedVersionChecker.check("1.0", testManifestWithRecAndLat, "1.0.3").apply(ForgedVersionChecker.Result::printForgeLog).apply(::println)
		println("Expected status: UP_TO_DATE Target: null")
		println()
		println("====> [cur=1.0.4] <====")
		ForgedVersionChecker.check("1.0", testManifestWithRecAndLat, "1.0.4").apply(ForgedVersionChecker.Result::printForgeLog).apply(::println)
		println("Expected status: OUTDATED Target: 1.0.5")
		println()
		println("====> [cur=1.0.5] <====")
		ForgedVersionChecker.check("1.0", testManifestWithRecAndLat, "1.0.5").apply(ForgedVersionChecker.Result::printForgeLog).apply(::println)
		println("Expected status: AHEAD Target: null")
		println()
	}

	@Test
	fun testReadString() {
		ForgedVersionChecker.fromString(testManifestChannels).apply(::println)
	}

}
