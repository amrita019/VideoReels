package com.amrita.reels.utils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object{
        private const val DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val OUTPUT_FORMAT = "dd MMM yyyy - HH:mm"

        /**
         * Convert date from "yyyy-MM-dd'T'HH:mm:ss'Z'" to "dd MMM yyyy - HH:mm" format
         * */
        fun convertDateToReadableFormat(dateString: String): String {
            val originalFormat: DateFormat = SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z, Locale.getDefault())
            val targetFormat: DateFormat = SimpleDateFormat(OUTPUT_FORMAT, Locale.getDefault())
            var output = ""
            try {
                originalFormat.parse(dateString)?.let {
                    output = targetFormat.format(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return output
        }

        fun getSpannableTitleAndText(
            title: String,
            text: String
        ): SpannableStringBuilder {
            val spannable = SpannableStringBuilder(title)
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.append(": $text")
            return spannable
        }


    }

}