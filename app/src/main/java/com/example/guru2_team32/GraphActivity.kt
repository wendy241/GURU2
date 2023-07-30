package com.example.guru2_team32

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat


class GraphActivity : AppCompatActivity() {
    data class CommitData(val date: String, val commitNum: Float?, val dayOfWeek: String)

    private var weight: Float? = null
    private val dataList: MutableList<CommitData> = mutableListOf()

    private lateinit var lineChart: LineChart
    private lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var lineChartCardView: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        setupDataList()

        val weightString = intent.getStringExtra("weight")
        weight = if (!weightString.isNullOrEmpty()) weightString.toFloat() else 0.0f

        lineChart = findViewById(R.id.line_chart)
        horizontalScrollView = findViewById(R.id.horizontal_scroll_view)
        lineChartCardView = findViewById<CardView>(R.id.lineChartCardView)

        setupChart()
        hideChart() // 초기에는 그래프를 숨기도록 설정

        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radioButton) {
                drawGraph("월간")
            } else if (checkedId == R.id.radioButton2) {
                drawGraph("주간")
            }

            showChart() // 라디오 버튼을 선택할 때마다 그래프를 보이도록 설정

        }

        var imageButton1 = findViewById<ImageButton>(R.id.imageButton1)
        var imageButton2 = findViewById<ImageButton>(R.id.imageButton2)
        var imageButton3 = findViewById<ImageButton>(R.id.imageButton3)

        imageButton1.setOnClickListener {
            var intent = Intent(this, GraphActivity::class.java)
            startActivity(intent)
        }

        imageButton3.setOnClickListener {

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupDataList() {
        dataList.clear() // dataList 초기화
        dataList.addAll(
            listOf(
                CommitData("월", 51.3F, "월요일"),
                CommitData("화", 51.2F, "화요일"),
                CommitData("수", 50.9F, "수요일"),
                CommitData("목", 51.2F, "목요일"),
                CommitData("금", 51.0F, "금요일"),
                CommitData("토", 50.6F, "토요일"),
                CommitData("일", weight, "일요일"), // 인텐트로부터 받은 weight 값을 적용
            )
        )
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

            val xValues = dataList.mapIndexed { index, data -> index.toFloat() }
            xAxis.setGranularity(1f)
            valueFormatter = XAxisCustomFormatter(changeDateText(dataList), xValues)
        }
    }

    private fun drawGraph(timeUnit: String) {
        val entries = if (timeUnit == "주간") {
            dataList.mapIndexed { index, data ->
                val commitNum = data.commitNum ?: 0.0f
                Entry(index.toFloat(), commitNum)
            }
        } else {
            // 월간 데이터로 변경
            val monthlyDataList = dataList.groupBy { it.date.substring(0, 5) }
                .map { (_, group) -> group.last() } // 각 월별 마지막 데이터 선택
            monthlyDataList.mapIndexed { index, data ->
                // weight가 Nullable하므로 null일 경우에 대한 처리를 해야합니다.
                val commitNum = data.commitNum ?: 0.0f
                Entry(index.toFloat(), commitNum)
            }
        }

        val lineDataSet = LineDataSet(entries, "entries")
        lineDataSet.apply {
            color = resources.getColor(R.color.black, null)
            circleRadius = 5f
            lineWidth = 3f
            setCircleColor(resources.getColor(R.color.purple_700, null))
            circleHoleColor = resources.getColor(R.color.purple_700, null)
            setDrawHighlightIndicators(false)
            setDrawValues(true)
            valueTextColor = resources.getColor(R.color.black, null)
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
    }

    private fun changeDateText(dataList: List<CommitData>): List<String> {
        return dataList.map { data -> data.dayOfWeek }
    }

    class XAxisCustomFormatter(
        private val xAxisData: List<String>,
        private val xValues: List<Float>
    ) : ValueFormatter() {

        override fun getFormattedValue(value: Float): String {
            val index = value.toInt()
            return if (index in 0 until xAxisData.size) {
                xAxisData[index]
            } else {
                ""
            }
        }

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index in 0 until xAxisData.size) {
                xAxisData[index]
            } else {
                ""
            }
        }
    }

    private fun hideChart() {
        lineChart.visibility = View.GONE
    }

    private fun showChart() {
        lineChart.visibility = View.VISIBLE
    }

    class DecimalValueFormatter(private val decimalDigits: Int) : ValueFormatter() {
        private val format = DecimalFormat("###,###,##0.${"#".repeat(decimalDigits)}")

        override fun getFormattedValue(value: Float): String {
            return format.format(value)
        }
    }
}