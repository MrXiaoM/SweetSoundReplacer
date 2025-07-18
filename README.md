# SweetSoundReplacer

Minecraft 音效发包替换插件，支持 Minecraft `1.17.1` - `1.21.8`，需要依赖 ProtocolLib。

## 简介

使用这个插件来替换服务端发给客户端的音效。以插件默认配置为例，插件会将 Minecraft 1.19.3 加入的以下几个音效事件替换成等价的原版音效。
+ `block.note_block.imitate.creeper`
+ `block.note_block.imitate.ender_dragon`
+ `block.note_block.imitate.piglin`
+ `block.note_block.imitate.skeleton`
+ `block.note_block.imitate.wither_skeleton`
+ `block.note_block.imitate.zombie`

上述的这些音效事件，同时在服务端和客户端的 SOUND_EVENT 固有注册表中注册了（详见 [Minecraft Wiki](https://zh.minecraft.wiki/w/Java%E7%89%88%E5%A3%B0%E9%9F%B3%E4%BA%8B%E4%BB%B6)），部分场景需要音效在注册表中才能使用，比如[自定义生物群系](https://zh.minecraft.wiki/w/%E7%94%9F%E7%89%A9%E7%BE%A4%E7%B3%BB%E6%95%B0%E6%8D%AE%E6%A0%BC%E5%BC%8F)的背景音乐。

虽然服务端可以修改音效事件注册表，但客户端不能在不安装 Mod 的情况下修改注册表。将服务端发送给客户端的音效事件替换成等价的音效，就可以将这几个音效事件空出来放音乐了。

## 命令

+ `/soundreplacer reload` 重载插件配置文件
