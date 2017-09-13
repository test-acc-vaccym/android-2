package com.jianchi.fsp.buddhismnetworkradio;

import android.content.Context;
import android.content.res.Resources;

import com.alibaba.fastjson.JSON;
import com.jianchi.fsp.buddhismnetworkradio.api.Channel;
import com.jianchi.fsp.buddhismnetworkradio.api.Server;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fsp on 16-7-5.
 */
public class WebApi {

    /*
    节目时间表:
    视频音频播放器
    线路选择:http://www.amtb.tw/tvchannel/play-1-revised.asp
    最新讯息:http://www.amtb.tw/tvchannel/show_marquee.asp
    经文讲义:http://ft.hwadzan.com/mycalendar/mycalendar_embed_livetv.php?calendar_name=livetv
    * */

    private static final HashMap<String, String> programsListUrlMap = new HashMap<String, String>(){
        {
            put("淨空老法師直播",      "http://ft.hwadzan.com/mycalendar/mycalendar_embed.php?calendar_name=livetv&showview=day&valign=true&bgcolor=none&showtimecolumns=start&tvmenu=3");
            put("文化教育",            "http://ft.hwadzan.com/mycalendar/mycalendar_embed.php?calendar_name=cult&showview=day&valign=true&bgcolor=none&showtimecolumns=start&tvmenu=3");
            put("粵語節目",            "http://ft.hwadzan.com/mycalendar/mycalendar_embed.php?calendar_name=yueyu");
            put("台語節目",            "http://ft.hwadzan.com/mycalendar/mycalendar_embed.php?calendar_name=wdmaster");
            put("English",             "http://www.hwadzan.com/mycalendar/mycalendar_embed.php?calendar_name=English");
        }
    };

    //private static final String programsListUrl  = "http://ft.hwadzan.com/mycalendar/mycalendar_embed.php?calendar_name=livetv&showview=day&valign=true&bgcolor=none&showtimecolumns=start&tvmenu=3";
    //文化 http://ft.hwadzan.com/mycalendar/mycalendar_embed.php?calendar_name=cult&showview=day&valign=true&bgcolor=none&showtimecolumns=start&tvmenu=3
    //粤语 http://ft.hwadzan.com/mycalendar/mycalendar_embed.php?calendar_name=yueyu
    //台语 http://ft.hwadzan.com/mycalendar/mycalendar_embed.php?calendar_name=wdmaster
    //英语 http://www.hwadzan.com/mycalendar/mycalendar_embed.php?calendar_name=English
    /*
    * <table border='0'>
                            <tr>
                                <td bgcolor='#FFF4FF' class='style1'>
                                  <strong>18:55 - 21:00</strong>
<br />                                  Lian Fan&#039;s Four lessons(movie)<br />                                </td>
                            </tr>
                        </table>


   <table border='0'>
                            <tr>
                                <td class='style1'>
                                  <strong>00:00</strong>
                                  2014淨土大經科註 第352集                                </td>
                            </tr>


                        */


    //系念 图片不变

    private static final String serversListUrl  = "http://www.amtb.tw/tvchannel/play-1-revised.asp";
    private static final String newsListUrl  = "http://www.amtb.tw/tvchannel/show_marquee.asp";
    private static final String noteUrl  = "http://ft.hwadzan.com/mycalendar/mycalendar_embed_livetv.php?calendar_name=livetv";

    private static final int maxLoadTimes = 3;

    Pattern programsListPattern = Pattern.compile("<td [\\s\\S]*?</td>");
    Pattern programsListTablePattern = Pattern.compile("<table border='0'>[\\s\\S]*?</table>");
    Pattern programsListReplaceSpeacePattern = Pattern.compile("\\s+");
    Pattern htmlTagPattern = Pattern.compile("<[^>]*>");

    Pattern serversListPattern = Pattern.compile("serverAddress = \"(.*?)\";\\s*serverName = \"(.*?)\";");
    Pattern newsListPattern = Pattern.compile("<div id='bul_\\d'>(.*?)</div>");

    Pattern noteItemPattern = Pattern.compile("<p class=\"MsoNormal\"[^>]*>\\s*([\\s\\S]*?)\\s*</p>");
    Pattern selectServerCodePattern = Pattern.compile("selectServerCode = \"(.*?)\";");
    Pattern selectCityCodePattern = Pattern.compile("selectCityCode = \"(.*?)\";");

    private OkHttpClient client;
    IProgramsListEvent programsListEvent;
    IServersListEvent serversListEvent;
    INewsListEvent newsListEvent;
    IStringEvent noteEvent;
    int programsLoadTimes;
    int serversLoadTimes;
    int newsLoadTimes;
    int noteLoadTimes;

    Call getNoteCall;
    Call getProgramsListCall;

    private Context context;
    public WebApi(Context context){
        this.context=context;
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public void GetNote(IStringEvent noteEvent) {
        this.noteEvent = noteEvent;
        noteLoadTimes = 0;
        Request request = new Request.Builder()
                .url(noteUrl)
                .build();

        if(getNoteCall==null) {
            getNoteCall = client.newCall(request);
        }
        else if(getNoteCall.isExecuted()) {
            getNoteCall.cancel();
            getNoteCall = client.newCall(request);
        }

        getNoteCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.e("GetNote onFailure", e.getMessage());
                e.printStackTrace();
                if(noteLoadTimes<maxLoadTimes)//e.getCause().equals(SocketTimeoutException.class) &&
                {
                    noteLoadTimes++;
                    MyLog.w("GetNote onFailure", "e.getCause().equals(SocketTimeoutException.class) && noteLoadTimes<maxLoadTimes");
                    getNoteCall = client.newCall(call.request());
                    getNoteCall.enqueue(this);
                } else {
                    WebApi.this.noteEvent.getMsg(null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = new String(response.body().bytes(), "utf8");
                Matcher m = noteItemPattern.matcher(html);
                StringBuilder sb = new StringBuilder();
                while (m.find()){
                    String nm = m.group(1);
                    if(nm.startsWith("<")) {
                        Matcher hm = htmlTagPattern.matcher(nm);
                        nm = hm.replaceAll("");
                    }
                    nm = Decode(nm);
                    sb.append(nm).append("\r\n");
                }
                if(sb.length()==0)
                    MyLog.w("GetNote onResponse", html);

                WebApi.this.noteEvent.getMsg(sb.toString());
            }
        });
    }


    public void GetNewsList(INewsListEvent newsListEvent) {
        this.newsListEvent = newsListEvent;
        newsLoadTimes = 0;
        Request request = new Request.Builder()
                .url(newsListUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.e("GetNewsList onFailure", e.getMessage());
                e.printStackTrace();
                if(newsLoadTimes<maxLoadTimes)//e.getCause().equals(SocketTimeoutException.class) &&
                {
                    MyLog.w("GetNewsList onFailure", "e.getCause().equals(SocketTimeoutException.class) && noteLoadTimes<maxLoadTimes");
                    newsLoadTimes++;
                    client.newCall(call.request()).enqueue(this);
                }else {
                    WebApi.this.newsListEvent.getItems(null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = new String(response.body().bytes(), "big5");


                Matcher m = newsListPattern.matcher(html);
                List<String> newsList = new ArrayList<String>();
                while (m.find()){
                    String nm = m.group(1);
                    if(nm.startsWith("<")) {
                        Matcher hm = htmlTagPattern.matcher(nm);
                        nm = hm.replaceAll("");
                    }
                    newsList.add(nm);
                }
                if(newsList.size()==0)
                    MyLog.w("GetNewsList onResponse", html);
                WebApi.this.newsListEvent.getItems(newsList);
            }
        });
    }

    public void GetChannelList(IChannelListEvent iChannelListEvent) {
        ChannelList channelList = new ChannelList();
        channelList.channels=initChannels();
        channelList.selectedChannelTitle =channelList.channels.get(0).title;
        iChannelListEvent.getItems(channelList);
    }

    private List<Channel> initChannels(){
        String json = readRawFile(R.raw.channels);
        return JSON.parseArray(json, Channel.class);
    }

    private String readRawFile(int rawId)
    {
        String tag = "readRawFile";
        String content=null;
        Resources resources=context.getResources();
        InputStream is=null;
        try{
            is=resources.openRawResource(rawId);
            byte buffer[]=new byte[is.available()];
            is.read(buffer);
            content=new String(buffer);
            MyLog.i(tag, "read:"+content);
        }
        catch(IOException e)
        {
            MyLog.e(tag, e.getMessage());
        }
        finally
        {
            if(is!=null)
            {
                try{
                    is.close();
                }catch(IOException e)
                {
                    MyLog.e(tag, e.getMessage());
                }
            }
        }
        return content;
    }


    public void GetProgramsList(IProgramsListEvent programsListEvent, final String type) {

        this.programsListEvent = programsListEvent;

        if(type.equals("三時繫念法會")){
            String json = readRawFile(R.raw.ssxn);
            ProgramsList programsList = new ProgramsList();
            programsList.programs=JSON.parseArray(json, String.class);
            WebApi.this.programsListEvent.getItems(programsList);
            return;
        }

        String programsListUrl = programsListUrlMap.get(type);
        programsLoadTimes = 0;
        Request request = new Request.Builder()
                .url(programsListUrl)
                .build();

        if(getProgramsListCall==null) {
            getProgramsListCall = client.newCall(request);
        }
        else if(getProgramsListCall.isExecuted()) {
            getProgramsListCall.cancel();
            getProgramsListCall = client.newCall(request);
        }

        getProgramsListCall.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.e("GetProgramsList onFailure", e.getMessage());
                e.printStackTrace();
                if(programsLoadTimes<maxLoadTimes)//e.getCause().equals(SocketTimeoutException.class) &&
                {
                    MyLog.w("GetProgramsList onFailure", "noteLoadTimes<maxLoadTimes");
                    programsLoadTimes++;
                    getProgramsListCall = client.newCall(call.request());
                    getProgramsListCall.enqueue(this);
                }else {
                    WebApi.this.programsListEvent.getItems(null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = new String(response.body().bytes(), "utf8");

                ProgramsList programsList = parseProgramsList(html, type);
                WebApi.this.programsListEvent.getItems(programsList);
            }
        });
    }

    private ProgramsList parseProgramsList(String html, String type){

        String tr = "";
        Matcher trm = programsListTablePattern.matcher(html);
        String[] tbs = new String[7];
        int tbsi = 0;
        while (trm.find())
        {
            tbs[tbsi]=trm.group();
            tbsi++;
        }
        if(type.equals("淨空老法師直播") || type.equals("文化教育")){
            tr = tbs[0];
        }
        else {
            Calendar cal = Calendar.getInstance();
            int wk = cal.DAY_OF_WEEK;//1到7
            tr = tbs[wk-1];
        }

        Matcher m = programsListPattern.matcher(tr);
        ProgramsList programsList = new ProgramsList();
        while (m.find()){
            String fm = m.group();

            Matcher hm = htmlTagPattern.matcher(fm);
            fm = hm.replaceAll("");

            Matcher pm = programsListReplaceSpeacePattern.matcher(fm);
            fm = pm.replaceAll(" ");

            fm = Decode(fm);
            programsList.programs.add(fm.trim());
        }

        if(programsList.programs.size()==0)
            MyLog.w("GetProgramsList onResponse", html);

        return programsList;
    }

    public void GetServersList(IServersListEvent serversListEvent) {
        this.serversListEvent = serversListEvent;
        serversLoadTimes = 0;
        Request request = new Request.Builder()
                .url(serversListUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.e("GetServersList onFailure", e.getMessage());
                e.printStackTrace();
                if(serversLoadTimes<maxLoadTimes)//e.getCause().equals(SocketTimeoutException.class) &&
                {
                    MyLog.w("GetServersList onFailure", "e.getCause().equals(SocketTimeoutException.class) && noteLoadTimes<maxLoadTimes");
                    serversLoadTimes++;
                    client.newCall(call.request()).enqueue(this);
                }else {
                    WebApi.this.serversListEvent.getServers(null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = new String(response.body().bytes(), "big5");
                ServersList serverList = new ServersList();
                serverList.servers=initServers();

                Matcher mc1 = selectServerCodePattern.matcher(html);
                Matcher mc2 = selectCityCodePattern.matcher(html);
                if(mc1.find())
                    serverList.selectServerCode=mc1.group(1);
                if(mc2.find())
                    serverList.selectCityCode=mc2.group(1);

                //正则表达式解析那里也要进行错误处理
                if(serverList.servers.size()==0) {
                    WebApi.this.serversListEvent.getServers(null);
                    MyLog.w("GetServersList onResponse", html);
                } else {
                    WebApi.this.serversListEvent.getServers(serverList);
                }
            }
        });
    }

    private List<Server> initServers(){
        String json = readRawFile(R.raw.servers);
        return JSON.parseArray(json, Server.class);
    }
    /// <summary>
    ///解析html成 普通文本
    /// </summary>
    /// <param name="str">string</param>
    /// <returns>string</returns>
    public static String Decode(String str)
    {
        str = str.replace("<br>","\n");
        str = str.replace("&gt;",">");
        str = str.replace("&lt;","<");
        str = str.replace("&nbsp;"," ");
        str = str.replace("&quot;","\"");
        str = str.replace("&#039;","'");
        str = str.replace("&amp;","&");
        return str;
    }

}
