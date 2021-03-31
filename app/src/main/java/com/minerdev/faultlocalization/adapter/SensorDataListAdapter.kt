package com.minerdev.faultlocalization.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.minerdev.faultlocalization.databinding.ItemSensorDataBinding
import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.utils.Constants.TAG
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException


class SensorDataListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Sensor, SensorDataListAdapter.ViewHolder>(diffCallback) {
    val socketList = ArrayList<Socket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSensorDataBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        var socket: Socket? = null
        try {
            socket = IO.socket(Constants.BASE_URL)

        } catch (e: URISyntaxException) {
            Log.e(TAG, e.reason)
        }

        socket?.let { s ->
            socketList.add(s)
            holder.bind(item, s)
        }
    }

    operator fun get(position: Int): Sensor {
        return getItem(position)
    }

    class ViewHolder(val binding: ItemSensorDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sensor: Sensor, socket: Socket) {
            binding.tvName.text = sensor.name
            binding.tvState.text = sensor.state.name
            setChart(binding.lineChart)

            socket.connect()
            socket.on(Socket.EVENT_CONNECT) {
                Log.d(TAG, "Connected!")
                socket.emit("start", sensor.id)
                socket.on("onReceived") {
                    Log.d(TAG, "New message has been received!")
                    Log.d(TAG, it[0].toString())

                    val jsonResponse = JSONObject(it[0].toString())
                    addEntry(
                        binding.lineChart,
                        jsonResponse.getDouble("IONGAUGEPRESSURE").toFloat()
                    )
                }
            }
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

            chart.setTouchEnabled(false)

            chart.isDragEnabled = false
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

    class DiffCallback : DiffUtil.ItemCallback<Sensor>() {
        override fun areItemsTheSame(oldItem: Sensor, newItem: Sensor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sensor, newItem: Sensor): Boolean {
            return oldItem == newItem
        }
    }
}