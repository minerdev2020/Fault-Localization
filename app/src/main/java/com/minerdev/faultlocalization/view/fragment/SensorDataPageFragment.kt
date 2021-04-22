package com.minerdev.faultlocalization.view.fragment

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.minerdev.faultlocalization.databinding.FragmentSensorDataBinding
import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.utils.Constants.BASE_URL
import com.minerdev.faultlocalization.utils.Constants.TAG
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.*

class SensorDataPageFragment(private val sensor: Sensor) : Fragment() {
    private val binding by lazy { FragmentSensorDataBinding.inflate(layoutInflater) }
    private var socket: Socket? = null

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
        binding.tvFrom.text = "now"
        binding.tvTo.text = "now"

        binding.tvFrom.setOnClickListener {
            val dialog = DatePickerDialogFragment()
            dialog.listener = View.OnClickListener {
                binding.tvFrom.text = dialog.date
                if (dialog.date == "now") {
                    socket?.emit("onDateChanged", buildJsonObject {
                        put("from", dialog.date)
                    })
                } else {
                    socket?.emit("onDateChanged", buildJsonObject {
                        put("from", getUnixTime(dialog.date))
                    })
                }
            }
            dialog.show(requireActivity().supportFragmentManager, "DatePickerDialogFragment")
        }
        binding.tvTo.setOnClickListener {
            val dialog = DatePickerDialogFragment()
            dialog.listener = View.OnClickListener {
                binding.tvTo.text = dialog.date
                if (dialog.date == "now") {
                    socket?.emit("onDateChanged", buildJsonObject {
                        put("to", dialog.date)
                    })
                } else {
                    socket?.emit("onDateChanged", buildJsonObject {
                        put("to", getUnixTime(dialog.date))
                    })
                }
            }
            dialog.show(requireActivity().supportFragmentManager, "DatePickerDialogFragment")
        }

        binding.btnReset.setOnClickListener {
            binding.tvFrom.text = "now"
            binding.tvTo.text = "now"
            socket?.emit("onDateChanged", buildJsonObject {
                put("from", binding.tvFrom.text.toString())
                put("to", binding.tvTo.text.toString())
            })
        }

        setChart(binding.chart)

        try {
            socket = IO.socket(BASE_URL)

        } catch (e: URISyntaxException) {
            Log.e(TAG, e.reason)
        }

        socket?.let { s ->
            s.connect()
            s.on(Socket.EVENT_CONNECT) {
                Log.d(TAG, "Connected!")
                s.emit("start", sensor.id)
                s.on("onReceived") { response ->
                    val jsonResponse = JSONObject(response[0].toString())
                    addEntry(
                        binding.chart,
                        jsonResponse.getDouble(sensor.name).toFloat()
                    )
                }
            }
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

    private fun setChart(chart: LineChart) {
        chart.setDrawGridBackground(true)
        chart.setBackgroundColor(Color.BLACK)
        chart.setGridBackgroundColor(Color.BLACK)

        chart.description.isEnabled = true
        val des = chart.description
        des.isEnabled = true
        des.text = "Real-Time DATA"
        des.textSize = 15f
        des.textColor = Color.WHITE

        chart.setTouchEnabled(true)

        chart.isDragEnabled = true
        chart.isDragDecelerationEnabled = false
        chart.setScaleEnabled(false)

        chart.isAutoScaleMinMaxEnabled = true

        chart.setPinchZoom(false)

        chart.xAxis.setDrawGridLines(true)
        chart.xAxis.setDrawAxisLine(false)

        chart.xAxis.isEnabled = true
        chart.xAxis.setDrawGridLines(false)

        val l = chart.legend
        l.isEnabled = true
        l.formSize = 10f
        l.textSize = 12f
        l.textColor = Color.WHITE

        val leftAxis = chart.axisLeft
        leftAxis.isEnabled = true
        leftAxis.textColor = Color.rgb(255, 255, 255)
        leftAxis.setDrawGridLines(true)
        leftAxis.gridColor = Color.rgb(255, 255, 255)

        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false

        chart.invalidate()
    }

    private fun addEntry(chart: LineChart, num: Float) {
        var data = chart.data
        if (data == null) {
            data = LineData()
            chart.data = data
        }

        var set = data.getDataSetByIndex(0)
        if (set == null) {
            set = createSet()
            data.addDataSet(set)
        }

        data.addEntry(Entry(set.entryCount.toFloat(), num), 0)
        data.notifyDataChanged()

        chart.notifyDataSetChanged()

        chart.setVisibleXRangeMaximum(150f)
        chart.moveViewTo(data.entryCount.toFloat(), 50f, YAxis.AxisDependency.LEFT)
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "Real-time Line Data")
        set.lineWidth = 1f
        set.setDrawValues(false)
        set.valueTextColor = Color.rgb(255, 255, 255)
        set.color = Color.rgb(255, 255, 255)
        set.mode = LineDataSet.Mode.LINEAR
        set.setDrawCircles(false)
        set.highLightColor = Color.rgb(190, 190, 190)

        return set
    }
}