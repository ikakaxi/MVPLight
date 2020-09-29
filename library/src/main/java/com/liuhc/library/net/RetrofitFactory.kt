package com.liuhc.library.net

import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * 描述:
 * 作者:liuhaichao
 * 创建日期：2019-11-04 on 14:27
 */
class RetrofitFactory {

    //baseUrl
    private lateinit var baseUrl: String

    //单位:秒
    private var timeOutSecond: Long = 10

    //拦截器
    private var interceptors: Array<out Interceptor> = emptyArray()

    //CallAdapter
    private var callAdapterFactories: Array<out CallAdapter.Factory> = emptyArray()

    //Converter
    private var convertersFactories: Array<out Converter.Factory> = emptyArray()

    //Cache
    private var cache: Cache? = null

    //Https证书
    private var certificates: Array<out InputStream> = emptyArray()

    //Https密钥
    private var keyStore: InputStream? = null

    //Https密码
    private var keyStorePassword: String? = null

    //如果该值不为空将会使用该值
    private var okHttpClient: OkHttpClient? = null

    fun baseUrl(baseUrl: String): RetrofitFactory {
        this.baseUrl = baseUrl
        return this
    }

    fun timeOutSecond(timeOutSecond: Long): RetrofitFactory {
        this.timeOutSecond = timeOutSecond
        return this
    }

    fun interceptors(vararg interceptors: Interceptor): RetrofitFactory {
        this.interceptors = interceptors
        return this
    }

    fun callAdapterFactories(vararg callAdapterFactories: CallAdapter.Factory): RetrofitFactory {
        this.callAdapterFactories = callAdapterFactories
        return this
    }

    fun convertersFactories(vararg convertersFactories: Converter.Factory): RetrofitFactory {
        this.convertersFactories = convertersFactories
        return this
    }

    fun cache(cache: Cache): RetrofitFactory {
        this.cache = cache
        return this
    }

    fun certificates(vararg certificates: InputStream): RetrofitFactory {
        this.certificates = certificates
        return this
    }

    fun keyStore(keyStore: InputStream): RetrofitFactory {
        this.keyStore = keyStore
        return this
    }

    fun keyStorePassword(keyStorePassword: String): RetrofitFactory {
        this.keyStorePassword = keyStorePassword
        return this
    }

    /**
     * 根据已有的Retrofit构建一个新的Retrofit
     */
    fun create(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .apply {
                callAdapterFactories.map(::addCallAdapterFactory)
                convertersFactories.map(::addConverterFactory)
            }
            .client(okHttpClient ?: createOkHttpClient())
            .build()
    }

    /**
     * 默认的构建OkHttpClient的方法
     */
    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .installHttpsCertificates()
            .readTimeout(timeOutSecond, TimeUnit.SECONDS)
            .writeTimeout(timeOutSecond, TimeUnit.SECONDS)
            .connectTimeout(timeOutSecond, TimeUnit.SECONDS)
            .apply {
                interceptors.map(::addInterceptor)
            }
            .build()
    }

    @Suppress("DEPRECATION")
    private fun OkHttpClient.Builder.installHttpsCertificates(): OkHttpClient.Builder {
        if (certificates.isEmpty()) return this
        val x509TrustManager: X509TrustManager = prepareTrustManager(*certificates).fetch()
        val keyManagers = prepareKeyManager(keyStore, keyStorePassword)
        val sslContext = SSLContext.getInstance("TLS")
        val trustManager = X509TrustManagerImpl(x509TrustManager)
        sslContext.init(keyManagers, arrayOf<TrustManager>(trustManager), null)
        return sslSocketFactory(sslContext.socketFactory)
    }

    private fun prepareKeyManager(keyInput: InputStream?, password: String?): Array<KeyManager>? {
        keyInput ?: return null
        password ?: return null
        val keyStore = KeyStore.getInstance("BKS")
        keyStore.load(keyInput, password.toCharArray())
        val keyManagerFactory =
            KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(keyStore, password.toCharArray())
        return keyManagerFactory.keyManagers
    }

    private fun prepareTrustManager(vararg certificates: InputStream): Array<TrustManager> {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null)
        certificates.forEachIndexed { index, certificate ->
            certificate.use {
                val certificateAlias = index.toString()
                keyStore.setCertificateEntry(
                    certificateAlias,
                    certificateFactory.generateCertificate(it)
                )
            }
        }
        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)
        return trustManagerFactory.trustManagers
    }

    private inline fun <reified T> Array<*>.fetch(): T = first { it is T } as T

    private inner class X509TrustManagerImpl(private val localTrustManager: X509TrustManager) :
        X509TrustManager {

        private val defaultTrustManager: X509TrustManager

        init {
            val trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            @Suppress("CAST_NEVER_SUCCEEDS")
            trustManagerFactory.init(null as? KeyStore)
            defaultTrustManager = trustManagerFactory.trustManagers.fetch()
        }

        override fun checkClientTrusted(chain: Array<out X509Certificate>, authType: String?) {
            println("checkClientTrusted\tchain:${chain.contentToString()}\tauthType:$authType")
        }

        override fun checkServerTrusted(chain: Array<out X509Certificate>, authType: String?) {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType)
            } catch (ce: CertificateException) {
                localTrustManager.checkServerTrusted(chain, authType)
            }
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
    }

}