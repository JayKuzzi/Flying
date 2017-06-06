package com.bb.offerapp.util;

import android.content.ContentProvider;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by bb on 2017/5/19.
 */

public class ReadContactMsg {
    private String name;
    private String phone;
    public ReadContactMsg(Context context, Intent data){
        super();
        Uri contactData = data.getData();
        CursorLoader cursorLoader = new CursorLoader(context,contactData,null,null,null,null);
        Cursor cursor = cursorLoader.loadInBackground();
        if(cursor.moveToFirst()){
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            phone = "此联系人暂未存入号码";
            Cursor phones = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null,
                    null);
            if (phones.moveToFirst()) {
                phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phones.close();
        }
        cursor.close();
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }


}