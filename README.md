### [中文](README-zh.md) | English

# TBS IPTV player for Android devices, based on libvlc.

[official site](http://www.turboshow.cn) (Chinese)

[Downloads](https://github.com/plestoon/tbs-android/releases)

![](http://www.turboshow.cn/assets/screenshot.png)
## Configuration
### web interface
`http://{yourAppHostIp}:1212`

Currently the web interface is only in Chinese.

### web api
#### Import TV channels
`POST http://{yourAppHostIp}:1212/api/settings/playlist`

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

## Build web interface

`$ cd web`

`$ ./deploy.sh`
