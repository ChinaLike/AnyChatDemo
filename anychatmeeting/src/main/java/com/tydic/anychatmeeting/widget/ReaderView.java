package com.tydic.anychatmeeting.widget;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

/**
 * 作者：like on 2018/6/13 10:10
 * <p>
 * 邮箱：like@tydic.com
 * <p>
 * 描述：文档读取
 */
public class ReaderView extends RelativeLayout implements TbsReaderView.ReaderCallback{


    private Context context;

    private TbsReaderView mTbsReaderView;

    private DownloadManager mDownloadManager;

    private DownloadObserver mDownloadObserver;
    /**
     * 文件名称
     */
    private String mFileName;

    private long mRequestId;

    public ReaderView(Context context) {
        super(context);
        init(context);
    }

    public ReaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ReaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        mTbsReaderView = new TbsReaderView(context,this);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mTbsReaderView,layoutParams);

    }

    /**
     * 打开文档
     * @param url  文档的下载链接，如果本地存在自动判断
     */
    public void openDocument(String url){
        mFileName = parseName(url);
        if (isLocalExist()){
            //本地存在直接打开
            displayFile();
        }else {
            //本地不存在，先下载文件
            startDownload(url);
        }
    }

    /**
     * 向阅读器传递数据，打开文件
     */
    private void displayFile() {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", getLocalFile().getPath());
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        boolean result = mTbsReaderView.preOpen(parseFormat(mFileName), false);
        if (result) {
            mTbsReaderView.openFile(bundle);
        }
    }

    /**
     * 根据文件名称获取文件名不带后缀
     * @param fileName
     * @return
     */
    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件名称带后缀
     * @param url
     * @return
     */
    private String parseName(String url) {
        String fileName = null;
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = String.valueOf(System.currentTimeMillis());
            }
        }
        return fileName;
    }

    /**
     * 判断文件在本地是否存在
     * @return
     */
    private boolean isLocalExist() {
        return getLocalFile().exists();
    }

    /**
     * 从下载文件中获取文件
     * @return
     */
    private File getLocalFile() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mFileName);
    }

    /**
     * 下载文件
     */
    private void startDownload(String mFileUrl) {
        mDownloadObserver = new DownloadObserver(new Handler());
        context.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), true, mDownloadObserver);

        mDownloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mFileUrl));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mFileName);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        mRequestId = mDownloadManager.enqueue(request);
    }


    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    /**
     * 当Activity销毁时调用
     */
    public void onDestroy(){
        mTbsReaderView.onStop();
        if (mDownloadObserver != null) {
            context.getContentResolver().unregisterContentObserver(mDownloadObserver);
        }
    }

    private void queryDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(mRequestId);
        Cursor cursor = null;
        try {
            cursor = mDownloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载的字节数
                int currentBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //总需下载的字节数
                int totalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //状态所在的列索引
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                Log.i("downloadUpdate: ", currentBytes + " " + totalBytes + " " + status);
             //   mDownloadBtn.setText("正在下载：" + currentBytes + "/" + totalBytes);
                if (DownloadManager.STATUS_SUCCESSFUL == status ) {
//                    mDownloadBtn.setVisibility(View.GONE);
//                    mDownloadBtn.performClick();
                    displayFile();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    private class DownloadObserver extends ContentObserver {

        private DownloadObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Log.i("downloadUpdate: ", "onChange(boolean selfChange, Uri uri)");
            queryDownloadStatus();
        }
    }
}
