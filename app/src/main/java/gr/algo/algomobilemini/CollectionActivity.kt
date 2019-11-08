package gr.algo.algomobilemini

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_collection_avtivity.*
import kotlinx.android.synthetic.main.content_collection_avtivity.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CollectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_avtivity)
        setSupportActionBar(toolbar)
        setTitle("Εισπράξεις")
        val bundle = intent.extras
        val customer = bundle.getSerializable("customer") as Customer
        customerText.text = customer.name
        var ftrDate = {
            var answer: String
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                //val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.")
                answer = current.format(formatter)
                Log.d("answer", answer)

            } else {
                var date = Date();
                //val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
                val formatter = SimpleDateFormat("MMM dd yyyy")
                answer = formatter.format(date)
                Log.d("answer", answer)

            }
            answer
        }
        dateText.text = ftrDate()

        acceptButton3.setOnClickListener { view ->
            val amount = amountEdit.text.toString().toFloatOrNull()
            if (!isValidAmount(amount)) return@setOnClickListener

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Επιβεβαίωση καταχώρησης")
            builder.setMessage("H είσπραξη θα καταχωρηθεί!! Επιβεβαίωση!") // add the buttons
            builder.setPositiveButton("Καταχώρηση", DialogInterface.OnClickListener { dialog, id ->
                val handler = MyDBHandler(context = this.baseContext, version = 1, name = null, factory = null)
                handler.insertCollection(customer, amount!!)
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

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this.baseContext, CashMenuActivity::class.java)
        this.baseContext.startActivity(i)
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
