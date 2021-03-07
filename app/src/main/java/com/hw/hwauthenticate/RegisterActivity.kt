package com.hw.hwauthenticate

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hw.hwauthenticate.utils.Constants
import com.hw.hwauthenticate.utils.ExpandUtils.toast
import com.hw.hwauthenticate.utils.SpUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_person_center.iv_head
import kotlinx.android.synthetic.main.activity_register.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private val mRequestCode = 110
    private var mFilePath: String? = null
    private var mBirthDay: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun register(view: View) {
        if (TextUtils.isEmpty(rUsername.text)) {
            toast(this, getString(R.string.action_empty_username))
            return
        }
        if (TextUtils.isEmpty(rPassword.text)) {
            toast(this, getString(R.string.action_empty_password))
            return
        }
        val userId = Constants.USER_ID_KEY + rUsername.text.toString()
        var result = SpUtils.getString(Constants.USER_ID_KEY)
        if (TextUtils.isEmpty(result)) {
            result = ""
        }
        SpUtils.putString(Constants.USER_ID_KEY, "$result,$userId")
        SpUtils.putString(Constants.USER_NAME_KEY + userId, rUsername.text.toString())
        SpUtils.putString(Constants.USER_PSW_KEY + userId, rUsername.text.toString())
        SpUtils.putString(Constants.CURRENT_USER_DATA_KEY, userId)
        if (mFilePath != null) {
            SpUtils.putString(Constants.USER_AVATAR_KEY + userId, mFilePath!!)
        }
        if (mBirthDay != null) {
            SpUtils.putString(Constants.USER_BIRTH_KEY + userId, mBirthDay!!)
        }
        setResult(RESULT_OK)
        finish()
    }

    fun selectBirthDate(view: View) {
        val calendar = Calendar.getInstance()
        val dataDialog = DatePickerDialog(
                this, { view, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth, 0, 0)
            val date = Date()
            date.time = calendar.time.time
            rTvSelectBirth.text =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
            mBirthDay = rTvSelectBirth.text.toString()
        },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
        )
        dataDialog.show()
    }

    fun selectPhoto(view: View) {
        val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, mRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mRequestCode && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage: Uri? = data.data
            rIvHead.setImageURI(selectedImage)
            Thread {
                if (selectedImage != null) {
                    val inputStream = contentResolver.openInputStream(selectedImage)
                    val file = File(
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "${System.currentTimeMillis()}.jpg"
                    )
                    if (inputStream != null) {
                        val fos = FileOutputStream(file)
                        val buffer = ByteArray(1024)
                        var length = 0
                        do {
                            length = inputStream.read(buffer)
                            if (length > 0) {
                                fos.write(buffer, 0, length)
                            } else {
                                break
                            }
                        } while (true)
                        inputStream.close()
                        fos.close()
                    }
                    mFilePath = file.absolutePath
                }
            }.start()
        }
    }

    fun backPage(view: View) {
        finish()
    }

}