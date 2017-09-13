package com.edroplet.qxs.wificommunication.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edroplet.qxs.wificommunication.R;
import com.edroplet.qxs.wificommunication.utils.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qxs on 2017/7/25.
 */

public class MessageListViewAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; //用来下载图片的类，后面有介绍

    // 所有的静态变量
    static final String URL = "http://api.androidhive.info/music/music.xml";//xml目的地址,打开地址看一下
    // XML 节点
    static final String KEY_SONG = "song"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_ARTIST = "artist";
    static final String KEY_DURATION = "duration";
    static final String KEY_THUMB_URL = "thumb_url";

    public MessageListViewAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_raw, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // 标题
        TextView artist = (TextView)vi.findViewById(R.id.artist); // 歌手名
        TextView duration = (TextView)vi.findViewById(R.id.duration); // 时长
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // 缩略图

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        // 设置ListView的相关值
        title.setText(song.get(KEY_TITLE));
        artist.setText(song.get(KEY_ARTIST));
        duration.setText(song.get(KEY_DURATION));
        imageLoader.DisplayImage(song.get(KEY_THUMB_URL), thumb_image);
        return vi;
    }
}
