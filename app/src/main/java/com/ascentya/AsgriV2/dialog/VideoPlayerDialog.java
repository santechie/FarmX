package com.ascentya.AsgriV2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.ascentya.AsgriV2.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class VideoPlayerDialog extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_video_player, container);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_video_player);
        dialog.setCancelable(false);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        dialog.getWindow().setAttributes(params);

        return dialog;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VideoView videoView = view.findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(getArguments().getString("url")));
        videoView.start();

        view.setOnClickListener(v -> dismiss());
    }

    public static void showVideo(FragmentManager fragmentManager, String videoUrl){
        Bundle args = new Bundle();
        args.putString("url", videoUrl);

        VideoPlayerDialog videoPlayerDialog = new VideoPlayerDialog();
        videoPlayerDialog.setArguments(args);
        videoPlayerDialog.show(fragmentManager, "video");
    }

    public static void showVideoDialog(Context context, String videoUrl){
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogLayout = inflater.inflate(R.layout.dialog_video_player, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setView(dialogLayout);
        dialog.show();
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final VideoView video_player_view = (VideoView) dialog.findViewById(R.id.videoView);
        final ImageView closeBtn = (ImageView) dialog.findViewById(R.id.close);

        WindowManager.LayoutParams lp = new WindowManager
                .LayoutParams(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        lp.copyFrom(dialog.getWindow().getAttributes());
        dialog.getWindow().setAttributes(lp);

        Uri uri = Uri.parse(videoUrl);
        video_player_view.setVideoURI(uri);
        video_player_view.start();

        closeBtn.setOnClickListener(v -> dialog.dismiss());
    }
}
