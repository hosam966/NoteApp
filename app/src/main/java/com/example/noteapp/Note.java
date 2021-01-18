package com.example.noteapp;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

    private String note;
    private Uri imageUri;
    private Boolean checked;
    private int color;

    protected Note(Parcel in) {
        note = in.readString();
        imageUri = in.readParcelable(Uri.class.getClassLoader());
        byte tmpChecked = in.readByte();
        checked = tmpChecked == 0 ? null : tmpChecked == 1;
        color = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public Note(String note, int color) {
        this.note = note;
        this.color = color;
    }

    public Note(String note, int color, Uri imageUri) {
        this.note = note;
        this.imageUri = imageUri;
        this.color = color;
    }

    public Note(String note, int color,  Boolean checked) {
        this.note = note;
        this.checked = checked;
        this.color = color;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean isChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(note);
        dest.writeParcelable(imageUri, flags);
        dest.writeByte((byte) (checked == null ? 0 : checked ? 1 : 2));
        dest.writeInt(color);
    }
}


