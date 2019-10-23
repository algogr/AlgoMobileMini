package gr.algo.algomobilemini

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

import android.os.Bundle

import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.widget.Toast


import kotlinx.android.synthetic.main.activity_upload_db.*
import kotlinx.android.synthetic.main.content_upload_db.*
import org.json.JSONArray
import org.json.JSONObject

operator fun JSONArray.iterator(): Iterator<JSONObject> =
        (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()
class UploadDB : AppCompatActivity() {

    lateinit var toastMsg:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_db)
        setSupportActionBar(toolbar)
        setTitle("Επικοινωνία με ERP")


        button.setOnClickListener {
            toastMsg="Η αποστολή ξεκίνησε"
            if (isNetworkAvailable()) {
                UploadFilesTask(this).execute(1)
                Toast.makeText(this,toastMsg, Toast.LENGTH_LONG).show()

            }

            else
                Toast.makeText(baseContext,"Δεν υπάρχει δίκτυο", Toast.LENGTH_LONG).show()


        }
        button2.setOnClickListener {
            if (isNetworkAvailable()) {
                val r=DownloadFilesTask(this).execute(1)


            }

            else
                Toast.makeText(baseContext,"Δεν υπάρχει δίκτυο",Toast.LENGTH_LONG).show()
        }


    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }




    companion object {
        val TAG = "MainActivity"
    }
}