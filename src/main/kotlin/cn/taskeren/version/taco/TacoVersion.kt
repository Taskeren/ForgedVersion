package cn.taskeren.version.taco

import org.apache.maven.artifact.versioning.ComparableVersion

/**
 * Taco Version V1
 */
data class TacoVersion(
	val name: String,
	val versions: LinkedHashSet<TacoVersionEntry> = linkedSetOf(),
	val subVersions: LinkedHashMap<String, TacoVersion> = linkedMapOf()
): Iterable<TacoVersion.TacoVersionEntry> {

	var parent: TacoVersion? = null

	val fullName: String get() = if(parent != null) "${parent!!.fullName}/${name}" else name

	data class TacoVersionEntry(val version: ComparableVersion, val properties: Map<String, Any> = mapOf()) {
		override fun hashCode(): Int {
			return version.hashCode()
		}

		override fun equals(other: Any?): Boolean {
			return other is TacoVersionEntry && version == other.version && properties == other.properties
		}
	}

	fun addVersion(version: ComparableVersion, properties: Map<String, Any>) {
		versions += TacoVersionEntry(version, properties)
	}

	fun removeVersion(version: ComparableVersion): Boolean {
		return versions.removeAll { it.version == version }
	}

	fun getOrCreateSub(subName: String): TacoVersion {
		return subVersions.computeIfAbsent(subName) { createSub(subName) }
	}

	internal fun createSub(subName: String) = TacoVersion(subName).apply { this.parent = this@TacoVersion; this@TacoVersion.subVersions[subName] = this }

	override fun iterator(): Iterator<TacoVersionEntry> = versions.iterator()
}