# mirai-keyword-reply

mirai插件，支持关键词回复和一言回复

## 使用方法

从 [releases](https://github.com/moesnow/mirai-keyword-reply/releases/latest) 下载插件，放入`plugins`内

首次运行后会生成配置文件位于 `config/top.kotori.KeywordReply/KeywordReply.yml`

```yaml
group_id: 123456789 # 启用的群号
Keyword: # 例如：消息内包含hello，就会回复hi
  hello: hi
  hi: hello
hitokoto: true # 是否开启一言，发送“一言”触发
```