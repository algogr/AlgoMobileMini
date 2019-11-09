package gr.algo.algomobilemini


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_tab2.*
import kotlinx.android.synthetic.main.fragment_tab2.view.*
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket;
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Handler
import android.util.Log
//import com.sun.xml.internal.ws.streaming.XMLStreamWriterUtil.getOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*
import java.lang.Compiler.command




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class Tab2Fragment : Fragment() {


    var finDocLines= mutableListOf<FinDocLine>()
    lateinit var selectedItemsListAdapter:FinDocLinesAdapter
    var sumNV:Float=0.00f
    var sumVat:Float=0.00f
    var totalAmount:Float=0.00f
    var vatStatus:Int=-1
    private  var itemFragment=lineItem()

    var insertListener:OnInsertListener?=null
    var discardListener:OnDiscardListener?=null


    lateinit var mBluetoothAdapter: BluetoothAdapter
    lateinit var mmSocket: BluetoothSocket
    lateinit var mmDevice: BluetoothDevice
    lateinit var mmOutputStream: OutputStream
    lateinit var mmInputStream: InputStream
    lateinit var workerThread: Thread

    lateinit var readBuffer: ByteArray
    var readBufferPosition:Int=0
    @Volatile
    var stopWorker:Boolean=false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view= inflater.inflate(R.layout.fragment_tab2, container, false)
        val listview = view.findViewById<ListView>(R.id.listView)
        //finDocLines=arguments.getSerializable("list") as MutableList<FinDocLine>
        selectedItemsListAdapter=FinDocLinesAdapter(this.context,finDocLines)
        //val selectedItemsListAdapter=FinDocLinesAdapter(this.context,finDocLines)
        listview.adapter=selectedItemsListAdapter

        view.sumNValueTextView.text=sumNV.toString()
        view.sumVATTextView.text=sumVat.toString()
        view.sumTotalAmountTextView.text=totalAmount.toString()

        view.acceptButton2.setOnClickListener { v:View->
            insertListener?.onInsert(view.sumNValueTextView.text.toString().toFloat(),view.sumVATTextView.text.toString().toFloat(),
                    view.sumTotalAmountTextView.text.toString().toFloat())
            //TODO("ACTIVATE BLUETOOTH")
            //findBT()
            //openBT()
            //sendData()
           // closeBT()



        }

        view.cancelButton2.setOnClickListener { v:View->
            discardListener?.onDiscard()
        }


        return view
    }



    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)


    }

    fun notifyChanges(){
        selectedItemsListAdapter.notifyDataSetChanged()
        var netValue=0f
        var vat=0f

        for (findocline in finDocLines)
        {
            netValue+=findocline.netValue
            vat+=findocline.vatValue


        }

        sumVat=vat
        sumNV=netValue
        totalAmount=netValue+vat
        view?.sumNValueTextView?.text=sumNV.toString()
        view?.sumVATTextView?.text=sumVat.toString()
        view?.sumTotalAmountTextView?.text=totalAmount.toString()


    }



    inner class FinDocLinesAdapter:BaseAdapter{
        private var finDocLinesList:MutableList<FinDocLine>?
        private var context:Context?=null


        constructor(context:Context,finDocLinesList:MutableList<FinDocLine>?):super(){

            this.context=context

            this.finDocLinesList=finDocLinesList


        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val holder: Tab2Fragment.ViewHolder
            val view: View


            if (convertView==null){

                view= layoutInflater.inflate(R.layout.fragment_tab2_item,parent,false)
                holder= ViewHolder(view)
                view.tag=holder

            }
            else {

                view = convertView
                holder = view.tag as Tab2Fragment.ViewHolder
            }


            holder.position=position


            //holder.iteID.text=finDocLinesList!![position].iteID
            holder.iteCode.text=finDocLinesList!![position].iteCode
            holder.iteDescr.text=finDocLinesList!![position].iteDescription
            holder.firstQty.text=finDocLinesList!![position].firstQty.toString()
            holder.price.text=finDocLinesList!![position].price.toString()
            holder.discount.text=finDocLinesList!![position].discount.toString()
            holder.vtcid.text=finDocLinesList!![position].vtcID.toString()
            holder.netValue.text=finDocLinesList!![position].netValue.toString()





            return view
        }



        override fun getItem(position: Int): Any {
            notifyDataSetChanged()
            return finDocLinesList!![position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return finDocLinesList!!.size
        }

    }



    inner class ViewHolder(view: View?){

        var position:Int
        var iteID:Int
        var iteCode:TextView
        var iteDescr:TextView
        var firstQty:TextView
        var price:TextView
        var discount:TextView
        var vtcid:TextView
        var netValue:TextView
        //var vatAmount:TextView


        init{



            this.position=-1
            this.iteID=-1
            this.vtcid=view?.findViewById<TextView>(R.id.vatTextView) as TextView
            this.iteCode=view?.findViewById<TextView>(R.id.codeTextView) as TextView
            this.iteDescr=view?.findViewById<TextView>(R.id.descrTextView) as TextView
            this.firstQty=view?.findViewById<TextView>(R.id.firstQtyTextView) as TextView
            this.price=view?.findViewById<TextView>(R.id.priceTextView) as TextView
            this.discount=view?.findViewById<TextView>(R.id.discountTextView) as TextView
            this.vtcid=view?.findViewById<TextView>(R.id.vatTextView) as TextView
            this.netValue=view?.findViewById<TextView>(R.id.netValueTextView) as TextView
            //val vat=this.netValue.toString().toFloat()*vtcid/100
            //this.vatAmount=vat.toString() as TextView


            view?.setOnClickListener {
                v:View ->
                val test =this@Tab2Fragment.frameLayout2
                //test.removeAllViews()

                //val itemFragment:Fragment = lineItem()
                val fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction()
                val args=Bundle()
                args.putString("description",finDocLines!![position].iteDescription)
                args.putString("code",finDocLines!![position].iteCode)
                args.putInt("iteId",finDocLines!![position].iteID)
                args.putInt("vtcId",finDocLines!![position].vtcID)
                args.putFloat("firstQty",finDocLines!![position].firstQty)
                args.putFloat("price",finDocLines!![position].price)
                args.putFloat("discount",finDocLines!![position].discount)
                args.putFloat("netValue",finDocLines!![position].netValue)
                args.putFloat("vatValue",finDocLines!![position].vatValue)
                args.putInt("vatstatus",vatStatus)
                args.putInt("mode",1)
                args.putInt("position",position)


                itemFragment.basket=this@Tab2Fragment
                itemFragment.arguments=args

                fragmentTransaction.replace(R.id.containerFrame2, itemFragment)
                fragmentTransaction.addToBackStack(null)




                fragmentTransaction.commit()

            }
        }
    }


    fun setOnInsertListener(listener:OnInsertListener){

        this.insertListener=listener


    }

    fun setOnDiscardListener(listener: OnDiscardListener){

        this.discardListener=listener
    }

    interface OnInsertListener{

        fun onInsert(netValue: Float, vatValue: Float, totalValue: Float):Unit

    }

    interface OnDiscardListener{

        fun onDiscard():Unit

    }



    fun findBT()
    {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

            if (mBluetoothAdapter == null) {
                TODO()
                //myLabel.setText("No bluetooth adapter available")
            }

            if (!mBluetoothAdapter.isEnabled()) {
                val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBluetooth, 0)
            }

            val pairedDevices = mBluetoothAdapter.getBondedDevices()

            if (pairedDevices.size > 0) {
                for (device in pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName() == "imz2200") {
                        mmDevice = device
                        break
                    }
                }
            }

            Log.d("JIM","Bluetooth Device Foung")

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @Throws(IOException::class)
    fun openBT() {
        try {

            // Standard SerialPortService ID
            val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")

            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid)
            mmSocket.connect()
            mmOutputStream = mmSocket.getOutputStream()
            mmInputStream = mmSocket.getInputStream()

            beginListenForData()

            Log.d("JIM","Bluetooth Opened")

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun beginListenForData() {
        try {
            val handler = Handler()

            // this is the ASCII code for a newline character
            val delimiter: Byte = 10

            stopWorker = false
            readBufferPosition = 0
            readBuffer = ByteArray(1024)

            workerThread = Thread(Runnable {
                while (!Thread.currentThread().isInterrupted && !stopWorker) {

                    try {

                        val bytesAvailable = mmInputStream.available()

                        if (bytesAvailable > 0) {

                            val packetBytes = ByteArray(bytesAvailable)
                            mmInputStream.read(packetBytes)

                            for (i in 0 until bytesAvailable) {

                                val b = packetBytes[i]
                                if (b == delimiter) {

                                    val encodedBytes = ByteArray(readBufferPosition)
                                    System.arraycopy(
                                            readBuffer, 0,
                                            encodedBytes, 0,
                                            encodedBytes.size
                                    )

                                    // specify US-ASCII encoding
                                    val data = String(encodedBytes, Charset.forName("UTF-8"))
                                    readBufferPosition = 0

                                    // tell the user data were sent to bluetooth printer device
                                    Log.d("JIM",data)
                                    //handler.post(Runnable { myLabel.setText(data) })

                                } else {
                                    readBuffer[readBufferPosition++] = b
                                }
                            }
                        }

                    } catch (ex: IOException) {
                        stopWorker = true
                    }

                }
            })

            workerThread.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    @Throws(IOException::class)
    fun sendData() {
        try {

            format("! U1 setvar \"device.languages\" \"zpl\"");
            format("^XA");
            format("^MMC,Y");
            format("^MNN^LL1200");
            //format("^CWT,E:TT0003M_.TTF");
            format("^CFT,30,30");
            format("^FO175,100^CI28^ATN,36,20^FH^FD GUY DEBORD ^FS");
            format("^FS");
            format("^XZ");

            Log.d("JIM","SEND")




        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun format(message:String){
        val msg=message.plus("\n")
        //msg += "\n"
        mmOutputStream.write(msg.toByteArray())

    }


    @Throws(IOException::class)
    fun closeBT() {
        try {
            stopWorker = true
            mmOutputStream.close()
            mmInputStream.close()
            mmSocket.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}
