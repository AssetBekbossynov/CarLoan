package kz.assetbekbossynov.payday

import com.google.gson.annotations.SerializedName

data class CustomResponse(@SerializedName("status") var status: String,
                          @SerializedName("lead_id") var leadId: String,
                          @SerializedName("price") var price: Double,
                          @SerializedName("url") var url: String,
                          @SerializedName("message") var message: String,
                          @SerializedName("reject_url") var rejectUrl: String) {
}