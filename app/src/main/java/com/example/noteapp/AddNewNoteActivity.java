package com.example.noteapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AddNewNoteActivity extends AppCompatActivity {

    private static final int READ_PHOTO_FROM_GALLERY_PERMISSION = 120;
    private static final int PICK_IMAGE = 100;

    RadioButton redRadioButton;
    RadioButton greenRadioButton;
    RadioButton blueRadioButton;

    RadioButton imageRadioButton;
    RadioButton noteRadioButton;
    RadioButton checkBoxRadioButton;

    EditText editText;
    ImageView imageView;
    CheckBox checkBox;

    Button buttonSubmit;

    private Uri mSelectedPhotoUri;
    private int color;
    private NoteType noteType;

    private Note note;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        redRadioButton = findViewById(R.id.red_radio_Button);
        greenRadioButton = findViewById(R.id.green_radio_Button);
        blueRadioButton = findViewById(R.id.blue_radio_Button);
        editText = findViewById(R.id.edit_note_text);
        imageView = findViewById(R.id.photoImageView);
        checkBox = findViewById(R.id.checkBox);
        buttonSubmit = findViewById(R.id.button_submit);

        imageRadioButton = findViewById(R.id.image_note_radio_Button);
        noteRadioButton = findViewById(R.id.text_note_radio_Button);
        checkBoxRadioButton = findViewById(R.id.checkbox_note_radio_Button);

        redRadioButton.setButtonDrawable(R.drawable.ic_red);
        greenRadioButton.setButtonDrawable(R.drawable.ic_green);
        blueRadioButton.setButtonDrawable(R.drawable.ic_blue);

        imageRadioButton.setButtonDrawable(R.drawable.ic_image);
        noteRadioButton.setButtonDrawable(R.drawable.ic_note);
        checkBoxRadioButton.setButtonDrawable(R.drawable.ic_checkbox);
        editText.setTextColor(Color.BLACK);

        redRadioButton.setOnClickListener(v -> {

            editText.setBackgroundColor(Color.RED);
            color = ContextCompat.getColor(AddNewNoteActivity.this, R.color.red);
        });

        greenRadioButton.setOnClickListener(v -> {
            editText.setBackgroundColor(Color.GREEN);
            color = ContextCompat.getColor(AddNewNoteActivity.this, R.color.green);
        });

        blueRadioButton.setOnClickListener(v -> {
            editText.setBackgroundColor(Color.BLUE);
            color = ContextCompat.getColor(AddNewNoteActivity.this, R.color.blue);
        });

        imageRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    noteType = NoteType.PHOTO_NOTE;
                }
            }
        });

        noteRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imageView.setVisibility(View.GONE);
                    checkBox.setVisibility(View.GONE);
                    noteType = NoteType.TEXT_NOTE;
                }
            }
        });

        checkBoxRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imageView.setVisibility(View.GONE);
                    checkBox.setVisibility(View.VISIBLE);
                    noteType = NoteType.CHECK_NOTE;
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });


        if (getIntent() != null && getIntent().hasExtra(Constants.EXTRA_NOTE_TEXT)) {
            note = getIntent().getParcelableExtra(Constants.EXTRA_NOTE_TEXT);
            position = getIntent().getIntExtra(Constants.EXTRA_POSITION, -1);
            editText.setText(note.getNote());
            color = note.getColor();
            if (note.getImageUri() != null) {
                mSelectedPhotoUri = note.getImageUri();
                imageView.setImageURI(mSelectedPhotoUri);
                imageRadioButton.setChecked(true);
            } else if (note.isChecked() != null) {
              checkBox.setChecked(note.isChecked());
              checkBoxRadioButton.setChecked(true);
            } else {
                noteRadioButton.setChecked(true);
            }
        } else {
            color = ContextCompat.getColor(this, R.color.blue);
            noteType = NoteType.PHOTO_NOTE;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_PHOTO_FROM_GALLERY_PERMISSION) {
            // <
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                firePickPhotoIntent();
            } else {
                Toast.makeText(this, R.string.read_permission_required, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                setSelectedPhoto(data.getData());
                getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
    }

    private void setSelectedPhoto(Uri data) {
        imageView.setImageURI(data);
        mSelectedPhotoUri = data;

    }

    public void selectPhoto() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_PHOTO_FROM_GALLERY_PERMISSION);
        } else {
            firePickPhotoIntent();
        }
    }

    private void firePickPhotoIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }

    public void submit() {
        if (editText != null) {
            Intent intent = new Intent();
            String noteUserText = editText.getText().toString();
            if (note == null) {
                switch (noteType) {
                    case TEXT_NOTE:
                        note = new Note(noteUserText, color);
                        break;
                    case PHOTO_NOTE:
                        note = new Note(noteUserText, color, mSelectedPhotoUri);
                        break;
                    case CHECK_NOTE:
                        note = new Note(noteUserText, color, checkBox.isChecked());
                        break;
                }
            } else {
                note.setNote(noteUserText);
                note.setColor(color);
                intent.putExtra(Constants.EXTRA_POSITION, position);
                switch (noteType) {
                    case TEXT_NOTE:
                        note.setImageUri(null);
                        note.setChecked(null);
                        break;
                    case PHOTO_NOTE:
                        note.setImageUri(mSelectedPhotoUri);
                        note.setChecked(null);
                        break;
                    case CHECK_NOTE:
                        note.setChecked(checkBox.isChecked());
                        note.setImageUri(null);
                        break;
                }
            }
            intent.putExtra(Constants.EXTRA_NOTE_TEXT, note);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}