package com.xuexiang.cdaccount.somethingDao.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xuexiang.cdaccount.ExpanableBill.BillDataDay;
import com.xuexiang.cdaccount.ExpanableBill.BillDataItem;
import com.xuexiang.cdaccount.ExpanableBill.BillDataMonth;
import com.xuexiang.cdaccount.ExpanableBill.BillDataYear;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.database.AccountDataEntry;
import com.xuexiang.cdaccount.database.ChartDataEntry;
import com.xuexiang.cdaccount.somethingDao.DatabaseHelper;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.xuexiang.xutil.XUtil.getResources;

/**
 * Bill账单
 */
public class BillDao {
    private final DatabaseHelper mHelper;

    public BillDao(Context context) {
        mHelper = new DatabaseHelper(context);
    }

    public int id_num = 0;

    public void changeaccount(int id, int type, double money){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        if(type == 0){
            String sql = "select * from Account where Account_ID = "+id+"";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()){
                double m = cursor.getDouble(cursor.getColumnIndex("Account_OutMoney"));
                m+=money;
                sql = "update Account set Account_OutMoney = '"+m+"' where Account_ID = "+id+"";
                db.execSQL(sql);
            }
        }
        else{
            String sql = "select * from Account where Account_ID = "+id+"";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()){
                double m = cursor.getDouble(cursor.getColumnIndex("Account_InMoney"));
                m+=money;
                sql = "update Account set Account_InMoney = '"+m+"' where Account_ID = "+id+"";
                db.execSQL(sql);
            }
        }
    }

    public void insertBill(int type, int subcategory, int account, int toaccount, int member, String year, String month, String day, String time, String remark, double money) {//通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "insert into Bill(Bill_ID, Bill_TYPE, Bill_SubCategory, Bill_Account, Bill_toAccount, Bill_Member, year, month, day, time, Bill_Remark, Bill_Money) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{id_num, type, subcategory, account, toaccount, member, year, month, day, time, remark, money});
        id_num++;
        db.close();
    }

    public void InsertBill(int type, String subcategory, String account, String toaccount, String member, String year, String month, String day, String time, String remark, double money) {  //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int sub = 0, acc = 0, toa = 0, mem = 0;
        if (type == 0) {
            String sql = "select * from OutSubCategory where OutSubCategory_Name = '" + subcategory + "'";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext())
                sub = cursor.getInt(cursor.getColumnIndex("OutSubCategory_ID"));
        } else {
            String sql = "select * from InSubCategory where InSubCategory_Name = '" + subcategory + "'";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext())
                sub = cursor.getInt(cursor.getColumnIndex("InSubCategory_ID"));
        }
        String sql = "select * from Account where Account_Name = '" + account + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext())
            acc = cursor.getInt(cursor.getColumnIndex("Account_ID"));

        sql = "select * from Account where Account_Name = '" + toaccount + "'";
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext())
            toa = cursor.getInt(cursor.getColumnIndex("Account_ID"));

        sql = "select * from Member where Member_Name = '" + member + "'";
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext())
            mem = cursor.getInt(cursor.getColumnIndex("Member_ID"));
        db.close();
        insertBill(type, sub, acc, toa, mem, year, month, day, time, remark, money);
        changeaccount(acc, type, money);
    }

    public boolean InsertCategory(String Top, String sub, int type) {   //通过测试,返回值需要判断
        SQLiteDatabase db = mHelper.getWritableDatabase();

        boolean sucessTop = false;
        boolean sucessSub = false;
        if (type == 0) {
            String sql = "select OutTopCategory_Name from OutTopCategory where OutTopCategory_Name = '" + Top + "'";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("OutTopCategory_Name")).equals(Top))
                    sucessTop = true;
            }
            sql = "select OutSubCategory_Name from OutSubCategory where OutSubCategory_Name = '" + sub + "'";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("OutSubCategory_Name")).equals(sub))
                    sucessSub = true;
            }
        } else {
            String sql = "select InTopCategory_Name from InTopCategory where InTopCategory_Name = '" + Top + "'";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("InTopCategory_Name")).equals(Top))
                    sucessTop = true;
            }
            sql = "select InSubCategory_Name from InSubCategory where InSubCategory_Name = '" + sub + "'";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("InSubCategory_Name")).equals(sub))
                    sucessSub = true;
            }
        }
        if (sucessSub && sucessTop) {
            db.close();
            return false;
        }

        if (type == 0) { //支出

            Cursor cursor = db.query("OutTopCategory", null, null, null, null, null, null);
            int cnt = 0;
            while (cursor.moveToNext()) {
                cnt = cursor.getInt(cursor.getColumnIndex("OutTopCategory_ID"));
                cnt++;
            }

            cursor = db.query("OutSubCategory", null, null, null, null, null, null);
            int cnt1 = 0;
            while (cursor.moveToNext()) {
                cnt1 = cursor.getInt(cursor.getColumnIndex("OutSubCategory_ID"));
                cnt1++;
            }

            String sql = "select * from OutTopCategory where OutTopCategory_Name = '" + Top + "'";
            cursor = db.rawQuery(sql, null);
            int tmp = -1;
            while (cursor.moveToNext()) {
                tmp = cursor.getInt(cursor.getColumnIndex("OutTopCategory_ID"));
            }
            if (tmp == -1)  //没有找到一级目录
            {
                sql = "insert into OutTopCategory(OutTopCategory_Id, OutTopCategory_name) values(?,?)";
                db.execSQL(sql, new Object[]{cnt, Top});

                sql = "insert into OutSubCategory(OutSubCategory_Id, OutSubCategory_Parent, OutSubCategory_name) values(?,?,?)";
                db.execSQL(sql, new Object[]{cnt1, cnt, sub});
            } else {    //找到一级目录，tmp是标号
                sql = "insert into OutSubCategory(OutSubCategory_Id, OutSubCategory_Parent, OutSubCategory_name) values(?,?,?)";
                db.execSQL(sql, new Object[]{cnt1, tmp, sub});
            }
        } else {
            Cursor cursor = db.query("InTopCategory", null, null, null, null, null, null);
            int cnt = 0;
            while (cursor.moveToNext()) {
                cnt = cursor.getInt(cursor.getColumnIndex("InTopCategory_ID"));
                cnt++;
            }

            cursor = db.query("InSubCategory", null, null, null, null, null, null);
            int cnt1 = 0;
            while (cursor.moveToNext()) {
                cnt1 = cursor.getInt(cursor.getColumnIndex("InSubCategory_ID"));
                cnt1++;
            }

            String sql = "select * from InTopCategory where InTopCategory_Name = '" + Top + "'";
            cursor = db.rawQuery(sql, null);
            int tmp = -1;
            while (cursor.moveToNext()) {
                tmp = cursor.getInt(cursor.getColumnIndex("InTopCategory_ID"));
            }
            if (tmp == -1)  //没有找到一级目录
            {
                sql = "insert into InTopCategory(InTopCategory_Id, InTopCategory_name) values(?,?)";
                db.execSQL(sql, new Object[]{cnt, Top});

                sql = "insert into InSubCategory(InSubCategory_Id, InSubCategory_Parent,InSubCategory_name) values(?,?,?)";
                db.execSQL(sql, new Object[]{cnt1, cnt, sub});
            } else {    //找到一级目录，tmp是标号
                sql = "insert into InSubCategory(InSubCategory_Id, InSubCategory_Parent, InSubCategory_name) values(?,?,?)";
                db.execSQL(sql, new Object[]{cnt1, tmp, sub});
            }
        }
        db.close();
        return true;
    }

    public boolean InsertMember(String name) { //通过测试,返回值需要判断
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String sql = "select Member_Name from Member where Member_Name = '" + name + "'";
        Cursor cursor = db.rawQuery(sql, null);
        boolean sucess = false;
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("Member_Name")).equals(name)) {
                sucess = true;
            }
        }
        if (sucess) {
            db.close();
            return false;
        }

        cursor = db.query("Member", null, null, null, null, null, null);
        int tmp = 0;
        while (cursor.moveToNext()) {
            tmp = cursor.getInt(cursor.getColumnIndex("Member_ID"));
            tmp++;
        }
        sql = "insert into Member(Member_Id, Member_Name) values(?,?)";
        db.execSQL(sql, new Object[]{tmp, name});

        db.close();
        return true;
    }

    public boolean InsertAccount(String name) {   //通过测试,返回值需要判断
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select Account_Name from Account where Account_Name = '" + name + "'";
        Cursor cursor = db.rawQuery(sql, null);
        boolean sucess = false;
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("Account_Name")).equals(name)) {
                sucess = true;
            }
        }
        if (sucess) {
            db.close();
            return false;
        }

        cursor = db.query("Account", null, null, null, null, null, null);
        int tmp = 0;
        while (cursor.moveToNext()) {
            tmp = cursor.getInt(cursor.getColumnIndex("Account_ID"));
            tmp++;
        }
        sql = "insert into Account(Account_Id, Account_Name, Account_InMoney, Account_OutMoney) values(?,?,?,?)";
        db.execSQL(sql, new Object[]{tmp, name,0,0});
        db.close();
        return true;
    }

    public List<String> QueryMember() {  //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select Member_name from Member";
        Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<String>();
        while (cursor.moveToNext()) {
            String tmp = cursor.getString(cursor.getColumnIndex("Member_Name"));
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List<String> QueryAccount() {  //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select Account_name from Account where Account_ID > 0";
        Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<String>();
        while (cursor.moveToNext()) {
            String tmp = cursor.getString(cursor.getColumnIndex("Account_Name"));
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public double QueryMonthIncome() {  //通过测试
        Calendar cal = Calendar.getInstance();
        int tmp = cal.get(Calendar.MONTH) + 1;
        int tmp1 = cal.get(Calendar.YEAR);
        String Month = "" + tmp;
        String Year = "" + tmp1;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select sum(Bill_Money) as ans from Bill where month = '" + Month + "' AND Bill_TYPE = 1 AND year = '" + Year + "'";
        Cursor cursor = db.rawQuery(sql, null);
        double re = 0;
        if (cursor.moveToNext()) re = cursor.getDouble(cursor.getColumnIndex("ans"));
        db.close();
        return re;
    }

    public double QueryMonthpay() {  //通过测试
        Calendar cal = Calendar.getInstance();
        int tmp = cal.get(Calendar.MONTH) + 1;
        int tmp1 = cal.get(Calendar.YEAR);
        String Month = "" + tmp;
        String Year = "" + tmp1;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select sum(Bill_Money) as ans from Bill where month = '" + Month + "' AND Bill_TYPE = 0 AND year = '" + Year + "'";
        Cursor cursor = db.rawQuery(sql, null);
        double re = 0;
        if (cursor.moveToNext()) re = cursor.getDouble(cursor.getColumnIndex("ans"));
        db.close();
        return re;
    }

    public List<String> GetRecentDate() {  //通过测试
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from Bill order by year || month || day || time DESC";
        Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<String>();
        int cnt = 0;
        while (cursor.moveToNext() && cnt < 20) {
            if (cursor.getInt(cursor.getColumnIndex("Bill_TYPE")) == 2) continue;
            cnt++;
            String Y = cursor.getString(cursor.getColumnIndex("year"));
            String M = cursor.getString(cursor.getColumnIndex("month"));
            String D = cursor.getString(cursor.getColumnIndex("day"));
            String T = cursor.getString(cursor.getColumnIndex("time"));
            String ans = Y + "-" + M + "-" + D + " " + T;
            re.add(ans);
        }
        db.close();
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
        db.close();
        return re;
    }

    public List<Integer> GetRecentIO() {   //通过测试
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "select * from Bill order by year || month || day || time DESC";
        Cursor cursor = db.rawQuery(sql, null);
        List<Integer> re = new LinkedList<Integer>();
        int cnt = 0;
        while (cursor.moveToNext() && cnt < 20) {
            if (cursor.getInt(cursor.getColumnIndex("Bill_TYPE")) == 2) continue;
            cnt++;
            Integer IO = cursor.getInt(cursor.getColumnIndex("Bill_TYPE"));
            re.add(IO);
        }
        db.close();
        return re;
    }

    public List<String> QueryOutTopCategory() {  //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select OutTopCategory_Name from OutTopCategory";
        Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<String>();
        while (cursor.moveToNext()) {
            String tmp = cursor.getString(cursor.getColumnIndex("OutTopCategory_Name"));
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List<List<String>> QueryOutSubCategory() {   //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from OutTopCategory";
        Cursor cursor = db.rawQuery(sql, null);
        List<List<String>> re = new LinkedList<List<String>>();

        while (cursor.moveToNext()) {
            int tmp1 = cursor.getInt(cursor.getColumnIndex("OutTopCategory_ID"));
            String sql1 = "select OutSubCategory_Name from OutSubCategory where OutSubCategory_Parent = " + tmp1 + "";
            Cursor cursor1 = db.rawQuery(sql1, null);
            List<String> tmp = new LinkedList<String>();
            while (cursor1.moveToNext()) {
                String tmp3 = cursor1.getString(cursor1.getColumnIndex("OutSubCategory_Name"));
                tmp.add(tmp3);
            }
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List<String> QueryInTopCategory() {  //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select InTopCategory_Name from InTopCategory";
        Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<String>();
        while (cursor.moveToNext()) {
            String tmp = cursor.getString(cursor.getColumnIndex("InTopCategory_Name"));
            re.add(tmp);
        }
        db.close();
        return re;
    }

    public List<List<String>> QueryInSubCategory() {   //通过测试
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from InTopCategory";
        Cursor cursor = db.rawQuery(sql, null);
        List<List<String>> re = new LinkedList<List<String>>();

        while (cursor.moveToNext()) {
            int tmp1 = cursor.getInt(cursor.getColumnIndex("InTopCategory_ID"));
            String sql1 = "select InSubCategory_Name from InSubCategory where InSubCategory_Parent = " + tmp1 + "";
            Cursor cursor1 = db.rawQuery(sql1, null);
            List<String> tmp = new LinkedList<String>();
            while (cursor1.moveToNext()) {
                String tmp3 = cursor1.getString(cursor1.getColumnIndex("InSubCategory_Name"));
                tmp.add(tmp3);
            }
            re.add(tmp);
        }
        db.close();
        return re;
    }

    /**
     * 按一级分类分组获取支出
     * @param start_year 开始日期年份
     * @param start_month 开始日期月份
     * @param start_day 开始日期天
     * @param end_year 结束日期年份
     * @param end_month 结束日期月份
     * @param end_day 结束日期天
     * @return List<ChartDataEntry>
     */
    public List<ChartDataEntry> GetDataByOutTopCategory(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){//通过测试
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
        db.close();
        return re;
    }


    /**
     * 按二级分类分组获取支出
     * @param start_year 开始日期年份
     * @param start_month 开始日期月份
     * @param start_day 开始日期天
     * @param end_year 结束日期年份
     * @param end_month 结束日期月份
     * @param end_day 结束日期天
     * @return List<ChartDataEntry>
     */
    public List<ChartDataEntry> GetDataByOutSubCategory(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){  //通过测试
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
        db.close();
        return re;
    }


    /**
     * 按一级分类分组获取收入
     * @param start_year 开始日期年份
     * @param start_month 开始日期月份
     * @param start_day 开始日期天
     * @param end_year 结束日期年份
     * @param end_month 结束日期月份
     * @param end_day 结束日期天
     * @return List<ChartDataEntry>
     */
    public List<ChartDataEntry> GetDataByInTopCategory(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){//通过测试
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Map<Integer, Integer> mp1 = new HashMap<Integer, Integer>();
        String sql2 = "select * from InSubCategory";
        Cursor cursor2 = db.rawQuery(sql2, null);
        while(cursor2.moveToNext()){
            mp1.put(cursor2.getInt(cursor2.getColumnIndex("InSubCategory_ID")),cursor2.getInt(cursor2.getColumnIndex("InSubCategory_Parent")));
        }

        Map<Integer, String> mp = new HashMap<Integer, String>();
        String sql1 = "select * from InTopCategory";
        Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("InTopCategory_ID")),cursor1.getString(cursor1.getColumnIndex("InTopCategory_Name")));
        }

        String sql = "select Bill_ID, Bill_SubCategory, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND Bill_TYPE = 1 group by Bill_SubCategory";
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
        db.close();
        return re;
    }


    /**
     * 按二级分类分组获取收入
     * @param start_year 开始日期年份
     * @param start_month 开始日期月份
     * @param start_day 开始日期天
     * @param end_year 结束日期年份
     * @param end_month 结束日期月份
     * @param end_day 结束日期天
     * @return List<ChartDataEntry>
     */
    public List<ChartDataEntry> GetDataByInSubCategory(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){  //通过测试
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Map<Integer, String> mp = new HashMap<Integer, String>();
        String sql1 = "select * from InSubCategory";
        Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("InSubCategory_ID")),cursor1.getString(cursor1.getColumnIndex("InSubCategory_Name")));
        }

        String sql = "select Bill_ID, Bill_SubCategory, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND Bill_TYPE = 1 group by Bill_SubCategory";
        Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<ChartDataEntry>();

        while(cursor.moveToNext()){
            Integer tmp1 = new Integer(cursor.getInt(cursor.getColumnIndex("Bill_SubCategory")));
            ChartDataEntry tmp = new ChartDataEntry(mp.get(tmp1),cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        db.close();
        return re;
    }


    /**
     * 按成员分组获取收入或者支出
     * @param start_year  开始日期年份
     * @param start_month  开始日期月份
     * @param start_day  开始日期天
     * @param end_year  结束日期年份
     * @param end_month  结束日期月份
     * @param end_day  结束日期天
     * @param type  收支类型
     * @return  List<ChartDataEntry>
     */
    public List<ChartDataEntry> GetDataByMember(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day, int type){
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Map<Integer, String> mp = new HashMap<Integer, String>();
        String sql1 = "select * from Member";
        Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("Member_ID")),cursor1.getString(cursor1.getColumnIndex("Member_Name")));
        }

        String sql = "select Bill_ID, Bill_Member, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND Bill_TYPE = "+type+" group by Bill_Member";
        Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<ChartDataEntry>();

        while(cursor.moveToNext()){
            Integer tmp1 = new Integer(cursor.getInt(cursor.getColumnIndex("Bill_Member")));
            ChartDataEntry tmp = new ChartDataEntry(mp.get(tmp1),cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        db.close();
        return re;
    }


    /**
     * 按账户分作获取收入或者支出
     * @param start_year  开始日期年份
     * @param start_month  开始日期月份
     * @param start_day  开始日期天
     * @param end_year  结束日期年份
     * @param end_month  结束日期月份
     * @param end_day  结束日期天
     * @param type  收支类型
     * @return  List<ChartDataEntry>
     */
    public List<ChartDataEntry> GetDataByAccount(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day, int type){
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Map<Integer, String> mp = new HashMap<Integer, String>();
        String sql1 = "select * from Account";
        Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("Account_ID")),cursor1.getString(cursor1.getColumnIndex("Account_Name")));
        }

        String sql = "select Bill_ID, Bill_Account, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND Bill_TYPE = "+type+" group by Bill_Account";
        Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<ChartDataEntry>();

        while(cursor.moveToNext()){
            Integer tmp1 = new Integer(cursor.getInt(cursor.getColumnIndex("Bill_Account")));
            ChartDataEntry tmp = new ChartDataEntry(mp.get(tmp1),cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        db.close();
        return re;
    }


    /**
     * 获取每一天的总收入或者支出
     * @param start_year  开始日期年份
     * @param start_month  开始日期月份
     * @param start_day  开始日期天
     * @param end_year  结束日期年份
     * @param end_month  结束日期月份
     * @param end_day  结束日期天
     * @param type  收支类型
     * @return  List<ChartDataEntry>
     */
    public List<ChartDataEntry> GetSumByDate(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day, int type){ //按天求和
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        String sql = "select year || month || day as date, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND Bill_TYPE = "+type+" group by date";
        Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<ChartDataEntry>();

        while(cursor.moveToNext()){
            ChartDataEntry tmp = new ChartDataEntry(cursor.getString(cursor.getColumnIndex("date")), cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        db.close();
        return re;
    }


    /**
     * 获取账户名和收入支出
     * @return List<AccountDataEntry>
     */
    public List<AccountDataEntry> getBalanceByAccount() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from Account where Account_ID >= 1";
        Cursor cursor = db.rawQuery(sql, null);
        List<AccountDataEntry> re = new LinkedList<>();
        while (cursor.moveToNext()) {
            AccountDataEntry tmp = new AccountDataEntry(cursor.getString(cursor.getColumnIndex("Account_Name")), cursor.getDouble(cursor.getColumnIndex("Account_InMoney")), cursor.getDouble(cursor.getColumnIndex("Account_OutMoney")));
            re.add(tmp);
        }
        db.close();
        return re;
    }


    /**
     * 获取流水账户
     * @param Year  需要流水的年份
     * @param member  筛选的成员
     * @param account  筛选的账户
     * @return BillDataYear
     */
    public BillDataYear getJournalAccount(String Year, String member, String account){
        double YearIncome = 0, YearOutcome = 0;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        List<BillDataMonth> billDataMonthList = new LinkedList<>();
        int member1 = -1, account1 = -1;
        String sql1 = "select * from Account where Account_Name = '"+account+"'";
        Cursor cursor1 = db.rawQuery(sql1, null);
        while (cursor1.moveToNext())
        {
            account1 = cursor1.getInt(cursor1.getColumnIndex("Account_ID"));
        }
        sql1 = "select * from Member where Member_Name = '"+member+"'";
        cursor1 = db.rawQuery(sql1, null);
        while (cursor1.moveToNext())
        {
            member1 = cursor1.getInt(cursor1.getColumnIndex("Member_ID"));
        }
        for(int i = 12; i >0 ; i--){
            boolean monthexist = false;
            String itmp = String.valueOf(i);
            if(i < 10){
                String tmp = String.valueOf(i);
                itmp = "0"+tmp;
            }
            double monthincome = 0.0, monthoutcome = 0.0;
            List<BillDataDay> billDataDayList = new LinkedList<>();
            for(int j = 31; j > 0; j--){
                String jtmp = String.valueOf(j);
                if(j < 10){
                    String tmp = String.valueOf(j);
                    jtmp = "0"+tmp;
                }
                List<BillDataItem> billDataItemList = new LinkedList<>();
                String sql = "";
                if(member.equals(getResources().getString(R.string.unlimited)) && account.equals(getResources().getString(R.string.unlimited))) sql = "select * from Bill where year = '"+Year+"' AND month = '"+itmp+"' AND day = '"+jtmp+"'";
                else if(member.equals(getResources().getString(R.string.unlimited))) sql = "select * from Bill where year = '"+Year+"' AND month = '"+itmp+"' AND day = '"+jtmp+"' AND Bill_Account = "+account1+"";
                else if(account.equals(getResources().getString(R.string.unlimited))) sql = "select * from Bill where year = '"+Year+"' AND month = '"+itmp+"' AND day = '"+jtmp+"' AND Bill_Member = "+member1+"";
                else sql = "select * from Bill where year = '"+Year+"' AND month = '"+itmp+"' AND day = '"+jtmp+"' AND Bill_Account = "+account1+" AND Bill_Member = "+member1+"";
                Cursor cursor = db.rawQuery(sql, null);
                double income = 0.0, outcome = 0.0;
                boolean dayexist = false;
                while(cursor.moveToNext()){
                    dayexist = true;
                    monthexist = true;
                    String category = "", acc = "", toaccount = "", mem = "";
                    int type = cursor.getInt(cursor.getColumnIndex("Bill_TYPE"));

                    String sql3;
                    int category1 = cursor.getInt(cursor.getColumnIndex("Bill_SubCategory"));
                    if(type == 0){
                        sql3 = "select * from OutSubCategory where OutSubCategory_ID = "+category1+"";
                        Cursor cursor2 = db.rawQuery(sql3, null);
                        while (cursor2.moveToNext()){
                            category = cursor2.getString(cursor2.getColumnIndex("OutSubCategory_Name"));
                        }
                    }
                    else{
                        sql3 = "select * from InSubCategory where InSubCategory_ID = "+category1+"";
                        Cursor cursor2 = db.rawQuery(sql3, null);
                        while (cursor2.moveToNext()){
                            category = cursor2.getString(cursor2.getColumnIndex("InSubCategory_Name"));
                        }
                    }

                    int acc1 = cursor.getInt(cursor.getColumnIndex("Bill_Account"));
                    sql3 = "select * from Account where Account_ID = "+acc1+"";
                    Cursor cursor2 = db.rawQuery(sql3, null);
                    while (cursor2.moveToNext()){
                        acc = cursor2.getString(cursor2.getColumnIndex("Account_Name"));
                    }

                    int toaccount1 = cursor.getInt(cursor.getColumnIndex("Bill_toAccount"));
                    sql3 = "select * from Account where Account_ID = "+toaccount1+"";
                    cursor2 = db.rawQuery(sql3, null);
                    while (cursor2.moveToNext()){
                        toaccount = cursor2.getString(cursor2.getColumnIndex("Account_Name"));
                    }

                    int mem1 = cursor.getInt(cursor.getColumnIndex("Bill_Member"));
                    sql3 = "select * from Member where Member_ID = "+mem1+"";
                    cursor2 = db.rawQuery(sql3, null);
                    while (cursor2.moveToNext()){
                        mem = cursor2.getString(cursor2.getColumnIndex("Member_Name"));
                    }

                    String yea = cursor.getString(cursor.getColumnIndex("year"));
                    String month = cursor.getString(cursor.getColumnIndex("month"));
                    String day = cursor.getString(cursor.getColumnIndex("day"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    double money = cursor.getDouble(cursor.getColumnIndex("Bill_Money"));
                    String remark = cursor.getString(cursor.getColumnIndex("Bill_Remark"));
                    BillDataItem tmp = new BillDataItem(type, category, acc, toaccount, mem, yea, month, day, time, money, remark);
                    billDataItemList.add(tmp);
                    if(type == 0) outcome += money;
                    else income += money;
                }
                if(!dayexist) continue;
                monthincome += income;
                monthoutcome += outcome;
                BillDataDay tmp = new BillDataDay(jtmp, income, outcome, billDataItemList);
                billDataDayList.add(tmp);
            }
            if(!monthexist) continue;
            BillDataMonth tmp = new BillDataMonth(itmp, monthincome, monthoutcome, billDataDayList);
            billDataMonthList.add(tmp);
            YearIncome += monthincome;
            YearOutcome += monthoutcome;
        }
        BillDataYear re = new BillDataYear(Year, YearIncome, YearOutcome, billDataMonthList);
        db.close();
        return re;
    }


    /**
     * 修改账户名称
     * @param preaccountname 原账户名
     * @param nowname 新账户名
     * @return
     */
    public boolean ChangeAccountName(String preaccountname, String nowname){
        boolean eixt = false;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select * from Account where Account_Name = '"+preaccountname+"'";
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            eixt = true;
        }
        if(eixt){
            sql = "update Account set Account_Name = '"+nowname+"' where Account_Name = '"+preaccountname+"'";
            db.execSQL(sql);
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }


    /**
     * 数据库初始化
     */
    public void Init(){
        SQLiteDatabase db = mHelper.getReadableDatabase();
        String sql = "insert into OutSubCategory(OutSubCategory_ID, OutSubCategory_Parent, OutSubCategory_Name) values(?,?,?)";
        db.execSQL(sql,new Object[]{0, 999999, ""});

        sql = "insert into InSubCategory(InSubCategory_ID, InSubCategory_Parent, InSubCategory_Name) values(?,?,?)";
        db.execSQL(sql,new Object[]{0, 999999, ""});

        sql = "insert into Account(Account_ID, Account_Name) values(?,?)";
        db.execSQL(sql,new Object[]{0,""});

        db.close();
    }


}