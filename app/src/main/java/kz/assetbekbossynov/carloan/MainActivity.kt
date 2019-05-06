package kz.assetbekbossynov.carloan

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Fabric.with(this, Crashlytics())

        pdloan.setOnClickListener {
            val intent = Intent(this, QuestionnaireActivity::class.java)
            Answers.getInstance().logCustom(CustomEvent("PAYDAY_MAINACTIVITY"))
            intent.putExtra("btn", "payday")
            startActivity(intent)
        }

        iloan.setOnClickListener {
            val intent = Intent(this, QuestionnaireActivity::class.java)
            Answers.getInstance().logCustom(CustomEvent("INSTALLMENT_MAINACTIVITY"))
            intent.putExtra("btn", "installment")
            startActivity(intent)
        }

        blog.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        privacy.setOnClickListener {
            val intent = Intent(this, PolicyActivity::class.java)
            startActivity(intent)
        }
    }
}
