### [中文](README-zh.md) | English

# TBS media player for Android TV, based on libvlc.

[official site](http://www.turboshow.cn) (Chinese)

[Downloads](https://github.com/turboshow/tbs-tv-android/releases)

![](http://www.turboshow.cn/assets/img/screenshot_tv.ede24e22.png)

## Features

* DLNA
* IPTV with udpxy support

## Upcoming

* Local storage browsing
* SMB
* Screen casting

## IPTV Configuration
### web interface
`http://{yourAppHostIp}:1212`

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
