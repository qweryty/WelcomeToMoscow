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

        // make default player info, no name, no nothing
        buttonMaps = (Button) rootView.findViewById(R.id.button_maps);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        attachedContext = context;
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
                    }
                    adapter.notifyDataSetChanged();
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
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long ld) {
        selectedQuest = (QuestInfo) parent.getItemAtPosition(position);
        // show extra info about quest
        LinearLayout quest_info = (LinearLayout) currentActivity.findViewById(R.id.quest_all_info);
        TextView quest_long_info = (TextView) quest_info.findViewById(R.id.quest_text_describe);
        quest_long_info.setText(selectedQuest.info_short);
        buttonMaps.setEnabled(true);
    }
}
