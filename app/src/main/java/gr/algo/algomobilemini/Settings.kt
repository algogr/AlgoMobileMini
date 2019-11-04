package gr.algo.algomobilemini

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder

import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_settings.*

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)
        setTitle("Ρυθμίσεις")
        val handler = MyDBHandler(context = baseContext, version = 1, name = null, factory = null)
        val address=handler.getSettings(1)
        val port=handler.getSettings(2)
        addressEdit.text= SpannableStringBuilder(address)
        portEdit.text= SpannableStringBuilder(port)

        acceptButton3.setOnClickListener { view ->
            val address=addressEdit.text.toString()
            val port=portEdit.text.toString()
            val query=" UPDATE tabletinfo SET serveraddress='"+address+"',serverport='"+port+"'"

            val handler = MyDBHandler(context = baseContext, version = 1, name = null, factory = null)
            handler.insertUpdate(query)
            handler.close()
            val i = Intent(view.context,MainActivity::class.java)
            view.context.startActivity(i)

        }

        cancelButton3.setOnClickListener{
            val i = Intent(it.context, MainActivity::class.java)
            it.context.startActivity(i)

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val i= Intent(this,MainActivity::class.java)

        startActivity(i)
    }

}
