package kz.assetbekbossynov.payday

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var list: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Fabric.with(this, Crashlytics())

        list = arrayListOf(getString(R.string.what_is_pd), getString(R.string.put_fast),
                getString(R.string.how_they_work), getString(R.string.unsecured),
                getString(R.string.different), getString(R.string.good_idea))

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

        rv.adapter = AboutAdapter(list, this)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }
}
