package cn.hugeterry.coordinatortablayout.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.hugeterry.coordinatortablayout.R;


/**
 * Created by qxs on 2017/8/29.
 */

public class EdropletNavigationView extends ListView {

    private Context mContext;
    private DrawerLayout mDrawerLayout;
    private ListView mLvMenu;
    private static int oddBackgroundColor = Color.GRAY;
    private static int evenBackgroundColor = Color.BLUE;

    public EdropletNavigationView(Context context) {
        super(context);
        mContext = context;
    }

    public EdropletNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.EdropletNavigationView);
        if (attributes != null) {
            oddBackgroundColor = attributes.getResourceId(R.styleable.EdropletNavigationView_lv_background_odd, R.color.bottom_tab_nav_background);
            evenBackgroundColor = attributes.getResourceId(R.styleable.EdropletNavigationView_lv_background_even, R.color.bottom_tab_nav_background);
        }
    }

    public EdropletNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.EdropletNavigationView);
        if (attributes != null) {
            oddBackgroundColor = attributes.getResourceId(R.styleable.EdropletNavigationView_lv_background_odd, R.color.bottom_tab_nav_background);
            evenBackgroundColor = attributes.getResourceId(R.styleable.EdropletNavigationView_lv_background_even, R.color.bottom_tab_nav_background);
        }
    }

    public static class EdropetListViewAdapter extends BaseAdapter implements ListAdapter {
        private List<String> data;
        private int layout;
        private Context context;
        private ImageView iv = null;
        private TextView tv = null;

        public EdropetListViewAdapter(List<String> data, int layout, Context context) {
            this.data = data;
            this.layout = layout;
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return data.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(layout, null);

                /*
                iv = (ImageView) view.findViewById(R.id.iv);
                tv = (TextView) view.findViewById(R.id.tv);

                view.setTag(new ObjectClass(iv, tv));
                */
            }
            /*else {
                ObjectClass objectclass = (ObjectClass) view.getTag();
                iv = objectclass.iv;
                tv = objectclass.text;
            }
            if (data.get(arg0).length() <= 3)
                iv.setImageResource(R.drawable.p_1);
            else if (data.get(arg0).length() <= 5)
                iv.setImageResource(R.drawable.p_2);
            else if (data.get(arg0).length() > 5)
                iv.setImageResource(R.drawable.p_3);
            tv.setText(data.get(arg0));
            */

            if (position % 2 == 0){
                view.setBackgroundColor(oddBackgroundColor);
            }else {
                view.setBackgroundColor(evenBackgroundColor);
            }
            return view;
        }

        private final class ObjectClass {
            ImageView iv = null;
            TextView text = null;

            public ObjectClass(ImageView iv, TextView text) {
                this.iv = iv;
                this.text = text;
            }
        }
    }
}
