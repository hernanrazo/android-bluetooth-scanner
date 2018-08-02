package hernanrazo.bluetoothdemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private final int BLUETOOTH_REQUEST = 1;
    private final int LOCATION_REQUEST = 2;
    private final int REQUEST_DISCOVERABLE = 3;
    Button onBtn;
    Button offBtn;
    Button disconnect;
    ListView deviceList;




    //function for requesting location permission
    public boolean locationPermissionCheck() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            //show explanation for allowing permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this).setTitle("Locations Permission Needed")
                        .setMessage("Locations permission needed to continue")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        LOCATION_REQUEST);
                            }
                        }).create().show();

                //request permission without explanation
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        switch (requestCode) {

            case LOCATION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {

                    //permission is granted, continue with functionality
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(this, "Location Permission granted.", Toast.LENGTH_SHORT).show();
                    }

                    //permission denied, display toast
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

        //initialize buttons and listview
        onBtn = findViewById(R.id.onBtn);
        offBtn = findViewById(R.id.offBtn);
        disconnect = findViewById(R.id.disconnect);
        deviceList = findViewById(R.id.deviceList);

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

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

                //call location permission access
                locationPermissionCheck();
            }
        }

        //set listeners for when  buttons are pressed

        //turn on bluetooth
        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(btAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(intent, BLUETOOTH_REQUEST);
            }
        });

        //turn off bluetooth
        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btAdapter.disable();
            }
        });

        //make device discoverable
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!btAdapter.isDiscovering()) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVERABLE);
                }
            }
        });

        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = deviceList.getItemAtPosition(position);

                Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
                ArrayList<String> devices = new ArrayList<>();


                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice bt : pairedDevices) {

                        devices.add(bt.getName());
                    }

                    ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, devices);
                    deviceList.setAdapter(arrayAdapter);
                }
            }

        });
    }
}