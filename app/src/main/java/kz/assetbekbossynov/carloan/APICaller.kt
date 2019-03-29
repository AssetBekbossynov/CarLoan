package kz.assetbekbossynov.payday

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APICaller {

    var paydayAPI: PayDayService
    var installmentAPI: InstallmentService

    init {

        val gson = GsonBuilder().create()

        val interceptor = HttpLoggingInterceptor()

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(interceptor)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://leadsgate.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient.build())
                .build()


        installmentAPI = retrofit.create(InstallmentService::class.java)
        paydayAPI = retrofit.create(PayDayService::class.java)
    }

    fun getInstallmentApi(): InstallmentService{
        return installmentAPI
    }

    fun getPayDayApi(): PayDayService{
        return paydayAPI
    }
}