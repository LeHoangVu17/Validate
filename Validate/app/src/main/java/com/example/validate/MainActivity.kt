package com.example.validate

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.validate.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val nameRegex = Regex("^[\\p{L}]+(\\s[\\p{L}]+)+$")
    private val phoneRegex = Regex("^(0|\\+84)(3|5|7|8|9)\\d{8}$")
    private val studentIdRegex = Regex("^[A-Za-z0-9]{12,14}$")
    private val classRegex = Regex("^[A-Za-z0-9._\\- ]{2,20}$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegister.setOnClickListener { onRegisterClick(it) }
        binding.btnGoDetail.setOnClickListener { openDetail() }
    }

    private fun openDetail() {
        startActivity(Intent(this, DetailActivity::class.java))
    }

    private fun onRegisterClick(view: View) = with(binding) {
        val name = tilName.text()
        val email = tilEmail.text()
        val phone = tilPhone.text()
        val studentId = tilStudentId.text()
        val className = tilClass.text()

        val results = listOf(
            setError(tilName, validateName(name)),
            setError(tilEmail, validateEmail(email)),
            setError(tilPhone, validatePhone(phone)),
            setError(tilStudentId, validateStudentId(studentId)),
            setError(tilClass, validateClass(className))
        )

        if (results.all { it }) {
            saveInfo(name, email, phone, studentId, className)
            Snackbar.make(view, "Đăng kí thành công!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Xem chi tiết") { openDetail() }
                .show()
        } else {
            Snackbar.make(view, "Vui lòng kiểm tra lại thông tin!", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun saveInfo(name: String, email: String, phone: String, studentId: String, className: String) {
        getSharedPreferences(DetailActivity.PREFS_NAME, MODE_PRIVATE)
            .edit()
            .putString(DetailActivity.KEY_NAME, name)
            .putString(DetailActivity.KEY_EMAIL, email)
            .putString(DetailActivity.KEY_PHONE, phone)
            .putString(DetailActivity.KEY_STUDENT_ID, studentId)
            .putString(DetailActivity.KEY_CLASS, className)
            .apply()
    }


    private fun validateName(v: String): String? = when {
        v.isEmpty() -> "Họ tên không được để trống"
        !nameRegex.matches(v) -> "Họ tên phải gồm ít nhất 2 từ và chỉ chứa chữ cái"
        else -> null
    }

    private fun validateEmail(v: String): String? = when {
        v.isEmpty() -> "Email không được để trống"
        !Patterns.EMAIL_ADDRESS.matcher(v).matches() -> "Email không đúng định dạng"
        else -> null
    }

    private fun validatePhone(v: String): String? = when {
        v.isEmpty() -> "Số điện thoại không được để trống"
        !phoneRegex.matches(v) -> "SĐT không hợp lệ "
        else -> null
    }

    private fun validateStudentId(v: String): String? = when {
        v.isEmpty() -> "Mã sinh viên không được để trống"
        !studentIdRegex.matches(v) -> "MSV gồm 12-14 ký tự chữ/số "
        else -> null
    }

    private fun validateClass(v: String): String? = when {
        v.isEmpty() -> "Lớp không được để trống"
        !classRegex.matches(v) -> "Lớp không hợp lệ "
        else -> null
    }


    private fun setError(til: TextInputLayout, error: String?): Boolean {
        til.error = error
        return error == null
    }

    private fun TextInputLayout.text(): String = editText?.text?.toString()?.trim().orEmpty()
}