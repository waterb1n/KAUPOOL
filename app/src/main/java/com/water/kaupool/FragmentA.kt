package com.water.kaupool

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.skt.Tmap.TMapData
import com.skt.Tmap.TMapData.ConvertGPSToAddressListenerCallback
import com.skt.Tmap.TMapGpsManager
import com.skt.Tmap.TMapGpsManager.onLocationChangedCallback
import com.skt.Tmap.TMapPoint
import com.skt.Tmap.TMapView
import com.water.kaupool.FragmentB.Companion.manage_list
import com.water.kaupool.LoginActivity.Companion.db_manager
import com.water.kaupool.LoginActivity.Companion.loginName
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * https://robotluv1226.blog.me/220843350043
 */
@RequiresApi(api = Build.VERSION_CODES.N)
class FragmentA : Fragment(), onLocationChangedCallback, ConvertGPSToAddressListenerCallback {
    private val m_tmapPoint = ArrayList<TMapPoint>()
    private val mArrayMarkerID = ArrayList<String>()
    //private val m_mapPoint = ArrayList<MapPoint>()

    var myTag: String? = null
    var time_required = 0
    var distance = 0
    var lat1 = 0.0
    var lat2 = 0.0
    var lon1 = 0.0
    var lon2 = 0.0
    var latitude = 0.0
    var longitude = 0.0
    private var myYear = 0
    private var myMonth = 0
    private var myDay = 0
    private var myHour = 0
    private var myMinute = 0
    var total_date: String? = null
    var total_time: String? = null
    var locationManager: LocationManager? = null
    var framelayout: FrameLayout? = null
    var info_box: LinearLayout? = null
    var address1: EditText? = null
    var address2: EditText? = null
    var info_time: TextView? = null
    var info_distance: TextView? = null
    var search: Button? = null
    var choiceBtn: ToggleButton? = null
    var register: Button? = null
    private val mContext: Context? = null
    private val m_bTrackingMode = true
    private var tmapview: TMapView? = null
    private var tmapdata: TMapData? = null
    private var tmapgps: TMapGpsManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment1, container, false)
        framelayout = v.findViewById<View>(R.id.mapview) as FrameLayout
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        initClick()
        initMap()
    }

    fun setClickedData(pos: Int) {
        val manage_query = db_manager!!.orderByChild("date")
        manage_list!!.clear()
        manage_query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                manage_list!!.clear()
                for (data in dataSnapshot.children) {
                    if (data.value != null) {
                        val m = data.getValue(manager::class.java)
                        if (m!!.check) manage_list!!.add(m)
                    }
                }

                val man = manage_list!![pos]!!

                address1!!.setText(man.start)
                address2!!.setText(man.end)

                searchAddress(man.start, man.end)

                info_box!!.visibility = View.VISIBLE
                tmapview!!.setCenterPoint((lon1 + lon2) / 2, (lat1 + lat2) / 2, true)

                val point1 = TMapPoint(lat1, lon1)
                val point2 = TMapPoint(lat2, lon2)

                val passList = ArrayList<TMapPoint>()
                //startLocationService()

                passList.add(TMapPoint(latitude, longitude))
                passList.add(TMapPoint(37.557176, 126.864279))

                tmapdata!!.findMultiPointPathData(point1, point2, passList, 0
                ) { tMapPolyLine ->
                    tmapview!!.addTMapPath(tMapPolyLine)

                    val latSpan = Math.abs(lat1 - lat2)
                    val lonSpan = Math.abs(lon1 - lon2)

                    tmapview!!.zoomToSpan(latSpan, lonSpan)

                    distance = tMapPolyLine.distance.toInt()
                    time_required = (tMapPolyLine.distance / 1000).toInt()

                    Thread(Runnable {
                        activity!!.runOnUiThread {
                            info_distance!!.text = distance.toString() + "km"
                            info_time!!.text = time_required.toString() + "분"
                        }
                    }) //.start()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onConvertToGPSToAddress(s: String) {}
    override fun onLocationChange(location: Location) {
        if (m_bTrackingMode) {
            tmapview!!.setLocationPoint(location.longitude, location.latitude)
        }
    }

    fun init() {
        myTag = tag
        (activity as MainActivity?)!!.fragment = myTag
        //(activity as MainActivity?)!!.fragment = tag
        address1 = activity!!.findViewById<View>(R.id.departure) as EditText
        address2 = activity!!.findViewById<View>(R.id.destination) as EditText
        search = activity!!.findViewById<View>(R.id.searchBtn) as Button
        choiceBtn = activity!!.findViewById<View>(R.id.choiceBtn) as ToggleButton
        register = activity!!.findViewById<View>(R.id.register) as Button
        info_box = activity!!.findViewById<View>(R.id.info_box) as LinearLayout
        info_time = activity!!.findViewById<View>(R.id.info_time) as TextView
        info_distance = activity!!.findViewById<View>(R.id.info_distance) as TextView
        val datePicker = activity!!.findViewById<View>(R.id.datepicker) as Button
        val timePicker = activity!!.findViewById<View>(R.id.timepicker) as Button

        val myDateSetListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            datePicker.text = String.format("%d년 %d월 %d일", year, monthOfYear + 1, dayOfMonth)
            total_date = String.format("%d%02d%02d", year, monthOfYear + 1, dayOfMonth)
            //Toast.makeText(getActivity(), total_date, Toast.LENGTH_SHORT).show();
        }

        val myTimeSetListener = OnTimeSetListener { view, hourOfDay, minute ->
            timePicker.text = String.format("%d시 %d분", hourOfDay, minute)
            total_time = String.format("%02d%02d", hourOfDay, minute)
            //Toast.makeText(getActivity(), total_time, Toast.LENGTH_SHORT).show();
        }

        datePicker.setOnClickListener {
            val c = Calendar.getInstance()
            myYear = c[Calendar.YEAR]
            myMonth = c[Calendar.MONTH]
            myDay = c[Calendar.DAY_OF_MONTH]
            val dlgDate: Dialog = DatePickerDialog(activity!!, myDateSetListener, myYear, myMonth, myDay)
            dlgDate.show()
        }

        timePicker.setOnClickListener {
            val c = Calendar.getInstance()
            myHour = c[Calendar.HOUR_OF_DAY]
            myMinute = c[Calendar.MINUTE]
            val dlgTime: Dialog = TimePickerDialog(activity, myTimeSetListener, myHour, myMinute, false)
            dlgTime.show()
        }
    }

    /**
     * Button listener
     */
    fun initClick() {
        choiceBtn!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                address1!!.setText("한국항공대학교")
                address2!!.setText("")
            } else {
                address1!!.setText("")
                address2!!.setText("한국항공대학교")
            }
        }
        search!!.setOnClickListener {
            info_box!!.visibility = View.VISIBLE
            searchAddress()

            // 지도 중심좌표 이동
            tmapview!!.setCenterPoint((lon1 + lon2) / 2, (lat1 + lat2) / 2, true)
            val point1 = TMapPoint(lat1, lon1)
            val point2 = TMapPoint(lat2, lon2)
            val passList = ArrayList<TMapPoint>()

            tmapdata!!.findMultiPointPathData(point1, point2, passList, 0
            ) { tMapPolyLine ->
                tmapview!!.addTMapPath(tMapPolyLine)
                val latSpan = Math.abs(lat1 - lat2)
                val lonSpan = Math.abs(lon1 - lon2)
                tmapview!!.zoomToSpan(latSpan, lonSpan)

                distance = tMapPolyLine.distance.toInt()
                time_required = (tMapPolyLine.distance / 1000).toInt()
                Thread(Runnable {
                    activity!!.runOnUiThread {
                        info_distance!!.text = distance.toString() + "km"
                        info_time!!.text = time_required.toString() + "분"
                    }
                }) //.start()
            }


            register!!.setOnClickListener(View.OnClickListener {
                val man_query: Query = db_manager?.orderByChild("date")!!.equalTo(total_date)
                man_query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val date = initTime()
                        val man = manager(date, total_time, loginName, "", true, address1?.getText().toString(), address2?.getText().toString(), 0)
                        db_manager?.child(date)?.setValue(man)
                        (activity as MainActivity?)?.getViewPager()?.setCurrentItem(1)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            })
        }
    }

    fun initMap() {
        tmapdata = TMapData()
        if (activity != null) {
            tmapview = TMapView(activity)
        }
        tmapview!!.setHttpsMode(true)
        //tmapview = new TMapView(getActivity());
        tmapview!!.setSKTMapApiKey(ApiKey) // api 키 설정
        tmapview!!.setLanguage(TMapView.LANGUAGE_KOREAN)
        tmapview!!.setIconVisibility(true)
        tmapview!!.zoomLevel = 10 // 7~19
        tmapview!!.mapType = TMapView.MAPTYPE_STANDARD // 지도 타입 - 일반지도
        tmapview!!.setCompassMode(false) // 단말의 방향에 따라 움직이는 나침반 모드
        tmapview!!.setTrackingMode(true) // 화면중심을 단말의 현재위치로 이동
        framelayout!!.addView(tmapview)
        tmapgps = TMapGpsManager(activity)
        tmapgps!!.minTime = 1000
        tmapgps!!.minDistance = 5f
        //tmapgps.setProvider(tmapgps.NETWORK_PROVIDER);
        tmapgps!!.provider = TMapGpsManager.GPS_PROVIDER
        LocationPermission()
    }

    private fun LocationPermission() {
        // ACCESS_FINE_LOCATION: NETWORK_PROVIDER, GPS_PROVIDER
        // ACCESS_COARSE_LOCATION: NETWORK_PROVIDER
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한 거절
            //최초 권한 요청인지, user 재요청인지
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 사용자가 승인 거절을 누른 경우 or 아직 요청하지 않은 경우
                // 권한 재요청
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 100)
            } else {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 100)
            }
        } else {
            /// 권한 승인된 상태
            tmapgps!!.OpenGps()
        }
    }

    private fun startLocationService() {
        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val minTime: Long = 10
        val minDistance = 1f
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "Don't have permissions.", Toast.LENGTH_LONG).show()
            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener)
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocationListener)
    }

    private fun stopLocationService() {
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "Don't have permissions.", Toast.LENGTH_LONG).show()
            return
        }
        locationManager!!.removeUpdates(mLocationListener)
    }

    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("test", "onLocationChanged, location:$location")
            longitude = location.longitude
            latitude = location.latitude
            tmapview!!.setCenterPoint(longitude, latitude)
            val address = searchAddress(activity, latitude, longitude)
            address1!!.setText(address)
            stopLocationService() // 미수신시
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    // lat, lon to address in KOREA
    open fun searchAddress(mContext: Context?, latitude: Double, longitude: Double): String? {
        var nowAddress: String? = "Cannot confirm current location."
        val geocoder = Geocoder(mContext, Locale.KOREA)
        var address: List<Address>? = null
        if (geocoder != null) {
            try {
                address = geocoder.getFromLocation(latitude, longitude, 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (address != null && address.size > 0) {
                val currentLocationAddress = address[0].getAddressLine(0).toString()
                nowAddress = currentLocationAddress
            }
        }
        return nowAddress
    }

    /**
     * MapView
     * https://blog.naver.com/ajh794/221018636934
     */
    fun searchAddress() {
        val gc = Geocoder(activity, Locale.KOREA)
        val addr1 = address1!!.text.toString()
        val addr2 = address2!!.text.toString()
        try {
            val addrList1: List<Address>? = gc.getFromLocationName(addr1, 5)
            val addrList2: List<Address>? = gc.getFromLocationName(addr2, 5)
            if (addrList1 != null && addrList2 != null) {
                lat1 = addrList1[0].latitude
                lon1 = addrList1[0].longitude
                lat2 = addrList2[0].latitude
                lon2 = addrList2[0].longitude
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun searchAddress(addr1: String?, addr2: String?) {
        val gc = Geocoder(activity, Locale.KOREA)
        try {
            val addrList1: List<Address>? = gc.getFromLocationName(addr1, 5)
            val addrList2: List<Address>? = gc.getFromLocationName(addr2, 5)
            if (addrList1 != null && addrList2 != null) {
                lat1 = addrList1[0].latitude
                lon1 = addrList1[0].longitude
                lat2 = addrList2[0].latitude
                lon2 = addrList2[0].longitude
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    interface OnFragmentInteractionListener {}

    companion object {
        private const val ApiKey = "l7xxfee14d4fa6ac4197a95728747f7f1da4"
        fun initTime(): String {
            val formatter = SimpleDateFormat("yyyyMMdd_HHmmss") // output form
            val now = Date() // date on system
            return formatter.format(now)
        }
    }
}