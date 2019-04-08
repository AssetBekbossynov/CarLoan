package kz.assetbekbossynov.carloan

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Fabric.with(this, Crashlytics())

        pdloan.setOnClickListener {
            val intent = Intent(this, QuestionnaireActivity::class.java)
            intent.putExtra("btn", "payday")
            startActivity(intent)
        }

        iloan.setOnClickListener {
            val intent = Intent(this, QuestionnaireActivity::class.java)
            intent.putExtra("btn", "installment")
            startActivity(intent)
        }

        blog.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }
}
