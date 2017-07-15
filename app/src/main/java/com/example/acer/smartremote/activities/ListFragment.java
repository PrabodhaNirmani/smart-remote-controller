package com.example.acer.smartremote.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.acer.smartremote.Models.Remote;
import com.example.acer.smartremote.Models.RemoteLab;
import com.example.acer.smartremote.R;
import com.example.acer.smartremote.activities.dummy.DummyContent;
import com.example.acer.smartremote.activities.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListFragment extends Fragment {

//     TODO: Customize parameter /argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
//     TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    private RecyclerView mRemoteRecylcerView;
    private ListFragment.RemoteAdapter mAdapter;
    private RemoteLab mRemoteLab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRemoteLab = RemoteLab.get(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_list, container, false);
        mRemoteRecylcerView = (RecyclerView) view.findViewById(R.id.remote_recycler_view);
        mRemoteRecylcerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        updateUI();
        return view;
    }

    private void updateUI(){

        //List<Remote> remotes = this.mRemoteLab.getDummyRemoteList();
        List<Remote> remotes = this.mRemoteLab.getRemotes();

        if (mAdapter == null){
            mAdapter = new RemoteAdapter(remotes);
            mRemoteRecylcerView.setAdapter(mAdapter);
        } else {
            mAdapter.setRemotes(remotes);
            mAdapter.notifyDataSetChanged();
        }
    }
    public ListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ListFragment newInstance(int columnCount) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_list_list, container, false);
//
//        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new MylistRecyclerViewAdapter(DummyContent.ITEMS, mListener));
//        }
//        return view;
//    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    private class RemoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Remote  mRemote;
        private TextView mRemoteBrandTextView;

        public RemoteHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mRemoteBrandTextView = (TextView)itemView.findViewById(R.id.list_item_remote_brand_text_view);
        }

        public void bindRemote(Remote remote){
            mRemote = remote;
            mRemoteBrandTextView.setText(mRemote.getRemoteBrand());

        }

        @Override
        public void onClick(View view) {
            Intent intent = RemoteActivity.newIntent(getActivity(), this.mRemote.getRemoteID());
            startActivity(intent);
        }
    }
    private class RemoteAdapter extends RecyclerView.Adapter<ListFragment.RemoteHolder>{
        private List<Remote> mRemotes;

        public RemoteAdapter(List<Remote> remotes){
            mRemotes = remotes;
        }

        @Override
        public ListFragment.RemoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.activity_list_item, parent, false);
            return new RemoteHolder(view);
        }

        @Override
        public void onBindViewHolder(ListFragment.RemoteHolder holder, int position) {
            Remote remote = mRemotes.get(position);
            holder.bindRemote(remote);
        }

        @Override
        public int getItemCount() {
            return mRemotes.size();
        }

        public void setRemotes(List<Remote> remotes){
            mRemotes = remotes;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }
}
