package com.example.wifidirecthandler;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Looper.getMainLooper;

/**
 * Created by rinnaris on 2017-02-19.
 */

public class WifiDirectHandler {

    //Global Variables
    //********************************************************

    //service discovery variables
    //********************************************************
    private static final int SERVER_PORT = 20000; //server port is an arbitrary int >10000
    final HashMap<String, String> buddies = new HashMap<String, String>(); //hashmap containing all nearby phone services
    private String serviceName; //the name of the service including control word and profile info
    private WifiP2pServiceRequest serviceRequest; //used for requesting services from other phones
    //********************************************************

    //Calling Activity Info
    //********************************************************
    private Context context; //context from mainactivity
    private MainActivity mActivity; //reference to mainactivity
    //********************************************************

    //Wifi P2P Variables
    //********************************************************
    private final IntentFilter intentFilter = new IntentFilter(); //intent filter to pass to the broadcast receiver
    private WifiP2pManager mManager; //wifi manager
    private WifiP2pManager.Channel mChannel; //don't know what channels are for but they're required
    private WifiDirectBroadCastReciever receiver; //the actual broadcast receiver
    //********************************************************

    //End Global Variables
    //********************************************************

    //********************************************************

    //Methods
    //Constructor
    //contextIn is context from creator activity
    //manager is a WifiP2Pmanager created in creator activity
    //profile is string representation of profile
    //mActivityIn is reference to the creator activity
    //TODO change profile from a string to a profile object
    public WifiDirectHandler(Context contextIn, WifiP2pManager manager, String profile, MainActivity mActivityIn) {

        //assigning Activity Info Variables
        context = contextIn;
        mActivity = mActivityIn;

        //assigning WifiP2P variables
        mManager = manager;
        mChannel = mManager.initialize(context, getMainLooper(), null);

        //Adding Actions to intentFilter needed for Wifi P2P things
        //*******************************************************************************
        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        //*******************************************************************************

        //calling set up methods
        //*******************************************************************************
        handlerRegisterReciever();
        boolean ignoreThisItsJustAWayOfWaitingUntilThisMethodIsDone = setupServiceName(profile);
        startRegistration();
        //*******************************************************************************
    }

    //one time call methods
    //************************************************************************************

    //registers the broadcast receiver call in activities on create or on resume method
    //on create for always running, on resume for only running when activity shown
    public void handlerRegisterReciever(){
        receiver = new WifiDirectBroadCastReciever(mManager, mChannel, context); //creates new receiver
        context.registerReceiver(receiver, intentFilter);   //registers it in main activitys context
    }


    //unregisters the receiver call this in onDestroy() or onPause()
    //same as above
    public void handlerUnRegisterReciever(){
        context.unregisterReceiver(receiver);
    }

    //***********************************************************************************

    //Methods not actually needed for basic functionality
    //************************************************************************************

    //searches for WifiP2P phones once
    //not actually needed for final app unless you want to connect for more data sharing
    public void searchForPhonesOnce(){
        receiver.discover();
    }

    //returns a list of nearby WifiP2P phones
    //same as before
    public List<WifiP2pDevice> getPeers(){
        return receiver.getPeers();
    }

    //creates a string containing info from all nearby WifiP2P phones
    //just for debugging
    public String peersToString(){
        List<WifiP2pDevice> peers = getPeers(); //get list of peers
        String out = ""; //create initial string
        for (WifiP2pDevice device: peers) {
            out += device.toString(); //add each peers info to the string
        }
        return out; //return the string
    }

    //connects to a given phone
    //again not needed for basic functions
    public void connectToPeer(WifiP2pDevice peer){
        receiver.connectTo(peer);
    }

    //***********************************************************************************

    //this is not a nice method, open at your own risk
    //creates a service with the profile information for other phones to find
    //only needs to be called once from this object, you shouldn't put a call to this anywhere else
    public void startRegistration() {

        //Creates a string map with information about service including serviceName
        Map record = new HashMap();
        record.put("listenport", String.valueOf(SERVER_PORT));
        record.put("buddyname", serviceName);
        record.put("available", "visible");

        //I don't know what this stuff does it was in the dev page
        //************************************************************************************

        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record);

        mManager.clearLocalServices(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int reason) {
            }
        });

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        mManager.addLocalService(mChannel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                // Unless you want to update the UI or add logging statements.
            }

            @Override
            public void onFailure(int arg0) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
            }
        });
    }

    //sets up serviceName string with profile info and control code
    //TODO update this to work with profile object
    private boolean setupServiceName(String profile){
        serviceName = "BuddyApp " + profile;
        return false;
    }


    //this method has actually given me nightmares, do yourself a favour and just pretend its not here
    //TODO this only works the first twelve times it is called for one phone, or maybe not now
    public void discoverService() {
        WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {
            @Override
        /* Callback includes:
         * fullDomain: full domain name: e.g "printer._ipp._tcp.local."
         * record: TXT record dta as a map of key/value pairs.
         * device: The device running the advertised service.
         */

            public void onDnsSdTxtRecordAvailable(String fullDomain, Map record, WifiP2pDevice device) {
                buddies.clear();
                buddies.put(device.deviceAddress, (String) record.get("buddyname"));
                processBuddyName((String)record.get("buddyname"));
                System.out.println("BUDDYNAME" + record.get("buddyname"));
            }
        };
        WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                                WifiP2pDevice resourceType) {

                // Update the device name with the human-friendly version from
                // the DnsTxtRecord, assuming one arrived.
                resourceType.deviceName = buddies
                        .containsKey(resourceType.deviceAddress) ? buddies
                        .get(resourceType.deviceAddress) : resourceType.deviceName;

                /*// Add to the custom adapter defined specifically for showing
                // wifi devices.
                WiFiDirectServicesList fragment = (WiFiDirectServicesList) getFragmentManager()
                        .findFragmentById(R.id.frag_peerlist);
                WiFiDevicesAdapter adapter = ((WiFiDevicesAdapter) fragment
                        .getListAdapter());

                adapter.add(resourceType);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "onBonjourServiceAvailable " + instanceName);*/
            }
        };
        mManager.setDnsSdResponseListeners(mChannel, servListener, txtListener);
        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        mManager.addServiceRequest(mChannel, serviceRequest, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        // Success!
                    }

                    @Override
                    public void onFailure(int code) {
                        // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                    }
                });
        mManager.discoverServices(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Success!
                System.out.println("servicesuccess");
            }

            @Override
            public void onFailure(int code) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                if (code == WifiP2pManager.P2P_UNSUPPORTED) {
                    System.out.println("P2P not supported");
                }
                else{
                    System.out.println("Something went wrong");
                }

                }});
    }

    //give it the name of a nearby service and it'll parse it
    //TODO Make this actually parse the string and return a profile object
    private void processBuddyName(String buddyname) {
        String out = buddyname;
        out.replace("BUDDYNAME", "");
        mActivity.text.setText(out);
        System.out.println(out);
    }

    //discovers nearby services
    //call this instead of discoverService for reasons I don't know
    public void services() {
        discoverService();
    }

    //this probably doesn't need to be called externally. Let this class handle it
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
