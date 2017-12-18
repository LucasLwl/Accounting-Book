package com.phone.konka.accountingbook.Utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * ContentProvider,用于本进程或者其他进程操作数据库数据
 * <p>
 * Created by 廖伟龙 on 2017/12/11.
 */

public class AccountProvider extends ContentProvider {

    private Context mContext;


    /**
     * 数据库表名
     */
    private final String mTableName = "account";


    /**
     * 数据库管理类
     */
    private DBManager mDBManager;


    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDBManager = DBManager.getInstance(mContext);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return mDBManager.getReadableDatabase().query(mTableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        mDBManager.getWritableDatabase().insert(mTableName, null, values);
        mContext.getContentResolver().notifyChange(uri, null);
        mDBManager.closeDatabase();
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = mDBManager.getWritableDatabase().delete(mTableName, selection, selectionArgs);
        if (count > 0)
            mContext.getContentResolver().notifyChange(uri, null);
        mDBManager.getReadableDatabase();
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = mDBManager.getWritableDatabase().update(mTableName, values, selection, selectionArgs);
        if (count > 0)
            mContext.getContentResolver().notifyChange(uri, null);
        mDBManager.closeDatabase();
        return count;
    }
}
