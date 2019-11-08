package gr.algo.algomobilemini

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_expenses.*
import kotlinx.android.synthetic.main.content_collection_avtivity.*
import kotlinx.android.synthetic.main.content_expenses.*
import kotlinx.android.synthetic.main.content_expenses.acceptButton3
import kotlinx.android.synthetic.main.content_expenses.cancelButton3

class ExpensesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)
        setSupportActionBar(toolbar)
        setTitle("Έξοδα")

        acceptButton3.setOnClickListener{
            val amount = amountEdit2.text.toString().toFloatOrNull()
            val justification=justificationEdit.text.toString()
            if (!isValidAmount(amount)) return@setOnClickListener

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Επιβεβαίωση καταχώρησης")
            builder.setMessage("H είσπραξη θα καταχωρηθεί!! Επιβεβαίωση!") // add the buttons
            builder.setPositiveButton("Καταχώρηση", DialogInterface.OnClickListener { dialog, id ->
                val handler = MyDBHandler(context = this.baseContext, version = 1, name = null, factory = null)
                handler.insertExpense(amount!!,justification)
                val i = Intent(this.baseContext, CashMenuActivity::class.java)
                this.baseContext.startActivity(i)

            })
            builder.setNegativeButton("Επιστροφή", null) // create and show the alert dialog
            val dialog = builder.create()
            dialog.show()

        }


        cancelButton3.setOnClickListener{
            val i = Intent(this.baseContext, CashMenuActivity::class.java)
            this.baseContext.startActivity(i)

        }

    }


    private  fun isValidAmount(amount:Float?):Boolean{
        val amountValidator=fun(value:Float?):Boolean {
            if (amount==null) {
                Toast.makeText(applicationContext, "Δεν καταχωρήθηκε ποσό", Toast.LENGTH_SHORT).show()
                return false
            }


            if (amount<0.01f) {
                Toast.makeText(applicationContext, "To ποσό είναι λάθος", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }
        return amountValidator(amount)
    }

}
