package com.example.roomdbnoteapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    Context context;
    ArrayList<NoteModel> list;
    DBHelper dbHelper;

    public NoteAdapter(Context context, ArrayList<NoteModel> list, DBHelper dbHelper) {
        this.context = context;
        this.list = list;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.note_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        // click function on delete button
        holder.delete.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setCancelable(true)
                    .setMessage("Are you Sure You Want To Delete")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbHelper.noteDao().delete(new NoteModel(list.get(holder.getAdapterPosition()).getId()
                                    , list.get(holder.getAdapterPosition()).getTitle(), list.get(holder.getAdapterPosition()).getDescription()));

                            list.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            AlertDialog alert11 = alertDialogBuilder.create();
            alert11.show();

        });
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvdesc.setText(list.get(position).getDescription());
        //Update Note Click function
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_box);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                EditText name1 = dialog.findViewById(R.id.etTitle);
                EditText desc = dialog.findViewById(R.id.etDesc);
                Button btnUpdate = dialog.findViewById(R.id.buttonAdd);
                btnUpdate.setText("Update Note");

                name1.setText(list.get(holder.getAdapterPosition()).getTitle());
                desc.setText(list.get(holder.getAdapterPosition()).getDescription());

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newName = name1.getText().toString();
                        String newPhonenum = desc.getText().toString();

                        NoteModel model = new NoteModel(list.get(holder.getAdapterPosition()).getId(), newName, newPhonenum);

                        dbHelper.noteDao().update(model);

                        //Refresh the recyclerview after Updating Note
                        list.set(holder.getAdapterPosition(), model);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvdesc;
        ImageView delete;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.etTitle);
            tvdesc = itemView.findViewById(R.id.etDesc);
            delete = itemView.findViewById(R.id.imgDelete);
        }
    }
}