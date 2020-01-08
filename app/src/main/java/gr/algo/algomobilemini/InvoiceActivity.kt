package gr.algo.algomobilemini
//----------WHEN IS THIRD HE BECOMES CUSTOMER AND SUBSIDIARY BECOMES NULL AND THE REAL CUSTOMER BECOMES SHTPTOPERID AND IT'S SUBSIDIARY SHPTOADDID----------



//import android.app.Fragment
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_invoice.*
import kotlinx.android.synthetic.main.fragment_tab2.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class InvoiceActivity : FragmentActivity(),Tab1Fragment.OnItemSelectedListener,
        lineItem.OnAcceptListener,lineItem.OnDeleteListener,Tab2Fragment.OnInsertListener,Tab2Fragment.OnDiscardListener,Tab2Fragment.OnPrintListener {

    var finDoc:FinDoc
    var finDocLines= mutableListOf<FinDocLine>()
    var selectedItemId:Int=-1
    var basket: Tab2Fragment= Tab2Fragment()
    var mode:Int=0
    var customer:Customer?=null
    var hasChanged:Boolean=false

    init {
        this.finDoc=FinDoc(ftrdate = nowToString() )

        basket.finDocLines=finDocLines

    }







    fun nowToString():String{
        var answer:String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()

            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.")
            answer =  current.format(formatter)
            Log.d("answer",answer)

        } else {
            var date = Date();

            val formatter = SimpleDateFormat("MMM dd yyyy")
            answer = formatter.format(date)
            Log.d("answer",answer)

        }
        return answer

    }


    override  fun onItemSelected(position:Int,iteId:Int):Int{

        selectedItemId=iteId
        return  position

    }


    override fun onAccept(finDocLine: FinDocLine,position: Int): Unit {
        if (position==-1)
        {finDocLines.add(finDocLine)}
        else
        {
            finDocLines!![position].firstQty=finDocLine.firstQty
            finDocLines!![position].price=finDocLine.price
            finDocLines!![position].discount=finDocLine.discount
            finDocLines!![position].secDiscount=finDocLine.secDiscount
            finDocLines!![position].netValue=finDocLine.netValue
            finDocLines!![position].vatValue=finDocLine.vatValue


        }
        hasChanged=true
        basket.acceptButton2.isEnabled=true

        basket.printerButton.isEnabled=false

        basket.notifyChanges()

    }


    override  fun onDelete(position: Int){

        hasChanged=true
        basket.printerButton.isEnabled=false
        basket.acceptButton2.isEnabled=true
        basket.notifyChanges()
    }


    override fun onInsert(netValue: Float, vatValue: Float, totalValue: Float,totFirstQty:Float,totSecQty:Float) {

        val builder = AlertDialog.Builder(basket.context)


        val ft:()->Unit={
                onPrint(basket.context)
                finDocLines.clear()
                val i = Intent(this.baseContext,if (mode==0) RouteActivity::class.java else InvoiceListActivity::class.java)
                this.baseContext.startActivity(i)

        }

        builder.setTitle("Επιβεβαίωση καταχώρησης")
        builder.setMessage("Nα  καταχωρηθεί το παραστατικό;") // add the buttons
        builder.setPositiveButton("Ναι", DialogInterface.OnClickListener { dialog, id ->
            finDoc.netValue = netValue
            finDoc.vatAmount = vatValue
            finDoc.totAmount = totalValue
            finDoc.totFirstQty=totFirstQty
            finDoc.totSecQty=totSecQty
            var isUpdate=false
            val handler = MyDBHandler(context = this.baseContext, version = 1, name = null, factory = null)

            if (mode == 1)
            {
                var query="delete from storetradelines where ftrid=" + finDoc.id.toString()
                handler.insertUpdate(query)
                query="delete from fintrade where id=" + finDoc.id.toString()
                handler.insertUpdate(query)
                isUpdate=true
            }


            if (finDoc.third!=null) {
                finDoc.customer = finDoc.third!!
                finDoc.subsidiary = null
            }

            val ftrid=handler.insertInvoice(finDoc,isUpdate)
            handler.insertLines(ftrid,finDocLines)

            val builder1=AlertDialog.Builder(basket.context)
            builder1.setTitle("Εκτύπωση παραστατικού")
            builder1.setMessage("Να εκτυπωθεί το παραστατικό;")
            builder1.setPositiveButton("Ναι",{dialog,id->
                onPrint(basket.context)
                ft()
            })
            builder1.setNegativeButton("Οχι",{dialog,id->
                 ft()
            })
            val dialog1=builder1.create()
            dialog1.show()



        })
        builder.setNegativeButton("Όχι",null) // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()







    }

    override fun onDiscard() {
        val builder = AlertDialog.Builder(basket.context)
        if (mode==0) {
            builder.setTitle("Επιβεβαίωση εξόδου")
            builder.setMessage("Το παραστατικοό δε θα καταχωρηθεί!! Επιβεβαίωση!") // add the buttons
            builder.setPositiveButton("Έξοδος", DialogInterface.OnClickListener { dialog, id ->
                finDocLines.clear()
                val i = Intent(basket.context, RouteActivity::class.java)
                basket.context.startActivity(i)

            })

            builder.setNegativeButton("Επιστροφή", null) // create and show the alert dialog
        }
        else
        {
            builder.setTitle("Επιβεβαίωση διαγραφής")
            builder.setMessage("Το παραστατικό θα διαγραφεί!! Επιβεβαίωση!") // add the buttons
            builder.setPositiveButton("Διαγραφή", DialogInterface.OnClickListener { dialog, id ->
                val handler = MyDBHandler(context = this.baseContext, version = 1, name = null, factory = null)
                var query="delete from storetradelines where ftrid=" + finDoc.id.toString()
                handler.insertUpdate(query)
                query="delete from fintrade where id=" + finDoc.id.toString()
                handler.insertUpdate(query)
                val i = Intent(basket.context, InvoiceListActivity::class.java)
                basket.context.startActivity(i)

            })

            builder.setNegativeButton("Επιστροφή", null) // create and show the alert dialog
        }
        val dialog = builder.create()
        dialog.show()


    }



    override fun onPrint(context: Context)
    {

        val bt=BTPrinterBixolon(context)


        val handler = MyDBHandler(context = context, version = 1, name = null, factory = null)
        val company=handler.getCompanyData()
        bt.connect(company, finDoc, finDocLines, bt::invoice)



    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)
        //setSupportActionBar(toolbar)

        val extras=intent.extras
        mode=extras.getInt("mode")
        finDoc.customer=extras?.getSerializable("customer") as Customer??: Customer()

        finDoc.dsrId=extras?.getInt("dsrtype")?:0
        finDoc.isCash=extras?.getInt("iscash")?:-1
        finDoc.deliveryAddress=extras?.getString("deliveryaddress")
        finDoc.comments=extras?.getString("comments")

        val docSeries:DocSeries?=extras?.getSerializable("docseries") as DocSeries?
        if(docSeries!=null && mode==0) {
            finDoc.shortDescr = docSeries.shortDescr
            finDoc.typeDescr = docSeries.description
            finDoc.copies = docSeries.copies
        }





        val handler=MyDBHandler(context=this.baseContext,version = 1,name=null,factory = null)
        val salesmanId=handler.getSalesman()
        finDoc.salesmanId=salesmanId
        finDoc.third=extras.getSerializable("third") as Customer?

        finDoc.subsidiary=extras?.getSerializable("subsidiary") as Subsidiary?



        basket.vatStatus=finDoc.customer?.vatstatusid







        if (finDoc.third!=null && mode==0)
        {
            //val c=handler.getCustomerById(finDoc.cusId)!!
            finDoc.shpToPerId=finDoc.customer.erpid
            finDoc.shpToAddid=finDoc.subsidiary?.erpId


        }


        if (mode==1)
        {
            mode=1
            finDoc=handler.getInvoiceById(extras.getInt("ftrid"))
            finDocLines=handler.getInvoiceLines(extras.getInt("ftrid"))
            basket.finDocLines=finDocLines

        }


        setupViewPager()



    }

    override fun onAttachFragment(fragment: Fragment) {

        if (fragment is Tab1Fragment) {

            fragment.setOnItemSelectedListener(this)
        }

        if (fragment is Tab2Fragment) {
            fragment.setOnInsertListener(this)
            fragment.setOnDiscardListener(this)
            fragment.setOnPrintListener(this)
        }



        if (fragment is lineItem) {

            fragment.setOnAcceptListener(this)
            fragment.setOnDeleteListener(this)
        }


    }


    override fun onBackPressed() {
        super.onBackPressed()
        val i:Intent=if (mode==0) Intent(this, RouteActivity::class.java) else Intent(this, InvoiceListActivity::class.java)
        startActivity(i)
    }




    private fun configureTabLayout(){

        tab_layout.addTab(tab_layout.newTab().setText("Είδη"))
        tab_layout.addTab(tab_layout.newTab().setText("Καλάθι"))
        val adapter = TabPagerAdapter(supportFragmentManager,
                tab_layout.tabCount)
        adapter.finDocLines=finDocLines
        pager.adapter = adapter
        pager.addOnPageChangeListener(
                TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })


    }

    private fun setupViewPager(){
        //var firstFragment=Tab1Fragment()
        //var secondFragment=Tab2Fragment()
        //var lineItemFragment=lineItem()
        var firstFragment=ContainerFragment1()
        var secondFragment=ContainerFragment2()
        secondFragment.basket=basket
        tab_layout.addTab(tab_layout.newTab().setText("Είδη"))
        tab_layout.addTab(tab_layout.newTab().setText("Καλάθι"))
        val adapter = TabPagerAdapter(supportFragmentManager,
                tab_layout.tabCount)
        adapter.addFragment(firstFragment)
        //adapter.addFragment(basket)
        adapter.addFragment(secondFragment)
        adapter.finDocLines=finDocLines
        pager.adapter = adapter
        pager.addOnPageChangeListener(
                TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {

                basket.notifyChanges()
                pager.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

                basket.notifyChanges()

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })

    }
}