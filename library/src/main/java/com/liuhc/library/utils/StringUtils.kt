package com.liuhc.library.utils

import java.util.regex.Pattern

object StringUtils {
    fun removeAllBank(str: String?): String {
        var s = ""
        if (str != null) {
            val p = Pattern.compile("\\s*|\t|\r|\n")
            val m = p.matcher(str)
            s = m.replaceAll("")
        }
        return s
    }

    fun removeAllBank(str: String?, count: Int): String {
        var s = ""
        if (str != null) {
            val p = Pattern.compile("\\s{$count,}|\t|\r|\n")
            val m = p.matcher(str)
            s = m.replaceAll(" ")
        }
        return s
    }
}