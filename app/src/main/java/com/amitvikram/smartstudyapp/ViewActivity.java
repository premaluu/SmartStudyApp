package com.amitvikram.smartstudyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ViewActivity extends AppCompatActivity {
    //creating ar fragment
    private ArFragment arFragment;
    private Intent intent;
    private ModelRenderable renderable;
    private float HEIGHT = 1.25f;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference modelRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        openDialog();
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arfragment);
        intent = getIntent();
        String modelName = intent.getStringExtra("model_name");
        String modelLink = intent.getStringExtra("model_link");
        modelName = modelName+".glb";


        downloadModel(modelName);
    }
    public void openDialog() {
        Instruction in = new Instruction();
        in.show(getSupportFragmentManager(), "Instruction Dialog");
    }
    private void downloadModel(String name) {
        Toast.makeText(this, "Model Downloading, Please wait..", Toast.LENGTH_SHORT).show();
        //FirebaseApp.initializeApp(this);
        modelRef = FirebaseStorage.getInstance().getReference().child(name);
        try {
            File file = File.createTempFile(name, "glb");
            modelRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    buildModel(file);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void buildModel(File file) {
        RenderableSource renderableSource = RenderableSource
                .builder()
                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build();

        ModelRenderable
                .builder()
                .setSource(this, renderableSource)
                .setRegistryId(file.getPath())
                .build()
                .thenAccept(modelRenderable -> {
                    Toast.makeText(this, "Model Downloaded..", Toast.LENGTH_LONG).show();
                    renderable = modelRenderable;
                });
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
            anchorNode.setRenderable(renderable);
            arFragment.getArSceneView().getScene().addChild(anchorNode);
        });
    }
}