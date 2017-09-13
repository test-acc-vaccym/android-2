package com.jianchi.fsp.buddhismnetworkradio;

import android.net.Uri;

import com.jianchi.fsp.buddhismnetworkradio.api.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fsp on 16-7-5.
 */
public class ServersList {
    public List<Server> servers;
    public String selectServerCode="";
    public String  selectCityCode="";
    private Server selectedServer = null;

    public ServersList(){
        servers = new ArrayList<>();
    }

    public Server getServerByName(String serverName){
        for(Server si : servers)
            if(si.title.equals(serverName))
                return si;
        return null;
    }

    public void setSelectedServer(Server s){
        selectedServer = s;
    }

    public Server getSelectedServer(){
        if(selectedServer==null)
            selectedServer = getServerAuto();
        return selectedServer;
    }

    public Server getServerAuto(){

        if(selectServerCode.equals("TW")){
            return getServerByName("臺北");
        }else if(selectServerCode.equals("JP")){
            return getServerByName("日本");
        }else if(selectServerCode.equals("DE")){
            return getServerByName("德國");
        }else if(selectServerCode.equals("CN")){
            if(selectCityCode.equals("JIANGSU")){ //-- 江蘇
                return getServerByName("江蘇");
            }else if(selectCityCode.equals("SHANDONG")){ //-- 山東
                return getServerByName("山東");
            }else{   //-- 北京
                return getServerByName("北京");
            }
        }else{	//-- default 北京
            return getServerByName("北京");
        }
    }
}
