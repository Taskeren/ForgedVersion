package cn.taskeren.version.forge

import org.apache.maven.artifact.versioning.ComparableVersion

data class VersionCheckResult(
	val status: VersionStatus,
	val current: ComparableVersion?,
	val target: ComparableVersion?,
	val changes: LinkedHashMap<ComparableVersion, String>,
	val homepage: String?
) {
	fun printForgeLog() = println("Found status: $status Current: $current Target: $target")
}