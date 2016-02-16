package com.alex.googlenewsreader.file_manager;

import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by alex on 16/02/2016.
 */
public class FileManager {

    Context context;

    public FileManager(Context c) {
        context = c;
    }

    public void Write(String tag){
        try {
            FileOutputStream fos = context.openFileOutput("TAG", Context.MODE_APPEND);
            if(isEmpty()) {
                tag = tag+" ";
                fos.write(tag.getBytes());
            }else{
                tag = ","+tag+" ";
                fos.write(tag.getBytes());
            }
            fos.flush();
            fos.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> Read(){
        ArrayList<String> list = new ArrayList<>();
        String s = "";
        String[] l;
        try{
            FileInputStream fis = context.openFileInput("TAG");

            byte[] bytes = new byte[256];
            int read = -1;

            while((read = fis.read(bytes))>0){
                s+=new String(bytes,0,read-1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        l = s.split(",");
        for (String st: l) {
            list.add(st);
        }
        return list;
    }

    public boolean isEmpty(){
        String s = "";
        try{
            FileInputStream fis = context.openFileInput("TAG");

            byte[] bytes = new byte[256];
            int read = -1;

            while((read = fis.read(bytes))>0){
                s+=new String(bytes,0,read-1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(s.isEmpty() || s.equals("")){
            return true;
        }
        return false;
    }
}
