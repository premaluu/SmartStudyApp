package com.amitvikram.smartstudyapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Instruction extends AppCompatDialogFragment {

    @NonNull
    @Override
    public  Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder build =new AlertDialog.Builder(getActivity());
        build.setTitle("Instruction To Display")
                .setMessage("1.Shake your device for detecting Surface."+"\n"+"2.Wait till model downloaded from online storage"
                        +"\n"+"3.Once 'Model downloaded' message displayed tap on the dots."
                        +"\n"+"4.Congratulations!! Model displayed succesfully")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
return build.create();
    }


}
