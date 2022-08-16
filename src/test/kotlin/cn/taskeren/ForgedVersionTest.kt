package cn.taskeren

import cn.taskeren.version.forge.ForgedVersion
import com.google.gson.GsonBuilder
import org.junit.Test

class ForgedVersionTest {

	@Test
	fun testForgedVersion() {
		val forgedVersion = ForgedVersion.build {

			channel("alpha") {
				version("1.0.0", "The first successful build.")
				version("1.0.1", "The second build with some patches.")
				version("1.1.0", "The minor update for the Cave & Cliff.")
				version("1.2.0", "The Great API update.", recommended = true)
				version("1.2.1", "The Unknown issue fixes")
			}

			channel("snapshot") {
				version("1.0.0.34", "Github Pull Request #1.")
				version("1.0.0.35", "Github Pull Request #2.")
				version("1.0.1.67", "Github Commit for AbstractPlayer patches.")
				version("1.1.0.12", "Github Pull Request #6 Implementations for Cave & Cliff.")
				version("1.1.0.16", "Fixed problems where players can fly out of the map unintentionally.")
				version("1.2.0.65", "Fixed NPE for endpoint '/v0/Gameplay/GetPlayerInstance/'.")
				version("1.2.1.99", "Declared variant Hex is unavailable in some platform.")
			}

		}

		println(GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(forgedVersion.toJsonTree()))
	}

}