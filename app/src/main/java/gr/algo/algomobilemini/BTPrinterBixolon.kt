package gr.algo.algomobilemini

import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.text.style.CharacterStyle
import android.util.Log
import android.util.Printer
import android.widget.Toast
import app.akexorcist.bluetotohspp.library.BluetoothSPP
import jpos.JposException
import jpos.POSPrinter
import jpos.POSPrinterConst

import java.io.InputStream
import java.io.OutputStream
//import com.sun.corba.se.impl.util.Utility.autoConnect
import app.akexorcist.bluetotohspp.library.BluetoothState
import gr.algo.algomobilemini.PrinterCommands.PrinterCommands.FEED_PAPER_AND_CUT
import gr.algo.algomobilemini.PrinterCommands.PrinterCommands.GS
import hr.istratech.bixolon.driver.command.general.Alignment
import hr.istratech.bixolon.driver.command.print.CharacterSize
import hr.istratech.bixolon.driver.command.print.CodePage
import hr.istratech.bixolon.driver.command.print.DeviceFont
import hr.istratech.bixolon.driver.general.TextPrinterBuilder


class BTPrinterBixolon(val context: Context):BTPrinter {
    val NEW_LINE = "\r\n"
    private var bluetoothSPP: BluetoothSPP? = null
    private var userFeedback: UserFeedback? = null
    private var totFirstQty:Float=0.00f
    private var totSeqQty:Float=0.00f
    private var counter:Int=0
    private val testc=context
    init {
        bluetoothSPP = BluetoothSPP(context)
        userFeedback = UserFeedback(context)
        /////////////////////////////??????????
        if (!bluetoothSPP!!.isBluetoothEnabled()) {
            // Do somthing if bluetooth is disabled
        } else {
            // Do something if bluetooth is already enabled
        }

    }

    var connected:Boolean?=null





    fun connect( copies:Int,company:Company,findoc: FinDoc,finDocLines: MutableList<FinDocLine>,print: (copies:Int,company:Company,findoc:FinDoc,finDocLines:MutableList<FinDocLine>)->Unit) {
        counter=0

        Log.d("JIMPRINT","PRINT1")
        if (bluetoothSPP!!.isBluetoothEnabled() && bluetoothSPP!!.isBluetoothAvailable()) {

            if (!bluetoothSPP!!.isServiceAvailable()) {
                Log.d("JIMPRINT","PRINT2")
                bluetoothSPP!!.setupService()
                bluetoothSPP!!.startService(BluetoothState.DEVICE_OTHER)
            }

            if (bluetoothSPP!!.isServiceAvailable()) {

                Log.d("JIMPRINT","PRINT3")
                /*
                if(bluetoothSPP!!.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bluetoothSPP!!.disconnect()
                }
                 */


                bluetoothSPP!!.setBluetoothStateListener(object : BluetoothSPP.BluetoothStateListener{


                    override fun onServiceStateChanged(state: Int) {
                        if(state == BluetoothState.STATE_CONNECTED) {
                            print(copies,company, findoc, finDocLines)
                            bluetoothSPP!!.stopAutoConnect()
                            //bluetoothSPP!!.disconnect()
                            Log.d("JIMPRINT", "STATE CONNECTED_")

/*
                            Handler().postDelayed(Runnable {
                                bluetoothSPP!!.stopService()
                            }, 5000)

*/

                        }
                        else if(state == BluetoothState.STATE_CONNECTING) {

                            counter++
                            Log.d("JIMPRINT", "STATE CONNECTΙNG_")
                            if (counter==15) {
                                userFeedback!!.error("Ο εκτυπωτής δε είναι αναμμένος!!")
                                bluetoothSPP!!.stopAutoConnect()

                            }


                        }
                        else if(state == BluetoothState.STATE_LISTEN)
                            Log.d("JIMPRINT","STATE LISTEN")
                        else if(state == BluetoothState.STATE_NONE)
                            Log.d("JIMPRINT","STATE NONE")
                    }

                })


                bluetoothSPP!!.autoConnect("SPP-R310")


                bluetoothSPP!!.setOnDataReceivedListener(BluetoothSPP.OnDataReceivedListener { data, message ->
                    Log.d("JIMPRINT","PRINT6")
                    userFeedback!!.alert(message)
                })

            }
        } else {
            userFeedback!!.error("Ενεργοποιήστε το bluetooth!")
            return
        }
    }




    fun disconnect(){
        bluetoothSPP!!.disconnect()
        bluetoothSPP!!.stopService()
    }



    override fun findBT() {

    }

    override fun openBT(context: Context) {


    }


    override fun closeBT() {

    }

    override fun invoiceData(findoc: FinDoc, customer: Customer, finDocLines: MutableList<FinDocLine>, company: Company) {
        val stringBuilder=StringBuilder()

        stringBuilder.append(company.name)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append(company.occupation)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append(company.address+" "+company.city)
        stringBuilder.append(NEW_LINE)


        val printer = TextPrinterBuilder
                .aPrinterBuilder()
                .withCodePage(CodePage.CP_737_GREEK)
                .withGeneralControlSequence(Alignment.LEFT)
                .withTextControlSequence(CharacterSize.NORMAL)
                .withTextControlSequence(DeviceFont.DEVICE_FONT_A)
                .buildPrinter(stringBuilder.toString())

        bluetoothSPP!!.send(printer.command, false)








    }


    fun invoice(copies: Int,company: Company,findoc: FinDoc,finDocLines:MutableList<FinDocLine>) {
        for (i in 1..copies)
        {


                totFirstQty=0.00f
                totSeqQty=0.00f

                companyData(company)
                customerData(findoc)

                detailsData(findoc,finDocLines)
                totalsData(findoc)
                Thread.sleep(10000)


    }
        Handler().postDelayed(Runnable {
            disconnect()
        }, 20000)


    }


    fun bolthird(copies: Int,company: Company,findoc: FinDoc,finDocLines:MutableList<FinDocLine>) {
        for (i in 1..copies)
        {


            totFirstQty=0.00f
            totSeqQty=0.00f

            companyData(company)
            customerData(findoc)
            thirdData(findoc)
            detailsData(findoc,finDocLines)
            totalsData(findoc)

            Thread.sleep(10000)


        }
        Handler().postDelayed(Runnable {
            disconnect()
        }, 20000)


    }



    class UserFeedback(private val context: Context) {


        fun alert(message: String) {

                showPopup(context, message, "Alert")
        }

        fun success(message: String) {

                showPopup(context, message, "Success")
        }

        fun error(message: String) {

                showPopup(context, message, "Error")
        }

        fun longToast(message: String) {

                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun shortToast(message: String) {

                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun showPopup(context: Context, alertText: String, title: String) {
            val alertDialog = AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(alertText)
                    .create()

            // Setting OK Button
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok") { dialog, which -> }

            // Showing Alert Message
            alertDialog.show()
        }

    }


    fun companyData(company: Company)
    {

        val stringBuilder=StringBuilder()

        stringBuilder.append(company.name)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append(company.occupation)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append(company.address+" "+company.city)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append(company.tel1+" "+company.tel2)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΑΦΜ:"+company.afm+" - ΔΟΥ:"+company.doy)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("________________________________________________")
        stringBuilder.append(NEW_LINE)



        var printer = TextPrinterBuilder
                .aPrinterBuilder()
                .withCodePage(CodePage.CP_737_GREEK)
                .withGeneralControlSequence(Alignment.CENTER)
                .withTextControlSequence(CharacterSize.NORMAL)
                .withTextControlSequence(DeviceFont.DEVICE_FONT_A)
                .buildPrinter(stringBuilder.toString())

        bluetoothSPP!!.send(printer.command, false)

    }


    fun customerData(findoc: FinDoc)
    {
        val stringBuilder:StringBuilder=java.lang.StringBuilder()
        stringBuilder.setLength(0)

        stringBuilder.append(findoc.typeDescr).append("      ΣΕΙΡΑ:").append(findoc.shortDescr).append("     ΑΡ.:").append(findoc.dsrNumber)
        stringBuilder.append(NEW_LINE)

        Log.d("JIM-FTRDATE",findoc.ftrdate)
        val t=findoc.ftrdate.split("-")
        stringBuilder.append("ΗΜΕΡΟΜΗΝΙΑ:"+t[2]+"/"+t[1]+"/"+t[0])
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("____________________________________")
        stringBuilder.append(NEW_LINE)
        if (findoc.dsrId==6)
        stringBuilder.append("ΣΤΟΙΧΕΙΑ ΠΑΡΑΛΗΠΤΗ ΚΑΤ'ΕΝΤΟΛΗ & ΛΟΓ/ΜΟ ΕΝΤΟΛΕΑ")
        else
        stringBuilder.append("ΣΤΟΙΧΕΙΑ ΠΕΛΑΤΗ/ΕΝΤΟΛΕΑ")
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("____________________________________")
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΕΠΩΝΥΜΙΑ:"+findoc.customer.name)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΕΠΠΑΓΕΛΜΑ:"+findoc.customer.occupation)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΔΙΕΥΘΥΝΣΗ:"+findoc.customer.address)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΠΟΛΗ:"+findoc.customer.city)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΑΦΜ:"+findoc.customer.afm+" ΔΟΥ:"+findoc.customer.doy)
        stringBuilder.append(NEW_LINE)
        val u={findoc.subsidiary?.descr?:findoc.deliveryAddress}
        stringBuilder.append("ΔΙΕΥΘ.ΠΑΡΑΔ.:"+u())
        stringBuilder.append(NEW_LINE)
        stringBuilder.append(findoc.subsidiary?.street+" "+findoc.subsidiary?.city)


        val printer = TextPrinterBuilder
                .aPrinterBuilder()
                .withCodePage(CodePage.CP_737_GREEK)
                .withGeneralControlSequence(Alignment.LEFT)
                .withTextControlSequence(CharacterSize.NORMAL)
                .withTextControlSequence(DeviceFont.DEVICE_FONT_A)
                .buildPrinter(stringBuilder.toString())

        bluetoothSPP!!.send(printer.command, false)
        stringBuilder.setLength(0)

    }



    fun thirdData(findoc:FinDoc)
    {
        val stringBuilder=StringBuilder()
        stringBuilder.setLength(0)

        stringBuilder.append("____________________________________")
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΣΤΟΙΧΕΙΑ ΠElATH/ΕΝΤΟΛΕΑ")
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("____________________________________")
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΕΠΩΝΥΜΙΑ:"+findoc.third?.name)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΕΠΑΓΓΕΛΜΑ:"+findoc.third?.occupation)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΔΙΕΥΘΥΝΣΗ:"+findoc.third?.address)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΠΟΛΗ:"+findoc.third?.city)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΑΦΜ:"+findoc.third?.afm+" ΔΟΥ:"+findoc.third?.doy)



        val printer = TextPrinterBuilder
                .aPrinterBuilder()
                .withCodePage(CodePage.CP_737_GREEK)
                .withGeneralControlSequence(Alignment.LEFT)
                .withTextControlSequence(CharacterSize.NORMAL)
                .withTextControlSequence(DeviceFont.DEVICE_FONT_A)
                .buildPrinter(stringBuilder.toString())

        bluetoothSPP!!.send(printer.command, false)


    }


    fun detailsData(findoc: FinDoc,finDocLines: MutableList<FinDocLine>)
    {
        val stringBuilder:StringBuilder=java.lang.StringBuilder()

        stringBuilder.setLength(0)
        stringBuilder.append("________________________________________________")
        stringBuilder.append(NEW_LINE)

        var printer = TextPrinterBuilder
                .aPrinterBuilder()
                .withCodePage(CodePage.CP_737_GREEK)
                .withGeneralControlSequence(Alignment.LEFT)
                .withTextControlSequence(CharacterSize.NORMAL)
                .withTextControlSequence(DeviceFont.DEVICE_FONT_A)
                .buildPrinter(stringBuilder.toString())
        bluetoothSPP!!.send(printer.command, false)

        stringBuilder.setLength(0)

        stringBuilder.append("ΚΩΔΙΚΟΣ    ΠΕΡΙΓΡΑΦΗ                         M.M")
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("ΠΟΣΟΤ.     ΤΙΜΗ   ΕΚΠΤ1 %  ΕΚΠΤ2 %  ΚΑΘ.ΑΞΙΑ ΦΠΑ")

        printer = TextPrinterBuilder
                .aPrinterBuilder()
                .withCodePage(CodePage.CP_737_GREEK)
                .withGeneralControlSequence(Alignment.LEFT)
                .withTextControlSequence(CharacterSize.NORMAL)
                .withTextControlSequence(DeviceFont.DEVICE_FONT_A)
                .buildPrinter(stringBuilder.toString())
        bluetoothSPP!!.send(printer.command, false)

        stringBuilder.setLength(0)
        stringBuilder.append("________________________________________________")


        printer = TextPrinterBuilder
                .aPrinterBuilder()
                .withCodePage(CodePage.CP_737_GREEK)
                .withGeneralControlSequence(Alignment.LEFT)
                .withTextControlSequence(CharacterSize.NORMAL)
                .withTextControlSequence(DeviceFont.DEVICE_FONT_A)
                .buildPrinter(stringBuilder.toString())
        bluetoothSPP!!.send(printer.command, false)

        for (i in (0..finDocLines.size-1))
        {

            stringBuilder.setLength(0)
            val t=finDocLines[i]
            totFirstQty+=t.firstQty
            totSeqQty+=t.secQty
            val daArray= arrayListOf<Int>()
            daArray.add(2)
            daArray.add(3)
            daArray.add(6)
            daArray.add(7)


            val price={if (findoc.dsrId in daArray) 0 else t.price }
            val discount={if (findoc.dsrId in daArray) 0 else t.discount }
            val secDiscount={if (findoc.dsrId in daArray) 0 else t.secDiscount }
            val netValue={if (findoc.dsrId in daArray) 0 else t.netValue}


            stringBuilder.append(t.iteCode.padEnd(9,' ')," ",t.iteDescription.padEnd(34,' ')," ",t.muDescr)
            printer = TextPrinterBuilder
                    .aPrinterBuilder()
                    .withCodePage(CodePage.CP_737_GREEK)
                    .withGeneralControlSequence(Alignment.LEFT)
                    .withTextControlSequence(CharacterSize.NORMAL)
                    .withTextControlSequence(DeviceFont.DEVICE_FONT_A)
                    .buildPrinter(stringBuilder.toString())
            bluetoothSPP!!.send(printer.command, false)
            stringBuilder.setLength(0)
            stringBuilder.append(t.firstQty.toString().padStart(7,' '),"  ",price().toString().padStart(7,' '),"  ",discount().toString().padStart(6,' '),"  ",secDiscount().toString().padStart(6,' '),"  ",netValue().toString().padStart(8,' '),"  ",t.vatPercent.toString())
            printer = TextPrinterBuilder
                    .aPrinterBuilder()
                    .withCodePage(CodePage.CP_737_GREEK)
                    .withGeneralControlSequence(Alignment.RIGHT)
                    .withTextControlSequence(CharacterSize.NORMAL)
                    .withTextControlSequence(DeviceFont.DEVICE_FONT_A)
                    .buildPrinter(stringBuilder.toString())
            bluetoothSPP!!.send(printer.command, false)

        }

        stringBuilder.setLength(0)
        stringBuilder.append("________________________________________________")


        printer = TextPrinterBuilder
                .aPrinterBuilder()
                .withCodePage(CodePage.CP_737_GREEK)
                .withGeneralControlSequence(Alignment.LEFT)
                .withTextControlSequence(CharacterSize.NORMAL)
                .withTextControlSequence(DeviceFont.DEVICE_FONT_A)
                .buildPrinter(stringBuilder.toString())
        bluetoothSPP!!.send(printer.command, false)
    }



    fun totalsData(findoc: FinDoc)
    {

        val stringBuilder:StringBuilder=java.lang.StringBuilder()
        val daArray= arrayListOf<Int>()
        daArray.add(2)
        daArray.add(3)
        daArray.add(6)
        daArray.add(7)

        val netValue={if (findoc.dsrId in daArray) 0.00f else findoc.netValue}
        val vatAmount={if (findoc.dsrId in daArray) 0.00f else findoc.vatAmount}
        val totAmount={if (findoc.dsrId in daArray) 0.00f else findoc.totAmount}



        stringBuilder.setLength(0)
        stringBuilder.append("Συν.Ποσότητα:"+totFirstQty.toString().padStart(12,' '))
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("Καθαρή Αξία:"+netValue().toString().padStart(12,' '))
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("Αξία ΦΠΑ:"+vatAmount().toString().padStart(12,' '))
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("Πληρωτέο:"+totAmount().toString().padStart(12,' '))
        stringBuilder.append(NEW_LINE)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append("------------------------------------------------")
        stringBuilder.append(NEW_LINE)
        stringBuilder.append(NEW_LINE)
        stringBuilder.append(NEW_LINE)


        val printer = TextPrinterBuilder
                .aPrinterBuilder()
                .withCodePage(CodePage.CP_737_GREEK)
                .withGeneralControlSequence(Alignment.RIGHT)
                .withTextControlSequence(CharacterSize.NORMAL)
                .withTextControlSequence(DeviceFont.DEVICE_FONT_A)
                .buildPrinter(stringBuilder.toString())
        bluetoothSPP!!.send(printer.command, false)

        bluetoothSPP!!.send(FEED_PAPER_AND_CUT,false)

    }








}