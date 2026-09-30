/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl/android/app/job/IJobCallback.aidl
 */
package android.app.job;
/**
 * The server side of the JobScheduler IPC protocols.  The app-side implementation
 * invokes on this interface to indicate completion of the (asynchronous) instructions
 * issued by the server.
 * 
 * In all cases, the 'who' parameter is the caller's service binder, used to track
 * which Job Service instance is reporting.
 */
public interface IJobCallback extends android.os.IInterface
{
  /** Default implementation for IJobCallback. */
  public static class Default implements android.app.job.IJobCallback
  {
    /**
     * Immediate callback to the system after sending a start signal, used to quickly detect ANR.
     * 
     * @param jobId Unique integer used to identify this job.
     * @param ongoing True to indicate that the client is processing the job. False if the job is
     * complete
     */
    @Override public void acknowledgeStartMessage(int jobId, boolean ongoing) throws android.os.RemoteException
    {
    }
    /**
     * Immediate callback to the system after sending a stop signal, used to quickly detect ANR.
     * 
     * @param jobId Unique integer used to identify this job.
     * @param reschedule Whether or not to reschedule this job.
     */
    @Override public void acknowledgeStopMessage(int jobId, boolean reschedule) throws android.os.RemoteException
    {
    }
    /** Called to deqeue next work item for the job. */
    @Override public android.app.job.JobWorkItem dequeueWork(int jobId) throws android.os.RemoteException
    {
      return null;
    }
    /** Called to report that job has completed processing a work item. */
    @Override public boolean completeWork(int jobId, int workId) throws android.os.RemoteException
    {
      return false;
    }
    /**
     * Tell the job manager that the client is done with its execution, so that it can go on to
     * the next one and stop attributing wakelock time to us etc.
     * 
     * @param jobId Unique integer used to identify this job.
     * @param reschedule Whether or not to reschedule this job.
     */
    @Override public void jobFinished(int jobId, boolean reschedule) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.app.job.IJobCallback
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.app.job.IJobCallback interface,
     * generating a proxy if needed.
     */
    public static android.app.job.IJobCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.app.job.IJobCallback))) {
        return ((android.app.job.IJobCallback)iin);
      }
      return new android.app.job.IJobCallback.Stub.Proxy(obj);
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
        case TRANSACTION_acknowledgeStartMessage:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.acknowledgeStartMessage(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_acknowledgeStopMessage:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.acknowledgeStopMessage(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_dequeueWork:
        {
          int _arg0;
          _arg0 = data.readInt();
          android.app.job.JobWorkItem _result = this.dequeueWork(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_completeWork:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          boolean _result = this.completeWork(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_jobFinished:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.jobFinished(_arg0, _arg1);
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
    private static class Proxy implements android.app.job.IJobCallback
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
      /**
       * Immediate callback to the system after sending a start signal, used to quickly detect ANR.
       * 
       * @param jobId Unique integer used to identify this job.
       * @param ongoing True to indicate that the client is processing the job. False if the job is
       * complete
       */
      @Override public void acknowledgeStartMessage(int jobId, boolean ongoing) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(jobId);
          _data.writeInt(((ongoing)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_acknowledgeStartMessage, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
       * Immediate callback to the system after sending a stop signal, used to quickly detect ANR.
       * 
       * @param jobId Unique integer used to identify this job.
       * @param reschedule Whether or not to reschedule this job.
       */
      @Override public void acknowledgeStopMessage(int jobId, boolean reschedule) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(jobId);
          _data.writeInt(((reschedule)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_acknowledgeStopMessage, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /** Called to deqeue next work item for the job. */
      @Override public android.app.job.JobWorkItem dequeueWork(int jobId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.app.job.JobWorkItem _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(jobId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_dequeueWork, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.app.job.JobWorkItem.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /** Called to report that job has completed processing a work item. */
      @Override public boolean completeWork(int jobId, int workId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(jobId);
          _data.writeInt(workId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_completeWork, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /**
       * Tell the job manager that the client is done with its execution, so that it can go on to
       * the next one and stop attributing wakelock time to us etc.
       * 
       * @param jobId Unique integer used to identify this job.
       * @param reschedule Whether or not to reschedule this job.
       */
      @Override public void jobFinished(int jobId, boolean reschedule) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(jobId);
          _data.writeInt(((reschedule)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_jobFinished, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "android.app.job.IJobCallback";
    static final int TRANSACTION_acknowledgeStartMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_acknowledgeStopMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_dequeueWork = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_completeWork = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_jobFinished = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
  }
  /**
   * Immediate callback to the system after sending a start signal, used to quickly detect ANR.
   * 
   * @param jobId Unique integer used to identify this job.
   * @param ongoing True to indicate that the client is processing the job. False if the job is
   * complete
   */
  public void acknowledgeStartMessage(int jobId, boolean ongoing) throws android.os.RemoteException;
  /**
   * Immediate callback to the system after sending a stop signal, used to quickly detect ANR.
   * 
   * @param jobId Unique integer used to identify this job.
   * @param reschedule Whether or not to reschedule this job.
   */
  public void acknowledgeStopMessage(int jobId, boolean reschedule) throws android.os.RemoteException;
  /** Called to deqeue next work item for the job. */
  public android.app.job.JobWorkItem dequeueWork(int jobId) throws android.os.RemoteException;
  /** Called to report that job has completed processing a work item. */
  public boolean completeWork(int jobId, int workId) throws android.os.RemoteException;
  /**
   * Tell the job manager that the client is done with its execution, so that it can go on to
   * the next one and stop attributing wakelock time to us etc.
   * 
   * @param jobId Unique integer used to identify this job.
   * @param reschedule Whether or not to reschedule this job.
   */
  public void jobFinished(int jobId, boolean reschedule) throws android.os.RemoteException;
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
