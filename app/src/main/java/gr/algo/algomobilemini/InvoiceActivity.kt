package gr.algo.algomobilemini

//import android.app.Fragment
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_invoice.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class InvoiceActivity : FragmentActivity(),Tab1Fragment.OnItemSelectedListener,
        lineItem.OnAcceptListener,lineItem.OnDeleteListener,Tab2Fragment.OnInsertListener,Tab2Fragment.OnDiscardListener {

    var finDoc:FinDoc
    var finDocLines= mutableListOf<FinDocLine>()
    var selectedItemId:Int=-1
    var basket: Tab2Fragment= Tab2Fragment()
    var mode:Int=0

    init {
        this.finDoc=FinDoc(ftrdate = nowToString() )
        basket.finDocLines=finDocLines

    }







    fun nowToString():String{
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
            finDocLines!![position].netValue=finDocLine.netValue
            finDocLines!![position].vatValue=finDocLine.vatValue


        }
        Log.d("JIM","1")
        basket.notifyChanges()

    }


    override  fun onDelete(position: Int){

        Log.d("JIM","2")
        basket.notifyChanges()
    }


    override fun onInsert(netValue: Float, vatValue: Float, totalValue: Float) {

        val builder = AlertDialog.Builder(basket.context)
        builder.setTitle("Επιβεβαίωση καταχώρησης")
        builder.setMessage("Το παραστατικοό θα καταχωρηθεί!! Επιβεβαίωση!") // add the buttons
        builder.setPositiveButton("Καταχώρηση", DialogInterface.OnClickListener { dialog, id ->
            finDoc.netValue = netValue
            finDoc.vatAmount = vatValue
            finDoc.totAmount = totalValue
            var isUpdate=false
            val handler = MyDBHandler(context = this.baseContext, version = 1, name = null, factory = null)
            Log.d("JIM-MODE",mode.toString())
            if (mode == 1)
            {
                var query="delete from storetradelines where ftrid=" + finDoc.id.toString()
                handler.insertUpdate(query)
                query="delete from fintrade where id=" + finDoc.id.toString()
                handler.insertUpdate(query)
                isUpdate=true
            }

            val ftrid=handler.insertInvoice(finDoc,isUpdate)
            handler.insertLines(ftrid,finDocLines)
            finDocLines.clear()
            val i = Intent(this.baseContext,if (mode==0) RouteActivity::class.java else InvoiceListActivity::class.java)
            this.baseContext.startActivity(i)

        })
        builder.setNegativeButton("Επιστροφή", null) // create and show the alert dialog
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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)
        //setSupportActionBar(toolbar)

        val extras=intent.extras
        finDoc.cusId=extras.getInt("cusid")
        finDoc.dsrId=extras.getInt("dsrtype")
        finDoc.vatStatus=extras.getInt("vatstatusid")
        basket.vatStatus=extras.getInt("vatstatusid")

        finDoc.isCash=extras.getInt("iscash")
        finDoc.deliveryAddress=extras?.getString("deliveryaddress")
        finDoc.comments=extras?.getString("comments")
        val handler=MyDBHandler(context=this.baseContext,version = 1,name=null,factory = null)
        val salesmanId=handler.getSalesman()
        finDoc.salesmanId=salesmanId

        if (extras.getInt("mode")==1)
        {
            mode=1
            finDoc=handler.getInvoiceById(extras.getInt("ftrid"))
            finDocLines=handler.getInvoiceLines(extras.getInt("ftrid"))
            basket.finDocLines=finDocLines
        }

        Log.d("JIM-MODE",mode?.toString())
        setupViewPager()



    }

    override fun onAttachFragment(fragment: Fragment) {

        if (fragment is Tab1Fragment) {

            fragment.setOnItemSelectedListener(this)
        }

        if (fragment is Tab2Fragment) {
            fragment.setOnInsertListener(this)
            fragment.setOnDiscardListener(this)
        }



        if (fragment is lineItem) {

            fragment.setOnAcceptListener(this)
            fragment.setOnDeleteListener(this)
        }


    }


    override fun onBackPressed() {
        //super.onBackPressed()
        //var i:Intent=if (mode==0) Intent(this, RouteActivity::class.java) else Intent(this, InvoiceListActivity::class.java)
        //startActivity(i)
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
                Log.d("JIM","3")
                basket.notifyChanges()
                pager.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                Log.d("JIM","4")
                basket.notifyChanges()

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })

    }
}