package com.example.michael.cs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.michael.cs.Data.Devices.Circuit;
import com.example.michael.cs.Data.Devices.Device;
import com.example.michael.cs.Data.Devices.Plug;
import com.example.michael.cs.Data.Devices.RGBLamp;
import com.example.michael.cs.Data.Devices.Temp;
import com.example.michael.cs.Data.Devices.WhiteLamp;

import java.util.ArrayList;

import static com.example.michael.cs.Constants.LIST_ITEM_CIRCUIT;
import static com.example.michael.cs.Constants.LIST_ITEM_LAMP;
import static com.example.michael.cs.Constants.LIST_ITEM_LAMP_RGB;
import static com.example.michael.cs.Constants.LIST_ITEM_PLUG;
import static com.example.michael.cs.Constants.LIST_ITEM_TEMP;

/**
 * Created by Patrick PC on 09.11.2016.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    Context context;
    ArrayList<Device> deviceList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View v) {
            super(v);

        }
    }

    public class RGBLampViewHolder extends ViewHolder {

        TextView name;
        TextView color;
        TextView dim;
        TextView room;
        TextView group;
        SwitchCompat switchRGBLamp;

        public RGBLampViewHolder(View v) {
            super(v);

            this.name = (TextView) v.findViewById(R.id.lamp_name);
            this.color = (TextView) v.findViewById(R.id.lamp_color);
            this.dim = (TextView) v.findViewById(R.id.lamp_dim);
            this.room = (TextView) v.findViewById(R.id.room_footer);
            this.group = (TextView) v.findViewById(R.id.group_footer);
            this.switchRGBLamp = (SwitchCompat) v.findViewById(R.id.switch_lamp);
        }
    }

    public class LampViewHolder extends ViewHolder {
        TextView name;
        TextView dim;
        TextView room;
        TextView group;
        SwitchCompat switchLamp;

        public LampViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.lamp_name);
            this.dim = (TextView) v.findViewById(R.id.lamp_dim);
            this.room = (TextView) v.findViewById(R.id.room_footer);
            this.group = (TextView) v.findViewById(R.id.group_footer);
            this.switchLamp = (SwitchCompat) v.findViewById(R.id.switch_lamp);
        }
    }

    public class TempViewHolder extends ViewHolder {
        TextView name;
        TextView temp;
        TextView room;
        TextView group;
        SwitchCompat switchTemp;

        public TempViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.temp_name);
            this.temp = (TextView) v.findViewById(R.id.temp_value);
            this.room = (TextView) v.findViewById(R.id.room_footer);
            this.group = (TextView) v.findViewById(R.id.group_footer);
            this.switchTemp = (SwitchCompat) v.findViewById(R.id.switch_temp);
        }
    }

    public class PlugViewHolder extends ViewHolder {
        TextView name;
        TextView room;
        TextView group;
        SwitchCompat switchPlug;

        public PlugViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.plug_name);
            this.room = (TextView) v.findViewById(R.id.room_footer);
            this.group = (TextView) v.findViewById(R.id.group_footer);
            this.switchPlug = (SwitchCompat) v.findViewById(R.id.switch_plug);
        }
    }

    public class CircuitViewHolder extends ViewHolder {
        TextView name;
        TextView room;
        TextView group;
        SwitchCompat switchCircuit;

        public CircuitViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.circuit_name);
            this.room = (TextView) v.findViewById(R.id.room_footer);
            this.group = (TextView) v.findViewById(R.id.group_footer);
            this.switchCircuit = (SwitchCompat) v.findViewById(R.id.switch_circuit);
        }
    }

    public CustomAdapter(Context contexts, ArrayList<Device> deviceList) {

        this.context = contexts;
        this.deviceList = deviceList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

        Log.i(TAG, "onCreateViewHolder: " + viewType);

        ViewHolder returnViewHolder = null;

        switch (viewType) {

            case -1:
                returnViewHolder = null;
                break;

            case LIST_ITEM_TEMP:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_temp, viewGroup, false);
                returnViewHolder = new TempViewHolder(v);
                break;

            case LIST_ITEM_PLUG:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_plug, viewGroup, false);
                returnViewHolder = new PlugViewHolder(v);
                break;

            case LIST_ITEM_CIRCUIT:

                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_circuit, viewGroup, false);
                returnViewHolder = new CircuitViewHolder(v);
                break;

            case LIST_ITEM_LAMP:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_lamp_white, viewGroup, false);
                returnViewHolder = new LampViewHolder(v);
                break;


            case LIST_ITEM_LAMP_RGB:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_lamp_rgb, viewGroup, false);
                returnViewHolder = new RGBLampViewHolder(v);
                break;

            default:
                break;
        }

        return returnViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        int itemType = viewHolder.getItemViewType();
        Log.i(TAG, "onBindViewHolder: received " + itemType);

        if (itemType == LIST_ITEM_PLUG) {

            Log.i(TAG, "onBindViewHolder: in Plug");
            Plug device = (Plug) deviceList.get(position);

            PlugViewHolder holder = (PlugViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.group.setText(context.getString(R.string.group) + ": " + device.getGroup().getName());
            holder.room.setText(context.getString(R.string.room) + ": " + device.getRoom().getName());
            holder.switchPlug.setChecked(device.isOn());
        }
        if (itemType == LIST_ITEM_TEMP) {

            Log.i(TAG, "onBindViewHolder: in Temp");
            Temp device = (Temp) deviceList.get(position);

            TempViewHolder holder = (TempViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.group.setText(context.getString(R.string.group) + ": " + device.getGroup().getName());
            holder.room.setText(context.getString(R.string.room) + ": " + device.getRoom().getName());
            holder.temp.setText(device.getTemp() + " " + context.getString(R.string.celcius));
            holder.switchTemp.setChecked(device.isOn());
        }
        if (itemType == LIST_ITEM_CIRCUIT) {

            Log.i(TAG, "onBindViewHolder: in Circuit");
            Circuit device = (Circuit) deviceList.get(position);

            CircuitViewHolder holder = (CircuitViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.group.setText(context.getString(R.string.group) + ": " + device.getGroup().getName());
            holder.room.setText(context.getString(R.string.room) + ": " + device.getRoom().getName());
            holder.switchCircuit.setChecked(device.isOn());
        }
        if (itemType == LIST_ITEM_LAMP) {

            Log.i(TAG, "onBindViewHolder: in WhiteLamp");
            WhiteLamp device = (WhiteLamp) deviceList.get(position);

            LampViewHolder holder = (LampViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.group.setText(context.getString(R.string.group) + ": " + device.getGroup().getName());
            holder.room.setText(context.getString(R.string.room) + ": " + device.getRoom().getName());
            holder.dim.setText(device.getDim() + " " + context.getString(R.string.percent));
            holder.switchLamp.setChecked(device.isOn());
        }

        if (itemType == LIST_ITEM_LAMP_RGB) {

            Log.i(TAG, "onBindViewHolder: in RgBLamp");
            RGBLamp device = (RGBLamp) deviceList.get(position);

            RGBLampViewHolder holder = (RGBLampViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.group.setText(context.getString(R.string.group) + ": " + device.getGroup().getName());
            holder.room.setText(context.getString(R.string.room) + ": " + device.getRoom().getName());
            holder.dim.setText(device.getDim() + " " + context.getString(R.string.percent));
            holder.color.setText(device.getColorHex());
            holder.switchRGBLamp.setChecked(device.isOn());
        }
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (deviceList.get(position) instanceof Plug) {
            return Constants.LIST_ITEM_PLUG;
        }

        if (deviceList.get(position) instanceof WhiteLamp) {
            return Constants.LIST_ITEM_LAMP;
        }

        if (deviceList.get(position) instanceof RGBLamp) {
            return Constants.LIST_ITEM_LAMP_RGB;
        }

        if (deviceList.get(position) instanceof Temp) {
            return LIST_ITEM_TEMP;
        }

        if (deviceList.get(position) instanceof Circuit) {
            return Constants.LIST_ITEM_CIRCUIT;
        } else {
            return -1;
        }
    }
}


