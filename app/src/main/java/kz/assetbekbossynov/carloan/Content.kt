package kz.assetbekbossynov.carloan

import com.google.gson.annotations.SerializedName

data class Content(@SerializedName("requested_amount") var requested_amount: List<String>?,
                   @SerializedName("first_name") var first_name: List<String>?,
                   @SerializedName("last_name") var last_name: List<String>?,
                   @SerializedName("zip") var zip: List<String>?,
                   @SerializedName("state") var state: List<String>?,
                   @SerializedName("city") var city: List<String>?,
                   @SerializedName("address") var address: List<String>?,
                   @SerializedName("address_length_months") var address_length_months: List<String>?,
                   @SerializedName("birth_date") var birth_date: List<String>?,
                   @SerializedName("email") var email: List<String>?,
                   @SerializedName("home_phone") var home_phone: List<String>?,
                   @SerializedName("work_phone") var work_phone: List<String>?,
                   @SerializedName("employer") var employer: List<String>?,
                   @SerializedName("job_title") var job_title: List<String>?,
                   @SerializedName("employed_months") var employed_months: List<String>?,
                   @SerializedName("monthly_income") var monthly_income: List<String>?,
                   @SerializedName("pay_date1") var pay_date1: List<String>?,
                   @SerializedName("pay_date2") var pay_date2: List<String>?,
                   @SerializedName("drivers_license_number") var drivers_license_number: List<String>?,
                   @SerializedName("drivers_license_state") var drivers_license_state: List<String>?,
                   @SerializedName("bank_name") var bank_name: List<String>?,
                   @SerializedName("bank_phone") var bank_phone: List<String>?,
                   @SerializedName("bank_aba") var bank_aba: List<String>?,
                   @SerializedName("bank_account_number") var bank_account_number: List<String>?,
                   @SerializedName("ssn") var ssn: List<String>?,
                   @SerializedName("bank_account_length_months") var bank_account_length_months: List<String>?) {
}