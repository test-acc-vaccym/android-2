package top.edroplet.encdec.activities.animation;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import top.edroplet.encdec.R;
import top.edroplet.encdec.utils.util.ImageOperator;
import top.edroplet.encdec.utils.util.ImageOperator.*;

import static android.content.ContentValues.TAG;

public class AnimatorActivity extends Activity implements Animation.AnimationListener {
    AnimationDrawable animDraw;
    Resources res;
    Animation anim_alpha_in, anim_alpha_out, anim_scal_in, anim_scale_out, anim_translate, anim_rotate;
    private TextView tv;
    private Button btn_test;
    private int[] dIds = {R.drawable.anim001, R.drawable.anim002, R.drawable.anim003, R.drawable.anim004, R.drawable.anim005, R.drawable.anim006};
    private ImageView iv;
    private boolean isStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        tv = (TextView) findViewById(R.id.anim_tv);
        btn_test = (Button) findViewById(R.id.anim_btn_test);

        anim_alpha_in = AnimationUtils.loadAnimation(this, R.anim.tweened_anim_alpha_01);
        anim_alpha_out = AnimationUtils.loadAnimation(this, R.anim.tweened_anim_alpha_10);
        anim_rotate = AnimationUtils.loadAnimation(this, R.anim.tweened_anim_rotate);
        anim_scal_in = AnimationUtils.loadAnimation(this, R.anim.tweened_anim_scale_in);
        anim_scale_out = AnimationUtils.loadAnimation(this, R.anim.tweened_anim_scale_out);
        anim_translate = AnimationUtils.loadAnimation(this, R.anim.tweened_anim_trans);

        anim_translate.setAnimationListener(this);
        anim_scale_out.setAnimationListener(this);
        anim_scal_in.setAnimationListener(this);
        anim_rotate.setAnimationListener(this);
        anim_alpha_out.setAnimationListener(this);
        anim_alpha_in.setAnimationListener(this);
//        tv.setAnimation(anim_alpha_in);
        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(anim_alpha_in);
        animSet.addAnimation(anim_alpha_out);
        AnimationSet animSet2 = new AnimationSet(true);
        animSet2.addAnimation(anim_alpha_out);
        animSet2.addAnimation(anim_alpha_in);

        tv.startAnimation(anim_alpha_in);
        btn_test.startAnimation(anim_alpha_in);

        // frame to frame 动画
        iv = (ImageView) findViewById(R.id.anim_iv);
        res = getResources();
        animDraw = new AnimationDrawable();
        /*
        for (int i = 0; i < dIds.length; i++) {
            animDraw.addFrame(res.getDrawable(dIds[i]), 100);
        }
        */
        Bitmap resource = BitmapFactory.decodeResource(res, R.drawable.apic4383);
        // Bitmap leftImg = Bitmap.createBitmap(resource,0,0,645,1029);
        int width = resource.getWidth();
        int height = resource.getHeight();
        Bitmap rightImg = Bitmap.createBitmap(resource, width * 6 / 13, 0, width * 7 / 13, height);
        // String photoUrl = MediaStore.Images.Media.insertImage(this.getContentResolver(),leftImg,"bigDark","Big image of dark");
        // Log.i(TAG, "onCreate: photo url = "+photoUrl);
        ImageOperator io = new ImageOperator();
        for (ImagePiece ip : io.split(rightImg, 3, 5)) {
            Drawable draw = new BitmapDrawable(res, ip.bitmap);
            animDraw.addFrame(draw, 300);
        }
        iv.setBackground(animDraw);
        iv.postInvalidate();
        iv.setEnabled(true);
        iv.setMaxWidth(693);
        iv.setMinimumWidth(230);
        iv.setMaxHeight(210);
        iv.setMinimumHeight(200);
        animDraw.setVisible(true, true);
        animDraw.setOneShot(false);
        animDraw.start();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
//        System.out.println(tv.getAnimation());
//        System.out.println(btn_test.getAnimation());
//        System.out.println(animation.hashCode());
        if (animation.hashCode() == anim_alpha_in.hashCode()) {
            tv.startAnimation(anim_alpha_out);
            btn_test.startAnimation(anim_alpha_out);
        } else if (animation.hashCode() == anim_alpha_out.hashCode()) {
            tv.startAnimation(anim_scal_in);
            btn_test.startAnimation(anim_scal_in);
        } else if (animation.hashCode() == anim_scal_in.hashCode()) {
            tv.startAnimation(anim_scale_out);
            btn_test.startAnimation(anim_scale_out);
        } else if (animation.hashCode() == anim_scale_out.hashCode()) {
            tv.startAnimation(anim_rotate);
            btn_test.startAnimation(anim_rotate);
        } else if (animation.hashCode() == anim_rotate.hashCode()) {
            tv.startAnimation(anim_translate);
            btn_test.startAnimation(anim_translate);
        } else if (animation.hashCode() == anim_translate.hashCode()) {
            tv.startAnimation(anim_alpha_in);
            btn_test.startAnimation(anim_alpha_in);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animDraw.stop();
        animDraw.clearColorFilter();
    }

    public void toggleAnimDraw() {
        if (isStop) {
            isStop = false;
            animDraw.start();
        } else {
            isStop = true;
            animDraw.stop();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            toggleAnimDraw();
        }
        return super.onTouchEvent(event);
    }

}
