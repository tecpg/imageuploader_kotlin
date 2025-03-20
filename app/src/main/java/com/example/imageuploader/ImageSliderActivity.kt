package com.example.imageuploader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class ImageSliderActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var imageList: List<String>
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_slider)

        viewPager = findViewById(R.id.viewPager)

        imageList = intent.getStringArrayListExtra("images") ?: emptyList()
        position = intent.getIntExtra("position", 0)

        val adapter = ImageSliderAdapter(this, imageList)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(position, false)
    }
}
