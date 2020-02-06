package gr.algo.algomobilemini

import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import okhttp3.*
import okio.Okio
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DownloadFilesTask(val context:UploadDB): AsyncTask<Int, Int, String>() {

    val mContext=context
    private var result: String? = null
    private val activityReference: WeakReference<UploadDB> = WeakReference(context)


    override fun doInBackground(vararg p0: Int?): String? {

        val dir=File("/data/data/gr.algo.algomobilemini/databases/")
        val handler = MyDBHandler(context = mContext, version = 1, name = null, factory = null)
        val address=handler.getSettings(1)
        val port=handler.getSettings(2)
        val serverUrl="http://"+address+":"+port+"/files/algo.sqlite"


        val client = OkHttpClient()
        val request = Request.Builder().url(serverUrl).build()

        client.newCall(request).enqueue(object: Callback {override fun onFailure(call: Call, e: IOException) {

            result="Υπάρχει πρόβλημα στη σύνδεση με τον server"
            onPostExecute("Υπάρχει πρόβλημα στη σύνδεση με τον server")
        }

            override fun onResponse(call: Call, response: Response) {

                if (response.body()==null) Log.d("JIMNULL","ITS NULLL!!!!")

                val contentType = response.header("content-type", null)
                var ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentType)
                ext = if (ext == null) {
                    ".sqlite"
                } else {
                    ".$ext"
                }

                // use provided name or generate a temp file
                var file: File? = null
                val name="algo"
                file = if (name != null) {
                    val filename = String.format("%s%s", name, ext)
                    File(dir.absolutePath, filename)
                } else {

                    val timestamp=
                            {
                                var answer:String
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val current = LocalDateTime.now()
                                    //val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
                                    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-kkmmss")
                                    answer =  current.format(formatter)


                                } else {
                                    var date = Date();
                                    //val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
                                    val formatter = SimpleDateFormat("yyyyMMdd-kkmmss")
                                    answer = formatter.format(date)


                                }
                                answer
                            }




                    File.createTempFile(timestamp.toString(), ext, dir)
                }

                val result = response.body()
                val sink = Okio.buffer(Okio.sink(file))
                /*
                sink.writeAll(body!!.source())
                sink.close()
                body.close()
                 */

                result?.source().use { input ->
                    sink.use { output ->
                        output.writeAll(input)
                    }
                }

                onPostExecute("Η λήψη ήταν επιτυχής")
            }



        })






        return result.toString()
    }
    val h: Handler = Handler(Looper.getMainLooper());

    protected fun onProgressUpdate(vararg progress: Int) {
    }

    override fun onPostExecute(result: String) {

        if(result!=null) {
            h.post(Runnable() {
                run() {
                    Toast.makeText(context, result , Toast.LENGTH_SHORT).show();
                }
            })
        }
    }


}