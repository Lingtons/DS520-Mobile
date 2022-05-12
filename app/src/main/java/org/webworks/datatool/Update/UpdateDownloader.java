package org.webworks.datatool.Update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import org.webworks.datatool.R;
import java.io.File;

public class UpdateDownloader extends AsyncTask<String,Integer,Void> {

    private Context context;
    private long downloadReference;

    UpdateDownloader(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... urls) {
        try {
            String PATH = Environment.getExternalStorageDirectory()+"/Download/";
            File file = new File(PATH);
            if (!file.exists()) {
                file.mkdirs();
            }

            File outputFile = new File(file,context.getString(R.string.update_download_app_name));

            if(outputFile.exists()){
                outputFile.delete();
            }

            DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri Download_Uri = Uri.parse(urls[0]);
            DownloadManager.Request requests = new DownloadManager.Request(Download_Uri);
            requests.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            requests.setAllowedOverRoaming(false);
            requests.setTitle(context.getString(R.string.update_download_manager_title));
            requests.setDescription(context.getString(R.string.update_download_manager_content));
            requests.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, context.getString(R.string.update_download_app_name));

            downloadReference = downloadManager.enqueue(requests);

            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            context.registerReceiver(downloadReceiver, filter);
        } catch (Exception e) {

        }
        return null;
    }

    /**
    * This method sets the recevier for the update download
    * */
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context _context, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadReference == referenceId) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/Download/" + context.getString(R.string.update_download_app_name))),
                        "application/vnd.android.package-archive");
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            }
        }
    };
}
