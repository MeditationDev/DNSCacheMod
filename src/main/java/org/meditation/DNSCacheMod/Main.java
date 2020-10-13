package org.meditation.DNSCacheMod;

import com.alibaba.dcm.DnsCacheEntry;
import com.google.gson.*;
import net.minecraft.command.ICommandManager;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import com.alibaba.dcm.DnsCacheManipulator;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.meditation.DNSCacheMod.utils.FileUtils;

import java.io.File;
import java.util.List;

@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main {
    public static final String MODID = "dnscachemod";
    public static final String VERSION = "1.0";
    public static DNSData[] lastData=null;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        try {
            ClientCommandHandler.instance.registerCommand(new DNSModifyCommand());
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        try{
            if(!new File("./config/DNSCache.json").exists()){
                FileUtils.writeFile("./config/DNSCache.json","[]");
            }
            loadDNSCache();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void reloadDNSCache() throws Exception {
        for(DNSData data : lastData){
            DnsCacheManipulator.removeDnsCache(data.target);
            System.out.println("REMOVE "+data.target);
        }
        loadDNSCache();
    }

    public static void loadDNSCache() throws Exception {
        DNSData[] dnsData=new Gson().fromJson(FileUtils.readFile("./config/DNSCache.json"),DNSData[].class);
        lastData=dnsData;
        for(DNSData data : dnsData){
            String fromIp=java.net.InetAddress.getByName(data.from).getHostAddress();
            System.out.println("FROM:"+data.from+" FROMIP:"+fromIp+" TARGET:"+data.target);
            DnsCacheManipulator.setDnsCache(data.target,fromIp);
        }
    }
}
