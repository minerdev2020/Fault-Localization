package com.minerdev.faultlocalization.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.FragmentSensorDataBinding
import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.model.SensorData
import com.minerdev.faultlocalization.utils.Constants
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.*
import kotlin.collections.ArrayList

class SensorDataPageFragment(private val sensor: Sensor) : Fragment() {
    private val binding by lazy { FragmentSensorDataBinding.inflate(layoutInflater) }
    private var socket: Socket? = null
    private var timeOffset = 0L
    private var samplingTime = 1
    private var fromDate = ""
    private var toDate = ""

    class MyMarkerView(context: Context, layoutResource: Int) :
        MarkerView(context, layoutResource) {
        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            super.refreshContent(e, highlight)
        }

        override fun getOffset(): MPPointF {
            return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvName.text = sensor.name
        binding.tvState.text = sensor.state.name
        binding.tvFrom.text = "全部"
        binding.tvTo.text = "全部"

        setSamplingTimeSelector()
        setDateRangeSelector()
        setChart(binding.chart)

        try {
            socket = IO.socket(Constants.DB_URL)

        } catch (e: URISyntaxException) {
            Log.e(Constants.TAG, e.reason)
        }

        socket?.let { s ->
            s.connect()
            s.on(Socket.EVENT_CONNECT) {
                Log.d(Constants.TAG, "Connected!")
                s.emit("start", buildJsonObject {
                    put("sensor_id", sensor.id)
                    put("sampling_time", samplingTime)
                })
            }
        }

        socket?.on("onInit") { response ->
            if (!response.isNullOrEmpty()) {
                val jsonResponse = JSONObject(response[0].toString())
                initData(
                    binding.chart,
                    Json.decodeFromString(jsonResponse.getString("initial_data"))
                )
            }
        }

        socket?.on("onNewData") { response ->
            if (!response.isNullOrEmpty()) {
                Log.d(Constants.TAG, response[0].toString())
                addData(
                    binding.chart,
                    Json.decodeFromString(response[0].toString())
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        socket?.disconnect()
    }

    private fun getUnixTime(time: String): Long {
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
        val date = format.parse(time)
        return date.time
    }

    private fun getHMS(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date)
        return format.toString()
    }

    private fun getYMD(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        return format.toString()
    }

    private fun addData(chart: LineChart, sensorData: SensorData) {
        val set = chart.data.getDataSetByIndex(0)
        set.removeEntry(0)
        set.addEntry(
            Entry(
                ((sensorData.time - timeOffset) / 1000 / samplingTime).toFloat(),
                sensorData.value
            )
        )
        chart.setVisibleXRangeMaximum(60F)
        chart.moveViewToX(((sensorData.time - timeOffset) / 1000 / samplingTime).toFloat())
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setChart(chart: LineChart) {
        with(chart) {
            setNoDataText("暂无数据")
            setDrawGridBackground(true)
            setBackgroundColor(Color.BLACK)
            setGridBackgroundColor(Color.BLACK)
            isAutoScaleMinMaxEnabled = true

            setTouchEnabled(true)
            setScaleEnabled(false)
            setPinchZoom(false)
            isDragEnabled = false
            isDragDecelerationEnabled = false
            isDoubleTapToZoomEnabled = false

            setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view?.parent?.requestDisallowInterceptTouchEvent(true)
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        view?.parent?.requestDisallowInterceptTouchEvent(false)
                    }
                    else -> {

                    }
                }
                false
            }
        }

        with(chart.description) {
            isEnabled = false
            text = "sensor data"
            textSize = 15F
            textColor = Color.WHITE
        }

        with(chart.legend) {
            isEnabled = false
            formSize = 10F
            textSize = 12F
            textColor = Color.WHITE
        }

        with(chart.xAxis) {
            isEnabled = true
            textColor = Color.WHITE
            gridColor = Color.WHITE
            labelRotationAngle = 45F
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(true)
            setDrawAxisLine(true)
            setLabelCount(5, false)

            valueFormatter = object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    val time = value.toLong() * 1000 * samplingTime + timeOffset
                    return if (samplingTime <= 60)
                        getHMS(time)
                    else
                        getYMD(time)
                }
            }
        }

        with(chart.axisLeft) {
            isEnabled = true
            textColor = Color.WHITE
            gridColor = Color.WHITE
            setDrawGridLines(true)
        }

        with(chart.axisRight) {
            isEnabled = false
        }

        chart.invalidate()
    }

    private fun initData(chart: LineChart, sensorDataList: List<SensorData>) {
        if (sensorDataList.isNotEmpty()) {
            timeOffset = sensorDataList[0].time
            with(chart) {
                clear()
                data = LineData(createSet(sensorDataList))
            }
        }
    }

    private fun createSet(sensorDataList: List<SensorData>): LineDataSet {
        val entryList = ArrayList<Entry>()
        for (sensorData in sensorDataList) {
            entryList.add(
                Entry(
                    ((sensorData.time - timeOffset) / 1000 / samplingTime).toFloat(),
                    sensorData.value
                )
            )
        }

        return LineDataSet(entryList, "value").apply {
            setDrawValues(false)
            setDrawCircles(false)
            lineWidth = 1F
            valueTextColor = Color.WHITE
            color = Color.WHITE
            mode = LineDataSet.Mode.LINEAR
            highLightColor = Color.rgb(190, 190, 190)
        }
    }

    private fun setSamplingTimeSelector() {
        binding.btnOneSec.setOnClickListener {
            samplingTime = 1
            socket?.emit("onSamplingTimeChanged", buildJsonObject {
                put("sensor_id", sensor.id)
                put("sampling_time", samplingTime)
            })
        }

        binding.btnOneMin.setOnClickListener {
            samplingTime = 60
            socket?.emit("onSamplingTimeChanged", buildJsonObject {
                put("sensor_id", sensor.id)
                put("sampling_time", samplingTime)
            })
        }

        binding.btnOneHour.setOnClickListener {
            samplingTime = 3600
            socket?.emit("onSamplingTimeChanged", buildJsonObject {
                put("sensor_id", sensor.id)
                put("sampling_time", samplingTime)
            })
        }

        binding.btnOneDay.setOnClickListener {
            samplingTime = 3600 * 24
            socket?.emit("onSamplingTimeChanged", buildJsonObject {
                put("sensor_id", sensor.id)
                put("sampling_time", samplingTime)
            })
        }

        binding.btnOneMonth.setOnClickListener {
            samplingTime = 3600 * 24 * 30
            socket?.emit("onSamplingTimeChanged", buildJsonObject {
                put("sensor_id", sensor.id)
                put("sampling_time", samplingTime)
            })
        }
    }

    private fun getCurrentSamplingTime(): Int {
        return when (binding.toggleButtonGroup.checkedButtonId) {
            R.id.btn_one_sec -> 1
            R.id.btn_one_min -> 60
            R.id.btn_one_hour -> 3600
            R.id.btn_one_day -> 3600 * 24
            R.id.btn_one_month -> 3600 * 24 * 30
            else -> 3600 * 24 * 30
        }
    }

    private fun calculateSamplingTime(from: Long, to: Long): Int {
        var diffTime = (to - from) / 1000
        Log.d(Constants.TAG, "$from $to $diffTime")
        return when {
            diffTime < 60 -> 1
            60.let { diffTime /= it; diffTime } < 60 -> 60
            60.let { diffTime /= it; diffTime } < 24 -> 3600
            24.let { diffTime /= it; diffTime } < 30 -> 3600 * 24
            30.let { diffTime /= it; diffTime } < 12 -> 3600 * 24 * 30
            else -> 3600 * 24 * 30
        }
    }

    private fun setDateRangeSelector() {
        binding.tvFrom.setOnClickListener {
            val dialog = DatePickerDialogFragment()
            dialog.listener = View.OnClickListener {
                binding.tvFrom.text = dialog.date
                if (dialog.date == "全部") {
                    fromDate = ""
                    samplingTime = if (toDate == "") {
                        getCurrentSamplingTime()
                    } else {
                        calculateSamplingTime(System.currentTimeMillis(), toDate.toLong())
                    }

                    socket?.emit("onDateChanged", buildJsonObject {
                        put("sensor_id", sensor.id)
                        put("sampling_time", samplingTime)
                        put("from", fromDate)
                        put("to", toDate)
                    })

                } else {
                    fromDate = getUnixTime(dialog.date).toString()
                    samplingTime = if (toDate == "") {
                        calculateSamplingTime(fromDate.toLong(), System.currentTimeMillis())
                    } else {
                        calculateSamplingTime(fromDate.toLong(), toDate.toLong())
                    }

                    socket?.emit("onDateChanged", buildJsonObject {
                        put("sensor_id", sensor.id)
                        put("sampling_time", samplingTime)
                        put("from", fromDate)
                        put("to", toDate)
                    })
                }
            }
            dialog.show(requireActivity().supportFragmentManager, "DatePickerDialogFragment")
        }
        binding.tvTo.setOnClickListener {
            val dialog = DatePickerDialogFragment()
            dialog.listener = View.OnClickListener {
                binding.tvTo.text = dialog.date
                if (dialog.date == "全部") {
                    toDate = ""
                    samplingTime = if (fromDate == "") {
                        getCurrentSamplingTime()
                    } else {
                        calculateSamplingTime(fromDate.toLong(), System.currentTimeMillis())
                    }

                    socket?.emit("onDateChanged", buildJsonObject {
                        put("sensor_id", sensor.id)
                        put("sampling_time", samplingTime)
                        put("from", fromDate)
                        put("to", toDate)
                    })

                } else {
                    toDate = getUnixTime(dialog.date).toString()
                    samplingTime = if (toDate == "") {
                        calculateSamplingTime(System.currentTimeMillis(), toDate.toLong())
                    } else {
                        calculateSamplingTime(fromDate.toLong(), toDate.toLong())
                    }

                    socket?.emit("onDateChanged", buildJsonObject {
                        put("sensor_id", sensor.id)
                        put("sampling_time", samplingTime)
                        put("from", fromDate)
                        put("to", toDate)
                    })
                }
            }
            dialog.show(requireActivity().supportFragmentManager, "DatePickerDialogFragment")
        }

        binding.btnReset.setOnClickListener {
            binding.tvFrom.text = "全部"
            binding.tvTo.text = "全部"
            fromDate = ""
            toDate = ""
            samplingTime = getCurrentSamplingTime()
            socket?.emit("onDateChanged", buildJsonObject {
                put("sensor_id", sensor.id)
                put("sampling_time", samplingTime)
                put("from", fromDate)
                put("to", toDate)
            })
        }
    }
}