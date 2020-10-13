package org.meditation.DNSCacheMod;

public class DNSData {
    public String from;
    public String target;
    public DNSData(String from,String target){
        this.from=from;
        this.target=target;
    }
    public String toJson(){
        return "{\"from\":\""+this.from+"\",\"target\":\""+this.target+"\"}";
    }
}
