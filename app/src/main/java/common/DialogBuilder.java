package common;

import android.content.Context;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.instantly.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DialogBuilder {

    public static MaterialDialog buildTaskDetailDialog(JSONObject item, Context context, MaterialDialog.ButtonCallback callback) {
        String title = null;
        try {
            title = item.getString("RewardTitle");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .customView(R.layout.dialog_task_detail, true)
                .positiveText(R.string.pick)
                .negativeText(android.R.string.cancel)
                .callback(callback).build();

        TextView category = (TextView) dialog.findViewById(R.id.category);
        TextView date = (TextView) dialog.findViewById(R.id.date);
        TextView bounty = (TextView) dialog.findViewById(R.id.bounty);
        TextView description = (TextView) dialog.findViewById(R.id.description);

        try {
            category.setText(item.getString("Category"));
            bounty.setText("$" + item.getString("Rewards"));
            description.setText(item.getString("Description"));
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            // date.setText(df.format(item.getString("StartTime")));
            date.setText("05/12/2015");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dialog;
    }
}
