package com.shir.androidfinalproject.Fragments;

import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.shir.androidfinalproject.Adapters.PlaceListAdapter;
import com.shir.androidfinalproject.Data.PlaceContract;
import com.shir.androidfinalproject.R;
import com.shir.androidfinalproject.CallBacks.Geofencing;

import java.util.ArrayList;
import java.util.List;

public class AddEventLocationsFragment extends Fragment
        implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "AddEventLocationsFrag";
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int PLACE_PICKER_REQUEST = 1;

    Switch onOffSwitch;
    CheckBox cbLocationPermission;
    CheckBox cbRingerPermissions;
    Button btnAddPlace;
    FloatingActionButton btnGoToAddDates;

    private RecyclerView mRecyclerView;
    private PlaceListAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    List<Place> lstPlaces = new ArrayList<>();

    private boolean mIsEnabled;
    private GoogleApiClient mClient;
    private Geofencing mGeofencing;
    private Context context;
    private View view;

    private AddLocationsFragmentListener mListener;

    public AddEventLocationsFragment() {
        // Required empty public constructor
    }

    public static AddEventLocationsFragment newInstance() {
        return new AddEventLocationsFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event_locations, container, false);
        this.view = view;
        context = getContext();

        initViews();
        initListeners();
        initObjects();

        return view;
    }

    private void initViews() {

        onOffSwitch = (Switch)view.findViewById(R.id.enable_switch);

        cbLocationPermission = (CheckBox)view.findViewById(R.id.cbLocationPermission);
        cbRingerPermissions = (CheckBox)view.findViewById(R.id.cbRingerPermissions);

        btnAddPlace = (Button)view.findViewById(R.id.btnAddPlace);
        btnGoToAddDates = (FloatingActionButton)view.findViewById(R.id.btnGoToAddDates);

        // Set up the recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.places_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PlaceListAdapter(context, lstPlaces);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListeners() {

        mIsEnabled = getActivity().getPreferences(Context.MODE_PRIVATE)
                .getBoolean(getString(R.string.setting_enabled), false);
        onOffSwitch.setChecked(mIsEnabled);
        onOffSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
            editor.putBoolean(getString(R.string.setting_enabled), isChecked);
            mIsEnabled = isChecked;
            editor.commit();
            if (isChecked) mGeofencing.registerAllGeofences();
            else mGeofencing.unRegisterAllGeofences();
        });

        cbLocationPermission.setOnClickListener(this);
        cbRingerPermissions.setOnClickListener(this);
        btnAddPlace.setOnClickListener(this);
        btnGoToAddDates.setOnClickListener(this);
    }

    private void initObjects() {
        mClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage((FragmentActivity) getActivity(), this)
                .build();

        mGeofencing = new Geofencing(context, mClient);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cbLocationPermission:
                onLocationPermissionClicked();
                break;
            case R.id.cbRingerPermissions:
                onRingerPermissionsClicked();
                break;
            case R.id.btnAddPlace:
                onAddPlaceButtonClicked();
                break;
            case R.id.btnGoToAddDates:
                if ((mAdapter.getItemCount() > 0) && (mListener != null)){
                    List<String> lstAddresses = new ArrayList<>(lstPlaces.size());

                    if (!lstPlaces.isEmpty()){
                        for (Place place: lstPlaces) {
                            lstAddresses.add(place.getAddress().toString());
                        }
                    }

                    mListener.onGoToAddDatesClick(lstAddresses);
                }
                break;
        }
    }

    public void refreshPlacesData() {
        Uri uri = PlaceContract.PlaceEntry.CONTENT_URI;
        Cursor data = getActivity().getContentResolver().query(
                uri,
                null,
                null,
                null,
                null);

        if (data == null || data.getCount() == 0) return;
        List<String> guids = new ArrayList<>();
        while (data.moveToNext()) {
            guids.add(data.getString(data.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_ID)));
        }
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mClient,
                guids.toArray(new String[guids.size()]));
        placeResult.setResultCallback(places -> {
            mAdapter.swapPlaces(places);
            mGeofencing.updateGeofencesList(places);
            if (mIsEnabled) mGeofencing.registerAllGeofences();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PLACE_PICKER_REQUEST:
                if (resultCode == getActivity().RESULT_OK) {
                    Place place = PlacePicker.getPlace(context, data);
                    if (place == null) {
                        Log.i(TAG, "No place selected");
                        return;
                    }

                    if ((!place.getAddress().equals(""))&&(!lstPlaces.contains(place))){
                        lstPlaces.add(place);

                        //String placeID = place.getId();
                        // Insert a new place into DB
                        //ContentValues contentValues = new ContentValues();
                        //contentValues.put(PlaceContract.PlaceEntry.COLUMN_PLACE_ID, placeID);
                        //getActivity().getContentResolver().insert(PlaceContract.PlaceEntry.CONTENT_URI, contentValues);
                         //Get live data information
                        //refreshPlacesData();

                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    public void onLocationPermissionClicked() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_FINE_LOCATION);
    }

    public void onRingerPermissionsClicked() {
        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        startActivity(intent);
    }

    public void onAddPlaceButtonClicked() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, getString(R.string.need_location_permission_message), Toast.LENGTH_LONG).show();
            return;
        }
        try {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(getActivity());
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        try{
            refreshPlacesData();
        }finally {
            Log.i(TAG, "API Client Connection Successful!");
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "API Client Connection Suspended!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e(TAG, "API Client Connection Failed!");
    }

    @Override
    public void onResume() {
        super.onResume();

        // Initialize location permissions checkbox
        CheckBox locationPermissions = (CheckBox)view.findViewById(R.id.cbLocationPermission);
        if (ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissions.setChecked(false);
        } else {
            locationPermissions.setChecked(true);
            locationPermissions.setEnabled(false);
        }

        // Initialize ringer permissions checkbox
        CheckBox ringerPermissions = (CheckBox)view.findViewById(R.id.cbRingerPermissions);
        NotificationManager nm = (NotificationManager)getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

        // Check if the API supports such permission change and check if permission is granted
        if (android.os.Build.VERSION.SDK_INT >= 24 && !nm.isNotificationPolicyAccessGranted()) {
            ringerPermissions.setChecked(false);
        } else {
            ringerPermissions.setChecked(true);
            ringerPermissions.setEnabled(false);
        }
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

    public interface AddLocationsFragmentListener {
        void onGoToAddDatesClick(List<String> locations);
    }
}
