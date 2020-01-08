package gr.algo.algomobilemini

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager

        adapter = RecyclerAdapter(this)
        recycler_view.adapter = adapter


        Runtime.getRuntime().exec("cp /sdcard/algo.sqlite /data/data/gr.algo.algomobilemini/databases/")

        val dbHandler= MyDBHandler(this,null,null,1)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


    @Throws(IOException::class)
    private fun copyDataBase(dbname:String) {
        // Open your local db as the input stream
        val myInput = getAssets().open(dbname)
        // Path to the just created empty db
        val outFileName = getDatabasePath(dbname)
        // Open the empty db as the output stream
        val myOutput = FileOutputStream(outFileName)
        // transfer bytes from the inputfile to the outputfile
        val buffer = ByteArray(1024)
        val length:Int
        while ((myInput.read(buffer)) > 0)
        {
            myOutput.write(buffer, 0, myInput.read(buffer))
        }
        // Close the streams
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }
}
