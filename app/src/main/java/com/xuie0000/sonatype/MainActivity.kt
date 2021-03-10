package com.xuie0000.sonatype

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.xuie0000.VersionHelper

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // test library
    Log.d("MainActivity", "version:${VersionHelper.getVersionName(this)}")
  }
}