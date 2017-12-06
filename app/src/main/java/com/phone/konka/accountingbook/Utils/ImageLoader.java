package com.phone.konka.accountingbook.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

/**
 * Created by 廖伟龙 on 2017/12/5.
 */

public class ImageLoader {


    private Context mContext;

    private LruCache<Integer, Bitmap> mCache;

    private int maxMemory;

    private static ImageLoader mInstance;


    private ImageLoader(Context context) {

        mContext = context;
        maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        mCache = new LruCache<Integer, Bitmap>(maxMemory / 4) {
            @Override
            protected int sizeOf(Integer key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public static ImageLoader getInstance(Context context) {

        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null)
                    mInstance = new ImageLoader(context);
            }
        }
        return mInstance;
    }

    public Bitmap getBitmap(int id) {
        Bitmap bitmap = null;
        bitmap = mCache.get(id);


        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), id);
            mCache.put(id, bitmap);
        }
        return bitmap;
    }

}
