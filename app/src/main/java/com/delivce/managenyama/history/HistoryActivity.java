package com.delivce.managenyama.history;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.delivce.managenyama.R;
import com.delivce.managenyama.adapters.SalesAdapter;
import com.delivce.managenyama.models.History;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    Button btnGeneratePDF;
    RecyclerView historyRV;

    SalesAdapter salesAdapter;

    public final String categoryCollection = "meat_sales";
    List<Map<String, Object>> sales = new ArrayList<>();

    List<History> historyList = new ArrayList<>();
    FirebaseFirestore db;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnGeneratePDF = findViewById(R.id.btn_generate_pdf);
        historyRV = findViewById(R.id.rv_history_sales);

        historyRV.setNestedScrollingEnabled(true);
        historyRV.setLayoutManager(new LinearLayoutManager(HistoryActivity.this, LinearLayoutManager.VERTICAL, false));

        btnGeneratePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePDF();
            }
        });

        dialog = ProgressDialog.show(HistoryActivity.this, "",
                "Fetching history. Please wait...", true);
        dialog.show();

        sales.clear();
        fetchSuppliers();


    }

    private void generatePDF() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        ConstraintLayout root = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null); //RelativeLayout is root view of my UI(xml) file.
        root.setDrawingCacheEnabled(true);
        Bitmap screen= getBitmapFromView(this.getWindow().findViewById(R.id.history_constraint_layout)); // here give id of our root layout (here its my RelativeLayout's id)
    }


    private void fetchSuppliers() {
        db = FirebaseFirestore.getInstance();
        Log.d("FETCH_CATEGORY_SUCCESS", String.valueOf(db.collection(categoryCollection)));
        db.collection(categoryCollection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d("FETCH_isSuccessful", "TRUE");
                            for(QueryDocumentSnapshot document: task.getResult()){
                                Log.d("FETCH_CATEGORY_SUCCESS", document.getId() + " => " + document.getData());
                                sales.add(document.getData());
                            }
                            Log.d("CATEGORIES_LIST", String.valueOf(sales));

                            salesAdapter = new SalesAdapter(HistoryActivity.this, db, sales);
                            LinearLayoutManager layoutManager=new LinearLayoutManager(HistoryActivity.this, LinearLayoutManager.VERTICAL, false);

                            historyRV.setLayoutManager(layoutManager);
                            historyRV.setAdapter(salesAdapter);
                            dialog.dismiss();
                        }else {

                            Log.w("FETCH_SUPPLIERS_ERROR", "Error getting documents.", task.getException());
                            Toast.makeText(HistoryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
//
//    public void convertBitmaptoPDF(Bitmap screen) throws DocumentException, IOException {
//        Document document = new Document();
//
//        String directoryPath = android.os.Environment.getExternalStorageDirectory().toString();
//
//        PdfWriter.getInstance(document, new FileOutputStream(directoryPath + "/example.pdf")); //  Change pdf's name.
//
//        document.open();
//
//        Image image = Image.getInstance(screen);  // Change image's name and extension.
//
//        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
//                - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
//        image.scalePercent(scaler);
//        image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
//
//        document.add(image);
//        document.close();
//    }

}