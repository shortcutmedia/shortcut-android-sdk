package sc.shortcut.deeplinkingdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import sc.shortcut.deeplinkingdemo.common.MainFragment;
import sc.shortcut.sdk.Shortcut;


public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");

        Uri deepLinkFromSDK = Shortcut.getInstance().getDeepLink();
        if (deepLinkFromSDK != null) {
            Log.d(TAG, "opened with deep link (getDeepLink()): " + deepLinkFromSDK);
        }

        Uri deepLink = getIntent().getData();
        if (deepLink != null) {
            Log.d(TAG, "opened with deep link: " + deepLink);
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container, new MainFragment())
                .commit();
    }

    @Override
    public void onRequestStartWithDeepLinking() {
        Log.d(TAG, "onRequestStartWithDeepLinking ");

        Intent viewIntent = new Intent();
        viewIntent.setAction(Intent.ACTION_VIEW);
        viewIntent.setData(Uri.parse("scdemo://shortcut.sc/demo?sc_link_id=384729"));
        startActivity(viewIntent);
    }

    @Override
    public void onRequestAppRestart() {
        Log.d(TAG, "onRequestAppRestart ");
        startActivity(new Intent(this, this.getClass()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_go_to_2nd) {
            Intent i = new Intent(this, SecondActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
