package com.phone.konka.accountingbook.Utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by 廖伟龙 on 2017/12/11.
 */

public class AccountProvider extends ContentProvider {

    private Context mContext;

    private DBOperator mDBOperator;

    private final String mTableName = "account";

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDBOperator = new DBOperator(mContext);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return mDBOperator.query(mTableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        mDBOperator.insert(mTableName, null, values);
        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = mDBOperator.delete(mTableName, selection, selectionArgs);
        if (count > 0)
            mContext.getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = mDBOperator.update(mTableName, values, selection, selectionArgs);
        if (count > 0)
            mContext.getContentResolver().notifyChange(uri, null);
        return count;
    }
}
