package list_view.pick;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.joseph.criko.R;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import common.Complete;
import common.User;
import list_view.History;
import list_view.ListAdapter;
import list_view.ListAdapterCallback;
import main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class PickHistoryFragment extends Fragment implements ListAdapterCallback {

    private static final String TAG = "PickHistoryFragment";
    private RecyclerView mRecyclerView;
    public static List<JSONObject> mContentItems = new ArrayList<JSONObject>();
    private User user;

    public PickHistoryFragment(User user) {
        super();
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list_view, container, false);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.pick_history));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.Adapter mAdapter = new RecyclerViewMaterialAdapter(
                new ListAdapter(mContentItems, ListAdapter.TYPE_PICK_HISTORY, this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ((MainActivity) getActivity()).closeFloatingMenu();
            }
        });
        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

    @Override
    public void onListItemClicked(int position) {
        final JSONObject item = mContentItems.get(position);
        String title = null;
        try {
            title = item.getString("RewardTitle");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MaterialDialog dialog = new MaterialDialog.Builder(this.getActivity())
                .title(title)
                .customView(R.layout.dialog_task_detail, true)
                .positiveText(R.string.complete)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        try {
                            new Complete("picker", item.getInt("RewardID")).execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new History(user, "picker").execute();
                    }

                }).build();
        dialog.show();
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
    }

    public void update() {
        if (mRecyclerView != null) {
            RecyclerView.Adapter mAdapter = new RecyclerViewMaterialAdapter(
                    new ListAdapter(mContentItems, ListAdapter.TYPE_PICK_HISTORY, this));
            mRecyclerView.setAdapter(mAdapter);
            
        }
    }
}
