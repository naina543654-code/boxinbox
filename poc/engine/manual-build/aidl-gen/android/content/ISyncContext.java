/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl/android/content/ISyncContext.aidl
 */
package android.content;
/**
 * Interface used by the SyncAdapter to indicate its progress.
 * @hide
 */
public interface ISyncContext extends android.os.IInterface
{
  /** Default implementation for ISyncContext. */
  public static class Default implements android.content.ISyncContext
  {
    /**
     * Call to indicate that the SyncAdapter is making progress. E.g., if this SyncAdapter
     * downloads or sends records to/from the server, this may be called after each record
     * is downloaded or uploaded.
     */
    @Override public void sendHeartbeat() throws android.os.RemoteException
    {
    }
    /**
     * Signal that the corresponding sync session is completed.
     * @param result information about this sync session
     */
    @Override public void onFinished(android.content.SyncResult result) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.content.ISyncContext
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.content.ISyncContext interface,
     * generating a proxy if needed.
     */
    public static android.content.ISyncContext asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.content.ISyncContext))) {
        return ((android.content.ISyncContext)iin);
      }
      return new android.content.ISyncContext.Stub.Proxy(obj);
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
        case TRANSACTION_sendHeartbeat:
        {
          this.sendHeartbeat();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_onFinished:
        {
          android.content.SyncResult _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.SyncResult.CREATOR);
          this.onFinished(_arg0);
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
    private static class Proxy implements android.content.ISyncContext
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
       * Call to indicate that the SyncAdapter is making progress. E.g., if this SyncAdapter
       * downloads or sends records to/from the server, this may be called after each record
       * is downloaded or uploaded.
       */
      @Override public void sendHeartbeat() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_sendHeartbeat, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
       * Signal that the corresponding sync session is completed.
       * @param result information about this sync session
       */
      @Override public void onFinished(android.content.SyncResult result) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, result, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onFinished, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "android.content.ISyncContext";
    static final int TRANSACTION_sendHeartbeat = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onFinished = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  /**
   * Call to indicate that the SyncAdapter is making progress. E.g., if this SyncAdapter
   * downloads or sends records to/from the server, this may be called after each record
   * is downloaded or uploaded.
   */
  public void sendHeartbeat() throws android.os.RemoteException;
  /**
   * Signal that the corresponding sync session is completed.
   * @param result information about this sync session
   */
  public void onFinished(android.content.SyncResult result) throws android.os.RemoteException;
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
