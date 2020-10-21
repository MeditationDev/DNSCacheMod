# DNSCacheMod
Dynamic modify your Minecraft DNSCache(likes host file)!  
Support all versions of Minecraft theoretically  

## Commands
Command is */dnscachemod*  
Usages:  
*/dnscachemod add from target* ADD A DOMAIN FOR DNSCACHE  
*/dnscachemod remove from* REMOVE A DOMAIN FOR DNSCACHE  
*/dnscachemod createsharelink from target* CREATE A DNSCACHE SHARE LINK  
*/dnscachemod loadsharelink link* LOAD A DNSCACHE SHARE LINK  
*/dnscachemod list* LIST DOMAINS USING DNSCACHE  
*/dnscachemod reload* RELOAD DNSCACHES  

## Data
data file is in MINECRAFT_FOLDER/config/DNSCache.json  
format:  
~~~
[
    {"from":"mc.hypixel.net","target":"meditationdev.org"}
]
~~~
and connect to "meditationdev.org" in minecraft then go into ""  
*from* is the original server address  
*target* is the target you want to change DNS for it  

## ShareLink
It is a base64 Str begin with *dnscache://* and split with "!"  
like this:*dnscache://aWRjLmN1YmVwYWFzLmNvbSFhY2NlbC5oeXBpeGVsLm5ldA==*  
"aWRjLmN1YmVwYWFzLmNvbSFhY2NlbC5oeXBpeGVsLm5ldA==" is a base64 String means "idc.cubepaas.com!accel.hypixel.net"  
before ! is "from",after is "target"  