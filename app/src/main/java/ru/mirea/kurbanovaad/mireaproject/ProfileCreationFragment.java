package ru.mirea.kurbanovaad.mireaproject;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ProfileCreationFragment extends Fragment implements ActivityResultCallback<ActivityResult> {

    private static final int REQUEST_CODE_PERMISSION = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private boolean isPermissionsGranted = false;
    private Uri imageUri;
    private ImageView imageView;
    private EditText editTextName;
    private EditText editTextSurname;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_creation, container, false);

        imageView = view.findViewById(R.id.imageView);
        editTextName = view.findViewById(R.id.editTextName);
        editTextSurname = view.findViewById(R.id.editTextSurname);

        Button buttonAddPhoto = view.findViewById(R.id.buttonUpdatePhoto);
        buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdatePhoto(v);
            }
        });


        // Handle permissions
        //permissionsRequest();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        permissionsRequest();
    }

    private void permissionsRequest() {
        final boolean storage_permission = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        final boolean camera_permission = PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA);

        if (storage_permission && camera_permission) {
            isPermissionsGranted = true;
        } else {
            isPermissionsGranted = false;
            requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionsGranted = true;
            } else {
                isPermissionsGranted = false;
            }
        }
    }


    public void onUpdatePhoto(View v) {
        Log.i("check", "OnUpdatePhotoButtonClicked");
        if (!isPermissionsGranted) {
            Log.i("", "\tHas no permissions...");
            permissionsRequest();
            return;
        }

        try {
            File file = createImageFile();
            String authorities = "ru.mirea.kurbanovaad.mireaproject.fileprovider";
            imageUri = FileProvider.getUriForFile(requireContext(), authorities, file);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onActivityResult(@NonNull ActivityResult result) {
        onActivityResult(REQUEST_IMAGE_CAPTURE, result.getResultCode(), result.getData());
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
}
