package com.ascentya.AsgriV2.my_farm.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.my_farm.model.FileImageItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageItemAdapter extends RecyclerView.Adapter<ImageItemAdapter.ImageViewHolder> {

    private Action action;
    Context context;
    ArrayList<FileImageItem> getImages;

    public ImageItemAdapter(Action action) {
        this.action = action;
        this.context = context;
        this.getImages = getImages;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageViewHolder viewHolder = new ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_activity_files_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String url = getImages.get(position).getUrl();
        holder.setImage(url);

        holder.fileDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                String title = URLUtil.guessFileName(url, null, null);
                request.setTitle(title);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                DownloadManager downloadManager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);

            }
        });
    }

    @Override
    public int getItemCount() {
        return action.getFileReportItems().size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView fileDownload;
        private TextView fileText;

        public ImageViewHolder(View itemView) {
            super(itemView);

            fileDownload = itemView.findViewById(R.id.fileText);
            fileText = itemView.findViewById(R.id.fileDownload);
//            ArrayList<FileImageItem> getImages;


//            fileDownload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    String url = getImages.get(position).getUrl();
//
//                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getUrl));
//                    String title = URLUtil.guessFileName(getUrl, null, null);
//                    request.setTitle(title);
//                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
//
//                    DownloadManager downloadManager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
//                    downloadManager.enqueue(request);
//
//                }
//            });
        }

        void setImage(String link){
            Glide.with(itemView.getContext()).load(link).into(fileDownload);
        }


//        public void update(FileImageItem link) {
//            fileText.setText(link.getName());
//            Glide.with(itemView.getContext()).load(link.getUrl()).into(fileDownload);
//        }
    }

    interface Action{
        ArrayList<FileImageItem> getFileReportItems();
    }


}
