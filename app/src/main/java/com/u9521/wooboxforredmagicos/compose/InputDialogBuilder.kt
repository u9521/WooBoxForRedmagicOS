package com.u9521.wooboxforredmagicos.compose

import android.content.SharedPreferences
import android.widget.Toast
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.dialog.MIUIDialog
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.activity.SettingsActivity

// 类型适配器基类
abstract class PrefsTypeAdapter<T> {
    abstract fun parse(input: String): T?
    abstract fun save(editor: SharedPreferences.Editor, key: String, value: T)

    // 内置类型实现
    companion object {
        val INT: PrefsTypeAdapter<Int> = object : PrefsTypeAdapter<Int>() {
            override fun parse(input: String) = input.toIntOrNull()
            override fun save(editor: SharedPreferences.Editor, key: String, value: Int) {
                editor.putInt(key, value)
            }
        }
        val LONG: PrefsTypeAdapter<Long> = object : PrefsTypeAdapter<Long>() {
            override fun parse(input: String) = input.toLongOrNull()
            override fun save(editor: SharedPreferences.Editor, key: String, value: Long) {
                editor.putLong(key, value)
            }
        }
        val FLOAT: PrefsTypeAdapter<Float> = object : PrefsTypeAdapter<Float>() {
            override fun parse(input: String) = input.toFloatOrNull()
            override fun save(editor: SharedPreferences.Editor, key: String, value: Float) {
                editor.putFloat(key, value)
            }
        }
        val STRING: PrefsTypeAdapter<String> = object : PrefsTypeAdapter<String>() {
            override fun parse(input: String) = input.ifEmpty { null }
            override fun save(editor: SharedPreferences.Editor, key: String, value: String) {
                editor.putString(key, value)
            }
        }
    }
}


// 泛型配置类
data class InputDialogConfig<T>(
    val titleRes: Int,
    val messageRes: Int,
    val spKey: String,
    val defaultValue: T,
    val typeAdapter: PrefsTypeAdapter<T>,
    val inputHint: String = "请输入",
    val errorTitleRes: Int = R.string.input_format_error,
    val errorMessageRes: Int = R.string.invalid_format
)

// 增强版弹窗构建器
class InputDialogBuilder<T>(
    private val itemData: InitView.ItemData,
    val context: SettingsActivity,
    private val config: InputDialogConfig<T>
) {
    private val sp = context.getSP()!!

    fun show(onSuccess: (T) -> Unit = {}) {
        val currentValue = when (config.defaultValue) {
            is Int -> sp.getInt(config.spKey, config.defaultValue as Int).toString()
            is Long -> sp.getLong(config.spKey, config.defaultValue as Long).toString()
            is Float -> sp.getFloat(config.spKey, config.defaultValue as Float).toString()
            is String -> sp.getString(config.spKey, config.defaultValue as String) ?: ""
            else -> throw IllegalArgumentException("Unsupported type")
        }
        MIUIDialog(context) {
            var inputStr = ""
            var emptyFlag = false
            setTitle(config.titleRes)
            setMessage(config.messageRes)
            setEditText(
                text = currentValue,
                hint = config.inputHint,
            ) {
                emptyFlag = if (it == "") {
                    true
                } else {
                    false
                }
                inputStr = it
            }
            setLButton(R.string.cancel) { dismiss() }
            setRButton(R.string.Done) {
                if (inputStr == "") {
                    if (emptyFlag) {
//                    restoredefault
                        Toast.makeText(context, "restore default", Toast.LENGTH_SHORT).show()
                    }
                    dismiss()
                    return@setRButton
                }
                handleInput(inputStr, onSuccess)
            }
        }.show()
    }

    private fun handleInput(input: String, onSuccess: (T) -> Unit) {
        config.typeAdapter.parse(input)?.let { parsedValue ->
            sp.edit().also { editor ->
                config.typeAdapter.save(editor, config.spKey, parsedValue)
            }.apply()
            onSuccess(parsedValue)
        } ?: showErrorDialog()
    }

    private fun showErrorDialog() {
        MIUIDialog(context) {
            setCancelable(false)
            setTitle(config.errorTitleRes)
            setMessage(config.errorMessageRes)
            setRButton(R.string.Done) { dismiss() }
        }.show()
    }
}

// 使用示例
//fun showSettingsDialogs(activity: Activity) {
//    // Int类型示例
//    InputDialogBuilder(
//        activity, InputDialogConfig(
//            titleRes = R.string.max_year_title,
//            messageRes = R.string.max_year_tips,
//            spKey = "max_year",
//            defaultValue = 2099,
//            typeAdapter = PrefsTypeAdapter.INT,
//            inputType = InputType.TYPE_CLASS_NUMBER
//        )
//    ).show()

// Float类型示例
//    InputDialogBuilder(
//        activity, InputDialogConfig(
//            titleRes = R.string.temperature_setting,
//            messageRes = R.string.temperature_tips,
//            spKey = "target_temp",
//            defaultValue = 36.5f,
//            typeAdapter = PrefsTypeAdapter.FLOAT,
//            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
//        )
//    ).show()
//
//    // Long类型示例
//    InputDialogBuilder(
//        activity, InputDialogConfig(
//            titleRes = R.string.file_size_limit,
//            messageRes = R.string.file_size_tips,
//            spKey = "max_file_size",
//            defaultValue = 1048576L,
//            typeAdapter = PrefsTypeAdapter.LONG,
//            inputHint = "单位：字节"
//        )
//    ).show()
//
//    // String类型示例
//    InputDialogBuilder(
//        activity, InputDialogConfig(
//            titleRes = R.string.user_name,
//            messageRes = R.string.name_tips,
//            spKey = "user_name",
//            defaultValue = "默认用户",
//            typeAdapter = PrefsTypeAdapter.STRING,
//            inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
//        )
//    ).show()
//}

// DSL扩展（可选）
//fun <T> Context.inputDialog(
//    config: InputDialogConfig<T>.() -> Unit
//): InputDialogBuilder<T> {
//    val dialogConfig = InputDialogConfig<T>().apply(config)
//    return InputDialogBuilder(this, dialogConfig)
//}
//
//// DSL使用示例
//activity.inputDialog<Float> {
//    titleRes = R.string.volume_setting
//    messageRes = R.string.volume_tips
//    spKey = "target_volume"
//    defaultValue = 0.8f
//    typeAdapter = PrefsTypeAdapter.FLOAT
//    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
//}.show()
