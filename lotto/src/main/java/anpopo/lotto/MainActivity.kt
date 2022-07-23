package anpopo.lotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val clearButton: Button by lazy {
        findViewById(R.id.clearButton)
    }

    private val addButton: Button by lazy {
        findViewById(R.id.addButton)
    }

    private val run: Button by lazy {
        findViewById(R.id.run)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById(R.id.numberPicker)
    }

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.ballNumber1),
            findViewById(R.id.ballNumber2),
            findViewById(R.id.ballNumber3),
            findViewById(R.id.ballNumber4),
            findViewById(R.id.ballNumber5),
            findViewById(R.id.ballNumber6)
        )
    }

    private var didRun = false
    private val pickNumberSet = hashSetOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45
        numberPicker.wrapSelectorWheel

        initRunButton()
        initAddButton()
        initClearButton()

    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach {
                it.isVisible = false
            }
            didRun = false
        }
    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            if (didRun){
                Toast.makeText(this, "초기화 후에 시도해주세용~", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개만 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickNumberSet.size]
            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            setBackgroundColor(textView, numberPicker.value)

            pickNumberSet.add(numberPicker.value)
        }
    }

    private fun setBackgroundColor(textView: TextView, number: Int) {
        when (number) {
            in 1..10 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_green)
            in 21..30 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_gray)
            in 31..40 -> textView.background =
                ContextCompat.getDrawable(this, R.drawable.circle_blue)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
        }
    }

    private fun initRunButton() {
        run.setOnClickListener {
            val list = getRandomNumber()
            didRun = true

            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]

                textView.text = number.toString()
                textView.isVisible = true
                setBackgroundColor(textView, number)
            }
        }
    }

    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)) continue
                    this.add(i)
                }
            }

        numberList.shuffle()

        return (pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)).sorted()
    }
}
