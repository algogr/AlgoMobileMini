package gr.algo.algomobilemini

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MyDBHandler(context: Context,name:String?,factory:SQLiteDatabase.CursorFactory?,
                  version: Int):SQLiteOpenHelper(context,DATABASE_NAME,factory,DATABASE_VERSION)
{

    override fun onCreate(db: SQLiteDatabase) {
        createTables(db)





        }





    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    fun sampleRoutes(){

        var route=Route(1,"Καβάλα",1)
        val values=ContentValues()
        values.put("description","test2")
        values.put("erpid",1)
        values.put("id",12)
        val db=this.writableDatabase
        db.insert("Route",null,values)
        db.close()


    }


    fun createTables(db:SQLiteDatabase){
        val CREATE_COMPANYDATA:String="CREATE TABLE CompanyData ( `name` TEXT, `occupation` TEXT, " +
                "`address` TEXT, `city` TEXT, `afm` TEXT, `doy` TEXT, `tel1` TEXT, `tel2` TEXT, " +
                "`email` TEXT, `site` TEXT )"

        val CREATE_ROUTE:String ="CREATE TABLE Route ( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "`description` TEXT NOT NULL,'erpid' INTEGER )"

        val CREATE_VATSTATUS:String="CREATE TABLE `Vatstatus` (\n" +
                "\t`codeid`\tTEXT NOT NULL,\n" +
                "\t`description`\tTEXT NOT NULL,\n" +
                "\t`vatcodeid`\tTEXT NOT NULL\n" +
                ")"
        val CREATE_CUSTFINDATA:String="CREATE TABLE `Custfindata` (`cusid`	INTEGER,`balance`	NUMERIC)"

        val CREATE_CUSTOMERPRICES="CREATE TABLE `customerprices` (\n" +
                "\t`cusid`\tINTEGER NOT NULL,\n" +
                "\t`iteid`\tINTEGER NOT NULL,\n" +
                "\t`price`\tNUMERIC NOT NULL\n" +
                ")"
        val CREATE_STORETRADELINES="CREATE TABLE IF NOT EXISTS \"storetradelines\" (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`iteid`\tINTEGER NOT NULL,\n" +
                "\t`ftrid`\tINTEGER NOT NULL,\n" +
                "\t`primaryqty`\tNUMERIC NOT NULL,\n" +
                "\t`price`\tNUMERIC NOT NULL,\n" +
                "\t`discount`\tNUMERIC,\n" +
                "\t`discountpercent`\tINTEGER,\n" +
                "\t`linevalue`\tNUMERIC,\n" +
                "\t`vatamount`\tNUMERIC,\n" +
                "\t`vatid`\tINTEGER NOT NULL\n" +
                ")"

        val CREATE_STOREFINDATA="CREATE TABLE `storefindata` (\n" +
                "\t`iteid`\tINTEGER,\n" +
                "\t`startqty`\tINTEGER,\n" +
                "\t`qty`\tINTEGER\n" +
                ")"

        val CREATE_FINTRADE="CREATE TABLE IF NOT EXISTS \"fintrade\" (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`ftrdate`\tTEXT NOT NULL,\n" +
                "\t`dsrid`\tINTEGER NOT NULL,\n" +
                "\t`dsrnumber`\tINTEGER NOT NULL,\n" +
                "\t`cusid`\tINTEGER NOT NULL,\n" +
                "\t`salesmanid`\tINTEGER,\n" +
                "\t`comments`\tTEXT,\n" +
                "\t`deliveryaddress`\tTEXT,\n" +
                "\t`erpupd`\tINTEGER,\n" +
                "\t`netvalue`\tNUMERIC,\n" +
                "\t`vatamount`\tNUMERIC,\n" +
                "\t`totamount`\tNUMERIC,\n" +
                "\t`cash`\tINTEGER\n" +
                ")"

        val CREATE_CASHTRN="CREATE TABLE IF NOT EXISTS \"cashtrn\" (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`trndate`\tTEXT,\n" +
                "\t`trntype`\tINTEGER,\n" +
                "\t`amount`\tNUMERIC,\n" +
                "\t`justification`\tTEXT,\n" +
                "\t`trncategory`\tINTEGER,\n" +
                "\t`ftrid`\tINTEGER,\n" +
                "\t`perid`\tINTEGER\n" +
                ")"

        val CREATE_DOY="CREATE TABLE IF NOT EXISTS \"Doy\" (\n" +
                "\t`id`\tINTEGER NOT NULL,\n" +
                "\t`code`\tTEXT NOT NULL,\n" +
                "\t`description`\tTEXT NOT NULL,\n" +
                "\t`erpid`\tINTEGER,\n" +
                "\tPRIMARY KEY(id)\n" +
                ")"

        val CREATE_VAT="CREATE TABLE IF NOT EXISTS \"Vat\" (\n" +
                "\t`codeid`\tTEXT NOT NULL,\n" +
                "\t`percent0`\tREAL NOT NULL,\n" +
                "\t`percent1`\tREAL\n" +
                ")"

        val CREATE_DOCSERIES="CREATE TABLE IF NOT EXISTS \"docseries\" (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`description`\tTEXT NOT NULL,\n" +
                "\t`lastno`\tINTEGER NOT NULL,\n" +
                "\t`quantmode`\tREAL NOT NULL,\n" +
                "\t`valmode`\tREAL NOT NULL,\n" +
                "\t`codeid`\tTEXT NOT NULL,\n" +
                "\t`shortdescr`\tTEXT,\n" +
                "\t`erpdsrid`\tINTEGER\n" +
                ")"

        val CREATE_SALESMAN="CREATE TABLE `salesman` (\n" +
                "\t`erpid`\tINTEGER,\n" +
                "\t`name`\tTEXT,\n" +
                "\tPRIMARY KEY(erpid)\n" +
                ")"

        val CREATE_PRINTERS="CREATE TABLE `printers` (\n" +
                "\t`code`\tINTEGER,\n" +
                "\t`macaddress`\tTEXT,\n" +
                "\t`active`\tINTEGER\n" +
                ");"

        val CREATE_TABLETINFO:String="CREATE TABLE TABLETINFO (salesmanid INTEGER,printercode INTEGER,serveraddress TEXT,serverport INTEGER )"

        val TABLETINFODATA="INSERT INTO tabletinfo(salesmanid,printercode,serveraddress,serverport) VALUES (1,0,'192.168.0.11',8080)"

        val CREATE_MATERIAL="CREATE TABLE [Material] (\n" +
                "[code] TEXT  NOT NULL,\n" +
                "[description] TEXT  NOT NULL,\n" +
                "[price] NUMERIC  NULL,\n" +
                "[vatid] INTEGER  NULL,\n" +
                "[maxdiscount] NUMERIC  NULL,\n" +
                "[unit] TEXT  NULL,\n" +
                "[erpid] INTEGER  NULL,\n" +
                "[id] INTEGER  NULL\n" +
                ")"

        val CREATE_CUSTOMER="CREATE TABLE [Customer] (\n" +
                "[name] TEXT  NULL,\n" +
                "[address] TEXT  NULL,\n" +
                "[district] TEXT  NULL,\n" +
                "[title] NUMERIC  NULL,\n" +
                "[afm] TEXT  NULL,\n" +
                "[doyid] INTEGER  NULL,\n" +
                "[erpid] INTEGER  NULL,\n" +
                "[occupation] TEXT  NULL,\n" +
                "[tel1] TEXT  NULL,\n" +
                "[tel2] TEXT  NULL,\n" +
                "[fax] TEXT  NULL,\n" +
                "[email] TEXT  NULL,\n" +
                "[vatstatusid] INTEGER  NULL,\n" +
                "[city] TEXT  NULL,\n" +
                "[comments] TEXT  NULL,\n" +
                "[routeid] INTEGER  NULL,\n" +
                "[erpupd] INTEGER  NULL,\n" +
                "[id] INTEGER  NULL\n" +
                ")"





        db.execSQL(CREATE_COMPANYDATA)
        db.execSQL(CREATE_ROUTE)
        db.execSQL(CREATE_VATSTATUS)
        db.execSQL(CREATE_CUSTFINDATA)
        db.execSQL(CREATE_CUSTOMERPRICES)
        db.execSQL(CREATE_STORETRADELINES)
        db.execSQL(CREATE_FINTRADE)
        db.execSQL(CREATE_CASHTRN)
        db.execSQL(CREATE_DOY)
        db.execSQL(CREATE_VAT)
        db.execSQL(CREATE_STOREFINDATA)
        db.execSQL(CREATE_DOCSERIES)
        db.execSQL(CREATE_SALESMAN)
        db.execSQL(CREATE_PRINTERS)
        db.execSQL(CREATE_TABLETINFO)
        db.execSQL(TABLETINFODATA)
        db.execSQL(CREATE_MATERIAL)
        db.execSQL(CREATE_CUSTOMER)


    }




    fun getInvoiceById(ftrId:Int):FinDoc{
        val query="SELECT f.id,f.ftrdate,f.dsrid,f.dsrnumber,f.cusid,f.salesmanid,f.comments,f.deliveryaddress,f.erpupd,f.netvalue,f.vatamount,f.totamount,d." +
                "shortdescr FROM fintrade f,docseries d where f.dsrid=d.id and f.id="+ftrId.toString()

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)
        cursor.moveToPosition(0)
        val id= cursor.getInt(0)
        val ftrDate = cursor.getString(1)
        val dsrId=cursor.getInt(2)
        val dsrNumber=cursor.getInt(3)
        val cusId=cursor.getInt(4)
        val salesmanId=cursor.getInt(5)
        val comments=cursor.getString(6)
        val deliveryAddress:String=cursor.getString(7)
        val erpUpd:Int=cursor.getInt(8)
        val netValue:Float=cursor.getFloat(9)
        val vatAmount:Float=cursor.getFloat(10)
        val totAmount:Float=cursor.getFloat(11)
        val shortDescr=cursor.getString(12)

        val invoice=FinDoc(id,cusId,dsrId,ftrDate,dsrNumber,salesmanId,comments,deliveryAddress,erpUpd,netValue,vatAmount,totAmount,shortDescr=shortDescr)

        return invoice

    }

    fun getInvoices():MutableList<FinDoc>
    {
        val query="SELECT f.id,f.ftrdate,f.dsrid,f.dsrnumber,f.cusid,f.salesmanid,f.comments,f.deliveryaddress,f.erpupd,f.netvalue,f.vatamount,f.totamount,d.shortdescr FROM fintrade f,docseries d where f.dsrid=d.id order by ftrdate,dsrnumber"

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        val invoiceList= mutableListOf<FinDoc>()

        val i=cursor.count-1
        for (j in 0..i)
        {
            cursor.moveToPosition(j)




            val id= cursor.getInt(0)
            val ftrDate = cursor.getString(1)
            val dsrId=cursor.getInt(2)
            val dsrNumber=cursor.getInt(3)
            val cusId=cursor.getInt(4)
            val salesmanId=cursor.getInt(5)
            val comments=cursor.getString(6)
            val deliveryAddress:String=cursor.getString(7)
            val erpUpd:Int=cursor.getInt(8)
            val netValue:Float=cursor.getFloat(9)
            val vatAmount:Float=cursor.getFloat(10)
            val totAmount:Float=cursor.getFloat(11)
            val shortDescr=cursor.getString(12)


            val invoice=FinDoc(id,cusId,dsrId,ftrDate,dsrNumber,salesmanId,comments,deliveryAddress,erpUpd,netValue,vatAmount,totAmount,shortDescr=shortDescr)



            invoiceList.add(j,invoice)


        }
        cursor.close()
        db.close()
        return  invoiceList


    }





    fun getAllRoutes():MutableList<Route>?
    {
        val query="SELECT * FROM Route"

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        val routeList= mutableListOf<Route>()

        val i=cursor.count-1
        for (j in 0..i)
        {
            cursor.moveToPosition(j)




            val id= Integer.parseInt(cursor.getString(0))
            val description = cursor.getString(1)
            val erpid=Integer.parseInt(cursor.getString(2))

            val route=Route(id,description,erpid)

            routeList.add(j,route)


        }
        cursor.close()
        db.close()
        return  routeList

    }


    fun getCustomersByRoute(route:Int):MutableList<Customer>?
    {
        val query="SELECT c.*,cf.balance from customer c left outer join custfindata cf on c.erpid=cf.cusid where c.routeid=$route order by c.name"


        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        val customerList= mutableListOf<Customer>()

        val i=cursor.count-1
        for (j in 0..i)
        {
            cursor.moveToPosition(j)


            val name:String=cursor.getString(0)
            val address:String?=cursor.getString(1)
            val district:String?=cursor.getString(2)
            val title:String?=cursor.getString(3)
            val afm:String=cursor.getString(4)
            val doyid:Int?=cursor.getInt(5)
            val erpid: Int=cursor.getInt(6)
            val occupation:String?=cursor.getString(7)
            val tel1:String?=cursor.getString(8)
            val tel2:String?=cursor.getString(9)
            val fax:String?=cursor.getString(10)
            val email:String?=cursor.getString(11)
            val vatstatusid:Int=Integer.parseInt(cursor.getString(12))
            val city:String?=cursor.getString(13)
            val comments:String?=cursor.getString(14)
            val routeid:Int=Integer.parseInt(cursor.getString(15))
            val erpupd:Int=Integer.parseInt(cursor.getString(16))
            val id:Int =cursor.getInt(17)
            val balance:Float=cursor.getFloat(18)


            val customer=Customer(name,address,district,title,afm,doyid,erpid,occupation,tel1,tel2,fax,email,vatstatusid,city,comments,
                    routeid,erpupd,id,balance)


            customerList.add(j,customer)


        }
        cursor.close()
        db.close()

        return  customerList


    }


    fun getCustomerById(cusId:Int):Customer?
    {

        val query="SELECT c.*,cf.balance from customer c left outer join custfindata cf on c.erpid=cf.cusid where c.id="+cusId
        Log.d("JIM-QUERY",query)
        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)
        var customer:Customer?=null
        if(cursor.moveToPosition(0)) {
            val name: String = cursor.getString(0)
            val address: String? = cursor.getString(1)
            val district: String? = cursor.getString(2)
            val title: String? = cursor.getString(3)
            val afm: String = cursor.getString(4)
            val doyid: Int? = cursor.getInt(5)
            val erpid: Int = cursor.getInt(6)
            val occupation: String? = cursor.getString(7)
            val tel1: String? = cursor.getString(8)
            val tel2: String? = cursor.getString(9)
            val fax: String? = cursor.getString(10)
            val email: String? = cursor.getString(11)
            val vatstatusid: Int = cursor.getInt(12)
            val city: String? = cursor.getString(13)
            val comments: String? = cursor.getString(14)
            val routeid: Int = cursor.getInt(15)
            val erpupd: Int = cursor.getInt(16)
            val id: Int = cursor.getInt(17)
            val balance: Float = cursor.getFloat(18)


            customer = Customer(name, address, district, title, afm, doyid, erpid, occupation, tel1, tel2, fax, email, vatstatusid, city, comments,
                    routeid, erpupd, id, balance)



            cursor.close()
            db.close()
        }


        return  customer


    }


    fun getCustomerByErpId(cusId:Int):Customer? {

        val query = "SELECT c.*,cf.balance from customer c left outer join custfindata cf on c.erpid=cf.cusid where c.erpid=" + cusId
        Log.d("JIM-QUERY", query)
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var customer: Customer? = null
        if (cursor.moveToPosition(0))
        {
        val name: String = cursor.getString(0)
        val address: String? = cursor.getString(1)
        val district: String? = cursor.getString(2)
        val title: String? = cursor.getString(3)
        val afm: String = cursor.getString(4)
        val doyid: Int? = cursor.getInt(5)
        val erpid: Int = cursor.getInt(6)
        val occupation: String? = cursor.getString(7)
        val tel1: String? = cursor.getString(8)
        val tel2: String? = cursor.getString(9)
        val fax: String? = cursor.getString(10)
        val email: String? = cursor.getString(11)
        val vatstatusid: Int = cursor.getInt(12)
        val city: String? = cursor.getString(13)
        val comments: String? = cursor.getString(14)
        val routeid: Int = cursor.getInt(15)
        val erpupd: Int = cursor.getInt(16)
        val id: Int = cursor.getInt(17)
        val balance: Float = cursor.getFloat(18)


        customer = Customer(name, address, district, title, afm, doyid, erpid, occupation, tel1, tel2, fax, email, vatstatusid, city, comments,
                routeid, erpupd, id, balance)
    }


        cursor.close()
        db.close()

        return  customer


    }



    fun getAllCustomers():MutableList<Customer>
    {

        val query="SELECT c.*,cf.balance from customer c left outer join custfindata cf on c.erpid=cf.cusid order by c.name"

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        val customerList= mutableListOf<Customer>()

        val i=cursor.count-1
        for (j in 0..i)
        {
            cursor.moveToPosition(j)


            val name:String=cursor.getString(0)
            val address:String=cursor.getString(1)
            val district:String=cursor.getString(2)
            val title:String=cursor.getString(3)
            val afm:String=cursor.getString(4)
            val doyid:Int=Integer.parseInt(cursor.getString(5))
            val erpid: Int=Integer.parseInt(cursor.getString(6))
            val occupation:String=cursor.getString(7)
            val tel1:String=cursor.getString(8)
            val tel2:String=cursor.getString(9)
            val fax:String=cursor.getString(10)
            val email:String=cursor.getString(11)
            val vatstatusid:Int=Integer.parseInt(cursor.getString(12))
            val city:String=cursor.getString(13)
            val comments:String=cursor.getString(14)
            val routeid:Int=Integer.parseInt(cursor.getString(15))
            val erpupd:Int=Integer.parseInt(cursor.getString(16))
            val id:Int=Integer.parseInt(cursor.getString(17))
            val balance:Float=cursor.getFloat(18)


            val customer=Customer(name,address,district,title,afm,doyid,erpid,occupation,tel1,tel2,fax,email,vatstatusid,city,comments,
                    routeid,erpupd,id,balance)


            customerList.add(j,customer)


        }
        cursor.close()
        db.close()

        return  customerList


    }

    fun getAllItems():MutableList<Material>
    {
        val query="SELECT m.*,st.qty FROM Material m left outer join storefindata st on m.erpid=" +
                "st.iteid order by m.description"

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        val itemList= mutableListOf<Material>()

        val i=cursor.count-1
        for (j in 0..i)
        {
            cursor.moveToPosition(j)
            val code=cursor.getString(0)
            val description=cursor.getString(1)
            val price=cursor.getFloat(2)
            val vatid=cursor.getInt(3)
            val maxdiscount=cursor.getFloat(4)
            val unit=cursor.getString(5)
            val erpid=cursor.getInt(6)
            val id=cursor.getInt(7)
            val balance=cursor.getFloat(8)

            val item=Material(code,description, price, vatid, maxdiscount, unit, erpid, id,balance)

            itemList.add(j,item)






        }
        cursor.close()
        db.close()
        return itemList


    }

    fun getStoreCustData(cusId:Int?,iteId:Int):Bundle
    {
        val query="SELECT ifnull(lastqty,0),lastdate,ifnull(lastprice,0),ifnull(lastdiscount,0),ifnull(lastdiscount2,0) from storecustdata where iteid="+
                iteId.toString()+" and cusid="+cusId.toString()
        Log.d("JIM-QUERY",query)
        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)
        val bundle: Bundle = Bundle()
        if (cursor.moveToPosition(0)) {

            bundle.putFloat("lastqty", cursor.getFloat(0))
            bundle.putFloat("lastprice", cursor.getFloat(2))
            bundle.putFloat("lastdiscount", cursor.getFloat(3))
            bundle.putFloat("lastdiscount2", cursor.getFloat(4))
            bundle.putString("lastdate", cursor.getString(1))
        }
        else
        {
            bundle.putFloat("lastqty", 0f)
            bundle.putFloat("lastprice", 0f)
            bundle.putFloat("lastdiscount", 0f)
            bundle.putFloat("lastdiscount2", 0f)
            bundle.putString("lastdate", "")

        }
        return bundle


    }

    fun getDoys():MutableList<Doy>
    {
        val query="SELECT * FROM Doy order by description"

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        val doyList= mutableListOf<Doy>()

        val i=cursor.count-1
        for (j in 0..i)
        {
            cursor.moveToPosition(j)
            val code=cursor.getString(1)
            val description=cursor.getString(2)
            val erpid=cursor.getString(3).toInt()


            val doy=Doy(code,description, erpid)

            doyList.add(j,doy)






        }
        cursor.close()
        db.close()
        return doyList


    }

    fun getDoybyErpid( erpId:Int):Doy
    {
        var doy:Doy?=null
        if (erpId !=null)
        {
            val query = "SELECT * FROM Doy where erpid=" + erpId

            val db = this.writableDatabase
            val cursor = db.rawQuery(query, null)

            if(cursor.moveToPosition(0)) {
                val code = cursor.getString(1)
                val description = cursor.getString(2)
                val erpid = cursor.getString(3).toInt()


                doy = Doy(code, description, erpid)


                cursor.close()
                db.close()
            }
        }
        return doy?:Doy()


    }


    fun getSettings(field:Int):String{
        val query="SELECT serveraddress,serverport from tabletinfo"
        val db=this.writableDatabase
        val cursor:Cursor?=db.rawQuery(query,null)
        cursor?.moveToPosition(0)
        var answer:String?=""
        when (field){
            1->answer=cursor?.getString(0)
            2->answer=cursor?.getInt(1).toString()

        }
        cursor?.close()
        db.close()
        Log.d("JIM2",answer?:"")
        return answer?:""
    }

    fun insertUpdate(query:String){

        val db=this.writableDatabase
        db.execSQL(query)
        db.close()
    }

    fun insertCollection(customer: Customer,amount:Float)
    {
        val db=this.writableDatabase
        val cusId=if (customer.erpid>0 ) customer.erpid else customer.id
        val query= "INSERT INTO cashtrn (trndate,trntype,amount,justification,trncategory,perid) VALUES (date('now'),1," +amount.toString()+",'" + customer.name+
         "'," + "1" + "," +  cusId.toString() + ")"
        Log.d("JIM",query)
        db.execSQL(query)
        db.close()
    }


    fun insertExpense(amount:Float,justification:String?)
    {
        val db=this.writableDatabase

        val query= "INSERT INTO cashtrn (trndate,trntype,amount,justification,trncategory) " +
                "VALUES (date('now'),1," +amount.toString()+",'" + justification+
                "'," + "1" +  ")"

        db.execSQL(query)
        db.close()
    }


    fun insertCashOpen(amount:Float)
    {
        val db=this.writableDatabase

        val query= "INSERT INTO cashtrn (trndate,trntype,amount,justification,trncategory) " +
                "VALUES (date('now'),0," +amount.toString()+",'Άνοιγμα Ταμείου'," + "1" +  ")"

        db.execSQL(query)
        db.close()
    }




    fun insertInvoice(findoc:FinDoc,isUpdate:Boolean):Int{
        var query="SELECT lastno FROM docseries WHERE codeid='"+findoc.dsrId.toString()+"'"
        val db=this.writableDatabase
        var cursor=db.rawQuery(query,null)
        cursor.moveToPosition(0)
        val lastno= {
            var no = cursor.getInt(0)
            if (isUpdate) no else no+=1
            no
        }
                cursor.getInt(0)
        query="SELECT erpid FROM customer WHERE id="+findoc.cusId
        Log.d("JIM",query)
        cursor=db.rawQuery(query,null)
        cursor.moveToPosition(0)
        var cusErpId=cursor.getInt(0)
        if (cusErpId==0) cusErpId=findoc.cusId


       query="INSERT into fintrade (ftrdate,dsrid,dsrnumber,cusid,salesmanid,comments,deliveryaddress,erpupd,netvalue,vatamount,totamount,cash) VALUES " +
                "(date('now'),"+findoc.dsrId.toString()+","+(lastno()).toString()+","+cusErpId.toString()+","+findoc.salesmanId.toString()+",'"+findoc.comments+"','"+findoc.deliveryAddress+
                "',"+findoc.erpUpd.toString()+","+findoc.netValue.toString()+","+ findoc.vatAmount.toString()+
                ","+findoc.totAmount.toString()+","+findoc.isCash.toString()+")"
        Log.d("JIM",query)
        db.execSQL(query)
        query="SELECT id from fintrade order by id desc limit 1"
        Log.d("JIM",query)
        cursor=db.rawQuery(query,null)
        cursor.moveToPosition(0)
        val ftrid=cursor.getInt(0)
        query="UPDATE docseries SET lastno="+(lastno()).toString()+" where codeid="+findoc.dsrId.toString()
        Log.d("JIM",query)
        db.execSQL(query)
        if(findoc.isCash==0) {
            query = "update custfindata set balance=balance+" + findoc.totAmount + "*(select valmode from docseries where codeid='" + findoc.dsrId.toString() + "') where cusid=" + findoc.cusId.toString()
            db.execSQL(query)
        }
        else {
            query = "select name from customer where erpid=" + findoc.cusId.toString()
            cursor = db.rawQuery(query, null)
            cursor.moveToPosition(0)
            val cusName = cursor.getString(0)
            query = "INSERT INTO cashtrn (trndate,trntype,amount,justification,trncategory,ftrid,perid) VALUES (date('now'),1," + findoc.totAmount + ",'" + cusName + "'," + "1" + "," + ftrid.toString() + "," + if (cusErpId>0 ) cusErpId.toString() else findoc.cusId.toString() + ")"
            db.execSQL(query)
        }
        cursor.close()
        db.close()
        return ftrid
    }





    fun insertLines(ftrId:Int,findocLines:MutableList<FinDocLine>){
        val db=this.writableDatabase
        for (findocLine in findocLines)
        {
            var query="INSERT into storetradelines(iteid,ftrid,primaryqty,price,discount,discountpercent,linevalue,vatamount,vatid) " +
                    "VALUES ("+findocLine.iteID.toString()+","+ftrId.toString()+","+findocLine.firstQty.toString()+","+findocLine.price.toString()+","+findocLine.discount.toString()+","+findocLine.discount.toString()+
                    ","+findocLine.netValue.toString()+","+findocLine.vatValue.toString()+","+findocLine.vtcID.toString()+")"
            Log.d("JIM",query)
            db.execSQL(query)
            query="UPDATE storefindata set qty=ifnull(qty,0)+(select quantmode from docseries dc,fintrade f where f.dsrid=dc.id and f.id="+ftrId+")*"+findocLine.firstQty+" where iteid="+findocLine.iteID
            db.execSQL(query)
        }
        db.close()

    }

    fun getInvoiceLines(ftrId:Int):MutableList<FinDocLine>{
        val db=this.writableDatabase
        val query="select st.iteid,st.primaryqty,st.price,st.discount,st.linevalue,st.vatamount,st.vatid,m.code,m.description " +
                "from storetradelines st,material m where m.erpid=st.iteid and st.ftrid="+ftrId.toString()
        Log.d("JIM-MODE",query)
        val cursor=db.rawQuery(query,null)
        val finDocLines= mutableListOf<FinDocLine>()
        val i=cursor.count-1
        for (j in 0..i) {
            cursor.moveToPosition(j)
            val iteId = cursor.getInt(0)
            val primaryQty = cursor.getFloat(1)
            val price = cursor.getFloat(2)
            val discount = cursor.getFloat(3)
            val lineValue = cursor.getFloat(4)
            val vatAmount = cursor.getFloat(5)
            val vatId = cursor.getInt(6)
            val iteCode=cursor.getString(7)
            val iteDescription=cursor.getString(8)

            val finDocLine = FinDocLine(iteID = iteId, firstQty = primaryQty, price = price, discount = discount, netValue = lineValue, vatValue = vatAmount, vtcID = vatId,iteCode = iteCode,iteDescription = iteDescription)
            finDocLines.add(finDocLine)
        }
        cursor.close()
        db.close()
        return finDocLines



    }

    fun insertCustomer(customer:Customer){
        val db=this.writableDatabase
        val query="INSERT INTO customer (name,address,district,title,afm,doyid,erpid,occupation,tel1,tel2,fax,email,vatstatusid,city,comments,routeid,erpupd) " +
                "VALUES('"+customer.name+"','"+customer.address+"','"+customer.district+"','"+customer.title+"','"+customer.afm+"',"+customer.doyid+","+customer.erpid+",'"+
                customer.occupation+"','"+customer.tel1+"','"+customer.tel2+"','"+customer.fax+"','"+customer.email+"',"+customer.vatstatusid+",'"+customer.city+"','"+customer.comments+
                "',"+customer.routeid+",0)"
        db.execSQL(query)


    }


    fun getVatPercent(vtcId:String,status:Int):Float
    {
        val db=this.writableDatabase
        val query="SELECT percent0,percent1 FROM vat where codeid='"+vtcId+"'"

        val cursor=db.rawQuery(query,null)
        cursor.moveToPosition(0)
        val percent=cursor.getFloat(status)
        cursor.close()
        db.close()
        return percent
    }



    fun getSalesman():Int{

        val db=this.writableDatabase
        val query="SELECT salesmanid from tabletinfo"
        val cursor=db.rawQuery(query,null)
        cursor.moveToPosition(0)
        val salesmanid=cursor.getInt(0)
        cursor.close()
        db.close()
        return salesmanid

    }

    companion object {
        private val DATABASE_VERSION=1
        // private val DATABASE_NAME="/storage/sdcard1/algomobilemini/algo.sqlite"
        private val DATABASE_NAME="algo.sqlite"
    }



}