package com.u9521.wooboxforredmagicos.fragment

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.BuildConfig
import com.u9521.wooboxforredmagicos.R

object AboutModule : MyFragment() {
    override val regKey: String
        get() = "about_module"
    override val iData: InitView.ItemData.() -> Unit
        get() = {
            Author(
                authorHead = mactivity!!.getDrawable(R.drawable.app_icon)!!,
                authorName = mactivity!!.getString(R.string.app_name),
                authorTips = BuildConfig.VERSION_NAME + "(" + BuildConfig.BUILD_TYPE + ")",
                onClickListener = {
                    try {
                        Toast.makeText(
                            mactivity!!, "求一个start，Thanks♪(･ω･)ﾉ", Toast.LENGTH_LONG
                        ).show()
                        val uri = Uri.parse("https://github.com/u9521/WooBoxForRedmagicOS/releases")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        mactivity!!.startActivity(intent)
                    } catch (e: Exception) {
                    }
                })
            Line()
            TitleText(text = mactivity!!.getString(R.string.developer))
            Author(
                authorHead = mactivity!!.getDrawable(R.drawable.lt)!!,
                authorName = "乌堆小透明",
                authorTips = "LittleTurtle2333",
                onClickListener = {
                    try {
                        mactivity!!.startActivity(
                            Intent(
                                Intent.ACTION_VIEW, Uri.parse("coolmarket://u/883441")
                            )
                        )
                        Toast.makeText(
                            mactivity!!, "乌堆小透明：靓仔，点个关注吧！", Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(mactivity!!, "本机未安装酷安应用", Toast.LENGTH_SHORT).show()
                        val uri = Uri.parse("http://www.coolapk.com/u/883441")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        mactivity!!.startActivity(intent)
                    }
                })
            Author(
                authorHead = mactivity!!.getDrawable(R.drawable.u9521)!!,
                authorName = "9521",
                authorTips = "u9521",
                onClickListener = {
                    try {
                        Toast.makeText(
                            mactivity!!, "9521：关注了也不一定更新喵", Toast.LENGTH_SHORT
                        ).show()
                        val uri = Uri.parse("https://github.com/u9521")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        mactivity!!.startActivity(intent)
                    } catch (e: Exception) {
                    }
                })
            Line()
            TitleText(text = mactivity!!.getString(R.string.thank_list))
            TextSummaryArrow(
                TextSummaryV(
                    textId = R.string.contributor_list, onClickListener = {
                        try {
                            val uri =
                                Uri.parse("https://github.com/u9521/WooBoxForRedmagicOS/graphs/contributors")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            mactivity!!.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(mactivity!!, "访问失败QAQ", Toast.LENGTH_SHORT).show()
                        }
                    })
            )
            TextSummaryArrow(
                TextSummaryV(
                    textId = R.string.third_party_open_source_statement, onClickListener = {
                        try {
                            val uri =
                                Uri.parse("https://github.com/u9521/WooBoxForRedmagicOS#%E7%AC%AC%E4%B8%89%E6%96%B9%E5%BC%80%E6%BA%90%E5%BC%95%E7%94%A8")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            mactivity!!.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(mactivity!!, "访问失败", Toast.LENGTH_SHORT).show()
                        }
                    })
            )
            Line()
            TitleText(text = mactivity!!.getString(R.string.discussions))
            TextSummaryArrow(TextSummaryV(textId = R.string.qq_channel, onClickListener = {
                try {
                    Toast.makeText(mactivity!!, "还木有QQ群呢", Toast.LENGTH_SHORT).show()
                    return@TextSummaryV
                    val uri =
                        Uri.parse("https://qun.qq.com/qqweb/qunpro/share?_wv=3&_wwv=128&inviteCode=29Mu64&from=246610&biz=ka")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    mactivity!!.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(mactivity!!, "访问失败", Toast.LENGTH_SHORT).show()
                }
            }))
            TextSummaryArrow(
                TextSummaryV(
                    textId = R.string.tg_channel,
                    tipsId = R.string.tg_channel_summary,
                    onClickListener = {
                        Toast.makeText(mactivity!!, "还木有TG群呢", Toast.LENGTH_SHORT).show()
                        return@TextSummaryV
                        try {
                            mactivity!!.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("tg://resolve?domain=simplicityrom")
                                )
                            )
                        } catch (e: Exception) {
                            Toast.makeText(
                                mactivity!!, "本机未安装Telegram应用", Toast.LENGTH_SHORT
                            ).show()
                            val uri = Uri.parse("https://t.me/simplicityrom")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            mactivity!!.startActivity(intent)
                        }
                    })
            )
            TextSummaryArrow(
                TextSummaryV(
                    textId = R.string.issues, tipsId = R.string.issues_url, onClickListener = {
                        try {
                            val uri =
                                Uri.parse("https://github.com/u9521/WooBoxForRedmagicOS/issues")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            mactivity!!.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(mactivity!!, "访问失败", Toast.LENGTH_SHORT).show()
                        }
                    })
            )
            Line()
            TitleText(mactivity!!.getString(R.string.other))
            TextSummaryArrow(
                TextSummaryV(
                    textId = R.string.app_coolapk_url,
                    tipsId = R.string.app_coolapk_url_summary,
                    onClickListener = {
                        Toast.makeText(
                            mactivity!!, "不会上架喵", Toast.LENGTH_SHORT
                        ).show()
                        return@TextSummaryV
                        try {
                            mactivity!!.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("coolmarket://apk/com.lt2333.wooboxforcoloros")
                                )
                            )
                            Toast.makeText(
                                mactivity!!, "恳求一个五星好评，Thanks♪(･ω･)ﾉ", Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(mactivity!!, "本机未安装酷安应用", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            )
            TextSummaryArrow(
                TextSummaryV(
                    textId = R.string.opensource, tipsId = R.string.github_url, onClickListener = {
                        try {
                            val uri =
                                Uri.parse("https://github.com/u9521/WooBoxForRedmagicOS")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            mactivity!!.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(mactivity!!, "访问失败", Toast.LENGTH_SHORT).show()
                        }
                    })
            )
            TextSummaryArrow(
                TextSummaryV(
                    textId = R.string.participate_in_translation,
                    tipsId = R.string.participate_in_translation_summary,
                    onClickListener = {
                        Toast.makeText(
                            mactivity!!, "抱歉，没有精力做i18n喵", Toast.LENGTH_SHORT
                        ).show()
                        return@TextSummaryV
                        try {
                            val uri = Uri.parse("https://crowdin.com/project/wooboxforcoloros")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            mactivity!!.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(mactivity!!, "访问失败", Toast.LENGTH_SHORT).show()
                        }
                    })
            )
        }
}