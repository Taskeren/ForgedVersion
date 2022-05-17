@file:Suppress("MemberVisibilityCanBePrivate")

package cn.taskeren.version

import org.apache.maven.artifact.versioning.ComparableVersion

class ForgedVersionChannel(val name: String) : Iterable<ForgedVersionChannel.Version> {

	data class Version(val version: ComparableVersion, val changelog: String, val recommended: Boolean = false) :
		Comparable<Version> {
		override fun compareTo(other: Version): Int = version.compareTo(other.version)
	}

	internal val versions = sortedSetOf<Version>()

	override fun iterator(): Iterator<Version> = versions.iterator()

	val latest: Version get() = versions.last()
	val recommended: Version? get() = versions.lastOrNull { it.recommended }

	fun addVersion(version: ComparableVersion, changelog: String, recommended: Boolean = false) = apply {
		versions += Version(version, changelog, recommended)
	}

	fun removeVersion(version: ComparableVersion) = versions.removeIf { it.version == version }

	override fun toString(): String = if(recommended != null) {
		"$name[lat=${latest.version}, rec=${recommended!!.version}]"
	} else {
		"$name[lat=${latest.version}]"
	}

	class Builder(val name: String) {
		private val branch = ForgedVersionChannel(name)

		fun version(versionStr: String, changelog: String, recommended: Boolean = false) =
			version(ComparableVersion(versionStr), changelog, recommended)

		fun version(version: ComparableVersion, changelog: String, recommended: Boolean = false) =
			branch.addVersion(version, changelog, recommended)

		fun build(): ForgedVersionChannel = branch
	}

}