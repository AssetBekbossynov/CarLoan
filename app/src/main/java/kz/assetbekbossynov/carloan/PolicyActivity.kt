package kz.assetbekbossynov.carloan

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.expandable_item.view.*
import kotlinx.android.synthetic.main.activity_policy.disclaimer
import kotlinx.android.synthetic.main.activity_policy.econsent
import kotlinx.android.synthetic.main.activity_policy.faq
import kotlinx.android.synthetic.main.activity_policy.marketing
import kotlinx.android.synthetic.main.activity_policy.privacy
import kotlinx.android.synthetic.main.activity_policy.rates
import kotlinx.android.synthetic.main.activity_policy.responsible
import kotlinx.android.synthetic.main.activity_policy.terms
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.title as titleLayout
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class PolicyActivity : AppCompatActivity() {

    var text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy)

        Fabric.with(this, Crashlytics())

        Answers.getInstance().logCustom(CustomEvent("Policy_page"))

        rates.div_title.text = "Rates and Fees"
        terms.div_title.text = "Terms of Use"
        privacy.div_title.text = "Privacy Policy"
        econsent.div_title.text = "E-Consent"
        responsible.div_title.text = "Responsible Lending  Policy"
        marketing.div_title.text = "Marketing Practices"
        faq.div_title.text = "FAQ"
        disclaimer.div_title.text = "Disclaimer and APR Representative "


        rates.info.text = getTextFromFile("rates.txt")
        terms.info.text = getTextFromFile("terms.txt")
        privacy.info.text = getTextFromFile("privacy.txt")
        econsent.info.text = getTextFromFile("econsent.txt")
        responsible.info.text = getTextFromFile("responsible.txt")
        marketing.info.text = getTextFromFile("marketing.txt")
        faq.info.text = getTextFromFile("faq.txt")
        disclaimer.info.text = getTextFromFile("disclaimer.txt")


        rates.setOnClickListener { toggleLayout(rates) }
        terms.setOnClickListener { toggleLayout(terms) }
        privacy.setOnClickListener { toggleLayout(privacy) }
        econsent.setOnClickListener { toggleLayout(econsent) }
        responsible.setOnClickListener { toggleLayout(responsible) }
        marketing.setOnClickListener { toggleLayout(marketing) }
        faq.setOnClickListener { toggleLayout(faq) }
        disclaimer.setOnClickListener { toggleLayout(disclaimer) }

        titleLayout.text = "Privacy policy"

        backbutton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun toggleLayout(view: View){
        view.div_content.toggle()
        if (view.div_content.isExpanded){
            view.div_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up, 0)
        }else{
            view.div_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down, 0)
        }
    }

    fun getTextFromFile(path: String): String {

        var reader: BufferedReader? = null

        try {
            reader = BufferedReader(
                    InputStreamReader(assets.open(path)));

            text = reader.readLines().joinToString("\n")

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                }
            }
        }
        return text
    }
}
