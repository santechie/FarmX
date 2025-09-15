package com.ascentya.AsgriV2.farmx.postharvest_diseas;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ascentya.AsgriV2.Models.DiseasesAi_Model;
import com.ascentya.AsgriV2.Models.Maincrops_Model;
import com.ascentya.AsgriV2.Models.PestsAi_Model;
import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.Utils.DebouncedOnClickListener;
import com.ascentya.AsgriV2.Utils.GsonUtils;
import com.ascentya.AsgriV2.Utils.ViewDialog;
import com.ascentya.AsgriV2.Utils.Webservice;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class PestsDisease_Activity extends AppCompatActivity {
    ImageButton pastePin;
    ImageView pest_image;
    TextView pests, type, crop, control_measure, pest_desc, pest_name, landName;

    List<PestsAi_Model> pest_Data;
    List<DiseasesAi_Model> diseases_Data;
    Button register;
    ViewDialog viewDialog;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pests_diseases_layout);
        pest_Data = new ArrayList<>();
        diseases_Data = new ArrayList<>();
        landName = findViewById(R.id.land);
        register = findViewById(R.id.register);
        back = findViewById(R.id.back);
        back.setOnClickListener(new DebouncedOnClickListener(1500) {
            @Override
            public void onDebouncedClick(View v) {
                onBackPressed();
            }
        });
        viewDialog = new ViewDialog(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PestsDisease_Activity.this, "Submitted", Toast.LENGTH_SHORT).show();
            }
        });

        Log.i(getClass().getSimpleName(), GsonUtils.getGson().toJson(getIntent().getStringExtra("crop_id")));

        pastePin = findViewById(R.id.pastePin);
        pest_name = findViewById(R.id.pest_name);
        pest_image = findViewById(R.id.pest_image);
        pest_desc = findViewById(R.id.pest_desc);
        control_measure = findViewById(R.id.control_measure);
        pests = findViewById(R.id.pests);
        type = findViewById(R.id.type);
        crop = findViewById(R.id.crop);
//        pests.setText(getIntent().getStringExtra("title"));


        if (getIntent().getBooleanExtra("type", true)) {
            type.setText("Main crop");
        } else {
            type.setText("Inter crop");
        }



        setCropName();

        pastePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {

                        viewDialog.showDialog();
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                String cropId = getIntent().getStringExtra("crop_id");

                                viewDialog.hideDialog();

                                if (getIntent().getStringExtra("title").equalsIgnoreCase("Pests")) {


                                    if (Webservice.Data_crops.get(searchFor(cropId)).getName().equalsIgnoreCase("banana")) {
                                        pests.setText("Pest :" + "\n Serpentine leaf miner: Liriomyza trifolii");
                                        pest_desc.setText("Drying and dropping of leaves, Leaves with serpentine mines");
                                        control_measure.setText("Collect and destroy mined leaves and Spray NSKE 5%");
                                        pest_image.setImageResource(R.drawable.banana_pest);

                                    } else if (Webservice.Data_crops.get(searchFor(cropId)).getName().equalsIgnoreCase("coconut")) {
                                        pests.setText("Leaf Eating Caterpillar/ Black Headed Caterpillar: Opisina arenosella");
                                        pest_desc.setText("This causes severe damage to palms in coastal and back water areas and in certain internal packets of peninsular in India. The pest occurs round the year with the spike in population during summer (Mar-May)");
                                        control_measure.setText("As a prophylactic measure, the first affected leaves may be cut and burnt during the beginning of the summer season.");
                                        pest_image.setImageResource(R.drawable.coconut_pest);

                                    } else if (Webservice.Data_crops.get(searchFor(cropId)).getName().equalsIgnoreCase("ginger")) {
                                        pests.setText("Soft rot or rhizome rot : Pythium aphanidermatum/ P. vexans / P. myriotylum");
                                        pest_desc.setText("The infection starts at the collar region of the pseudostems and progresses upwards as well as downwards. The collar region of the affected pseudostem becomes water soaked and the rotting spreads to the rhizome resulting in soft rot.");
                                        control_measure.setText("Treatment of seed rhizomes with mancozeb 0.3% for 30 minutes before storage and once again before planting reduces the incidence of the disease.");
                                        pest_image.setImageResource(R.drawable.ginger_pest);

                                    } else if (Webservice.Data_crops.get(searchFor(cropId)).getName().equalsIgnoreCase("bringal")) {
                                        pests.setText("Stem borer: Euzophera perticella");
                                        pest_desc.setText("Top shoots of young plants droop and wither.");
                                        control_measure.setText("Collect and destroy the damaged and dead plants, Light trap @1/ha to attract and kill adults");
                                        pest_image.setImageResource(R.drawable.brinjal_pest);

                                    } else if (Webservice.Data_crops.get(searchFor(cropId)).getName().equalsIgnoreCase("chilli")) {
                                        pests.setText("Cercospora leaf spot :Cercospora capsici");
                                        pest_desc.setText("Leaf lesions typically are brown and circular with small to large light grey centres and dark brown margins. The lesions may enlarge to 1cm or more in diameter and some times coalesce.");
                                        control_measure.setText("Spray twice at 10-15 days interval with Mancozeb 0.25% or Chlorothalonil (Kavach) 0.1%.");
                                        pest_image.setImageResource(R.drawable.chilli_pest);

                                    } else {
                                        pests.setText("Pest :" + "\n Serpentine leaf miner: Liriomyza trifolii");
                                        pest_desc.setText("Drying and dropping of leaves, Leaves with serpentine mines");
                                        control_measure.setText("Collect and destroy mined leaves and Spray NSKE 5%");
                                        pest_image.setImageResource(R.drawable.banana_pest);


                                    }


                                } else {

                                    if (Webservice.Data_crops.get(searchFor(cropId)).getName().equalsIgnoreCase("banana")) {
                                        pests.setText("Diseases :" + "\n Damping off : Pythium aphanidermatum");
                                        pest_desc.setText("Damping off of tomato occurs in two stages, i.e. the pre-emergence and the post-emergence phase.");
                                        control_measure.setText("Seed treatment with fungal culture Trichoderma viride (4 g/kg of seed) or Thiram (3 g/kg of seed) is the only preventive measure to control the pre-emergence damping off.");
                                        pest_image.setImageResource(R.drawable.banana_pest);
                                    } else if (Webservice.Data_crops.get(searchFor(cropId)).getName().equalsIgnoreCase("coconut")) {
                                        pests.setText("Diseases :" + "\n WILT: Ganoderma lucidem and Ganoderma applanatum");
                                        pest_desc.setText("Initial symptoms of Thanjore wilt (Ganoderma wilt) start with yellowing and drooping of the outer whorl of leaves.");
                                        control_measure.setText("Aureofungin-sol 2 g +1 g Copper Sulphate in 100ml water applied as root feeding. (The active absorbing root of pencil thickness must be selected and a slanting cut is made. Keep the solution in a polythene bag or bottle and the cut end of the root should be dipped in the solution).");
                                        pest_image.setImageResource(R.drawable.coconut_diases);
                                    } else if (Webservice.Data_crops.get(searchFor(cropId)).getName().equalsIgnoreCase("ginger")) {
                                        pests.setText("Diseases :" + "\n Shoot borer: Conogethes punctiferalis ");
                                        pest_desc.setText("The presence of a bore-hole on the pseudostem through which frass is extruded and the withered and yellow central shoot");
                                        control_measure.setText("Spraying malathion 0.1% at 30 day intervals during July to October is effective in controlling the pest infestation.");
                                        pest_image.setImageResource(R.drawable.ginger_diseases);
                                    } else if (Webservice.Data_crops.get(searchFor(cropId)).getName().equalsIgnoreCase("bringal")) {
                                        pests.setText("Diseases :" + "\n Cercospora Leaf Spot :Cercospora solani -melongenae, C. solani");
                                        pest_desc.setText("The leaf spots are characterized by chlorotic lesions, angular to irregular in shape, later turn grayish-brown with profuse sporulation at the centre of the spot.");
                                        control_measure.setText("Spraying 1 per cent Bordeaux mixture or 2 g Copper oxychloride or 2.5 g Zineb per litre of water effectively controls leaf spots.");
                                        pest_image.setImageResource(R.drawable.brinjal_diseases);
                                    } else if (Webservice.Data_crops.get(searchFor(cropId)).getName().equalsIgnoreCase("chilli")) {
                                        pests.setText("Diseases :" + "\n Serpentine leaf miner: Liriomyza trifolii");
                                        pest_desc.setText("Drying and dropping of leaves, Leaves with serpentine mines");
                                        control_measure.setText("Provide poison bait with carbaryl 1.25 kg, rice bran 12.5 kg, jaggery 1.25 kg and water 7.5 lit/ha or spray any one of the following insecticide");
                                        pest_image.setImageResource(R.drawable.chilli_disease);
                                    } else {
                                        pests.setText("Diseases :" + "\n Gram caterpillar: Helicoverpa armigera");
                                        pest_desc.setText("Grown up larvae mainly bore into the fruits.");
                                        control_measure.setText("Provide poison bait with carbaryl 1.25 kg, rice bran 12.5 kg, jaggery 1.25 kg and water 7.5 lit/ha or spray any one of the following insecticide");
                                        pest_image.setImageResource(R.drawable.banana_pest);
                                    }

                                }        //Do something after 100ms
                            }
                        }, 2500);


//
                    }
                }).show(PestsDisease_Activity.this);
            }
        });
    }

    private Integer searchFor(String data) {
        Integer pos = 0;

        //notifiy adapter
        for (int i = 0; i < Webservice.Data_crops.size(); i++) {
            String unitString = Webservice.Data_crops.get(i).getCrop_id();
            if (unitString.equals(data.toLowerCase())) {
                pos = i;
                return pos;
            }
        }
        return pos;
    }

    private void setCropName(){

        boolean isMain = getIntent().getBooleanExtra("type", true);
        String land = getIntent().getStringExtra("land");

        try {
            Maincrops_Model maincrops_model = GsonUtils.getGson().fromJson(land, Maincrops_Model.class);
            landName.setText(maincrops_model.getLand_name() + " - " + maincrops_model.getTaluk());
            if (!isMain){
                JSONArray jsonArray = new JSONArray(maincrops_model.getIntercrop());
                crop.setText(jsonArray.getJSONObject(0).getString("zone"));
            }else {
                JSONArray jsonArray = new JSONArray(maincrops_model.getMaincrop());
                crop.setText(jsonArray.getJSONObject(0).getString("zone"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}