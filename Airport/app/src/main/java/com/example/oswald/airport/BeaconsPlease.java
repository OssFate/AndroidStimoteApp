import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.List;


public class BeaconsPlease extends Activity {


    private int scanInterval;
    private int scanPause;
    private double dist_a, dist_b, dist_c;

        beaconManager = new BeaconManager(this);
        beaconManager.setBackgroundScanPeriod(scanInterval, scanPause);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {

            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {

                for (Beacon beacon : beacons) {
                    double accuraccy = Utils.computeAccuracy(beacon);

                    if (region.getIdentifier().equals(MINT)) {
                        //imprimir distancia a mint
                        dist_a = accuraccy; //??
                    } else if (region.getIdentifier().equals(OSSBB)) {
                        //imprimir distancia a OssBB
                        dist_b = accuraccy; //??
                    } else if (region.getIdentifier().equals(ICE)) {
                        //imprimir distancia a ice
                        dist_c = accuraccy; //??
                    }

                    updatePosition(dist_a,dist_b,dist_c);
                }
            }
        });

    }

    private void initFromPreferences() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        scanInterval = Integer.valueOf(prefs.getString(getString(R.string.pref_key_scan_interval), Defaults.SCAN_INTERVAL));
        scanPause = Integer.valueOf(prefs.getString(getString(R.string.pref_key_scan_pause), Defaults.SCAN_PAUSE));


        mintRegion = new Region(MINT, prefs.getString(getString(R.string.pref_key_mint_uuid), Defaults.MINT_UUID),
                Integer.valueOf(prefs.getString(getString(R.string.pref_key_mint_major), Defaults.MINT_MAJOR)),
                Integer.valueOf(prefs.getString(getString(R.string.pref_key_mint_minor), Defaults.MINT_MINOR)));

        ossbbRegion = new Region(OSSBB, prefs.getString(getString(R.string.pref_key_ossbb_uuid), Defaults.OSSBB_UUID),
                Integer.valueOf(prefs.getString(getString(R.string.pref_key_ossbb_major), Defaults.OSSBB_MAJOR)),
                Integer.valueOf(prefs.getString(getString(R.string.pref_key_ossbb_minor), Defaults.OSSBB_MINOR)));

        iceRegion = new Region(ICE, prefs.getString(getString(R.string.pref_key_ice_uuid), Defaults.ICE_UUID),
                Integer.valueOf(prefs.getString(getString(R.string.pref_key_ice_major), Defaults.ICE_MAJOR)),
                Integer.valueOf(prefs.getString(getString(R.string.pref_key_ice_minor), Defaults.ICE_MINOR)));

    }

    private void updatePosition(double dist_a, dist_b, dist_c) {
        //triangulacion con 3 datos de distancias
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFromPreferences();
        startRanging();
    }

    @Override
    protected void onPause() {
        stopRanging();
        super.onPause();
    }

    private void stopRanging() {
        try {
            beaconManager.stopRanging(mintRegion);
            beaconManager.stopRanging(ossbbRegion);
            beaconManager.stopRanging(iceRegion);
        } catch (RemoteException e) {
            Log.e("E", "Cannot stop ranging butt fuck it", e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startRanging();
    }

    private void startRanging() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(mintRegion);
                    beaconManager.startRanging(ossbbRegion);
                    beaconManager.startRanging(iceRegion);
                } catch (RemoteException e) {
                    Log.e("E", "Cannot start ranging", e);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        stopRanging();
        super.onStop();
    }

}