package com.haritbrij.haritBrij;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.haritbrij.haritBrij.models.Tree;

import java.util.ArrayList;
import java.util.List;

public class TreeListAdapter extends RecyclerView.Adapter<TreeListAdapter.ViewHolder> {
    private ArrayList<Tree> treesList;
    public TreeListAdapter(List<Tree>treeList) {
        treesList = (ArrayList<Tree>) treeList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView treeIdTextView;
        public TextView treeImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            treeIdTextView = itemView.findViewById(R.id.tree_id_item);
            treeImageView = itemView.findViewById(R.id.tree_image_item);
        }
    }

    @NonNull
    @Override
    public TreeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.treelistitem, parent, false);

        return new TreeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TreeListAdapter.ViewHolder holder, int position) {
        holder.treeIdTextView.setText(treesList.get(position).id);
    }

    @Override
    public int getItemCount() {
        return treesList.size();
    }
}