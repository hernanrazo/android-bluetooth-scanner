package hernanrazo.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int BLUETOOTH_REQUEST = 1;
    private int LOCATION_REQUEST = 2;

    //TODO: request location permission



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
    }


}
