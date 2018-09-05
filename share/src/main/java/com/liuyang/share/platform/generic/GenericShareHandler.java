/*
 * Copyright (C) 2015 Bilibili <jungly.ik@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liuyang.share.platform.generic;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;

import com.liuyang.share.R;
import com.liuyang.share.ShareBuilder;
import com.liuyang.share.ShareResultCallBack;
import com.liuyang.share.SocializeMedia;
import com.liuyang.share.platform.base.BaseShareHandler;
import com.liuyang.share.exception.ShareException;
import com.liuyang.share.params.BaseShareParam;
import com.liuyang.share.params.ShareParamAudio;
import com.liuyang.share.params.ShareParamImage;
import com.liuyang.share.params.ShareParamText;
import com.liuyang.share.params.ShareParamVideo;
import com.liuyang.share.params.ShareParamWebPage;

/**
 * 其他分项方式
 */
public class GenericShareHandler extends BaseShareHandler {

    public GenericShareHandler(Activity context,ShareBuilder shareBuilder) {
        super(context,shareBuilder);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // nothing to do
    }


    @Override
    protected void init() throws ShareException {

    }

    @Override
    protected void shareText(ShareParamText params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareImage(ShareParamImage params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareWebPage(ShareParamWebPage params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareAudio(ShareParamAudio params) throws ShareException {
        share(params);
    }

    @Override
    protected void shareVideo(ShareParamVideo params) throws ShareException {
        share(params);
    }

    private void share(BaseShareParam param) {
        ShareResultCallBack shareListener = getShareResultCallBack();
        Intent shareIntent = createIntent(param.getTitle(), param.getContent());
        Intent chooser = Intent.createChooser(shareIntent, "分享到：");
        try {
            getContext().startActivity(chooser);
        } catch (ActivityNotFoundException ignored) {
            if (shareListener != null) {
                shareListener.onError(getShareMedia(),new ShareException(getContext().getString(R.string.share_sdk_activity_not_found_failed)));
            }
        }
    }

    private Intent createIntent(String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        return intent;
    }

    @Override
    protected boolean isNeedActivityContext() {
        return true;
    }

    @Override
    public SocializeMedia getShareMedia() {
        return SocializeMedia.GENERIC;
    }
}
