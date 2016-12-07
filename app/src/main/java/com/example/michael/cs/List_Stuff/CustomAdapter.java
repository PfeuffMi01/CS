package com.example.michael.cs.List_Stuff;

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
import com.example.michael.cs.Constants;
import com.example.michael.cs.Data.Devices.Device;
import com.example.michael.cs.Data.Devices.DoorSensor;
import com.example.michael.cs.Data.Devices.HumiditySensor;
import com.example.michael.cs.Data.Devices.MovementSensor;
import com.example.michael.cs.Data.Devices.Plug;
import com.example.michael.cs.Data.Devices.PlugWithConsumption;
import com.example.michael.cs.Data.Devices.RGBLamp;
import com.example.michael.cs.Data.Devices.Temp;
import com.example.michael.cs.Data.Devices.WhiteLamp;
import com.example.michael.cs.Data.Devices.WindowSensor;
import com.example.michael.cs.Data.Room;
import com.example.michael.cs.R;

import java.util.ArrayList;

import static android.view.View.GONE;
import static com.example.michael.cs.Constants.LIST_ITEM_DOOR_SENSOR;
import static com.example.michael.cs.Constants.LIST_ITEM_HUMIDITY;
import static com.example.michael.cs.Constants.LIST_ITEM_LAMP_RGB;
import static com.example.michael.cs.Constants.LIST_ITEM_LAMP_WHITE;
import static com.example.michael.cs.Constants.LIST_ITEM_MOVEMENT_SENSOR;
import static com.example.michael.cs.Constants.LIST_ITEM_PLUG;
import static com.example.michael.cs.Constants.LIST_ITEM_PLUG_CONSUMPTION;
import static com.example.michael.cs.Constants.LIST_ITEM_TEMP;
import static com.example.michael.cs.Constants.LIST_ITEM_WINDOW_SENSOR;
import static com.example.michael.cs.Constants.ROOM_BED;
import static com.example.michael.cs.Constants.ROOM_GARAGE;
import static com.example.michael.cs.Constants.ROOM_GARDEN;
import static com.example.michael.cs.Constants.ROOM_HALLWAY;
import static com.example.michael.cs.Constants.ROOM_KITCHEN;
import static com.example.michael.cs.Constants.ROOM_LIVING;

/**
 * Erzeugt die ViewHolder für die Geräte
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "CustomAdapter";

    Context context;
    ArrayList<Device> deviceList;
    private boolean onBind;

    private void listItemHasBeenClicked(int adapterPosition, int list_item_id, View view) {
        ((MainActivity) context).listItemHasBeenClicked(adapterPosition, list_item_id, view);
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        TextView name;
        TextView room;
        ImageView icon;
        ImageView roomImg;
        SwitchCompat switchOnOff;

        public ViewHolder(View v) {
            super(v);

            this.icon = (ImageView) v.findViewById(R.id.image);
            this.name = (TextView) v.findViewById(R.id.device_name);
            this.room = (TextView) v.findViewById(R.id.room_footer);
            this.roomImg = (ImageView) v.findViewById(R.id.room_footer_img);
            this.switchOnOff = (SwitchCompat) v.findViewById(R.id.switch_on_off);
            this.switchOnOff.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if (!onBind) {
                int adapterPosition = getAdapterPosition();
                ((MainActivity) context).switchInItemHasBeenClicked(adapterPosition, b);
            }
        }
    }


    public class HumidityViewHolder extends ViewHolder {

        TextView humidity;

        public HumidityViewHolder(View v) {
            super(v);

            this.humidity = (TextView) v.findViewById(R.id.humidity);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    listItemHasBeenClicked(adapterPosition, LIST_ITEM_HUMIDITY, view);
                }
            });
        }
    }

    public class MovementSensorViewHolder extends ViewHolder {

        TextView lastMovement;

        public MovementSensorViewHolder(View v) {
            super(v);

            this.lastMovement = (TextView) v.findViewById(R.id.last_movement);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    listItemHasBeenClicked(adapterPosition, Constants.LIST_ITEM_MOVEMENT_SENSOR, view);
                }
            });
        }
    }

    public class DoorSensorViewHolder extends ViewHolder {

        TextView status;

        public DoorSensorViewHolder(View v) {
            super(v);

            this.status = (TextView) v.findViewById(R.id.status);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    listItemHasBeenClicked(adapterPosition, Constants.LIST_ITEM_DOOR_SENSOR, view);
                }
            });
        }
    }

    public class WindowSensorViewHolder extends ViewHolder {

        TextView status;

        public WindowSensorViewHolder(View v) {
            super(v);

            this.status = (TextView) v.findViewById(R.id.status);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    listItemHasBeenClicked(adapterPosition, Constants.LIST_ITEM_WINDOW_SENSOR, view);
                }
            });
        }
    }


    public class RGBLampViewHolder extends ViewHolder {

        TextView color;
        TextView dim;
        TextView status;


        public RGBLampViewHolder(View v) {
            super(v);

            this.color = (TextView) v.findViewById(R.id.color);
            this.dim = (TextView) v.findViewById(R.id.dim);
            this.status = (TextView) v.findViewById(R.id.status);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    listItemHasBeenClicked(adapterPosition, Constants.LIST_ITEM_LAMP_RGB, view);
                }
            });
        }
    }


    public class WhiteLampViewHolder extends ViewHolder {
        TextView dim;
        TextView status;

        public WhiteLampViewHolder(View v) {
            super(v);

            this.dim = (TextView) v.findViewById(R.id.dim);
            this.status = (TextView) v.findViewById(R.id.status);

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

        TextView temp;


        public TempViewHolder(View v) {
            super(v);

            this.temp = (TextView) v.findViewById(R.id.temperature);

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

        TextView status;

        public PlugViewHolder(View v) {
            super(v);

            this.status = (TextView) v.findViewById(R.id.status);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    listItemHasBeenClicked(adapterPosition, Constants.LIST_ITEM_PLUG, view);
                }
            });
        }
    }

    public class PlugWithConsumptionViewHolder extends ViewHolder {

        TextView status;
        TextView consumption;

        public PlugWithConsumptionViewHolder(View v) {
            super(v);

            this.status = (TextView) v.findViewById(R.id.status);
            this.consumption = (TextView) v.findViewById(R.id.consumption);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int adapterPosition = getAdapterPosition();
                    listItemHasBeenClicked(adapterPosition, Constants.LIST_ITEM_PLUG, view);
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

            case LIST_ITEM_HUMIDITY:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_humidity, viewGroup, false);
                returnViewHolder = new HumidityViewHolder(v);
                break;

            case LIST_ITEM_PLUG:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_plug, viewGroup, false);
                returnViewHolder = new PlugViewHolder(v);
                break;

            case LIST_ITEM_PLUG_CONSUMPTION:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_plug_consumption, viewGroup, false);
                returnViewHolder = new PlugWithConsumptionViewHolder(v);
                break;


            case LIST_ITEM_LAMP_WHITE:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_lamp_white, viewGroup, false);
                returnViewHolder = new WhiteLampViewHolder(v);
                break;


            case LIST_ITEM_LAMP_RGB:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_lamp_rgb, viewGroup, false);
                returnViewHolder = new RGBLampViewHolder(v);
                break;

            case LIST_ITEM_MOVEMENT_SENSOR:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_movement_sensor, viewGroup, false);
                returnViewHolder = new MovementSensorViewHolder(v);
                break;

            case LIST_ITEM_DOOR_SENSOR:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_door_sensor, viewGroup, false);
                returnViewHolder = new DoorSensorViewHolder(v);
                break;

            case LIST_ITEM_WINDOW_SENSOR:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.recycler_list_item_window_sensor, viewGroup, false);
                returnViewHolder = new WindowSensorViewHolder(v);
                break;

            default:
                break;
        }

        return returnViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        onBind = true;

        int itemType = viewHolder.getItemViewType();
        Log.i(TAG, "onBindViewHolder: received " + itemType);

        if (itemType == LIST_ITEM_PLUG) {

            Log.i(TAG, "onBindViewHolder: in Plug");
            Plug device = (Plug) deviceList.get(position);
            PlugViewHolder holder = (PlugViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.plug_group));
            holder.room.setText(device.getRoom().getName());
            holder.status.setText("Status: " + device.getStatus());
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.switchOnOff.setChecked(device.isOn());
        }

        if (itemType == LIST_ITEM_MOVEMENT_SENSOR) {

            Log.i(TAG, "onBindViewHolder: in Movement Sensor");
            MovementSensor device = (MovementSensor) deviceList.get(position);
            MovementSensorViewHolder holder = (MovementSensorViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.movement_sens));
            holder.room.setText(device.getRoom().getName());
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.lastMovement.setText("Letzte Bewegung: " + device.getLastMovement());
            holder.switchOnOff.setVisibility(GONE);
        }

        if (itemType == LIST_ITEM_PLUG_CONSUMPTION) {

            Log.i(TAG, "onBindViewHolder: in Plug Consumption");
            PlugWithConsumption device = (PlugWithConsumption) deviceList.get(position);
            PlugWithConsumptionViewHolder holder = (PlugWithConsumptionViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.plug_consumption));
            holder.room.setText(device.getRoom().getName());
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.consumption.setText("Σ-Verbrauch: " + device.getConsumption() + " Watt");
            holder.switchOnOff.setChecked(device.isOn());
            holder.status.setVisibility(GONE);

        }

        if (itemType == LIST_ITEM_TEMP) {

            Log.i(TAG, "onBindViewHolder: in Temp");
            Temp device = (Temp) deviceList.get(position);

            TempViewHolder holder = (TempViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.temp));
            holder.room.setText(device.getRoom().getName());
            holder.temp.setText("Temp.: " + device.getTemp() + " " + context.getString(R.string.celcius));
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.switchOnOff.setVisibility(GONE);
        }

        if (itemType == LIST_ITEM_LAMP_WHITE) {

            Log.i(TAG, "onBindViewHolder: in WhiteLamp");
            WhiteLamp device = (WhiteLamp) deviceList.get(position);

            WhiteLampViewHolder holder = (WhiteLampViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.lamp_white));
            holder.room.setText(device.getRoom().getName());
            holder.dim.setText("Dim: " + device.getDim() + context.getString(R.string.percent));
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.switchOnOff.setChecked(device.isOn());
            holder.status.setText("Status: " + device.getStatus());
        }

        if (itemType == LIST_ITEM_LAMP_RGB) {

            Log.i(TAG, "onBindViewHolder: in RgBLamp");
            RGBLamp device = (RGBLamp) deviceList.get(position);

            RGBLampViewHolder holder = (RGBLampViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.lamp_rgb));
            holder.room.setText(device.getRoom().getName());
            holder.dim.setText("Dim: " + device.getDim() + context.getString(R.string.percent));
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));

            // Den deutschen Namen der Farbe herausfinden
            String colorName = device.getSelectedColorName();
            String colorNameGerman = "";
            String[] colorNames = context.getResources().getStringArray(R.array.colorIntNames);
            String[] colorNamesGerman = context.getResources().getStringArray(R.array.colorIntNamesGerman);

            for (int i = 0; i < colorNames.length; i++) {
                if (colorName.equals(colorNames[i])) {
                    colorNameGerman = colorNamesGerman[i];
                }
            }

            holder.color.setText("Farbe: " + colorNameGerman);
            holder.switchOnOff.setChecked(device.isOn());
            holder.status.setText("Status: " + device.getStatus());
        }


        if (itemType == LIST_ITEM_DOOR_SENSOR) {

            Log.i(TAG, "onBindViewHolder: in Door Sens");
            DoorSensor device = (DoorSensor) deviceList.get(position);
            DoorSensorViewHolder holder = (DoorSensorViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.door_sens));
            holder.room.setText(device.getRoom().getName());
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.status.setText("Status: " + device.getStatus());
            holder.switchOnOff.setVisibility(GONE);
        }

        if (itemType == LIST_ITEM_WINDOW_SENSOR) {

            Log.i(TAG, "onBindViewHolder: in Window Sens");
            WindowSensor device = (WindowSensor) deviceList.get(position);
            WindowSensorViewHolder holder = (WindowSensorViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.window_sens));
            holder.room.setText(device.getRoom().getName());
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.status.setText("Status: " + device.getStatus());
            holder.switchOnOff.setVisibility(GONE);
        }

        if (itemType == LIST_ITEM_HUMIDITY) {

            Log.i(TAG, "onBindViewHolder: in Weather Stat");
            HumiditySensor device = (HumiditySensor) deviceList.get(position);
            HumidityViewHolder holder = (HumidityViewHolder) viewHolder;
            holder.name.setText(device.getName());
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.humidity));
            holder.room.setText(device.getRoom().getName());
            holder.roomImg.setImageDrawable(getCorrectRoomImg(device.getRoom()));
            holder.humidity.setText("Feucht.: " + device.getHumidity() + context.getString(R.string.percent));
            holder.switchOnOff.setVisibility(GONE);
        }

        onBind = false;
    }

    private Drawable getCorrectRoomImg(Room room) {

        Drawable img;

        switch (room.getName()) {

            case ROOM_BED:
                img = context.getResources().getDrawable(R.drawable.bed_room);
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

            case ROOM_HALLWAY:
                img = context.getResources().getDrawable(R.drawable.hallway);
                break;

            case ROOM_KITCHEN:
                img = context.getResources().getDrawable(R.drawable.kitchen_room);
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

        if (deviceList.get(position) instanceof PlugWithConsumption) {
            return Constants.LIST_ITEM_PLUG_CONSUMPTION;
        } else if (deviceList.get(position) instanceof Plug) {
            return Constants.LIST_ITEM_PLUG;
        } else if (deviceList.get(position) instanceof WhiteLamp) {
            return Constants.LIST_ITEM_LAMP_WHITE;
        } else if (deviceList.get(position) instanceof RGBLamp) {
            return Constants.LIST_ITEM_LAMP_RGB;
        } else if (deviceList.get(position) instanceof Temp) {
            return LIST_ITEM_TEMP;
        } else if (deviceList.get(position) instanceof WindowSensor) {
            return LIST_ITEM_WINDOW_SENSOR;
        } else if (deviceList.get(position) instanceof DoorSensor) {
            return LIST_ITEM_DOOR_SENSOR;
        } else if (deviceList.get(position) instanceof MovementSensor) {
            return LIST_ITEM_MOVEMENT_SENSOR;
        } else if (deviceList.get(position) instanceof HumiditySensor) {
            return LIST_ITEM_HUMIDITY;
        } else

        {
            return -1;
        }
    }
}


