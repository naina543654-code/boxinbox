/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl/android/net/IConnectivityManager.aidl
 */
package android.net;
public interface IConnectivityManager extends android.os.IInterface
{
  /** Default implementation for IConnectivityManager. */
  public static class Default implements android.net.IConnectivityManager
  {
    @Override public android.net.NetworkInfo getActiveNetworkInfo() throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.net.NetworkInfo getActiveNetworkInfoForUid(int uid, boolean ignoreBlocked) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.net.NetworkInfo getNetworkInfo(int networkType) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.net.NetworkInfo[] getAllNetworkInfo() throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isActiveNetworkMetered() throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean requestRouteToHostAddress(int networkType, int address) throws android.os.RemoteException
    {
      return false;
    }
    @Override public android.net.LinkProperties getActiveLinkProperties() throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.net.LinkProperties getLinkProperties(int networkType) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.net.IConnectivityManager
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.net.IConnectivityManager interface,
     * generating a proxy if needed.
     */
    public static android.net.IConnectivityManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.net.IConnectivityManager))) {
        return ((android.net.IConnectivityManager)iin);
      }
      return new android.net.IConnectivityManager.Stub.Proxy(obj);
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
        case TRANSACTION_getActiveNetworkInfo:
        {
          android.net.NetworkInfo _result = this.getActiveNetworkInfo();
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getActiveNetworkInfoForUid:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          android.net.NetworkInfo _result = this.getActiveNetworkInfoForUid(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getNetworkInfo:
        {
          int _arg0;
          _arg0 = data.readInt();
          android.net.NetworkInfo _result = this.getNetworkInfo(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getAllNetworkInfo:
        {
          android.net.NetworkInfo[] _result = this.getAllNetworkInfo();
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_isActiveNetworkMetered:
        {
          boolean _result = this.isActiveNetworkMetered();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_requestRouteToHostAddress:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          boolean _result = this.requestRouteToHostAddress(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getActiveLinkProperties:
        {
          android.net.LinkProperties _result = this.getActiveLinkProperties();
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getLinkProperties:
        {
          int _arg0;
          _arg0 = data.readInt();
          android.net.LinkProperties _result = this.getLinkProperties(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements android.net.IConnectivityManager
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
      @Override public android.net.NetworkInfo getActiveNetworkInfo() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.net.NetworkInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getActiveNetworkInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.net.NetworkInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.net.NetworkInfo getActiveNetworkInfoForUid(int uid, boolean ignoreBlocked) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.net.NetworkInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(uid);
          _data.writeInt(((ignoreBlocked)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_getActiveNetworkInfoForUid, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.net.NetworkInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.net.NetworkInfo getNetworkInfo(int networkType) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.net.NetworkInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(networkType);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getNetworkInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.net.NetworkInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.net.NetworkInfo[] getAllNetworkInfo() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.net.NetworkInfo[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllNetworkInfo, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArray(android.net.NetworkInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isActiveNetworkMetered() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isActiveNetworkMetered, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean requestRouteToHostAddress(int networkType, int address) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(networkType);
          _data.writeInt(address);
          boolean _status = mRemote.transact(Stub.TRANSACTION_requestRouteToHostAddress, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.net.LinkProperties getActiveLinkProperties() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.net.LinkProperties _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getActiveLinkProperties, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.net.LinkProperties.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.net.LinkProperties getLinkProperties(int networkType) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.net.LinkProperties _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(networkType);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLinkProperties, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.net.LinkProperties.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "android.net.IConnectivityManager";
    static final int TRANSACTION_getActiveNetworkInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getActiveNetworkInfoForUid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_getNetworkInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_getAllNetworkInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_isActiveNetworkMetered = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_requestRouteToHostAddress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_getActiveLinkProperties = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_getLinkProperties = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
  }
  public android.net.NetworkInfo getActiveNetworkInfo() throws android.os.RemoteException;
  public android.net.NetworkInfo getActiveNetworkInfoForUid(int uid, boolean ignoreBlocked) throws android.os.RemoteException;
  public android.net.NetworkInfo getNetworkInfo(int networkType) throws android.os.RemoteException;
  public android.net.NetworkInfo[] getAllNetworkInfo() throws android.os.RemoteException;
  public boolean isActiveNetworkMetered() throws android.os.RemoteException;
  public boolean requestRouteToHostAddress(int networkType, int address) throws android.os.RemoteException;
  public android.net.LinkProperties getActiveLinkProperties() throws android.os.RemoteException;
  public android.net.LinkProperties getLinkProperties(int networkType) throws android.os.RemoteException;
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
