package com.u9521.wooboxforredmagicos.compose

import android.view.View
import android.widget.Switch
import cn.fkj233.ui.activity.data.DataBinding
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.activity.SettingsActivity

// 类型安全配置体系
sealed class SubItemConfig {

    data class Switch(
        val switchKey: String,
        val textResId: Int,
        val textTipsResId: Int? = null
    ) : SubItemConfig()

    data class Arrow(
        val textResId: Int,
        val onClick: () -> Unit
    ) : SubItemConfig()

    data class SeekBar(
        val seekbarKey: String,
        val minSteps: Int = 3,
        val maxSteps: Int = 50,
        val defaultSteps: Int = 15
    ) : SubItemConfig()

    data class Text(
        val textResId: Int,
    ) : SubItemConfig()

    data class TextSummary(
        val textResId: Int,
        val textTipsResId: Int? = null,
    ) : SubItemConfig()
}

data class SwitchGroupConfig(
    val masterKey: String,
    val masterTextRes: Int,
    val masterTipRes: Int?,
    val subItems: List<SubItemConfig>?,
    val subItem: SubItemConfig?,
    val masterColorRes: Int = R.color.purple_700,
) {
    constructor(
        masterKey: String,
        masterTextRes: Int,
        subItem: SubItemConfig,
        masterColorRes: Int = R.color.purple_700,
    ) : this(masterKey, masterTextRes, null, null, subItem, masterColorRes)

    constructor(
        masterKey: String,
        masterTextRes: Int,
        masterTipRes: Int,
        subItem: SubItemConfig,
        masterColorRes: Int = R.color.purple_700,
    ) : this(masterKey, masterTextRes, masterTipRes, null, subItem, masterColorRes)

    constructor(
        masterKey: String,
        masterTextRes: Int,
        subItems: List<SubItemConfig>?,
        masterColorRes: Int = R.color.purple_700,
    ) : this(masterKey, masterTextRes, null, subItems, null, masterColorRes)

    constructor(
        masterKey: String,
        masterTextRes: Int,
        masterTipRes: Int,
        subItems: List<SubItemConfig>?,
        masterColorRes: Int = R.color.purple_700,
    ) : this(masterKey, masterTextRes, masterTipRes, subItems, null, masterColorRes)


}


class SwitchGroupBuilder(
    private val itemData: InitView.ItemData,
    context: SettingsActivity,
    private val config: SwitchGroupConfig
) {
    private val sp = context.getSP()!!
    fun build() {
        val masterBinding = createMasterBinding()
        addMasterSwitch(masterBinding)
        addSubItems(masterBinding)
        addSubItem(masterBinding)
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
            TextSummaryWithSwitch(
                TextSummaryV(
                    textId = config.masterTextRes,
                    tipsId = config.masterTipRes,
                    colorId = config.masterColorRes
                ),
                SwitchV(config.masterKey, dataBindingSend = binding.bindingSend)
            )
        }
    }

    private fun addSubItems(masterBinding: DataBinding.BindingData) {
        config.subItems?.forEach { sub ->
            when (sub) {
                is SubItemConfig.Switch -> addSwitchItem(sub, masterBinding)
                is SubItemConfig.Arrow -> addArrowItem(sub, masterBinding)
                is SubItemConfig.SeekBar -> addSeekBar(sub, masterBinding)
                is SubItemConfig.Text -> addText(sub, masterBinding)
                is SubItemConfig.TextSummary -> addTextSummary(sub, masterBinding)
            }
        }
    }

    private fun addSubItem(masterBinding: DataBinding.BindingData) {
        config.subItem?.let {
            when (it) {
                is SubItemConfig.Switch -> addSwitchItem(it, masterBinding)
                is SubItemConfig.Arrow -> addArrowItem(it, masterBinding)
                is SubItemConfig.SeekBar -> addSeekBar(it, masterBinding)
                is SubItemConfig.Text -> addText(it, masterBinding)
                is SubItemConfig.TextSummary -> addTextSummary(it, masterBinding)
            }
        }
    }

    private fun addSwitchItem(
        config: SubItemConfig.Switch,
        masterBinding: DataBinding.BindingData
    ) {
        itemData.apply {
            TextSummaryWithSwitch(
                TextSummaryV(textId = config.textResId, tipsId = config.textTipsResId),
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

    private fun addSeekBar(
        config: SubItemConfig.SeekBar,
        masterBinding: DataBinding.BindingData
    ) {
        itemData.apply {
            SeekBarWithText(
                config.seekbarKey,
                config.minSteps,
                config.maxSteps,
                config.defaultSteps,
                dataBindingRecv = masterBinding.binding.getRecv(2)
            )
        }
    }

    private fun addText(
        config: SubItemConfig.Text,
        masterBinding: DataBinding.BindingData
    ) {
        itemData.apply {
            Text(
                textId = config.textResId,
                dataBindingRecv = masterBinding.binding.getRecv(2)
            )
        }
    }

    private fun addTextSummary(
        config: SubItemConfig.TextSummary,
        masterBinding: DataBinding.BindingData
    ) {
        itemData.apply {
            TextSummary(
                textId = config.textResId,
                tipsId = config.textTipsResId,
                dataBindingRecv = masterBinding.binding.getRecv(2)
            )
        }
    }
}


