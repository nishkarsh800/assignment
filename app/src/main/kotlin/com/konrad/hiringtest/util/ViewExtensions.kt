package com.konrad.hiringtest.util

import android.content.res.Resources

/**
 * Takes a dp value as a float and convert it to px as an int.
 */
fun Float.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()