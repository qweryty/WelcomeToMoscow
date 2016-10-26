package newteam2.welcometomoscow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView chooseQuestList;
    private QuestInfo selectedQuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find the ListView resource.
        chooseQuestList = (ListView) findViewById( R.id.choose_quest );

        final ArrayList<QuestInfo> values = getQuestInfoFromServer();
        final QuestListAdapter adapter = new QuestListAdapter(this, values);
        chooseQuestList.setAdapter(adapter);

        // add some random action
        chooseQuestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                 onQuestItemClick(parent, view, position, id);
            }
        });
    }

    private void onQuestItemClick(AdapterView<?> parent, final View view, int position, long id) {
        selectedQuest = (QuestInfo) parent.getItemAtPosition(position);
        // show extra info about quest
        LinearLayout quest_info = (LinearLayout) findViewById( R.id.quest_all_info );
        TextView quest_long_info = (TextView) quest_info.findViewById( R.id.quest_text_describe );
        quest_long_info.setText( selectedQuest.info_long );
    }

    private ArrayList<QuestInfo> getQuestInfoFromServer() {
        ArrayList<QuestInfo> data = new ArrayList<>();
        data.add(new QuestInfo( "Moscow", "Long and difficult", "Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 21 ));
        data.add(new QuestInfo( "Linux", "You get to cry", "perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 32 ));
        data.add(new QuestInfo( "Android", "But it gets better", "omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 29));
        data.add(new QuestInfo( "new Moscow(2)", "Some more filler text", "iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 44));
        data.add(new QuestInfo( "Test scrolling 1", "Not much to say 1", "sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 43));
        data.add(new QuestInfo( "Test scrolling 2", "Not much to say 2", "voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher , 47));
        data.add(new QuestInfo( "Test scrolling 3", "Not much to say 3", "accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 53 ));
        data.add(new QuestInfo( "Test scrolling 4", "Not much to say 4", "totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 523));
        data.add(new QuestInfo( "Test scrolling 5", "Not much to say 5", ", quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 528));
        data.add(new QuestInfo( "Test scrolling 6", "Not much to say 6", ", quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 531));
        data.add(new QuestInfo( "Test scrolling 6", "Not much to say 6", ", quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 438));
        return data;
    }

    public void ButtonClickViewMap(View view) {
        if (selectedQuest == null) {
            return;
        }
        // Get ready to share quest info
        MainApplication app = (MainApplication) getApplication();
        app.setCurrentQuestInfo(selectedQuest);
        // start activity
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

}
