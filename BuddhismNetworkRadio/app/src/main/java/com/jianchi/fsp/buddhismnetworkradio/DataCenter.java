package com.jianchi.fsp.buddhismnetworkradio;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.alibaba.fastjson.JSON;
import com.jianchi.fsp.buddhismnetworkradio.api.Channel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by fsp on 16-7-6.
 */
public class DataCenter {
    private ProgramsList programs;
    private ChannelList channels;
    private List<String> news;
    private ServersList servers;
    private String note;

    private boolean programsBack;
    private boolean newsBack;
    private boolean serversBack;
    private boolean noteBack;
    private boolean channelBack;

    public DataCenter(){
        programsBack = false;
        newsBack = false;
        serversBack = false;
        noteBack=false;
        channelBack=false;
    }

    public boolean allSetValue(){
        return programs!=null && news!=null && servers!=null && note!=null && channels!=null;
    }

    public boolean allBack(){
        return programsBack && newsBack && serversBack && noteBack && channelBack;
    }

    public void setPrograms(ProgramsList programs) {
        programsBack = true;
        this.programs = programs;
    }

    public void setNews(List<String> news) {
        newsBack = true;
        this.news = news;
    }

    public void setServers(ServersList servers) {
        serversBack = true;
        this.servers = servers;
    }

    public void setNote(String note) {
        noteBack = true;
        this.note = note;
    }

    public void setChannels(ChannelList channels) {
        channelBack=true;
        this.channels = channels;
    }

    public ProgramsList getPrograms() {
        return programs;
    }
    public List<String> getNews() {
        return news;
    }
    public ChannelList getChannels() {
        return channels;
    }
    public ServersList getServers() {
        return servers;
    }
    public String getNote() {
        return note;
    }

    public Uri getSoundUriAuto(){
        String url = channels.getChannelByTitle(channels.getSelectedChannel().title).audioUrl;
        Uri uri = Uri.parse(url.replace("server", servers.getSelectedServer().domain));
        return uri;
    }

    public Uri getVideoUriAuto(){
        String url = channels.getChannelByTitle(channels.getSelectedChannel().title).tvUrl;
        Uri uri = Uri.parse(url.replace("server", servers.getSelectedServer().domain));
        return uri;
    }
}
