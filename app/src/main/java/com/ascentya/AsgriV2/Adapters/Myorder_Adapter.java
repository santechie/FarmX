package com.ascentya.AsgriV2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ascentya.AsgriV2.R;
import com.ascentya.AsgriV2.buysell.Track_Order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import androidx.recyclerview.widget.RecyclerView;

public class Myorder_Adapter extends RecyclerView.Adapter<Myorder_Adapter.ViewHolder> {

    private List<Myoders_Model> items = new ArrayList<>();
    private Context ctx;


    public Myorder_Adapter(Context context, List<Myoders_Model> items) {
        this.items = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView order_id;
        LinearLayout cancel;
        LinearLayout aftercanel;
        TextView cancelorder;
        TextView price;
        TextView order_status;
        TextView order_time;
        TextView order_date;
        TextView order;
        TextView vieworder;
        TextView orderp, paymentmode;
        TextView orderst, pmode;
        TextView trackorder, pstatus, paymentstatus;

        public ViewHolder(View view) {
            super(view);
            order_id = (TextView) view.findViewById(R.id.order_id);
            cancel = (LinearLayout) view.findViewById(R.id.cancel);
            aftercanel = (LinearLayout) view.findViewById(R.id.aftercancel);
            cancelorder = (TextView) view.findViewById(R.id.cancelorder);
            paymentmode = (TextView) view.findViewById(R.id.paymentmode);
            pmode = (TextView) view.findViewById(R.id.pmode);
            price = (TextView) view.findViewById(R.id.price);
            order_status = (TextView) view.findViewById(R.id.order_status);
            paymentstatus = (TextView) view.findViewById(R.id.paymentstatus);
            order_time = (TextView) view.findViewById(R.id.order_time);
            order_date = (TextView) view.findViewById(R.id.order_date);
            pstatus = (TextView) view.findViewById(R.id.pstatus);
            order = (TextView) view.findViewById(R.id.orderid);
            vieworder = (TextView) view.findViewById(R.id.vieworder);
            orderp = (TextView) view.findViewById(R.id.pricet);
            orderst = (TextView) view.findViewById(R.id.orderstatus);
            trackorder = (TextView) view.findViewById(R.id.trackorder);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorders_row, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            final Myoders_Model history = items.get(position);
            holder.order_id.setText(history.getOrder_id());
            holder.price.setText("\u20b9" + history.getOrder_price());
            holder.pstatus.setText(history.getPayment_status());
            holder.paymentmode.setText(history.getPayment_mode());
//            holder.pmode.setText(history.getp().replaceAll("_"," "));

            StringTokenizer tk = new StringTokenizer(history.getOrdered_date());
            String date = tk.nextToken();
            String time = tk.nextToken();

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");

            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("MMM dd,yyyy");

            Date dt, date1;
            try {
                dt = sdf.parse(time);
                date1 = inputFormat.parse(date);

                holder.order_time.setText(sdfs.format(dt));
                holder.order_date.setText(outputFormat.format(date1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
//
            if (history.getOrder_status().equalsIgnoreCase("ORDER CONFIRMED")) {
                holder.order_status.setText("ORDER CONFIRMED");
                holder.order_status.setTextColor(ctx.getResources().getColor(R.color.red));
            } else if (history.getOrder_status().equalsIgnoreCase("PROCESSING")) {
                holder.order_status.setText("PROCESSING");
                holder.order_status.setTextColor(ctx.getResources().getColor(R.color.red));
            } else if (history.getOrder_status().equalsIgnoreCase("DELIVERED")) {
                holder.order_status.setText("DELIVERED");
                holder.cancel.setVisibility(View.GONE);
                holder.aftercanel.setVisibility(View.VISIBLE);
                holder.order_status.setTextColor(ctx.getResources().getColor(R.color.green_farmx));
            } else if (history.getOrder_status().equalsIgnoreCase("DISPATCHED")) {
                holder.cancel.setVisibility(View.GONE);
                holder.aftercanel.setVisibility(View.VISIBLE);
                holder.order_status.setText("DISPATCHED");
                holder.order_status.setTextColor(ctx.getResources().getColor(R.color.red));
            } else if (history.getOrder_status().equalsIgnoreCase("REFUNDED")) {
                holder.order_status.setText("REFUNDED");
                holder.cancel.setVisibility(View.GONE);
                holder.aftercanel.setVisibility(View.VISIBLE);
                holder.order_status.setTextColor(ctx.getResources().getColor(R.color.red));
            } else if (history.getOrder_status().equalsIgnoreCase("CANCELLED")) {
                holder.order_status.setText("CANCELLED");
                holder.cancel.setVisibility(View.GONE);
                holder.aftercanel.setVisibility(View.VISIBLE);
                holder.order_status.setTextColor(ctx.getResources().getColor(R.color.red));
            }


            holder.trackorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(ctx, Track_Order.class);
                    i.putExtra("status", history.getOrder_status());
                    ctx.startActivity(i);
                }
            });


//            if(position %2 == 1)
//            {
//                holder.c_name.setTextColor(ctx.getResources().getColor(R.color.subheadlines_colour));
//                //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            }
//            else
//            {
//                holder.c_name.setTextColor(ctx.getResources().getColor(R.color.orange_delete));
//                //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
//            }


//            Glide.with(ctx).load(Webservice.Searchicon).into(view.icon);


        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
