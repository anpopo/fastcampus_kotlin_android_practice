package anpopo.secret.diary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private val firstPassword:NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.firstPassword)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val secondPassword:NumberPicker by lazy {
        findViewById<NumberPicker?>(R.id.secondPassword)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val thirdPassword:NumberPicker by lazy {
        findViewById<NumberPicker?>(R.id.thirdPassword)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val openButton:AppCompatButton by lazy {
        findViewById(R.id.openButton)
    }

    private val changePassword:AppCompatButton by lazy {
        findViewById(R.id.changePassword)
    }

    private var changePasswordMode = false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstPassword
        secondPassword
        thirdPassword

        openButton.setOnClickListener {

            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

            val passwordFromUser = "${firstPassword.value}${secondPassword.value}${thirdPassword.value}"

            if (userPasswordCheck(passwordPreferences, passwordFromUser)) {
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                errorAlertShow()
            }
        }

        changePassword.setOnClickListener {

            val passwordFromUser = "${firstPassword.value}${secondPassword.value}${thirdPassword.value}"

            if (changePasswordMode) {
                val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
                // kotlin extension 문법 ktx
                passwordPreferences.edit(true) {
                    putString("password", passwordFromUser)
                }

                changePasswordMode = false;
                changePassword.setBackgroundColor(Color.BLACK)

            } else {
                // 비밀번호 변경 모드 활성화  :: 비밀번호 맞는지 확인
                val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)

                if (userPasswordCheck(passwordPreferences, passwordFromUser)) {
                    Toast.makeText(this, "변경할 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                    changePassword.setBackgroundColor(Color.CYAN)
                    changePasswordMode = true
                } else {
                    errorAlertShow()
                }
            }
        }
    }

    private fun userPasswordCheck(
        passwordPreferences: SharedPreferences,
        passwordFromUser: String
    ) = passwordPreferences.getString("password", "000")
        .equals(passwordFromUser)

    private fun errorAlertShow() {
        AlertDialog.Builder(this)
            .setTitle("실패")
            .setMessage("비밀번호 오류")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }
}