package com.u9521.wooboxforredmagicos.compose

import android.view.View
import android.widget.Switch
import cn.fkj233.ui.activity.data.DataBinding
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import com.u9521.wooboxforredmagicos.activity.SettingsActivity
import com.u9521.wooboxforredmagicos.R

// 类型安全配置体系
sealed class SubItemConfig {
    abstract val textResId: Int

    data class Switch(
        override val textResId: Int,
        val switchKey: String
    ) : SubItemConfig()

    data class Arrow(
        override val textResId: Int,
        val onClick: () -> Unit
    ) : SubItemConfig()
}

data class SwitchGroupConfig(
    val masterKey: String,
    val masterTextRes: Int,
    val subItems: List<SubItemConfig>,
    val masterColorRes: Int = R.color.purple_700,
)

class SwitchGroupBuilder(
    private val itemData: InitView.ItemData,
    private val context: SettingsActivity,
    private val config: SwitchGroupConfig
) {
    private val sp = context.getSP()!!
    fun build() {
        val masterBinding = createMasterBinding()
        addMasterSwitch(masterBinding)
        addSubItems(masterBinding)
    }

    private fun createMasterBinding() = itemData.GetDataBinding(
        defValue = { sp.getBoolean(config.masterKey, false) }
    ) { view, flags, data ->
        when (flags) {
            1 -> (view as Switch).isEnabled = data as Boolean
            2 -> view.visibility =
                if (data as Boolean) View.VISIBLE else View.GONE
        }
    }

    private fun addMasterSwitch(binding: DataBinding.BindingData) {
        itemData.apply {
            TextWithSwitch(
                TextV(textId = config.masterTextRes, colorId = config.masterColorRes),
                SwitchV(config.masterKey, dataBindingSend = binding.bindingSend)
            )
        }
    }

    private fun addSubItems(masterBinding: DataBinding.BindingData) {
        config.subItems.forEach { sub ->
            when (sub) {
                is SubItemConfig.Switch -> addSwitchItem(sub, masterBinding)
                is SubItemConfig.Arrow -> addArrowItem(sub, masterBinding)
            }
        }
    }

    private fun addSwitchItem(
        config: SubItemConfig.Switch,
        masterBinding: DataBinding.BindingData
    ) {
        itemData.apply {
            TextWithSwitch(
                TextV(textId = config.textResId),
                SwitchV(config.switchKey),
                dataBindingRecv = masterBinding.binding.getRecv(2)
            )
        }
    }

    private fun addArrowItem(
        config: SubItemConfig.Arrow,
        masterBinding: DataBinding.BindingData
    ) {
        itemData.apply {
            TextSummaryArrow(
                TextSummaryV(
                    textId = config.textResId,
                    onClickListener = { config.onClick() }
                ),
                dataBindingRecv = masterBinding.binding.getRecv(2)
            )
        }
    }
}

// 使用示例
//InitView.ItemData.() -> Unit = {
//    SwitchGroupBuilder(this, SwitchGroupConfig(
//        masterKey = "custom_clock_switch",
//        masterTextRes = R.string.custom_clock_switch,
//        subItems = listOf(
//            SubItemConfig.Switch(
//                textResId = R.string.status_bar_time_year,
//                switchKey = "status_bar_time_year"
//            ),
//            SubItemConfig.Arrow(
//                textResId = R.string.setting_Fun_override_maxyear,
//                onClick = {
//                    // 自定义点击处理逻辑
//                    startActivity(Intent(context, YearSettingActivity::class.java))
//                }
//            ),
//            SubItemConfig.Switch(
//                textResId = R.string.status_bar_time_month,
//                switchKey = "status_bar_time_month"
//            )
//        )
//    )).build()
//}
