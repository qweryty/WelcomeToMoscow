package newteam2.welcometomoscow;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestsListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView chooseQuestList;
    private QuestInfo selectedQuest;
    private PlayerData playerData;
    private final ArrayList<QuestInfo> values = new ArrayList<>();
    private QuestListAdapter adapter;
    private View rootView;
    private Context attachedContext;
    private Button buttonMaps;
    private Activity currentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_quests_list, container, false);
        // Find the ListView resource.
        chooseQuestList = (ListView) rootView.findViewById(R.id.choose_quest);

        // init list
        adapter = new QuestListAdapter(currentActivity, values);
        chooseQuestList.setAdapter(adapter);
        chooseQuestList.setOnItemClickListener(this);

        // add quests
        DownloadQuestsAsync();
        AddDummyQuests();

        // make default player info, no name, no nothing
        playerData = new PlayerData();
        buttonMaps = (Button) rootView.findViewById(R.id.button_maps);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        attachedContext = context;
    }

    private void AddDummyQuests() {
        final List<QuestEvent> quest_one = new ArrayList<>();
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
        adapter.notifyDataSetChanged();
    }

    private void DownloadQuestsAsync() {
        GetApi getApi = GetData.getRetrofit().create(GetApi.class);

        Call<ResponseQ> obj = getApi.getData();
        obj.enqueue(new Callback<ResponseQ>() {
            @Override
            public void onResponse(Call<ResponseQ> call, Response<ResponseQ> response) {
                if (response.isSuccessful()) {
                    List<QuestInfo> listQuest = response.body().getQuestList();
                    for (QuestInfo questInfo : listQuest) {
                        values.add(questInfo);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(currentActivity.getApplicationContext(), "Error1", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseQ> call, Throwable t) {
                Toast.makeText(currentActivity.getApplicationContext(), "Error2", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ButtonClickViewMap(View view) {
        if (selectedQuest == null) {
            return;
        }
        // Get ready to share quest info
        MainApplication app = (MainApplication) currentActivity.getApplication();
        app.setCurrentQuestInfo(selectedQuest);
        app.setCurrentPlayerData(playerData);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long ld) {
        selectedQuest = (QuestInfo) parent.getItemAtPosition(position);
        // show extra info about quest
        LinearLayout quest_info = (LinearLayout) currentActivity.findViewById(R.id.quest_all_info);
        TextView quest_long_info = (TextView) quest_info.findViewById(R.id.quest_text_describe);
        quest_long_info.setText(selectedQuest.info_long);
    }
}
