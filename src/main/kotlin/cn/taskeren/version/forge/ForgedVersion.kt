@file:Suppress("MemberVisibilityCanBePrivate")

package cn.taskeren.version.forge

import com.google.gson.JsonObject
import org.apache.maven.artifact.versioning.ComparableVersion

class ForgedVersion(
	val homepage: String? = null,
	val channels: List<ForgedVersionChannel> = mutableListOf()
) {

	/**
	 * Check version status of the current application.
	 *
	 * @param channel the update channel, like `release`, `snapshot`, `nightly`, etc.
	 * @param current the version of the current application
	 */
	fun check(channel: String, current: ComparableVersion): VersionCheckResult {
		var status = VersionStatus.PENDING
		var target: ComparableVersion? = null
		val changes = linkedMapOf<ComparableVersion, String>()

		runCatching {
			val ch = channels.find { it.name == channel } ?: error("Can't find channel[$channel]")

			val latest = ch.latest
			val recommended = ch.recommended
			if(recommended != null) {
				if(current == recommended.version) {
					status = VersionStatus.UP_TO_DATE
				} else if(current > recommended.version) {
					status = VersionStatus.AHEAD
					if(current < latest.version) {
						status = VersionStatus.OUTDATED
						target = latest.version
					}
				} else {
					status = VersionStatus.OUTDATED
					target = recommended.version
				}
			} else {
				if(current < latest.version) {
					status = VersionStatus.BETA_OUTDATED
					target = latest.version
				} else {
					status = VersionStatus.BETA
				}
			}

			ch.forEach { ver ->
				if(ver.version > current && (target == null || ver.version.compareTo(target) < 1)) {
					changes[ver.version] = ver.changelog
				}
			}
		}.onFailure {
			status = VersionStatus.FAILED
		}

		return VersionCheckResult(status, current, target, changes, homepage)
	}

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

	@Suppress("DEPRECATION")
	companion object {
		@JvmStatic
		fun build(homepage: String? = null, func: Builder.() -> Unit) = Builder(homepage).apply(func).build()

		@JvmStatic
		fun fromJson(jsonStr: String) = ForgedVersionChecker.fromString(jsonStr)

		@JvmStatic
		fun fromJson(jsonObject: JsonObject) = ForgedVersionChecker.fromJson(jsonObject)
	}

}