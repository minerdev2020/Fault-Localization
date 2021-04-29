package com.minerdev.faultlocalization.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.adapter.SectionPageAdapter
import com.minerdev.faultlocalization.base.BaseActivity
import com.minerdev.faultlocalization.databinding.ActivityMainBinding
import com.minerdev.faultlocalization.service.NotificationService
import com.minerdev.faultlocalization.utils.Constants.FINISH_INTERVAL_TIME
import com.minerdev.faultlocalization.view.fragment.*

class MainActivity : BaseActivity() {
    private var backPressedTime: Long = 0

    private val adapter = SectionPageAdapter(this)
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewPager()

        setupBottomNavigationView()

        // 설정이 끝나고 타이틀 바꿔야 오류가 안 남
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = adapter.getPageTitle(0)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(Intent(applicationContext, NotificationService::class.java))

        } else {
            startService(Intent(applicationContext, NotificationService::class.java))
        }

        val data = intent.getStringExtra("data")
        if (data != null) {
            val intent = Intent(this, AlertActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("data", data)
            }
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_main_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        if (searchView.hasFocus()) {
            searchView.onActionViewCollapsed()
            return
        }

        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
            ActivityCompat.finishAffinity(this)

        } else {
            backPressedTime = tempTime
            Toast.makeText(this, "再输入一遍会退出程序。", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewPager() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNav.menu.getItem(position).isChecked = true
                supportActionBar?.title = adapter.getPageTitle(position)
                binding.searchView.onActionViewCollapsed()
            }
        })

        adapter.addFragment(AlertFragment(), "警报")
        adapter.addFragment(PersonFragment(), "人员")
        adapter.addFragment(EquipmentFragment(), "设备")
        adapter.addFragment(MessageFragment(), "申请")
        adapter.addFragment(SettingsFragment(), "设置")

        binding.viewPager.adapter = adapter
    }

    private fun setupBottomNavigationView() {
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_alert -> binding.viewPager.currentItem = 0
                R.id.tab_person -> binding.viewPager.currentItem = 1
                R.id.tab_equipment -> binding.viewPager.currentItem = 2
                R.id.tab_message -> binding.viewPager.currentItem = 3
                R.id.tab_settings -> binding.viewPager.currentItem = 4
                else -> {
                }
            }
            true
        }
    }
}