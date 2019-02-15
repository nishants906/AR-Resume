/*
 * Copyright 2018 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.nishant.resume;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * This is a simple example that shows how to create an augmented reality (AR) application using the
 * ARCore and Sceneform APIs.
 */
public class ResumeActivity extends AppCompatActivity {
    private static final int RC_PERMISSIONS = 0x123;
    private boolean installRequested;

    private GestureDetector gestureDetector;
    private Snackbar loadingMessageSnackbar = null;

    ArFragment arFragment;
    private ArSceneView arSceneView;

    private ModelRenderable sunRenderable;
    private ViewRenderable descriptiondata,descriptiondata1,descriptiondata2,descriptiondata3,resume,aboutme,fella,halanx,
            zoruk,wipro,taproom,escale,metr,twel,gradu,instructions;

    // True once scene is loaded
    private boolean hasFinishedLoading = false;

    // True once the scene has been placed.
    private boolean hasPlacedSolarSystem = false;

    // Astronomical units to meters ratio. Used for positioning the planets of the solar system.
    private static final float AU_TO_METERS = 0.5f;


    ImageView im;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!DemoUtils.checkIsSupportedDeviceOrFinish(this)) {
            // Not a supported device.
            return;
        }


        setContentView(R.layout.activity_solar);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_scene_view);
        arSceneView = arFragment.getArSceneView();

        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);


        try {
            Session session = DemoUtils.createArSession(this, installRequested);
            Config config = new Config(session);
            config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
            config.setPlaneFindingMode(Config.PlaneFindingMode.HORIZONTAL);
            session.configure(config);
            arSceneView.setupSession(session);



        } catch (UnavailableException e) {
            Log.e("erroroccur",e.toString());
        }







// To apply texture on plane rendering

//        Texture.Sampler sampler = Texture.Sampler.builder()
//                .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
//                .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
//
//                .setWrapMode(Texture.Sampler.WrapMode.REPEAT).build();
//
//        // Build texture with sampler
//        CompletableFuture<Texture> trigrid = Texture.builder()
//                .setSource(ResumeActivity.this,R.drawable.fill)
//                .setSampler(sampler).build();
//
//        // Set plane texture
//        this.arFragment.getArSceneView()
//                .getPlaneRenderer()
//                .getMaterial()
//                .thenAcceptBoth(trigrid, (material, texture) -> {
//                    material.setTexture(PlaneRenderer.MATERIAL_TEXTURE, texture);
//                })
//                .exceptionally(throwable -> {
//                    Log.d("enter1",throwable.getMessage());
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setMessage(throwable.getMessage()).setTitle("Error");
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                    return null;
//                });
//

        // Build all the planet models.
        CompletableFuture<ModelRenderable> sunStage = ModelRenderable.builder().setSource(this, Uri.parse("model__0.sfb")).build();
        CompletableFuture<ViewRenderable> descriptionstage = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> descriptionstage1 = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> descriptionstage2 = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> descriptionstage3 = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> about = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> fell = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> wipr = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> halan = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> zoru = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> escal = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> taproo = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> metric = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> twelth = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> grad = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> resume1 = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();
        CompletableFuture<ViewRenderable> instruction = ViewRenderable.builder().setView(this, R.layout.planet_card_view).build();

        CompletableFuture.allOf(sunStage,descriptionstage,descriptionstage1,descriptionstage2,resume1).handle((notUsed, throwable) -> {
            // When you build a Renderable, Sceneform loads its resources in the background while
            // returning a CompletableFuture. Call handle(), thenAccept(), or check isDone()
            // before calling get().

            if (throwable != null) {
                DemoUtils.displayError(this, "Unable to load renderable", throwable);
                return null;
            }

            try {
                sunRenderable = sunStage.get();
                descriptiondata = descriptionstage.get();
                descriptiondata1 = descriptionstage1.get();
                descriptiondata2 = descriptionstage2.get();
                descriptiondata3 = descriptionstage3.get();
                wipro = wipr.get();
                halanx = halan.get();
                fella = fell.get();
                taproom = taproo.get();
                escale = escal.get();
                zoruk = zoru.get();
                metr = metric.get();
                twel = twelth.get();
                gradu = grad.get();
                aboutme = about.get();
                resume = resume1.get();
                instructions = instruction.get();


                // Everything finished loading successfully.
                hasFinishedLoading = true;

            } catch (InterruptedException | ExecutionException ex) {
                DemoUtils.displayError(this, "Unable to load renderable", ex);
            }

            return null;
        });

        // Set up a tap gesture detector.
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onSingleTap(e);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });


        // Set a touch listener on the Scene to listen for taps.
        arSceneView.getScene().setOnTouchListener((HitTestResult hitTestResult, MotionEvent event) -> {
            // If the solar system hasn't been placed yet, detect a tap and then check to see if
            // the tap occurred on an ARCore plane to place the solar system.
            if (!hasPlacedSolarSystem) {
                return gestureDetector.onTouchEvent(event);
            }

            // Otherwise return false so that the touch event can propagate to the scene.
            return false;
        });

        // Set an update listener on the Scene that will hide the loading message once a Plane is
        // detected.
        arSceneView.getScene().addOnUpdateListener(frameTime -> {
            if (loadingMessageSnackbar == null) {
                return;
            }

            Frame frame = arSceneView.getArFrame();
            if (frame == null) {
                return;
            }

            if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
                return;
            }

            for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
                if (plane.getTrackingState() == TrackingState.TRACKING) {
                    hideLoadingMessage();
                }
            }
        });

        // Lastly request CAMERA permission which is required by ARCore.
        DemoUtils.requestCameraPermission(this, RC_PERMISSIONS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (arSceneView == null) {
            return;
        }

        if (arSceneView.getSession() == null) {
            // If the session wasn't created yet, don't resume rendering.
            // This can happen if ARCore needs to be updated or permissions are not granted yet.
            try {
                Session session = DemoUtils.createArSession(this, installRequested);
                if (session == null) {
                    installRequested = DemoUtils.hasCameraPermission(this);
                    return;
                } else {
                    arSceneView.setupSession(session);
                }
            } catch (UnavailableException e) {
                DemoUtils.handleSessionException(this, e);
            }
        }

        try {
            arSceneView.resume();
        } catch (CameraNotAvailableException ex) {
            DemoUtils.displayError(this, "Unable to get camera", ex);
            finish();
            return;
        }

        if (arSceneView.getSession() != null) {
            showLoadingMessage();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (arSceneView != null) {
            arSceneView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (arSceneView != null) {
            arSceneView.destroy();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        if (!DemoUtils.hasCameraPermission(this)) {
            if (!DemoUtils.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                DemoUtils.launchPermissionSettings(this);
            } else {
                Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Standard Android full-screen functionality.
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onSingleTap(MotionEvent tap) {
        if (!hasFinishedLoading) {
            // We can't do anything yet.
            return;
        }

        Frame frame = arSceneView.getArFrame();
        if (frame != null) {
            if (!hasPlacedSolarSystem && tryPlaceSolarSystem(tap, frame)) {
                hasPlacedSolarSystem = true;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean tryPlaceSolarSystem(MotionEvent tap, Frame frame) {
        if (tap != null && frame.getCamera().getTrackingState() == TrackingState.TRACKING) {
            for (HitResult hit : frame.hitTest(tap)) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    // Create the Anchor.
                    Anchor anchor = hit.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arSceneView.getScene());
                    Node solarSystem = createSolarSystem();
                    anchorNode.addChild(solarSystem);
                    return true;
                }
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Node createSolarSystem() {
        Node base = new Node();

        Node sun = new Node();
        sun.setParent(base);
        sun.setLocalPosition(new Vector3(0.0f, 0.5f, 0.0f));

        Node sunVisual = new Node();
        sunVisual.setParent(sun);
        sunVisual.setRenderable(sunRenderable);

        sunVisual.setLocalPosition(new Vector3(-0.1f,0.0f,0.0f));
        Quaternion q1 = sunVisual.getWorldRotation();
        Quaternion q2 = new Quaternion(180,150,150,-150);
        sunVisual.setLocalRotation(Quaternion.multiply(q1, q2));

        sunVisual.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));

        Node sc = createNode(String.valueOf(Html.fromHtml("<b>About</b>")),sun,0.5f,descriptiondata,0,14);
        sc.setLocalPosition(new Vector3(0.0f,0.45f * AU_TO_METERS, 0.0f));

        Node s1 = createNode(String.valueOf(Html.fromHtml("<b>Education</b>")),sun,0.5f,descriptiondata1,0,14);
        s1.setLocalPosition(new Vector3(0.55f * AU_TO_METERS, 0.0f, 0.0f));

        Node s2 = createNode("Work \n Experience",sun,0.5f,descriptiondata2,0,14);
        s2.setLocalPosition(new Vector3(-0.67f * AU_TO_METERS, -0.05f, 0.0f));

        Node s3 = createNode("Resume",sun,0.5f,descriptiondata3,0,14);
        s3.setLocalPosition(new Vector3(0.0f,-0.7f * AU_TO_METERS, 0.0f));

        sc.setEnabled(false);
        s1.setEnabled(false);
        s2.setEnabled(false);
        s3.setEnabled(false);


        Node s4 = createNode("",s3,0.5f,resume,0,14);
        s4.setLocalPosition(new Vector3(-0.0f,-2f,2.5f * AU_TO_METERS));
        s4.setEnabled(false);

        im = resume.getView().findViewById(R.id.im_pic);
        im.setImageResource(R.drawable.res);

        Quaternion q3 = s4.getLocalRotation();
        Quaternion q4 = new Quaternion(new Vector3(-0.5f,0f,0f),100);
        s4.setLocalRotation(Quaternion.multiply(q3, q4));

        Node s5 = createNode("Nishant Sardana \n 9711556178",sc,0.5f,aboutme,0,10);
        ImageView im = (ImageView) aboutme.getView().findViewById(R.id.im_pic);
        im.setImageResource(R.drawable.self);
        s5.setEnabled(false);
        s5.setLocalPosition(new Vector3(0.0f,0.2f, 0.0f));



        Node c1 = createNode("",s2,0.05f,wipro,0,14);
        c1.setLocalScale(new Vector3(0.1f,0.1f,0.1f));
        c1.setLocalPosition(new Vector3(-0.03f,0.25f,0));
        im = wipro.getView().findViewById(R.id.im_pic);
        im.setImageResource(R.drawable.wipro);



        Node c2 = createNode("",s2,0.05f,escale,0,14);
        c2.setLocalScale(new Vector3(0.11f,0.11f,0.11f));
        c2.setLocalPosition(new Vector3(0.15f,0.2f,0));
        im = escale.getView().findViewById(R.id.im_pic);
        im.setImageResource(R.drawable.escale);


        Node c3 = createNode("",s2,0.05f,fella,0,14);
        c3.setLocalScale(new Vector3(0.12f,0.12f,0.12f));
        c3.setLocalPosition(new Vector3(-0.23f,0.18f,0));
        im = fella.getView().findViewById(R.id.im_pic);
        im.setImageResource(R.drawable.fella);



        Node c4 = createNode("",s2,0.05f,taproom,0,14);
        c4.setLocalScale(new Vector3(0.2f,0.2f,0.2f));
        c4.setLocalPosition(new Vector3(-0.20f,-0.07f,0));
        im = taproom.getView().findViewById(R.id.im_pic);
        im.setImageResource(R.drawable.taproom);



        Node c5 = createNode("",s2,0.05f,zoruk,0,14);
        c5.setLocalScale(new Vector3(0.1f,0.1f,0.1f));
        c5.setLocalPosition(new Vector3(0.18f,-0.11f,0));
        im = zoruk.getView().findViewById(R.id.im_pic);
        im.setImageResource(R.drawable.zoruk);



        Node c6 = createNode("",s2,0.05f,halanx,0,14);
        c6.setLocalScale(new Vector3(0.1f,0.1f,0.1f));
        c6.setLocalPosition(new Vector3(0.025f,-0.29f,0));
        im =  halanx.getView().findViewById(R.id.im_pic);
        im.setImageResource(R.drawable.halanx);

        c1.setEnabled(false);
        c2.setEnabled(false);
        c3.setEnabled(false);
        c4.setEnabled(false);
        c5.setEnabled(false);
        c6.setEnabled(false);


        Node met = createNode("Class 10th\n - 9.4 CGPA",s1,0.05f,metr,0,10);
        met.setLocalScale(new Vector3(1f,1f,2f));
        met.setLocalPosition(new Vector3(0.25f,0.25f,0.2f));


        Node twelve = createNode("Class 12th\n - 91 %",s1,0.05f,twel,0,10);
        met.setLocalScale(new Vector3(1f,1f,2f));
        twelve.setLocalPosition(new Vector3(0.35f,0f,0.2f));


        Node graduation = createNode("Graduation\n (B.Tech)\n - 8.6 CGPA",s1,0.05f,gradu,0,10);
        met.setLocalScale(new Vector3(1f,1,2f));
        graduation.setLocalPosition(new Vector3(0.25f,-0.25f,0.2f));

        met.setEnabled(false);
        twelve.setEnabled(false);
        graduation.setEnabled(false);


        sun.setOnTapListener((hitTestResult, motionEvent) ->{
            sc.setEnabled(!sc.isEnabled());
            s1.setEnabled(!s1.isEnabled());
            s2.setEnabled(!s2.isEnabled());
            s3.setEnabled(!s3.isEnabled());
        });


        sc.setOnTapListener((hitTestResult, motionEvent) -> s5.setEnabled(!s5.isEnabled()));
        s3.setOnTapListener((hitTestResult, motionEvent) -> s4.setEnabled(!s4.isEnabled()));
        s2.setOnTapListener((hitTestResult, motionEvent) ->{

            c1.setEnabled(!c1.isEnabled());
            c2.setEnabled(!c2.isEnabled());
            c3.setEnabled(!c3.isEnabled());
            c4.setEnabled(!c4.isEnabled());
            c5.setEnabled(!c5.isEnabled());
            c6.setEnabled(!c6.isEnabled());


        } );

        s1.setOnTapListener((hitTestResult, motionEvent) -> {
            met.setEnabled(!met.isEnabled());
            twelve.setEnabled(!twelve.isEnabled());
            graduation.setEnabled(!graduation.isEnabled());

        });

        return base;
    }







    @RequiresApi(api = Build.VERSION_CODES.N)
    private Node createNode(String name, Node parent, float auFromParent, ViewRenderable renderable,int pos,float textsize) {
        // Orbit is a rotating node with no renderable positioned at the sun.
        // The planet is positioned relative to the orbit so that it appears to rotate around the sun.
        // This is done instead of making the sun rotate so each planet can orbit at its own speed.

//        RotatingNode orbit = new RotatingNode(solarSettings, true);
//        orbit.setDegreesPerSecond(orbitDegreesPerSecond);
//        orbit.setParent(parent);

        // Create the planet and position it relative to the sun.

        Node node = new Node();
        node.setParent(parent);
        node.setRenderable(renderable);

        View v = renderable.getView();
        TextView textView = v.findViewById(R.id.planetInfoCard);
        textView.setVisibility(View.VISIBLE);
        textView.setTextSize(textsize);

        textView.setText(name);

        if (name.equals("")){
            textView.setVisibility(View.GONE);
        }


        return node;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Node createrotatingnode(String name, Node parent, float auFromParent, int orbitDegreesPerSecond,ViewRenderable renderable,int pos) {
        // Orbit is a rotating node with no renderable positioned at the sun.
        // The planet is positioned relative to the orbit so that it appears to rotate around the sun.
        // This is done instead of making the sun rotate so each planet can orbit at its own speed.

        RotatingNode orbit = new RotatingNode( true);
        orbit.setDegreesPerSecond(orbitDegreesPerSecond);
        orbit.setParent(parent);

        // Create the planet and position it relative to the sun.

        Node node = new Node();
        node.setParent(orbit);
        node.setRenderable(renderable);

//        View v = renderable.getView();
//        TextView textView = v.findViewById(R.id.planetInfoCard);
//        textView.setText(name);


        return node;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private TransformableNode createtranformablenode(String name, Node parent, float auFromParent, ViewRenderable renderable,int pos) {
        // Orbit is a rotating node with no renderable positioned at the sun.
        // The planet is positioned relative to the orbit so that it appears to rotate around the sun.
        // This is done instead of making the sun rotate so each planet can orbit at its own speed.

//        RotatingNode orbit = new RotatingNode(solarSettings, true);
//        orbit.setDegreesPerSecond(orbitDegreesPerSecond);
//        orbit.setParent(parent);

        // Create the planet and position it relative to the sun.

        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(parent);
        node.setRenderable(renderable);


        ImageView textView = (ImageView) renderable.getView();
        textView.setImageResource(R.drawable.res);
        textView.setVisibility(View.VISIBLE);
//        Quaternion q1 = sunVisual.getWorldRotation();
//        Quaternion q2 = new Quaternion(180,150,150,-150);
//        // last one for direction with head
//        //
//        sunVisual.setLocalRotation(Quaternion.multiply(q1, q2));

//        node.setLocalPosition(new Vector3(0.0f,auFromParent * AU_TO_METERS, 0.0f));


        return node;
    }








    private void showLoadingMessage() {
        if (loadingMessageSnackbar != null && loadingMessageSnackbar.isShownOrQueued()) {
            return;
        }

        loadingMessageSnackbar = Snackbar.make(ResumeActivity.this.findViewById(android.R.id.content), "Finding plane", Snackbar.LENGTH_INDEFINITE);
        loadingMessageSnackbar.getView().setBackgroundColor(0xbf323232);
        loadingMessageSnackbar.show();
    }

    private void hideLoadingMessage() {
        if (loadingMessageSnackbar == null) {
            return;
        }

        loadingMessageSnackbar.dismiss();
        loadingMessageSnackbar = null;
    }
}
