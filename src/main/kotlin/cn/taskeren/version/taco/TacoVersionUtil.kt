package cn.taskeren.version.taco

import cn.taskeren.version.taco.TacoConst.tacoNameValidator
import com.google.gson.*
import org.apache.maven.artifact.versioning.ComparableVersion

object TacoVersionUtil {

	private val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

	fun TacoVersion.toJsonTree(): JsonElement {
		val json = JsonObject()

		this.versions.forEach { entry ->
			json.add(entry.version.toString(), gson.toJsonTree(entry.properties))
		}

		this.subVersions.forEach { (subVersionName, subVersion) ->
			json.add("$name-$subVersionName", subVersion.toJsonTree())
		}

		return json
	}

	fun JsonObject.toTacoVersion(parent: TacoVersion? = null): TacoVersion {
		if(parent == null) {
			if(this.size() != 1) { error("Invalid Json File: entry size is not one") }

			val rootName = this.keySet().first()
			val tacoRoot = TacoVersion(rootName)
			this[rootName].asJsonObject.toTacoVersion(tacoRoot)
			return tacoRoot
		} else {
			this.keySet().forEach { entryName ->
				if(entryName.startsWith("${parent.name}-")) {
					val subName = entryName.removePrefix("${parent.name}-")
					if(tacoNameValidator(subName)) {
						val subVersionTaco = parent.createSub(subName)
						this[entryName].asJsonObject.toTacoVersion(subVersionTaco)
					} else {
						error("Invalid Version Name: '${subName}' contains invalid character")
					}
				} else {
					val v = ComparableVersion(entryName)
					val p = gson.fromJson(this[entryName].asJsonObject, Map::class.java)
					@Suppress("UNCHECKED_CAST")
					val entry = TacoVersion.TacoVersionEntry(v, p as Map<String, Any>)
					parent.versions += entry
				}
			}
			return parent
		}
	}

	fun TacoVersion.printGraphicString(depth: Int = 0) {
		out(fullName, depth)
		for(v in versions) {
			out("${v.version}: ${v.properties["changelog"]}", depth)
		}
		for(sub in subVersions) {
			sub.value.printGraphicString(depth+1)
		}
	}

	private fun out(message: Any, prefixLength: Int = 0) {
		for(i in 0 until prefixLength) print("\t")
		println(message)
	}

}