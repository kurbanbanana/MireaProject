package ru.mirea.kurbanovaad.mireaproject;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Button;

import ru.mirea.kurbanovaad.mireaproject.databinding.FragmentEstablishmentsBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class Establishments extends Fragment {
    private MapView mapview = null;
    private FragmentEstablishmentsBinding binding;
    private static final int REQUEST_CODE_PERMISSION = 200;
    private boolean isWork;
    private MyLocationNewOverlay locationNewOverlay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEstablishmentsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Button btnCenterMap = view.findViewById(R.id.btn_center_map);

        Context context = getActivity();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        mapview = binding.mapView;
        mapview.setZoomRounding(true);
        mapview.setMultiTouchControls(true);

        IMapController mapController = mapview.getController();
        mapController.setZoom(10.0);
        GeoPoint startPoint = new GeoPoint(55.743929, 37.593442);
        mapController.setCenter(startPoint);

        int locationPermissionStatus = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if(locationPermissionStatus == PackageManager.PERMISSION_GRANTED){
            isWork = true;
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
        }

        if(isWork){
            locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mapview);
            locationNewOverlay.enableMyLocation();
            mapview.getOverlays().add(locationNewOverlay);
        }

        CompassOverlay compassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), mapview);
        compassOverlay.enableCompass();
        mapview.getOverlays().add(compassOverlay);

        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapview);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapview.getOverlays().add(scaleBarOverlay);


        Marker marker1 = new Marker(mapview);
        marker1.setPosition(new GeoPoint(55.757462, 37.634858));
        marker1.setOnMarkerClickListener((marker, mapView) -> {
            Toast.makeText(context, "Makaroff Optics, \n" +
                            "ул. Маросейка, 6-8с1, Москва",
                    Toast.LENGTH_SHORT).show();
            return true;
        });
        mapview.getOverlays().add(marker1);
        marker1.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
        marker1.setTitle("Makaroff Optics, Метро: Китай-город");


        Marker marker2 = new Marker(mapview);
        marker2.setPosition(new GeoPoint(55.690452, 37.534562));
        marker2.setOnMarkerClickListener((marker, mapView) -> {
            Toast.makeText(context, "Makaroff Optics, \n" +
                            "просп. Вернадского, 9/10, Москва",
                    Toast.LENGTH_SHORT).show();
            return true;
        });
        mapview.getOverlays().add(marker2);
        marker2.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
        marker2.setTitle("Makaroff Optics, метро: Университет");


        Marker marker3 = new Marker(mapview);
        marker3.setPosition(new GeoPoint(55.778636, 37.601857));
        marker3.setOnMarkerClickListener((marker, mapView) -> {
            Toast.makeText(context, "Makaroff Optics, \n" +
                            "Долгоруковская ул., 40, Москва",
                    Toast.LENGTH_SHORT).show();
            return true;
        });
        mapview.getOverlays().add(marker3);
        marker3.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
        marker3.setTitle("Makaroff Optics, Метро: Новослободская");

        btnCenterMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMapController mapController = mapview.getController();
                GeoPoint newCenter = new GeoPoint(55.743929, 37.593442); // Новый центр карты
                mapController.setCenter(newCenter);
                mapController.setZoom(10.0);
            }
        });


        return view;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                isWork = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!isWork) getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()));
        if (mapview != null) {
            mapview.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getActivity(), PreferenceManager.getDefaultSharedPreferences(getActivity()));
        if (mapview != null) {
            mapview.onPause();
        }
    }
}
