package com.hw.hwauthenticate

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hw.hwauthenticate.utils.Constants
import com.hw.hwauthenticate.utils.ExpandUtils.toast
import com.hw.hwauthenticate.utils.SpUtils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val mRequestCode = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 999
            )
        }

    }

    fun login(view: View) {
        if (TextUtils.isEmpty(username.text)) {
            toast(this, getString(R.string.action_empty_username))
            return
        }
        if (TextUtils.isEmpty(password.text)) {
            toast(this, getString(R.string.action_empty_password))
            return
        }
        val userIds = SpUtils.getString(Constants.USER_ID_KEY)
        if (TextUtils.isEmpty(userIds)) {
            toast(this, "Wrong username or password!")
            return
        }
        val array = userIds.split(",")
        for (s in array) {
            val name = SpUtils.getString(Constants.USER_NAME_KEY + s)
            val pws = SpUtils.getString(Constants.USER_PSW_KEY + s)
            if ((name == username.text.toString()) && (pws == password.text.toString())) {
                //save current user id
                SpUtils.putString(Constants.CURRENT_USER_DATA_KEY, s)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return
            }
        }
        toast(this,"Wrong username or password!")
    }

    fun register(view: View) {
        startActivityForResult(Intent(this, RegisterActivity::class.java), mRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == mRequestCode) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}