package com.liuhc.library.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.IOException
import java.lang.reflect.Type
import kotlin.jvm.Throws

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2019-10-13 on 17:55
 */
object GsonUtil {

    private var gson: Gson? = null

    private fun <T> fromJson(cls: Class<T>?, type: Type?, srcStr: String): T? {
        var result: T? = null
        if (TextUtils.isEmpty(srcStr)) {
            return null
        }
        try {
            result = if (type != null) {
                gson!!.fromJson(srcStr, type)
            } else {
                gson!!.fromJson(srcStr, cls)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    @Synchronized
    fun <T> fromJson(type: Type?, srcStr: String): T? {
        return fromJson(null, type, srcStr)
    }

    @Synchronized
    fun <T> fromJson(cls: Class<T>?, srcStr: String): T? {
        return fromJson(cls, null, srcStr)
    }

    @Synchronized
    fun toJson(`object`: Any?): String {
        return gson!!.toJson(`object`)
    }

    init {
        gson = GsonBuilder().disableHtmlEscaping()
            .registerTypeAdapter(String::class.java, object : TypeAdapter<String?>() {
                @Throws(IOException::class)
                override fun write(out: JsonWriter, value: String?) {
                    if (value == null) {
                        out.value("")
                        return
                    }
                    out.value(value)
                }

                @Throws(IOException::class)
                override fun read(reader: JsonReader): String? {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull()
                        return ""
                    }
                    return reader.nextString()
                }
            }).create()
    }

    /**
     * 请求参数转化成RequestBody
     * @param map
     * @return
     */
    fun getJsonRequestBodyByObject(map: Any): RequestBody {
        val obj: String = toJson(map)
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj)
    }
}