package com.example.roomdatabase;


import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    void insert(MainData mainData);

    @Delete
    void delete(MainData mainData);

    @Query("UPDATE users SET user_name= :sName WHERE ID = :sID")
    void updateName(int sID,String sName);

    @Query("UPDATE users SET user_email= :sEmail WHERE ID = :sID")
    void updateEmail(int sID,String sEmail);

    @Query("UPDATE users SET user_phone= :sPhone WHERE ID = :sID")
    void updatePhone(int sID,String sPhone);

    @Query("UPDATE users SET user_password= :sPass WHERE ID = :sID")
    void updatePassword(int sID,String sPass);

    @Query("UPDATE users SET user_gender= :sGender WHERE ID = :sID")
    void updateGender(int sID,String sGender);


    @Query("SELECT * FROM users")
    List<MainData> getAll();
}
