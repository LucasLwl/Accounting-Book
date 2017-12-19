package com.phone.konka.accountingbook.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * Created by 廖伟龙 on 2017/12/5.
 */

public class ImageLoader {


    private Context mContext;

    private LruCache<Integer, Bitmap> mCache;

    private int maxMemory;

    private static ImageLoader mInstance;


    private ThreadPoolManager mThreadPool;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ImageView imgView = (ImageView) msg.obj;
            Bundle bundle = msg.getData();
            Bitmap bitmap = bundle.getParcelable("bitmap");
            if ((int) imgView.getTag() == msg.arg1)
                imgView.setImageBitmap(bitmap);
        }
    };


    private ImageLoader(Context context) {

        mContext = context;
        maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        mCache = new LruCache<Integer, Bitmap>(maxMemory / 4) {
            @Override
            protected int sizeOf(Integer key, Bitmap value) {

                int size = value.getRowBytes() * value.getHeight() / 1024;
                return size;
            }
        };

        mThreadPool = ThreadPoolManager.getInstance();
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

    public Bitmap getBitmap(int id, int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        bitmap = mCache.get(id);


        if (bitmap == null) {
            bitmap = decodeSamplesBitmap(id, reqWidth, reqHeight);
//            bitmap = BitmapFactory.decodeResource(mContext.getResources(), id);
            if (bitmap != null) {
                mCache.put(id, bitmap);
            }
        }
        return bitmap;
    }


    public void getBitmap(final int id, final ImageView imgView) {
        final Bitmap[] bitmap = {null};
        bitmap[0] = mCache.get(id);


        if (bitmap[0] == null) {
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    imgView.setTag(id);
                    bitmap[0] = decodeSamplesBitmap(id, imgView.getWidth(), imgView.getHeight());
                    if (bitmap[0] != null) {
                        mCache.put(id, bitmap[0]);
                    }
                    Message msg = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("bitmap", bitmap[0]);
                    msg.setData(bundle);
                    msg.obj = imgView;
                    msg.arg1 = id;
                    msg.sendToTarget();
                }
            });

        } else {
            imgView.setImageBitmap(bitmap[0]);
        }
    }

    public Bitmap decodeSamplesBitmap(int resId, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(mContext.getResources(), resId);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(mContext.getResources(), resId, options);

    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        int height = options.outHeight;
        int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth)
                inSampleSize *= 2;
        }
        return inSampleSize;
    }

}
