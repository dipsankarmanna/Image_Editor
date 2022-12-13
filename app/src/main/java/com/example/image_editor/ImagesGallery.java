package com.example.image_editor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

public class ImagesGallery {
    public static ArrayList<String> imagelist(Context context){
        Uri uri;
        Cursor cursor;
        int coloum_index_data, column_index_folder_name;
        ArrayList<String> listofimages=new ArrayList<>();
        String absolutepath;

        uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection={MediaStore.MediaColumns.DATA,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        String orderby=MediaStore.Video.Media.DATE_TAKEN;
        cursor=context.getContentResolver().query(uri,projection,null,null,orderby+" DESC");
        coloum_index_data=cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        //column_index_folder_name=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()){
            absolutepath=cursor.getString(coloum_index_data);

            listofimages.add(absolutepath);
        }

        return listofimages;
    }
}
