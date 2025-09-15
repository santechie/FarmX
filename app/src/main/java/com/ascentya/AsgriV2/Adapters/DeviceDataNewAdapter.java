package com.ascentya.AsgriV2.Adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.Activitys.DeviceDataActivity;
import com.ascentya.AsgriV2.Models.DeviceData;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DeviceDataUtils;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.e_market.utils.DateUtils;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceDataNewAdapter extends RecyclerView.Adapter<DeviceDataNewAdapter.DeviceDataViewHolder> {

    public static final int MASTER_HEAD = 1;
    public static final int SLAVE_HEAD = 2;
    public static final int MASTER_ITEM = 3;
    public static final int SLAVE_ITEM = 4;

    private boolean expandMaster = false, expandSlave = false;
    private ArrayList<BaseItem> optimizedDeviceData = new ArrayList<>();
    private DeviceDataAdapter.Action action;
    public int expandedMasterId = -1/*, expandedSlaveId = -1*/;

    public DeviceDataNewAdapter(DeviceDataAdapter.Action action) {
        this.action = action;
    }

    @NonNull
    @Override
    public DeviceDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case MASTER_HEAD:
            case SLAVE_HEAD:
                view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_device_data_head_item, parent, false);
            break;
            case MASTER_ITEM:
            case SLAVE_ITEM:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_device_data_item, parent, false);
                break;
        }
        return new DeviceDataViewHolder(view, this, action);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceDataViewHolder holder, int position) {
        holder.update(optimizedDeviceData.get(position));
    }

    public void update(){
        prepareList();
        notifyDataSetChanged();
    }

    private void prepareList(){
        optimizedDeviceData.clear();
        ArrayList<DeviceData> masterDataList = new ArrayList<>();
        for (int m=0; m<action.getDataList().size(); m++){
            DeviceData deviceData = action.getDataList().get(m);
            if (deviceData.getDeviceType().equals("master")){
                if (!isDeviceExist(deviceData.getDeviceId(), masterDataList)){
                    masterDataList.add(deviceData);
                }else {
                    if (isUpdatedData(deviceData, getDeviceData(deviceData.getDeviceId(), masterDataList))){
                        masterDataList.remove(getDeviceData(deviceData.getDeviceId(), masterDataList));
                        masterDataList.add(deviceData);
                    }
                }
            }
        }

        ArrayList<Integer> masterIds = new ArrayList<>();

        for (int d=0; d<action.getDataList().size(); d++){
            DeviceData deviceData = action.getDataList().get(d);
            if (deviceData.getDeviceType().equals("master")){
                if (!masterIds.contains(deviceData.getDeviceId())){
                    masterIds.add(deviceData.getDeviceId());
                }
            }else {
                if (!masterIds.contains(deviceData.getMasterId())){
                    masterIds.add(deviceData.getMasterId());
                }
            }
        }

        Collections.sort(masterIds);

        System.out.println("Master Id's: " + GsonUtils.getGson().toJson(masterIds));

        for (int m=0; m<masterIds.size(); m++){

            DeviceData masterData = getDeviceData(masterIds.get(m), masterDataList);

            if (masterData == null){
                masterData = new DeviceData();
                masterData.setDeviceId(masterIds.get(m));
                masterData.setDeviceType("master");
            }

            if (!isDeviceExist(masterData.getDeviceId(), MASTER_HEAD, optimizedDeviceData))
                optimizedDeviceData.add(new HeadItem(masterData));

            if (expandedMasterId == masterData.getDeviceId()) {
                if (masterData.hasValidData())
                    optimizedDeviceData.add(new ViewItem(masterData));
                ArrayList<DeviceData> slaveDataList = getUpdatedSlaveList(masterData.getDeviceId());
                for (int s=0; s<slaveDataList.size(); s++){
                    DeviceData slaveData = slaveDataList.get(s);
                    //optimizedDeviceData.add(new HeadItem(slaveData));
                    optimizedDeviceData.add(new ViewItem(slaveData));
                   /* if (expandedSlaveId == slaveData.getDeviceId()){ }*/
                }
            }
        }
    }

    private ArrayList<DeviceData> getUpdatedSlaveList(int masterDeviceId){
        ArrayList<DeviceData> slaveDataList = new ArrayList<>();
        for (int s=0; s<action.getDataList().size(); s++){
            DeviceData slaveData = action.getDataList().get(s);
            if (slaveData.getMasterId() == masterDeviceId){
                if (isDeviceExist(slaveData.getDeviceId(), slaveDataList)){
                    DeviceData oldDeviceData = getDeviceData(slaveData.getDeviceId(), slaveDataList);
                    if (isUpdatedData(slaveData, oldDeviceData)){
                        slaveDataList.remove(oldDeviceData);
                        slaveDataList.add(slaveData);
                    }
                }else {
                    slaveDataList.add(slaveData);
                }
            }
        }

        Collections.sort(slaveDataList, new Comparator<DeviceData>() {
            @Override
            public int compare(DeviceData deviceData, DeviceData t1) {
                return deviceData.getDeviceId() - t1.getDeviceId();
            }
        });

        return slaveDataList;
    }

    private boolean isDeviceExist(int deviceId, int type, ArrayList<BaseItem> baseItemList){
        for (int i=0; i<baseItemList.size(); i++){
            if (baseItemList.get(i).getDeviceData().getDeviceId() == deviceId
                    && baseItemList.get(i).getType() == type){
                return true;
            }
        }
        return false;
    }

    private boolean isDeviceExist(int deviceId, ArrayList<DeviceData> deviceDataList){
        for (int i=0; i<deviceDataList.size(); i++){
            if (deviceDataList.get(i).getDeviceId() == deviceId){
                return true;
            }
        }
        return false;
    }

    private BaseItem getDeviceData(int deviceId, int type, ArrayList<BaseItem> baseItemList){
        for (int d=0; d<baseItemList.size(); d++){
            if (baseItemList.get(d).getDeviceData().getDeviceId() == deviceId
                    && baseItemList.get(d).getType() == type){
                return baseItemList.get(d);
            }
        }
        return null;
    }

    private DeviceData getDeviceData(int deviceId, ArrayList<DeviceData> deviceDataList){
        for (int d=0; d<deviceDataList.size(); d++){
            if (deviceDataList.get(d).getDeviceId() == deviceId){
                return deviceDataList.get(d);
            }
        }
        return null;
    }

    private boolean isUpdatedData(DeviceData newDeviceData, DeviceData oldDeviceData){
        return new Date(newDeviceData.getCreatedAt()).after(new Date(oldDeviceData.getCreatedAt()));
    }

    @Override
    public int getItemViewType(int position) {
        return optimizedDeviceData
                .get(position)
                .getType();
    }

    @Override
    public int getItemCount() {
        return optimizedDeviceData.size();
    }

    public void setSelected(int type, int deviceId){
        if (type == MASTER_HEAD){
            if (expandedMasterId != deviceId) {
                expandedMasterId = deviceId;
            }else {
                expandedMasterId = -1;
            }
            //expandedSlaveId = -1;
        }else if (type == SLAVE_HEAD){
            /*if (expandedSlaveId != deviceId) {
                expandedSlaveId = deviceId;
            }else {
                expandedSlaveId = -1;
            }*/
        }
        update();
    }

    public void resetSelected(){
        expandedMasterId = -1;
        //expandedSlaveId = -1;
        update();
    }

    class DeviceDataViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTv, headTv, dateTv, timeTv;
        private ImageView chartIv, arrowIv;
        private RecyclerView gridView;
        private GridAdapter adapter;
        private BaseItem baseItem;
        private DeviceData deviceData;
        private DeviceDataNewAdapter mainAdapter;

        public DeviceDataViewHolder(@NonNull View itemView, DeviceDataNewAdapter adapter, DeviceDataAdapter.Action action) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameTv);
            arrowIv = itemView.findViewById(R.id.arrowIv);

            headTv = itemView.findViewById(R.id.head);
            dateTv = itemView.findViewById(R.id.date);
            timeTv = itemView.findViewById(R.id.time);
            chartIv = itemView.findViewById(R.id.chartIcon);
            gridView = itemView.findViewById(R.id.gridView);

            mainAdapter = adapter;

            if (chartIv != null)
                chartIv.setOnClickListener(view ->
                        action.onChartClicked(deviceData.getDeviceId()));

            itemView.setOnClickListener(view -> clickedView());
        }

        private void setGridAdapter(){
            if (adapter != null)
                return;

            DividerItemDecoration verticalDiv = new DividerItemDecoration(itemView.getContext(),
                    LinearLayoutManager.VERTICAL);
            verticalDiv.setDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.line_drawable));
            DividerItemDecoration horizontalDiv = new DividerItemDecoration(itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL);
            horizontalDiv.setDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.line_drawable));
            gridView.addItemDecoration(verticalDiv);
            gridView.addItemDecoration(horizontalDiv);
            adapter = new GridAdapter(action);
            gridView.setAdapter(adapter);
        }

        public void update(BaseItem baseItem) {

            this.baseItem = baseItem;
            this.deviceData = baseItem.deviceData;

            if (baseItem instanceof HeadItem){
                setHeadItem(baseItem.type);
            }else if (baseItem instanceof ViewItem){
                setViewItem(baseItem.type);
            }

            /*
            String nameText = "";
            if (getItemViewType() == 1) {
                nameText = "Master " + "<b>" + deviceData.getDeviceId() + "</b>";
                ((GridLayoutManager) gridView.getLayoutManager()).setSpanCount(3);
            } else {
                nameText = "Slave " + "<b>" + deviceData.getDeviceId() + "</b>";
                ((GridLayoutManager) gridView.getLayoutManager()).setSpanCount(3);
            }
            nameTv.setText(Html.fromHtml(nameText));
            dateTv.setText(DateUtils.splitDate(deviceData.getCreatedAt()));
            timeTv.setText(DateUtils.splitTime(deviceData.getCreatedAt()));
            adapter.setDeviceData(deviceData, getItemViewType());*/
        }

        private void setHeadItem(int type){
            if (type == MASTER_HEAD){
                String nameText = "Master " + "<b>" + deviceData.getDeviceId() + "</b>";
                nameTv.setText(Html.fromHtml(nameText));
                arrowIv.setImageResource(mainAdapter.expandedMasterId == deviceData.getDeviceId()?
                        R.drawable.ic_arrow_up : R.drawable.ic_baseline_arrow_drop_down_24);
            } else if (type == SLAVE_HEAD){
                String nameText = "Slave " + "<b>" + deviceData.getDeviceId() + "</b>";;
                nameTv.setText(Html.fromHtml(nameText));
                /*arrowIv.setImageResource(mainAdapter.expandedSlaveId == deviceData.getDeviceId()?
                        R.drawable.ic_arrow_up : R.drawable.ic_baseline_arrow_drop_down_24);*/
            }

        }

        private void setViewItem(int type){
            int viewType = 0;
            int spanCount = 3;
            setGridAdapter();
            ((GridLayoutManager) gridView.getLayoutManager()).setSpanCount(spanCount);
            String nameText = "";
            if (type == MASTER_ITEM){
                viewType = 1;
                nameText = "Master " + "<b>" + deviceData.getDeviceId() + "</b>";;
            }else if (type == SLAVE_ITEM){
                viewType = 1;
                //viewType = 2;
                nameText = "Slave " + "<b>" + deviceData.getDeviceId() + "</b>";;
            }
            headTv.setText(Html.fromHtml(nameText));
            dateTv.setText(DateUtils.splitDate(deviceData.getCreatedAt()));
            timeTv.setText(DateUtils.splitTime(deviceData.getCreatedAt()));
            adapter.setDeviceData(deviceData, viewType);
        }

        private void clickedView(){
            action.onViewClicked(baseItem.getType(), deviceData.getDeviceId());
        }

    }

    public static class GridAdapter extends RecyclerView.Adapter<GridAdapter.DataItemViewHolder> {

        private DeviceDataAdapter.Action action;
        private ArrayList<DeviceDataItem> deviceDataItems = new ArrayList<>();
        private int viewType = 0;

        private String image = null;

        public GridAdapter(DeviceDataAdapter.Action action) {
            this.action = action;
        }

        public void setDeviceData(DeviceData deviceData, String image, int viewType){
            this.image = image;
            this.setDeviceData(deviceData, viewType);
        }

        public void setDeviceData(DeviceData deviceData, int viewType) {
            this.viewType = viewType;

            deviceDataItems.clear();

            if (image != null){
                DeviceDataItem imageItem = new DeviceDataItem();
                imageItem.image = image;
                deviceDataItems.add(imageItem);
            }

            DeviceDataItem temperatureData = new DeviceDataItem();
            temperatureData.name = DeviceDataActivity.TEMPERATURE;
            temperatureData.icon = R.drawable.ic_temperature;
            temperatureData.symbol = DeviceDataActivity.TEMPERATURE_SYMBOL;
            temperatureData.value = deviceData.getTemperature() + "";
            temperatureData.rangeFrom = getRange(DeviceDataActivity.TEMPERATURE, true);
            temperatureData.rangeTo = getRange(DeviceDataActivity.TEMPERATURE, false);
            temperatureData.valueIcon = getValueIcon(temperatureData);

            deviceDataItems.add(temperatureData);

            DeviceDataItem humidityData = new DeviceDataItem();
            humidityData.name = DeviceDataActivity.HUMIDITY;
            humidityData.icon = R.drawable.ic_humidity;
            humidityData.symbol = DeviceDataActivity.HUMIDITY_SYMBOL;
            humidityData.value = deviceData.getHumidity() + "";
            humidityData.rangeFrom = getRange(DeviceDataActivity.HUMIDITY, true);
            humidityData.rangeTo = getRange(DeviceDataActivity.HUMIDITY, false);
            humidityData.valueIcon = getValueIcon(humidityData);

            deviceDataItems.add(humidityData);

            DeviceDataItem phData = new DeviceDataItem();
            phData.name = DeviceDataActivity.PH;
            phData.icon = R.drawable.ic_ph;
            phData.symbol = DeviceDataActivity.PH_SYMBOL;
            phData.value = deviceData.getPh() + "";
            phData.rangeFrom = getRange(DeviceDataActivity.PH, true);
            phData.rangeTo = getRange(DeviceDataActivity.PH, false);
            phData.valueIcon = getValueIcon(phData);

            deviceDataItems.add(phData);

            DeviceDataItem moistureData = new DeviceDataItem();
            moistureData.name = DeviceDataActivity.MOISTURE;
            moistureData.icon = R.drawable.ic_moisture;
            moistureData.symbol = DeviceDataActivity.MOISTURE_SYMBOL;
            moistureData.value = deviceData.getSoilMoisture() + "";
            moistureData.rangeFrom = getRange(DeviceDataActivity.MOISTURE, true);
            moistureData.rangeTo = getRange(DeviceDataActivity.MOISTURE, false);
            moistureData.valueIcon = getValueIcon(moistureData);

            deviceDataItems.add(moistureData);

            DeviceDataItem visibilityData = new DeviceDataItem();
            visibilityData.name = DeviceDataActivity.VISIBILITY;
            visibilityData.icon = R.drawable.ic_visibility;
            visibilityData.symbol = DeviceDataActivity.VISIBILITY_SYMBOL;
            visibilityData.value = deviceData.getLight() + "";
            visibilityData.rangeFrom = getRange(DeviceDataActivity.VISIBILITY, true);
            visibilityData.rangeTo = getRange(DeviceDataActivity.VISIBILITY, false);
            visibilityData.valueIcon = getValueIcon(visibilityData);

            deviceDataItems.add(visibilityData);

            notifyDataSetChanged();
        }

        @DrawableRes
        private int getValueIcon(DeviceDataItem deviceDataItem){
            try {
                float value = Float.parseFloat(deviceDataItem.value);
                float from = Float.parseFloat(deviceDataItem.rangeFrom.trim());
                float to = Float.parseFloat(deviceDataItem.rangeTo.trim());
                if (value < from){
                    return R.drawable.low;
                }else if (value > to){
                    return R.drawable.high;
                }else {
                    return R.drawable.pay_tick;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return R.drawable.ic_error;
        }

        private String getRange(String type, boolean isFrom){
            if (isFrom)
                return action.getActualRange(type).split("-")[0];
            else
                return action.getActualRange(type).split("-")[1];
        }

        @NonNull
        @Override
        public DataItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == 100) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_device_data_image, parent, false);
            } else if (this.viewType == 1){
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_device_data_item_item_4, parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_device_data_item_item_3, parent, false);
            }
            return new DataItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DataItemViewHolder holder, int position) {
            try {
                holder.update(deviceDataItems.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return deviceDataItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 && deviceDataItems.get(position).isImage()) return 100;
            return viewType;
        }

        class DataItemViewHolder extends RecyclerView.ViewHolder {

            private TextView nameTv, valueTv, valueRangeTv;
            private ImageView image, valueImage;

            public DataItemViewHolder(@NonNull View itemView) {
                super(itemView);

                nameTv = itemView.findViewById(R.id.name);
                valueTv = itemView.findViewById(R.id.value);
                valueRangeTv = itemView.findViewById(R.id.range);

                image = itemView.findViewById(R.id.image);
                valueImage = itemView.findViewById(R.id.valueImage);
            }

            public void update(DeviceDataItem deviceDataItem) throws Exception {

                if (deviceDataItem.isImage()){
                    Glide.with(itemView).load(deviceDataItem.image).into(image);
                    return;
                }

                boolean isVisibility = deviceDataItem.name.equals(DeviceDataActivity.VISIBILITY);
                boolean isPh = deviceDataItem.name.equals(DeviceDataActivity.PH);

                String value = new DecimalFormat("#").format(Float.parseFloat(deviceDataItem.value));
                String from = new DecimalFormat("#").format(Float.parseFloat(deviceDataItem.rangeFrom));
                String to = new DecimalFormat("#").format(Float.parseFloat(deviceDataItem.rangeTo));

                if (isPh){
                    from = deviceDataItem.rangeFrom;
                    to = deviceDataItem.rangeTo;
                    value = String.format("%.2f", Float.parseFloat(deviceDataItem.value));
                }

                nameTv.setText(deviceDataItem.name);

                if (isVisibility){
                    valueTv.setText(DeviceDataUtils.formatLightValue(value, deviceDataItem.symbol));
                    valueRangeTv.setText(from + " - " + DeviceDataUtils.formatLightValue(to, deviceDataItem.symbol));
                }else {
                    valueTv.setText(value + "" + deviceDataItem.symbol);
                    valueRangeTv.setText(from + deviceDataItem.symbol + " - " + to + "" + deviceDataItem.symbol);
                }
                image.setImageResource(deviceDataItem.icon);
                valueImage.setImageResource(deviceDataItem.valueIcon);
            }
        }
    }

    public static class DeviceDataItem {
        public int icon, valueIcon;
        public String name, value, symbol, image;
        public String rangeFrom, rangeTo;

        public boolean isImage() {
            return image != null;
        }
    }

    public class BaseItem{
        private DeviceData deviceData;
        private int type;

        private BaseItem(DeviceData deviceData){
            setDeviceData(deviceData);
        }

        public DeviceData getDeviceData() {
            return deviceData;
        }

        public void setDeviceData(DeviceData deviceData) {
            this.deviceData = deviceData;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public class HeadItem extends BaseItem{
        private HeadItem(DeviceData deviceData) {
            super(deviceData);
            setType(deviceData.getDeviceType()
                    .equals("master") ? MASTER_HEAD : SLAVE_HEAD);
        }

    }

    public class ViewItem extends BaseItem{
        private ViewItem(DeviceData deviceData) {
            super(deviceData);
            setType(deviceData.getDeviceType()
                    .equals("master") ? MASTER_ITEM : SLAVE_ITEM);
        }
    }
}
