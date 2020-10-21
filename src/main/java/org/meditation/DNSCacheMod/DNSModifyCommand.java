package org.meditation.DNSCacheMod;

import com.google.gson.Gson;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.meditation.DNSCacheMod.utils.FileUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.Base64;

public class DNSModifyCommand extends CommandBase {

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "dnscachemod";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return EnumChatFormatting.GREEN+"DNSCacheMod by MeditationDev!\n"
                +EnumChatFormatting.WHITE +"/dnscachemod add <from> <target>"+EnumChatFormatting.GRAY+" ADD A DOMAIN FOR DNSCACHE\n"
                +EnumChatFormatting.WHITE+"/dnscachemod remove <from>"+EnumChatFormatting.GRAY+" REMOVE A DOMAIN FOR DNSCACHE\n"
                +EnumChatFormatting.WHITE +"/dnscachemod createsharelink <from> <target>"+EnumChatFormatting.GRAY+" CREATE A DNSCACHE SHARE LINK\n"
                +EnumChatFormatting.WHITE +"/dnscachemod loadsharelink <link>"+EnumChatFormatting.GRAY+" LOAD A DNSCACHE SHARE LINK\n"
                +EnumChatFormatting.WHITE+"/dnscachemod list"+EnumChatFormatting.GRAY+" LIST DOMAINS USING DNSCACHE\n"
                +EnumChatFormatting.WHITE+"/dnscachemod reload"+EnumChatFormatting.GRAY+" RELOAD DNSCACHES";
    }

    public void sendCommandUsage(ICommandSender sender){
        sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            if(args.length==0){
                sendCommandUsage(sender);
                return;
            }
            switch (args[0].toLowerCase()){
                case "add": {
                    if(args.length!=3){
                        sendCommandUsage(sender);
                        return;
                    }
                    StringBuilder proJsonStr = new StringBuilder("[");
                    DNSData[] dnsData = new Gson().fromJson(FileUtils.readFile("./config/DNSCache.json"), DNSData[].class);
                    for (DNSData data : dnsData) {
                        proJsonStr.append("\n\t").append(data.toJson()).append(",");
                    }
                    proJsonStr.append("\n\t").append(new DNSData(args[1],args[2]).toJson());
                    proJsonStr.append("\n]");
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "ADDED!"));
                    FileUtils.writeFile("./config/DNSCache.json", proJsonStr.toString());
                    Main.reloadDNSCache();
                    break;
                }
                case "remove": {
                    if(args.length!=2){
                        sendCommandUsage(sender);
                        return;
                    }
                    boolean canFind = false;
                    StringBuilder proJsonStr = new StringBuilder("[");
                    DNSData[] dnsData = new Gson().fromJson(FileUtils.readFile("./config/DNSCache.json"), DNSData[].class);
                    for (DNSData data : dnsData) {
                        if (data.from.equals(args[1])) {
                            canFind = true;
                        }else{
                            proJsonStr.append("\n\t").append(data.toJson()).append(",");
                        }
                    }
                    proJsonStr.append("\n]");
                    if(canFind){
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "REMOVED"));
                        FileUtils.writeFile("./config/DNSCache.json", proJsonStr.toString());
                        Main.reloadDNSCache();
                    }else{
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "TARGET NOT FOUND!Try to use /dnscachemod list to find"));
                    }
                    break;
                }
                case "list": {
                    DNSData[] dnsData = new Gson().fromJson(FileUtils.readFile("./config/DNSCache.json"), DNSData[].class);
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"LIST OF DNSCACHES("+dnsData.length+")"));
                    for (DNSData data : dnsData) {
                        String fromIp = java.net.InetAddress.getByName(data.from).getHostAddress();
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "FROM:" + data.from + " FROMIP:" + fromIp + " TARGET:" + data.target));
                    }
                    break;
                }
                case "createsharelink":{
                    if(args.length!=3){
                        sendCommandUsage(sender);
                        return;
                    }
                    String shareLink="dnscache://",oriShareLink;
                    oriShareLink=args[1]+"!"+args[2];
                    Base64.Encoder encoder = Base64.getEncoder();
                    shareLink+=encoder.encodeToString(oriShareLink.getBytes());
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW+"Share link is:"+EnumChatFormatting.WHITE+shareLink));
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"Successfully copy to clipboard!"));
                    StringSelection selection = new StringSelection(shareLink);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, selection);
                    break;
                }
                case "loadsharelink":{
                    if(args.length!=2){
                        sendCommandUsage(sender);
                        return;
                    }
                    String base64ShareLink,oriShareLink;
                    String[] splitShareLink;
                    Base64.Decoder decoder = Base64.getDecoder();
                    if(args[1].length()<13){
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"INVALID SHARE LINK"));
                    }
                    if(!args[1].substring(0,11).equals("dnscache://")){
                        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"INVALID SHARE LINK"));
                    }
                    base64ShareLink=args[1].substring(11);
                    oriShareLink=new String(decoder.decode(base64ShareLink));
                    splitShareLink=oriShareLink.split("!");
                    String[] cmdArgs={"add",splitShareLink[0],splitShareLink[1]};
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW+"ADDING SHARE LINK:\n"+EnumChatFormatting.WHITE+"FROM:"+splitShareLink[0]+" TARGET:"+splitShareLink[1]));
                    this.processCommand(sender,cmdArgs);
                    break;
                }
                case "reload": {
                    Main.reloadDNSCache();
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Reloaded!"));
                    break;
                }
                default: {
                    sendCommandUsage(sender);
                    break;
                }
            }
        }catch (Exception e){
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "COMMAND PROCESS ERROR!STACKTRACE BELOW:"));
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + ExceptionUtils.getStackTrace(e)));
        }
    }
}
