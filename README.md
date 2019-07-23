TBS IPTV player for Android devices, based on libvlc.

[official site](http://www.turboshow.cn) (Chinese)

Download [APK](http://www.turboshow.cn/assets/tbs.apk)

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
`POST http://{yourAppHostIp}:1212/api/settings/udpxy`

body
```
{
    "addr": "192.168.1.254:1234"
}
```
To disable it, POST
```
{
    "addr": null
}
```

## Build web interface
`$ cd web`

`$ yarn build`

`$ cp -r build ../app/src/main/assets/www`