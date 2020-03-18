package com.ht117.badger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import com.ht117.badgerlib.BadgeView
import com.ht117.badgerlib.Shape
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textBadge = BadgeView.Builder(this)
            .setShape(Shape.RoundRect)
            .setBadgeGravity(Gravity.TOP or Gravity.LEFT)
            .setWidthAndHeight(12, 12)
            .setMargin(top = -12, left = -12)
            .build()
        textBadge.setBadgeCount(12)
//        textBadge.bind(tvText)

        val imgBadge = BadgeView.Builder(this)
            .setShape(Shape.Circle)
            .setBadgeGravity(Gravity.TOP or Gravity.RIGHT)
            .setMargin(top = 8, right = 8)
            .setWidthAndHeight(28, 28)
            .build()
        imgBadge.setBadgeCount(12)
        imgBadge.bind(ivImage)

        btnBind.setOnClickListener {
            if (imgBadge.isBinded) {
                imgBadge.unbind()
            } else {
                imgBadge.bind(ivImage)
            }
        }
    }
}
