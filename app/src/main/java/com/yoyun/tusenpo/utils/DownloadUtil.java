package com.yoyun.tusenpo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by Yoyun on 2017/7/19.
 */

public class DownloadUtil {

    public static Observable<String> downloadFile(final String fileUrl, final String filePath) {
        return Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                        File file = new File(filePath);
                        if (file.exists()) {

                        } else {
                            URL url = new URL(fileUrl);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(6000);
                            connection.setDoInput(true);
                            connection.setUseCaches(false);
                            InputStream is = connection.getInputStream();

                            byte[] bs = new byte[1024];
                            int len;
                            OutputStream os = new FileOutputStream(filePath);
                            while ((len = is.read(bs)) != -1) {
                                os.write(bs, 0, len);
                            }

                            os.close();
                            is.close();
                        }

                        e.onNext(filePath);
                        e.onComplete();
                    }
                });
    }
}
