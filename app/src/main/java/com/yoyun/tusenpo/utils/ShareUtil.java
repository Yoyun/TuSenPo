package com.yoyun.tusenpo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Yoyun on 2017/7/19.
 */

public class ShareUtil {

    public static void shareSingleImage(Context context, String imagePath) {
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            return;
        }
        Uri imageUri = Uri.fromFile(imageFile);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType("image/*");
        context.startActivity(Intent.createChooser(intent, "分享到"));
    }
}
