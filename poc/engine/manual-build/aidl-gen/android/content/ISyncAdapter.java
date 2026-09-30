/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl/android/content/ISyncAdapter.aidl
 */
package android.content;
/** Interface used to control the sync activity on a SyncAdapter */
public interface ISyncAdapter extends android.os.IInterface
{
  /** Default implementation for ISyncAdapter. */
  public static class Default implements android.content.ISyncAdapter
  {
    /**
     * Initiate a sync for this account. SyncAdapter-specific parameters may
     * be specified in extras, which is guaranteed to not be null.
     * 
     * @param syncContext the ISyncContext used to indicate the progress of the sync. When
     *   the sync is finished (successfully or not) ISyncContext.onFinished() must be called.
     * @param authority the authority that should be synced
     * @param account the account that should be synced
     * @param extras SyncAdapter-specific parameters
     */
    @Override public void startSync(android.content.ISyncContext syncContext, java.lang.String authority, android.accounts.Account account, android.os.Bundle extras) throws android.os.RemoteException
    {
    }
    /**
     * Cancel the most recently initiated sync. Due to race conditions, this may arrive
     * after the ISyncContext.onFinished() for that sync was called.
     * @param syncContext the ISyncContext that was passed to {@link #startSync}
     */
    @Override public void cancelSync(android.content.ISyncContext syncContext) throws android.os.RemoteException
    {
    }
    /**
     * Initialize the SyncAdapter for this account and authority.
     * 
     * @param account the account that should be synced
     * @param authority the authority that should be synced
     */
    @Override public void initialize(android.accounts.Account account, java.lang.String authority) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.content.ISyncAdapter
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.content.ISyncAdapter interface,
     * generating a proxy if needed.
     */
    public static android.content.ISyncAdapter asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.content.ISyncAdapter))) {
        return ((android.content.ISyncAdapter)iin);
      }
      return new android.content.ISyncAdapter.Stub.Proxy(obj);
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
        case TRANSACTION_startSync:
        {
          android.content.ISyncContext _arg0;
          _arg0 = android.content.ISyncContext.Stub.asInterface(data.readStrongBinder());
          java.lang.String _arg1;
          _arg1 = data.readString();
          android.accounts.Account _arg2;
          _arg2 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          android.os.Bundle _arg3;
          _arg3 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          this.startSync(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_cancelSync:
        {
          android.content.ISyncContext _arg0;
          _arg0 = android.content.ISyncContext.Stub.asInterface(data.readStrongBinder());
          this.cancelSync(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_initialize:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.initialize(_arg0, _arg1);
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
    private static class Proxy implements android.content.ISyncAdapter
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
       * Initiate a sync for this account. SyncAdapter-specific parameters may
       * be specified in extras, which is guaranteed to not be null.
       * 
       * @param syncContext the ISyncContext used to indicate the progress of the sync. When
       *   the sync is finished (successfully or not) ISyncContext.onFinished() must be called.
       * @param authority the authority that should be synced
       * @param account the account that should be synced
       * @param extras SyncAdapter-specific parameters
       */
      @Override public void startSync(android.content.ISyncContext syncContext, java.lang.String authority, android.accounts.Account account, android.os.Bundle extras) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(syncContext);
          _data.writeString(authority);
          _Parcel.writeTypedObject(_data, account, 0);
          _Parcel.writeTypedObject(_data, extras, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_startSync, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
       * Cancel the most recently initiated sync. Due to race conditions, this may arrive
       * after the ISyncContext.onFinished() for that sync was called.
       * @param syncContext the ISyncContext that was passed to {@link #startSync}
       */
      @Override public void cancelSync(android.content.ISyncContext syncContext) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(syncContext);
          boolean _status = mRemote.transact(Stub.TRANSACTION_cancelSync, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
       * Initialize the SyncAdapter for this account and authority.
       * 
       * @param account the account that should be synced
       * @param authority the authority that should be synced
       */
      @Override public void initialize(android.accounts.Account account, java.lang.String authority) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(authority);
          boolean _status = mRemote.transact(Stub.TRANSACTION_initialize, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "android.content.ISyncAdapter";
    static final int TRANSACTION_startSync = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_cancelSync = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_initialize = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
  }
  /**
   * Initiate a sync for this account. SyncAdapter-specific parameters may
   * be specified in extras, which is guaranteed to not be null.
   * 
   * @param syncContext the ISyncContext used to indicate the progress of the sync. When
   *   the sync is finished (successfully or not) ISyncContext.onFinished() must be called.
   * @param authority the authority that should be synced
   * @param account the account that should be synced
   * @param extras SyncAdapter-specific parameters
   */
  public void startSync(android.content.ISyncContext syncContext, java.lang.String authority, android.accounts.Account account, android.os.Bundle extras) throws android.os.RemoteException;
  /**
   * Cancel the most recently initiated sync. Due to race conditions, this may arrive
   * after the ISyncContext.onFinished() for that sync was called.
   * @param syncContext the ISyncContext that was passed to {@link #startSync}
   */
  public void cancelSync(android.content.ISyncContext syncContext) throws android.os.RemoteException;
  /**
   * Initialize the SyncAdapter for this account and authority.
   * 
   * @param account the account that should be synced
   * @param authority the authority that should be synced
   */
  public void initialize(android.accounts.Account account, java.lang.String authority) throws android.os.RemoteException;
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
