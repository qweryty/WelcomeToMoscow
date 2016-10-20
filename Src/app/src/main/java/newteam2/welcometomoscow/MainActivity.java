package newteam2.welcometomoscow;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

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
        final QuestMenuEntryAdapter adapter = new QuestMenuEntryAdapter(this, values);
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
        data.add(new QuestInfo( "Moscow", "Long and difficult", "Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher ));
        data.add(new QuestInfo( "Linux", "You get to cry", "perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher ));
        data.add(new QuestInfo( "Android", "But it gets better", "omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher ));
        data.add(new QuestInfo( "new Moscow(2)", "Some more filler text", "iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher ));
        data.add(new QuestInfo( "Test scrolling 1", "Not much to say 1", "sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher ));
        data.add(new QuestInfo( "Test scrolling 2", "Not much to say 2", "voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher ));
        data.add(new QuestInfo( "Test scrolling 3", "Not much to say 3", "accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher ));
        data.add(new QuestInfo( "Test scrolling 4", "Not much to say 4", "totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher ));
        data.add(new QuestInfo( "Test scrolling 5", "Not much to say 5", ", quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher ));
        data.add(new QuestInfo( "Test scrolling 6", "Not much to say 6", ", quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher ));
        data.add(new QuestInfo( "Test scrolling 6", "Not much to say 6", ", quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher ));
        return data;
    }

    public void ButtonClickViewMap(View view) {
        if (selectedQuest == null) {
            return;
        }
        Intent i = new Intent(this, MapsActivity.class);
        i.putExtra("quest_name", selectedQuest.name);
        startActivity(i);
    }

}
