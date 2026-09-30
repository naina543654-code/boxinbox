/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl/android/location/ILocationListener.aidl
 */
package android.location;
public interface ILocationListener extends android.os.IInterface
{
  /** Default implementation for ILocationListener. */
  public static class Default implements android.location.ILocationListener
  {
    @Override public void onLocationChanged(android.location.Location location) throws android.os.RemoteException
    {
    }
    @Override public void onStatusChanged(java.lang.String provider, int status, android.os.Bundle extras) throws android.os.RemoteException
    {
    }
    @Override public void onProviderEnabled(java.lang.String provider) throws android.os.RemoteException
    {
    }
    @Override public void onProviderDisabled(java.lang.String provider) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.location.ILocationListener
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.location.ILocationListener interface,
     * generating a proxy if needed.
     */
    public static android.location.ILocationListener asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.location.ILocationListener))) {
        return ((android.location.ILocationListener)iin);
      }
      return new android.location.ILocationListener.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
        data.enforceInterface(descriptor);
      }
      if (code == INTERFACE_TRANSACTION) {
        reply.writeString(descriptor);
        return true;
      }
      switch (code)
      {
        case TRANSACTION_onLocationChanged:
        {
          android.location.Location _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.location.Location.CREATOR);
          this.onLocationChanged(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_onStatusChanged:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          android.os.Bundle _arg2;
          _arg2 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          this.onStatusChanged(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_onProviderEnabled:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.onProviderEnabled(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_onProviderDisabled:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.onProviderDisabled(_arg0);
          reply.writeNoException();
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements android.location.ILocationListener
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public void onLocationChanged(android.location.Location location) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, location, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onLocationChanged, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onStatusChanged(java.lang.String provider, int status, android.os.Bundle extras) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(provider);
          _data.writeInt(status);
          _Parcel.writeTypedObject(_data, extras, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onStatusChanged, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onProviderEnabled(java.lang.String provider) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(provider);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onProviderEnabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onProviderDisabled(java.lang.String provider) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(provider);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onProviderDisabled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "android.location.ILocationListener";
    static final int TRANSACTION_onLocationChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onStatusChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_onProviderEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_onProviderDisabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
  }
  public void onLocationChanged(android.location.Location location) throws android.os.RemoteException;
  public void onStatusChanged(java.lang.String provider, int status, android.os.Bundle extras) throws android.os.RemoteException;
  public void onProviderEnabled(java.lang.String provider) throws android.os.RemoteException;
  public void onProviderDisabled(java.lang.String provider) throws android.os.RemoteException;
  /** @hide */
  static class _Parcel {
    static private <T> T readTypedObject(
        android.os.Parcel parcel,
        android.os.Parcelable.Creator<T> c) {
      if (parcel.readInt() != 0) {
          return c.createFromParcel(parcel);
      } else {
          return null;
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedObject(
        android.os.Parcel parcel, T value, int parcelableFlags) {
      if (value != null) {
        parcel.writeInt(1);
        value.writeToParcel(parcel, parcelableFlags);
      } else {
        parcel.writeInt(0);
      }
    }
  }
}
