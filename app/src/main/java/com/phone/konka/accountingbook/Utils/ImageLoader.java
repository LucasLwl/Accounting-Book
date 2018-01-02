package com.phone.konka.accountingbook.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * 图片缓存加载器
 * <p>
 * Created by 廖伟龙 on 2017/12/5.
 */

public class ImageLoader {


    /**
     * 上下文对象
     */
    private Context mContext;


    /**
     * 缓存器
     */
    private LruCache<Integer, Bitmap> mCache;


    /**
     * 分配的最大内存
     */
    private int maxMemory;


    /**
     * 当前类的私有对象
     */
    private static ImageLoader mInstance;


    /**
     * 线程池
     */
    private ThreadPoolManager mThreadPool;


    /**
     * 用于异步加载图片后，转到UI线程显示图片
     */
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


    /**
     * 私有构造方法
     *
     * @param context
     */
    private ImageLoader(Context context) {
        mContext = context;

//        获取分配的最大内存
        maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

//        初始化存储器，以kb单位
        mCache = new LruCache<Integer, Bitmap>(maxMemory / 16) {
            @Override
            protected int sizeOf(Integer key, Bitmap value) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Log.i("ddd", "value: " + value.getAllocationByteCount() / 1024);
                    return value.getAllocationByteCount() / 1024;

                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                    Log.i("ddd", "value: " + value.getByteCount() / 1024);
                    return value.getByteCount() / 1024;
                }

                Log.i("ddd", "value: " + value.getRowBytes() * value.getHeight() / 1024);
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };

//        初始化线程池
        mThreadPool = ThreadPoolManager.getInstance();
    }


    /**
     * 获取本类的对象
     *
     * @param context
     * @return
     */
    public static ImageLoader getInstance(Context context) {

        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null)
                    mInstance = new ImageLoader(context);
            }
        }
        return mInstance;
    }


    /**
     * 异步加载Resource中的图片
     *
     * @param id
     * @param imgView
     */
    public void getBitmap(final int id, final ImageView imgView) {
        imgView.setTag(id);

        Bitmap bitmap = mCache.get(id);

        if (bitmap != null) {
            imgView.setImageBitmap(bitmap);
            return;
        }


        /**
         * 当缓存器中没有该图片时，使用线程池异步加载Resource中的图片
         */

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = decodeSamplesBitmap(id, imgView.getWidth(), imgView.getHeight());
                if (bitmap != null) {
                    mCache.put(id, bitmap);
                }

//              加载完，通过发送Message来转到Handler处理
                Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putParcelable("bitmap", bitmap);
                msg.setData(bundle);
                msg.obj = imgView;
                msg.arg1 = id;
                msg.sendToTarget();
            }
        });
    }


    /**
     * 按照需求大小加载图片
     *
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSamplesBitmap(int resId, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(mContext.getResources(), resId);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(mContext.getResources(), resId, options);

    }


    /**
     * 按照需求大小计算图片的inSampleSize
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
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
