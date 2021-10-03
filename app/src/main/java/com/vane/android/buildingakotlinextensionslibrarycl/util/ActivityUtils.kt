package com.vane.android.buildingakotlinextensionslibrarycl.util

import android.app.Activity
import android.location.Location
import android.widget.TextView
import androidx.annotation.IdRes

fun Activity.findAndSetText(@IdRes id: Int, text: String) {
    findViewById<TextView>(id).text = text
}

fun Activity.showLocation(@IdRes id: Int, location: Location?) {
    if (location != null) {
        findAndSetText(id, location.toString())
    } else {
        findAndSetText(id, "Location unknown")
    }
}