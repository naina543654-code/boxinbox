/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl/top/niunaijun/blackbox/core/system/pm/IBPackageInstallerService.aidl
 */
package top.niunaijun.blackbox.core.system.pm;
// Declare any non-default types here with import statements
public interface IBPackageInstallerService extends android.os.IInterface
{
  /** Default implementation for IBPackageInstallerService. */
  public static class Default implements top.niunaijun.blackbox.core.system.pm.IBPackageInstallerService
  {
    @Override public int installPackageAsUser(top.niunaijun.blackbox.core.system.pm.BPackageSettings ps, int userId) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public int uninstallPackageAsUser(top.niunaijun.blackbox.core.system.pm.BPackageSettings ps, boolean removeApp, int userId) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public int clearPackage(top.niunaijun.blackbox.core.system.pm.BPackageSettings ps, int userId) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public int updatePackage(top.niunaijun.blackbox.core.system.pm.BPackageSettings ps) throws android.os.RemoteException
    {
      return 0;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements top.niunaijun.blackbox.core.system.pm.IBPackageInstallerService
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an top.niunaijun.blackbox.core.system.pm.IBPackageInstallerService interface,
     * generating a proxy if needed.
     */
    public static top.niunaijun.blackbox.core.system.pm.IBPackageInstallerService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof top.niunaijun.blackbox.core.system.pm.IBPackageInstallerService))) {
        return ((top.niunaijun.blackbox.core.system.pm.IBPackageInstallerService)iin);
      }
      return new top.niunaijun.blackbox.core.system.pm.IBPackageInstallerService.Stub.Proxy(obj);
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
        case TRANSACTION_installPackageAsUser:
        {
          top.niunaijun.blackbox.core.system.pm.BPackageSettings _arg0;
          _arg0 = _Parcel.readTypedObject(data, top.niunaijun.blackbox.core.system.pm.BPackageSettings.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          int _result = this.installPackageAsUser(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_uninstallPackageAsUser:
        {
          top.niunaijun.blackbox.core.system.pm.BPackageSettings _arg0;
          _arg0 = _Parcel.readTypedObject(data, top.niunaijun.blackbox.core.system.pm.BPackageSettings.CREATOR);
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          int _arg2;
          _arg2 = data.readInt();
          int _result = this.uninstallPackageAsUser(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_clearPackage:
        {
          top.niunaijun.blackbox.core.system.pm.BPackageSettings _arg0;
          _arg0 = _Parcel.readTypedObject(data, top.niunaijun.blackbox.core.system.pm.BPackageSettings.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          int _result = this.clearPackage(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_updatePackage:
        {
          top.niunaijun.blackbox.core.system.pm.BPackageSettings _arg0;
          _arg0 = _Parcel.readTypedObject(data, top.niunaijun.blackbox.core.system.pm.BPackageSettings.CREATOR);
          int _result = this.updatePackage(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements top.niunaijun.blackbox.core.system.pm.IBPackageInstallerService
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
      @Override public int installPackageAsUser(top.niunaijun.blackbox.core.system.pm.BPackageSettings ps, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, ps, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_installPackageAsUser, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int uninstallPackageAsUser(top.niunaijun.blackbox.core.system.pm.BPackageSettings ps, boolean removeApp, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, ps, 0);
          _data.writeInt(((removeApp)?(1):(0)));
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_uninstallPackageAsUser, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int clearPackage(top.niunaijun.blackbox.core.system.pm.BPackageSettings ps, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, ps, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_clearPackage, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int updatePackage(top.niunaijun.blackbox.core.system.pm.BPackageSettings ps) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, ps, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_updatePackage, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    static final int TRANSACTION_installPackageAsUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_uninstallPackageAsUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_clearPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_updatePackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "top.niunaijun.blackbox.core.system.pm.IBPackageInstallerService";
  public int installPackageAsUser(top.niunaijun.blackbox.core.system.pm.BPackageSettings ps, int userId) throws android.os.RemoteException;
  public int uninstallPackageAsUser(top.niunaijun.blackbox.core.system.pm.BPackageSettings ps, boolean removeApp, int userId) throws android.os.RemoteException;
  public int clearPackage(top.niunaijun.blackbox.core.system.pm.BPackageSettings ps, int userId) throws android.os.RemoteException;
  public int updatePackage(top.niunaijun.blackbox.core.system.pm.BPackageSettings ps) throws android.os.RemoteException;
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
