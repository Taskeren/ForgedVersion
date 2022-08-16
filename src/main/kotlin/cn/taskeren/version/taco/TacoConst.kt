package cn.taskeren.version.taco

object TacoConst {

	var tacoNameValidator: String.() -> Boolean = { !contains('-') }

}