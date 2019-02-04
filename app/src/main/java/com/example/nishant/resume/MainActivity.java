package com.example.nishant.resume;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nishant.resume.CustomArFragment;
import com.example.nishant.resume.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private boolean shoulAddplane = true;
    ImageView imview;
    int count=0;



    private static final float INFO_CARD_Y_POS_COEFF = 0.55f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent)
            {
                placeObject(arFragment,hitResult.createAnchor(),Uri.parse("model__0.sfb"),null);
            }
        });
    }

    public boolean setupAugmentedImageDatabase(Config config, Session session){

        AugmentedImageDatabase augmentedImageDatabase = null;
        Map<String,Bitmap> bitmap = loadimage();

        config.setFocusMode(Config.FocusMode.AUTO);

        if (bitmap ==null){
            return false;
        }

        augmentedImageDatabase = new AugmentedImageDatabase(session);

        for ( Map.Entry<String, Bitmap> entry : bitmap.entrySet()) {

            augmentedImageDatabase.addImage(entry.getKey(), entry.getValue());
        }

        config.setAugmentedImageDatabase(augmentedImageDatabase);
        return true;


    }
    public Map<String,Bitmap> loadimage() {
        Map<String,Bitmap> bm = new HashMap<>();
        try (InputStream is = getAssets().open("bk.png")) {
            bm.put("airplane",BitmapFactory.decodeStream(is));

        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream is = getAssets().open("bk.png")) {
            bm.put("airplane",BitmapFactory.decodeStream(is));

        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream is = getAssets().open("fox.jpg")) {
            bm.put("fox",BitmapFactory.decodeStream(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onUpdateFrame(FrameTime frameTime)
    {
        int[] a = arFragment.getArSceneView().getArFrame().getCamera().getImageIntrinsics().getImageDimensions();
//        Log.d("image-dimensions", String.valueOf(a[0])+" "+String.valueOf(a[0]) );
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<AugmentedImage> augmentedImages = frame.getUpdatedTrackables(AugmentedImage.class);
//        Log.d("data1234",frame.getUpdatedTrackables(AugmentedImage.class).to String());


        for (AugmentedImage augmentedImage:augmentedImages){
//            Log.d("datadone",augmentedImage.getName());


            if (augmentedImage.getTrackingState() == TrackingState.TRACKING){
                if (augmentedImage.getName().equals("airplane") && shoulAddplane){

                    placeObject(arFragment,augmentedImage.createAnchor(augmentedImage.getCenterPose()),Uri.parse("model__0.sfb"),augmentedImage.getName());

                }
                else if (augmentedImage.getName().equals("fox")){
                    placeObject(arFragment,augmentedImage.createAnchor(augmentedImage.getCenterPose()),Uri.parse("fox.sfb"), augmentedImage.getName());

                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void placeObject(ArFragment fragment, Anchor anchor, Uri model, String name){

        ModelRenderable.builder()
                .setSource(fragment.getContext(),model)
                .build()
                .thenAccept(renderable -> addnodetoscene(fragment, anchor, renderable))
                .exceptionally(throwable -> {
                    Log.d("enter1",throwable.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(throwable.getMessage()).setTitle("Error");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return null;
                });

        }



    public void addnodetoscene(ArFragment fragment, Anchor anchor,Renderable renderable){


        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(fragment.getArSceneView().getScene());
        Node node = new Node();

        Quaternion q1 = node.getWorldRotation();
        Quaternion q2 = new Quaternion(180,150,150,-150);
        // last one for direction with head
        //
        node.setLocalRotation(Quaternion.multiply(q1, q2));

//        node.setLocalRotation(q2);

        node.setParent(anchorNode);
        node.setRenderable(renderable);

        node.setOnTouchListener(new Node.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                if (count == 0) {

                    ImageView im = new ImageView(MainActivity.this);
                    im.setImageDrawable(getResources().getDrawable(R.drawable.self));


                    Node infoCard = new Node();
                    infoCard.setParent(node);


                    ViewRenderable.builder()
                            .setView(MainActivity.this, im)
                            .build()
                            .thenAccept(
                                    (renderable) -> {

                                        infoCard.setRenderable(renderable);
                                    })
                            .exceptionally(
                                    (throwable) -> {
                                        throw new AssertionError("Could not load plane card view.", throwable);
                                    });



                }


                return false;
            }
        });


    }


 }
