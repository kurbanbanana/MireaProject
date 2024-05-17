package ru.mirea.kurbanovaad.mireaproject;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WorkFileFragment extends Fragment {

    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_file, container, false);

        editText = view.findViewById(R.id.editTextText);


        view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Обработка нажатия на FAB
                    // Шифрование и сохранение
                    String content = editText.getText().toString();
                    String encryptedText = encryptText(content);
                    saveTextToFile(encryptedText);
                }
        });

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String encryptedText = getTextFromFile();
                    String decryptedText = decryptText(encryptedText);
                    editText.setText(decryptedText);
                }
        });

            return view;
    }

    private void saveTextToFile(String text) {
            String fileName = "encrypted_text.txt";
            try {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file = new File(path, fileName);
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(text.getBytes());
                outputStream.close();
                Toast.makeText(getActivity(), "Файл успешно сохранен", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Ошибка сохранения файла", Toast.LENGTH_SHORT).show();
            }
    }

    private String getTextFromFile() {
            String fileName = "encrypted_text.txt";
            StringBuilder text = new StringBuilder();
            try {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file = new File(path, fileName);
                FileInputStream inputStream = new FileInputStream(file);
                int character;
                while ((character = inputStream.read()) != -1) {
                    text.append((char) character);
                }
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Ошибка чтения файла", Toast.LENGTH_SHORT).show();
            }
            return text.toString();
    }

    private String encryptText(String text) {
            StringBuilder encryptedText = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                char currentChar = text.charAt(i);
                if (Character.isLetter(currentChar)) {
                    char encryptedChar;
                    if (Character.isUpperCase(currentChar)) {
                        encryptedChar = (char) ('Z' - (currentChar - 'A'));
                    } else {
                        encryptedChar = (char) ('z' - (currentChar - 'a'));
                    }
                    encryptedText.append(encryptedChar);
                } else {
                    encryptedText.append(currentChar); // оставляем символы, не являющиеся буквами, без изменений
                }
            }
            return encryptedText.toString();
    }

    private String decryptText(String encryptedText) {
            StringBuilder decryptedText = new StringBuilder();
            for (int i = 0; i < encryptedText.length(); i++) {
                char currentChar = encryptedText.charAt(i);
                if (Character.isLetter(currentChar)) {
                    char decryptedChar;
                    if (Character.isUpperCase(currentChar)) {
                        decryptedChar = (char) ('A' + ('Z' - currentChar));
                    } else {
                        decryptedChar = (char) ('a' + ('z' - currentChar));
                    }
                    decryptedText.append(decryptedChar);
                } else {
                    decryptedText.append(currentChar); // оставляем символы, не являющиеся буквами, без изменений
                }
            }
            return decryptedText.toString();
    }
}
