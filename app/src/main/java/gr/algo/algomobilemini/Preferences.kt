package gr.algo.algomobilemini

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

import kotlinx.android.synthetic.main.activity_preferences.*
import kotlinx.android.synthetic.main.content_preferences.*

class Preferences : AppCompatActivity(){
    val balanceCheckList= arrayOf("Καμία ενέργεια","Προειδοποίηση","Απαγόρευση")
    var balanceCheck:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        setSupportActionBar(toolbar)
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, balanceCheckList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        balancecheckSpinnerspinner!!.setAdapter(aa)
        val handler=MyDBHandler(context = this.baseContext,version = 1,name=null,factory = null)
        //TODO("CHANGE_DELETE INVOICES")
        val preferences=handler.getPreferences()
        if (preferences.getSerializable("addcustomer") as Int==1) switch1.isChecked
        if (preferences.getSerializable("updatecustomer") as Int==1) switch2.isChecked
        activedocs.text=SpannableStringBuilder(preferences.getSerializable("activedocs") as String??:"")
        defaultdoc.text=SpannableStringBuilder((preferences.getSerializable("defaultdoc") as Int?).toString()?:"")
        balancecheckSpinnerspinner.setSelection(preferences.getSerializable("balancecheck") as Int??:0)

        acceptButton4.setOnClickListener { view ->
            val addCustomer:Int=if(switch1.isChecked) 1 else 0
            val updateCustomer=if(switch1.isChecked) 1 else 0

            val activeDocs:String=activedocs.text.toString()
            val defaultDoc:Int=defaultdoc.text.toString().toInt()

            balanceCheck=balancecheckSpinnerspinner.selectedItemPosition
            val query=" UPDATE preferences SET addcustomer=$addCustomer,updatecustomer=$updateCustomer,activedocs='$activeDocs',defaultdoc=$defaultDoc,balancecheck=$balanceCheck"
            Log.d("JIM",query)
            val handler = MyDBHandler(context = baseContext, version = 1, name = null, factory = null)
            handler.insertUpdate(query)
            handler.close()
            val i = Intent(view.context,MainActivity::class.java)
            view.context.startActivity(i)

        }

        cancelButton4.setOnClickListener{
            val i = Intent(it.context, MainActivity::class.java)
            it.context.startActivity(i)

        }

    }




}
