package com.xzy.ui.xtoast
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        default_toast.setOnClickListener {
            XToast.showDefaultToast(this, "默认的 Toast")
        }

        custom_position_toast.setOnClickListener {
            XToast.showCustomPositionToast(this, "自定义位置的 Toast")
        }

        custom_bg_toast.setOnClickListener {
            XToast.showCustomBgToast(this, "自定义背景颜色的 Toast")
        }

        toast_with_img.setOnClickListener {
            XToast.showToastWithImg(this, "带图片的 Toast", R.mipmap.ic_success)
        }

        toast_with_img_and_anim.setOnClickListener {
            XToast.showToastWithAnimAndImg(this,"带图片带动画的Toast",R.mipmap.ic_success)
        }

        custom_toast.setOnClickListener {
            XToast.showCustomToast(this, "显示纯文本的自定义 Toast")
        }

        toast_from_other_thread.setOnClickListener {
            XToast.showToast(this, "我来自其他线程！")
        }

        toast_with_anim.setOnClickListener {
            AnimToast.makeText(this@MainActivity, "带动画的 Toast", false).show()
        }


        toast_with_anim2.setOnClickListener {
            XToast.showToastWithAnim(this,false,"带动画的 Toast2")
        }
    }
}
