package top.imaikuai

import com.alibaba.fastjson.JSON
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.utils.info
import org.jsoup.Jsoup

object KeywordReply : KotlinPlugin(
    JvmPluginDescription(
        id = "top.imaikuai.KeywordReply",
        name = "Keyword Reply",
        version = "1.0-SNAPSHOT",
    )
) {
    object KeywordReply : AutoSavePluginConfig("KeywordReply") {
        val groupid by value("123456789")
        val Keyword: Map<String, String> by value(
            mapOf(
                "hello" to "hi",
                "hi" to "hello",
            )
        )
        val hitokoto: Boolean by value(true)
    }

    override fun onEnable() {
        KeywordReply.reload()
        logger.info { "Keyword Reply loaded" }
        GlobalEventChannel.subscribeAlways<GroupMessageEvent> { event ->
            if (sender.group.id.toString() == KeywordReply.groupid) {
                if (KeywordReply.hitokoto && message.contentToString() == "一言") {
                    val html = Jsoup.connect("https://v1.hitokoto.cn/?encode=json").ignoreContentType(true).get()
                    val hitokoto = JSON.parseObject(html.text())
                    val at = At(event.sender.id)
                    subject.sendMessage(at + (hitokoto.getString("hitokoto") + "来源：" + hitokoto.getString("from")))

                } else {
                    for ((key, value) in KeywordReply.Keyword) {
                        if (message.contentToString().contains(key)) {
                            subject.sendMessage(value)
                        }
                    }
                }
            }
        }
    }
}
