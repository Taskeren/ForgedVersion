package cn.taskeren

import cn.taskeren.version.forge.ForgedVersion
import cn.taskeren.version.forge.VersionCheckResult
import org.apache.maven.artifact.versioning.ComparableVersion
import org.junit.Test
import java.io.File

class ForgedVersionCheckerTest {

	private val testManifestWithLat = File("./src/test/resources/testManifest-plat-1.0-lat.json").readText()
	private val testManifestWithRecAndLat = File("./src/test/resources/testManifest-plat-1.0-rec-lat.json").readText()
	private val testManifestWithEmptyPromos = File("./src/test/resources/testManifest-plat-1.0.json").readText()
	private val testManifestChannels = File("./src/test/resources/testManifest-channels.json").readText()

	@Test
	fun testForgedVersionWithLatOnly() {
		println("Manifest[pf=1.0, lat=1.0.5]")
		println(testManifestWithLat)
		println()
		println("====> [cur=1.0.0] <====")
		ForgedVersion.fromJson(testManifestWithLat).check("1.0", ComparableVersion("1.0.0"))
			.apply(VersionCheckResult::printForgeLog).apply(::println)
		println("Expected status: BETA_OUTDATED Target: 1.0.5")
		println()
		println("====> [cur=1.0.3] <====")
		ForgedVersion.fromJson(testManifestWithLat).check("1.0", ComparableVersion("1.0.3"))
			.apply(VersionCheckResult::printForgeLog).apply(::println)
		println("Expected status: BETA_OUTDATED Target: 1.0.5")
		println()
		println("====> [cur=1.0.4] <====")
		ForgedVersion.fromJson(testManifestWithLat).check("1.0", ComparableVersion("1.0.4"))
			.apply(VersionCheckResult::printForgeLog).apply(::println)
		println("Expected status: BETA_OUTDATED Target: 1.0.5")
		println()
		println("====> [cur=1.0.5] <====")
		ForgedVersion.fromJson(testManifestWithLat).check("1.0", ComparableVersion("1.0.5"))
			.apply(VersionCheckResult::printForgeLog).apply(::println)
		println("Expected status: BETA Target: null")
		println()
	}

	@Test
	fun testForgedVersionWithRecAndLat() {
		println("Manifest[pf=1.0, rec=1.0.3, lat=1.0.5]")
		println(testManifestWithRecAndLat)
		println()
		println("====> [cur=1.0.0] <====")
		ForgedVersion.fromJson(testManifestWithRecAndLat).check("1.0", ComparableVersion("1.0.0"))
			.apply(VersionCheckResult::printForgeLog).apply(::println)
		println("Expected status: OUTDATED Target: 1.0.3")
		println()
		println("====> [cur=1.0.3] <====")
		ForgedVersion.fromJson(testManifestWithRecAndLat).check("1.0", ComparableVersion("1.0.3"))
			.apply(VersionCheckResult::printForgeLog).apply(::println)
		println("Expected status: UP_TO_DATE Target: null")
		println()
		println("====> [cur=1.0.4] <====")
		ForgedVersion.fromJson(testManifestWithRecAndLat).check("1.0", ComparableVersion("1.0.4"))
			.apply(VersionCheckResult::printForgeLog).apply(::println)
		println("Expected status: OUTDATED Target: 1.0.5")
		println()
		println("====> [cur=1.0.5] <====")
		ForgedVersion.fromJson(testManifestWithRecAndLat).check("1.0", ComparableVersion("1.0.5"))
			.apply(VersionCheckResult::printForgeLog).apply(::println)
		println("Expected status: AHEAD Target: null")
		println()
	}

	@Test
	fun testForgedVersionWithEmptyPromos() {
		println("Manifest[pf=1.0, rec=1.0.3, lat=1.0.5]")
		println(testManifestWithEmptyPromos)
		println()
		println("====> [cur=1.0.0] <====")
		ForgedVersion.fromJson(testManifestWithEmptyPromos).check("1.0", ComparableVersion("1.0.0")).apply(
			VersionCheckResult::printForgeLog).apply(::println)
		println("Expected status: OUTDATED Target: 1.0.3")
		println()
		println("====> [cur=1.0.3] <====")
		ForgedVersion.fromJson(testManifestWithEmptyPromos).check("1.0", ComparableVersion("1.0.3")).apply(
			VersionCheckResult::printForgeLog).apply(::println)
		println("Expected status: UP_TO_DATE Target: null")
		println()
		println("====> [cur=1.0.4] <====")
		ForgedVersion.fromJson(testManifestWithEmptyPromos).check("1.0", ComparableVersion("1.0.4")).apply(
			VersionCheckResult::printForgeLog).apply(::println)
		println("Expected status: OUTDATED Target: 1.0.5")
		println()
		println("====> [cur=1.0.5] <====")
		ForgedVersion.fromJson(testManifestWithEmptyPromos).check("1.0", ComparableVersion("1.0.5")).apply(
			VersionCheckResult::printForgeLog).apply(::println)
		println("Expected status: AHEAD Target: null")
		println()
	}

	@Test
	fun testReadString() {
		ForgedVersion.fromJson(testManifestChannels).apply(::println)
	}

}
