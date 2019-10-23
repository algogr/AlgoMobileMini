package gr.algo.algomobilemini

//import android.app.Fragment
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_invoice.*

class InvoiceActivity : FragmentActivity(),Tab1Fragment.OnItemSelectedListener,
        lineItem.OnAcceptListener,lineItem.OnDeleteListener,Tab2Fragment.OnInsertListener,Tab2Fragment.OnDiscardListener {

    var finDoc:FinDoc
    var finDocLines= mutableListOf<FinDocLine>()
    var selectedItemId:Int=-1
    var basket: Tab2Fragment= Tab2Fragment()

    init {
        this.finDoc=FinDoc()
        basket.finDocLines=finDocLines

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
        builder.setPositiveButton("Καταχώρηση", DialogInterface.OnClickListener{ dialog, id->
            finDoc.netValue=netValue
            finDoc.vatAmount=vatValue
            finDoc.totAmount=totalValue
            val handler=MyDBHandler(context=this.baseContext,version = 1,name=null,factory = null)
            val ftrid=handler.insertInvoice(finDoc)
            handler.insertLines(ftrid,finDocLines)
            finDocLines.clear()
            val i = Intent(this.baseContext,RouteActivity::class.java)
            this.baseContext.startActivity(i)

        })
        builder.setNegativeButton("Επιστροφή", null) // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()






    }

    override fun onDiscard() {
        val builder = AlertDialog.Builder(basket.context)
        builder.setTitle("Επιβεβαίωση εξόδου")
        builder.setMessage("Το παραστατικοό δε θα καταχωρηθεί!! Επιβεβαίωση!") // add the buttons
        builder.setPositiveButton("Έξοδος", DialogInterface.OnClickListener{ dialog, id->
            finDocLines.clear()
            val i = Intent(basket.context,RouteActivity::class.java)
            basket.context.startActivity(i)

        })
        builder.setNegativeButton("Επιστροφή", null) // create and show the alert dialog
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