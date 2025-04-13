package com.u9521.wooboxforredmagicos.compose

import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.activity.SettingsActivity

object LsposedInactiveTip {
    fun getTextSumV(activity: SettingsActivity): TextSummaryV? {
        val ts = TextSummaryV(
            textId = R.string.lsposed_inactive_wraning,
            colorId = R.color.red,
            onClickListener = {
                MIUIDialog(activity) {
                    setTitle(R.string.matters_needing_attention)
                    setMessage(
                        R.string.lsposed_inactive_tips
                    )
                    setRButton(R.string.Done) {
                        dismiss()
                    }
                }.show()
            })
        return if (!activity.lsposedLoaded) {
            ts
        } else {
            null
        }
    }
}
