package gr.algo.algomobilemini

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*

import kotlinx.android.synthetic.main.activity_invoice_header.*
import kotlinx.android.synthetic.main.content_invoice_header.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class InvoiceHeaderActivity : AppCompatActivity() {

    //TODO("CHECK ID DDE THIRD IS GIVEN")
    //TODO("CHECK ID DDE IF THIRD AND CUSTOMER IS GIVEN")
    var mCustomer:Customer=Customer()
    var mThird:Customer?= null
    var mSubsidiary:Subsidiary?= null
    var subs:MutableList<Subsidiary>?= mutableListOf<Subsidiary>()
    var thirds=mutableListOf<Customer>()


    var ftrDate={
        val answer:String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            //val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.")
            answer =  current.format(formatter)
            Log.d("answer",answer)

        } else {
            val date = Date()
            //val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
            val formatter = SimpleDateFormat("MMM dd yyyy")
            answer = formatter.format(date)
            Log.d("answer",answer)

        }
        answer
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val i= Intent(this,CustomerListActivity::class.java)
        i.putExtra("routeid",mCustomer.routeid)
        startActivity(i)
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_header)
        setSupportActionBar(toolbar)
        setTitle("Στοιχεία Τιμολογίου")

        subSpinner.visibility=View.GONE
        thirdSpinner.visibility=View.GONE



        val bundle = intent.extras
        mCustomer = bundle.getSerializable("customer") as Customer



        val handler=MyDBHandler(context = this.baseContext,version = 1,name=null,factory = null)
        subs=handler.getCustomerSubs(mCustomer.id)
        if (!subs!!.isEmpty()) {
            var subsDescr = Array(subs!!.size){""}

            for (j in 0..subs!!.size-1) {
                subsDescr[j]=subs!![j].descr
            }
            val adapter = ArrayAdapter(
                    this, // Context
                    android.R.layout.simple_spinner_item,
                    subsDescr
            )
            subSpinner.adapter = adapter
            if (subsDescr.size > 0) subSpinner.visibility = View.VISIBLE

        }







        nameTextView.setText(mCustomer.name)
        titleTextView.setText(mCustomer.title)
        addressTextView.setText(mCustomer.address)
        cityTextView.setText(mCustomer.city)
        balanceTextView.setText("ΥΠOΛΟΙΠΟ:${mCustomer.balance}")

        ftrdateTextView.setText(ftrDate())

        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId==R.id.ddeRadioButton)
            {
                thirdSpinner.visibility=View.VISIBLE
                thirds=handler.getThirdCustomers()
                if (!thirds.isEmpty()) {
                    var thirdsDescr = Array(thirds!!.size){""}

                    for (j in 0..thirds!!.size-1) {
                        thirdsDescr[j]=thirds!![j].name



                    }
                    val adapter = ArrayAdapter(
                            this, // Context
                            android.R.layout.simple_spinner_item,
                            thirdsDescr
                    )

                    thirdSpinner.adapter = adapter
                    if (thirdsDescr.size > 0) thirdSpinner.visibility = View.VISIBLE

                }


            }
            else
            {
                thirdSpinner.visibility=View.GONE
            }
        })


        mapButton.setOnClickListener{

            var latitude: Double=0.00
            var longitude: Double=0.00

            var geocodeMatches: List<Address>? = null

            try {
                geocodeMatches = Geocoder(this).getFromLocationName(
                        mCustomer.address+" "+mCustomer.city, 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (geocodeMatches != null) {
                latitude = geocodeMatches[0].latitude
                longitude = geocodeMatches[0].longitude

            }


            val i=Intent(it.context,MapsActivity::class.java)
            i.putExtra("latitude",latitude)
            i.putExtra("longitude",longitude)
            i.putExtra("place",mCustomer.address+" "+mCustomer.city)
            i.putExtra("routeid",mCustomer.routeid)
            startActivity(i)
        }



        fab1.setOnClickListener{
            view->
            val i=Intent(view.context,CustomerActivity::class.java)
            i.putExtra("mode",1)
            i.putExtra("cusid",mCustomer.id)
            startActivity(i)
        }






        fab.setOnClickListener { view ->
            // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //     .setAction("Action", null).show()
            val dsrtda=findViewById<RadioButton>(R.id.tdaRadioButton)
            val dsrda=findViewById<RadioButton>(R.id.daRadioButton)
            val dsrde=findViewById<RadioButton>(R.id.deRadioButton)
            val dsrpist=findViewById<RadioButton>(R.id.pistRadioButton)
            val dsrorder=findViewById<RadioButton>(R.id.orderRadioButton)
            val dsrdde=findViewById<RadioButton>(R.id.ddeRadioButton)
            val dsrtype:()->Int={
                var type:Int=-1
                if (dsrtda.isChecked){type=1}
                if (dsrpist.isChecked){type=2}
                if (dsrda.isChecked){type=3}
                if (dsrde.isChecked){type=4}
                if (dsrorder.isChecked){type=5}
                if (dsrdde.isChecked){type=6}
                type

            }
            val handler=MyDBHandler(context = this.baseContext,version = 1,name=null,factory = null)
            val docSeries=handler.getDocSeriesDetails(dsrtype().toString())

            if (subSpinner.visibility==View.VISIBLE)
                mSubsidiary = subs!![subSpinner.selectedItemPosition]




            if (thirdSpinner.visibility==View.VISIBLE)
                 mThird=thirds!![thirdSpinner.selectedItemPosition]





            val i= Intent(view.context,InvoiceActivity::class.java)
            i.putExtra("ftrdate",findViewById<TextView>(R.id.ftrdateTextView).text)
            i.putExtra("dsrtype",dsrtype())
            i.putExtra("deliveryaddress",deliveryeditText.text.toString())
            i.putExtra("iscash",if (cashcheckBox.isChecked()) 1 else 0 )
            i.putExtra("comments",commentEditText.text.toString())
            i.putExtra("mode",0)
            val bundle=Bundle()
            bundle.putSerializable("customer",mCustomer)
            bundle.putSerializable("docseries",docSeries)
            if (mSubsidiary!=null) bundle.putSerializable("subsidiary",mSubsidiary)
            Log.d("JIM-BEFTHIRD",mThird?.erpid.toString())
            if (mThird!=null) bundle.putSerializable("third",mThird)

            if (!bundle.isEmpty()) i.putExtras(bundle)
            startActivity(i)

        }
    }

}
