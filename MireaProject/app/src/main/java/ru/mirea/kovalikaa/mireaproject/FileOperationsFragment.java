package ru.mirea.kovalikaa.mireaproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileOperationsFragment extends Fragment {

    private RecyclerView operationsRecyclerView;
    private FloatingActionButton fabAddOperation;
    private OperationAdapter operationAdapter;
    private List<String> operationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_operations, container, false);

        operationsRecyclerView = view.findViewById(R.id.operationsRecyclerView);
        fabAddOperation = view.findViewById(R.id.fabAddOperation);

        operationList = new ArrayList<>();
        operationAdapter = new OperationAdapter(operationList);
        operationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        operationsRecyclerView.setAdapter(operationAdapter);

        loadExistingFiles();

        fabAddOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        return view;
    }

    private void loadExistingFiles() {
        String[] files = requireContext().fileList();
        for (String file : files) {
            if (file.endsWith(".pdf")) {
                operationList.add(file);
            }
        }
        operationAdapter.notifyDataSetChanged();
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input_text, null);
        final EditText inputText = dialogView.findViewById(R.id.inputText);
        builder.setView(dialogView);

        dialogView.findViewById(R.id.convertButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = inputText.getText().toString().trim();
                if (!text.isEmpty()) {
                    String fileName = convertToPdf(text);
                    if (fileName != null) {
                        operationList.add(fileName);
                        operationAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "PDF создан: " + fileName, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Введите текст", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.create().show();
    }

    private String convertToPdf(String text) {
        try {
            String fileName = "document_" + System.currentTimeMillis() + ".pdf";
            File file = new File(requireContext().getFilesDir(), fileName);
            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph(text));
            document.close();
            return fileName;
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ошибка при создании PDF", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private String readTextFile(String fileName) {
        try {
            FileInputStream fis = requireContext().openFileInput(fileName.replace(".pdf", ".txt"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            fis.close();
            return content.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private void openPdf(String fileName) {
        File file = new File(requireContext().getFilesDir(), fileName);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Не удалось открыть PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private class OperationAdapter extends RecyclerView.Adapter<OperationAdapter.ViewHolder> {
        private List<String> operations;

        public OperationAdapter(List<String> operations) {
            this.operations = operations;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_operation, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String fileName = operations.get(position);
            holder.operationTextView.setText(fileName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Показываем текст или открываем PDF
                    String textContent = readTextFile(fileName);
                    if (!textContent.isEmpty()) {
                        Toast.makeText(getContext(), "Содержимое: " + textContent, Toast.LENGTH_LONG).show();
                    } else {
                        openPdf(fileName);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return operations.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView operationTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                operationTextView = itemView.findViewById(R.id.operationTextView);
            }
        }
    }
}