package com.xzy.ui.xtoast;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MiuiToast {
    private boolean mShowTime;
    private WindowManager mWdm;
    private boolean mIsShow;
    private View mToastView;
    private Timer mTimer;
    private WindowManager.LayoutParams mParams;

    private MiuiToast(Context context, String text, boolean showTime) {
        mShowTime = showTime;//记录Toast的显示长短类型
        mIsShow = false;//记录当前Toast的内容是否已经在显示
        mWdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //通过Toast实例获取当前android系统的默认Toast的View布局
        mToastView = Toast.makeText(context, text, Toast.LENGTH_SHORT).getView();
        mToastView.setBackgroundColor(context.getResources().getColor(R.color.colorAccent,null));
        mTimer = new Timer();
        //设置布局参数
        setParams();
    }

    public static MiuiToast MakeText(Context context, String text, boolean showTime) {
        return new MiuiToast(context, text, showTime);
    }


    private void setParams() {
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.MiuiToast;//设置进入退出动画效果
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.gravity = Gravity.BOTTOM;
        mParams.y = 250;
    }


    public void show() {
        if (!mIsShow) {//如果Toast没有显示，则开始加载显示
            mIsShow = true;
            mWdm.addView(mToastView, mParams);//将其加载到windowManager上
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mWdm.removeView(mToastView);
                    mIsShow = false;
                }
            }, (long) (mShowTime ? 3500 : 2000));
        }

    }
}
