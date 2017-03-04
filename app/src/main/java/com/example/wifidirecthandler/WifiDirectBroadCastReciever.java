package com.example.wifidirecthandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by rinnaris on 2017-02-19.
 */

public class WifiDirectBroadCastReciever extends BroadcastReceiver{
    private MainActivity mActivity;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private Context mContext;

    private ConnectionInfoListener connectionListener = new ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            // InetAddress from WifiP2pInfo struct.
            if(info.groupFormed){
                // communication stuff here
            }
        }
    };

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            System.out.println("Peers available");
            Collection<WifiP2pDevice> refreshedPeers = peerList.getDeviceList();
            if (!refreshedPeers.equals(peers)) {
                peers.clear();
                peers.addAll(refreshedPeers);
                //do anything else needed when there are new peers
            }

            if (peers.size() == 0) {
                System.out.println("No devices found");
            }
        }
    };
    public WifiDirectBroadCastReciever(WifiP2pManager mManagerIn, WifiP2pManager.Channel mChannelIn, Context contextIn) {
        mManager = mManagerIn;
        mChannel = mChannelIn;
        mContext = contextIn;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            //see if wifiP2P is enabled

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                //wifi enabled
                System.out.println("WifiP2P is Enabled");
            } else {
                //wifi not enabled
                System.out.println("WifiP2P is NOT Enabled");
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            //there are phones nearby
            if (mManager != null) {
                mManager.requestPeers(mChannel, peerListListener);
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (mManager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // We are connected with the other device, request connection
                // info to find group owner IP
                System.out.println("We have connection");


                mManager.requestConnectionInfo(mChannel, connectionListener);
            }

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            //yeah I don't know what this is for yet

        }
    }

    //begin checking for phones in a new thread
    public void discover() {
        System.out.println("discover");

        new Thread(new Runnable() {

            @Override
            public void run() {
                //your 1st command
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        //
                        System.out.println("Success");
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        //
                        System.out.println("Failure");
                    }
                });

            }
        }).start();
    }

    public List<WifiP2pDevice> getPeers() {
        return peers;
    }

    public void connectTo(WifiP2pDevice peer) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = peer.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(mContext, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
