package com.xuexiang.cdaccount.somethingDao.Dao;

import android.annotation.SuppressLint;
import android.content.Context;

import com.xuexiang.cdaccount.ExpanableBill.BillDataDay;
import com.xuexiang.cdaccount.ExpanableBill.BillDataItem;
import com.xuexiang.cdaccount.ExpanableBill.BillDataMonth;
import com.xuexiang.cdaccount.ExpanableBill.BillDataYear;
import com.xuexiang.cdaccount.R;
import com.xuexiang.cdaccount.dbclass.AccountDataEntry;
import com.xuexiang.cdaccount.dbclass.ChartDataEntry;
import com.xuexiang.cdaccount.somethingDao.DatabaseHelper;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

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
    private int id_num = 0;

    private final SQLiteDatabase db;

    public BillDao(Context context) {
        String key = "WbgsZfdiApPA";

        DatabaseHelper mHelper = new DatabaseHelper(context);
        db = mHelper.getWritableDatabase(key);
    }



    /**
     *  初始化数据库，建立表
     */
    public void myCreateTable(){
        String sql;

        sql = "create table "+"Bill"+"(Bill_ID int, Bill_TYPE int, Bill_SubCategory int, Bill_Account int, Bill_toAccount int, Bill_Member int, year varchar(5), month varchar(3), day varchar(3), time varchar(15), Bill_Remark varchar(255), Bill_Money double)";
        db.execSQL(sql);

        sql = "create table "+"OutTopCategory"+"(OutTopCategory_ID int, OutTopCategory_Name String)";
        db.execSQL(sql);

        sql = "create table "+"OutSubCategory"+"(OutSubCategory_ID int, OutSubCategory_Parent, OutSubCategory_Name varchar(15))";
        db.execSQL(sql);

        sql = "create table "+"InTopCategory"+"(InTopCategory_ID int, InTopCategory_Name varchar(15))";
        db.execSQL(sql);

        sql = "create table "+"InSubCategory"+"(InSubCategory_ID int, InSubCategory_Parent, InSubCategory_Name)";
        db.execSQL(sql);

        sql = "create table "+"Account"+"(Account_ID int, Account_Name varchar(15), Account_InMoney double, Account_OutMoney)";
        db.execSQL(sql);

        sql = "create table "+"Member"+"(Member_ID int, Member_Name varchar(15))";
        db.execSQL(sql);
    }


    /**
     * 数据库初始化内容，插入特殊数据
     */
    public void initSpecialData(){
        String sql = "insert into OutSubCategory(OutSubCategory_ID, OutSubCategory_Parent, OutSubCategory_Name) values(?,?,?)";
        db.execSQL(sql,new Object[]{0, 999999, ""});

        sql = "insert into InSubCategory(InSubCategory_ID, InSubCategory_Parent, InSubCategory_Name) values(?,?,?)";
        db.execSQL(sql,new Object[]{0, 999999, ""});

        sql = "insert into Account(Account_ID, Account_Name) values(?,?)";
        db.execSQL(sql,new Object[]{0,""});
    }


    /**
     * 初始化固定表内容
     */
    public void initData(){
        String sql;
        sql = "insert into OutTopCategory(OutTopCategory_Id, OutTopCategory_name) values(?,?)";
        db.execSQL(sql, new Object[]{0, "餐饮"});
        db.execSQL(sql, new Object[]{1, "交通"});
        db.execSQL(sql, new Object[]{2, "娱乐"});
        db.execSQL(sql, new Object[]{3, "购物"});

        sql = "insert into OutSubCategory(OutSubCategory_Id, OutSubCategory_Parent, OutSubCategory_name) values(?,?,?)";
        db.execSQL(sql, new Object[]{1, 0,"早餐"});
        db.execSQL(sql, new Object[]{2, 0,"午餐"});
        db.execSQL(sql, new Object[]{3, 0,"晚餐"});
        db.execSQL(sql, new Object[]{4, 0,"水果"});
        db.execSQL(sql, new Object[]{5, 0,"零食"});
        db.execSQL(sql, new Object[]{6, 1,"公交"});
        db.execSQL(sql, new Object[]{7, 1,"地铁"});
        db.execSQL(sql, new Object[]{8, 1,"火车"});
        db.execSQL(sql, new Object[]{9, 1,"飞机"});
        db.execSQL(sql, new Object[]{10, 1,"出租"});
        db.execSQL(sql, new Object[]{11, 2,"电影"});
        db.execSQL(sql, new Object[]{12, 2,"健身"});
        db.execSQL(sql, new Object[]{13, 2,"游戏"});
        db.execSQL(sql, new Object[]{14, 2,"旅游"});
        db.execSQL(sql, new Object[]{15, 2,"按摩"});
        db.execSQL(sql, new Object[]{16, 3,"电器"});
        db.execSQL(sql, new Object[]{17, 3,"家具"});
        db.execSQL(sql, new Object[]{18, 3,"数码"});
        db.execSQL(sql, new Object[]{19, 3,"服饰"});
        db.execSQL(sql, new Object[]{20, 3,"美妆"});

        sql = "insert into InTopCategory(InTopCategory_Id, InTopCategory_name) values(?,?)";
        db.execSQL(sql, new Object[]{0, "职业收入"});
        db.execSQL(sql, new Object[]{1, "生意收入"});
        db.execSQL(sql, new Object[]{2, "人情收入"});

        sql = "insert into InSubCategory(InSubCategory_Id, InSubCategory_Parent,InSubCategory_name) values(?,?,?)";
        db.execSQL(sql, new Object[]{1, 0,"工资"});
        db.execSQL(sql, new Object[]{2, 0,"奖金"});
        db.execSQL(sql, new Object[]{3, 0,"福利"});
        db.execSQL(sql, new Object[]{4, 1,"提成"});
        db.execSQL(sql, new Object[]{5, 1,"退款"});
        db.execSQL(sql, new Object[]{6, 1,"贷款"});
        db.execSQL(sql, new Object[]{7, 2,"红包"});
        db.execSQL(sql, new Object[]{8, 2,"礼金"});
        db.execSQL(sql, new Object[]{9, 2,"压岁钱"});

        sql = "insert into Member(Member_Id, Member_Name) values(?,?)";
        db.execSQL(sql, new Object[]{0, "无成员"});
        db.execSQL(sql, new Object[]{1, "本人"});
        db.execSQL(sql, new Object[]{2, "配偶"});
        db.execSQL(sql, new Object[]{3, "子女"});
        db.execSQL(sql, new Object[]{4, "父母"});

        sql = "insert into Account(Account_Id, Account_Name, Account_InMoney, Account_OutMoney) values(?,?,?,?)";
        db.execSQL(sql, new Object[]{1, "现金",0,0});
        db.execSQL(sql, new Object[]{2, "信用卡",0,0});
        db.execSQL(sql, new Object[]{3, "储蓄卡",0,0});
        db.execSQL(sql, new Object[]{4, "支付宝",0,0});
        db.execSQL(sql, new Object[]{5, "基金",0,0});
    }


    /**
     * 插入一条流水账单(包含查询对应选项在相应表中的ID)
     * @param type  类型：支出、收入、转账
     * @param subcategory  二级分类
     * @param account  账户
     * @param toAccount  转账到账的账户
     * @param member  成员
     * @param year  年份
     * @param month  月份
     * @param day  日
     * @param time  时刻
     * @param remark  备注
     * @param money  金额
     */
    @SuppressLint("Recycle")
    public void insertBill(int type, String subcategory, String account, String toAccount, String member, String year, String month, String day, String time, String remark, double money) {  //通过测试
        int subAccountID = 0, accontID = 0, toa = 0, memberID = 0;
        if (type == 0) {
            String sql = "select * from OutSubCategory where OutSubCategory_Name = '" + subcategory + "'";
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext())
                subAccountID = cursor.getInt(cursor.getColumnIndex("OutSubCategory_ID"));
        } else {
            String sql = "select * from InSubCategory where InSubCategory_Name = '" + subcategory + "'";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext())
                subAccountID = cursor.getInt(cursor.getColumnIndex("InSubCategory_ID"));
        }
        String sql = "select * from Account where Account_Name = '" + account + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext())
            accontID = cursor.getInt(cursor.getColumnIndex("Account_ID"));

        sql = "select * from Account where Account_Name = '" + toAccount + "'";
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext())
            toa = cursor.getInt(cursor.getColumnIndex("Account_ID"));

        sql = "select * from Member where Member_Name = '" + member + "'";
        cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext())
            memberID = cursor.getInt(cursor.getColumnIndex("Member_ID"));
        cursor.close();

        insertBillByID(type, subAccountID, accontID, toa, memberID, year, month, day, time, remark, money);
        changeAccountMoneyWithBill(accontID, type, money, toa);
    }


    /**
     * 根据选项ID插入一条流水记账
     * @param type  类型：支出、收入、转账
     * @param subcategory  二级分类
     * @param account  账户
     * @param toaccount  转入账户
     * @param member  成员
     * @param year  年份
     * @param month  月份
     * @param day  日
     * @param time  时刻
     * @param remark  备注
     * @param money  金额
     */
    public void insertBillByID(int type, int subcategory, int account, int toaccount, int member, String year, String month, String day, String time, String remark, double money) {//通过测试

        String sql = "insert into Bill(Bill_ID, Bill_TYPE, Bill_SubCategory, Bill_Account, Bill_toAccount, Bill_Member, year, month, day, time, Bill_Remark, Bill_Money) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{id_num, type, subcategory, account, toaccount, member, year, month, day, time, remark, money});
        id_num++;
    }

    /**
     * 插入流水账同步更新账户金额
     * @param id 账户ID
     * @param type  收支类型
     * @param money  金额
     * @param toAccount  转入账户
     */
    @SuppressLint("Recycle")
    public void changeAccountMoneyWithBill(int id, int type, double money, int toAccount){

        if(type == 0){
            String sql = "select * from Account where Account_ID = "+id+"";
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()){
                double m = cursor.getDouble(cursor.getColumnIndex("Account_OutMoney"));
                m+=money;
                sql = "update Account set Account_OutMoney = '"+m+"' where Account_ID = "+id+"";
                db.execSQL(sql);
            }
            cursor.close();
        }
        else if(type == 1){
            String sql = "select * from Account where Account_ID = "+id+"";
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()){
                double m = cursor.getDouble(cursor.getColumnIndex("Account_InMoney"));
                m+=money;
                sql = "update Account set Account_InMoney = '"+m+"' where Account_ID = "+id+"";
                db.execSQL(sql);
            }
            cursor.close();
        }
        else{
            String sql = "select * from Account where Account_ID = "+id+"";
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()){
                double m = cursor.getDouble(cursor.getColumnIndex("Account_OutMoney"));
                m+=money;
                sql = "update Account set Account_OutMoney = '"+m+"' where Account_ID = "+id+"";
                db.execSQL(sql);
            }

            sql = "select * from Account where Account_ID = "+ toAccount +"";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()){
                double m = cursor.getDouble(cursor.getColumnIndex("Account_InMoney"));
                m+=money;
                sql = "update Account set Account_InMoney = '"+m+"' where Account_ID = "+ toAccount +"";
                db.execSQL(sql);
            }
            cursor.close();
        }

    }


    /**
     * 新建分类
     * @param topCategory  一级分类
     * @param subCategory  二级分类
     * @param type  收支类型
     * @return  是否插入成功
     */
    @SuppressLint("Recycle")
    public boolean insertCategory(String topCategory, String subCategory, int type) {   //通过测试,返回值需要判断

        boolean sucessTop = false;
        boolean sucessSub = false;
        if (type == 0) {
            String sql = "select OutTopCategory_Name from OutTopCategory where OutTopCategory_Name = '" + topCategory + "'";
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("OutTopCategory_Name")).equals(topCategory))
                    sucessTop = true;
            }
            sql = "select OutSubCategory_Name from OutSubCategory where OutSubCategory_Name = '" + subCategory + "'";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("OutSubCategory_Name")).equals(subCategory))
                    sucessSub = true;
            }
            cursor.close();
        } else {
            String sql = "select InTopCategory_Name from InTopCategory where InTopCategory_Name = '" + topCategory + "'";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("InTopCategory_Name")).equals(topCategory))
                    sucessTop = true;
            }
            sql = "select InSubCategory_Name from InSubCategory where InSubCategory_Name = '" + subCategory + "'";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex("InSubCategory_Name")).equals(subCategory))
                    sucessSub = true;
            }
            cursor.close();
        }
        if (sucessSub && sucessTop) {
            db.close();
            return false;
        }


        int cnt = 0;
        if (type == 0) { //支出

            Cursor cursor = db.query("OutTopCategory", null, null, null, null, null, null);
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

            String sql = "select * from OutTopCategory where OutTopCategory_Name = '" + topCategory + "'";
            cursor = db.rawQuery(sql, null);
            int tmp = -1;
            while (cursor.moveToNext()) {
                tmp = cursor.getInt(cursor.getColumnIndex("OutTopCategory_ID"));
            }
            if (tmp == -1)  //没有找到一级目录
            {
                sql = "insert into OutTopCategory(OutTopCategory_Id, OutTopCategory_name) values(?,?)";
                db.execSQL(sql, new Object[]{cnt, topCategory});

                sql = "insert into OutSubCategory(OutSubCategory_Id, OutSubCategory_Parent, OutSubCategory_name) values(?,?,?)";
                db.execSQL(sql, new Object[]{cnt1, cnt, subCategory});
            } else {    //找到一级目录，tmp是标号
                sql = "insert into OutSubCategory(OutSubCategory_Id, OutSubCategory_Parent, OutSubCategory_name) values(?,?,?)";
                db.execSQL(sql, new Object[]{cnt1, tmp, subCategory});
            }
            cursor.close();
        }
        else if(type == 1) {
            Cursor cursor = db.query("InTopCategory", null, null, null, null, null, null);
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

            String sql = "select * from InTopCategory where InTopCategory_Name = '" + topCategory + "'";
            cursor = db.rawQuery(sql, null);
            int tmp = -1;
            while (cursor.moveToNext()) {
                tmp = cursor.getInt(cursor.getColumnIndex("InTopCategory_ID"));
            }
            if (tmp == -1)  //没有找到一级目录
            {
                sql = "insert into InTopCategory(InTopCategory_Id, InTopCategory_name) values(?,?)";
                db.execSQL(sql, new Object[]{cnt, topCategory});

                sql = "insert into InSubCategory(InSubCategory_Id, InSubCategory_Parent,InSubCategory_name) values(?,?,?)";
                db.execSQL(sql, new Object[]{cnt1, cnt, subCategory});
            } else {    //找到一级目录，tmp是标号
                sql = "insert into InSubCategory(InSubCategory_Id, InSubCategory_Parent, InSubCategory_name) values(?,?,?)";
                db.execSQL(sql, new Object[]{cnt1, tmp, subCategory});
            }
            cursor.close();
        }
        return true;
    }


    /**
     * 新建一个新成员
     * @param memberName  成员名
     * @return  是否插入成功
     */
    @SuppressLint("Recycle")
    public boolean insertMember(String memberName) { //通过测试,返回值需要判断

        String sql = "select Member_Name from Member where Member_Name = '" + memberName + "'";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        boolean sucess = false;
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("Member_Name")).equals(memberName)) {
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
        db.execSQL(sql, new Object[]{tmp, memberName});

        cursor.close();

        return true;
    }


    /**
     * 新建一个账户
     * @param accountName  账户名
     * @return  是否成功
     */
    @SuppressLint("Recycle")
    public boolean insertAccount(String accountName) {   //通过测试,返回值需要判断

        String sql = "select Account_Name from Account where Account_Name = '" + accountName + "'";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        boolean sucess = false;
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("Account_Name")).equals(accountName)) {
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
        db.execSQL(sql, new Object[]{tmp, accountName,0,0});

        cursor.close();

        return true;
    }


    /**
     * 查询所有成员项（包含无成员）
     * @return  成员列表
     */
    public List<String> queryMemberList() {  //通过测试

        String sql = "select Member_name from Member";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<>();
        while (cursor.moveToNext()) {
            String tmp = cursor.getString(cursor.getColumnIndex("Member_Name"));
            re.add(tmp);
        }
        cursor.close();

        return re;
    }

    /**
     * 查询所有账户
     * @return  账户列表
     */
    public List<String> queryAccountList() {  //通过测试

        String sql = "select Account_name from Account where Account_ID > 0";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<>();
        while (cursor.moveToNext()) {
            String tmp = cursor.getString(cursor.getColumnIndex("Account_Name"));
            re.add(tmp);
        }
        cursor.close();

        return re;
    }

    /**
     * 查询当月总收入
     * @return 当月总收入金额
     */
    public double queryMonthIncome() {  //通过测试
        Calendar cal = Calendar.getInstance();
        int tmp = cal.get(Calendar.MONTH) + 1;
        int tmp1 = cal.get(Calendar.YEAR);
        String Month = "" + tmp;
        String Year = "" + tmp1;

        String sql = "select sum(Bill_Money) as ans from Bill where month = '" + Month + "' AND Bill_TYPE = 1 AND year = '" + Year + "'";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        double re = 0;
        if (cursor.moveToNext()) re = cursor.getDouble(cursor.getColumnIndex("ans"));
        cursor.close();

        return re;
    }

    /**
     * 查询当月总支出
     * @return  当月总支出金额
     */
    public double queryMonthOutcome() {  //通过测试
        Calendar cal = Calendar.getInstance();
        int tmp = cal.get(Calendar.MONTH) + 1;
        int tmp1 = cal.get(Calendar.YEAR);
        String Month = "" + tmp;
        String Year = "" + tmp1;

        String sql = "select sum(Bill_Money) as ans from Bill where month = '" + Month + "' AND Bill_TYPE = 0 AND year = '" + Year + "'";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        double re = 0;
        if (cursor.moveToNext()) re = cursor.getDouble(cursor.getColumnIndex("ans"));
        cursor.close();

        return re;
    }

    /**
     * 获取最近20条收支
     * @return 20条收支记录
     */
    public List<String> getRecentBillDate() {  //通过测试

        String sql = "select * from Bill order by year || month || day || time DESC";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<>();
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
        cursor.close();

        return re;
    }

    public List<String> getRecentInformation() {   //通过测试
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        String sql = "select * from Bill order by year || month || day || time DESC";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<>();
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
                @SuppressLint("Recycle") Cursor cursor1 = db.rawQuery(sql, null);
                while(cursor1.moveToNext())
                {
                    nametmp = cursor1.getString(cursor1.getColumnIndex("OutSubCategory_Name"));
                }
            }
            else{
                sql = "select * from InSubCategory where InSubCategory_ID ="+txt+"";
                @SuppressLint("Recycle") Cursor cursor1 = db.rawQuery(sql, null);
                while(cursor1.moveToNext())
                {
                    nametmp = cursor1.getString(cursor1.getColumnIndex("InSubCategory_Name"));
                }
            }
            String Mon = decimalFormat.format(cursor.getDouble(cursor.getColumnIndex("Bill_Money")))+"元";
            String ans = IO+nametmp+Mon;
            re.add(ans);
        }
        cursor.close();

        return re;
    }

    public List<Integer> getRecentIO() {   //通过测试

        String sql = "select * from Bill order by year || month || day || time DESC";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<Integer> re = new LinkedList<>();
        int cnt = 0;
        while (cursor.moveToNext() && cnt < 20) {
            if (cursor.getInt(cursor.getColumnIndex("Bill_TYPE")) == 2) continue;
            cnt++;
            Integer IO = cursor.getInt(cursor.getColumnIndex("Bill_TYPE"));
            re.add(IO);
        }
        cursor.close();

        return re;
    }

    public List<String> queryOutTopCategory() {  //通过测试

        String sql = "select OutTopCategory_Name from OutTopCategory";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<>();
        while (cursor.moveToNext()) {
            String tmp = cursor.getString(cursor.getColumnIndex("OutTopCategory_Name"));
            re.add(tmp);
        }
        cursor.close();

        return re;
    }

    public List<List<String>> queryOutSubCategory() {   //通过测试

        String sql = "select * from OutTopCategory";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<List<String>> re = new LinkedList<>();

        while (cursor.moveToNext()) {
            int tmp1 = cursor.getInt(cursor.getColumnIndex("OutTopCategory_ID"));
            String sql1 = "select OutSubCategory_Name from OutSubCategory where OutSubCategory_Parent = " + tmp1 + "";
            @SuppressLint("Recycle") Cursor cursor1 = db.rawQuery(sql1, null);
            List<String> tmp = new LinkedList<>();
            while (cursor1.moveToNext()) {
                String tmp3 = cursor1.getString(cursor1.getColumnIndex("OutSubCategory_Name"));
                tmp.add(tmp3);
            }
            re.add(tmp);
        }
        cursor.close();

        return re;
    }

    public List<String> queryInTopCategory() {  //通过测试

        String sql = "select InTopCategory_Name from InTopCategory";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<String> re = new LinkedList<>();
        while (cursor.moveToNext()) {
            String tmp = cursor.getString(cursor.getColumnIndex("InTopCategory_Name"));
            re.add(tmp);
        }
        cursor.close();

        return re;
    }

    public List<List<String>> queryInSubCategory() {   //通过测试
        String sql = "select * from InTopCategory";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<List<String>> re = new LinkedList<>();

        while (cursor.moveToNext()) {
            int tmp1 = cursor.getInt(cursor.getColumnIndex("InTopCategory_ID"));
            String sql1 = "select InSubCategory_Name from InSubCategory where InSubCategory_Parent = " + tmp1 + "";
            @SuppressLint("Recycle") Cursor cursor1 = db.rawQuery(sql1, null);
            List<String> tmp = new LinkedList<>();
            while (cursor1.moveToNext()) {
                String tmp3 = cursor1.getString(cursor1.getColumnIndex("InSubCategory_Name"));
                tmp.add(tmp3);
            }
            re.add(tmp);
        }
        cursor.close();
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
    public List<ChartDataEntry> getDataByOutTopCategory(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){//通过测试
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;

        Map<Integer, Integer> mp1 = new HashMap<>();
        String sql2 = "select * from OutSubCategory";
        @SuppressLint("Recycle") Cursor cursor2 = db.rawQuery(sql2, null);
        while(cursor2.moveToNext()) {
            mp1.put(cursor2.getInt(cursor2.getColumnIndex("OutSubCategory_ID")),cursor2.getInt(cursor2.getColumnIndex("OutSubCategory_Parent")));
        }
        cursor2.close();

        Map<Integer, String> mp = new HashMap<>();
        String sql1 = "select * from OutTopCategory";
        @SuppressLint("Recycle") Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("OutTopCategory_ID")),cursor1.getString(cursor1.getColumnIndex("OutTopCategory_Name")));
        }
        cursor1.close();

        String sql = "select Bill_ID, Bill_SubCategory, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND Bill_TYPE = 0 group by Bill_SubCategory";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<>();

        Map<String,Double> ans = new HashMap<>();
        while(cursor.moveToNext()){
            Integer tmp1 = cursor.getInt(cursor.getColumnIndex("Bill_SubCategory"));
            Integer tmp3 = mp1.get(tmp1);
            String tmp2 = mp.get(tmp3);
            double tmp4 = cursor.getDouble(cursor.getColumnIndex("nums"));
            if(ans.containsKey(tmp2)){
                double tmp5 = ans.get(tmp2);
                assert tmp2 != null;
                ans.put(tmp2, tmp4+tmp5);
            }
            else {
                assert tmp2 != null;
                ans.put(tmp2, tmp4);
            }
        }
        for (Map.Entry<String, Double> entry : ans.entrySet()) {
            ChartDataEntry tmp = new ChartDataEntry(entry.getKey(), entry.getValue());
            re.add(tmp);
        }
        cursor.close();

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
    public List<ChartDataEntry> getDataByOutSubCategory(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){  //通过测试
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;

        Map<Integer, String> mp = new HashMap<>();
        String sql1 = "select * from OutSubCategory";
        @SuppressLint("Recycle") Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("OutSubCategory_ID")),cursor1.getString(cursor1.getColumnIndex("OutSubCategory_Name")));
        }
        cursor1.close();

        String sql = "select Bill_ID, Bill_SubCategory, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND Bill_TYPE = 0 group by Bill_SubCategory";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<>();

        while(cursor.moveToNext()){
            Integer tmp1 = cursor.getInt(cursor.getColumnIndex("Bill_SubCategory"));
            ChartDataEntry tmp = new ChartDataEntry(mp.get(tmp1),cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        cursor.close();

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
    public List<ChartDataEntry> getDataByInTopCategory(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){//通过测试
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;

        Map<Integer, Integer> mp1 = new HashMap<>();
        String sql2 = "select * from InSubCategory";
        @SuppressLint("Recycle") Cursor cursor2 = db.rawQuery(sql2, null);
        while(cursor2.moveToNext()){
            mp1.put(cursor2.getInt(cursor2.getColumnIndex("InSubCategory_ID")),cursor2.getInt(cursor2.getColumnIndex("InSubCategory_Parent")));
        }
        cursor2.close();

        Map<Integer, String> mp = new HashMap<>();
        String sql1 = "select * from InTopCategory";
        @SuppressLint("Recycle") Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("InTopCategory_ID")),cursor1.getString(cursor1.getColumnIndex("InTopCategory_Name")));
        }
        cursor1.close();

        String sql = "select Bill_ID, Bill_SubCategory, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND Bill_TYPE = 1 group by Bill_SubCategory";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<>();

        Map<String,Double> ans = new HashMap<>();
        while(cursor.moveToNext()){
            Integer tmp1 = cursor.getInt(cursor.getColumnIndex("Bill_SubCategory"));
            Integer tmp3 = mp1.get(tmp1);
            String tmp2 = mp.get(tmp3);
            double tmp4 = cursor.getDouble(cursor.getColumnIndex("nums"));
            if(ans.containsKey(tmp2)){
                double tmp5 = ans.get(tmp2);
                assert tmp2 != null;
                ans.put(tmp2, tmp4+tmp5);
            }
            else {
                assert tmp2 != null;
                ans.put(tmp2, tmp4);
            }
        }
        for (Map.Entry<String, Double> entry : ans.entrySet()) {
            ChartDataEntry tmp = new ChartDataEntry(entry.getKey(), entry.getValue());
            re.add(tmp);
        }
        cursor.close();

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
    public List<ChartDataEntry> getDataByInSubCategory(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day){  //通过测试
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;

        Map<Integer, String> mp = new HashMap<>();
        String sql1 = "select * from InSubCategory";
        @SuppressLint("Recycle") Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("InSubCategory_ID")),cursor1.getString(cursor1.getColumnIndex("InSubCategory_Name")));
        }
        cursor1.close();

        String sql = "select Bill_ID, Bill_SubCategory, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND Bill_TYPE = 1 group by Bill_SubCategory";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<>();

        while(cursor.moveToNext()){
            Integer tmp1 = cursor.getInt(cursor.getColumnIndex("Bill_SubCategory"));
            ChartDataEntry tmp = new ChartDataEntry(mp.get(tmp1),cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        cursor.close();

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
    public List<ChartDataEntry> getDataByMember(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day, int type){
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;

        Map<Integer, String> mp = new HashMap<>();
        String sql1 = "select * from Member";
        @SuppressLint("Recycle") Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("Member_ID")),cursor1.getString(cursor1.getColumnIndex("Member_Name")));
        }
        cursor1.close();

        String sql = "select Bill_ID, Bill_Member, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND Bill_TYPE = "+type+" group by Bill_Member";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<>();

        while(cursor.moveToNext()){
            Integer tmp1 = cursor.getInt(cursor.getColumnIndex("Bill_Member"));
            ChartDataEntry tmp = new ChartDataEntry(mp.get(tmp1),cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        cursor.close();

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
    public List<ChartDataEntry> getDataByAccount(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day, int type){
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;

        Map<Integer, String> mp = new HashMap<>();
        String sql1 = "select * from Account";
        @SuppressLint("Recycle") Cursor cursor1 = db.rawQuery(sql1, null);
        while(cursor1.moveToNext()){
            mp.put(cursor1.getInt(cursor1.getColumnIndex("Account_ID")),cursor1.getString(cursor1.getColumnIndex("Account_Name")));
        }
        cursor1.close();

        String sql = "select Bill_ID, Bill_Account, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND Bill_TYPE = "+type+" group by Bill_Account";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<>();

        while(cursor.moveToNext()){
            Integer tmp1 = cursor.getInt(cursor.getColumnIndex("Bill_Account"));
            ChartDataEntry tmp = new ChartDataEntry(mp.get(tmp1),cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        cursor.close();

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
    public List<ChartDataEntry> getSumByDate(String start_year, String start_month, String start_day, String end_year, String end_month, String end_day, int type){ //按天求和
        String st = start_year+start_month+start_day;
        String ed = end_year+end_month+end_day;

        String sql = "select year || month || day as date, sum(Bill_Money) as nums from Bill where year || month || day >= '"+st+"' AND year || month || day <= '"+ed+"' AND Bill_TYPE = "+type+" group by date";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<ChartDataEntry> re = new LinkedList<>();

        while(cursor.moveToNext()){
            ChartDataEntry tmp = new ChartDataEntry(cursor.getString(cursor.getColumnIndex("date")), cursor.getDouble(cursor.getColumnIndex("nums")));
            re.add(tmp);
        }
        cursor.close();

        return re;
    }


    /**
     * 获取账户名和收入支出
     * @return List<AccountDataEntry>
     */
    public List<AccountDataEntry> getBalanceByAccount() {
        String sql = "select * from Account where Account_ID >= 1";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        List<AccountDataEntry> re = new LinkedList<>();
        while (cursor.moveToNext()) {
            AccountDataEntry tmp = new AccountDataEntry(cursor.getString(cursor.getColumnIndex("Account_Name")), cursor.getDouble(cursor.getColumnIndex("Account_InMoney")), cursor.getDouble(cursor.getColumnIndex("Account_OutMoney")));
            re.add(tmp);
        }
        cursor.close();
        return re;
    }


    /**
     * 获取流水账户
     * @param Year  需要流水的年份
     * @param member  筛选的成员
     * @param account  筛选的账户
     * @return BillDataYear
     */
    @SuppressLint("Recycle")
    public BillDataYear getJournalAccount(String Year, String member, String account){
        double YearIncome = 0, YearOutcome = 0;
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
        cursor1.close();

        for(int i = 12; i >0 ; i--){
            boolean monthexist = false;
            String itmp = String.valueOf(i);
            if(i < 10){
                String tmp = String.valueOf(i);
                itmp = "0"+tmp;
            }
            double monthincome = 0.0, monthoutcome = 0.0;
            List<BillDataDay> billDataDayList = new LinkedList<>();
            for(int j = 31; j > 0; j--)
            {
                String jtmp = String.valueOf(j);
                if(j < 10){
                    String tmp = String.valueOf(j);
                    jtmp = "0"+tmp;
                }
                List<BillDataItem> billDataItemList = new LinkedList<>();
                String sql;
                if(member.equals(getResources().getString(R.string.unlimited)) && account.equals(getResources().getString(R.string.unlimited))) sql = "select * from Bill where year = '"+Year+"' AND month = '"+itmp+"' AND day = '"+jtmp+"'";
                else if(member.equals(getResources().getString(R.string.unlimited))) sql = "select * from Bill where year = '"+Year+"' AND month = '"+itmp+"' AND day = '"+jtmp+"' AND (Bill_Account = "+account1+" or Bill_toAccount = "+account1+")";
                else if(account.equals(getResources().getString(R.string.unlimited))) sql = "select * from Bill where year = '"+Year+"' AND month = '"+itmp+"' AND day = '"+jtmp+"' AND Bill_Member = "+member1+"";
                else sql = "select * from Bill where year = '"+Year+"' AND month = '"+itmp+"' AND day = '"+jtmp+"' AND (Bill_Account = "+account1+" or Bill_toAccount = "+account1+") AND Bill_Member = "+member1+"";
                Cursor cursor = db.rawQuery(sql, null);
                double income = 0.0, outcome = 0.0;
                boolean dayexist = false;
                while(cursor.moveToNext())
                {
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
                        cursor2.close();
                    }
                    else{
                        sql3 = "select * from InSubCategory where InSubCategory_ID = "+category1+"";
                        Cursor cursor2 = db.rawQuery(sql3, null);
                        while (cursor2.moveToNext()){
                            category = cursor2.getString(cursor2.getColumnIndex("InSubCategory_Name"));
                        }
                        cursor2.close();
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
                    cursor2.close();

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
                cursor.close();
                if(!dayexist) {
                    continue;
                }
                monthincome += income;
                monthoutcome += outcome;
                BillDataDay tmp = new BillDataDay(Year, itmp, jtmp, income, outcome, billDataItemList);
                billDataDayList.add(tmp);

            }
            if(!monthexist) continue;
            BillDataMonth tmp = new BillDataMonth(Year, itmp, monthincome, monthoutcome, billDataDayList);
            billDataMonthList.add(tmp);
            YearIncome += monthincome;
            YearOutcome += monthoutcome;
        }
        BillDataYear re = new BillDataYear(Year, YearIncome, YearOutcome, billDataMonthList);
        return re;
    }


    /**
     * 修改账户名称
     * @param preAccountName 原账户名
     * @param newAccountName 新账户名
     * @return boolen
     */
    public boolean ChangeAccountName(String preAccountName, String newAccountName){
        boolean isPreNameLegal = false;
        String sql = "select * from Account where Account_Name = '"+ preAccountName +"'";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            isPreNameLegal = true;
        }
        boolean isNewNameLegal = true;
        if(isPreNameLegal){
            sql = "select * from Account where Account_Name = '"+ newAccountName +"'";
            cursor = db.rawQuery(sql, null);
            while(cursor.moveToNext()){
                isNewNameLegal = false;
            }
        }
        if(isPreNameLegal && isNewNameLegal){
            sql = "update Account set Account_Name = '"+ newAccountName +"' where Account_Name = '"+ preAccountName +"'";
            db.execSQL(sql);
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }



    public void Destory(){
        String sql = "drop TABLE Bill";
        db.execSQL(sql);
        sql = "drop TABLE OutTopCategory";
        db.execSQL(sql);
        sql = "drop TABLE OutSubCategory";
        db.execSQL(sql);
        sql = "drop TABLE InTopCategory";
        db.execSQL(sql);
        sql = "drop TABLE InSubCategory";
        db.execSQL(sql);
        sql = "drop TABLE Account";
        db.execSQL(sql);
        sql = "drop TABLE Member";
        db.execSQL(sql);
    }


    public void closeDataBase() {
        db.close();
    }

}