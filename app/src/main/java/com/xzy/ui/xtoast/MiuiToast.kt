package com.xzy.ui.xtoast

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast

import java.util.Timer
import java.util.TimerTask

@SuppressLint("ShowToast")
/**
 * 参考了 https://www.cnblogs.com/net168/p/4237528.html
 * 但是去掉了 setParams 方法中的 mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
 * 否则在 Android7.0以上会闪退。
 * **/
class MiuiToast private constructor(
    context: Context,
    text: String,
    private val mShowTime: Boolean
) {
    private val mWdm: WindowManager
    private var mIsShow: Boolean = false
    private val mToastView: View
    private val mTimer: Timer
    private var mParams: WindowManager.LayoutParams? = null

    init {
        mIsShow = false//记录当前Toast的内容是否已经在显示
        mWdm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //通过Toast实例获取当前android系统的默认Toast的View布局
        mToastView = Toast.makeText(context, text, Toast.LENGTH_SHORT).view
        mToastView.setBackgroundColor(context.resources.getColor(R.color.colorAccent, null))
        mTimer = Timer()
        //设置布局参数
        setParams()
    }//记录Toast的显示长短类型


    private fun setParams() {
        mParams = WindowManager.LayoutParams()
        mParams!!.height = WindowManager.LayoutParams.WRAP_CONTENT
        mParams!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        mParams!!.format = PixelFormat.TRANSLUCENT
        mParams!!.windowAnimations = R.style.MiuiToast//设置进入退出动画效果
        mParams!!.flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        mParams!!.gravity = Gravity.BOTTOM
        mParams!!.y = 250
    }


    fun show() {
        if (!mIsShow) {//如果Toast没有显示，则开始加载显示
            mIsShow = true
            mWdm.addView(mToastView, mParams)//将其加载到windowManager上
            mTimer.schedule(object : TimerTask() {
                override fun run() {
                    mWdm.removeView(mToastView)
                    mIsShow = false
                }
            }, (if (mShowTime) 3500 else 2000).toLong())
        }

    }

    companion object {
        fun makeText(context: Context, text: String, showTime: Boolean): MiuiToast {
            return MiuiToast(context, text, showTime)
        }
    }
}
