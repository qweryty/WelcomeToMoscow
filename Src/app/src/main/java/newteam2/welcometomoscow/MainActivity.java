package newteam2.welcometomoscow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity{
    QuestsListFragment questsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questsListFragment = (QuestsListFragment) getFragmentManager().findFragmentById(R.id.quests_list_fragment);
    }

    public void ButtonClickViewMap(View view) {
        questsListFragment.ButtonClickViewMap(view);
        // start activity
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }
}
