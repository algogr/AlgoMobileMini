package gr.algo.algomobilemini

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_invoice_header.*
import kotlinx.android.synthetic.main.content_invoice_header.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class InvoiceHeaderActivity : AppCompatActivity() {

    var cusId:Int=-1
    var vatStatusId=-1

    var ftrDate={
        var answer:String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            //val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.")
            answer =  current.format(formatter)
            Log.d("answer",answer)

        } else {
            var date = Date();
            //val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
            val formatter = SimpleDateFormat("MMM dd yyyy")
            answer = formatter.format(date)
            Log.d("answer",answer)

        }
        answer
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i= Intent(this,RouteActivity::class.java)

        startActivity(i)
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_header)
        setSupportActionBar(toolbar)
        setTitle("Στοιχεία Τιμολογίου")
        val extras=intent.extras
        val name:String= extras.getString("name")
        val title:String?=extras.getString("title")
        val address:String?=extras.getString("address")
        val city:String?=extras.getString("city")
        this.cusId=extras.getInt("cusid")
        this.vatStatusId=extras.getInt("vatstatusid")



        val nameText=findViewById<TextView>(R.id.nameTextView) as TextView
        val titleText=findViewById<TextView>(R.id.titleTextView) as TextView
        val addressText=findViewById<TextView>(R.id.addressTextView) as TextView
        val cityText=findViewById<TextView>(R.id.cityTextView) as TextView
        val ftrdateText=findViewById<TextView>(R.id.ftrdateTextView) as TextView



        nameText.setText(name)
        titleText.setText(title)
        addressText.setText(address)
        cityText.setText(city)

        ftrdateText.setText(ftrDate())



        fab1.setOnClickListener{
            view->
            val i=Intent(view.context,CustomerActivity::class.java)
            i.putExtra("mode",1)
            i.putExtra("cusid",this.cusId)
            startActivity(i)
        }






        fab.setOnClickListener { view ->
            // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //     .setAction("Action", null).show()
            val dsrtda=findViewById<RadioButton>(R.id.tdaRadioButton)
            val dsrda=findViewById<RadioButton>(R.id.daRadioButton)
            val dsrpist=findViewById<RadioButton>(R.id.pistRadioButton)
            val dsrorder=findViewById<RadioButton>(R.id.orderRadioButton)
            val dsrtype:()->Int={
                var type:Int=-1
                if (dsrtda.isChecked){type=1}
                if (dsrda.isChecked){type=2}
                if (dsrpist.isChecked){type=3}
                if (dsrorder.isChecked){type=4}
                type

            }
            val i= Intent(view.context,InvoiceActivity::class.java)
            i.putExtra("ftrdate",findViewById<TextView>(R.id.ftrdateTextView).text)
            i.putExtra("cusid",cusId)
            i.putExtra("dsrtype",dsrtype())
            i.putExtra("vatstatusid",vatStatusId)
            i.putExtra("deliveryaddress",deliveryeditText.text.toString())
            i.putExtra("iscash",if (cashcheckBox.isChecked()) 1 else 0 )
            i.putExtra("comments",commentEditText.text.toString())

            startActivity(i)

        }
    }

}
