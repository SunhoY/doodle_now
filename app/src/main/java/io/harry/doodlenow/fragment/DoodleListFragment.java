package io.harry.doodlenow.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.doodlenow.DoodleApplication;
import io.harry.doodlenow.R;
import io.harry.doodlenow.adapter.DoodleListAdapter;
import io.harry.doodlenow.itemdecoration.VerticalSpaceItemDecoration;
import io.harry.doodlenow.model.Doodle;
import io.harry.doodlenow.service.DoodleService;
import io.harry.doodlenow.service.ServiceCallback;

public class DoodleListFragment extends Fragment {
    @BindView(R.id.doodle_list)
    RecyclerView doodleList;

    @Inject
    DoodleListAdapter doodleListAdapter;
    @Inject
    DoodleService doodleService;

    public enum DoodleListType {
        Today, ThisWeek
    }

    private DoodleListType doodleListType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DoodleApplication) getActivity().getApplication()).getDoodleComponent().inject(this);

        doodleListType = (DoodleListType) getArguments().getSerializable("doodleListType");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doodle_list, container, false);

        ButterKnife.bind(this, view);

        final LinearLayoutManager contentListLayoutManager = new LinearLayoutManager(getActivity());
        doodleList.setLayoutManager(contentListLayoutManager);
        doodleList.setAdapter(doodleListAdapter);
        doodleList.addItemDecoration(new VerticalSpaceItemDecoration(getActivity(), R.dimen.list_view_vertical_space));

        return view;
    }

    @Override
    public void onResume() {
        long start;
        long end;
        if(doodleListType == DoodleListType.ThisWeek) {
            end = new DateTime().getMillis();
            start = new DateTime().withTimeAtStartOfDay().minusDays(7).getMillis();
        } else {
            end = new DateTime().withTimeAtStartOfDay().plusHours(9).getMillis();
            start = new DateTime().minusDays(1).withTimeAtStartOfDay().plusHours(9).getMillis();
        }

        doodleService.getDoodles(start, end, new ServiceCallback<List<Doodle>>() {
            @Override
            public void onSuccess(List<Doodle> items) {
                DoodleListAdapter doodleListAdapter = (DoodleListAdapter) doodleList.getAdapter();
                doodleListAdapter.refreshDoodles(items);
            }

            @Override
            public void onFailure(String message) {

            }
        });

        super.onResume();
    }
}
