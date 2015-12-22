package list_view;

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

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.instantly.app.R;
import com.instantly.app.common.DialogBuilder;
import com.instantly.app.common.Picker;
import com.instantly.app.common.User;
import com.instantly.app.main.MainActivity;
import com.instantly.app.map_view.TaskMapViewFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class TaskListViewFragment extends Fragment implements ListAdapterCallback {

    private static final String TAG = "TaskListViewFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<JSONObject> mContentItems;
    private User user;

    public TaskListViewFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list_view, container, false);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.task_list));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mContentItems = new ArrayList<JSONObject>(TaskMapViewFragment.getMarkerInfo().values());
        mAdapter = new RecyclerViewMaterialAdapter(new ListAdapter(mContentItems, ListAdapter.TYPE_TASK_LIST, this));
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
        DialogBuilder.buildTaskDetailDialog(item, getActivity(), new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                try {
                    new Picker(item.getInt("RewardID"), item.getInt("CustomerID"), user, TaskListViewFragment.this.getActivity().getApplicationContext()).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).show();
    }

    public void update() {
        if (mRecyclerView != null) {
            mContentItems = new ArrayList<JSONObject>(TaskMapViewFragment.getMarkerInfo().values());
            mAdapter = new RecyclerViewMaterialAdapter(new ListAdapter(mContentItems, ListAdapter.TYPE_TASK_LIST, this));
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}

