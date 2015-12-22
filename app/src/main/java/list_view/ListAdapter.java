package list_view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.instantly.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import list_view.ListAdapterCallback;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "ListAdapter";

    public static final int TYPE_TASK_LIST = 0;
    public static final int TYPE_PICK_HISTORY = 1;
    public static final int TYPE_POST_HISTORY = 2;

    private int mViewType;
    private List<JSONObject> mContentList;
    private ListAdapterCallback mListItemOnClickCallback;

    public ListAdapter(List<JSONObject> contentList, int type, ListAdapterCallback callback) {
        mViewType = type;
        mContentList = contentList;
        mListItemOnClickCallback = callback;
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (mViewType) {
            case TYPE_TASK_LIST:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.task_list_item, parent, false);
                break;
            case TYPE_POST_HISTORY:
            case TYPE_PICK_HISTORY:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.history_list_item, parent, false);
                break;
            default:
                Log.e(TAG, "Unknown adapter type");
                return null;
        }
        return new RecyclerView.ViewHolder(view) {
        };
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mViewType > 2 || mViewType < 0) {
            Log.e(TAG, "Unknown adapter type");
            return;
        }
        JSONObject item = mContentList.get(position);
        if (mViewType == TYPE_POST_HISTORY) {
            TextView status = (TextView) holder.itemView.findViewById(R.id.status);
            String statusResult = "Unpicked";
            try {
                String pickTime = item.getString("PickTime");
                String pickerCompleteTime = item.getString("PickerCompleteTime");
                if(pickTime.length() != 0 && pickerCompleteTime.length() == 0) {
                    statusResult = "Incompleted";
                } else if(pickerCompleteTime.length() != 0) {
                    statusResult = "Completed";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            status.setText(statusResult);
        }
        if (mViewType == TYPE_PICK_HISTORY) {
            TextView status = (TextView) holder.itemView.findViewById(R.id.status);
            String statusResult = "Incompleted";
            try {
                String pickerCompleteTime = item.getString("PickerCompleteTime");
                String posterCompleteTime = item.getString("PosterCompleteTime");
                if(pickerCompleteTime.length() != 0 && posterCompleteTime.length() == 0) {
                    statusResult = "Not comfirmed";
                } else if(posterCompleteTime.length() != 0) {
                    statusResult = "Comfirmed";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            status.setText(statusResult);
        }
        TextView title = (TextView) holder.itemView.findViewById(R.id.title);
        TextView bounty = (TextView) holder.itemView.findViewById(R.id.bounty);
        TextView description = (TextView) holder.itemView.findViewById(R.id.description);
        try {
            title.setText(item.getString("RewardTitle"));
            bounty.setText("$" + item.getString("Rewards"));
            description.setText(item.getString("Description"));
        } catch (JSONException e) {
            e.getStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListItemOnClickCallback.onListItemClicked(position);
            }
        });
    }
}