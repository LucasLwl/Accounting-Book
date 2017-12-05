package com.phone.konka.accountingbook.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by 廖伟龙 on 2017/12/5.
 */

public class BitmapLrucache {


    private Context mContext;

    //    private LruCache<Integer, Bitmap> mCache;
    private LruCache<String, Bitmap> mCache;

    private int maxMemory;

    private static BitmapLrucache mInstance;


    private BitmapLrucache(Context context) {

        mContext = context;
        maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        mCache = new LruCache<String, Bitmap>(maxMemory / 8) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public static BitmapLrucache getInstance(Context context) {

        if (mInstance == null) {
            synchronized (BitmapLrucache.class) {
                if (mInstance == null)
                    mInstance = new BitmapLrucache(context);
            }
        }
        return mInstance;
    }

    public Bitmap getBitmap(int id, String text) {
        Bitmap bitmap = null;
        bitmap = mCache.get(text);


        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), id);
            mCache.put(text, bitmap);
            Log.i("dde", "size: " + mCache.putCount());

        }
        return bitmap;
    }

    public void displayAllData() {
        Map<String, Bitmap> map = mCache.snapshot();
        Iterator<Map.Entry<String, Bitmap>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Bitmap> entry = it.next();
            Log.i("ddd", "key= " + entry.getKey() + " and value= " + entry.getValue());
        }
    }
}
