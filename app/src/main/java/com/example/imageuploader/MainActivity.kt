package com.example.imageuploader

import UploadResponse
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imageuploader.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var selectImageBtn: Button
    private lateinit var uploadImageBtn: Button
    private lateinit var categoryEditText: EditText
    private lateinit var imageView: ImageView
    private var selectedImageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectImageBtn = findViewById(R.id.selectImageBtn)
        uploadImageBtn = findViewById(R.id.uploadImageBtn)
        categoryEditText = findViewById(R.id.categoryEditText)
        imageView = findViewById(R.id.imageView)

        selectImageBtn.setOnClickListener { openGallery() }
        uploadImageBtn.setOnClickListener { uploadImage() }


        val openGalleryBtn: Button = findViewById(R.id.openGalleryBtn)
        openGalleryBtn.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            imageView.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImage() {
        if (selectedImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val categoryText = categoryEditText.text.toString().trim()
        if (categoryText.isEmpty()) {
            Toast.makeText(this, "Please enter a category", Toast.LENGTH_SHORT).show()
            return
        }

        val imageFile = FileUtils.uriToFile(this, selectedImageUri!!)
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
        val categoryPart = RequestBody.create(MultipartBody.FORM, categoryText)

        RetrofitClient.apiService.uploadImage(categoryPart, imagePart)
            .enqueue(object : Callback<UploadResponse> {
                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Upload Success", Toast.LENGTH_SHORT).show()
                        Log.d("UploadResponse", "Success: ${response.body()}")
                    } else {
                        Toast.makeText(this@MainActivity, "Upload Failed", Toast.LENGTH_LONG).show()
                        Log.e("UploadError", "Failure: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Upload Error: ${t.message}", Toast.LENGTH_LONG).show()
                    Log.e("UploadError", "Error: ${t.message}")
                }
            })
    }
}
