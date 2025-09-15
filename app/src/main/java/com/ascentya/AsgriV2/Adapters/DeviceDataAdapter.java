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
import com.ascentya.AsgriV2.e_market.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceDataAdapter extends RecyclerView.Adapter<DeviceDataAdapter.DeviceDataViewHolder> {

    private Action action;

    public DeviceDataAdapter(Action action) {
        this.action = action;
    }

    @NonNull
    @Override
    public DeviceDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_device_data_item, parent, false);
        return new DeviceDataViewHolder(view, action);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceDataViewHolder holder, int position) {
        holder.update(action.getDataList().get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return action
                .getDataList()
                .get(position)
                .getDeviceType()
                .equals(DeviceDataActivity.MASTER) ? 1 : 2;
    }

    @Override
    public int getItemCount() {
        return action.getDataList().size();
    }

    class DeviceDataViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTv, dateTv, timeTv;
        private ImageView chartIv;
        private RecyclerView gridView;
        private GridAdapter adapter;
        private DeviceData deviceData;

        public DeviceDataViewHolder(@NonNull View itemView, Action action) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.head);
            dateTv = itemView.findViewById(R.id.date);
            timeTv = itemView.findViewById(R.id.time);
            chartIv = itemView.findViewById(R.id.chartIcon);
            gridView = itemView.findViewById(R.id.gridView);
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
            chartIv.setOnClickListener(view -> action.onChartClicked(deviceData.getDeviceId()));
        }

        public void update(DeviceData deviceData) {
            this.deviceData = deviceData;
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
            adapter.setDeviceData(deviceData, getItemViewType());
        }
    }

    class GridAdapter extends RecyclerView.Adapter<GridAdapter.DataItemViewHolder> {

        private Action action;
        private ArrayList<DeviceDataItem> deviceDataItems = new ArrayList<>();
        private int viewType = 0;

        public GridAdapter(Action action) {
            this.action = action;
        }

        public void setDeviceData(DeviceData deviceData, int viewType) {
            this.viewType = viewType;

            deviceDataItems.clear();

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
            phData.isDecimal = true;
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
            visibilityData.formatInK = true;
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
                float from = Float.parseFloat(deviceDataItem.rangeFrom);
                float to = Float.parseFloat(deviceDataItem.rangeTo);
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
            if (this.viewType == 1)
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_device_data_item_item_4, parent, false);
            else
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_device_data_item_item_3, parent, false);
            return new DataItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DataItemViewHolder holder, int position) {
            holder.update(deviceDataItems.get(position));
        }

        @Override
        public int getItemCount() {
            return deviceDataItems.size();
        }

        @Override
        public int getItemViewType(int position) {
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

            public void update(DeviceDataItem deviceDataItem) {

                String value = new DecimalFormat("#")
                        .format(Float.parseFloat(deviceDataItem.value));
                String from = new DecimalFormat("#")
                        .format(Float.parseFloat(deviceDataItem.rangeFrom));
                String to = new DecimalFormat("#")
                        .format(Float.parseFloat(deviceDataItem.rangeTo));

                if (deviceDataItem.isDecimal){
                    value = String.format("%0.1f", Float.parseFloat(deviceDataItem.value));
                }

                nameTv.setText(deviceDataItem.name);
                valueTv.setText((deviceDataItem.formatInK ?
                        DeviceDataUtils.formatLightValue(value, deviceDataItem.symbol)
                        : value)
                        + "" + deviceDataItem.symbol);
                valueRangeTv.setText(from + deviceDataItem.symbol + " - " + to + "" + deviceDataItem.symbol);
                image.setImageResource(deviceDataItem.icon);
                valueImage.setImageResource(deviceDataItem.valueIcon);
            }

        }
    }

    public interface Action {
        ArrayList<DeviceData> getDataList();
        void onChartClicked(int deviceId);
        void onViewClicked(int type, int deviceId);
        String getActualRange(String type);
    }

    public class DeviceDataItem {
        public int icon, valueIcon;
        public String name, value, symbol;
        public String rangeFrom, rangeTo;
        public boolean formatInK = false, isDecimal = false;
    }
}
