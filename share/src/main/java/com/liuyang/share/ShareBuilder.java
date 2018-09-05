package com.liuyang.share;

import android.content.Context;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.liuyang.share.down.DefaultImageDownLoader;
import com.liuyang.share.down.ImageDownLoader;
import com.liuyang.share.exception.LackConfigException;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 分享配置
 * Created by liuyang-ds on 2016/11/28.
 */
public class ShareBuilder implements Parcelable {

    private int defaultShareImage;
    private String qqAppId;
    private String qqScope;
    private String wxAppId;
    private String sinaAppKey;
    private String sinaRedirectUrl;
    private String sinaScope;

    private String imageCachePath;
    private ImageDownLoader imageDownloader;
    private Executor taskExecutor;

    private ShareBuilder(Builder builder) {
        if(builder.defaultShareImage==-1){
            throw new NullPointerException("builder is null,please check your qqAppId,wxAppId,sinaAppKey,sinaRedirectUrl,sinaScope,defaultShareImage") ;
        }
        defaultShareImage = builder.defaultShareImage;
        qqAppId = builder.qqAppId;
        qqScope = builder.qqScope;
        wxAppId = builder.wxAppId;
        sinaAppKey = builder.sinaAppKey;
        sinaRedirectUrl = builder.sinaRedirectUrl;
        sinaScope = builder.sinaScope;
        imageCachePath = builder.imageCachePath;
    }

    public String getImageCachePath() {
        return imageCachePath;
    }

    public int getDefaultShareImage() {
        return defaultShareImage;
    }

    public String getQqAppId() {
        return qqAppId;
    }

    public String getQqScope() {
        return qqScope;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public String getSinaAppKey() {
        return sinaAppKey;
    }

    public String getSinaRedirectUrl() {
        return sinaRedirectUrl;
    }

    public String getSinaScope() {
        return sinaScope;
    }

    public ImageDownLoader getImageDownloader() {
        if (imageDownloader == null) {
            imageDownloader = new DefaultImageDownLoader();
        }
        return imageDownloader;
    }

    public Executor getTaskExecutor() {
        if (taskExecutor == null) {
            taskExecutor = Executors.newCachedThreadPool();
        }
        return taskExecutor;
    }

    public static class Builder {
        public static final String IMAGE_CACHE_FILE_NAME = "shareImage";
        private Context mContext;

        private int defaultShareImage = -1;
        private String qqAppId;
        private String qqScope;
        private String wxAppId;
        private String sinaAppKey;
        private String sinaRedirectUrl;
        private String sinaScope;

        private String imageCachePath;

        public Builder(Context context) {
            mContext = context.getApplicationContext();
        }

        public Builder setDefaultShareImage(int defaultShareImage) {
            this.defaultShareImage = defaultShareImage;
            return this;
        }

        public Builder setQqAppId(String qqAppId) {
            this.qqAppId = qqAppId;
            return this;
        }

        public Builder setQqScope(String qqScope) {
            this.qqScope = qqScope;
            return this;
        }

        public Builder setWxAppId(String wxAppId) {
            this.wxAppId = wxAppId;
            return this;
        }

        public Builder setSinaAppKey(String sinaAppKey) {
            this.sinaAppKey = sinaAppKey;
            return this;
        }

        public Builder setSinaRedirectUrl(String sinaRedirectUrl) {
            this.sinaRedirectUrl = sinaRedirectUrl;
            return this;
        }

        public Builder setSinaScope(String sinaScope) {
            this.sinaScope = sinaScope;
            return this;
        }

        public Builder setImageCachePath(String imageCachePath) {
            this.imageCachePath = imageCachePath;
            return this;
        }

        public ShareBuilder build() {
            try {
                checkFields();
            } catch (LackConfigException e) {
                e.printStackTrace();
                return new ShareBuilder(new Builder(mContext));
            }
            return new ShareBuilder(this);
        }

        private void checkFields() throws LackConfigException {
            File imageCacheFile = null;
            if (!TextUtils.isEmpty(imageCachePath)) {
                imageCacheFile = new File(imageCachePath);
                if (!imageCacheFile.isDirectory()) {
                    imageCacheFile = null;
                } else if (!imageCacheFile.exists() && !imageCacheFile.mkdirs()) {
                    imageCacheFile = null;
                }
            }
            if (imageCacheFile == null) {
                imageCachePath = getDefaultImageCacheFile(mContext);
            }

            if (defaultShareImage == -1) {
                throw new LackConfigException("缺少默认分享图");
            }
            if (TextUtils.isEmpty(qqAppId)) {
                throw new LackConfigException("缺少qqAppId");
            }
            if (TextUtils.isEmpty(qqScope)) {
                throw new LackConfigException("缺少qqScope");
            }
            if (TextUtils.isEmpty(wxAppId)) {
                throw new LackConfigException("缺少wxAppId");
            }
            if (TextUtils.isEmpty(sinaAppKey)) {
                throw new LackConfigException("缺少sinaAppKey");
            }
            if (TextUtils.isEmpty(sinaRedirectUrl)) {
                throw new LackConfigException("缺少sinaRedirectUrl");
            }
            if (TextUtils.isEmpty(sinaRedirectUrl)) {
                throw new LackConfigException("缺少sinaScope");
            }


        }

        private static String getDefaultImageCacheFile(Context context) {
            String imageCachePath = null;
            File extCacheFile = context.getExternalCacheDir();
            if (extCacheFile == null) {
                extCacheFile = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            }
            if (extCacheFile != null) {
                imageCachePath = extCacheFile.getAbsolutePath() + File.separator + IMAGE_CACHE_FILE_NAME + File.separator;
                File imageCacheFile = new File(imageCachePath);
                imageCacheFile.mkdirs();
            }
            return imageCachePath;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.defaultShareImage);
        dest.writeString(this.qqAppId);
        dest.writeString(this.qqScope);
        dest.writeString(this.wxAppId);
        dest.writeString(this.sinaAppKey);
        dest.writeString(this.sinaRedirectUrl);
        dest.writeString(this.sinaScope);
        dest.writeString(this.imageCachePath);
    }

    protected ShareBuilder(Parcel in) {
        this.defaultShareImage = in.readInt();
        this.qqAppId = in.readString();
        this.qqScope = in.readString();
        this.wxAppId = in.readString();
        this.sinaAppKey = in.readString();
        this.sinaRedirectUrl = in.readString();
        this.sinaScope = in.readString();
        this.imageCachePath = in.readString();
        this.imageDownloader = new DefaultImageDownLoader();
        this.taskExecutor = Executors.newCachedThreadPool();
    }

    public static final Creator<ShareBuilder> CREATOR = new Creator<ShareBuilder>() {
        public ShareBuilder createFromParcel(Parcel source) {
            return new ShareBuilder(source);
        }

        public ShareBuilder[] newArray(int size) {
            return new ShareBuilder[size];
        }
    };
}
