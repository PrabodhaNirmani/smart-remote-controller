package com.example.acer.smartremote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by acer on 7/1/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="smart_remote_database";
    private static final String TABLE_NAME_1="device_table";
    private static final String COL_1_1="id";
    private static final String COL_1_2="device_name";
    private static final String COL_1_3="power";
    private static final String COL_1_4="increase";
    private static final String COL_1_5="decrease";
    private static final String COL_1_6="forward";
    private static final String COL_1_7="backward";


    private static final String TAG="Database helper";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
      //  SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_1="CREATE TABLE "+TABLE_NAME_1+" ("+COL_1_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                        +COL_1_2+" TEXT, "
                                                        +COL_1_3+" TEXT, "
                                                        +COL_1_4+" TEXT, "
                                                        +COL_1_5+" TEXT, "
                                                        +COL_1_6+" TEXT, "
                                                        +COL_1_7+" TEXT)";
        db.execSQL(sql_1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS "+TABLE_NAME_1);
        onCreate(db);
    }

    public boolean insertDevice(String name,String power,String increase, String decrease, String backward, String forward){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(COL_1_2,name);
        values.put(COL_1_3,power);
        values.put(COL_1_4,increase);
        values.put(COL_1_5,decrease);
        values.put(COL_1_6,forward);
        values.put(COL_1_7,backward);
        long result=db.insert(TABLE_NAME_1,null,values);
        if(result==-1)
            return false;
        else
            return true;
    }

    public Cursor getDevice(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+TABLE_NAME_1+" WHERE "+COL_1_1+"="+id+"",null);
        return result;
    }

    public int getDeviceId(){

        String countQuery = "SELECT  * FROM " + TABLE_NAME_1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }

//    public void deleteDevice(String device){
//        String[] res=device.split(" ");
//        String id=res[0];
//        SQLiteDatabase db=this.getWritableDatabase();
//        int res1=db.delete(TABLE_NAME_1,"id=?",new String[]{id});
//
//    }

    public Cursor getAllDevices(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+TABLE_NAME_1,null);
        return result;
    }

    public boolean updateDeviceName(String id,String name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COL_1_1,id);
        values.put(COL_1_2,name);
        long result=db.update(TABLE_NAME_1,values,"id = ?",new String[]{ id });
        if(result==-1)
            return false;
        else
            return true;

    }


    public String getCommand(String id, String type){
        SQLiteDatabase db=this.getWritableDatabase();

        Cursor result=null;
        try {
//            Cursor c = db.rawQuery("SELECT * FROM tbl1 WHERE TRIM(name) = '"+name.trim()+"'", null)
            String query="SELECT * FROM "+TABLE_NAME_1+" WHERE "+COL_1_1+"="+id+"";
//            String query="SELECT * FROM "+TABLE_NAME_2.trim()+" WHERE "+COL_2_0.trim()+"= '"+id+"' AND "+COL_2_1.trim()+"= '"+type+"'";
            Log.d(TAG,query);

            result=db.rawQuery(query,null);
            switch(type) {
                case "power":
                    return result.getString(2).toString();
                case "increase":
                    return result.getString(3).toString();
                case "decrease":
                    return result.getString(4).toString();
                case "forward":
                    return result.getString(5).toString();
                case "backword":
                    return result.getString(6).toString();
                default:
                    return null;
            }
        }catch (Exception e){
            Log.d(TAG,"Error occured during transaction");
            return null;
        }

    }

//    public boolean updateCommand(String id,String name,String power,String increase, String decrease, String backward, String forward){
//        SQLiteDatabase db=this.getWritableDatabase();
//        ContentValues values=new ContentValues();
//        values.put(COL_1_1,id);
//        values.put(COL_1_2,name);
//        values.put(COL_1_3,power);
//        values.put(COL_1_4,increase);
//        values.put(COL_1_5,decrease);
//        values.put(COL_1_6,forward);
//        values.put(COL_1_7,backward);
//        long result=db.update(TABLE_NAME_1,values,"dev_id = ? and type = ?",new String[]{ id, name });
//        if(result==-1)
//            return false;
//        else
//            return true;
//
//    }

//    public boolean confirmRegistration(String id){
//        SQLiteDatabase db=this.getWritableDatabase();
//
//        Cursor result=null;
//        try {
//            String query="SELECT * FROM "+TABLE_NAME_2+" WHERE "+COL_2_0+"="+id+"";
//            Log.d(TAG,query);
//            result=db.rawQuery(query,null);
////            for(result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
////
////                String line=result.getString(1)+" "+result.getString(3)+" "+result.getString(2);
////
////                Log.d(TAG,line);
////
////            }
//        }catch (Exception e){
//            Log.d(TAG,"Error occured during transaction");
////            Toast.makeText(InstantiateRemoteActivity.class,"Error occured durin transaction try again",Toast.LENGTH_LONG).show();
//        }
//        finally {
//            Log.d(TAG,String.valueOf(result.getCount()));
//            return result.getCount()== 5;
//        }
//
//    }
}
