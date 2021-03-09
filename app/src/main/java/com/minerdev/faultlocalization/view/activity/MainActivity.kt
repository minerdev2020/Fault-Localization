package com.minerdev.faultlocalization.view.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.adapter.SectionPageAdapter
import com.minerdev.faultlocalization.databinding.ActivityMainBinding
import com.minerdev.faultlocalization.retrofit.AuthRetrofitManager
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.utils.Constants.EQUIPMENT_STATE
import com.minerdev.faultlocalization.utils.Constants.EQUIPMENT_TYPE
import com.minerdev.faultlocalization.utils.Constants.FINISH_INTERVAL_TIME
import com.minerdev.faultlocalization.utils.Constants.MESSAGE_STATE
import com.minerdev.faultlocalization.utils.Constants.MESSAGE_TYPE
import com.minerdev.faultlocalization.utils.Constants.PERSON_STATE
import com.minerdev.faultlocalization.utils.Constants.PERSON_TYPE
import com.minerdev.faultlocalization.utils.Constants.SENSOR_STATE
import com.minerdev.faultlocalization.utils.Constants.SENSOR_TYPE
import com.minerdev.faultlocalization.view.fragment.EquipmentFragment
import com.minerdev.faultlocalization.view.fragment.MessageFragment
import com.minerdev.faultlocalization.view.fragment.PersonFragment
import com.minerdev.faultlocalization.view.fragment.SettingsFragment
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private var backPressedTime: Long = 0

    private val adapter = SectionPageAdapter(supportFragmentManager)
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_main_toolbar, menu)
        adapter.getItem(binding.viewPager.currentItem).onCreateOptionsMenu(menu, menuInflater)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        adapter.getItem(binding.viewPager.currentItem).onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
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
            ActivityCompat.finishAffinity(this);

        } else {
            backPressedTime = tempTime
            Toast.makeText(this, "再输入一遍会退出程序。", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewPager() {
        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.bottomNav.menu.getItem(position).isChecked = true
                supportActionBar?.title = adapter.getPageTitle(position)
                binding.searchView.onActionViewCollapsed()
                invalidateOptionsMenu()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        adapter.addFragment(PersonFragment(), "人员管理")
        adapter.addFragment(EquipmentFragment(), "设备管理")
        adapter.addFragment(MessageFragment(), "通知管理")
        adapter.addFragment(SettingsFragment(), "设置")

        binding.viewPager.adapter = adapter
    }

    private fun setupBottomNavigationView() {
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_person -> binding.viewPager.currentItem = 0
                R.id.tab_equip -> binding.viewPager.currentItem = 1
                R.id.tab_message -> binding.viewPager.currentItem = 2
                R.id.tab_settings -> binding.viewPager.currentItem = 3
                else -> {
                }
            }
            true
        }
    }
}