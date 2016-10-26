package newteam2.welcometomoscow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by artem on 10/20/16.
 */

public class QuestListAdapter extends ArrayAdapter<QuestInfo> {
    private final Context context;
    private final List<QuestInfo> values;

    public QuestListAdapter(Context context, List<QuestInfo> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.quest_menu_entry, parent, false);
        TextView q_name = (TextView) rowView.findViewById(R.id.firstLine);
        TextView q_info = (TextView) rowView.findViewById(R.id.secondLine);
        ImageView q_icon = (ImageView) rowView.findViewById(R.id.icon);
        QuestInfo qi = values.get(position);
        q_name.setText(qi.name);
        q_info.setText(qi.info_short);
        // for now we ignore the icon in the quest info and just use any image
        q_icon.setImageResource(R.mipmap.ic_launcher);
        return rowView;
    }
}
