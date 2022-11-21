package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var ntManager: NotificationManager
    private lateinit var okButton:Button
    private lateinit var fileValueText: TextView
    private lateinit var statusValueText: TextView
    private lateinit var status: String


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        ntManager =
            ContextCompat.getSystemService(
                this,
                NotificationManager::class.java
            ) as NotificationManager
        ntManager.cancelAll()

        fileValueText = findViewById(R.id.textView_fileName_value)
        statusValueText = findViewById(R.id.textView_status_value)
        okButton = findViewById(R.id.ok_button)

        val extras = intent.extras
        if (extras != null) {
            fileValueText.text = extras.getString("fileNameValue")
            statusValueText.text =  extras.getString("statusValue").toString()
        }

        if (status == getString(R.string.success))
            statusValueText.setTextColor(getColor(R.color.success_color))
        else
            statusValueText.setTextColor(getColor(R.color.fail_color))

        okButton.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
