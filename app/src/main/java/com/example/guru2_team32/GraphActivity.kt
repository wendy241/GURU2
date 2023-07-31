package com.example.guru2_team32

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class GraphActivity : AppCompatActivity() {
    data class CommitData(val date: String, val commitNum: Float, val dayOfWeek: String)

    private val dataList: MutableList<CommitData> = mutableListOf()

    private lateinit var lineChart: LineChart
    private lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var lineChartCardView: CardView
    private lateinit var weightEditText: EditText
    private lateinit var radioGroup: RadioGroup


    private fun saveData(weight: Int){
        var pref = this.getPreferences(0)
        var editor = pref.edit()

        editor.putInt("KEY_WEIGHT",
            weightEditText.text.toString()!!.toInt()).apply()
    }

    private fun loadData(){
        var pref = this.getPreferences(0)
        var weight = pref.getInt("KEY_WEIGHT", 0)

        if(weight != 0){
            weightEditText.setText(weight.toString())
        }
    }


    private fun generateMonthlyData(weight: Float?): List<CommitData> {
        val monthlyDataList = mutableListOf<CommitData>()
        val now: String = SimpleDateFormat("MM", Locale.KOREAN).format(Date())

        val dateFormat = SimpleDateFormat("MM", Locale.KOREAN).format(now)
        loadData()

        // 원하는 방법으로 월간 데이터를 생성하여 monthlyDataList에 추가합니다.
        // 예를 들어 아래와 같이 간단히 월별로 고정된 값을 설정할 수 있습니다.
        if (weight!! > 0.0f) {
            var weightEditText2 = weightEditText.text.toString().toFloat()

            monthlyDataList.add(CommitData("7월", weightEditText2, "7월"))
        }
        monthlyDataList.add(CommitData("8월", 51.8F, "$dateFormat+1 월"))
        monthlyDataList.add(CommitData("9월", 51.5F, "$dateFormat+2 월"))
        monthlyDataList.add(CommitData("10월", 51.2F, "$dateFormat+3 월"))
        monthlyDataList.add(CommitData("11월", 51.4F, "$dateFormat+4 월"))
        monthlyDataList.add(CommitData("12월", 51.6F, "$dateFormat+5 월"))
        monthlyDataList.add(CommitData("1월", 51.1F, "$dateFormat+6 월"))
        // 이하 생략

        return monthlyDataList
    }

    private fun generateWeeklyData(weight: Float?): List<CommitData> {
        val weeklyDataList = mutableListOf<CommitData>()

        // 원하는 방법으로 주간 데이터를 생성하여 weeklyDataList에 추가합니다.
        // 예를 들어 아래와 같이 간단히 주별로 고정된 값을 설정할 수 있습니다.
        weeklyDataList.add(CommitData("월", 51.3F, "월요일"))
        weeklyDataList.add(CommitData("화", 51.2F, "화요일"))
        weeklyDataList.add(CommitData("수", 50.9F, "수요일"))
        weeklyDataList.add(CommitData("목", 51.2F, "목요일"))
        weeklyDataList.add(CommitData("금", 51.0F, "금요일"))
        weeklyDataList.add(CommitData("토", 50.6F, "토요일"))

        if (weight!! > 0.0f) {
            var weightEditText2 = weightEditText.text.toString().toFloat()

            weeklyDataList.add(CommitData("일", weightEditText2, "일요일"))

        }
        return weeklyDataList
    }
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_graph)
            var inputButton = findViewById<Button>(R.id.inputButton)

            weightEditText = findViewById(R.id.weightEditText) // EditText 뷰를 찾아와서 weightEditText 변수에 초기화

            inputButton.setOnClickListener {
                var weight = weightEditText.text.toString().toFloatOrNull() ?: 0.0f // EditText에서 입력된 값을 가져오도록 수정
                saveData(weightEditText.text.toString()!!.toInt())

                setupDataList(weight)

            }

            radioGroup = findViewById(R.id.radioGroup)

            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == R.id.radioButton) {
                    drawGraph("월간")
                } else if (checkedId == R.id.radioButton2) {
                    drawGraph("주간")
                }

                showChart() // 라디오 버튼을 선택할 때마다 그래프를 보이도록 설정

            }





            lineChart = findViewById(R.id.line_chart)
            horizontalScrollView = findViewById(R.id.horizontal_scroll_view)
            lineChartCardView = findViewById<CardView>(R.id.lineChartCardView)

            setupChart()
            hideChart() // 초기에는 그래프를 숨기도록 설정

            var imageButton1 = findViewById<ImageButton>(R.id.imageButton1)
            var imageButton2 = findViewById<ImageButton>(R.id.imageButton2)
            var imageButton3 = findViewById<ImageButton>(R.id.imageButton3)



            imageButton3.setOnClickListener {

                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        private fun setupDataList(weight: Float?) {
            dataList.clear() // dataList 초기화

            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            if (selectedRadioButtonId == R.id.radioButton) {
                dataList.addAll(generateMonthlyData(weight))
            } else {
                dataList.addAll(generateWeeklyData(weight))
            }
        }


        private fun setupChart() {
            lineChart.apply {
                axisRight.isEnabled = false
                axisLeft.isEnabled = false
                legend.isEnabled = false
                description.isEnabled = false
                isDragXEnabled = true
                isScaleYEnabled = false
                isScaleXEnabled = false
            }

            val xAxis = lineChart.xAxis
            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(true)
                setDrawLabels(true)
                position = XAxis.XAxisPosition.BOTTOM
                textColor = resources.getColor(R.color.black, null)
                textSize = 10f
                labelRotationAngle = 0f
                setGranularity(1f)

                val xValues = dataList.mapIndexed { index, data -> index.toFloat() }
                xAxis.setGranularity(1f)
                valueFormatter = XAxisCustomFormatter(changeDateText(dataList))
            }
        }

        private fun drawGraph(timeUnit: String) {
            // 이전 그래프 선과 색 복원
            val colorResId = if (timeUnit == "주간") R.color.purple_700 else R.color.black
            val circleColorResId = if (timeUnit == "주간") R.color.purple_700 else R.color.black

            val entries = if (timeUnit == "주간") {
                // 주간 데이터로 변경
                val weeklyDataList = dataList
                weeklyDataList.mapIndexed { index, data ->
                    // weight가 Nullable하므로 null일 경우에 대한 처리를 해야합니다.
                    val commitNum = data.commitNum ?: 0.0f
                    Entry(index.toFloat(), commitNum)
                }
            } else {
                // 월간 데이터로 변경
                val monthlyDataList = dataList
                monthlyDataList.mapIndexed { index, data ->
                    // weight가 Nullable하므로 null일 경우에 대한 처리를 해야합니다.
                    val commitNum = data.commitNum ?: 0.0f
                    Entry(index.toFloat(), commitNum)
                }
            }

            val lineDataSet = LineDataSet(entries, "entries")
            lineDataSet.apply {
                color = resources.getColor(colorResId, null)
                circleRadius = 5f
                lineWidth = 3f
                setCircleColor(resources.getColor(circleColorResId, null))
                circleHoleColor = resources.getColor(circleColorResId, null)
                setDrawHighlightIndicators(false)
                setDrawValues(true)
                valueTextColor = resources.getColor(colorResId, null)
                valueFormatter = DecimalValueFormatter(2) // 소수점 아래 2자리까지 나오도록 설정
                valueTextSize = 10f
            }

            lineChart.apply {
                data = LineData(lineDataSet)
                notifyDataSetChanged()
                invalidate()
            }

            // 가로 스크롤뷰의 위치를 차트의 가장 오른쪽으로 이동
            horizontalScrollView.post {
                horizontalScrollView.scrollTo(lineChart.width, 0)
            }
            showChart()
        }

        class XAxisCustomFormatter(private val xAxisData: List<String>) : ValueFormatter() {

            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index in (0 until xAxisData.size)) {
                    xAxisData[index]
                } else {
                    ""
                }
            }


        }

        class DecimalValueFormatter(private val decimalDigits: Int) : ValueFormatter() {
            private val format = DecimalFormat("###,###,##0.${"#".repeat(decimalDigits)}")

            override fun getFormattedValue(value: Float): String {
                return format.format(value)
            }
        }


        private fun hideChart() {
            lineChart.visibility = View.GONE
        }

        private fun showChart() {
            lineChart.visibility = View.VISIBLE
        }

        private fun changeDateText(dataList: List<CommitData>): List<String> {
            val dateList = mutableListOf<String>()

            for (data in dataList) {
                dateList.add(data.date)
            }

            return dateList
        }
    }

