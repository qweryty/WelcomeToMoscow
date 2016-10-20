package newteam2.welcometomoscow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;

public class DialogToEnableGPS extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Question to display
        builder.setMessage(R.string.ask_gps_dialog);
        // User accepted the dialog
        builder.setPositiveButton(R.string.answer_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        // User cancelled the dialog
        builder.setNegativeButton(R.string.answer_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // nothing to do, just continue as normal
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
