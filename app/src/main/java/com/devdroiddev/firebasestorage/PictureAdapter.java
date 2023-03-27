package com.devdroiddev.firebasestorage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MyViewHolder> {
    private Context context;
    private List<PictureModel> pictureModelsList;

    public PictureAdapter(Context context) {
        this.context = context;
        pictureModelsList = new ArrayList<>();
    }

    public void add(PictureModel pictureModel)
    {
        pictureModelsList.add(pictureModel);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PictureModel pictureModel = pictureModelsList.get(position);
        holder.fileName.setText(pictureModel.getName());
        Glide.with(context)
                .load(pictureModel.getUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return pictureModelsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView fileName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.retImage);
            fileName = itemView.findViewById(R.id.retFile);
        }
    }

}
