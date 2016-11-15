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

    private ArrayList<QuestInfo> getQuestInfoFromServer() {
        final ArrayList<QuestInfo> data = new ArrayList<>();

        final List<QuestEvent> quest_one = new ArrayList<>();

        GetApi getApi = GetData.getRetrofit().create(GetApi.class);
        Call<EventList> obj = getApi.getData();
        obj.enqueue(new Callback<EventList>() {
            @Override
            public void onResponse(Call<EventList> call, Response<EventList> response) {
                /*for (int i = 0; i < response.body().getEvents().size(); i++) {
                    final double lat = Double.parseDouble(response.body().getEvents().get(i).getLat());
                    final double $long = Double.parseDouble(response.body().getEvents().get(i).get$long());
                    // QuestEventPredicate
                    quest_one.add(new QuestEvent(response.body().getEvents().get(i).getName(),
                            new LatLng(lat, $long),
                            new QuestEventPredicate() {
                                @Override
                                public boolean test(PlayerData playerData) {
                                    LatLng detskyMirLocation = new LatLng(lat, $long);
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
                }*/

                // Add field iconId and questId
                data.add(new QuestInfo(response.body().getName().toString(),
                        response.body().getDesc().toString(),
                        response.body().getLong_desc().toString(),
                        R.mipmap.ic_launcher,
                        21,
                        quest_one));
            }

            @Override
            public void onFailure(Call<EventList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
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

        data.add(new QuestInfo("Moscow"
                , "Long and difficult"
                , "Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas"
                , R.mipmap.ic_launcher
                , 21
                , quest_one));

        data.add(new QuestInfo("Linux", "You get to cry", "perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 32, new ArrayList<QuestEvent>()));
        data.add(new QuestInfo("Android", "But it gets better", "omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 29, new ArrayList<QuestEvent>()));
        data.add(new QuestInfo("new Moscow(2)", "Some more filler text", "iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 44, new ArrayList<QuestEvent>()));
        data.add(new QuestInfo("Test scrolling 1", "Not much to say 1", "sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 43, new ArrayList<QuestEvent>()));
        data.add(new QuestInfo("Test scrolling 2", "Not much to say 2", "voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 47, new ArrayList<QuestEvent>()));
        data.add(new QuestInfo("Test scrolling 3", "Not much to say 3", "accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 53, new ArrayList<QuestEvent>()));
        data.add(new QuestInfo("Test scrolling 4", "Not much to say 4", "totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 523, new ArrayList<QuestEvent>()));
        data.add(new QuestInfo("Test scrolling 5", "Not much to say 5", ", quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 528, new ArrayList<QuestEvent>()));
        data.add(new QuestInfo("Test scrolling 6", "Not much to say 6", ", quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 531, new ArrayList<QuestEvent>()));
        data.add(new QuestInfo("Test scrolling 6", "Not much to say 6", ", quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas", R.mipmap.ic_launcher, 438, new ArrayList<QuestEvent>()));
        return data;
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
