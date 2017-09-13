package cn.hugeterry.coordinatortablayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.lang.reflect.Field;

import cn.hugeterry.coordinatortablayout.adapter.MenuItemAdapter;
import cn.hugeterry.coordinatortablayout.listener.LoadHeaderImagesListener;
import cn.hugeterry.coordinatortablayout.utils.SystemView;
import cn.hugeterry.coordinatortablayout.view.EdropletToolbar;

/**
 * @author hugeterry(http://hugeterry.cn)
 */

public class CoordinatorTabLayout extends CoordinatorLayout {
    private int[] mImageArray, mColorArray;

    private Context mContext;
    private EdropletToolbar mToolbar;
    private ActionBar mActionbar;
    private TabLayout mTabLayout;
    // private ImageView mImageView;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private ListView mLvMenu;
    private
    @IdRes
    int mCurrentMenuItem;

    private Button operatorExplodedButton;
    private Button operatorFoldedButton;
    private Button operatorManualButton;
    private Activity homeActivity;
    private OnClickListener clickListener;

    private NiftyDialogBuilder dialogBuilder;

    public CoordinatorTabLayout(Context context) {
        super(context);
        mContext = context;
    }

    public CoordinatorTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (!isInEditMode()) {
            initView(context);
            initWidget(context, attrs);
        }
    }

    public CoordinatorTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (!isInEditMode()) {
            initView(context);
            initWidget(context, attrs);
        }
    }

    private void initView(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_coordinatortablayout, this, true);
        // 初始化 toolbar
        initToolbar();

        // 初始化底部导航栏
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);

        // 初始化右侧导航栏
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mCurrentMenuItem = R.id.nav_monitor;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                @IdRes int id = menuItem.getItemId();
                if (id == mCurrentMenuItem) {
                    //点击了当前的Tab则直接关闭
                    mDrawerlayout.closeDrawers();
                    return false;
                }
                /*
                switch (id) {
                    case R.id.nav_monitor:
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
                */

                if (id == R.id.nav_monitor) {
                    // Handle the camera action
                } else if (id == R.id.nav_communication) {
                    Toast.makeText(mContext.getApplicationContext(),"nav_communication clicked",Toast.LENGTH_LONG).show();
                } else if (id == R.id.nav_system_settings) {

                } else if (id == R.id.nav_satellite) {

                } else if (id == R.id.nav_calibration) {

                } else if (id == R.id.nav_manual) {

                } else if (id == R.id.nav_power) {

                } else if (id == R.id.nav_system_info) {

                } else if (id == R.id.nav_database) {

                } else if (id == R.id.nav_logs) {

                } else if (id == R.id.nav_update) {

                } else if (id == R.id.nav_help) {
                    Snackbar.make(null,"help clicked", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                mCurrentMenuItem = id;
                menuItem.setChecked(true);
                //关闭侧滑菜单
                mDrawerlayout.closeDrawers();
                return true;
            }
        });
        View navHead = navigationView.getHeaderView(0);
        if (navHead != null) {
            navHead.findViewById(R.id.right_nav_header_imageView).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO  点击事件
                    Toast.makeText(mContext, "right_nav_header_imageView clicked", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(getRootView(), "right_nav_header_imageView clicked", Snackbar.LENGTH_INDEFINITE)
                    //       .setAction("Action", null).show();
                }
            });
        } else {
            navigationView.findViewById(R.id.right_nav_header_imageView).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO  点击事件
                    Toast.makeText(mContext, "right_nav_header_imageView clicked", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(getRootView(), "right_nav_header_imageView clicked", Snackbar.LENGTH_INDEFINITE)
                    //        .setAction("Action", null).show();
                }
            });
        }

        Button right = (Button) findViewById(R.id.edroplet_toolbar_right_button);
        if (right != null) {
            right.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerlayout.openDrawer(GravityCompat.END);
                }
            });
        }
        Button left = (Button) findViewById(R.id.edroplet_toolbar_left_button);
        if (left != null){
            left.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeActivity != null){
                        Intent intent = new Intent(mContext, homeActivity.getClass());
                        mContext.startActivity(intent);
                    }else{
                        Toast.makeText(mContext,"No Home View!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        operatorExplodedButton = (Button)findViewById(R.id.button_operator_exploded);
        operatorFoldedButton  = (Button) findViewById(R.id.button_operator_folded);
        operatorManualButton = (Button) findViewById(R.id.button_operator_manual);
        operatorExplodedButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirm("确定打开？", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogBuilder != null)  dialogBuilder.dismiss();
                    }
                });
            }
        });
        operatorFoldedButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"收藏",Toast.LENGTH_SHORT).show();
            }
        });
        operatorManualButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"手动",Toast.LENGTH_SHORT).show();
            }
        });
        setUpDrawer();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    private boolean onConfirm(String message, View.OnClickListener listener){
        String []type = {"Fadein", "Slideleft", "Slidetop", "SlideBottom", "Slideright", "Fall", "Newspager", "Fliph", "Flipv", "RotateBottom", "RotateLeft", "Slit", "Shake", "Sidefill"};
        int i= (int) (type.length*Math.random());
        Effectstype effect = null;
        switch (i){
            case 0:effect=Effectstype.Fadein;break;
            case 1:effect=Effectstype.Slideright;break;
            case 2:effect=Effectstype.Slideleft;break;
            case 3:effect=Effectstype.Slidetop;break;
            case 4:effect=Effectstype.SlideBottom;break;
            case 5:effect=Effectstype.Newspager;break;
            case 6:effect=Effectstype.Fall;break;
            case 7:effect=Effectstype.Sidefill;break;
            case 8:effect=Effectstype.Fliph;break;
            case 9:effect=Effectstype.Flipv;break;
            case 10:effect=Effectstype.RotateBottom;break;
            case 11:effect=Effectstype.RotateLeft;break;
            case 12:effect=Effectstype.Slit;break;
            case 13:effect= Effectstype.Shake;break;
        }
        dialogBuilder = NiftyDialogBuilder.getInstance(mContext);
        dialogBuilder
                // 重点设置
                .withEffect(effect)        //设置对话框弹出样式
                //.setCustomView(R.layout.custom, MainActivity.this) // 设置自定义对话框的布局
                .withDuration(300)              //动画显现的时间（时间长就类似放慢动作）
                // 基本设置
                .withTitle(mContext.getString(R.string.operate_confirm_title))         //设置对话框标题
                .withTitleColor(ContextCompat.getColor(mContext, R.color.operate_confirm_title_color))          //设置标题字体颜色
                .withDividerColor(ContextCompat.getColor(mContext, R.color.operate_confirm_divider_color))      //设置分隔线的颜色
                .withMessage(message)//设置对话框显示内容
                .withMessageColor(ContextCompat.getColor(mContext, R.color.operate_confirm_message_color))       //设置消息字体的颜色
                .withDialogColor(ContextCompat.getColor(mContext, R.color.operate_confirm_dialog_color))        //设置对话框背景的颜色
                //.withIcon(getResources().getDrawable(R.drawable.logo)) //设置标题的图标
                // 设置是否模态，默认false，表示模态，
                //要求必须采取行动才能继续进行剩下的操作 | isCancelable(true)
                .isCancelableOnTouchOutside(true)
                .withButton1Text(mContext.getString(R.string.operate_confirm_ok))             //设置按钮1的文本
                .withButton2Text(mContext.getString(R.string.operate_confirm_cancel))         //设置按钮2的文本
                .setButton1Click(listener)
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), v.getContext().getString(R.string.operate_confirm_cancel_toast), Toast.LENGTH_SHORT).show();
                        dialogBuilder.dismiss();
                    }
                })
                .show();
        return true;
    }

    private void setUpDrawer() {
        mLvMenu = (ListView) findViewById(R.id.right_nav_menu);
        mLvMenu.setAdapter(new MenuItemAdapter(mContext));
        mLvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Log.e("CCCCC", "onItemClick: 0");
                        Toast.makeText(mContext,"monitor",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(mContext,String.valueOf(position),Toast.LENGTH_SHORT).show();
                        Log.e("CCCCC", "onItemClick: " + position);
                        break;
                }
            }
        });
    }

    public void  SetUpdateInfo(String info){
        //Create these objects above OnCreate()of your main activity
        TextView updatePrompt;
        //These lines should be added in the OnCreate() of your main activity
        updatePrompt =(TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_update));
        //This method will initialize the count value
        // initializeCountDrawer();
        updatePrompt.setGravity(Gravity.CENTER_VERTICAL);
        updatePrompt.setTypeface(null, Typeface.BOLD);
        updatePrompt.setTextColor(getResources().getColor(R.color.colorAccent));
        updatePrompt.setText(info);
    }

    public void setHomeActivity(Activity activity){
        this.homeActivity = activity;
    }

    public void setOnclickListener(OnClickListener listener){
        this.clickListener = listener;
    }

    private void initWidget(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs
                , R.styleable.CoordinatorTabLayout);

        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        //        int contentScrimColor = typedArray.getColor(
        //                R.styleable.CoordinatorTabLayout_contentScrim, typedValue.data);
        //        mCollapsingToolbarLayout.setContentScrimColor(contentScrimColor);

        int tabIndicatorColor = typedArray.getColor(R.styleable.CoordinatorTabLayout_tabIndicatorColor, Color.WHITE);
        mTabLayout.setSelectedTabIndicatorColor(tabIndicatorColor);

        int tabTextColor = typedArray.getColor(R.styleable.CoordinatorTabLayout_tabTextColor, Color.WHITE);
        mTabLayout.setTabTextColors(ColorStateList.valueOf(tabTextColor));
        typedArray.recycle();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
    }


    private void initToolbar() {
        mToolbar = (EdropletToolbar) findViewById(R.id.content_toolbar);
        // ((AppCompatActivity) mContext).setSupportActionBar(mToolbar);
        // mActionbar = ((AppCompatActivity) mContext).getSupportActionBar();
        //        mActionbar.setHomeAsUpIndicator(R.drawable.menu);
        //        mActionbar.setDisplayHomeAsUpEnabled(true);

    }

    /**
     * 设置Toolbar标题
     *
     * @param title 标题
     * @return
     */
    public CoordinatorTabLayout setTitle(String title) {
        TextView toolBarTitle = (TextView)findViewById(R.id.edroplet_toolbar_title);
        toolBarTitle.setText(title);
        if (mActionbar != null) {
            //mActionbar.setDisplayHomeAsUpEnabled(true);
            mActionbar.setDisplayShowTitleEnabled(false);
            //mActionbar.setDisplayOptions(1);
        }
        return this;
    }

    /**
     * 设置Toolbar显示返回按钮及标题
     *
     * @param canBack 是否返回
     * @return
     */
    public CoordinatorTabLayout setBackEnable(Boolean canBack) {
        if (canBack && mActionbar != null) {
            mActionbar.setDisplayHomeAsUpEnabled(true);
            mActionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_white_24dp);
        }
        return this;
    }

    /**
     * 设置每个tab对应的头部图片
     *
     * @param imageArray 图片数组
     * @return
     */
    public CoordinatorTabLayout setImageArray(@NonNull int[] imageArray) {
        mImageArray = imageArray;
        setupTabLayout();
        return this;
    }

    /**
     * 设置每个tab对应的头部照片和ContentScrimColor
     *
     * @param imageArray 图片数组
     * @param colorArray ContentScrimColor数组
     * @return
     */
    public CoordinatorTabLayout setImageArray(@NonNull int[] imageArray, @NonNull int[] colorArray) {
        mImageArray = imageArray;
        mColorArray = colorArray;
        setupTabLayout();
        return this;
    }

    /**
     * 设置每个tab对应的ContentScrimColor
     *
     * @param colorArray 图片数组
     * @return
     */
    public CoordinatorTabLayout setContentScrimColorArray(@NonNull int[] colorArray) {
        mColorArray = colorArray;
        return this;
    }

    private void setupTabLayout() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                /*
                mImageView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_dismiss));
                if (mLoadHeaderImagesListener == null) {
                    if (mImageArray != null) {
                        mImageView.setImageResource(mImageArray[tab.getPosition()]);
                    }
                } else {
                    mLoadHeaderImagesListener.loadHeaderImages(mImageView, tab);
                }
                if (mColorArray != null) {
                    mCollapsingToolbarLayout.setContentScrimColor( ContextCompat.getColor(mContext, mColorArray[tab.getPosition()]));
                }
                // mImageView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_show));

                */
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * 设置与该组件搭配的ViewPager
     *
     * @param viewPager 与TabLayout结合的ViewPager
     * @return
     */
    public CoordinatorTabLayout setupWithViewPager(ViewPager viewPager) {
        mTabLayout.setupWithViewPager(viewPager);
        return this;
    }

    /**
     * 获取该组件中的ActionBar
     */
    public ActionBar getActionBar() {
        return mActionbar;
    }

    /**
     * 获取该组件中的TabLayout
     */
    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    /**
     * 获取该组件中的ImageView
     */
    /*public ImageView getImageView() {
        return mImageView;
    }
    */
    /**
     * 设置LoadHeaderImagesListener
     *
     * @param loadHeaderImagesListener 设置LoadHeaderImagesListener
     * @return
     */
    public CoordinatorTabLayout setLoadHeaderImagesListener(LoadHeaderImagesListener loadHeaderImagesListener) {
        // mLoadHeaderImagesListener = loadHeaderImagesListener;
        setupTabLayout();
        return this;
    }

    /**
     * 设置透明状态栏
     *
     * @param activity
     * @return
     */
    public CoordinatorTabLayout setTransulcentStatusBar(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return this;
        }
        /*else {
            mToolbar.setPadding(0, SystemView.getStatusBarHeight(activity)/2, 0, 0);
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        return this;
    }


    /**
     * @description: 设置添加Tab
     */
    public CoordinatorTabLayout setTabsView(int[] tabTitlees, int[] tabImgs){
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.removeAllTabs();
        if (tabImgs == null || tabImgs.length == 0) {
            for (int i = 0; i < tabTitlees.length; i++) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                TabLayout.Tab tab = mTabLayout.newTab().setText(tabTitlees[i]);
                mTabLayout.addTab(tab);
            }
        }else if (tabImgs.length == tabTitlees.length){
            // 添加图标
            for (int i = 0; i < tabTitlees.length; i++) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                TabLayout.Tab tab = mTabLayout.newTab().setText(tabTitlees[i]).setIcon(tabImgs[i]);
                mTabLayout.addTab(tab);
            }
        }
        return this;
    }

    /**
     * @description 监听菜单
     */
    public boolean onCreateOptionsMenu(Menu menu){
        //如果drawerlayout没有关闭，则点击返回键应首先将其关闭
        if (mDrawerlayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerlayout.closeDrawer(GravityCompat.END);
        }else{
            mDrawerlayout.openDrawer(GravityCompat.END);
        }
        // resetItemLayout();
        return true;
    }

    /**
     * @description 自定义itemview
     */

    public void resetItemLayout() {
        //通过反射拿到menu的item布局。修改布局参数
        try {
            Field mPresenter = NavigationView.class.getDeclaredField("mPresenter");
            mPresenter.setAccessible(true);
            //此处mPresenter.get(navigationView)会得到一个NavigationMenuPresenter对象，但是该类是@hide的。所以此处直接再拿其内部的NavigationMenuView。该类也是@hide的。需要注意的是，该类继承自RecyclerView。菜单的布局也就是由其完成的。
            Field mMenuView = mPresenter.get(navigationView).getClass().getDeclaredField("mMenuView");
            mMenuView.setAccessible(true);
            Field mMenu = mPresenter.get(navigationView).getClass().getDeclaredField("mMenu");
            mMenu.setAccessible(true);
            //Field mItems = mMenuView.get(mMenu).getClass().getDeclaredField("mItems");
            //mItems.setAccessible(true);
            // Class al = (ArrayList) mItems.getType();
            //由于NavigationMenuView是隐藏类。此处用其父类。
            RecyclerView recycler = (RecyclerView) mMenuView.get(mPresenter.get(navigationView));
            for (int i = 0; i < recycler.getAdapter().getItemCount(); i++) {
                    RecyclerView.ViewHolder holder = recycler.findViewHolderForLayoutPosition(i);
                    //这里看实际项目了。我的项目中添加了一个head。
                    if (i == 0 || holder == null)//因为这里有一个header。所以要先排除第一个
                        continue;
                    //剩下的就是修改整体布局参数了。
                    ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = getResources().getDimensionPixelOffset(R.dimen.drawer_item_height);
                    holder.itemView.setLayoutParams(params);
                    if (i % 2 == 0) {
                        holder.itemView.setBackgroundColor(((AppCompatActivity) mContext).getResources().getColor(R.color.tab_checked));
                    } else {
                        holder.itemView.setBackgroundColor(((AppCompatActivity) mContext).getResources().getColor(R.color.tab_unchecked));
                    }

            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /***
     * 这里修改单个item的标题
     * setItemCounter(5,R.id.nav_monitor)
     */
    private void setItemCounter(int count,int resId){
        Menu menuNav = navigationView.getMenu();
        MenuItem element_nav_monitor = menuNav.findItem(resId);
        String before = element_nav_monitor.getTitle().toString();
        String counter = String.valueOf(count);
        String s = before + "   "+counter+" ";
        SpannableString sColored = new SpannableString( s );

        sColored.setSpan(new BackgroundColorSpan( Color.GRAY ), s.length()-(counter.length()+2), s.length(), 0);
        sColored.setSpan(new ForegroundColorSpan( Color.WHITE ), s.length()-(counter.length()+2), s.length(), 0);
        element_nav_monitor.setTitle(sColored);
    }
}