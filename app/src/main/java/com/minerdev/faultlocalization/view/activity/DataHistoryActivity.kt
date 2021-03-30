package com.minerdev.faultlocalization.view.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.adapter.SensorDataListAdapter
import com.minerdev.faultlocalization.databinding.ActivityDataHistoryBinding
import com.minerdev.faultlocalization.utils.Constants.BASE_URL
import com.minerdev.faultlocalization.utils.Constants.TAG
import com.minerdev.faultlocalization.viewmodel.EquipmentViewModel
import com.minerdev.faultlocalization.viewmodel.factory.EquipmentViewModelFactory
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class DataHistoryActivity : AppCompatActivity() {
    private val socketList = ArrayList<Socket>()
    private val binding by lazy { ActivityDataHistoryBinding.inflate(layoutInflater) }
    private val adapter by lazy { SensorDataListAdapter(SensorDataListAdapter.DiffCallback()) }
    private val viewModel: EquipmentViewModel by viewModels { EquipmentViewModelFactory(this) }

    private val onConnect = Emitter.Listener {
        Log.d(TAG, "Connected!")
    }

    private val onReceived = Emitter.Listener {
        Log.d(TAG, "New message has been received!")
        Log.d(TAG, it[0].toString())
    }

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "详细信息"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()

        id = intent.getIntExtra("id", 0)
        viewModel.item.observe(this, { it ->
            var socket: Socket? = null
            try {
                socket = IO.socket(BASE_URL)

            } catch (e: URISyntaxException) {
                Log.e(TAG, e.reason)
            }

            socket?.let { s ->
                s.connect()
                s.on(Socket.EVENT_CONNECT, onConnect)
                s.on("onReceived", onReceived)

                socketList.add(s)
            }

            adapter.submitList(it.sensor_info)
        })
        viewModel.loadItem(id)
    }

    override fun onDestroy() {
        super.onDestroy()
        for (socket in socketList) {
            socket.disconnect()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.recyclerView.adapter = adapter
    }
}