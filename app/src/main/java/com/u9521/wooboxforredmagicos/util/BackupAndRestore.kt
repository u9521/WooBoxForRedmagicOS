package com.u9521.wooboxforredmagicos.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Context.MODE_WORLD_READABLE
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity.Companion.activity
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.dialog.MIUIDialog
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.activity.SettingsActivity
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BackupAndRestore(private val context: SettingsActivity) {

    private val requestCodeBackup = 1001
    private val requestCodeRestore = 1002

    fun startBackup() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
            putExtra(
                Intent.EXTRA_TITLE, "Woobox_backup_${
                    SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.getDefault()).format(
                        Date()
                    )
                }.json"
            )
        }
        (context as Activity).startActivityForResult(intent, requestCodeBackup)
    }

    fun startRestore() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }
        (context as Activity).startActivityForResult(intent, requestCodeRestore)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            requestCodeBackup -> handleBackup(data)
            requestCodeRestore -> handleRestore(data)
        }
    }

    private fun handleBackup(data: Intent?) {
        data?.data?.let { uri ->
            try {
                val prefs = getSP()
                val allEntries = prefs!!.all
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val json = JSONObject(allEntries as Map<*, *>).toString()
                    outputStream.write(json.toByteArray())
                    Toast.makeText(context, "备份配置成功", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "备份配置失败: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleRestore(data: Intent?) {
        data?.data?.let { uri ->
            try {
                val prefs = getSP()
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val json = inputStream.bufferedReader().use { it.readText() }
                    val jsonObject = JSONObject(json)
                    prefs!!.edit().apply {
                        clear()
                        jsonObject.keys().forEach { key ->
                            when (val value = jsonObject[key]) {
                                is Boolean -> putBoolean(key, value)
                                is Int -> putInt(key, value)
                                is Long -> putLong(key, value)
                                is Float -> putFloat(key, value)
                                is String -> putString(key, value)
                                else -> putString(key, value.toString())
                            }
                        }
                        apply()
                    }
                    Toast.makeText(context, "还原配置成功", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "还原配置失败: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("WorldReadableFiles")
    fun getSP(): SharedPreferences? {
        try {
            return context.getSharedPreferences("WooboxConfig", MODE_WORLD_READABLE)
        } catch (exception: SecurityException) {
            val viewonlySP = context.getSharedPreferences("viewonly", MODE_PRIVATE)
            viewonlySP.edit().clear().apply()
            return viewonlySP
        }
    }

    fun confirmRestore(iData: InitView.ItemData) {
        iData.apply {
            MIUIDialog(activity) {
                setTitle(R.string.restore)
                setMessage(
                    R.string.restore_tips
                )
                setLButton(R.string.cancel) {
                    dismiss()
                }
                setRButton(R.string.Done) {
                    dismiss()
                    startRestore()
                }
            }.show()
        }

    }
}