package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var radioGrouping: RadioGroup
    private lateinit var radioButtonSelected : RadioButton
    private var fileNameIndex: Int = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        radioGrouping = findViewById(R.id.group)


        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )

        custom_button.setOnClickListener {

            if (radioGrouping.checkedRadioButtonId == -1) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.please_message),
                    Toast.LENGTH_LONG
                ).show()
            }else{
                val indexOfRadioButtonSelected = radioGrouping.checkedRadioButtonId
                radioButtonSelected = findViewById(indexOfRadioButtonSelected)
                when (radioButtonSelected.id) {
                    R.id.first_radio -> {
                        download(URL_first_radio)
                        fileNameIndex = 0
                    }
                    R.id.second_radio -> {
                        download(URL_second_radio)
                        fileNameIndex = 1
                    }
                    R.id.third_radio -> {
                        download(URL_third_radio)
                        fileNameIndex = 2
                    }
                }

            }

        }
    }


    fun getFileName(fileNameIndex: Int) : String{
        return when (fileNameIndex) {
            0 -> {
                getString(R.string.first_radio)
            }
            1 -> {
                getString(R.string.second_radio)
            }
            else -> {
                getString(R.string.third_radio)
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            context?.let {
                id?.let { it1 -> getStatus(it1) }?.let { it2 ->
                    notificationManager.sendNotification(
                        getString(R.string.notification_description),
                        it, it2,fileName = getFileName(fileNameIndex)
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)
    }

    companion object {
        private const val URL_first_radio = "https://codeload.github.com/bumptech/glide/zip/master"
        private const val URL_second_radio =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_third_radio = "https://codeload.github.com/square/retrofit/zip/master"
    }

    private fun getStatus(id: Long): String {
        val downloadManagerQuery = DownloadManager.Query()
        downloadManagerQuery.setFilterById(id)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val cursor: Cursor = downloadManager.query(downloadManagerQuery)

        val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        val status = cursor.getInt(columnIndex)
        var statusText = ""
        when (status) {
            DownloadManager.STATUS_FAILED -> statusText = getString(R.string.fail)
            DownloadManager.STATUS_SUCCESSFUL -> statusText = getString(R.string.success)
        }
        return statusText
    }

    private fun createChannel(channelId: String, channelName: String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,

                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(true)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_description)

            notificationManager.createNotificationChannel(notificationChannel)

        }

    }





}
