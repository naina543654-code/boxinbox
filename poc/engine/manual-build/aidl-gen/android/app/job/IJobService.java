/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl/android/app/job/IJobService.aidl
 */
package android.app.job;
/**
 * Interface that the framework uses to communicate with application code that implements a
 * JobService.  End user code does not implement this interface directly; instead, the app's
 * service implementation will extend android.app.job.JobService.
 */
public interface IJobService extends android.os.IInterface
{
  /** Default implementation for IJobService. */
  public static class Default implements android.app.job.IJobService
  {
    /** Begin execution of application's job. */
    @Override public void startJob(android.app.job.JobParameters jobParams) throws android.os.RemoteException
    {
    }
    /** Stop execution of application's job. */
    @Override public void stopJob(android.app.job.JobParameters jobParams) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.app.job.IJobService
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.app.job.IJobService interface,
     * generating a proxy if needed.
     */
    public static android.app.job.IJobService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.app.job.IJobService))) {
        return ((android.app.job.IJobService)iin);
      }
      return new android.app.job.IJobService.Stub.Proxy(obj);
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
        case TRANSACTION_startJob:
        {
          android.app.job.JobParameters _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.app.job.JobParameters.CREATOR);
          this.startJob(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_stopJob:
        {
          android.app.job.JobParameters _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.app.job.JobParameters.CREATOR);
          this.stopJob(_arg0);
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
    private static class Proxy implements android.app.job.IJobService
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
      /** Begin execution of application's job. */
      @Override public void startJob(android.app.job.JobParameters jobParams) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, jobParams, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_startJob, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /** Stop execution of application's job. */
      @Override public void stopJob(android.app.job.JobParameters jobParams) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, jobParams, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_stopJob, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "android.app.job.IJobService";
    static final int TRANSACTION_startJob = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_stopJob = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  /** Begin execution of application's job. */
  public void startJob(android.app.job.JobParameters jobParams) throws android.os.RemoteException;
  /** Stop execution of application's job. */
  public void stopJob(android.app.job.JobParameters jobParams) throws android.os.RemoteException;
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
