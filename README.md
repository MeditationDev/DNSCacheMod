# DNSCacheMod
Dynamic modify your Minecraft DNSCache(likes host file)!  
Support all versions of Minecraft theoretically  

## Commands
Command is */dnscachemod*  
Usages:  
*/dnscachemod add from target* ADD A DOMAIN FOR DNSCACHE  
*/dnscachemod remove* from REMOVE A DOMAIN FOR DNSCACHE  
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
and connect to "meditationdev.org" in minecraft then go into "mc.hypixel.net"  
*from* is the original server address  
*target* is the target you want to change DNS for it  