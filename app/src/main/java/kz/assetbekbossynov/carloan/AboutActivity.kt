package kz.assetbekbossynov.payday

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.title as titleLayout

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        Fabric.with(this, Crashlytics())

        backbutton.setOnClickListener {
            onBackPressed()
        }

        val intent = intent

        descTitle.text = intent.getStringExtra("title")
        descContent.text = intent.getStringExtra("content")

        titleLayout.text = "Additional information"
    }
}
