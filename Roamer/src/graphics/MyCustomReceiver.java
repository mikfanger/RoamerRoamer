package graphics;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyCustomReceiver extends BroadcastReceiver {
private static final String TAG = "MyCustomReceiver";
 
  @Override
  public void onReceive(Context context, Intent intent) {
    try {
      String action = intent.getAction();
      String channel = intent.getExtras().getString("com.parse.Channel");
      JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
 
      Iterator itr = json.keys();
      while (itr.hasNext()) {
        String key = (String) itr.next();
        System.out.println("..." + key + " => " + json.getString(key));
      }
    } catch (JSONException e) {
      System.out.println(e);
    }
  }
}