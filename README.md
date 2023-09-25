# mirai-keyword-reply

mirai插件，支持关键词回复和一言回复

## 使用方法

从 [releases](https://github.com/moesnow/mirai-keyword-reply/releases/latest) 下载插件，放入`plugins`内

首次运行后会生成配置文件位于 `config/top.kotori.KeywordReply/KeywordReply.yml`

```yaml
Groups: # 启用的群号
  - 123456789
  - 987654321
Keywords: # 例如：消息内包含key1和key2，就会回复value1
  value1:
    - key1
    - key2
  value2:
    - key3
    - key4
    - key5
hitokoto: true # 是否开启一言，发送“一言”触发
tiangou: true # 是否开启舔狗日记，发送“舔狗日记”触发
```