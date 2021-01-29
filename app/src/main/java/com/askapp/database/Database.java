package com.askapp.database;

import android.content.Context;

import com.SimpleSqlite.InsertData;
import com.SimpleSqlite.SimplifiedDatabase;
import com.SimpleSqlite.modelClasses.TableRowModel;
import com.SimpleSqlite.modelClasses.Types;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    Context context;
    private static String DATABASE_NAME = "USER_DATABASE";

    private static String USER_INFO_TABLE = "USER_INFO_TABLE";
    private static String NAME_ROW = "NAME";
    private static String PHONE_ROW = "PHONE";
    private static String STATUS_ROW = "STATUS";

    private static String CONTACTS_TABLE = "CONTACTS_TABLE";

    SimplifiedDatabase database;
    public Database(Context context) {
        this.context = context;
        database = new SimplifiedDatabase(context,DATABASE_NAME);
    }
    public void createUserDetailsTable(){
        ArrayList<TableRowModel> tableRowModels = new ArrayList<>();
        tableRowModels.add(new TableRowModel(NAME_ROW, new Types().getString()));
        tableRowModels.add(new TableRowModel(PHONE_ROW, new Types().getString()));
        tableRowModels.add(new TableRowModel(STATUS_ROW, new Types().getString()));
        try {
            database.createTable(USER_INFO_TABLE,tableRowModels);
        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayList<TableRowModel> tableRowModelsa = new ArrayList<>();
        tableRowModelsa.add(new TableRowModel(NAME_ROW, new Types().getString()));
        tableRowModelsa.add(new TableRowModel(PHONE_ROW, new Types().getString()));
        try {
            database.createTable(CONTACTS_TABLE,tableRowModelsa);
        }catch (Exception e){
            e.printStackTrace();
        }



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

    public void setStatus(String phone){
        ArrayList<HashMap> colms = database.queryAllItemsInTable(USER_INFO_TABLE);
        if (colms.size()!=0){
            database.updateColumnWithId(USER_INFO_TABLE,"1",STATUS_ROW,phone);
        }else{
            ArrayList<InsertData> insertData = new ArrayList<>();
            insertData.add(new InsertData(STATUS_ROW,phone,0));
            database.insertData(USER_INFO_TABLE,insertData);
        }
    }

    public String getPhone(){
        try {


            ArrayList<HashMap> maps = database.queryAllItemsInTable(USER_INFO_TABLE);
            if (!maps.isEmpty()) {
                return (database.getColumnWithId("1", USER_INFO_TABLE)).get(PHONE_ROW).toString();
            } else {
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    public String getName(){
        try {


            ArrayList<HashMap> maps = database.queryAllItemsInTable(USER_INFO_TABLE);
            if (!maps.isEmpty()) {
                return (database.getColumnWithId("1", USER_INFO_TABLE)).get(NAME_ROW).toString();
            } else {
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }
    public String getStatus(){
        try {

            ArrayList<HashMap> maps = database.queryAllItemsInTable(USER_INFO_TABLE);
            if (!maps.isEmpty()){
                return (database.getColumnWithId("1",USER_INFO_TABLE)).get(STATUS_ROW).toString();
            }else{
                return null;
            }
        }catch (Exception e) {
            return null;
        }


    }

    public Boolean addContact(String name,String phone){

        ArrayList<HashMap> maps = database.queryAllItemsInTable(CONTACTS_TABLE);
        Boolean alreadyAdded = false;
        if (!maps.isEmpty()){
            for (int i = 0; i < maps.size(); i++) {
                if (maps.get(i).get(PHONE_ROW).toString().equals(phone)){
                    alreadyAdded = true;
                }
            }
        }

        if (!alreadyAdded) {
            ArrayList<InsertData> insertData = new ArrayList<>();
            insertData.add(new InsertData(NAME_ROW, name, 0));
            insertData.add(new InsertData(PHONE_ROW, phone, 0));
            database.insertData(CONTACTS_TABLE, insertData);
        }
        return true;
    }

    public void removeContact(String phone){
        ArrayList<HashMap> contacts = getAllContacts();
        int position = -1;
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).get(PHONE_ROW).toString().equals(phone)){
                position = i;
                break;
            }
        }
        if (position!=-1){
            contacts.remove(position);
        }

        database.clearTable(CONTACTS_TABLE);

        for (int i = 0; i < contacts.size(); i++) {
            addContact(contacts.get(i).get(NAME_ROW).toString(),contacts.get(i).get(PHONE_ROW).toString());
        }


    }

    public ArrayList<HashMap> getAllContacts(){
        return database.queryAllItemsInTable(CONTACTS_TABLE);
    }







}
