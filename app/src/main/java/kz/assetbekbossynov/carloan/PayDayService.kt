package kz.assetbekbossynov.payday

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PayDayService {

    @POST("api/PaydayUS/post")
    fun createLoan(@Header("Content-Type") contentType: String,
                   @Query("requested_amount") requestedAmount: String,
                   @Query("employer") employer: String,
                   @Query("job_title") job_title: String,
                   @Query("employed_months") employed_month: String,
                   @Query("monthly_income") monthly_income: String,
                   @Query("pay_date1") pay_date1: String,
                   @Query("pay_date2") pay_date2: String,
                   @Query("pay_frequency") pay_frequency: String,
                   @Query("drivers_license_number") drivers_license_number: String,
                   @Query("drivers_license_state") drivers_license_state: String,
                   @Query("bank_name") bank_name: String,
                   @Query("bank_phone") bank_phone: String,
                   @Query("bank_aba") bank_aba: String,
                   @Query("bank_account_number") bank_account_number: String,
                   @Query("bank_account_type") bank_account_type: String,
                   @Query("direct_deposit") direct_deposit: String,
                   @Query("first_name") first_name: String,
                   @Query("last_name") last_name: String,
                   @Query("ssn") ssn: String,
                   @Query("birth_date") birth_date: String,
                   @Query("own_home") own_home: String,
                   @Query("address") address: String,
                   @Query("city") city: String,
                   @Query("state") state: String,
                   @Query("zip") zip: String,
                   @Query("email") email: String,
                   @Query("home_phone") home_phone: String,
                   @Query("work_phone") work_phone: String,
                   @Query("best_time_to_call") best_time_to_call: String,
                   @Query("active_military") active_military: String,
                   @Query("address_length_months") address_length_months: String,
                   @Query("bank_account_length_months") bank_account_length_months: String,
                   @Query("income_type") income_type: String,
                   @Query("session_id") session_id: String,
                   @Query("referrer") referrer: String,
                   @Query("domain") domain: String,
                   @Query("user_agent") user_agent: String,
                   @Query("client_ip_address") client_ip_address: String,
                   @Query("affiliate_id") affiliate_id: String,
                   @Query("api_key") api_key: String,
                   @Query("tier_key") tier_key: String): Call<CustomResponse>

}