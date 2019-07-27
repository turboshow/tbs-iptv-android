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
By default the player uses the RTP protocol. udpxy can be enabled to use HTTP.

`POST http://{yourAppHostIp}:1212/api/settings/udpxy`

body
```
    {
        "title": "Channel 1",
        "url": "rtp://239.0.0.1:1234"
    },
    {
        "title": "Channel 2",
        "url": "rpt://239.0.0.2:1234"
    },
    ...
```

## Build web interface

`$ cd web`

`$ ./deploy.sh`
