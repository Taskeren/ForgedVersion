package cn.taskeren.taco

import cn.taskeren.version.taco.TacoVersion
import cn.taskeren.version.taco.TacoVersionUtil.printGraphicString
import cn.taskeren.version.taco.TacoVersionUtil.toTacoVersion
import com.google.gson.JsonParser
import org.junit.Test
import java.io.File

class TacoVersionTest {

	private val defaultTacoVersionText = File("./src/test/resources/taco-default.json").readText()

	@Test
	fun testTacoParse() {
		val taco = JsonParser.parseString(defaultTacoVersionText).asJsonObject.toTacoVersion()
		taco.printGraphicString()
	}

	@Test
	fun testTacoPrint() {
		TacoVersion("aura").printGraphicString()
	}

}