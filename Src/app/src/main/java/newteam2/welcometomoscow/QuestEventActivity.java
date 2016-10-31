package newteam2.welcometomoscow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class QuestEventActivity extends AppCompatActivity {
    private QuestEvent currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_event);

        // To avoid: http://www.developerphil.com/dont-store-data-in-the-application-object/
        // Get quest info, from choose quest activity
        MainApplication app = (MainApplication) getApplication();
        currentEvent = app.getCurrentQuestEvent();
        if (currentEvent == null) {
            // There is no event, just go back to "map"
            finish();
            return;
        }

        TextView event_text = (TextView) findViewById( R.id.event_text );
        event_text.setText(currentEvent.text);
    }


    public void ButtonClickBackToMap(View view) {
        // finish this activity (just showing info) and get back to map
        finish();
    }
}
