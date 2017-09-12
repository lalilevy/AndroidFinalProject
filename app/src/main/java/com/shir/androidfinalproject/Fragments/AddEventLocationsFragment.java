package com.shir.androidfinalproject.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.shir.androidfinalproject.Adapters.LocationsAdapter;
import com.shir.androidfinalproject.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddEventLocationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddEventLocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventLocationsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AddEventLocationsFragment";

    private AddLocationsFragmentListener mListener;

    EditText etCurrLocation;
    ListView lvLocationsList;
    ImageView ivAddLocation;
    ImageView btnAddDates;

    ArrayList<String> lstLocations;
    LocationsAdapter adapter;

    public AddEventLocationsFragment() {
        // Required empty public constructor
    }

    public static AddEventLocationsFragment newInstance() {

        AddEventLocationsFragment fragment = new AddEventLocationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event_locations, container, false);

        etCurrLocation = (EditText) view.findViewById(R.id.tv_add_location);
        lvLocationsList = (ListView) view.findViewById(R.id.lv_locations_list);
        ivAddLocation = (ImageView) view.findViewById(R.id.iv_add_location);
        btnAddDates = (ImageView) view.findViewById(R.id.btn_add_dates);

        ivAddLocation.setOnClickListener(this);
        btnAddDates.setOnClickListener(this);
        if (lstLocations == null)
            lstLocations = new ArrayList<>();
        adapter = new LocationsAdapter(getActivity(), lstLocations);
        lvLocationsList.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddLocationsFragmentListener) {
            mListener = (AddLocationsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_location:
                String loc = etCurrLocation.getText().toString();

                if (TextUtils.isEmpty(loc)) {
                    etCurrLocation.setError("Must enter a location");
                    return;
                } else {
                    adapter.add(loc);
                    etCurrLocation.setText("");
                    //lstLocations.add(loc);
                    adapter.notifyDataSetChanged();
                }

                break;
            case R.id.btn_add_dates:
                if (!lstLocations.isEmpty() && mListener != null)
                    mListener.onAddDatesClicked(lstLocations);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface AddLocationsFragmentListener {
        void onAddDatesClicked(ArrayList<String> lstLocations);
    }
}
