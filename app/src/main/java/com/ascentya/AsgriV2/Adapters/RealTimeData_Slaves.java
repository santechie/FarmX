package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ascentya.AsgriV2.Models.CropEssentials_Model;
import com.ascentya.AsgriV2.Models.MylandMaster_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;

import org.apache.commons.lang3.Range;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RealTimeData_Slaves extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MylandMaster_Model> items = new ArrayList<>();
    private Context ctx;
    private Boolean masterslave;
    String Humidity_ts, pollution_ts, moisture_ts, light_ts, temt_ts, ph_ts;
    String crop_Id;
    ViewDialog viewDialog;
    CropEssential_Adapter cropEssentialAdapter;

    RecyclerView cropessentials_recycler;


    List<CropEssentials_Model> Data;

    String Temp_url, humidity_url, soilmos_url, ph_url, sunlight_url;
    String pos;

    public RealTimeData_Slaves(Context context, List<MylandMaster_Model> items, Boolean master, ViewDialog viewDialog, String pos) {
        this.items = items;
        ctx = context;
        this.masterslave = master;
        this.pos = pos;
        this.viewDialog = viewDialog;


        if (pos.equalsIgnoreCase("1")) {
            Temp_url = "https://api.thingspeak.com/channels/1342067/fields/2.json?api_key=XOHV91WYTJ1ERG87&results=2";
            humidity_url = "https://api.thingspeak.com/channels/1342067/fields/3.json?api_key=XOHV91WYTJ1ERG87&results=2";
            soilmos_url = "https://api.thingspeak.com/channels/1342067/fields/4.json?api_key=XOHV91WYTJ1ERG87&results=2";
            ph_url = "https://api.thingspeak.com/channels/1342067/fields/5.json?api_key=XOHV91WYTJ1ERG87&results=2";
            sunlight_url = "https://api.thingspeak.com/channels/1342067/fields/6.json?api_key=XOHV91WYTJ1ERG87&results=2";

        } else {
            Temp_url = "https://api.thingspeak.com/channels/1342078/fields/2.json?api_key=KBE9BVWPWVID77PQ&results=2";
            humidity_url = "https://api.thingspeak.com/channels/1342078/fields/3.json?api_key=KBE9BVWPWVID77PQ&results=2";
            soilmos_url = "https://api.thingspeak.com/channels/1342078/fields/4.json?api_key=KBE9BVWPWVID77PQ&results=2";
            ph_url = "https://api.thingspeak.com/channels/1342078/fields/5.json?api_key=KBE9BVWPWVID77PQ&results=2";
            sunlight_url = "https://api.thingspeak.com/channels/1342078/fields/6.json?api_key=KBE9BVWPWVID77PQ&results=2";

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title, status;


        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            status = (TextView) v.findViewById(R.id.status);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_real_time__data, parent, false);
        vh = new ViewHolder(v);
        cropessentials_recycler = v.findViewById(R.id.cropessentials_recycler);
        cropessentials_recycler.setLayoutManager(new LinearLayoutManager(ctx));
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final MylandMaster_Model Position_Object = items.get(position);

            gethumidity(getCategoryPos("tomato"));


//            view.title.setText(Position_Object.getMastername());
//            view.status.setText(Position_Object.getMaster_status());

//            view.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (masterslave) {
//                        Intent i = new Intent(ctx, MyLand_Slaves.class);
//                        ctx.startActivity(i);
//                    } else {
//
//                        Intent i = new Intent(ctx, RealTime_Data.class);
//                        ctx.startActivity(i);
//
//                    }
//
//                }
//            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void gethumidity(final Integer pos) {
        viewDialog.showDialog();
        Data = new ArrayList<>();
        AndroidNetworking.get(humidity_url)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    String hum_val = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field3");


                    if (hum_val.matches("-?(0|[1-9]\\d*)")) {
                        Range<Integer> test = Range.between(0, 100);
                        if (test.contains(Integer.parseInt(hum_val))) {
                            Humidity_ts = hum_val;
                        } else {
                            Humidity_ts = "0";
                        }
                    } else {
                        Humidity_ts = "0";
                    }
                    getcropid(pos);


                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }

    public void getcropid(final Integer pos) {

        AndroidNetworking.get("https://api.thingspeak.com/channels/1278986/fields/1.json?api_key=F0IZUDQ9RUJEH7HZ&results=2")

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");


                    crop_Id = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field1");

//                    search_name.setText("Node Id : " + crop_Id);

                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }

                getmoisture(pos);
            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }

    public void getmoisture(final Integer pos) {

        AndroidNetworking.get(soilmos_url)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    String moisture_val = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field4");


                    if (moisture_val.matches("-?(0|[1-9]\\d*)")) {
                        Range<Integer> test = Range.between(0, 100);
                        if (test.contains(Integer.parseInt(moisture_val))) {
                            moisture_ts = moisture_val;
                        } else {
                            moisture_ts = "0";
                        }
                    } else {
                        moisture_ts = "0";
                    }

                    getlight(pos);


                } catch (Exception e) {
                    viewDialog.hideDialog();

                    e.printStackTrace();
                }


            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }

    public void getlight(final Integer pos) {

        AndroidNetworking.get(sunlight_url)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    String light_val = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field6");


                    try {
                        Double temt_val = Double.valueOf(light_val);
                        Range<Double> test = Range.between(0.00, 5000.00);
                        if (test.contains(temt_val)) {
                            light_ts = String.valueOf(temt_val);
                        } else {
                            light_ts = "0";
                        }
                    } catch (Exception e) {
                        light_ts = "0";
                    }


//                    if (light_val.matches("-?(0|[1-9]\\d*)")) {
//                        Range<Integer> test = Range.between(0, 5000);
//                        if (test.contains(Integer.parseInt(light_val))) {
//                            light_ts = light_val;
//                        } else {
//                            light_ts = "0";
//                        }
//                    } else {
//                        light_ts = "0";
//                    }


                    gettemt(pos);


                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }

    public void gettemt(final Integer pos) {

        AndroidNetworking.get(Temp_url)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    String temp = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field2");


                    try {
                        Double temt_val = Double.valueOf(temp);
                        Range<Double> test = Range.between(0.00, 100.00);
                        if (test.contains(temt_val)) {
                            temt_ts = String.valueOf(temt_val);
                        } else {
                            temt_ts = "0";
                        }
                    } catch (Exception e) {
                        temt_ts = "0";
                    }


                    getph(pos);


                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }

    public void getph(final Integer pos) {

        AndroidNetworking.get(ph_url)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                viewDialog.hideDialog();
                try {

                    JSONArray jsonArray_humidity = jsonObject.getJSONArray("feeds");
                    String ph_val = jsonArray_humidity.getJSONObject(jsonArray_humidity.length() - 1).optString("field5");

                    try {
                        Range<Double> test = Range.between(0.00, 18.00);
                        if (test.contains(Double.parseDouble(ph_val))) {
                            ph_ts = ph_val;
                        } else {
                            ph_ts = "0";
                        }
                    } catch (Exception e) {
                        ph_ts = "0";
                    }


                } catch (Exception e) {
                    viewDialog.hideDialog();
                    e.printStackTrace();
                }


                CropEssentials_Model obj = new CropEssentials_Model();


                if (temt_ts.equalsIgnoreCase("0")) {
                    obj.setName("Temperature : Not Connected");

                } else {


//                    if (crop_Id.trim().equalsIgnoreCase(String.valueOf(pos).trim())) {
                    obj.setName("Temperature : " + temt_ts + " °C");

//                    } else {
//                        obj.setName("Temperature : Not Connected");
//                    }

                }
                obj.setDisc("This temperature might affect this crop");
                obj.setIcon(R.drawable.temperature);
                obj.setValues("26");
                obj.setTitle("temt");
                obj.setActual_value(Webservice.Data_crops.get(pos).getTempreture() + " %");
                String[] temstr = Webservice.Data_crops.get(pos).getTempreture().split("-", 2);

                obj.setSuccess(floatbetween(Float.parseFloat(temt_ts), Float.parseFloat(temstr[0].trim()), Float.parseFloat(temstr[1].trim())));
                obj.setStatusicon(R.drawable.equal);
                Data.add(obj);


                obj = new CropEssentials_Model();
                if (Humidity_ts.equalsIgnoreCase("0")) {
                    obj.setName("Humidity :  Not Connected");

                } else {
//                    if (crop_Id.trim().equalsIgnoreCase(String.valueOf(pos).trim())) {
                    obj.setName("Humidity : " + Humidity_ts + "%");
//                    } else {
//                        obj.setName("Humidity :  Not Connected");
//
//                    }

                }
                obj.setDisc("This is suitable for this crop");
                obj.setIcon(R.drawable.humidity);
                obj.setValues("26");
                obj.setTitle("Humidity");
                obj.setActual_value(Webservice.Data_crops.get(pos).getHumidity() + " %");
                String[] humstr = Webservice.Data_crops.get(pos).getHumidity().split("-", 2);
                obj.setSuccess(intbetween(Integer.parseInt(Humidity_ts), Integer.parseInt(humstr[0].trim()), Integer.parseInt(humstr[1].trim())));


                obj.setStatusicon(R.drawable.equal);
                Data.add(obj);
//                obj = new CropEssentials_Model();

//                if (pollution_ts.equalsIgnoreCase("0")) {
//                    obj.setName("Pollution :  Not Connected");
//
//                } else {
//                    if (crop_Id.trim().equalsIgnoreCase(String.valueOf(pos).trim())) {
//                        obj.setName("Pollution : " + pollution_ts + "%");
//
//
//                    } else {
//                        obj.setName("Pollution :  Not Connected");
//
//                    }
//
//                }
//                obj.setDisc("This pollution level may affect this crop");
//                obj.setIcon(R.drawable.pollution);
//                obj.setValues("26");
//                obj.setActual_value(Webservice.Data_crops.get(pos).getPollution() + " %");
//                obj.setTitle("Pollution");
//
//                String[] pollustr = Webservice.Data_crops.get(pos).getPollution().split("-", 2);
//                obj.setSuccess(intbetween(Integer.parseInt(pollution_ts), Integer.parseInt(pollustr[0].trim()), Integer.parseInt(pollustr[1].trim())));
//
//                obj.setStatusicon(R.drawable.equal);
//                Data.add(obj);

                obj = new CropEssentials_Model();


                if (ph_ts.equalsIgnoreCase("0")) {
                    obj.setName("PH - Not Connected");

                } else {
//                    if (crop_Id.trim().equalsIgnoreCase(String.valueOf(pos).trim())) {
                    obj.setName("PH : " + ph_ts);

//                    } else {
//                        obj.setName("PH - Not Connected");
//
//                    }

                }

                obj.setDisc("This PH level is suitable for this crop");
                obj.setIcon(R.drawable.ph);
                obj.setValues("26");
                obj.setTitle("Pollution");
                obj.setActual_value(Webservice.Data_crops.get(pos).getWaterph());

                String[] phstr = Webservice.Data_crops.get(pos).getWaterph().split("-", 2);
                obj.setSuccess(floatbetween(Float.parseFloat(ph_ts), Float.parseFloat(phstr[0].trim()), Float.parseFloat(phstr[1].trim())));

                obj.setStatusicon(R.drawable.success);
                Data.add(obj);


                obj = new CropEssentials_Model();

                if (moisture_ts.equalsIgnoreCase("0")) {
                    obj.setName("Moisture : Not Connected");

                } else {
//                    if (crop_Id.trim().equalsIgnoreCase(String.valueOf(pos).trim())) {
                    obj.setName("Moisture : " + moisture_ts + "%");

//                    } else {
//                        obj.setName("Moisture : Not Connected");
//
//
//                    }


                }
                obj.setDisc("This moisture level may affect this crop");
                obj.setIcon(R.drawable.moisure);
                obj.setValues("26");
                obj.setTitle("Pollution");
                obj.setActual_value(Webservice.Data_crops.get(pos).getMoisture() + "%");

                String[] moisustr = Webservice.Data_crops.get(pos).getMoisture().split("-", 2);
                obj.setSuccess(intbetween(Integer.parseInt(moisture_ts), Integer.parseInt(moisustr[0].trim()), Integer.parseInt(moisustr[1].trim())));


                obj.setStatusicon(R.drawable.equal);
                Data.add(obj);
                obj = new CropEssentials_Model();


                if (light_ts.equalsIgnoreCase("0")) {
                    obj.setName("Visibility : Not Connected");

                } else {
                    obj.setName("Visibility : " + light_ts);

                }


                obj.setDisc("This will not affect this crop");
                obj.setIcon(R.drawable.solids);
                obj.setValues("26");
                obj.setTitle("Pollution");
                obj.setActual_value("05-95 %");
                obj.setStatusicon(R.drawable.equal);
                obj.setSuccess("medium");
                Data.add(obj);


                obj = new CropEssentials_Model();


                obj.setName("NPK : ");


                obj.setDisc("This will not affect this crop");
                obj.setIcon(R.drawable.npk);
                obj.setValues("26");
                obj.setTitle("NPK");
                obj.setActual_value("0-100 %");
                obj.setStatusicon(R.drawable.equal);
                obj.setSuccess("medium");
                Data.add(obj);

                cropEssentialAdapter = new CropEssential_Adapter(ctx, Data);

                cropessentials_recycler.setAdapter(cropEssentialAdapter);

//                runLayoutAnimation(cropessentials_recycler);
//                runLayoutrightAnimation(crop_suggestion);


            }

            @Override
            public void onError(ANError anError) {

                viewDialog.hideDialog();
            }
        });
    }

    public static String floatbetween(float i, float minValueInclusive, float maxValueInclusive) {
        String range_string;
        if (i >= minValueInclusive && i <= maxValueInclusive) {

            range_string = "medium";
        } else if (i < minValueInclusive) {
            range_string = "low";
        } else if (i > maxValueInclusive) {
            range_string = "high";
        } else {
            range_string = "high";
        }


        return range_string;
    }

    public static String intbetween(int i, int minValueInclusive, int maxValueInclusive) {
        String range_string;
        if (i >= minValueInclusive && i <= maxValueInclusive) {

            range_string = "medium";
        } else if (i < minValueInclusive) {
            range_string = "low";
        } else if (i > maxValueInclusive) {
            range_string = "high";
        } else {
            range_string = "high";
        }


        return range_string;


        //        return (i >= minValueInclusive && i <= maxValueInclusive);
    }

    private int getCategoryPos(String category) {
//        Toast.makeText(this, String.valueOf(Webservice.crops.indexOf(category)), Toast.LENGTH_SHORT).show();
        return Integer.parseInt(Webservice.Data_crops.get(searchFor(category)).getCrop_id());
    }

    private Integer searchFor(String data) {
        Integer pos = 0;

        //notifiy adapter
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {


            String unitString = Webservice.Data_crops.get(i).getName().toLowerCase().trim();
            String C_name = Webservice.Data_crops.get(i).getS_name().toLowerCase().trim();


            if (unitString.equals(data.toLowerCase().trim()) || C_name.equals(data.toLowerCase().trim())) {


                pos = i;
                return pos;
            } else {
                pos = -1;
            }
        }
        return pos;
    }
}