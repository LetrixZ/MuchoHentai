package com.letrix.muchohentai.app.util

import android.app.Activity
import android.content.res.Resources
import android.view.View
import timber.log.Timber

object Util {
    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

    fun parseType(type: String, episode: Int?): String {
        return if (type == "Episode") "Episode $episode"
        else "PV Episode $episode "
    }

    fun getLanguage(audio: String, sub: String, expanded: Boolean = false): String {
        return if (sub != "None")
            when (sub) {
                "None" -> if (expanded) "Japanese" else "Raw"
                "Spanish" -> if (expanded) "Spanish subbed" else "Spa Sub"
                "English" -> if (expanded) "English subbed" else "Eng Sub"
                else -> ""
            }
        else when (audio) {
            "English" -> if (expanded) "English dubbed" else "Eng Dub"
            "Spanish" -> if (expanded) "Spanish dubbed" else "Spa Dub"
            else -> if (expanded) "Japanese" else "Raw"
        }
    }

    @Suppress("DEPRECATION")
    fun showSystemUI(activity: Activity) {
        val decorView: View = activity.window.decorView
        val uiOptions = decorView.systemUiVisibility
        var newUiOptions = uiOptions
        newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_LOW_PROFILE.inv()
        newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
        newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()
        newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE.inv()
        newUiOptions = newUiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
        decorView.systemUiVisibility = newUiOptions
    }

    @Suppress("DEPRECATION")
    fun hideSystemUI(activity: Activity) {
        val decorView: View = activity.window.decorView
        val uiOptions = decorView.systemUiVisibility
        var newUiOptions = uiOptions
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_FULLSCREEN
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = newUiOptions
    }
}