package com.example.client_for_reddit

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.OutputStream

class FullScreenImageActivity : AppCompatActivity() {

    private lateinit var fullScreenImageView: ImageView
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        fullScreenImageView = findViewById(R.id.fullScreenImageView)
        saveButton = findViewById(R.id.saveButton)

        // Получаем URL изображения из Intent
        val imageUrl = intent.getStringExtra("IMAGE_URL")

        if (imageUrl != null) {
            // Загружаем изображение в ImageView с помощью Picasso
            Picasso.get().load(imageUrl).into(fullScreenImageView)

            // Сохраняем изображение при нажатии на кнопку
            saveButton.setOnClickListener {
                saveImage(imageUrl)
            }
        } else {
            Toast.makeText(this, "Ошибка: URL изображения не найден", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImage(imageUrl: String) {
        Picasso.get().load(imageUrl).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                if (bitmap != null) {
                    saveImageToGallery(bitmap)
                } else {
                    Toast.makeText(this@FullScreenImageActivity, "Ошибка: не удалось загрузить изображение", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Toast.makeText(this@FullScreenImageActivity, "Ошибка: не удалось загрузить изображение", Toast.LENGTH_SHORT).show()
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        val fos: OutputStream?

        try {
            fos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                resolver.openOutputStream(imageUri!!)
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                val image = java.io.File(imagesDir, filename)
                java.io.FileOutputStream(image)
            }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(this, "Изображение сохранено в галерее", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка: не удалось сохранить изображение", Toast.LENGTH_SHORT).show()
        }
    }
}
