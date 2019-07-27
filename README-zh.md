### 中文 | [English](README.md)

# 土拨鼠影音，IPTV播放器。基于libvlc。

[官网](http://www.turboshow.cn)

[下载](https://gitee.com/sagittar/tbs-android/releases)

![](http://www.turboshow.cn/assets/screenshot.png)
## 设置
### web界面
`http://{app所在设备IP}:1212`

### Web API
#### 导入播放列表
注：请自行抓包制作播放列表，或者网上找

`POST http://{app所在设备IP}:1212/api/settings/playlist`

body
```
[
    {
        "title": "Channel 1",
        "addr": "239.0.0.1:1234"
    },
    {
        "title": "Channel 2",
        "addr": "239.0.0.2:1234"
    },
    ...
]
```

#### udpxy
播放器默认使用rtp协议，如路由器支持udpxy，可设置相应地址进行加速.

`POST http://{app所在设备IP}:1212/api/settings/udpxy`

body
```
{
    "addr": "192.168.1.254:1234"
}
```
关闭udpxy
 
 POST
```
{
    "addr": null
}
```

## build内置web界面

`$ cd web`

`$ ./deploy.sh`