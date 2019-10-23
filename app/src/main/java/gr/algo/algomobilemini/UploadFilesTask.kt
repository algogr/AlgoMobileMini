package gr.algo.algomobilemini

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import okhttp3.*
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference

class UploadFilesTask(val context:UploadDB) : AsyncTask<Int, Int, String>() {

    val mContext=context
    private var result: String? = null
    private val activityReference: WeakReference<UploadDB> = WeakReference(context)


    override fun doInBackground(vararg params: Int?): String? {
        val MEDIA_TYPE_PNG = MediaType.parse("binary")
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "test123")
                .addFormDataPart("file", "algo.sqlite", RequestBody.create(MEDIA_TYPE_PNG,
                        File("/data/data/gr.algo.algomobilemini/databases/algo.sqlite")))
                .build()

        val handler = MyDBHandler(context = mContext, version = 1, name = null, factory = null)
        val address=handler.getSettings(1)
        val port=handler.getSettings(2)
        val serverUrl="http://"+address+":"+port+"/upload"
        val request = Request.Builder()
                .url(serverUrl)
                .post(requestBody)
                .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            result=response.body().toString()

        } else {
            result="Η αποστολή ήταν επιτυχής"

        }

        return result
    }

    protected fun onProgressUpdate(vararg progress: Int) {
    }

    protected override fun onPostExecute(result: String) {
        val activity = activityReference.get()
        if (activity == null || activity.isFinishing) return
        //activity.progressBar.visibility = View.GONE
        //activity.toastMsg=result.let{ it }
        Toast.makeText(activity,result.let{it}, Toast.LENGTH_LONG).show()
    }
}