package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageDownload {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String download_url = "http://192.168.1.113:8090/static/images/test01.jpg";

    public static void run(){
//        final File file = f;
        new Thread(){
            @Override
            public void run(){
                Request request = new Request.Builder().get().url(download_url).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("ImageDownload", "download image failed!");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // 将响应数据转化为输入流数据
                        InputStream inputStream = response.body().byteStream();
                        // 将输入流数据转化为Bitmap位图数据
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        // 保存图片至指定路径
                        File file = new File("download_path");
                        file.createNewFile();
                        // 创建文件输出流对象用来向文件中写入数据
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        // 将bitmap存储为jpg格式的图片
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        // 刷新文件流
                        fileOutputStream.flush();
                        fileOutputStream.close();

                        Message msg = Message.obtain();
                        msg.obj = bitmap;

                    }
                });
            }
        }.start();
    }

//    private Handler handler = new Handler(){
//        @Override
//        public void handlerMessage(Message msg){
//            image.setImageBitmap((Bitmap) mag.obj);
//        }
//    }

}
