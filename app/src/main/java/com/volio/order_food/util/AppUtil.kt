package com.volio.order_food.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.volio.order_food.R
import java.io.File
import java.time.YearMonth
import java.util.*
import java.util.regex.Pattern

object AppUtil {

    const val TYPE_MP3 = ".mp3"
    const val AudioName = "LastRecord"

    const val VOICE_CHANGER_FOLDER = "AudioNote"
    const val CACHE_FOLDER = ".Voice"
    const val NO_MEDIA_FILE = ".nomedia"

    const val NONE = "none"
    const val START = "start"
    const val STOP = "stop"
    const val RESUME = "RESUME"

    private var mLastClickTime: Long = 0
    val listTheme = mutableListOf(
        R.color.white,
        R.color.colorTextBlack,
        R.color.blue,
        R.color.red,
        R.color.green,
        R.color.purple,
        R.color.yellow,
        R.color.orange,
        R.color.pink
    )
    val listColor =  mutableListOf(
        R.color.note_white,
        R.color.note_blue,
        R.color.note_red,
        R.color.note_green,
        R.color.note_purple,
        R.color.note_yellow,
        R.color.note_orange,
        R.color.note_pink
    )
    fun getWidthScreen(context: Activity): Int {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }


    fun getHeightScreen(context: Activity): Int {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    val currentTime: String
        get() = Calendar.getInstance().time.toString() + ""

    fun clickOneSecond(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
            mLastClickTime = SystemClock.elapsedRealtime()
            return true
        }
        return false
    }

    fun showToast(context: Context?, message: String?) {
        if (clickOneSecond()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun logE(TAG: String?, message: String?) {
        Log.e(TAG, message!!)
    }

    fun showToast(context: Context?, message: Int) {
        if (clickOneSecond()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun numberCart(count: Int): String {
        return if (count < 99) {
            count.toString() + ""
        } else {
            99.toString() + "+"
        }
    }

    fun isStrValid(text: String?): Boolean {
        return text != null && text.isNotEmpty()
    }

    fun isPhoneValid(text: String?): Boolean {
        return text != null && text.isNotEmpty() && text.length >= 9 && text.length <= 11
    }

    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern =
            Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isPassWordValid(password: String): Boolean {
        return password.length > 5
    }

    fun confirmPassWordValid(password: String, rePassword: String): Boolean {
        return password == rePassword.trim { it <= ' ' }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val conMgr =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val i = conMgr.activeNetworkInfo
        if (i == null) {
            showToast(context, R.string.no_internet_connected)
            return false
        }
        if (!i.isConnected) {
            showToast(context, R.string.no_internet_connected)
            return false
        }
        return i.isAvailable
    }

//    fun coppyText(context: Context, text: String?) {
//        val cm =
//            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        cm.text = text
//        showToast(context, R.string.copy_successful)
//    }

    fun countDayInMonth(month: Int, year: Int): Int {
        var daysInMonth: Int = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val yearMonthObject: YearMonth = YearMonth.of(year, month)
            daysInMonth = yearMonthObject.lengthOfMonth()
        } else {
            // Create a calendar object and set year and month
            val mycal: Calendar = GregorianCalendar(year, month, 1)
            // Get the number of days in that month
            daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
        return daysInMonth
    }

    fun deleteFileFromInternalStorage(imagePath: String): Boolean {
        val file = File(imagePath)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }
}