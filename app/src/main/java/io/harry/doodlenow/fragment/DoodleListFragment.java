package io.harry.doodlenow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.chrometab.ChromeTabHelper;
import io.harry.doodlenow.firebase.FirebaseHelper;
import io.harry.doodlenow.firebase.FirebaseHelperWrapper;
import io.harry.doodlenow.fragment.doodlerange.DoodleRange;
import io.harry.doodlenow.fragment.doodlerange.DoodleRangeCalculator;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.model.DoodleJson;

public class DoodleListFragment extends Fragment
        implements  DoodleListAdapter.OnDoodleItemClickListener,
                    ChildEventListener,
                    ValueEventListener,
                    SwipeRefreshLayout.OnRefreshListener
{
    @BindView(R.id.doodle_list)
    RecyclerView doodleList;
    @BindView(R.id.swipe_refresh_container)
    SwipeRefreshLayout swipeRefreshContainer;

    @Inject
    DoodleRangeCalculator doodleRangeCalculator;
    @Inject
    DoodleListAdapter doodleListAdapter;
    @Inject
    FirebaseHelperWrapper firebaseHelperWrapper;
    @Inject
    ChromeTabHelper chromeTabHelper;

    private FirebaseHelper firebaseHelper;
    private DoodleListType doodleListType;
    private Query doodlesQuery;
    private int standUpStartTime;
    private int archiveDuration;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DoodleApplication) getActivity().getApplication()).getDoodleComponent().inject(this);

        firebaseHelper = firebaseHelperWrapper.getFirebaseHelper("doodles");

        doodleListType = (DoodleListType) getArguments().getSerializable("doodleListType");

        standUpStartTime = getResources().getInteger(R.integer.stand_up_starts_at);
        archiveDuration = getResources().getInteger(R.integer.archive_duration);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doodle_list, container, false);

        ButterKnife.bind(this, view);

        final LinearLayoutManager contentListLayoutManager = new LinearLayoutManager(getActivity());
        doodleList.setLayoutManager(contentListLayoutManager);
        doodleList.setAdapter(doodleListAdapter);

        doodleListAdapter.setOnDoodleClickListener(this);

        DoodleRange doodleRange = doodleRangeCalculator.calculateRange(doodleListType, DateTime.now(), standUpStartTime, archiveDuration);

        doodlesQuery = firebaseHelper.getOrderByChildQuery("createdAt", doodleRange.getStartAt(), doodleRange.getEndAt());
        doodlesQuery.addValueEventListener(this);
        doodlesQuery.addChildEventListener(this);

        swipeRefreshContainer.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onDoodleItemClick(Doodle doodle) {
        String url = doodle.getUrl();

        chromeTabHelper.launchChromeTab((AppCompatActivity) getActivity(), url);
    }

    @Override
    public void onRefresh() {
        doodleListAdapter.clearDoodles();
        doodlesQuery.removeEventListener((ChildEventListener) this);
        doodlesQuery.removeEventListener((ValueEventListener) this);

        DoodleRange doodleRange = doodleRangeCalculator.calculateRange(doodleListType, DateTime.now(), standUpStartTime, archiveDuration);

        doodlesQuery = firebaseHelper.getOrderByChildQuery("createdAt", doodleRange.getStartAt(), doodleRange.getEndAt());
        doodlesQuery.addValueEventListener(this);
        doodlesQuery.addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        DoodleJson doodleJson = dataSnapshot.getValue(DoodleJson.class);
        doodleListAdapter.insertDoodle(0, new Doodle(doodleJson));
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(swipeRefreshContainer.isRefreshing()) {
            swipeRefreshContainer.setRefreshing(false);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
