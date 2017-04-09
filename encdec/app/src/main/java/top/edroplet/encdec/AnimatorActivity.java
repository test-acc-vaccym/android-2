package top.edroplet.encdec;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimatorActivity extends Activity implements Animation.AnimationListener {
    AnimationDrawable animDraw;
    Resources res;
    Animation anim_alpha_in, anim_alpha_out, anim_scal_in, anim_scale_out, anim_translate, anim_rotate;
    private TextView tv;
    private Button btn_test;
    private int[] dIds = {R.drawable.anim001, R.drawable.anim002, R.drawable.anim003, R.drawable.anim004, R.drawable.anim005, R.drawable.anim006};
    private ImageView iv;

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
        btn_test.startAnimation(anim_scal_in);

        iv = (ImageView) findViewById(R.id.anim_iv);
        res = getResources();
        animDraw = new AnimationDrawable();
        for (int i = 0; i < dIds.length; i++) {
            animDraw.addFrame(res.getDrawable(dIds[i]), 100);
        }
        iv.setBackgroundDrawable(animDraw);
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
//            if(tv.getAnimation() == anim_alpha_in)
            tv.startAnimation(anim_alpha_out);
//            if(btn_test.getAnimation() == anim_alpha_in)
//                btn_test.startAnimation(anim_alpha_out);
            //anim_alpha_out.start();
        } else if (animation.hashCode() == anim_alpha_out.hashCode()) {
//            if(tv.getAnimation() == anim_alpha_out)
            tv.startAnimation(anim_rotate);
//            if(btn_test.getAnimation() == anim_alpha_out)
//                btn_test.startAnimation(anim_alpha_in);
            //anim_alpha_in.start();
        } else if (animation.hashCode() == anim_scal_in.hashCode()) {
            tv.startAnimation(anim_scale_out);
        } else if (animation.hashCode() == anim_scale_out.hashCode()) {
            tv.startAnimation(anim_rotate);
        } else if (animation.hashCode() == anim_rotate.hashCode()) {
            tv.startAnimation(anim_translate);
        } else if (animation.hashCode() == anim_translate.hashCode()) {
            tv.startAnimation(anim_alpha_in);
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
}
