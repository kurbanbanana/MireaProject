package ru.mirea.kurbanovaad.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private EditText editTextName;
    private EditText editTextPhone;
    private Spinner spinnerBranch;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        editTextName = view.findViewById(R.id.editTextName);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        spinnerBranch = view.findViewById(R.id.spinnerBranch);
        Button buttonSaveProfile = view.findViewById(R.id.buttonSaveProfile);

        sharedPreferences = requireActivity().getSharedPreferences("profile_data", Context.MODE_PRIVATE);


        loadProfileData();

        setupBranchSpinner();


        buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });

        return view;
    }


    private void loadProfileData() {
        String name = sharedPreferences.getString("name", "");
        String phone = sharedPreferences.getString("phone", "");
        int branchPosition = sharedPreferences.getInt("branch_position", 0);

        editTextName.setText(name);
        editTextPhone.setText(phone);
        spinnerBranch.setSelection(branchPosition);
    }


    private void saveProfileData() {
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        int branchPosition = spinnerBranch.getSelectedItemPosition();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putInt("branch_position", branchPosition);
        editor.apply();
    }


    private void setupBranchSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.branches_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBranch.setAdapter(adapter);
    }
}