/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl/top/niunaijun/blackbox/core/system/pm/IBXposedManagerService.aidl
 */
package top.niunaijun.blackbox.core.system.pm;
public interface IBXposedManagerService extends android.os.IInterface
{
  /** Default implementation for IBXposedManagerService. */
  public static class Default implements top.niunaijun.blackbox.core.system.pm.IBXposedManagerService
  {
    @Override public boolean isXPEnable() throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setXPEnable(boolean enable) throws android.os.RemoteException
    {
    }
    @Override public boolean isModuleEnable(java.lang.String packageName) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void setModuleEnable(java.lang.String packageName, boolean enable) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<top.niunaijun.blackbox.entity.pm.InstalledModule> getInstalledModules() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void killAllProcesses() throws android.os.RemoteException
    {
    }
    @Override public void killPackageAsUser(java.lang.String packageName, int userId) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements top.niunaijun.blackbox.core.system.pm.IBXposedManagerService
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an top.niunaijun.blackbox.core.system.pm.IBXposedManagerService interface,
     * generating a proxy if needed.
     */
    public static top.niunaijun.blackbox.core.system.pm.IBXposedManagerService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof top.niunaijun.blackbox.core.system.pm.IBXposedManagerService))) {
        return ((top.niunaijun.blackbox.core.system.pm.IBXposedManagerService)iin);
      }
      return new top.niunaijun.blackbox.core.system.pm.IBXposedManagerService.Stub.Proxy(obj);
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
        case TRANSACTION_isXPEnable:
        {
          boolean _result = this.isXPEnable();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setXPEnable:
        {
          boolean _arg0;
          _arg0 = (0!=data.readInt());
          this.setXPEnable(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isModuleEnable:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _result = this.isModuleEnable(_arg0);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setModuleEnable:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.setModuleEnable(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getInstalledModules:
        {
          java.util.List<top.niunaijun.blackbox.entity.pm.InstalledModule> _result = this.getInstalledModules();
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_killAllProcesses:
        {
          this.killAllProcesses();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_killPackageAsUser:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          this.killPackageAsUser(_arg0, _arg1);
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
    private static class Proxy implements top.niunaijun.blackbox.core.system.pm.IBXposedManagerService
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
      @Override public boolean isXPEnable() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isXPEnable, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setXPEnable(boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setXPEnable, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isModuleEnable(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isModuleEnable, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setModuleEnable(java.lang.String packageName, boolean enable) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(((enable)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_setModuleEnable, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<top.niunaijun.blackbox.entity.pm.InstalledModule> getInstalledModules() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<top.niunaijun.blackbox.entity.pm.InstalledModule> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getInstalledModules, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(top.niunaijun.blackbox.entity.pm.InstalledModule.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void killAllProcesses() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_killAllProcesses, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void killPackageAsUser(java.lang.String packageName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_killPackageAsUser, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_isXPEnable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_setXPEnable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_isModuleEnable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_setModuleEnable = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_getInstalledModules = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_killAllProcesses = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_killPackageAsUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "top.niunaijun.blackbox.core.system.pm.IBXposedManagerService";
  public boolean isXPEnable() throws android.os.RemoteException;
  public void setXPEnable(boolean enable) throws android.os.RemoteException;
  public boolean isModuleEnable(java.lang.String packageName) throws android.os.RemoteException;
  public void setModuleEnable(java.lang.String packageName, boolean enable) throws android.os.RemoteException;
  public java.util.List<top.niunaijun.blackbox.entity.pm.InstalledModule> getInstalledModules() throws android.os.RemoteException;
  public void killAllProcesses() throws android.os.RemoteException;
  public void killPackageAsUser(java.lang.String packageName, int userId) throws android.os.RemoteException;
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
