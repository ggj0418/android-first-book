import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setIcon(LocationOverlay.DEFAULT_ICON);

        Marker sejongMarker = new Marker();
        sejongMarker.setPosition(new LatLng(37.550504, 127.073845));
        sejongMarker.setCaptionText("세종대학교");

        CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(new LatLng(37.550504, 127.073845),15);

        sejongMarker.setMap(naverMap);
        naverMap.moveCamera(cameraUpdate);
        naverMap.setMinZoom(5.0);
        naverMap.setMaxZoom(18.0);
    }
}