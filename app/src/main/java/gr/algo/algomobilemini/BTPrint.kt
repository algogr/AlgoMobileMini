package gr.algo.algomobilemini


import android.content.Intent
import android.os.Handler
import android.util.Log
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.bluetooth.BluetoothAdapter
import android.support.v4.app.ActivityCompat.startActivityForResult
import it.custom.printer.api.android.*
import java.io.InputStream
import java.io.OutputStream


class BTPrint {

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
                //startActivityForResult(enableBluetooth, 0)
            }

            val pairedDevices = mBluetoothAdapter.getBondedDevices()

            if (pairedDevices.size > 0) {
                for (device in pairedDevices) {

                    Log.d("JIM",device.name)
                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName() == "MY3A_626164346636") {
                        Log.d("JIM","MY#FOUND")
                        mmDevice = device
                        break
                    }
                }
            }




        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    fun findCustomBt(){
        val deviceList=CustomAndroidAPI.EnumBluetoothDevices()

        for (device in deviceList)
        {
            if (device.name== "MY3A_626164346636")
            {
                val t=CustomAndroidAPI()

                val prnDevice=t.getPrinterDriverBT(device)
                val font = PrinterFont()
                font.setEmphasized(true)
                font.setJustification(PrinterFont.FONT_JUSTIFICATION_CENTER)
                prnDevice.printText("Guy Debord")


            }
        }

    }


    @Throws(IOException::class)
    fun openZebraBT() {
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

    fun openCustomBT(){
        val t=CustomAndroidAPI()
        Log.d("JIM",mmDevice.name.toString())
        val prnDevice=t.getPrinterDriverBT(mmDevice)
        val font = PrinterFont()
        font.setEmphasized(true)
        font.setJustification(PrinterFont.FONT_JUSTIFICATION_CENTER)
        prnDevice.printText("Guy Debord")

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
    fun invoiceZebraData(findoc:FinDoc,customer:Customer,finDocLines:MutableList<FinDocLine>,company:Company) {
        try {
            /*

            format("! U1 setvar \"device.languages\" \"zpl\"");
            format("^XA");
            format("^MMC,Y");
            format("^MNN^LL1200");
            //format("^CWT,E:TT0003M_.TTF");
            format("^CFT,30,30");
            format("^FO175,100^CI28^ATN,36,20^FH^FD GUY DEBORD ^FS");
            format("^FS");
            format("^XZ");

             */

            format("! U1 setvar \"device.languages\" \"zpl\"");
            format("^XA");
            format("^MMC,Y");
            format("^MNN^LL1200");
            format("^CWT,E:TT0003M_.TTF");
            format("^CFT,30,30");
            format("^FO175,100^CI28^ATN,36,20^FH^FD"+company.name+"^FS");
            format("^FO200,150^CI28^ATN,36,20^FH^FD"+company.occupation+"^FS");
            format("^FO185,200^CI28^ATN,36,20^FH^FD"+company.address+" "+company.city+"^FS");
            //format("^FO125,250^CI28^ATN,36,20^FH^FDΑΦΜ:"+company.afm+" - ΔΟΥ:"+company.doy()+"^FS");
            format("^FO125,300^CI28^ATN,36,20^FH^FDΤΗΛ:"+company.tel1+" - "+company.tel2+"^FS");
            format("^FO125,350^CI28^ATN,36,20^FH^FDEMAIL:"+company.email+"^FS");
            format("^FO0,400^GB598,0,8^fs");
            //INVOICE DATA
            format("^FO0,430^CI28^ATN,27,15^FH^FD"+findoc.typeDescr+"^FS");
            format("^FO200,430^CI28^ATN,27,15^FH^FDΣΕΙΡΑ: "+findoc.shortDescr+"^FS");
            format("^FO300,430^CI28^ATN,27,15^FH^FDΑΡ.: "+findoc.dsrNumber+"^FS");
            format("^FO400,430^CI28^ATN,27,15^FH^FD"+findoc.ftrdate+"^FS");
            //format("^FO500,430^CI28^ATN,27,15^FH^FD"+findoc.time+"^FS");
            format("^FO0,460^GB598,0,8^fs");
            //CUSTOMER DATA
            //format("^FO50,480^CI28^ATN,36,20^FH^FDΠΕΛΑΤΗΣ:^FS");
            format("^FO050,480^CI28^ATN,36,20^FH^FD"+customer.name+"^FS");
            format("^FO050,520^CI28^ATN,36,20^FH^FD"+customer.occupation+"^FS");
            format("^FO50,560^CI28^ATN,36,20^FH^FD"+customer.address+"^FS");
            format("^FO425,560^CI28^ATN,36,20^FH^FD"+customer.city+"^FS");
            format("^FO050,600^CI28^ATN,36,20^FH^FDΑΦΜ:"+customer.afm+"^FS");
            format("^FO250,600^CI28^ATN,36,20^FH^FDΔΟΥ:"+customer.doyid+"^FS");
            format("^FO50,640^CI28^ATN,27,15^FH^FDΔΙΕΥΘ.ΠΑΡΑΔΟΣΗΣ:^FS");
            format("^FO200,640^CI28^ATN,27,15^FH^FD"+findoc.deliveryAddress+"^FS");
            format("^FO0,680^GB598,0,8^fs");
            //DETAILS HEADER
            format("^FO0,700^CI28^ATN,27,15^FH^FD"+"ΕΙΔΟΣ"+"^FS");
            format("^FO250,700^CI28^ATN,27,15^FH^FD"+"ΜΜ"+"^FS");
            format("^FO300,700^CI28^ATN,27,15^FH^FD"+"ΠΟΣΟΤΗΤΑ"+"^FS");
            format("^FO400,700^CI28^ATN,27,15^FH^FD"+"ΤΙΜΗ"+"^FS");
            format("^FO450,700^CI28^ATN,27,15^FH^FD"+"ΑΞΙΑ"+"^FS");
            format("^FO525,700^CI28^ATN,27,15^FH^FD"+"ΦΠΑ"+"^FS");
            format("^FO0,730^GB598,0,8^FS");
            //DETAILS
            var  globaly=0;
            var i:Int=0
            for(findocLine in finDocLines)
            {

                val y:Int=750+30*i;

                format("^FO0,"+y.toString()+"^CI28^ATN,27,15^FH^FD"+findocLine.iteDescription+"^FS")
                format("^FO250,"+y.toString()+"^CI28^ATN,27,15^FH^FD"+findocLine.muDescr+"^FS")
                format("^FO300,"+y.toString()+"^CI28^ATN,27,15^FH^FD"+findocLine.firstQty.toString().padStart(4,' ')+"^FS")

                format("^FO400,"+y.toString()+"^CI28^ATN,27,15^FH^FD"+findocLine.price.toString().padStart(6,' ')+"^FS")
                format("^FO450,"+y.toString()+"^CI28^ATN,27,15^FH^FD"+findocLine.netValue.toString().padStart(7,' ')+"^FS");
                format("^FO525,"+y.toString()+"^CI28^ATN,27,15^FH^FD"+findocLine.vtcID.toString().padStart(2,' ')+"^FS");
                globaly=y;

            }

            globaly=globaly+50;
            format("^FO0,"+globaly.toString()+"^GB598,0,8^FS");

            //TOTALS
            globaly=globaly+40;
            format("^FO250,"+globaly.toString()+"^CI28^ATN,36,20^FH^FDΚΑΘΑΡΗ ΑΞΙΑ:^FS");
            format("^FO500,"+globaly.toString()+"^CI28^ATN,36,20^FH^FD"+findoc.netValue+"^FS");

            globaly=globaly+40;

            format("^FO250,"+globaly.toString()+"^CI28^ATN,36,20^FH^FDΦΠΑ:^FS");
            format("^FO500,"+globaly.toString()+"^CI28^ATN,36,20^FH^FD"+findoc.vatAmount+"^FS");

            globaly=globaly+40;
            format("^FO0,"+globaly.toString()+"^GB598,0,8^FS");

            globaly=globaly+40;

            format("^FO250,"+globaly.toString()+"^CI28^ATN,54,20^FH^FDΠΛΗΡΩΤΕΟ:^FS");
            format("^FO500,"+globaly.toString()+"^CI28^ATN,54,20^FH^FD"+findoc.totAmount+"^FS");

            /*
            format("^FΟ250,"+globaly.toString()+"^CI28^ATN,36,20^FH^FDΠΛΗΡΩΤΕΟ:^FS");
            format("^FΟ550,"+globaly.toString()+"^CI28^ATN,36,20^FH^FD"+total()+"^FS");
            */
            format("^FS");
            format("^XZ");





            Log.d("JIM","SEND")




        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    @Throws(IOException::class)
    fun invoicePlainData() {
        try {

            format(PrinterCommands.ESC_ALIGN_CENTER.toString())

            format("GUY");
            format("deborf");
            format(PrinterCommands.ESC_ENTER.toString())
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