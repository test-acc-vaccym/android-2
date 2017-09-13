package io.pcyan.drawersample;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        this.mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        this.mViewPager = (ViewPager) view.findViewById(R.id.view_pager);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_tabbar);
        toolbar.setTitle("DrawerSample");
        ((MainActivity)getActivity()).setToolBar(toolbar);



        setupTabTextColor();
        setupViewPager();
        return view;
    }


    private void setupTabTextColor() {
//        int tabTextColor = getResources().getColor(android.R.color.black);
//        mTabLayout.setTabTextColors(tabTextColor, tabTextColor);
    }

    private void setupViewPager() {
        //You could use the normal supportFragmentManger if you like
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(pagerAdapter);
        //建立ViewPager与Tab的联系
//        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mTabLayout.setupWithViewPager(mViewPager);

        //设置ViewPager滚动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //更改页面后，通知Activity更改菜单选中项
                ((MainActivity)getActivity()).setMenuItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setPagerItem(int item){
        mViewPager.setCurrentItem(item);
    }

}
