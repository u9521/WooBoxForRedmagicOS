package com.u9521.wooboxforredmagicos.compose


import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.activity.SettingsActivity

class LsposedInactiveTip(val IData: InitView.ItemData, val activity: SettingsActivity) {
    private val mViews: InitView.ItemData.() -> Unit
        get() = {
            TextSummaryArrow(
                TextSummaryV(
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
            )
        }

    fun setViews() {
        if (activity.lsposedLoaded) {
            return
        }
        IData.apply(mViews)
    }
}
