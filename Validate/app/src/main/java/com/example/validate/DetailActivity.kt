package com.example.validate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.validate.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "student_prefs"
        const val KEY_NAME = "name"
        const val KEY_EMAIL = "email"
        const val KEY_PHONE = "phone"
        const val KEY_STUDENT_ID = "student_id"
        const val KEY_CLASS = "class_name"
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
        binding.btnGoRegister.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.btnDelete.setOnClickListener { confirmDelete() }
    }

    private fun loadData() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val empty = "Chưa có thông tin"

        with(binding) {
            tvName.text = prefs.getString(KEY_NAME, null) ?: empty
            tvEmail.text = prefs.getString(KEY_EMAIL, null) ?: empty
            tvPhone.text = prefs.getString(KEY_PHONE, null) ?: empty
            tvStudentId.text = prefs.getString(KEY_STUDENT_ID, null) ?: empty
            tvClass.text = prefs.getString(KEY_CLASS, null) ?: empty
        }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Xóa dữ liệu")
            .setMessage("Bạn có chắc muốn xóa toàn bộ thông tin đã đăng kí?")
            .setPositiveButton("Xóa") { _, _ ->
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().clear().apply()
                loadData()
                Snackbar.make(binding.root, "Đã xóa dữ liệu", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}