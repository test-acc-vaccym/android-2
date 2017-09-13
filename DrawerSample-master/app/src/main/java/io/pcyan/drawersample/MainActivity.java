package io.pcyan.drawersample;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private ListView mLvMenu;

    private
    @IdRes
    int mCurrentMenuItem;
    private android.support.design.widget.NavigationView mNavigationview;
    private android.support.v4.widget.DrawerLayout mDrawerlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initView
        this.mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mNavigationview = (NavigationView) findViewById(R.id.navigation_view);

        //设置菜单点击监听
        mNavigationview.setNavigationItemSelectedListener(this);

        mLvMenu = (ListView) findViewById(R.id.right_nav_menu);
        mLvMenu.setAdapter(new MenuItemAdapter(this));
        mLvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setTab(position);
                switch (position){
                    case 0:
                        break;
                }
            }
        });

        mNavigationview.setItemBackground(getResources().getDrawable(R.drawable.drawer_menu_item_background));
        //initFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new MainFragment(), "Main")
                .commit();
        mCurrentMenuItem = R.id.menu_item_1;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        @IdRes int id = menuItem.getItemId();
        if (id == mCurrentMenuItem) {
            //点击了当前的Tab则直接关闭
            mDrawerlayout.closeDrawers();
            return false;
        }
        //切换Tab
        switch (id) {
            case R.id.menu_item_1:
                setTab(0);
                break;
            case R.id.menu_item_2:
                setTab(1);
                break;
            case R.id.menu_item_3:
                setTab(2);
                break;
            case R.id.menu_item_4:
                setTab(3);
                break;
            case R.id.menu_item_5:
                setTab(4);
                break;
            default:
                break;
        }
        mCurrentMenuItem = id;
        menuItem.setChecked(true);
        //关闭侧滑菜单
        mDrawerlayout.closeDrawers();
        return true;
    }

    /**
     * 设置Tab
     * @param currentItem 要设置的Tab序号 index 0
     */
    private void setTab(int currentItem) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Main");
        if(fragment instanceof MainFragment){
            ((MainFragment)fragment).setPagerItem(currentItem);
        }
    }

    /**
     * set menu item check
     * @param position index 0
     */
    public void setMenuItem(int position){
        @IdRes int id = 0;
        switch (position){
            case 0:
                id = R.id.menu_item_1;
                break;
            case 1:
                id = R.id.menu_item_2;
                break;
            case 2:
                id = R.id.menu_item_3;
                break;
            case 3:
                id = R.id.menu_item_4;
                break;
            case 4:
                id = R.id.menu_item_5;
                break;
            default:
                break;
        }
        if(id!=0){
            mNavigationview.setCheckedItem(id);
            mCurrentMenuItem = id;
        }
    }

    public void setToolBar(Toolbar toolBar){
        if (toolBar != null) {
//            setSupportActionBar(toolBar);
            final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,mDrawerlayout,toolBar,R.string.app_name,R.string.app_name);
            mDrawerlayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
        }
    }
}
