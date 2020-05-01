### [中文](README-zh.md) | English

# TBS IPTV player for Android TV, with udpxy support.

[official site](http://www.turboshow.cn) (Chinese)

![](https://images.gitee.com/uploads/images/2019/0727/201458_7b480937_82552.png)

## IPTV Configuration
### web interface
`http://{yourAppHostIp}:1213`

### web api
#### Import TV channels
`POST http://{yourAppHostIp}:1213/api/settings/playlist`

body
```
[
    {
        "title": "Channel 1",
        "url": "rtp://239.0.0.1:1234"
    },
    {
        "title": "Channel 2",
        "url": "rtp://239.0.0.2:1234"
    },
    ...
]
```

#### udpxy
udpxy can be enabled to convert RTP to HTTP.

`POST http://{yourAppHostIp}:1212/api/settings/udpxy`

body
```
{
    "addr": "192.168.1.254:1234"
}
```

 To disable udpxy, POST
```
{
    "addr": null
}
```
