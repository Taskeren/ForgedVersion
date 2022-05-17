package cn.taskeren.version.cli

import cn.taskeren.version.ForgedVersion
import cn.taskeren.version.ForgedVersionChannel
import com.google.gson.GsonBuilder
import java.io.File

private val prettyGson = GsonBuilder().setPrettyPrinting().create()

fun main() {

	var builder: ForgedVersion.Builder? = null
	var channelBuilder: ForgedVersionChannel.Builder? = null

	while(true) {

		if(builder != null) {
			if(channelBuilder != null) {
				print("@${channelBuilder.name}> ")
			} else {
				print("@> ")
			}
		} else {
			print("> ")
		}

		when(readln()) {
			"q", "quit", "exit" -> {
				break
			}
			"create", "cr" -> {
				builder = ForgedVersion.Builder()
				println("创建新的更新文件实例")
			}
			"channel", "c" -> {
				if(builder == null) {
					println("请先使用 create 指令创建新的更新文件实例")
				} else {
					print("频道名称：")
					channelBuilder = ForgedVersionChannel.Builder(readln())
					println("打开新的更新频道")
				}
			}
			"version", "v" -> {
				if(channelBuilder == null) {
					println("请先使用 channel 指令打开更新频道")
				} else {
					print("版本：")
					val ver = readln()
					print("更新日志：")
					val changelog = readln()
					print("是否为推荐版本 true/[false]：")
					val rec = readln().toBoolean()
					channelBuilder.version(ver, changelog, rec)
					println("添加版本 $ver 到更新频道 ${channelBuilder.name} 推荐：$rec")
				}
			}
			"end", "e" -> {
				if(builder != null) {
					if(channelBuilder != null) {
						builder.channels.add(channelBuilder.build())
						println("关闭频道 ${channelBuilder.name}")
						channelBuilder = null
					} else {
						print("尚未导出，确定关闭么？true：")
						if(readln().toBoolean()) {
							builder = null
							println("关闭更新文件实例")
						}
					}
				} else {
					println("尚未打开任何内容")
				}
			}
			"export", "ex" -> {
				if(builder != null) {
					val file = File(readln())
					val json = builder.build().toJsonTree()
					println(prettyGson.toJson(json))
					file.writeText(json.toString())
					builder = null
					println("成功导出到 $file")
					println("已关闭更新文件实例")
				}
			}
			else -> {
				println("""
				# 所有可用的指令
				- q (quit, exit) : 退出客户端
				- create (cr)    : 创建一个新的更新文件
				  - channel (c)  : 填入频道信息
				  - version (v)  : 填入版本信息
				  - end (e)      : 结束频道/版本信息填入
				- export (ex)    : 将创建的更新文件导出
			""".trimIndent())
			}
		}
	}
	println("# See you next time.")
}