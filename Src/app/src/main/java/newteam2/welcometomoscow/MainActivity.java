package newteam2.welcometomoscow;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ListView chooseQuestList;
    private QuestInfo selectedQuest;
    private PlayerData playerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find the ListView resource.
        chooseQuestList = (ListView) findViewById(R.id.choose_quest);

        final ArrayList<QuestInfo> values = new ArrayList<>();
        final QuestListAdapter adapter = new QuestListAdapter(this, values);
        final List<QuestEvent> quest_one = new ArrayList<>();
        GetApi getApi = GetData.getRetrofit().create(GetApi.class);
        Call<QuestInfo> obj = getApi.getData();
        obj.enqueue(new Callback<QuestInfo>() {
            @Override
            public void onResponse(Call<QuestInfo> call, Response<QuestInfo> response) {
                if (response.isSuccessful()) {
                    values.add(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Error1", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuestInfo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error2", Toast.LENGTH_SHORT).show();
            }
        });

        quest_one.add(new QuestEvent("You have reached Detsky Mir, now keep walking until the Teatr"
                , new LatLng(55.7602, 37.6251)
                , new QuestEventPredicate() {
            @Override
            public boolean test(PlayerData playerData) {
                LatLng detskyMirLocation = new LatLng(55.7602, 37.6251);
                float result[] = new float[1];
                Location.distanceBetween(playerData.latLng.latitude
                        , playerData.latLng.longitude
                        , detskyMirLocation.latitude
                        , detskyMirLocation.longitude
                        , result);
                // result has the distance in meters (if less than X meters, return true)
                return result[0] < 70;
            }
        }));
        quest_one.add(new QuestEvent("Bolshoi Teatr square, blabla blabla"
                , new LatLng(55.7592, 37.6190)
                , new QuestEventPredicate() {
            @Override
            public boolean test(PlayerData playerData) {
                LatLng detskyMirLocation = new LatLng(55.7592, 37.6190);
                float result[] = new float[1];
                Location.distanceBetween(playerData.latLng.latitude
                        , playerData.latLng.longitude
                        , detskyMirLocation.latitude
                        , detskyMirLocation.longitude
                        , result);
                // result has the distance in meters (if less than X meters, return true)
                return result[0] < 110;
            }
        }));

        values.add(new QuestInfo("Moscow"
                , "Long and difficult"
                , "Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas"
                , R.mipmap.ic_launcher
                , 21
                , quest_one));
        values.add(new QuestInfo("Linux", "You get to cry", "perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 32, new ArrayList<QuestEvent>()));
        values.add(new QuestInfo("Android", "But it gets better", "omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 29, new ArrayList<QuestEvent>()));
        values.add(new QuestInfo("new Moscow(2)", "Some more filler text", "iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 44, new ArrayList<QuestEvent>()));
        values.add(new QuestInfo("Test scrolling 1", "Not much to say 1", "sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 43, new ArrayList<QuestEvent>()));
        values.add(new QuestInfo("Test scrolling 2", "Not much to say 2", "voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 47, new ArrayList<QuestEvent>()));
        values.add(new QuestInfo("Test scrolling 3", "Not much to say 3", "accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 53, new ArrayList<QuestEvent>()));
        values.add(new QuestInfo("Test scrolling 4", "Not much to say 4", "totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 523, new ArrayList<QuestEvent>()));
        values.add(new QuestInfo("Test scrolling 5", "Not much to say 5", ", quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 528, new ArrayList<QuestEvent>()));
        values.add(new QuestInfo("Test scrolling 6", "Not much to say 6", ", quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 531, new ArrayList<QuestEvent>()));
        values.add(new QuestInfo("Test scrolling 6", "Not much to say 6", ", quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 438, new ArrayList<QuestEvent>()));
        chooseQuestList.setAdapter(adapter);

        // add some random action
        chooseQuestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                onQuestItemClick(parent, view, position, id);
            }
        });
        // make default player info, no name, no nothing
        playerData = new PlayerData();
    }

    private void onQuestItemClick(AdapterView<?> parent, final View view, int position, long id) {
        selectedQuest = (QuestInfo) parent.getItemAtPosition(position);
        // show extra info about quest
        LinearLayout quest_info = (LinearLayout) findViewById(R.id.quest_all_info);
        TextView quest_long_info = (TextView) quest_info.findViewById(R.id.quest_text_describe);
        quest_long_info.setText(selectedQuest.info_long);
    }

    public void ButtonClickViewMap(View view) {
        if (selectedQuest == null) {
            return;
        }
        // Get ready to share quest info
        MainApplication app = (MainApplication) getApplication();
        app.setCurrentQuestInfo(selectedQuest);
        app.setCurrentPlayerData(playerData);
        // start activity
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

}
