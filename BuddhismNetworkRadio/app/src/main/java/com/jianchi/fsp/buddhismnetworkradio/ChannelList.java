package com.jianchi.fsp.buddhismnetworkradio;

import android.content.res.Resources;

import com.alibaba.fastjson.JSON;
import com.jianchi.fsp.buddhismnetworkradio.api.Channel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by fsp on 16-7-22.
 */
public class ChannelList {

    public String selectedChannelTitle;

    List<Channel> channels;

    Channel getChannelByTitle(String title){
        for(Channel c : channels)
            if(c.title.equals(title))
                return c;
        return null;
    }

    Channel getSelectedChannel(){
        return getChannelByTitle(selectedChannelTitle);
    }
}
