## Android  Toast 的通用实现

### 演示效果

![演示效果](https://github.com/hgncxzy/XToast/blob/master/gif/GIF.gif)

### 兼容性

网络上关于使用反射来实现带动画的 Toast 的那种实现方式，在 Android 7.0 只有动画效果失效，目前我还没找到很好的解决方案（具体可以查看源码中 ToastManager 类，该类中的动画在 Android7.0 失效）。以下封装类已经解决了兼容性问题，测试环境是  Android 6.0、Android 8.1，没发现问题。封装类中的实现原理，请看[这篇文章](https://www.cnblogs.com/net168/p/4237528.html)

### 封装类

```kotlin
package com.xzy.ui.xtoast

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.widget.Toast
import android.widget.ImageView
import android.widget.TextView
import android.app.Activity
import android.graphics.PixelFormat
import android.os.Handler
import android.text.Html
import android.view.View
import android.view.WindowManager
import androidx.annotation.DrawableRes
import java.util.*


@Suppress("DEPRECATION")
object XToast {

    private var toast: Toast? = null
    private var imgToast: Toast? = null
    private var textToast: Toast? = null
    private val handler = Handler()

    /**
     * 线程安全的 Toast
     *
     * */
    fun showToast(ctx: Context, showMsg: String) {
        handler.post { Toast.makeText(ctx, showMsg, Toast.LENGTH_SHORT).show() }
    }


    /**
     * 默认的 Toast
     *
     * */
    fun showDefaultToast(ctx: Context, showMsg: String) {
        Toast.makeText(ctx, showMsg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 自定义位置的 Toast
     *
     * */
    fun showCustomPositionToast(ctx: Context, showMsg: String) {
        val toast = Toast.makeText(ctx, showMsg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 66, 80)
        toast.view.setBackgroundColor(ctx.resources.getColor(R.color.colorPrimary))
        toast.show()
    }

    /**
     * 自定义位置的 Toast
     *
     * */
    fun showCustomBgToast(ctx: Context, showMsg: String) {
        val temp = "<font color=\"#FFFFFF\">$showMsg</font>"
        val text = Html.fromHtml(temp)
        val toast = Toast.makeText(ctx, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.view.setBackgroundColor(ctx.resources.getColor(R.color.colorPrimary))
        toast.show()
    }

    /**
     * 带图片的 Toast -- 自定义布局
     * **/
    @SuppressLint("InflateParams")
    fun showToastWithImg(activity: Activity, content: String, @DrawableRes imageResource: Int) {
        cancel()
        imgToast = Toast(activity)
        val view = activity.layoutInflater.inflate(R.layout.toast_with_img, null)
        val tvContent = view.findViewById(R.id.tv_content) as TextView
        val ivImage = view.findViewById(R.id.tv_image) as ImageView
        tvContent.text = content
        ivImage.setImageResource(imageResource)
        imgToast?.view = view
        imgToast?.setGravity(Gravity.CENTER, 0, 0)
        toast = imgToast
        toast?.show()
    }

    /**
     * 自定义文本 Toast -- 自定义布局
     * */
    @SuppressLint("ResourceType", "InflateParams")
    fun showCustomToast(activity: Activity, content: String) {
        cancel()
        textToast = Toast.makeText(activity, content, Toast.LENGTH_SHORT)
        val view = activity.layoutInflater.inflate(R.layout.toast, null)
        textToast?.view = view
        textToast?.setGravity(Gravity.CENTER, 0, 0)
        textToast?.setText(content)
        toast = textToast
        toast?.show()
    }


    @SuppressLint("ShowToast")
    /**
     *
     * 使用系统 Toast 的布局，并带动画效果
     * */
    fun showToastWithAnim(context: Context, changeShowTime: Boolean, text: String) {
        val wdm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var isShow = false
        // 设置布局，可以自定义
        val toastView = Toast.makeText(context, text, Toast.LENGTH_SHORT).view
        toastView.setBackgroundColor(context.resources.getColor(R.color.colorAccent, null))
        val timer = Timer()
        val layoutParams = WindowManager.LayoutParams()

        // 设置参数
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.format = PixelFormat.TRANSLUCENT
        layoutParams.windowAnimations = R.style.MiuiToast//设置进入退出动画效果
        layoutParams.flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        layoutParams.gravity = Gravity.BOTTOM
//        mParams.y = 250

        // 设置展示
        if (!isShow) {//如果Toast没有显示，则开始加载显示
            isShow = true
            wdm.addView(toastView, layoutParams)//将其加载到windowManager上
            timer.schedule(object : TimerTask() {
                override fun run() {
                    wdm.removeView(toastView)
                    isShow = false
                }
            }, (if (changeShowTime) 3500 else 2000).toLong())
        }

    }

    @SuppressLint("ShowToast", "InflateParams")
    /**
     *
     * 使用自定义布局，并带图片和动画效果
     * */
    fun showToastWithAnimAndImg(activity: Activity, content: String, imageResource: Int) {
        val wdm = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var isShow = false
        // 设置布局，可以自定义
        val toastView = activity.layoutInflater.inflate(R.layout.toast_with_img, null)
        val tvContent = toastView.findViewById(R.id.tv_content) as TextView
        val ivImage = toastView.findViewById(R.id.tv_image) as ImageView
        tvContent.text = content
        ivImage.setImageResource(imageResource)
        val timer = Timer()
        val layoutParams = WindowManager.LayoutParams()

        // 设置参数
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.format = PixelFormat.TRANSLUCENT
        layoutParams.windowAnimations = R.style.MiuiToast//设置进入退出动画效果
        layoutParams.flags = (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        layoutParams.gravity = Gravity.CENTER

        // 设置展示
        if (!isShow) {//如果Toast没有显示，则开始加载显示
            isShow = true
            wdm.addView(toastView, layoutParams)//将其加载到windowManager上
            timer.schedule(object : TimerTask() {
                override fun run() {
                    wdm.removeView(toastView)
                    isShow = false
                }
            }, 2000.toLong())
        }
    }

    private fun cancel() {
        toast?.cancel()
    }
}

/**
 * 参考了 https://www.cnblogs.com/net168/p/4237528.html
 * 但是去掉了 setParams 方法中的 mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
 * 否则在 Android7.0以上会闪退。
 * **/
@SuppressLint("ShowToast")
class AnimToast private constructor(
    context: Context,
    text: String,
    private val mShowTime: Boolean //记录Toast的显示长短类型
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
    }


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
        fun makeText(context: Context, text: String, showTime: Boolean): AnimToast {
            return AnimToast(context, text, showTime)
        }
    }
}

```

### 参考

1. https://blog.csdn.net/jdfkldjlkjdl/article/details/42031901 ( Toast 基础用法)
2. https://www.cnblogs.com/net168/p/4237528.html (带动画的 Toast)
3. https://www.cnblogs.com/net168/p/4058193.html (Toast 源码剖析)

## TODO

- [x] 研究带动画的 Toast  
- [ ] Toast 实现原理
- [ ] 和其他实现

### 作者

1. ID : hgncxzy
2. 邮箱：[hgncxzy@qq.com](mailto:hgncxzy@qq.com)
3. 项目地址：https://github.com/hgncxzy/XToast

