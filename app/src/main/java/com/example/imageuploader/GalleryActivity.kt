package com.example.imageuploader

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GalleryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var galleryAdapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns in grid

        fetchImagesFromServer()
    }

    private fun fetchImagesFromServer() {
        val category = "hair" // Category to fetch

        RetrofitClient.apiService.getUploadedImages(category).enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val images = response.body() ?: emptyList()
                    galleryAdapter = GalleryAdapter(this@GalleryActivity, images) // Pass context
                    recyclerView.adapter = galleryAdapter
                } else {
                    Toast.makeText(this@GalleryActivity, "Failed to load images", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(this@GalleryActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("GalleryActivity", "Error fetching images: ${t.message}")
            }
        })
    }
}
