package com.example.michael.cs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michael.cs.Activities.MainActivity;
import com.example.michael.cs.Data.Devices.Circuit;
import com.example.michael.cs.Data.Devices.Device;
import com.example.michael.cs.Data.Devices.Plug;
import com.example.michael.cs.Data.Devices.RGBLamp;
import com.example.michael.cs.Data.Devices.Temp;
import com.example.michael.cs.Data.Devices.WhiteLamp;
import com.example.michael.cs.Data.Rooms.Room;

import java.util.ArrayList;

import static com.example.michael.cs.Constants.LIST_ITEM_CIRCUIT;
import static com.example.michael.cs.Constants.LIST_ITEM_LAMP_WHITE;
import static com.example.michael.cs.Constants.LIST_ITEM_LAMP_RGB;
import static com.example.michael.cs.Constants.LIST_ITEM_PLUG;
import static com.example.michael.cs.Constants.LIST_ITEM_TEMP;
import static com.example.michael.cs.Constants.ROOM_BATH;
import static com.example.michael.cs.Constants.ROOM_BED;
import static com.example.michael.cs.Constants.ROOM_DINING;
import static com.example.michael.cs.Constants.ROOM_GARAGE;
import static com.example.michael.cs.Constants.ROOM_GARDEN;
import static com.example.michael.cs.Constants.ROOM_LIVING;

/**
 * Created by Patrick PC on 09.11.2016.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements View.OnClickListener{
    private static final String TAG = "CustomAdapter";

    Context context;
    ArrayList<Device> deviceList;

    private void listItemHasBeenClicked(int adapterPosition, int list_item_id, View view) {
        ((MainActivity) context).listItemHasBeenClicked(adapterPosition, list_item_id, view);
    }

    private void switchInItemHasBeenClicked(int adapterPosition, boolean b, int list_item_id) {
        ((MainActivity) context).switchInItemHasBeenClicked(adapterPosition, b);
    }

    @Override
    public void onClick(View view) {

    }

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
        ImageView roomImg;
        SwitchCompat switchRGBLamp;

        public RGBLampViewHolder(View v) {
            super(v);

            this.name = (TextView) v.findViewById(R.id.lamp_name);
            this.color = (TextView) v.findViewById(R.id.lamp_color);
            this.dim = (TextView) v.findViewById(R.id.lamp_dim);
            this.room = (TextView) v.findViewById(R.id.room_footer);
            this.group = (TextView) v.findViewById(R.id.group_footer);
            this.roomImg = (ImageView) v.findViewById(R.id.room_footer_img);
            this.switchRGBLamp = (SwitchCompat) v.findViewById(R.id.switch_lamp);

            switchRGBLamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int adapterPosition = getAdapterPosition();
                    switchInItemHasBeenClicked(adapterPosition, b, Constants.LIST_ITEM_LAMP_RGB);
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    listItemHasBeenClicked(adapterPosition, Constants.LIST_ITEM_LAMP_RGB, view);
                }
            });
        }
    }



    public class LampViewHolder extends ViewHolder {
        TextView name;
        TextView dim;
        TextView room;
        TextView group;
        ImageView roomImg;
        SwitchCompat switchLamp;

        public LampViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.lamp_name);
            this.dim = (TextView) v.findViewById(R.id.lamp_dim);
            this.room = (TextView) v.findViewById(R.id.room_footer);
            this.group = (TextView) v.findViewById(R.id.group_footer);
            this.roomImg = (ImageView) v.findViewById(R.id.room_footer_img);
            this.switchLamp = (SwitchCompat) v.findViewById(R.id.switch_lamp);

            switchLamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int adapterPosition = getAdapterPosition();
                    switchInItemHasBeenClicked(adapterPosition, b, Constants.LIST_ITEM_LAMP_WHITE);
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    listItemHasBeenClicked(adapterPosition, Constants.LIST_ITEM_LAMP_WHITE, view);
                }
            });
        }
    }

    public class TempViewHolder extends ViewHolder {
        TextView name;
        TextView temp;
        TextView room;
        TextView group;
        ImageView roomImg;
        SwitchCompat switchTemp;

        public TempViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.temp_name);
            this.temp = (TextView) v.findViewById(R.id.temp_value);
            this.room = (TextView) v.findViewById(R.id.room_footer);
            this.group = (TextView) v.findViewById(R.id.group_footer);
            this.roomImg = (ImageView) v.findViewById(R.id.room_footer_img);
            this.switchTemp = (SwitchCompat) v.findViewById(R.id.switch_temp);

            switchTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int adapterPosition = getAdapterPosition();
                    switchInItemHasBeenClicked(adapterPosition, b, Constants.LIST_ITEM_TEMP);
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    listItemHasBeenClicked(adapterPosition, Constants.LIST_ITEM_TEMP, view);
                }
            });
        }
    }

    public class PlugViewHolder extends ViewHolder {
        TextView name;
        TextView room;
        TextView group;
        ImageView roomImg;
        SwitchCompat switchPlug;

        public PlugViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.plug_name);
            this.room = (TextView) v.findViewById(R.id.room_footer);
            this.group = (TextView) v.findViewById(R.id.group_footer);
            this.roomImg = (ImageView) v.findViewById(R.id.room_footer_img);
            this.switchPlug = (SwitchCompat) v.findViewById(R.id.switch_plug);

            switchPlug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int adapterPosition = getAdapterPosition();
                    switchInItemHasBeenClicked(adapterPosition, b, Constants.LIST_ITEM_PLUG);
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    listItemHasBeenClicked(adapterPosition, Constants.LIST_ITEM_PLUG, view);
                }
            });
        }
    }

    public class CircuitViewHolder extends ViewHolder {
        TextView name;
        TextView room;
        TextView group;
        ImageView roomImg;
        SwitchCompat switchCircuit;

        public CircuitViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.circuit_name);
            this.room = (TextView) v.findViewById(R.id.room_footer);
            this.group = (TextView) v.findViewById(R.id.group_footer);
            this.roomImg = (ImageView) v.findViewById(R.id.room_footer_img);
            this.switchCircuit = (SwitchCompat) v.findViewById(R.id.switch_circuit);

            switchCircuit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int adapterPosition = getAdapterPosition();
                    switchInItemHasBeenClicked(adapterPosition, b, Constants.LIST_ITEM_CIRCUIT);
                }
            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    listItemHasBeenClicked(adapterPosition, Constants.LIST_ITEM_CIRCUIT, view);
                }
            });
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

            case LIST_ITEM_LAMP_WHITE:
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
            holder.room.setText(device.getRoom().getName());
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.switchPlug.setChecked(device.isOn());
        }
        if (itemType == LIST_ITEM_TEMP) {

            Log.i(TAG, "onBindViewHolder: in Temp");
            Temp device = (Temp) deviceList.get(position);

            TempViewHolder holder = (TempViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.group.setText(context.getString(R.string.group) + ": " + device.getGroup().getName());
            holder.room.setText(device.getRoom().getName());
            holder.temp.setText(device.getTemp() + " " + context.getString(R.string.celcius));
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.switchTemp.setChecked(device.isOn());
        }
        if (itemType == LIST_ITEM_CIRCUIT) {

            Log.i(TAG, "onBindViewHolder: in Circuit");
            Circuit device = (Circuit) deviceList.get(position);

            CircuitViewHolder holder = (CircuitViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.group.setText(context.getString(R.string.group) + ": " + device.getGroup().getName());
            holder.room.setText(device.getRoom().getName());
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.switchCircuit.setChecked(device.isOn());
        }
        if (itemType == LIST_ITEM_LAMP_WHITE) {

            Log.i(TAG, "onBindViewHolder: in WhiteLamp");
            WhiteLamp device = (WhiteLamp) deviceList.get(position);

            LampViewHolder holder = (LampViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.group.setText(context.getString(R.string.group) + ": " + device.getGroup().getName());
            holder.room.setText(device.getRoom().getName());
            holder.dim.setText(device.getDim() + " " + context.getString(R.string.percent));
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.switchLamp.setChecked(device.isOn());
        }

        if (itemType == LIST_ITEM_LAMP_RGB) {

            Log.i(TAG, "onBindViewHolder: in RgBLamp");
            RGBLamp device = (RGBLamp) deviceList.get(position);

            RGBLampViewHolder holder = (RGBLampViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.group.setText(context.getString(R.string.group) + ": " + device.getGroup().getName());
            holder.room.setText(device.getRoom().getName());
            holder.dim.setText(device.getDim() + " " + context.getString(R.string.percent));
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.color.setText(device.getColorHex());
            holder.switchRGBLamp.setChecked(device.isOn());
        }
    }

    private Drawable getCorrectRoomImg(Room room) {

        Drawable img;

        switch (room.getName()) {

            case ROOM_BATH:
                img = context.getResources().getDrawable(R.drawable.bath_room);
                break;

            case ROOM_BED:
                img = context.getResources().getDrawable(R.drawable.bed_room);
                break;


            case ROOM_DINING:
                img = context.getResources().getDrawable(R.drawable.dining_room);
                break;


            case ROOM_GARAGE:
                img = context.getResources().getDrawable(R.drawable.garage_room);
                break;


            case ROOM_GARDEN:
                img = context.getResources().getDrawable(R.drawable.garden_room_);
                break;

            case ROOM_LIVING:
                img = context.getResources().getDrawable(R.drawable.living_room);
                break;

            default:
                img = context.getResources().getDrawable(R.drawable.home);
                break;
        }

        return img;
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
            return Constants.LIST_ITEM_LAMP_WHITE;
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


