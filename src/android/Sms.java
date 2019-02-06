package com.jsmobile.plugins.sms;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.widget.Toast;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.os.Bundle ;
import android.net.Uri ;
import android.content.ContentResolver ;
import android.database.Cursor ;


import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * This class echoes a string called from JavaScript.
 */
//Changed from CDNsms to sms
public class Sms extends CordovaPlugin {
    private static final String SMS_GENERAL_ERROR = "SMS_GENERAL_ERROR";
    private static final String NO_SMS_SERVICE_AVAILABLE = "NO_SMS_SERVICE_AVAILABLE";
    private static final String SMS_FEATURE_NOT_SUPPORTED = "SMS_FEATURE_NOT_SUPPORTED";
    private static final String SENDING_SMS_ID = "SENDING_SMS";
    private static final String SMS_SENT = "SMS_SENT";
    private static final String SMS_DELIVERED = "SMS_DELIVERED";
    private static final String SMS_NOT_DELIVERED = "SMS_NOT_DELIVERED";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("sendMessage")) {
            String phoneNumber = args.getString(0);
            String message = args.getString(1);
            String index = args.getString(2);
            
            boolean isSupported = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
            
            if (! isSupported) {
                JSONObject errorObject = new JSONObject();
                
                errorObject.put("code", SMS_FEATURE_NOT_SUPPORTED);
                errorObject.put("message", "SMS feature is not supported on this device");
                
                callbackContext.sendPluginResult(new PluginResult(Status.ERROR, errorObject));
                return false;
            }
            
            this.sendSMS(phoneNumber, message, index, callbackContext);
            
            return true;
        }
        
        return false;
    }

    private String getId( Intent intent )
    {

        Bundle bundle = intent.getExtras();

        String uri = intent.getStringExtra("uri") ;
        String str[] = uri.split("/") ;

        List<String> uriArray = new ArrayList<String>();
        uriArray = Arrays.asList(str);

        String _id = uriArray.get( uriArray.size() - 1 ) ;

        return _id ;

    }

    private void sendSMS(String phoneNumber, String message, final String index, final CallbackContext callbackContext) throws JSONException {

        PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SENDING_SMS_ID + index ), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SMS_DELIVERED + index ), 0);


        // For when the SMS has been sent
        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                case Activity.RESULT_OK:
                    try {

                        JSONObject resultObject = new JSONObject();

                        String _id = Sms.this.getId( intent ) ;
                        resultObject.put("_id", _id );
                        resultObject.put("code", SMS_SENT );
                        resultObject.put("index", index );
                        resultObject.put("message", "SMS message is sent successfully");

                        PluginResult pluginResult = new PluginResult(Status.OK, resultObject); 
                        pluginResult.setKeepCallback( true ) ;
                        callbackContext.sendPluginResult( pluginResult );

                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    try {
                        JSONObject errorObject = new JSONObject();
                        
                        errorObject.put("code", NO_SMS_SERVICE_AVAILABLE);
                        errorObject.put("index", index );
                        errorObject.put("message", "SMS is not sent because no service is available");
                        
                        callbackContext.sendPluginResult(new PluginResult(Status.ERROR, errorObject));   
                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }
                    break;
                default:
                    try {
                        JSONObject errorObject = new JSONObject();
                        
                        errorObject.put("code", SMS_GENERAL_ERROR);
                        errorObject.put("message", "SMS general error");
                        errorObject.put("index", index );
                        
                        callbackContext.sendPluginResult(new PluginResult(Status.ERROR, errorObject));
                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }
                    
                    break;
                }
            }
        }, new IntentFilter(SENDING_SMS_ID + index ));


        // For when the SMS has been delivered
        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        try {

                            JSONObject resultObject = new JSONObject();
                            
                            resultObject.put("code", SMS_DELIVERED  );
                            resultObject.put("index", index );

                            resultObject.put("message", "SMS message is delivered successfully");

                            PluginResult pluginResult = new PluginResult(Status.OK, resultObject); 
                            pluginResult.setKeepCallback( true ) ;
                            callbackContext.sendPluginResult( pluginResult );
                            
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getActivity().getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject errorObject = new JSONObject();
                            
                            errorObject.put("code", SMS_NOT_DELIVERED);
                            errorObject.put("index", index );
                            errorObject.put("message", "SMS has not been delivered.");
                            
                            callbackContext.sendPluginResult(new PluginResult(Status.ERROR, errorObject));   
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                        break;
                }
            }
        }, new IntentFilter(SMS_DELIVERED + index ));

        SmsManager smsManager = SmsManager.getDefault();

        ArrayList<String> smsBodyParts = smsManager.divideMessage( message ) ;
        ArrayList<PendingIntent> sentPIs = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPIs = new ArrayList<PendingIntent>();
         
        for (int i = 0; i < smsBodyParts.size(); i++) {
            sentPIs.add(sentPI);
            deliveredPIs.add(deliveredPI);
        }

        smsManager.sendMultipartTextMessage(phoneNumber, null, smsBodyParts, sentPIs, deliveredPIs ) ;
    }
    
    private Activity getActivity() {
        return this.cordova.getActivity();
    }
}
