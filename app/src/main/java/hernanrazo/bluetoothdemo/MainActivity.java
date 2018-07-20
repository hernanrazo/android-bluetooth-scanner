package hernanrazo.bluetoothdemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int BLUETOOTH_REQUEST = 1;
    private final int LOCATION_REQUEST = 2;

    //TODO: request location permission

    public void locationPermission() {

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this).setTitle("Locations Permission Needed")
                        .setMessage("Locations permission needed to continue");
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionResult(int requestCode,
                                          String permissions[],
                                          int[] grantResults) {

        switch(requestCode) {

            case LOCATION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this,
                            "Locations permission needed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        //check if bluetooth is compatible on current device
        //if not, exit the app
        if (btAdapter == null) {

            new AlertDialog.Builder(this).setTitle("BlueTooth not Compatible")
                    .setMessage("This device does not support Bluetooth")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
        } else {

            //if bluetooth is compatible, ask the user to
            //enable bluetooth without leaving the app itself
            if (!btAdapter.isEnabled()) {

                Intent btEnableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(btEnableIntent, BLUETOOTH_REQUEST);
            }
        }
        locationPermission();
    }


}