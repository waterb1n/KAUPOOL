package com.water.kaupool;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.fragment.app.Fragment;

import android.widget.EditText;
import android.widget.Button;
import android.widget.FrameLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapData;

import java.io.IOException;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;

public class FragmentA extends Fragment {

    private static String ApiKey = "l7xxfee14d4fa6ac4197a95728747f7f1da4";
    double lat1, lat2, lon1, lon2;
    double Latitude, Longtitude = 0;
    FrameLayout framelayout;
    EditText address1, address2;
    Button search;
    private Context mContext = null;
    private TMapView tmapview = null;
    private TMapGpsManager gps = null;

    public FragmentA() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment1, container, false);
        framelayout = (FrameLayout) v.findViewById(R.id.mapview);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initClick();
        initMap();
    }

    public void init() {
        address1 = (EditText) getActivity().findViewById(R.id.departure);
        address2 = (EditText) getActivity().findViewById(R.id.destination);
        search = (Button) getActivity().findViewById(R.id.searchBtn);
    }

    /**
     * Button listener
     */
    public void initClick() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();

                // 지도 중심좌표 이동
                tmapview.setCenterPoint((lon1 + lon2) / 2, (lat1 + lon2) / 2, true);
                TMapPoint point1 = new TMapPoint(lat1, lon1);
                TMapPoint point2 = new TMapPoint(lat2, lon2);
            }
        });
    }

    public void initMap() {
        tmapview = new TMapView(getActivity());
        tmapview.setSKTMapApiKey(ApiKey);   // api 키 설정
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN);
        tmapview.setIconVisibility(true);
        tmapview.setZoomLevel(10); // 7~19
        tmapview.setMapType(TMapView.MAPTYPE_STANDARD); // 지도 타입 - 일반지도
        tmapview.setCompassMode(false); // 단말의 방향에 따라 움직이는 나침반 모드
        tmapview.setTrackingMode(true); // 화면중심을 단말의 현재위치로 이동
        framelayout.addView(tmapview);
    }

    public void setLocationPoint() {
    }


    /* MapView 참고 블로그
    https://blog.naver.com/ajh794/221018636934
     */
    public void getAddress() {
        Geocoder gc = new Geocoder(getActivity(), Locale.KOREA);
        String addr1 = address1.getText().toString();
        String addr2 = address2.getText().toString();

        try {
            List<Address> addrList1 = gc.getFromLocationName(addr1, 5);
            List<Address> addrList2 = gc.getFromLocationName(addr2, 5);

            if (addrList1 != null && addrList2 != null) {
                lat1 = addrList1.get(0).getLatitude();
                lon1 = addrList1.get(0).getLongitude();
                lat2 = addrList2.get(0).getLatitude();
                lon2 = addrList2.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
