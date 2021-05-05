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
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
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
    private var fromDate = ""
    private var toDate = ""

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

        binding.tvFrom.setOnClickListener {
            val dialog = DatePickerDialogFragment()
            dialog.listener = View.OnClickListener {
                binding.tvFrom.text = dialog.date
                if (dialog.date == "全部") {
                    fromDate = ""
                    socket?.emit("onDateChanged", buildJsonObject {
                        put("from", fromDate)
                        put("to", toDate)
                    })
                } else {
                    fromDate = getUnixTime(dialog.date).toString()
                    socket?.emit("onDateChanged", buildJsonObject {
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
                    socket?.emit("onDateChanged", buildJsonObject {
                        put("from", fromDate)
                        put("to", toDate)
                    })
                } else {
                    toDate = getUnixTime(dialog.date).toString()
                    socket?.emit("onDateChanged", buildJsonObject {
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
            socket?.emit("onDateChanged", buildJsonObject {
                put("from", fromDate)
                put("to", toDate)
            })
        }

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
                s.emit("start", sensor.id)
            }
        }

        socket?.on("onInit") { response ->
            val jsonResponse = JSONObject(response[0].toString())
            initData(
                binding.chart,
                Json.decodeFromString(jsonResponse.getString("initData"))
            )
        }

        socket?.on("onNewData") { response ->
            Log.d(Constants.TAG, response[0].toString())
            addData(
                binding.chart,
                Json.decodeFromString(response[0].toString())
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        socket?.disconnect()
    }

    private fun getUnixTime(time: String): Int {
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault())
        val date = format.parse(time)
        return (date.time / 1000).toInt()
    }

    private fun getHMS(time: Long): String {
        val date = Date(time * 1000)
        Log.d(Constants.TAG, "$time $date")
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date)
        return format.toString()
    }

    private fun addData(chart: LineChart, sensorData: SensorData) {
        val data = chart.data
        data.addEntry(Entry(sensorData.time.toFloat(), sensorData.value), 0)
        data.notifyDataChanged()

        chart.notifyDataSetChanged()
        chart.setVisibleXRangeMaximum(100F)
        chart.moveViewTo(sensorData.time.toFloat(), 50F, YAxis.AxisDependency.LEFT)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setChart(chart: LineChart) {
        with(chart) {
            setNoDataText("暂无数据")
            setDrawGridBackground(true)
            setBackgroundColor(Color.BLACK)
            setGridBackgroundColor(Color.BLACK)

            setTouchEnabled(true)

            isDragEnabled = true
            isDragDecelerationEnabled = true
            setScaleEnabled(true)

            isAutoScaleMinMaxEnabled = true

            setPinchZoom(true)

            description.isEnabled = true

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
            isEnabled = true
            text = "sensor data"
            textSize = 15f
            textColor = Color.WHITE
        }

        with(chart.legend) {
            isEnabled = true
            formSize = 10f
            textSize = 12f
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

            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                    return getHMS(value.toLong())
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
        with(chart) {
            clear()
            data = LineData(createSet(sensorDataList))
            data.notifyDataChanged()
            notifyDataSetChanged()
            invalidate()
        }
    }

    private fun createSet(sensorDataList: List<SensorData>): LineDataSet {
        val entryList = ArrayList<Entry>()
        for ((i, sensorData) in sensorDataList.withIndex()) {
            entryList.add(Entry(sensorData.time.toFloat(), sensorData.value))
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

    class MyMarkerView(context: Context, layoutResource: Int) :
        MarkerView(context, layoutResource) {
        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            super.refreshContent(e, highlight)
        }

        override fun getOffset(): MPPointF {
            return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        }
    }
}