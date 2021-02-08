package com.hw.hwauthenticate

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hw.hwauthenticate.utils.Constants
import com.hw.hwauthenticate.utils.SpUtils
import kotlinx.android.synthetic.main.activity_person_center.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class PersonCenterActivity : AppCompatActivity() {

    private var mUserId: String = ""
    private val mRequestCode = 110
    private var mName: String = ""
    private var mBirthDay: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_center)
        mUserId = SpUtils.getString(Constants.CURRENT_USER_DATA_KEY)
        mName = SpUtils.getString(Constants.USER_NAME_KEY + mUserId)
        pEdtUserName.setText(mName)
        mBirthDay = SpUtils.getString(Constants.USER_BIRTH_KEY + mUserId)
        pTvSelectBirth.text = mBirthDay
        val filePath = SpUtils.getString(Constants.USER_AVATAR_KEY + mUserId)
        if (!TextUtils.isEmpty(filePath)) {
            iv_head.setImageBitmap(BitmapFactory.decodeFile(filePath))
        }

        pEdtUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                SpUtils.putString(Constants.USER_NAME_KEY + mUserId, s?.toString() ?: "")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    fun backPage(view: View) {
        finish()
    }

    fun selectPhoto(view: View) {
        val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, mRequestCode)
    }

    fun selectBirthDate(view: View) {
        val calendar = Calendar.getInstance()
        val dataDialog = DatePickerDialog(
                this, { view, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth, 0, 0)
            val date = Date()
            date.time = calendar.time.time
            pTvSelectBirth.text =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
            SpUtils.putString(
                    Constants.USER_BIRTH_KEY + mUserId,
                    pTvSelectBirth.text.toString()
            )
        },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
        )
        dataDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mRequestCode && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage: Uri? = data.data
            iv_head.setImageURI(selectedImage)
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
                    SpUtils.putString(Constants.USER_AVATAR_KEY + mUserId, file.absolutePath)
                }
            }.start()
        }
    }

    fun loginOut(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}