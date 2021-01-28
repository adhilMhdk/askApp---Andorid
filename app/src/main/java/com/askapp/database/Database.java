package com.askapp.database;

import android.app.Activity;

import com.SimpleSqlite.InsertData;
import com.SimpleSqlite.SimplifiedDatabase;
import com.SimpleSqlite.modelClasses.TableRowModel;
import com.SimpleSqlite.modelClasses.Types;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    Activity activity;
    private static String DATABASE_NAME = "USER_DATABASE";
    private static String USER_INFO_TABLE = "USER_INFO_TABLE";
    private static String NAME_ROW = "NAME";
    private static String PHONE_ROW = "PHONE";

    SimplifiedDatabase database;
    public Database(Activity activity) {
        this.activity = activity;
        database = new SimplifiedDatabase(activity,DATABASE_NAME);
        ArrayList<TableRowModel> tableRowModels = new ArrayList<>();
        tableRowModels.add(new TableRowModel(NAME_ROW, new Types().getString()));
        tableRowModels.add(new TableRowModel(PHONE_ROW, new Types().getString()));
        database.createTable(USER_INFO_TABLE,tableRowModels);
    }
    public void setName(String name){
        ArrayList<HashMap> colms = database.queryAllItemsInTable(USER_INFO_TABLE);
        if (colms.size()!=0){
            database.updateColumnWithId(USER_INFO_TABLE,"1",NAME_ROW,name);
        }else{
            ArrayList<InsertData> insertData = new ArrayList<>();
            insertData.add(new InsertData(NAME_ROW,name,0));
            database.insertData(USER_INFO_TABLE,insertData);
        }
    }

    public void setPhone(String phone){
        ArrayList<HashMap> colms = database.queryAllItemsInTable(USER_INFO_TABLE);
        if (colms.size()!=0){
            database.updateColumnWithId(USER_INFO_TABLE,"1",PHONE_ROW,phone);
        }else{
            ArrayList<InsertData> insertData = new ArrayList<>();
            insertData.add(new InsertData(PHONE_ROW,phone,0));
            database.insertData(USER_INFO_TABLE,insertData);
        }
    }
    public String getPhone(){
        return (database.getColumnWithId("1",USER_INFO_TABLE)).get(PHONE_ROW).toString();
    }

    public String getName(){
        return (database.getColumnWithId("1",USER_INFO_TABLE)).get(NAME_ROW).toString();
    }







}
