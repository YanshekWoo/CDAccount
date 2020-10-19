package com.xuexiang.cdaccount.somethingDao.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xuexiang.cdaccount.database.ChartDataEntry;
import com.xuexiang.cdaccount.somethingDao.DatabaseHelper;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Bill账单
 */
public class BillDao {
    private final DatabaseHelper mHelper;
    public BillDao(Context context) {
        mHelper = new DatabaseHelper(context);
    }
    public int id_num = 0;

    public void test(){// 通过测试
      /*  SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from OutSubCategory";
        Cursor cursor = db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            String tmp = cursor.getString(cursor.getColumnIndex("OutSubCategory_Name"));
            int tmp1 = cursor.getInt(cursor.getColumnIndex("OutSubCategory_ID"));
            int tmp2 = cursor.getInt(cursor.getColumnIndex("OutSubCategory_Parent"));
            Log.d("MainActivity!!!!!", "name: " +tmp);
            Log.d("MainActivity!!!!!", "ID: " +tmp1 );
            Log.d("MainActivity!!!!!", "Parent: " +tmp2);
        }*/
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from Bill";
        Cursor cursor = db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            int sub = cursor.getInt(cursor.getColumnIndex("Bill_SubCategory"));
            int acc = cursor.getInt(cursor.getColumnIndex("Bill_Account"));
            int mem = cursor.getInt(cursor.getColumnIndex("Bill_Member"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int money = cursor.getInt(cursor.getColumnIndex("Bill_Money"));
            String rem = cursor.getString(cursor.getColumnIndex("Bill_Remark"));
          //  int tmp2 = cursor.getInt(cursor.getColumnIndex("OutSubCategory_Parent"));
            Log.d("MainActivity!!!!!", "sub: " +sub);
            Log.d("MainActivity!!!!!", "account: "+acc );
            Log.d("MainActivity!!!!!", "member: " +mem);
            Log.d("MainActivity!!!!!", "time: " +time);
            Log.d("MainActivity!!!!!", "money: " +money);
            Log.d("MainActivity!!!!!", "remark: " +rem);
          //  Log.d("MainActivity!!!!!", "Parent: " +tmp2);
        }
       // db.close();
    }

    public void insertBill(int type, int subcategory, int account, int toaccount, int member, String year, String month, String day, String time, String remark, double money){//通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "insert into Bill(Bill_ID, Bill_TYPE, Bill_SubCategory, Bill_Account, Bill_toAccount, Bill_Member, year, month, day, time, Bill_Remark, Bill_Money) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{id_num, type, subcategory, account, toaccount, member, year, month, day, time, remark, money});
        id_num++;
        db.close();
    }

    public void InsertBill(int type, String subcategory, String account, String toaccount, String member, String year, String month, String day, String time, String remark, double money){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int sub = 0, acc = 0, toa = 0, mem = 0;
        if(type == 0){
            String sql = "select * from OutSubCategory where OutSubCategory_Name = '"+subcategory+"'";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext())
                sub = cursor.getInt(cursor.getColumnIndex("OutSubCategory_ID"));
        }
        else{
            String sql = "select * from InSubCategory where InSubCategory_Name = '"+subcategory+"'";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext())
                sub = cursor.getInt(cursor.getColumnIndex("InSubCategory_ID"));
        }
        String sql = "select * from Account where Account_Name = '"+account+"'";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext())
            acc = cursor.getInt(cursor.getColumnIndex("Account_ID"));

        sql = "select * from Account where Account_Name = '"+toaccount+"'";
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext())
            toa = cursor.getInt(cursor.getColumnIndex("Account_ID"));

        sql = "select * from Member where Member_Name = '"+member+"'";
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext())
            mem = cursor.getInt(cursor.getColumnIndex("Member_ID"));
        db.close();
        insertBill(type, sub, acc, toa, mem, year, month, day, time, remark, money);
    }

    public void InsertCategory(String Top, String sub, int type){   //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();

        if(type == 0){ //支出

            Cursor cursor = db.query("OutTopCategory",null,null,null,null,null,null);
            int cnt = 0;
            while (cursor.moveToNext()){
                cnt = cursor.getInt(cursor.getColumnIndex("OutTopCategory_ID"));
                cnt++;
            }

            cursor = db.query("OutSubCategory",null,null,null,null,null,null);
            int cnt1 = 0;
            while (cursor.moveToNext()){
                cnt1 = cursor.getInt(cursor.getColumnIndex("OutSubCategory_ID"));
                cnt1++;
            }

            String sql = "select * from OutTopCategory where OutTopCategory_Name = '"+Top+"'";
            cursor = db.rawQuery(sql,null);
            int tmp = -1;
            while (cursor.moveToNext()){
                tmp = cursor.getInt(cursor.getColumnIndex("OutTopCategory_ID"));
            }
            if(tmp == -1)  //没有找到一级目录
            {
                sql = "insert into OutTopCategory(OutTopCategory_Id, OutTopCategory_name) values(?,?)";
                db.execSQL(sql, new Object[] {cnt, Top});

                sql = "insert into OutSubCategory(OutSubCategory_Id, OutSubCategory_Parent, OutSubCategory_name) values(?,?,?)";
                db.execSQL(sql, new Object[] {cnt1, cnt, sub});
            }else{    //找到一级目录，tmp是标号
                sql = "insert into OutSubCategory(OutSubCategory_Id, OutSubCategory_Parent, OutSubCategory_name) values(?,?,?)";
                db.execSQL(sql, new Object[] {cnt1, tmp, sub});
            }
        }
        else{
            Cursor cursor = db.query("InTopCategory",null,null,null,null,null,null);
            int cnt = 0;
            while (cursor.moveToNext()){
                cnt = cursor.getInt(cursor.getColumnIndex("InTopCategory_ID"));
                cnt++;
            }

            cursor = db.query("InSubCategory",null,null,null,null,null,null);
            int cnt1 = 0;
            while (cursor.moveToNext()){
                cnt1 = cursor.getInt(cursor.getColumnIndex("InSubCategory_ID"));
                cnt1++;
            }

            String sql = "select * from InTopCategory where InTopCategory_Name = '"+Top+"'";
            cursor = db.rawQuery(sql,null);
            int tmp = -1;
            while (cursor.moveToNext()){
                tmp = cursor.getInt(cursor.getColumnIndex("InTopCategory_ID"));
            }
            if(tmp == -1)  //没有找到一级目录
            {
                sql = "insert into InTopCategory(InTopCategory_Id, InTopCategory_name) values(?,?)";
                db.execSQL(sql, new Object[] {cnt, Top});

                sql = "insert into InSubCategory(InSubCategory_Id, InSubCategory_Parent,InSubCategory_name) values(?,?,?)";
                db.execSQL(sql, new Object[] {cnt1, cnt, sub});
            }else{    //找到一级目录，tmp是标号
                sql = "insert into InSubCategory(InSubCategory_Id, InSubCategory_Parent, InSubCategory_name) values(?,?,?)";
                db.execSQL(sql, new Object[] {cnt1, tmp, sub});
            }
        }
    }

    public void InsertMember(String name){ //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("Member",null,null,null,null,null,null);
        int tmp = 0;
        while (cursor.moveToNext()){
            tmp = cursor.getInt(cursor.getColumnIndex("Member_ID"));
            tmp++;
        }
        String sql = "insert into Member(Member_Id, Member_Name) values(?,?)";
        db.execSQL(sql, new Object[] {tmp, name});
        db.close();
    }

    public void InsertAccount(String name){   //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("Account",null,null,null,null,null,null);
        int tmp = 0;
        while (cursor.moveToNext()){
            tmp = cursor.getInt(cursor.getColumnIndex("Account_ID"));
            tmp++;
        }
        String sql = "insert into Account(Account_Id, Account_Name) values(?,?)";
        db.execSQL(sql, new Object[] {tmp, name});
        db.close();
    }

    public List<String> QueryMember(){  //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select Member_name from Member";
        Cursor cursor = db.rawQuery(sql,null);
        List<String> re = new LinkedList<String>();
        while(cursor.moveToNext()){
            String tmp = cursor.getString(cursor.getColumnIndex("Member_Name"));
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List<String> QueryAccount(){  //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select Account_name from Account";
        Cursor cursor = db.rawQuery(sql,null);
        List<String> re = new LinkedList<String>();
        while(cursor.moveToNext()){
            String tmp = cursor.getString(cursor.getColumnIndex("Account_Name"));
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public double QueryMonthIncome(){  //通过测试
        Calendar cal = Calendar.getInstance();
        int tmp = cal.get(Calendar.MONTH)+1;
        int tmp1 = cal.get(Calendar.YEAR);
        String Month = ""+tmp;
        String Year = ""+tmp1;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select sum(Bill_Money) as ans from Bill where month = '"+Month+"' AND Bill_TYPE = 1 AND year = '"+Year+"'";
        Cursor cursor = db.rawQuery(sql, null);
        double re = 0;
        if(cursor.moveToNext())re = cursor.getDouble(cursor.getColumnIndex("ans"));
        db.close();
        return re;
    }

    public double QueryMonthpay(){  //通过测试
        Calendar cal = Calendar.getInstance();
        int tmp = cal.get(Calendar.MONTH)+1;
        int tmp1 = cal.get(Calendar.YEAR);
        String Month = ""+tmp;
        String Year = ""+tmp1;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select sum(Bill_Money) as ans from Bill where month = '"+Month+"' AND Bill_TYPE = 0 AND year = '"+Year+"'";
        Cursor cursor = db.rawQuery(sql, null);
        double re = 0;
        if(cursor.moveToNext())re = cursor.getDouble(cursor.getColumnIndex("ans"));
        db.close();
        return re;
    }

    public List<String> GetRecentDate(){  //通过测试
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from Bill order by year || month || day || time DESC";
        Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<String>();
        int cnt = 0;
        while(cursor.moveToNext() && cnt < 20){
            if(cursor.getInt(cursor.getColumnIndex("Bill_TYPE")) == 2) continue;
            cnt++;
            String Y = cursor.getString(cursor.getColumnIndex("year"));
            String M = cursor.getString(cursor.getColumnIndex("month"));
            String D = cursor.getString(cursor.getColumnIndex("day"));
            String T = cursor.getString(cursor.getColumnIndex("time"));
            String ans = Y+"-"+M+"-"+D+" "+T;
            re.add(ans);
        }
        return re;
    }

    public List<String> GetRecentInformation(){   //通过测试
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from Bill order by year || month || day || time DESC";
        Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<String>();
        int cnt = 0;
        while(cursor.moveToNext() && cnt < 20){
            if(cursor.getInt(cursor.getColumnIndex("Bill_TYPE")) == 2) continue;
            cnt++;
            String IO = cursor.getInt(cursor.getColumnIndex("Bill_TYPE")) == 0 ? ("支出" + "："):("收入" + "：");
            int txt = cursor.getInt(cursor.getColumnIndex("Bill_SubCategory"));
            String nametmp = "";
            if(cursor.getInt(cursor.getColumnIndex("Bill_TYPE")) == 0)
            {
                sql = "select * from OutSubCategory where OutSubCategory_ID ="+txt+"";
                Cursor cursor1 = db.rawQuery(sql, null);
                while(cursor1.moveToNext())
                {
                    nametmp = cursor1.getString(cursor1.getColumnIndex("OutSubCategory_Name"));
                }
            }
            else{
                sql = "select * from InSubCategory where InSubCategory_ID ="+txt+"";
                Cursor cursor1 = db.rawQuery(sql, null);
                while(cursor1.moveToNext())
                {
                    nametmp = cursor1.getString(cursor1.getColumnIndex("InSubCategory_Name"));
                }
            }
            String Mon = decimalFormat.format(cursor.getDouble(cursor.getColumnIndex("Bill_Money")))+"元";
            String ans = IO+nametmp+Mon;
            re.add(ans);
        }
        return re;
    }

    public List<Integer> GetRecentIO(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from Bill order by year || month || day || time DESC";
        Cursor cursor = db.rawQuery(sql, null);
        List<Integer> re = new LinkedList<Integer>();
        int cnt = 0;
        while(cursor.moveToNext() && cnt < 20){
            if(cursor.getInt(cursor.getColumnIndex("Bill_TYPE")) == 2) continue;
            cnt++;
            Integer IO = cursor.getInt(cursor.getColumnIndex("Bill_TYPE"));
//            String txt = cursor.getString(cursor.getColumnIndex("Bill_SubCategory"))+" ";
//            String Mon = String.valueOf(cursor.getDouble(cursor.getColumnIndex("Bill_Money")));
//            String ans = IO+txt+Mon;
            re.add(IO);
        }
        return re;
    }

    public List<String> QueryOutTopCategory(){  //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select OutTopCategory_Name from OutTopCategory";
        Cursor cursor = db.rawQuery(sql,null);
        List<String> re = new LinkedList<String>();
        while(cursor.moveToNext()){
            String tmp = cursor.getString(cursor.getColumnIndex("OutTopCategory_Name"));
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List<List<String>> QueryOutSubCategory(){   //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from OutTopCategory";
        Cursor cursor = db.rawQuery(sql,null);
        List<List<String>> re = new LinkedList<List<String>>();

       while(cursor.moveToNext()){
            int tmp1 = cursor.getInt(cursor.getColumnIndex("OutTopCategory_ID"));
            String sql1 = "select OutSubCategory_Name from OutSubCategory where OutSubCategory_Parent = "+tmp1+"";
            Cursor cursor1 = db.rawQuery(sql1, null);
            List<String> tmp = new LinkedList<String>();
           while (cursor1.moveToNext()){
                String tmp3 = cursor1.getString(cursor1.getColumnIndex("OutSubCategory_Name"));
                tmp.add(tmp3);
            }
            re.add(tmp);
       }
        db.close();
        return re;
    }
    public List<String> QueryInTopCategory(){  //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select InTopCategory_Name from InTopCategory";
        Cursor cursor = db.rawQuery(sql,null);
        List<String> re = new LinkedList<String>();
        while(cursor.moveToNext()){
            String tmp = cursor.getString(cursor.getColumnIndex("InTopCategory_Name"));
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List<List<String>> QueryInSubCategory(){   //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from InTopCategory";
        Cursor cursor = db.rawQuery(sql,null);
        List<List<String>> re = new LinkedList<List<String>>();

        while(cursor.moveToNext()){
            int tmp1 = cursor.getInt(cursor.getColumnIndex("InTopCategory_ID"));
            String sql1 = "select InSubCategory_Name from InSubCategory where InSubCategory_Parent = "+tmp1+"";
            Cursor cursor1 = db.rawQuery(sql1, null);
            List<String> tmp = new LinkedList<String>();
            while (cursor1.moveToNext()){
                String tmp3 = cursor1.getString(cursor1.getColumnIndex("InSubCategory_Name"));
                tmp.add(tmp3);
            }
            re.add(tmp);
        }
        db.close();
        return re;
    }


    /**
     * 按一级分类分组获取收入支出
     * @param start_year 开始日期年份
     * @param start_month 开始日期月份
     * @param start_day 开始日期天
     * @param end_year 结束日期年份
     * @param end_month 结束日期月份
     * @param end_day 结束日期天
     * @return List<ChartDataEntry>
     */
    public List<ChartDataEntry> GetDateByOutTopCategory(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){//通过测试
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Map<Integer, Integer> mp1 = new HashMap<Integer, Integer>();
        String sql2 = "select * from OutSubCategory";
        Cursor cursor2 = db.rawQuery(sql2, null);
        while(cursor2.moveToNext()){
            mp1.put(cursor2.getInt(cursor2.getColumnIndex("OutSubCategory_ID")),cursor2.getInt(cursor2.getColumnIndex("OutSubCategory_Parent")));
        }

        Map<Integer, String> mp = new HashMap<Integer, String>();
        String sql1 = "select * from OutTopCategory";
        Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("OutTopCategory_ID")),cursor1.getString(cursor1.getColumnIndex("OutTopCategory_Name")));
        }

        String sql = "select Bill_ID, Bill_SubCategory, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' group by Bill_SubCategory";
        Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<ChartDataEntry>();

        Map<String,Double> ans =  new HashMap<String,Double>();
        while(cursor.moveToNext()){
            Integer tmp1 = new Integer(cursor.getInt(cursor.getColumnIndex("Bill_SubCategory")));
            Integer tmp3 =  new Integer(mp1.get(tmp1));
            String tmp2 = mp.get(tmp3);
            double tmp4 = cursor.getDouble(cursor.getColumnIndex("nums"));
            if(ans.containsKey(tmp2)){
                double tmp5 = ans.get(tmp2);
                ans.put(tmp2, tmp4+tmp5);
            }
            else
                ans.put(tmp2, tmp4);
        }
        for (Map.Entry<String, Double> entry : ans.entrySet()) {
            ChartDataEntry tmp = new ChartDataEntry(entry.getKey(), entry.getValue());
            re.add(tmp);
        }
        return re;
    }

    public List<ChartDataEntry> GetDateByOutSubCategory(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){  //通过测试
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Map<Integer, String> mp = new HashMap<Integer, String>();
        String sql1 = "select * from OutSubCategory";
        Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("OutSubCategory_ID")),cursor1.getString(cursor1.getColumnIndex("OutSubCategory_Name")));
        }

        String sql = "select Bill_ID, Bill_SubCategory, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' group by Bill_SubCategory";
        Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<ChartDataEntry>();

        while(cursor.moveToNext()){
            Integer tmp1 = new Integer(cursor.getInt(cursor.getColumnIndex("Bill_SubCategory")));
            ChartDataEntry tmp = new ChartDataEntry(mp.get(tmp1),cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        return re;
    }

    public List<ChartDataEntry> GetDateByMember(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){ //通过测试
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Map<Integer, String> mp = new HashMap<Integer, String>();
        String sql1 = "select * from Member";
        Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("Member_ID")),cursor1.getString(cursor1.getColumnIndex("Member_Name")));
        }

        String sql = "select Bill_ID, Bill_Member, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' group by Bill_Member";
        Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<ChartDataEntry>();

        while(cursor.moveToNext()){
            Integer tmp1 = new Integer(cursor.getInt(cursor.getColumnIndex("Bill_Member")));
            ChartDataEntry tmp = new ChartDataEntry(mp.get(tmp1),cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        return re;
    }

    public List<ChartDataEntry> GetDateByAccount(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){//通过测试
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Map<Integer, String> mp = new HashMap<Integer, String>();
        String sql1 = "select * from Account";
        Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("Account_ID")),cursor1.getString(cursor1.getColumnIndex("Account_Name")));
        }

        String sql = "select Bill_ID, Bill_Account, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' group by Bill_Account";
        Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<ChartDataEntry>();

        while(cursor.moveToNext()){
            Integer tmp1 = new Integer(cursor.getInt(cursor.getColumnIndex("Bill_Account")));
            ChartDataEntry tmp = new ChartDataEntry(mp.get(tmp1),cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        return re;
    }

    public List<ChartDataEntry> GetSumByDate(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){ //按天求和 通过测试
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String sql = "select year || month || day as date, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' group by date";
        Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<ChartDataEntry>();

        while(cursor.moveToNext()){
            ChartDataEntry tmp = new ChartDataEntry(cursor.getString(cursor.getColumnIndex("date")), cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        return re;
    }

    public List<ChartDataEntry> GetSumByDateAndOutTopCategory(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day, String category){    //通过测试
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String sql1 = "select * from OutTopCategory where OutTopCategory_Name = '"+category+"'";
        Cursor cursor1 = db.rawQuery(sql1, null);
        int id_parent = 0;
        while(cursor1.moveToNext())
            id_parent = cursor1.getInt(cursor1.getColumnIndex("OutTopCategory_ID"));

        String sql = "select year || month || day as date, sum(Bill_Money)as nums, Bill_SubCategory, OutSubCategory_ID, OutSubCategory_Parent from Bill inner join OutSubCategory on OutSubCategory_ID = Bill_SubCategory where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND OutSubCategory_Parent = "+id_parent+" group by date";
        Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<ChartDataEntry>();

        while(cursor.moveToNext()){
            ChartDataEntry tmp = new ChartDataEntry(cursor.getString(cursor.getColumnIndex("date")), cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        return re;
    }

/*
Bill_ID int, Bill_TYPE int, Bill_SubCategory int, Bill_Account int, Bill_toAccount int, Bill_Member int, year varchar(4), month varchar(2), day String, time varchar(15), Bill_Remark varchar(255), Bill_Money float
   public void insert(Bill bill){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "insert into _Bill(num,time,username,firstcategory,secondcategory,account,number,others,type) values(?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[] {bill.getNum(), bill.getTime(), bill.getUsername(), bill.getFirstcategory(), bill.getSecondcategory(), bill.getAccount(), bill.getNum(), bill.getOthers(), bill.getType()});
        db.close();
    }

    public void test(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query("_Bill", null, null,null,null,null,null);
        if(cursor.moveToNext()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("username"));
                String number = cursor.getString(cursor.getColumnIndex("number"));
                Log.d("MainActivityfuck", "name: " +name);
                Log.d("MainActivityfuck", "number: "+number);
            }while (cursor.moveToNext());
        }
    }

    public List getDateByFirstCategory(Date startDate, Date endDate)
    {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select firstcategory, sum(num) as countfirst from _Bill where time >= "+startDate+" AND "+endDate+" >= time group by firstcategory";
        Cursor cursor = db.rawQuery(sql,null);
        List<Bill> re = new LinkedList<Bill>();
        int numindex = cursor.getColumnIndex("countfirst");
        int firstcategoryindex = cursor.getColumnIndex("firstcategory");
        while (cursor.moveToNext()){
            Bill tmp = new Bill(new BigDecimal(cursor.getDouble(numindex)), null, null, cursor.getString(firstcategoryindex), null, null, null, null, -1);
            re.add(tmp);
        }
        db.close();
        return re;
    }


    public List getDateBySecondCategory(String category, Date startDate, Date endDate)
    {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select secondcategory, sum(num) as countsecond from _Bill where time >= "+startDate+" AND "+endDate+" >= time AND firstcategory = "+category+" group by secondcategory";
        Cursor cursor = db.rawQuery(sql,null);
        List<Bill> re = new LinkedList<Bill>();
        int numindex = cursor.getColumnIndex("countsecond");
        int secondcategoryindex = cursor.getColumnIndex("secondcategory");
        while (cursor.moveToNext()){
            Bill tmp = new Bill(new BigDecimal(cursor.getDouble(numindex)), null, null, null, cursor.getString(secondcategoryindex), null, null, null, -1);
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List getDateByNumberCategory(Date startDate, Date endDate)
    {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select number, sum(num) as countnum from _Bill where time >= "+startDate+" AND "+endDate+" >= time group by num";
        Cursor cursor = db.rawQuery(sql,null);
        List<Bill> re = new LinkedList<Bill>();
        int numberindex = cursor.getColumnIndex("number");
        int countnumindex = cursor.getColumnIndex("countnum");
        while (cursor.moveToNext()){
            Bill tmp = new Bill(new BigDecimal(cursor.getDouble(numberindex)), null, null, null, null, null, cursor.getString(countnumindex), null, -1);
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List getDateByAccount(Date startDate, Date endDate)
    {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select account, sum(num) as countaccount from _Bill where time >= "+startDate+" AND "+endDate+" >= time group by account";
        Cursor cursor = db.rawQuery(sql,null);
        List<Bill> re = new LinkedList<Bill>();
        int accountindex = cursor.getColumnIndex("account");
        int numberindex = cursor.getColumnIndex("countaccount");
        while (cursor.moveToNext()){
            Bill tmp = new Bill(new BigDecimal(cursor.getDouble(numberindex)), null, null, null, null, cursor.getString(accountindex), null, null, -1);
            re.add(tmp);
        }
        db.close();
        return re;

    }

    public List getDateByTime(Date startDate, Date endDate)
    {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select DATE(time) as day, sum(num) as countdatefirst from _Bill where time >= "+startDate+" AND "+endDate+" >= time group by DATE(time)";
        Cursor cursor = db.rawQuery(sql,null);
        List<Bill> re = new LinkedList<Bill>();
        int dayindex = cursor.getColumnIndex("day");
        int countindex = cursor.getColumnIndex("countdatefirst");
        while (cursor.moveToNext()){
            Bill tmp = new Bill(new BigDecimal(cursor.getDouble(countindex)), new Date (cursor.getLong(dayindex)), null, null, null, null, null, null, -1);
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List getDateFirstByTime(String category, Date startDate, Date endDate)
    {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select DATE(time) as day, sum(num) as countdatesecond from _Bill where time >= "+startDate+" AND "+endDate+" >= time AND "+category+" = firstcategory group by DATE(time)";
        Cursor cursor = db.rawQuery(sql,null);
        List<Bill> re = new LinkedList<Bill>();
        int dayindex = cursor.getColumnIndex("day");
        int countindex = cursor.getColumnIndex("countdatesecond");
        while (cursor.moveToNext()){
            Bill tmp = new Bill(new BigDecimal(cursor.getDouble(countindex)), new Date (cursor.getLong(dayindex)), null, null, null, null, null, null, -1);
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List querynumber(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select number as numberlist from _Bill group by number";
        Cursor cursor = db.rawQuery(sql,null);
        List<Bill> re = new LinkedList<Bill>();
        int numberindex = cursor.getColumnIndex("numberlist");
        while (cursor.moveToNext()){
            Bill tmp = new Bill(null, null, null, null, null, null, cursor.getString(numberindex), null, -1);
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List queryaccount(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select account as accountlist from _Bill group by account";
        Cursor cursor = db.rawQuery(sql,null);
        List<Bill> re = new LinkedList<Bill>();
        int accountindex = cursor.getColumnIndex("accountlist");
        while (cursor.moveToNext()){
            Bill tmp = new Bill(null, null, null, null, null, cursor.getString(accountindex), null, null, -1);
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List querycategory(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select firstcategory as fc, secondcategory as sc from _Bill";
        Cursor cursor = db.rawQuery(sql,null);
        List<Bill> re = new LinkedList<Bill>();
        int firstindex = cursor.getColumnIndex("fc");
        int secondindex = cursor.getColumnIndex("sc");
        while (cursor.moveToNext()){
            Bill tmp = new Bill(null, null, null, cursor.getString(firstindex), cursor.getString(secondindex), null, null, null, -1);
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public BigDecimal querymonthpay(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select sum(num) as Monthspay from _Bill where EXTRACT(month from time) = EXTRACT(month from now()) AND type = 1";
        Cursor cursor = db.rawQuery(sql,null);

        int index = cursor.getColumnIndex("Monthspay");
        BigDecimal re = new BigDecimal (cursor.getDouble(index));
        db.close();
        return re;
    }

    public BigDecimal querymonthincome(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select sum(num) as Monthsincome from _Bill where EXTRACT(month from time) = EXTRACT(month from now()) AND type = 0";
        Cursor cursor = db.rawQuery(sql,null);

        int index = cursor.getColumnIndex("Monthsincome");
        BigDecimal re = new BigDecimal (cursor.getDouble(index));
        db.close();
        return re;
    }

    public List querylatefirst(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select firstcategory, num, time from _Bill order by time DESC";
        Cursor cursor = db.rawQuery(sql, null);
        List<Bill> re = new LinkedList<Bill>();
        int firstindex = cursor.getColumnIndex("firstcategory");
        int timeindex = cursor.getColumnIndex("time");
        int numindex = cursor.getColumnIndex("num");
        int cnt = 0;
        while (cursor.moveToNext() && cnt < 20){
            Bill tmp = new Bill(new BigDecimal(cursor.getDouble(numindex)), new Date (cursor.getLong(timeindex)), null, cursor.getString(firstindex), null, null, null, null, -1);
            cnt++;
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List querylatesecond(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select secondcategory, num, time from _Bill order by time DESC";
        Cursor cursor = db.rawQuery(sql, null);
        List<Bill> re = new LinkedList<Bill>();
        int secondindex = cursor.getColumnIndex("secondcategory");
        int timeindex = cursor.getColumnIndex("time");
        int numindex = cursor.getColumnIndex("num");
        int cnt = 0;
        while (cursor.moveToNext() && cnt < 20){
            Bill tmp = new Bill(new BigDecimal(cursor.getDouble(numindex)), new Date (cursor.getLong(timeindex)), null, null, cursor.getString(secondindex), null, null, null, -1);
            cnt++;
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List queryaccountname(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select account from _Bill group by account";
        Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<String>();
        int accountindex = cursor.getColumnIndex("account");
        while(cursor.moveToNext()){
            String tmp = cursor.getString(accountindex);
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public  List queryaccountnum(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select sum(num) as pay from _Bill where type = 1 group by account";
        Cursor cursor = db.rawQuery(sql, null);
        List<BigDecimal> re = new LinkedList<BigDecimal>();
        int payindex = cursor.getColumnIndex("pay");
        while (cursor.moveToNext()){
            BigDecimal tmp = new BigDecimal(cursor.getDouble(payindex));
            re.add(tmp);
        }
        db.close();

        sql = "select sum(num) as income from _Bill where type = 0 group by account";
        cursor = db.rawQuery(sql, null);
        int incomeindex = cursor.getColumnIndex("income");
        int cnt = 0;
        while(cursor.moveToNext()){
            BigDecimal tmp = new BigDecimal(cursor.getDouble(incomeindex));
            re.set(cnt, tmp.subtract(re.get(cnt)));
            cnt++;
        }
        db.close();
        return re;
    }

    public BigDecimal querrynum(){
        List<BigDecimal> ans = new LinkedList<BigDecimal>();
        ans = queryaccountnum();
        BigDecimal re = new BigDecimal(0);
        for(int i = 0; i < ans.size(); i++){
            re.add(ans.get(i));
        }
        return re;
    }

    public List querywater(Date start, Date end, String Account, String Number){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select num,time,username,firstcategory,secondcategory,account,number,others,type from _Bill where "+Account+" = account AND "+Number+" = number AND start <= time AND end >= time order by time DESC";
        Cursor cursor = db.rawQuery(sql, null);
        int numindex = cursor.getColumnIndex("num");
        int firstcategoryindex = cursor.getColumnIndex("firstcategory");
        int timeindex = cursor.getColumnIndex("time");
        int username = cursor.getColumnIndex("username");
        int secondcategoryindex = cursor.getColumnIndex("secondcategory");
        int accountindex = cursor.getColumnIndex("account");
        int numberindex = cursor.getColumnIndex("number");
        int othersindex = cursor.getColumnIndex("othters");
        int typeindex = cursor.getColumnIndex("type");
        List<Bill> re = new LinkedList<Bill>();
        while(cursor.moveToNext()){
            Bill tmp = new Bill(new BigDecimal(cursor.getDouble(numindex)), new Date (cursor.getLong(timeindex)), cursor.getString(username), cursor.getString(firstcategoryindex), cursor.getString(secondcategoryindex),  cursor.getString(accountindex), cursor.getString(numberindex), cursor.getString(othersindex), cursor.getInt(typeindex));
            re.add(tmp);
        }
        return re;
    }

    public boolean querryaccount(String name){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select account from _Bill where "+name+" = account";
        Cursor cursor = db.rawQuery(sql, null);
        int cnt = 0;
        while (cursor.moveToNext()){
            cnt++;
        }
        db.close();
        if(cnt>0) return true;
        else return false;
    }*/
}