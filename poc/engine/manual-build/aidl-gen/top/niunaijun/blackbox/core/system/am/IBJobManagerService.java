/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl/top/niunaijun/blackbox/core/system/am/IBJobManagerService.aidl
 */
package top.niunaijun.blackbox.core.system.am;
// Declare any non-default types here with import statements
public interface IBJobManagerService extends android.os.IInterface
{
  /** Default implementation for IBJobManagerService. */
  public static class Default implements top.niunaijun.blackbox.core.system.am.IBJobManagerService
  {
    @Override public android.app.job.JobInfo schedule(android.app.job.JobInfo info, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public top.niunaijun.blackbox.entity.JobRecord queryJobRecord(java.lang.String processName, int jobId, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void cancelAll(java.lang.String processName, int userId) throws android.os.RemoteException
    {
    }
    @Override public int cancel(java.lang.String processName, int jobId, int userId) throws android.os.RemoteException
    {
      return 0;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements top.niunaijun.blackbox.core.system.am.IBJobManagerService
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an top.niunaijun.blackbox.core.system.am.IBJobManagerService interface,
     * generating a proxy if needed.
     */
    public static top.niunaijun.blackbox.core.system.am.IBJobManagerService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof top.niunaijun.blackbox.core.system.am.IBJobManagerService))) {
        return ((top.niunaijun.blackbox.core.system.am.IBJobManagerService)iin);
      }
      return new top.niunaijun.blackbox.core.system.am.IBJobManagerService.Stub.Proxy(obj);
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
        case TRANSACTION_schedule:
        {
          android.app.job.JobInfo _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.app.job.JobInfo.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          android.app.job.JobInfo _result = this.schedule(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_queryJobRecord:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          top.niunaijun.blackbox.entity.JobRecord _result = this.queryJobRecord(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_cancelAll:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          this.cancelAll(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_cancel:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          int _result = this.cancel(_arg0, _arg1, _arg2);
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
    private static class Proxy implements top.niunaijun.blackbox.core.system.am.IBJobManagerService
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
      @Override public android.app.job.JobInfo schedule(android.app.job.JobInfo info, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.app.job.JobInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, info, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_schedule, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.app.job.JobInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public top.niunaijun.blackbox.entity.JobRecord queryJobRecord(java.lang.String processName, int jobId, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        top.niunaijun.blackbox.entity.JobRecord _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(processName);
          _data.writeInt(jobId);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_queryJobRecord, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, top.niunaijun.blackbox.entity.JobRecord.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void cancelAll(java.lang.String processName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(processName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_cancelAll, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int cancel(java.lang.String processName, int jobId, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(processName);
          _data.writeInt(jobId);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_cancel, _data, _reply, 0);
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
    static final int TRANSACTION_schedule = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_queryJobRecord = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_cancelAll = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_cancel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "top.niunaijun.blackbox.core.system.am.IBJobManagerService";
  public android.app.job.JobInfo schedule(android.app.job.JobInfo info, int userId) throws android.os.RemoteException;
  public top.niunaijun.blackbox.entity.JobRecord queryJobRecord(java.lang.String processName, int jobId, int userId) throws android.os.RemoteException;
  public void cancelAll(java.lang.String processName, int userId) throws android.os.RemoteException;
  public int cancel(java.lang.String processName, int jobId, int userId) throws android.os.RemoteException;
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
