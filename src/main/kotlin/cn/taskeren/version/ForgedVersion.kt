@file:Suppress("MemberVisibilityCanBePrivate")

package cn.taskeren.version

import com.google.gson.JsonObject
import java.util.*

class ForgedVersion(
	val homepage: String? = null,
	val channels: List<ForgedVersionChannel> = mutableListOf()
) {

	/**
	 * Convert this [ForgedVersion] to MinecraftForge-styled [JsonObject].
	 *
	 * An example of the MinecraftForged-styled json:
	 *
	 *     {
	 *       "homepage": "https://this.is.optional.and.can.be.absent.com",
	 *       "promos": {
	 *         "<channel>-latest": "<latest-version-of-the-channel>"
	 *         "<channel>-recommended": "<optional-recommended-version-of-the-channel>"
	 *       },
	 *       "<channel>": {
	 *         "<channel-build-version>": "<changelog-of-this-build>"
	 *       }
	 *     }
	 */
	fun toJsonTree(): JsonObject {

		val json = JsonObject()

		if(homepage != null) {
			json.addProperty("homepage", homepage)
		}

		val promos = JsonObject()
		channels.forEach { branch ->
			promos.addProperty("${branch.name}-latest", "${branch.latest.version}")
			branch.recommended?.let { rec ->
				promos.addProperty("${branch.name}-recommended", "${rec.version}")
			}
		}

		json.add("promos", promos)

		channels.forEach { branch ->
			val branchJson = JsonObject()
			branch.versions.forEach { version ->
				branchJson.addProperty("${version.version}", version.changelog)
			}
			json.add(branch.name, branchJson)
		}

		return json
	}

	override fun toString(): String = "ForgedVersion${channels}"

	class Builder(var homepage: String? = null) {
		val channels = mutableListOf<ForgedVersionChannel>()

		fun channel(name: String, func: ForgedVersionChannel.Builder.() -> Unit) =
			channels.add(ForgedVersionChannel.Builder(name).apply(func).build())

		fun build(): ForgedVersion = ForgedVersion(homepage, channels)
	}

	companion object {
		fun build(homepage: String? = null, func: Builder.() -> Unit) = Builder(homepage).apply(func).build()
	}

}