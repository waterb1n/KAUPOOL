package com.water.kaupool

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import java.io.IOException

@RequiresApi(api = Build.VERSION_CODES.N)
class FragmentB : Fragment() {
    var listView: ListView? = null
    var curAddress: TextView? = null
    var locationManager: LocationManager? = null
    var latitude = 0.0
    var longitude = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
        UserData()
        startLocationService()
    }

    private fun init() {
        manage_list = ArrayList()
        listView = activity!!.findViewById<View>(R.id.listView) as ListView
        curAddress = activity!!.findViewById<View>(R.id.curAddress) as TextView

        listView!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val Tab = (activity as MainActivity?)!!.fragment
            val Frag = activity!!.supportFragmentManager.findFragmentByTag(Tab) as FragmentA?
            Frag!!.setClickedData(position)
            (activity as MainActivity?)?.getViewPager()?.currentItem = 0
        }

        listView!!.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id ->
            DeleteData(position)
            true
        }
    }

    fun UserData() {
        val manage_query = LoginActivity.db_manager!!.orderByChild("date")
        manage_list!!.clear()

        manage_query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                manage_list!!.clear()
                for (data in dataSnapshot.children) {
                    if (data.value != null) {
                        val m = data.getValue(manager::class.java)
                        if (m!!.check) {
                            manage_list!!.add(m)
                        }
                    }
                }

                if (activity != null) {
                    val adapter = MyListAdapter(activity!!, R.layout.data_list, manage_list!!)
                    listView!!.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun DeleteData(pos: Int) {
        val manage_query = LoginActivity.db_manager!!.orderByChild("date")
        manage_list!!.clear()

        manage_query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                manage_list!!.clear()
                for (data in dataSnapshot.children) {
                    if (data.value != null) {
                        val m = data.getValue(manager::class.java)
                        if (m!!.check) {
                            manage_list!!.add(m)
                        }
                    }
                }
                if (manage_list!!.size != null) {
                    manage_list!![pos]!!.check = false
                    LoginActivity.db_manager!!.child(manage_list!![pos]!!.date!!).setValue(manage_list!![pos])
                }


                if (activity != null) {
                    val adapter = MyListAdapter(activity!!, R.layout.data_list, manage_list!!)
                    listView!!.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    private fun startLocationService() {
        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val minTime: Long = 10 // update time
        val minDistance = 1f // update distance
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "Don't have permissions.", Toast.LENGTH_LONG).show()
            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener)
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocationListener)
        // location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
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
            val address = searchAddress(activity, latitude, longitude)
            val beginIdx = address!!.indexOf("시") + 2
            val endIdx = address!!.indexOf("동") + 1
            //GPS OFF 에서 작동하지 못함. 예외처리 필요
            //val curaddress = address.substring(beginIdx, endIdx)
            //curAddress!!.setText(curaddress)
            curAddress!!.setText(address)
            stopLocationService() // 미수신시
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    // lat, lon to address in KOREA
    open fun searchAddress(mContext: Context?, latitude: Double, longitude: Double): String? {
        var nowAddress: String? = "GPS ERROR"
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

    interface OnFragmentInteractionListener {}

    companion object {
        var manage_list: ArrayList<manager>? = null
    }
}