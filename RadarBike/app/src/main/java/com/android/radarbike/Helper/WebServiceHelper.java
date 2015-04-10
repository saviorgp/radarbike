package com.android.radarbike.Helper;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.android.radarbike.model.PositionsVO;
import com.android.radarbike.model.component.WebServiceWrapper;
import com.android.radarbike.utils.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlexGP on 25/03/2015.
 */
public class WebServiceHelper {

    public static List<PositionsVO> getPositions(){
        List<PositionsVO> posList = new ArrayList<PositionsVO>();
        try {
            JSONObject data = WebServiceWrapper.callGetPositions();
            JSONArray positions = data.getJSONArray("positions");
            for(int i=0;i<positions.length();i++){
                PositionsVO vo = new PositionsVO();
                vo.setLat(((JSONObject) positions.get(i)).getDouble("lat"));
                vo.setLng(((JSONObject) positions.get(i)).getDouble("lng"));
                posList.add(vo);
            }
        } catch(Throwable t){
            Logger.LOGE(t.getMessage());
        }

        return posList;
    }

    public static void sendPosition(Context context, PositionsVO vo){
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        WebServiceWrapper.callSetPosition(imei,vo.getLat(),vo.getLng());
    }
}