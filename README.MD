## Android  Toast 的通用实现

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
import android.os.Handler
import android.text.Html
import androidx.annotation.DrawableRes


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
    @SuppressLint("InflateParams")
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

    private fun cancel() {
        toast?.cancel()
    }

}

```

### 参考

https://blog.csdn.net/jdfkldjlkjdl/article/details/42031901

## TODO

1. 研究带动画的 Toast

### 作者

1. ID : hgncxzy
2. 邮箱：[hgncxzy@qq.com](mailto:hgncxzy@qq.com)
3. 项目地址：https://github.com/hgncxzy/XToast

