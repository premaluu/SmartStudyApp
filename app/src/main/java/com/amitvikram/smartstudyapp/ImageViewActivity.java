package com.amitvikram.smartstudyapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.Collection;

public class ImageViewActivity extends AppCompatActivity implements Scene.OnUpdateListener{
    private  CustomArFragment Ar;
    public Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        Ar=(CustomArFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentImage);
        Ar.getPlaneDiscoveryController().hide();//new
        Ar.getArSceneView().getScene().addOnUpdateListener(this);

    }

    public void setDataBase(Config config, Session session) {
        Bitmap deerBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.deer);
        Bitmap waterBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.water);
        Bitmap shBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.sodium);
        Bitmap etBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.ethanal);
        AugmentedImageDatabase aid=new AugmentedImageDatabase(session);
        aid.addImage("deer",deerBitmap);
        aid.addImage("water",waterBitmap);
        aid.addImage("sodium",shBitmap);
        aid.addImage("ethan",etBitmap);
        config.setAugmentedImageDatabase(aid);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onUpdate(FrameTime frameTime) {
        Frame frame=Ar.getArSceneView().getArFrame();
        Collection<AugmentedImage> images= frame.getUpdatedTrackables(AugmentedImage.class);

        for(AugmentedImage image : images ){

            if(image.getTrackingState()== TrackingState.TRACKING){

                if(image.getName().equals("water")){
                    Anchor anchor = image.createAnchor((image.getCenterPose()));
                    ModelRenderable.builder()
                            .setSource(this, Uri.parse("water.sfb"))
                            .build()
                            .thenAccept(modelRenderable -> placeModel(modelRenderable,anchor));

                }
                else if(image.getName().equals("deer")){
                    Anchor anchor = image.createAnchor((image.getCenterPose()));
                    ModelRenderable.builder()
                            .setSource(this, Uri.parse("deer.sfb"))
                            .build()
                            .thenAccept(modelRenderable -> placeModel(modelRenderable,anchor));
                }
                else if(image.getName().equals("sodium")){
                    Anchor anchor = image.createAnchor((image.getCenterPose()));
                    ModelRenderable.builder()
                            .setSource(this, Uri.parse("sodiumhydroxide.sfb"))
                            .build()
                            .thenAccept(modelRenderable -> placeModel(modelRenderable,anchor));
                }
                else if(image.getName().equals("ethan")){
                    Anchor anchor = image.createAnchor((image.getCenterPose()));
                    ModelRenderable.builder()
                            .setSource(this, Uri.parse("ethanal.sfb"))
                            .build()
                            .thenAccept(modelRenderable -> placeModel(modelRenderable,anchor));
                }


            }
        }
    }

    private void placeModel(ModelRenderable modelRenderable,Anchor anchor){
        AnchorNode anchorNode=new AnchorNode(anchor);
        anchorNode.setRenderable(modelRenderable);
        // TransformableNode transformableNode = new TransformableNode(Ar.getTransformationSystem());
        //transformableNode.setParent(anchorNode);
        //transformableNode.setRenderable(modelRenderable);
        Ar.getArSceneView().getScene().addChild(anchorNode);
        //transformableNode.select();

    }
}