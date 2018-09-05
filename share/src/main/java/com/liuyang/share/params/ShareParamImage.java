package com.liuyang.share.params;

import android.os.Parcel;

/**
 * 分享图片
 */
public class ShareParamImage extends BaseShareParam {

    private ShareImage mImage;

    public ShareParamImage() {
    }

    public ShareParamImage(String title, String content) {
        super(title, content);
    }

    public ShareParamImage(String title, String content, String targetUrl) {
        super(title, content, targetUrl);
    }

    public ShareImage getImage() {
        return mImage;
    }

    public String getImageUrl() {
        return mImage == null ? null : mImage.getNetImageUrl();
    }

    public void setImage(ShareImage image) {
        mImage = image;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(mImage, 0);
    }

    protected ShareParamImage(Parcel in) {
        super(in);
        mImage = in.readParcelable(ShareImage.class.getClassLoader());
    }

    public static final Creator<ShareParamImage> CREATOR = new Creator<ShareParamImage>() {
        public ShareParamImage createFromParcel(Parcel source) {
            return new ShareParamImage(source);
        }

        public ShareParamImage[] newArray(int size) {
            return new ShareParamImage[size];
        }
    };
}
