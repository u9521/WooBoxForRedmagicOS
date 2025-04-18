package com.u9521.wooboxforredmagicos.compose

import android.view.View
import android.widget.Switch
import cn.fkj233.ui.activity.data.DataBinding
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.activity.SettingsActivity


class SwitchWithSeekbar(
    private val itemData: InitView.ItemData,
    private val context: SettingsActivity,
    private val config: DataConfig
) {
    private val sp = context.getSP()!!
    fun build() {
        val switchBinding = createSwitchBinding()

        itemData.apply {
            createSwitchComponent(switchBinding)
            createSeekBarComponent(switchBinding)
        }
    }

    private fun createSwitchBinding() = itemData.GetDataBinding(
        defValue = { sp.getBoolean(config.switchKey, false) }
    ) { view, flags, data ->
        when (flags) {
            1 -> (view as Switch).isEnabled = data as Boolean
            2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
        }
    }

    private fun createSwitchComponent(binding: DataBinding.BindingData) =
        itemData.TextSummaryWithSwitch(
            TextSummaryV(
                textId = config.titleResId,
                tips = buildTips()
            ),
            SwitchV(
                config.switchKey,
                dataBindingSend = binding.bindingSend
            )
        )

    private fun createSeekBarComponent(binding: DataBinding.BindingData) =
        itemData.SeekBarWithText(
            config.seekbarKey,
            config.minSteps,
            config.maxSteps,
            config.defaultSteps,
            dataBindingRecv = binding.binding.getRecv(2)
        )

    private fun buildTips() = """
        ${context.getString(R.string.take_effect_after_reboot)}
        ${context.getString(config.summaryResId)}
    """.trimIndent()
}
data class DataConfig(
    val switchKey: String,
    val seekbarKey: String,
    val titleResId: Int,
    val summaryResId: Int,
    val minSteps: Int = 3,
    val maxSteps: Int = 50,
    val defaultSteps: Int = 15
)