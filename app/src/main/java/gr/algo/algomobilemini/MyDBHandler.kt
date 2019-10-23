package gr.algo.algomobilemini

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class MyDBHandler(context: Context,name:String?,factory:SQLiteDatabase.CursorFactory?,
                  version: Int):SQLiteOpenHelper(context,DATABASE_NAME,factory,DATABASE_VERSION)
{

    override fun onCreate(db: SQLiteDatabase) {
        //createTables(db)





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

        val CREATE_TABLETINFO="CREATE TABLE `tabletinfo` (\n" +
                "\t`salesmanid`\tINTEGER,\n" +
                "\t`printercode`\tINTEGER\n" +
                "\t`serveraddress`\tTEXT\n" +
                "\t`serverport`\tTEXT\n" +
                ")"

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
        db.execSQL(CREATE_DOCSERIES)
        db.execSQL(CREATE_SALESMAN)
        db.execSQL(CREATE_PRINTERS)
        db.execSQL(CREATE_TABLETINFO)
        db.execSQL(CREATE_MATERIAL)
        db.execSQL(CREATE_CUSTOMER)


    }


    fun getAllRoutes():MutableList<Route>?
    {
        val query="SELECT * FROM Route"

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        var routeList= mutableListOf<Route>()

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
        val query="SELECT * FROM customer WHERE routeid=$route order by name"

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        var customerList= mutableListOf<Customer>()

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


            val customer=Customer(name,address,district,title,afm,doyid,erpid,occupation,tel1,tel2,fax,email,vatstatusid,city,comments,
                    routeid,erpupd,id)


            customerList.add(j,customer)


        }
        cursor.close()
        db.close()

        return  customerList


    }


    fun getCustomerById(cusId:Int):Customer
    {

        val query="SELECT * FROM Customer where id="+cusId

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        cursor.moveToPosition(0)
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


            val customer=Customer(name,address,district,title,afm,doyid,erpid,occupation,tel1,tel2,fax,email,vatstatusid,city,comments,
                    routeid,erpupd,id)



        cursor.close()
        db.close()

        return  customer


    }


    fun getAllCustomers():MutableList<Customer>
    {

        val query="SELECT * FROM Customer order by name"

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        var customerList= mutableListOf<Customer>()

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


            val customer=Customer(name,address,district,title,afm,doyid,erpid,occupation,tel1,tel2,fax,email,vatstatusid,city,comments,
                    routeid,erpupd,id)


            customerList.add(j,customer)


        }
        cursor.close()
        db.close()

        return  customerList


    }

    fun getAllItems():MutableList<Material>
    {
        val query="SELECT * FROM Material"

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        var itemList= mutableListOf<Material>()

        val i=cursor.count-1
        for (j in 0..i)
        {
            cursor.moveToPosition(j)
            val code=cursor.getString(0)
            val description=cursor.getString(1)
            val price=cursor.getString(2).toFloat()
            val vatid=cursor.getString(3).toInt()
            val maxdiscount=cursor.getString(4).toFloat()
            val unit=cursor.getString(5)
            val erpid=cursor.getString(6).toInt()
            val id=cursor.getString(7).toInt()

            val item=Material(code,description, price, vatid, maxdiscount, unit, erpid, id)

            itemList.add(j,item)






        }
        cursor.close()
        db.close()
        return itemList


    }

    fun getDoys():MutableList<Doy>
    {
        val query="SELECT * FROM Doy order by description"

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        var doyList= mutableListOf<Doy>()

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
        val query="SELECT * FROM Doy where erpid="+erpId

        val db=this.writableDatabase
        val cursor=db.rawQuery(query,null)

        cursor.moveToPosition(0)
            val code=cursor.getString(1)
            val description=cursor.getString(2)
            val erpid=cursor.getString(3).toInt()


            val doy=Doy(code,description, erpid)


        cursor.close()
        db.close()
        return doy


    }


    fun getSettings(field:Int):String{
        val query="SELECT serveraddress,serverport from tabletinfo"
        val db=this.writableDatabase
        val cursor:Cursor?=db.rawQuery(query,null)
        cursor?.moveToPosition(0)
        var answer:String?=""
        when (field){
            1->answer=cursor?.getString(0)
            2->answer=cursor?.getString(1)

        }
        cursor?.close()
        db.close()
        return answer?:""
    }

    fun insertUpdate(query:String){

        val db=this.writableDatabase
        db.execSQL(query)
        db.close()
    }


    fun insertInvoice(findoc:FinDoc):Int{
        var query="SELECT lastno FROM docseries WHERE codeid='"+findoc.dsrId.toString()+"'"
        val db=this.writableDatabase
        var cursor=db.rawQuery(query,null)
        cursor.moveToPosition(0)
        val lastno=cursor.getInt(0)

       query="INSERT into fintrade (ftrdate,dsrid,dsrnumber,cusid,salesmanid,comments,deliveryaddress,erpupd,netvalue,vatamount,totamount,cash) VALUES " +
                "(date('now'),"+findoc.dsrId.toString()+","+(lastno+1).toString()+","+findoc.cusId.toString()+","+findoc.salesmanId.toString()+",'"+findoc.comments+"','"+findoc.deliveryAddress+
                "',"+findoc.erpUpd.toString()+","+findoc.netValue.toString()+","+ findoc.vatAmount.toString()+
                ","+findoc.totAmount.toString()+","+findoc.isCash.toString()+")"
        Log.d("JIM",query)
        db.execSQL(query)
        query="SELECT id from fintrade order by id desc limit 1"
        Log.d("JIM",query)
        cursor=db.rawQuery(query,null)
        cursor.moveToPosition(0)
        val ftrid=cursor.getInt(0)
        query="UPDATE docseries SET lastno="+(lastno+1).toString()+" where codeid="+findoc.dsrId.toString()
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
            query = "INSERT INTO cashtrn (trndate,trntype,amount,justification,trncategory,ftrid,perid) VALUES (date('now'),1," + findoc.totAmount + ",'" + cusName + "'," + "1" + "," + ftrid.toString() + "," + findoc.cusId.toString() + ")"
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

    fun insertCustomer(customer:Customer){
        val db=this.writableDatabase
        var query="INSERT INTO Customer] (name,address,district,title,afm,doyid,erpid,occupation,tel1,tel2,fax,email,vatstatusid,city,comments,routeid,erpupd) " +
                "VALUES('"+customer.name+"','"+customer.address+"','"+customer.district+"','"+customer.title+"','"+customer.afm+"',"+customer.doyid+","+customer.erpid+",'"+
                customer.occupation+"','"+customer.tel1+"','"+customer.tel2+"','"+customer.fax+"','"+customer.email+"',"+customer.vatstatusid+",'"+customer.city+"','"+customer.comments+
                "',"+customer.routeid+",0)"
        db.execSQL(query)


    }


    fun getVatPercent(vtcId:String,status:Int):Float
    {
        val db=this.writableDatabase
        val query="SELECT percent0,percent1 FROM vat where codeid='"+vtcId+"'"
        Log.d("JIMP",query.toString())
        val cursor=db.rawQuery(query,null)
        cursor.moveToPosition(0)
        val percent=cursor.getFloat(status)
        cursor.close()
        db.close()
        Log.d("JIMPERCENT",percent.toString())
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