/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl/top/niunaijun/blackbox/core/system/user/IBUserManagerService.aidl
 */
package top.niunaijun.blackbox.core.system.user;
public interface IBUserManagerService extends android.os.IInterface
{
  /** Default implementation for IBUserManagerService. */
  public static class Default implements top.niunaijun.blackbox.core.system.user.IBUserManagerService
  {
    @Override public top.niunaijun.blackbox.core.system.user.BUserInfo getUserInfo(int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean exists(int userId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public top.niunaijun.blackbox.core.system.user.BUserInfo createUser(int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<top.niunaijun.blackbox.core.system.user.BUserInfo> getUsers() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void deleteUser(int userId) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements top.niunaijun.blackbox.core.system.user.IBUserManagerService
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an top.niunaijun.blackbox.core.system.user.IBUserManagerService interface,
     * generating a proxy if needed.
     */
    public static top.niunaijun.blackbox.core.system.user.IBUserManagerService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof top.niunaijun.blackbox.core.system.user.IBUserManagerService))) {
        return ((top.niunaijun.blackbox.core.system.user.IBUserManagerService)iin);
      }
      return new top.niunaijun.blackbox.core.system.user.IBUserManagerService.Stub.Proxy(obj);
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
        case TRANSACTION_getUserInfo:
        {
          int _arg0;
          _arg0 = data.readInt();
          top.niunaijun.blackbox.core.system.user.BUserInfo _result = this.getUserInfo(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_exists:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _result = this.exists(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_createUser:
        {
          int _arg0;
          _arg0 = data.readInt();
          top.niunaijun.blackbox.core.system.user.BUserInfo _result = this.createUser(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getUsers:
        {
          java.util.List<top.niunaijun.blackbox.core.system.user.BUserInfo> _result = this.getUsers();
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_deleteUser:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.deleteUser(_arg0);
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
    private static class Proxy implements top.niunaijun.blackbox.core.system.user.IBUserManagerService
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
      @Override public top.niunaijun.blackbox.core.system.user.BUserInfo getUserInfo(int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        top.niunaijun.blackbox.core.system.user.BUserInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getUserInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, top.niunaijun.blackbox.core.system.user.BUserInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean exists(int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_exists, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public top.niunaijun.blackbox.core.system.user.BUserInfo createUser(int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        top.niunaijun.blackbox.core.system.user.BUserInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_createUser, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, top.niunaijun.blackbox.core.system.user.BUserInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<top.niunaijun.blackbox.core.system.user.BUserInfo> getUsers() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<top.niunaijun.blackbox.core.system.user.BUserInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getUsers, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(top.niunaijun.blackbox.core.system.user.BUserInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void deleteUser(int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteUser, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_getUserInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_exists = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_createUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_getUsers = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_deleteUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "top.niunaijun.blackbox.core.system.user.IBUserManagerService";
  public top.niunaijun.blackbox.core.system.user.BUserInfo getUserInfo(int userId) throws android.os.RemoteException;
  public boolean exists(int userId) throws android.os.RemoteException;
  public top.niunaijun.blackbox.core.system.user.BUserInfo createUser(int userId) throws android.os.RemoteException;
  public java.util.List<top.niunaijun.blackbox.core.system.user.BUserInfo> getUsers() throws android.os.RemoteException;
  public void deleteUser(int userId) throws android.os.RemoteException;
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
    static private <T extends android.os.Parcelable> void writeTypedList(
        android.os.Parcel parcel, java.util.List<T> value, int parcelableFlags) {
      if (value == null) {
        parcel.writeInt(-1);
      } else {
        int N = value.size();
        int i = 0;
        parcel.writeInt(N);
        while (i < N) {
    writeTypedObject(parcel, value.get(i), parcelableFlags);
          i++;
        }
      }
    }
  }
}
