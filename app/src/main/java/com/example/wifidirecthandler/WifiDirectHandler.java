package com.example.wifidirecthandler;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.net.wifi.p2p.nsd.WifiP2pServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Looper.getMainLooper;

/**
 * Created by rinnaris on 2017-02-19.
 */

public class WifiDirectHandler {
    //extra stuff
    //stuff for service discovery
    private static final int SERVER_PORT = 20000;
    private String infoString;
    final HashMap<String, String> buddies = new HashMap<String, String>();
    private WifiP2pServiceRequest serviceRequest;

    //from main activity
    private Context context;
    private MainActivity mActivity;

    //wifip2pstuff
    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiDirectBroadCastReciever receiver;

    //this is terrifying but may solve a problem i dunno
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

    //so is this
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
    //this too
    WifiP2pManager.ActionListener serviceActionListener = new WifiP2pManager.ActionListener() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailure(int reason) {

        }
    };

    public WifiDirectHandler(Context contextIn, WifiP2pManager manager, String profile, MainActivity mActivityIn) {

        context = contextIn;
        mActivity = mActivityIn;

        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = manager;
        mChannel = mManager.initialize(context, getMainLooper(), null);

        handlerRegisterReciever();
        boolean ignoreThisItsJustAWayOfWaitingUntilThisMethodIsDone = setupInfoString(profile);
        startRegistration();
    }

    /*context should be ".this" from main activity,
    //system service is "(WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE)"
    //from mainactivity*/
    public void WifiDirectHandler(Context contextIn, WifiP2pManager systemService){

        context = contextIn;

        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = systemService;
        mChannel = mManager.initialize(context, getMainLooper(), null);

        handlerRegisterReciever();
    }

    public void handlerRegisterReciever(){
        receiver = new WifiDirectBroadCastReciever(mManager, mChannel, context);
        context.registerReceiver(receiver, intentFilter);
    }

    //call this in mainactivity's onDestroy()
    public void handlerUnRegisterReciever(){
        context.unregisterReceiver(receiver);
    }

    public void searchForPhonesOnce(){
        receiver.discover();
    }

    public List<WifiP2pDevice> getPeers(){
        return receiver.getPeers();
    }

    public String peersToString(){
        List<WifiP2pDevice> peers = getPeers();
        String out = "";
        for (WifiP2pDevice device: peers) {
            out += device.toString();
        }
        return out;
    }

    public void connectToPeer(WifiP2pDevice peer){
        receiver.connectTo(peer);
    }

    public void startRegistration() {

        //  Create a string map containing information about your service.
        Map record = new HashMap();
        record.put("listenport", String.valueOf(SERVER_PORT));
        record.put("buddyname", infoString);
        record.put("available", "visible");

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

    private boolean setupInfoString(String profile){
        infoString = "BuddyApp " + profile;
        System.out.println("PROFILE " + profile);
        return false;
    }


    //so this method is a nightmare. I don't suggest trying to understand it as long as it works
    //TODO this only works the first five or six times it is called for one phone, or maybe not now
    public void discoverService() {
        mManager.setDnsSdResponseListeners(mChannel, servListener, txtListener);
        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        mManager.clearServiceRequests(mChannel, serviceActionListener);
        mManager.addServiceRequest(mChannel, serviceRequest, serviceActionListener);
        mManager.discoverServices(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // Success!
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

    private void processBuddyName(String buddyname) {
        //if service name is for this app
        if(buddyname.contains("FNF")){
            //if app has not seen this service before
            if(!mActivity.previousProfiles.contains(buddyname)){
                mActivity.previousProfiles.add(buddyname);
                String out = buddyname;
                out = out.replace("BUDDYNAME", "");
                out = out.replace("FNF", "");
                setMainText(out);
            }
        }
    }

    private void setMainText(String out) {
        mActivity.text.setText(out);
    }

    public void services() {
        discoverService();
        //boolean ignorethis = prepareServiceDiscovery();
        //startServiceDiscovery();
    }

    public boolean prepareServiceDiscovery() {
        mManager.setDnsSdResponseListeners(mChannel,
                new WifiP2pManager.DnsSdServiceResponseListener() {

                    @Override
                    public void onDnsSdServiceAvailable(String instanceName,
                                                        String registrationType, WifiP2pDevice srcDevice) {
                        // do all the things you need to do with detected service
                    }
                }, new WifiP2pManager.DnsSdTxtRecordListener() {

                    @Override
                    public void onDnsSdTxtRecordAvailable(
                            String fullDomainName, Map<String, String> record,
                            WifiP2pDevice device) {
                        // do all the things you need to do with detailed information about detected service
                    }
                });

        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        return true;
    }

    private void startServiceDiscovery() {
        mManager.removeServiceRequest(mChannel, serviceRequest,
                new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        mManager.addServiceRequest(mChannel, serviceRequest,
                                new WifiP2pManager.ActionListener() {

                                    @Override
                                    public void onSuccess() {
                                        mManager.discoverServices(mChannel,
                                                new WifiP2pManager.ActionListener() {

                                                    @Override
                                                    public void onSuccess() {
                                                        //service discovery started
                                                    }

                                                    @Override
                                                    public void onFailure(int error) {
                                                        // react to failure of starting service discovery
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onFailure(int error) {
                                        // react to failure of adding service request
                                    }
                                });
                    }

                    @Override
                    public void onFailure(int reason) {
                        // react to failure of removing service request
                    }
                });
    }

    public void setInfoString(String infoString) {
        this.infoString = infoString;
    }
}
