package com.example.nishant.resume;

import android.util.Log;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;


public class CustomArFragment extends ArFragment {

    @Override
    protected Config getSessionConfiguration(Session session){

        Log.d("setupdb","2");
        getPlaneDiscoveryController().setInstructionView(null); // hiding hand gesture


        Config config = new Config(session);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        session.configure(config);
        this.getArSceneView().setupSession(session);

        Log.d("setupdb","1");

        if (((MainActivity)getActivity()).setupAugmentedImageDatabase(config,session)){
            Log.d("setupdb","success");
        }
        else{
            Log.d("setupdb","fail");
        }

        return config;
    }


}
