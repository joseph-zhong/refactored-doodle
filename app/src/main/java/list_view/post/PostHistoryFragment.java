package list_view.post;

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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.instantly.app.R;
import com.instantly.app.common.Complete;
import com.instantly.app.common.User;
import com.instantly.app.list_view.History;
import com.instantly.app.list_view.ListAdapter;
import com.instantly.app.list_view.ListAdapterCallback;
import com.instantly.app.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class PostHistoryFragment extends Fragment implements ListAdapterCallback {

    private static final String TAG = "PostHistoryFragment";
    private RecyclerView mRecyclerView;
    public static List<JSONObject> mContentItems = new ArrayList<JSONObject>();
    private User user;
    private MainActivity activity;
    private int rate;

    public PostHistoryFragment(User user, MainActivity activity) {
        super();
        this.activity = activity;
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list_view, container, false);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.post_history));
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
                new ListAdapter(mContentItems, ListAdapter.TYPE_POST_HISTORY, this));
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
        String pickTime = "";
        try {
            pickTime = item.getString("PickTime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MaterialDialog dialog = null;
        if (pickTime.length() == 0) {
            dialog = new MaterialDialog.Builder(this.getActivity())
                    .title(title)
                    .customView(R.layout.dialog_task_detail, true)
                    .positiveText(R.string.comfirm)
                    .negativeText(android.R.string.cancel)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            Toast.makeText(activity.getApplicationContext(), "Can't confirm the task unpicked.", Toast.LENGTH_SHORT).show();
                        }

                    }).build();
        } else {
            dialog = new MaterialDialog.Builder(this.getActivity())
                    .title(title)
                    .customView(R.layout.dialog_task_detail, true)
                    .positiveText(R.string.comfirm)
                    .negativeText(android.R.string.cancel)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            int rewardID = 0;
                            try {
                                rewardID = item.getInt("RewardID");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            new Complete("poster", rewardID).execute();
                            new History(user, "poster").execute();
                            rateDialog(rewardID);
                        }

                    }).build();
        }
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
        dialog.show();
    }

    public void rateDialog(final int rewardID) {
        rate = 3;
        MaterialDialog dialog = new MaterialDialog.Builder(this.getActivity())
                .title("Rating")
                .customView(R.layout.dialog_rating, true)
                .positiveText(R.string.submit)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        Toast.makeText(activity.getApplicationContext(), "Thank you for rating service", Toast.LENGTH_SHORT).show();
                        new PickerRater(rewardID, rate).execute();
                    }

                }).build();
        RatingBar mratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
        mratingBar.setRating(3.0f);
        final TextView ratingValue = (TextView)dialog.findViewById(R.id.ratingValue);
        mratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                rate = (int)rating;
                if(rate < 2) {
                    ratingValue.setText(rate + " STAR");
                } else {
                    ratingValue.setText(rate + " STARS");
                }

            }
        });
        dialog.show();
    }

    public void update() {
        if (mRecyclerView != null) {
            RecyclerView.Adapter mAdapter = new RecyclerViewMaterialAdapter(
                    new ListAdapter(mContentItems, ListAdapter.TYPE_POST_HISTORY, this));
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
