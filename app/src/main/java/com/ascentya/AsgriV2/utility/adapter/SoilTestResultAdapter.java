package com.ascentya.AsgriV2.utility.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.data.Constants;
import com.ascentya.AsgriV2.utility.model.SoilTest;
import com.ascentya.AsgriV2.utility.model.WaterTest;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SoilTestResultAdapter extends RecyclerView.Adapter<SoilTestResultAdapter.SoilTestResultViewHolder> {

    private ArrayList<SoilTestResultItem> items = new ArrayList<>();

    @NonNull
    @Override
    public SoilTestResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_soil_test_result_item, parent, false);
        return new SoilTestResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoilTestResultViewHolder holder, int position) {
        holder.update(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void update(WaterTest waterTest){
        items.clear();

        SoilTestResultItem calciumItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.CALCIUM, waterTest.getCalcium());
        SoilTestResultItem magnesiumItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.MAGNESIUM, waterTest.getMagnesium());
        SoilTestResultItem sodiumItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.SODIUM, waterTest.getSodium());
        SoilTestResultItem potassiumItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.POTASSIUM, waterTest.getPotassium());
        SoilTestResultItem boronItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.BORON, waterTest.getBoron());
        SoilTestResultItem carbonateItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.CARBONATE, waterTest.getCarbonate());
        SoilTestResultItem bicarbonateItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.BICARBONATE, waterTest.getBicarbonate());
        SoilTestResultItem sulfateItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.SULFATE, waterTest.getSulfate());
        SoilTestResultItem chlorideItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.CHLORIDE, waterTest.getChloride());
        SoilTestResultItem nitrateItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.NITRATE, waterTest.getNitrate());
        SoilTestResultItem phosphorusItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.PHOSPHORUS, waterTest.getPhosphorus());
        SoilTestResultItem phItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.PH, waterTest.getPh());
        SoilTestResultItem conductivityItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.CONDUCTIVITY, waterTest.getConductivity());
        SoilTestResultItem hardnessItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.HARDNESS, waterTest.getHardness());
        SoilTestResultItem alkalinityItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.ALKALINITY, waterTest.getAlkalinity());
        SoilTestResultItem dissolvedSaltsItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.DISSOLVED_SALTS, waterTest.getTotalDissolvedSalts());
        SoilTestResultItem sarItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.SAR, waterTest.getSar());

        items.add(calciumItem);
        items.add(magnesiumItem);
        items.add(sodiumItem);
        items.add(potassiumItem);
        items.add(boronItem);
        items.add(carbonateItem);
        items.add(bicarbonateItem);
        items.add(sulfateItem);
        items.add(chlorideItem);
        items.add(nitrateItem);
        items.add(phosphorusItem);
        items.add(phItem);
        items.add(conductivityItem);
        items.add(hardnessItem);
        items.add(alkalinityItem);
        items.add(dissolvedSaltsItem);
        items.add(sarItem);

        notifyDataSetChanged();
    }

    public void update(SoilTest soilTest){

        items.clear();

        SoilTestResultItem phItem = SoilTestResultItem
                .get(Constants.SoilTestResultFields.PH, soilTest.getPhAcidity());
        SoilTestResultItem phosphorus = SoilTestResultItem
                .get(Constants.SoilTestResultFields.PHOSPHORUS, soilTest.getPhosphorus());
        SoilTestResultItem salinity = SoilTestResultItem
                .get(Constants.SoilTestResultFields.SALINITY, soilTest.getSalinity());
        SoilTestResultItem sodium = SoilTestResultItem
                .get(Constants.SoilTestResultFields.SODIUM, soilTest.getSodium());
        SoilTestResultItem sulphur = SoilTestResultItem
                .get(Constants.SoilTestResultFields.SULPHUR, soilTest.getSulphur());
        SoilTestResultItem nitrogen = SoilTestResultItem
                .get(Constants.SoilTestResultFields.NITROGEN, soilTest.getNitrogen());
        SoilTestResultItem calcium = SoilTestResultItem
                .get(Constants.SoilTestResultFields.CALCIUM, soilTest.getCalcium());
        SoilTestResultItem manganese = SoilTestResultItem
                .get(Constants.SoilTestResultFields.MANGANESE, soilTest.getManganese());
        SoilTestResultItem copper = SoilTestResultItem
                .get(Constants.SoilTestResultFields.COPPER, soilTest.getCopper());
        SoilTestResultItem potassium = SoilTestResultItem
                .get(Constants.SoilTestResultFields.POTASSIUM, soilTest.getPotassium());
        SoilTestResultItem magnesium = SoilTestResultItem
                .get(Constants.SoilTestResultFields.MAGNESIUM, soilTest.getMagnesium());
        SoilTestResultItem zinc = SoilTestResultItem
                .get(Constants.SoilTestResultFields.ZINC, soilTest.getZinc());
        SoilTestResultItem iron = SoilTestResultItem
                .get(Constants.SoilTestResultFields.IRON, soilTest.getIron());

        items.add(phItem);
        items.add(phosphorus);
        items.add(salinity);
        items.add(sodium);
        items.add(sulphur);
        items.add(nitrogen);
        items.add(calcium);
        items.add(manganese);
        items.add(copper);
        items.add(potassium);
        items.add(magnesium);
        items.add(zinc);
        items.add(iron);

        notifyDataSetChanged();

    }

    class SoilTestResultViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTv, valueTv;
        private ImageView imageView;

        public SoilTestResultViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.name);
            valueTv = itemView.findViewById(R.id.value);
            imageView = itemView.findViewById(R.id.image);
        }

        public void update(SoilTestResultItem item){
            imageView.setImageResource(item.base.getIcon());
            nameTv.setText(item.base.getName());
            valueTv.setText(item.value);
        }

    }

    static class SoilTestResultItem{
        Constants.SoilTestResultFields.Base base;
        String value;

        public SoilTestResultItem(Constants.SoilTestResultFields.Base base, String value) {
            this.base = base;
            this.value = value;
        }

        public static SoilTestResultItem get(Constants.SoilTestResultFields.Base base, String value){
            return new SoilTestResultItem(base, value);
        }
    }
}
