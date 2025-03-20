package com.example.imageuploader

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryAdapter(private val context: Context, private val imageList: List<String>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewItem)

        init {
            imageView.setOnClickListener {
                val intent = Intent(context, ImageSliderActivity::class.java)
                intent.putStringArrayListExtra("images", ArrayList(imageList))
                intent.putExtra("position", adapterPosition)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(imageList[position]).into(holder.imageView)
    }

    override fun getItemCount(): Int = imageList.size
}
