package com.example.roomdatabase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<MainData> dataList;
    private Activity context;
    private RoomDB database;
    private String uGender;

    public MainAdapter(Activity context,List<MainData>dataList){
        this.context=context;
        this.dataList=dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MainData data=dataList.get(position);

        database=RoomDB.getInstance(context);

        holder.name.setText(data.getName());
        holder.email.setText(data.getEmail());
        holder.phoneNo.setText(data.getPhone());
        holder.password.setText(data.getPassword());
        holder.gender.setText(data.getGender());


        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CutPasteId")
            @Override
            public void onClick(View view) {
                MainData d=dataList.get(holder.getAdapterPosition());

                int sID=d.getID();

                String sName=d.getName();

                String sEmail=d.getEmail();

                String sPhone=d.getPhone();

                String sPass=d.getPassword();

                String sGender=d.getGender();


                Dialog dialog=new Dialog(context,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                dialog.setContentView(R.layout.dialog_update);


                int width= WindowManager.LayoutParams.MATCH_PARENT;
                int height=WindowManager.LayoutParams.MATCH_PARENT;

                dialog.getWindow().setLayout(width,height);
                dialog.show();

                EditText editName=dialog.findViewById(R.id.edit_name);
                EditText editEmail=dialog.findViewById(R.id.edit_email);
                EditText editPhone=dialog.findViewById(R.id.edit_Phone_no);
                EditText editPass=dialog.findViewById(R.id.edit_password);

                RadioButton male,female;

                male=dialog.findViewById(R.id.update_radio_male);
                female=dialog.findViewById(R.id.update_radio_female);

                if(sGender.equals("Male")){
                    male.setChecked(true);
                }
                else if(sGender.equals("Female")){
                    female.setChecked(true);

                }
                uGender=sGender;

                Button btupdate=dialog.findViewById(R.id.bt_update);


                editName.setText(sName);
                editEmail.setText(sEmail);
                editPhone.setText(sPhone);
                editPass.setText(sPass);


               male.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       uGender="Male";
                   }
               });
               female.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       uGender="Female";
                   }
               });

                btupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        String uName=editName.getText().toString().trim();
                        String uEmail=editEmail.getText().toString().trim();
                        String uPhone=editPhone.getText().toString().trim();
                        String uPass=editPass.getText().toString().trim();

                        if(!validateUserName()|!validateEmail()|!validatePhone()|!validatePass()){
                            Toast.makeText(context.getApplication(), "Enter Valid Data to Update", Toast.LENGTH_LONG).show();
                            return;
                        }

                        database.mainDao().updateName(sID,uName);
                        database.mainDao().updateEmail(sID,uEmail);
                        database.mainDao().updatePhone(sID,uPhone);
                        database.mainDao().updatePassword(sID,uPass);
                        database.mainDao().updateGender(sID,uGender);

                        Toast.makeText(context.getApplication(), "Data Updated Successfully", Toast.LENGTH_LONG).show();
                        dataList.clear();
                        dataList.addAll(database.mainDao().getAll());
                        notifyDataSetChanged();
                    }

                    private boolean validateUserName() {
                        String val= editName.getText().toString();
                        String noWhiteSpace="\\A\\w{4,20}\\z";
                        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
                        Matcher ms = ps.matcher(editName.getText().toString());
                        boolean bs = ms.matches();

                        if(val.isEmpty()){
                            editName.setError("Field Cannot be empty");
                            return false;
                        }
                        else if (bs == false) {
                            editName.setError(("Username should not contain digits or special letters"));
                            return false;
                        }
                        else if(val.length()>=15){
                            editName.setError("Username to long");
                            return false;
                        }
                        else if(!val.matches(noWhiteSpace)){
                            editName.setError("White Space are not allowed");
                            return false;
                        }
                        else{
                            editName.setError(null);
                            return true;
                        }
                    }
                    private boolean validateEmail() {
                        String val= editEmail.getText().toString();
                        if(val.isEmpty()){
                            editEmail.setError("Field Cannot be empty");
                            return false;
                        }
                        else if(!Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
                            editEmail.setError("Invalid email address");
                            return false;
                        }
                        else{
                            editEmail.setError(null);
                            return true;
                        }
                    }
                    private boolean validatePhone() {
                        String val= editPhone.getText().toString();
                        if(val.isEmpty()){
                            editPhone.setError("Field Cannot be empty");
                            return false;
                        }
                        else if(!((val.length()) ==11)){
                            editPhone.setError("Phone Number should be of 11 Digits");
                            return false;
                        }
                        else{
                            editPhone.setError(null);
                            return true;
                        }
                    }
                    private boolean validatePass() {
                        String val= editPass.getText().toString();
                        String passwordVal="^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
                        CharSequence input=val;
                        Pattern pattern = Pattern.compile(passwordVal, Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(input);

                        if(val.isEmpty()){
                            editPass.setError("Field Cannot be empty");
                            return false;
                        }
                        else if(!matcher.matches()){
                            editPass.setError("Password is too weak");
                            return false;
                        }
                        else if(val.length()<8){
                            editPass.setError("Password should be of 8 or more characters");
                            return false;
                        }
                        else {
                            editPass.setError(null);
                            return true;
                        }
                    }
                });
            }
        });
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainData d=dataList.get(holder.getAdapterPosition());

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete Data");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        database.mainDao().delete(d);

                        int position=holder.getAdapterPosition();
                        dataList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,dataList.size());
                        Toast.makeText(context.getApplication(), "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,email,phoneNo,password,gender;
        ImageView btEdit,btDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.text_name);
            email=itemView.findViewById(R.id.text_email);
            phoneNo=itemView.findViewById(R.id.text_phone);
            password=itemView.findViewById(R.id.text_pass);
            gender=itemView.findViewById(R.id.text_gender);

            btEdit=itemView.findViewById(R.id.bt_edit);
            btDelete=itemView.findViewById(R.id.bt_delete);
        }
    }


}
