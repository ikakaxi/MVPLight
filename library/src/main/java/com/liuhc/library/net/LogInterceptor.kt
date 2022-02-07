package com.liuhc.library.net

import android.util.Log
import com.liuhc.library.ext.format
import com.orhanobut.logger.Logger
import okhttp3.*
import okio.Buffer
import okio.GzipSource
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * 打印网络请求的日志类
 * Created by liuhaichao on 19/9/19.
 *
 * @author liuhc
 */

object LogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //请求发起的时间
        val startTime = System.nanoTime()
        //打印请求数据
        val requestMsg = printRequest(request)
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Throwable) {
            log("${e.message}, request url=${request.url()}\n")
            e.printStackTrace()
            throw e
        }
        //打印返回的数据
        printResponse(System.nanoTime() - startTime, requestMsg, response)
        return response
    }

    private fun isStream(mediaType: MediaType?): Boolean {
        if (mediaType?.subtype()?.contains("plain") == true
            || mediaType?.subtype()?.contains("json") == true
            || mediaType?.subtype()?.contains("html") == true
        ) {
            return false
        }
        return true
    }

    private fun printRequest(request: Request): RequestMsg {
        val url = request.url().toString()
        val method = request.method()
        val headers = getHeadersString(request.headers())
        val mediaType = request.body()?.contentType()
        val requestStrBuffer = StringBuffer()
        requestStrBuffer.append("===>发送请求:$url\n ")
        requestStrBuffer.append("method: $method\n")
        requestStrBuffer.append("headers: $headers")
        mediaType?.let { requestStrBuffer.append("Content-Type: $it\n") }
        var postParam: String? = null
        if (!isStream(mediaType)) {
            //post请求的body请求参数
            postParam = request.body()?.let(::getPostParam)
            postParam?.let { requestStrBuffer.append("请求参数body: $it\n") }
        }
        log(requestStrBuffer.toString(), Log.INFO)
        return RequestMsg(url, method, headers, mediaType, postParam)
    }

    private data class RequestMsg(
        val url: String,
        val method: String,
        val headers: String,
        val mediaType: MediaType?,
        val postBody: String?
    )

    private fun printResponse(time: Long, requestMsg: RequestMsg, response: Response) {
        val requestUrl = requestMsg.url
        val responseUrl = response.request().url().toString()
        val mediaType = response.body()?.contentType()
        val code = response.code()
        var contentLength = response.body()?.contentLength()
        if (contentLength == null || contentLength == -1L) {
            //response.body()?.source()要在response.body()?.string()前面执行
            //否则在response.body()?.string()方法里会关闭source
            val source = response.body()?.source()
            source?.request(java.lang.Long.MAX_VALUE)
            contentLength = source?.buffer?.size() ?: -1L
        }
        val data = getResponseData(response)
        val responseStrBuffer = StringBuffer()
        responseStrBuffer.append("<===接收响应: ")
        //请求的url
        if (requestUrl == responseUrl) {
            responseStrBuffer.append("$requestUrl\n")
        } else {
            responseStrBuffer.append("请求的url:$requestUrl\n")
            responseStrBuffer.append("返回的url:$responseUrl\n")
        }
        responseStrBuffer.append("code: $code\n")
        responseStrBuffer.append("耗时: ${(time / 1e6).format(2)}ms\n")
        responseStrBuffer.append("method: ${requestMsg.method}\n")
        responseStrBuffer.append("request的Headers:\n${requestMsg.headers}")
        //request.headers()自带\n
        responseStrBuffer.append("response的Headers:\n${getHeadersString(response.headers())}")
        requestMsg.mediaType?.let { responseStrBuffer.append("Content-Type: $it\n") }
        if (!isStream(requestMsg.mediaType)) {
            requestMsg.postBody?.let { responseStrBuffer.append("请求参数body: ${requestMsg.postBody}\n") }
        }
        responseStrBuffer.append("数据大小: ${formatBytes(contentLength)}\n")
        if (!isStream(mediaType)) {
            responseStrBuffer.append("数据: $data\n")
        }
        log(responseStrBuffer.toString())
    }

    private fun formatBytes(bytes: Long): String {
        val byteDouble = bytes.toDouble()
        return when {
            bytes / (1024 * 1024) > 0 -> "${(byteDouble / (1024 * 1024)).format(2)} MB"
            bytes / 1024 > 0 -> "${(byteDouble / 1024).format(2)} KB"
            else -> "$bytes byte"
        }
    }

    private fun getHeadersString(headers: Headers?): String {
        if (headers == null) {
            return ""
        }
        val result = StringBuilder()
        var i = 0
        val size = headers.size()
        while (i < size) {
            result.append("\t").append(headers.name(i)).append(": ").append(headers.value(i)).append("\n")
            i++
        }
        return result.toString()
    }

    private val UTF8 = Charset.forName("UTF-8")

    private fun getResponseData(response: Response?): String? {
        if (response?.body() == null) {
            return null
        }
        val source = response.body()!!.source()
        source.request(Long.MAX_VALUE)
        var buffer = source.buffer()
        if ("gzip".equals(response.headers()["Content-Encoding"], ignoreCase = true)) {
            GzipSource(buffer.clone()).use { gzippedResponseBody ->
                buffer = Buffer()
                buffer.writeAll(gzippedResponseBody)
            }
        }
        val charset: Charset = response.body()!!.contentType()?.charset(StandardCharsets.UTF_8)
            ?: StandardCharsets.UTF_8
        return buffer.clone().readString(charset)
    }

    private fun getPostParam(requestBody: RequestBody?): String? {
        if (requestBody == null) {
            return null
        }
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        val charset: Charset = requestBody.contentType()?.charset() ?: UTF8
        return buffer.readString(charset)
    }

    private fun log(msg: String, logLevel: Int = Log.DEBUG) {
        when (logLevel) {
            Log.INFO -> {
                Logger.i(msg)
            }
            else -> {
                Logger.d(msg)
            }
        }
    }
}
