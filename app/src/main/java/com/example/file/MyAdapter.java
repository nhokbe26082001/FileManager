package com.example.file;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    Context context;
    File[] filesNfolders;
    public MyAdapter(Context context, File[] filesNfolders){
        this.context = context;
        this.filesNfolders = filesNfolders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File selectedFile = filesNfolders[position];
        holder.textView.setText(selectedFile.getName());

        if(selectedFile.isDirectory()){
            holder.imageView.setImageResource(R.drawable.baseline_folder_24);
        }
        else {
            holder.imageView.setImageResource(R.drawable.baseline_insert_drive_file_24);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedFile.isDirectory()){
                    Intent intent = new Intent(context, File_List_Activity.class);
                    String path = selectedFile.getAbsolutePath();
                    intent.putExtra("path", path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else{
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        String type = "image/*";
                        intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), type);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    catch (Exception e){
                        Toast.makeText(context.getApplicationContext(), "Không mở được file", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenu().add("MOVE");
                popupMenu.getMenu().add("DELETE");
                popupMenu.getMenu().add("RENAME");
                popupMenu.getMenu().add("NEW FOLDER");
                popupMenu.getMenu().add("NEW TEXT FILE");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("DELETE")){
                            AlertDialog alertDialog = new AlertDialog.Builder(context.getApplicationContext()).create();
                            alertDialog.setTitle("");
                            alertDialog.setMessage("DELETE ?");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            selectedFile.delete();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }

                        if(item.getTitle().equals("NEW FOLDER")){
                            String folder_main = "New Folder";

                            File f = new File(Environment.getExternalStorageDirectory(), folder_main);
                            if (!f.exists()) {
                                f.mkdirs();
                            }
                        }

                        if(item.getTitle().equals("RENAME")){
                            final EditText edittext = new EditText(context.getApplicationContext());
                            AlertDialog alertDialog = new AlertDialog.Builder(context.getApplicationContext()).create();
                            alertDialog.setTitle("");
                            alertDialog.setView(edittext);
                            alertDialog.setMessage("RENAME ?");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String YouEditTextValue = edittext.getText().toString();
                                            selectedFile.renameTo(YouEditTextValue);
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }

                        if(item.getTitle().equals("MOVE")){

                        }

                        if(item.getTitle().equals("NEW TEXT FILE")){

                        }
                        return true;
                    }
                });

                popupMenu.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return filesNfolders.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public ViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.fileNameTextView);
            imageView = itemView.findViewById(R.id.iconView);
        }
    }
}
