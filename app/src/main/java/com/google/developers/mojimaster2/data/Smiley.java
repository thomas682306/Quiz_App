package com.google.developers.mojimaster2.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * A Model class that holds information about the emoji.
 * Class defines a table for the Room database with primary key the {@see #mCode}.
 */
@Entity(tableName = DataSmileyName.TABLE_NAME)
public class Smiley {

    @PrimaryKey
    @ColumnInfo(name = DataSmileyName.COL_UNICODE)
    @NonNull
    private String mCode;

    @ColumnInfo(name = DataSmileyName.COL_NAME)
    private String mName;

    @ColumnInfo(name = DataSmileyName.COL_EMOJI)
    private String mEmoji;

    public Smiley(@NonNull String code, String name, String emoji) {
        this.mCode = code;
        this.mName = name;
        this.mEmoji = emoji;
    }

    @NonNull
    public String getCode() {
        return mCode;
    }

    public String getName() {
        return mName;
    }

    public String getEmoji() {
        return mEmoji;
    }

}
