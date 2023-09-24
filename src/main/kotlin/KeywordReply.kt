package top.imaikuai

import com.alibaba.fastjson.JSON
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import net.mamoe.mirai.utils.info
import org.jsoup.Jsoup

object KeywordReply : KotlinPlugin(
    JvmPluginDescription(
        id = "top.kotori.KeywordReply",
        name = "Keyword Reply",
        version = "1.0.1",
    )
) {
    object KeywordReply : AutoSavePluginConfig("KeywordReply") {
        val groupId by value("123456789")
        val Keyword: Map<String, String> by value(
            mapOf(
                "hello" to "hi",
                "hi" to "hello",
            )
        )
        val hitokoto: Boolean by value(true)
        val tiangou: Boolean by value(true)
    }

    override fun onEnable() {
        // 重新加载关键词配置
        KeywordReply.reload()
        logger.info { "Keyword Reply loaded" }

        // 订阅群组消息事件
        GlobalEventChannel.subscribeAlways<GroupMessageEvent> { event ->
            val groupId = event.sender.group.id.toString()
            val messageContent = event.message.contentToString()

            if (groupId == KeywordReply.groupId) {
                if (KeywordReply.hitokoto && messageContent == "一言") {
                    // 处理一言请求
                    handleHitokoto(event.subject, event.message)
                } else if (KeywordReply.tiangou && messageContent == "舔狗日记") {
                    // 处理舔狗日记请求
                    handleTiangou(event.subject, event.message)
                } else {
                    // 处理关键词回复
                    handleKeywordReply(event.subject, event.message)
                }
            }
        }
    }

    private suspend fun handleHitokoto(subject: Group, message: MessageChain) {
        try {
            val html = Jsoup.connect("https://v1.hitokoto.cn/?encode=json")
                .ignoreContentType(true)
                .get()
            val hitokoto = JSON.parseObject(html.text())
            val replyMessage = message.quote() +
                    "${hitokoto.getString("hitokoto")}\n来源：${hitokoto.getString("from")}"
            subject.sendMessage(replyMessage)
        } catch (e: Exception) {
            logger.error(e)
        }
    }

    private suspend fun handleTiangou(subject: Group, message: MessageChain) {
        try {
            val html = Jsoup.connect("https://cloud.qqshabi.cn/api/tiangou/api.php")
                .ignoreContentType(true)
                .get()
            val replyMessage = message.quote() + html.text()
            subject.sendMessage(replyMessage)
        } catch (e: Exception) {
            logger.error(e)
        }
    }

    private suspend fun handleKeywordReply(subject: Group, message: MessageChain) {
        val messageContent = message.contentToString()
        for ((key, value) in KeywordReply.Keyword) {
            if (messageContent.contains(key)) {
                subject.sendMessage(message.quote() + value)
                break
            }
        }
    }

}
