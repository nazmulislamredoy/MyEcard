package org.auction.udp;

/**
 * Created by alamgir on 6/5/2017.
 */

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.bdlions.client.reqeust.threads.IServerCallback;
import org.bdlions.client.reqeust.threads.UDPCom;
import org.bdlions.client.reqeust.uploads.UploadService;
import org.bdlions.transport.packet.IPacketHeader;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by alamgir on 5/21/2017.
 */
public class BackgroundUploader extends AsyncTask<Object, Integer, Void> {

    IPacketHeader packetHeader = null;
    String request = null;
    //IServerCallback callback = null;
    Handler handler = null;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");


    @Override
    protected Void doInBackground(Object ... params) {
        try {
            String imageUri = (String) params[0];
            Handler handler = (Handler) params [ 1 ];
            File image = new File(imageUri);
            //String fileName = UploadService.uploadImage("http://roomauction.co.uk/", image);
            String fileName = UploadService.uploadImage("http://212.24.103.134:8080/ECardServer/", image);
            //String fileName = UploadService.uploadImage("http://192.168.0.106:8080/", image);


            Message message = new Message();
            message.obj = fileName;
            handler.sendMessage(message);
            System.out.println("fileName: "+fileName);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


}

