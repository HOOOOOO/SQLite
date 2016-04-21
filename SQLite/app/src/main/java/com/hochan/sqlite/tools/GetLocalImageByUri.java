package com.hochan.sqlite.tools;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.LinearLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/4/19.
 */
public class GetLocalImageByUri extends AsyncTask<Uri, Void, Bitmap> {

    private static final String TAG = "GetLocalImageByUri: ";
    private Context mContext;

    public GetLocalImageByUri(Context context){
        this.mContext = context;
    }

    @Override
    protected Bitmap doInBackground(Uri... params) {
        return bitmapFactory(params[0]);
    }

    private Bitmap bitmapFactory(Uri uri){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream inputStream = null;
        try {
            inputStream = mContext.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(inputStream, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(TAG + "options.outWidth：" + options.outWidth);
        ScreenTools screenTools = ScreenTools.instance(mContext);
        int widthOfDialogWindow = MyApplication.mWidthOfScreen - screenTools.dip2px(15);
        System.out.println(TAG+"widthOfDialogWindow："+widthOfDialogWindow);
        options.inSampleSize = (int) (options.outWidth/widthOfDialogWindow*1.0);
        options.inJustDecodeBounds = false;
        Bitmap bm = null;
        Bitmap result = null;
        try {
            bm = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri), null, options);
            Matrix matrix = new Matrix();
            matrix.setScale((float) (widthOfDialogWindow * 1.0 / bm.getWidth()), (float) (widthOfDialogWindow * 1.0 / bm.getWidth()));
            result = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            System.out.println("result.getWidth(): "+result.getWidth());
            if(result != bm){
                bm.recycle();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
