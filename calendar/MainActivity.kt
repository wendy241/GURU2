package com.example.calendar_todolist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar
import android.graphics.Color
import android.app.DatePickerDialog
import android.graphics.Typeface
import com.example.calendar_todolist.R


class MainActivity : AppCompatActivity() {

    // 이전에 저장된 투두리스트를 불러오기 위한 변수
    private val sharedPrefFile = "com.example.a0730_3.SHARED_PREF_FILE"
    private val todoKey = "TODO_ITEMS"

    private lateinit var todoRecyclerView: RecyclerView
    private lateinit var todoListAdapter: TodoListAdapter
    private val todoItems = mutableListOf<TodoItemWithCheck>()

    // 선택된 날짜를 저장하는 변수
    private var selectedYear: Int = 0
    private var selectedMonth: Int = 0
    private var selectedDay: Int = 0


    // 더미 데이터 리스트
    private val dummyDataList = mutableListOf(
        TodoItemWithCheck("화랑대역부터 서울여대 정문까지 걷기","운동합시다!"),
        TodoItemWithCheck("태릉입구역부터 서울여대 정문까지 걷기","운동합시다!"),
        TodoItemWithCheck("화랑대역부터 서울여대 남문까지 걷기","운동합시다!"),
        TodoItemWithCheck("태릉입구역부터 서울여대 남문까지 걷기","운동합시다!"),
        TodoItemWithCheck("만주벌판 한 바퀴 걷기","운동합시다!"),


        TodoItemWithCheck("과학관 뒷 쪽 사택 산책하기","운동합시다!"),
        TodoItemWithCheck("삼각숲 한 바퀴 걷기","운동합시다!"),
        TodoItemWithCheck("공강 시간에 경춘선숲길 산책하기","운동합시다!"),
        TodoItemWithCheck("공강 시간 10분 어깨, 목 스트레칭하기","운동합시다!"),
        TodoItemWithCheck("50주년에서 만주벌판까지 산책하기","운동합시다!"),

        TodoItemWithCheck("하루동안 셔틀 이용하지 않기","운동합시다!"),
        TodoItemWithCheck("하루동안 학교 엘리베이터 사용하지 않기","운동합시다!"),
        TodoItemWithCheck("공강시간 학교 체력단련실에서 15분 운동하기","운동합시다!"),
        TodoItemWithCheck("화랑대역에서 자전거로 정문까지 가기","운동합시다!"),
        TodoItemWithCheck("화랑대역에서 태릉입구역까지 걷기","운동합시다!"),


        TodoItemWithCheck("공강 시간에 물 한 잔 마시기","건강한 식습관을 가져요:)"),
        TodoItemWithCheck("초코라떼가 먹고 싶을 땐 건강한 비틀주스 고바유 마시기","건강한 식습관을 가져요:)"),
        TodoItemWithCheck("입맛이 없다면 팬도로시 샐러드 먹기","건강한 식습관을 가져요:)"),
        TodoItemWithCheck("강의 시간에 물 마시기","건강한 식습관을 가져요:)"),
        TodoItemWithCheck("하루동안 학교 카페에서 카페인 음료 마시지 않기","건강한 식습관을 가져요:)"),

        TodoItemWithCheck("3끼 중 한 끼는 밀가루 음식 먹지 않기","건강한 식습관을 가져요:)"),
        TodoItemWithCheck("커피 대신에 녹차 마시기","건강한 식습관을 가져요:)"),
        TodoItemWithCheck("도시락 싸오기","건강한 식습관을 가져요:)"),
        TodoItemWithCheck("아침에 일어나서 물 한 컵 마시기","건강한 식습관을 가져요:)"),
        TodoItemWithCheck("달달한 음료 마시지 않기","건강한 식습관을 가져요:)"),

        TodoItemWithCheck("식사와 간식 규칙적인 시간에 먹기","건강한 식습관을 가져요:)"),
        TodoItemWithCheck("천천히 꼭꼭 씹어 먹기","건강한 식습관을 가져요:)"),
        TodoItemWithCheck("국물 음식 줄이기","건강한 식습관을 가져요:)"),
        TodoItemWithCheck("하루동안 인스턴트 음식 먹지 않기","건강한 식습관을 가져요:)"),
        TodoItemWithCheck("아침식사 꼭 챙기기","건강한 식습관을 가져요:)"),

        // 추가적인 더미 데이터를 원하는 만큼 추가할 수 있습니다.
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 상단 바 커스터마이징
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBarTitle: TextView = findViewById(R.id.customActionBarTitle)
        actionBarTitle.text = "안녕" // 상단 바 중앙에 ''라는 문구 표시
        actionBarTitle.setTextColor(Color.parseColor("#344991")) // 문구 색상 변경

        // 폰트 파일을 로드
        val customFont = Typeface.createFromAsset(assets, "NanumSquareNeoOTF-Bd.otf")

        // 폰트를 텍스트뷰에 적용
        actionBarTitle.typeface = customFont

        // 이전에 저장된 투두리스트를 불러옵니다.
        val savedTodoList = loadTodoListFromSharedPreferences()
        todoItems.addAll(savedTodoList)

        todoRecyclerView = findViewById(R.id.todoRecyclerView)
        todoListAdapter = TodoListAdapter(todoItems, this::updateTodoList) // updateTodoList 함수를 람다로 전달
        todoRecyclerView.layoutManager = LinearLayoutManager(this)
        todoRecyclerView.adapter = todoListAdapter

        val showDatePickerButton: Button = findViewById(R.id.showDatePickerButton)
        showDatePickerButton.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this@MainActivity,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->



                // 선택한 날짜를 변수에 저장합니다.
                this@MainActivity.selectedYear = selectedYear
                this@MainActivity.selectedMonth = selectedMonth
                this@MainActivity.selectedDay = selectedDayOfMonth

                // 선택한 날짜를 상단 바에 표시합니다.
                updateActionBarTitle()

                // 더미 데이터 리스트를 섞습니다.
                dummyDataList.shuffle()

                // 더미 데이터 리스트에서 처음 3개의 TodoItemWithCheck 객체를 todoItems 리스트에 추가합니다.
                todoItems.clear()
                todoItems.addAll(dummyDataList.take(3))

                todoListAdapter.notifyDataSetChanged()

                // 투두리스트를 SharedPreferences에 저장합니다.
                saveTodoListToSharedPreferences()

            }, year, month, day)

        datePickerDialog.show()
    }

    private fun updateActionBarTitle() {
        val actionBarTitle: TextView = findViewById(R.id.customActionBarTitle)
        actionBarTitle.text = "$selectedYear-$selectedMonth-$selectedDay"
    }



    // updateTodoList() 함수는 todoItems 리스트의 모든 체크박스 상태를 확인하고 문구를 업데이트하는 함수입니다.
    private fun updateTodoList(allChecked: Boolean) {
        val actionBarTitle: TextView = findViewById(R.id.customActionBarTitle)
        actionBarTitle.text = if (allChecked) {
            "슈니들의 건강한 날들을 응원해요, 앞으로도 헬-쓔!"
        } else {
            "슈니의 건강한 하루❤️"
        }

        // 투두리스트를 SharedPreferences에 저장합니다.
        saveTodoListToSharedPreferences()
    }

    private fun loadTodoListFromSharedPreferences(): List<TodoItemWithCheck> {
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val todoListJson = sharedPreferences.getString(todoKey, null)
        return if (todoListJson != null) {
            val typeToken = object : TypeToken<List<TodoItemWithCheck>>() {}.type
            Gson().fromJson(todoListJson, typeToken)
        } else {
            emptyList()
        }
    }

    private fun saveTodoListToSharedPreferences() {
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val todoListJson = Gson().toJson(todoItems)
        editor.putString(todoKey, todoListJson)
        editor.apply()
    }
}
