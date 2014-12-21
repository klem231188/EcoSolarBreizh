package bzh.eco.solar.model.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author : Clément.Tréguer
 */
public class BluetoothDeviceWrapper implements Parcelable {

    // -------------------------------------------------------------------------------------
    // Section : Fields(s)
    // -------------------------------------------------------------------------------------
    private BluetoothDevice bluetoothDevice;

    private State state;

    // -------------------------------------------------------------------------------------
    // Section : Constructor(s)
    // -------------------------------------------------------------------------------------
    public static BluetoothDeviceWrapper newInstance(BluetoothDevice device) {
        BluetoothDeviceWrapper instance = new BluetoothDeviceWrapper();
        instance.bluetoothDevice = device;
        instance.state = State.DISCONNECTED;

        return instance;
    }

    public BluetoothDeviceWrapper() {
    }

    public BluetoothDeviceWrapper(Parcel in) {
        readFromParcel(in);
    }

    // -------------------------------------------------------------------------------------
    // Section : Getter(s)/Setter(s)
    // -------------------------------------------------------------------------------------
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }


    // -------------------------------------------------------------------------------------
    // Section : Method(s) concerning parcelable
    // -------------------------------------------------------------------------------------
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(bluetoothDevice, flags);
        dest.writeParcelable(state, flags);
    }

    private void readFromParcel(Parcel in) {
        bluetoothDevice = in.readParcelable(BluetoothDevice.class.getClassLoader());
        state = in.readParcelable(State.class.getClassLoader());
    }

    public static final Creator<BluetoothDeviceWrapper> CREATOR = new Creator<BluetoothDeviceWrapper>() {
        public BluetoothDeviceWrapper createFromParcel(Parcel in) {
            return new BluetoothDeviceWrapper(in);
        }

        @Override
        public BluetoothDeviceWrapper[] newArray(int size) {
            return new BluetoothDeviceWrapper[size];
        }
    };

    // -------------------------------------------------------------------------------------
    // Section : Inner Class(es), enum(s), ...
    // -------------------------------------------------------------------------------------
    public enum State implements Parcelable {
        CONNECTED,
        CONNECTING,
        DISCONNECTING,
        DISCONNECTED;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeInt(ordinal());
        }

        public static final Creator<State> CREATOR = new Creator<State>() {
            @Override
            public State createFromParcel(final Parcel source) {
                return State.values()[source.readInt()];
            }

            @Override
            public State[] newArray(final int size) {
                return new State[size];
            }
        };
    }
}
