@file:Suppress("UNCHECKED_CAST")

package cn.taskeren.version.forge

import cn.taskeren.version.forge.VersionStatus.*
import com.google.gson.*
import org.apache.maven.artifact.versioning.ComparableVersion

@Deprecated("deprecated")
@Suppress("DEPRECATION")
object ForgedVersionChecker {

	@JvmStatic
	@Deprecated("deprecated", replaceWith = ReplaceWith("ForgedVersion.fromJson(versionJsonStr)"))
	fun fromString(versionJsonStr: String): ForgedVersion = fromJson(JsonParser.parseString(versionJsonStr))

	@JvmStatic
	@Deprecated("deprecated", replaceWith = ReplaceWith("ForgedVersion.fromJson(versionJson)"))
	fun fromJson(versionJson: JsonElement): ForgedVersion {

		val json = Gson().fromJson<Map<String, Any>>(versionJson, Map::class.java)

		val homepage = json["homepage"] as? String

		val channels = mutableListOf<ForgedVersionChannel>()
		json.forEach { (key, value) ->
			if(key != "promos" && key != "homepage") {
				if(value is Map<*, *>) {
					val channelData =
						value as? Map<String, String> ?: throw IllegalStateException("Can't read channel object $key")
					val channel = ForgedVersionChannel(key).apply(channels::add)
					channelData.forEach { (version, changelog) ->
						channel.addVersion(ComparableVersion(version), changelog)
					}
				}
			}
		}

		val promos = json["promos"] as? Map<String, String> ?: throw IllegalStateException("Can't read 'promos'")
		promos.forEach { (channelAndSuffix, versionStr) ->
			if(channelAndSuffix.endsWith("-recommended")) {
				val channelName = channelAndSuffix.substring(0, channelAndSuffix.length - 12)
				val channel = channels.firstOrNull { it.name == channelName }
					?: throw IllegalStateException("Can't find undefined channel[$channelName]")
				val version = channel.versions.find { it.version.toString() == versionStr }
					?: throw IllegalStateException("Can't find undefined version[$versionStr] in channel[$channelName]")
				// replace the old one with the recommended one
				channel.versions.remove(version)
				channel.versions.add(version.copy(recommended = true))
			} else if(channelAndSuffix.endsWith("-latest")) {
				val channelName = channelAndSuffix.substring(0, channelAndSuffix.length - 7)
				val channel = channels.firstOrNull { it.name == channelName }
					?: throw IllegalStateException("Can't find undefined channel[$channelName]")
				// only check the existence
				channel.versions.find { it.version.toString() == versionStr }
					?: throw IllegalStateException("Can't find undefined version[$versionStr] in channel[$channelName]")
			}
		}

		return ForgedVersion(homepage, channels)
	}

	@JvmStatic
	@Deprecated(
		"deprecated",
		replaceWith = ReplaceWith(
			expression = "ForgedVersion.fromJson(versionJsonStr).check(channelVer, ComparableVersion(currentVersionStr))",
			imports = ["org.apache.maven.artifact.versioning.ComparableVersion"]
		)
	)
	fun check(channelVer: String, versionJsonStr: String, currentVersionStr: String): VersionCheckResult {

		var status = PENDING
		val curVer = ComparableVersion(currentVersionStr)
		var targetVer: ComparableVersion? = null
		var homepage: String? = null
		val changes = linkedMapOf<ComparableVersion, String>()

		runCatching {
			val json = Gson().fromJson<Map<String, Any>>(versionJsonStr, Map::class.java)

			homepage = json["homepage"] as? String

			val promos = json["promos"] as? Map<String, String>
				?: throw IllegalStateException("'promos' in manifest is undefined")

			val lat = promos["$channelVer-latest"]
			val rec = promos["$channelVer-recommended"]

			if(rec != null) {
				val recVer = ComparableVersion(rec)
				if(curVer == recVer) {
					status = UP_TO_DATE
				} else if(curVer > recVer) {
					status = AHEAD
					if(lat != null) {
						val latVer = ComparableVersion(lat)
						if(curVer < latVer) {
							status = OUTDATED
							targetVer = latVer
						}
					}
				} else {
					status = OUTDATED
					targetVer = recVer
				}
			} else if(lat != null) {
				val latVer = ComparableVersion(lat)
				if(curVer < latVer) {
					status = BETA_OUTDATED
					targetVer = latVer
				} else {
					status = BETA
				}
			}

			val tmp = json[channelVer] as? Map<String, String>
			if(tmp != null) {
				val ordered = mutableListOf<ComparableVersion>()
				for(key in tmp.keys) {
					val ver = ComparableVersion(key)
					if(ver > curVer && (targetVer == null || ver.compareTo(targetVer) < 1)) {
						ordered += ver
					}
				}
				ordered.sort()
				for(ver in ordered) {
					changes[ver] = tmp[ver.toString()]!!
				}
			}
		}.onFailure {
			status = FAILED
		}

		return VersionCheckResult(status, curVer, targetVer, changes, homepage)
	}

}