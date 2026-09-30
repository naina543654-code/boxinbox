/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay/top/niunaijun/blackbox/core/IBActivityThread.aidl
 */
package top.niunaijun.blackbox.core;
public interface IBActivityThread extends android.os.IInterface
{
  /** Default implementation for IBActivityThread. */
  public static class Default implements top.niunaijun.blackbox.core.IBActivityThread
  {
    @Override public android.os.IBinder getActivityThread() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void bindApplication() throws android.os.RemoteException
    {
    }
    @Override public void restartJobService(java.lang.String selfId) throws android.os.RemoteException
    {
    }
    @Override public android.os.IBinder acquireContentProviderClient(android.content.pm.ProviderInfo providerInfo) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.os.IBinder peekService(android.content.Intent intent) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void stopService(android.content.Intent componentName) throws android.os.RemoteException
    {
    }
    @Override public void finishActivity(android.os.IBinder token) throws android.os.RemoteException
    {
    }
    @Override public void handleNewIntent(android.os.IBinder token, android.content.Intent intent) throws android.os.RemoteException
    {
    }
    @Override public void scheduleReceiver(top.niunaijun.blackbox.entity.am.ReceiverData data) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements top.niunaijun.blackbox.core.IBActivityThread
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an top.niunaijun.blackbox.core.IBActivityThread interface,
     * generating a proxy if needed.
     */
    public static top.niunaijun.blackbox.core.IBActivityThread asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof top.niunaijun.blackbox.core.IBActivityThread))) {
        return ((top.niunaijun.blackbox.core.IBActivityThread)iin);
      }
      return new top.niunaijun.blackbox.core.IBActivityThread.Stub.Proxy(obj);
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
        case TRANSACTION_getActivityThread:
        {
          android.os.IBinder _result = this.getActivityThread();
          reply.writeNoException();
          reply.writeStrongBinder(_result);
          break;
        }
        case TRANSACTION_bindApplication:
        {
          this.bindApplication();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_restartJobService:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.restartJobService(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_acquireContentProviderClient:
        {
          android.content.pm.ProviderInfo _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.pm.ProviderInfo.CREATOR);
          android.os.IBinder _result = this.acquireContentProviderClient(_arg0);
          reply.writeNoException();
          reply.writeStrongBinder(_result);
          break;
        }
        case TRANSACTION_peekService:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          android.os.IBinder _result = this.peekService(_arg0);
          reply.writeNoException();
          reply.writeStrongBinder(_result);
          break;
        }
        case TRANSACTION_stopService:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          this.stopService(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_finishActivity:
        {
          android.os.IBinder _arg0;
          _arg0 = data.readStrongBinder();
          this.finishActivity(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_handleNewIntent:
        {
          android.os.IBinder _arg0;
          _arg0 = data.readStrongBinder();
          android.content.Intent _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          this.handleNewIntent(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_scheduleReceiver:
        {
          top.niunaijun.blackbox.entity.am.ReceiverData _arg0;
          _arg0 = _Parcel.readTypedObject(data, top.niunaijun.blackbox.entity.am.ReceiverData.CREATOR);
          this.scheduleReceiver(_arg0);
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
    private static class Proxy implements top.niunaijun.blackbox.core.IBActivityThread
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
      @Override public android.os.IBinder getActivityThread() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.IBinder _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getActivityThread, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readStrongBinder();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void bindApplication() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_bindApplication, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void restartJobService(java.lang.String selfId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(selfId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_restartJobService, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public android.os.IBinder acquireContentProviderClient(android.content.pm.ProviderInfo providerInfo) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.IBinder _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, providerInfo, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_acquireContentProviderClient, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readStrongBinder();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.os.IBinder peekService(android.content.Intent intent) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.IBinder _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_peekService, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readStrongBinder();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void stopService(android.content.Intent componentName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, componentName, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_stopService, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void finishActivity(android.os.IBinder token) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder(token);
          boolean _status = mRemote.transact(Stub.TRANSACTION_finishActivity, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void handleNewIntent(android.os.IBinder token, android.content.Intent intent) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder(token);
          _Parcel.writeTypedObject(_data, intent, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_handleNewIntent, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void scheduleReceiver(top.niunaijun.blackbox.entity.am.ReceiverData data) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, data, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_scheduleReceiver, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_getActivityThread = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_bindApplication = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_restartJobService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_acquireContentProviderClient = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_peekService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_stopService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_finishActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_handleNewIntent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_scheduleReceiver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "top.niunaijun.blackbox.core.IBActivityThread";
  public android.os.IBinder getActivityThread() throws android.os.RemoteException;
  public void bindApplication() throws android.os.RemoteException;
  public void restartJobService(java.lang.String selfId) throws android.os.RemoteException;
  public android.os.IBinder acquireContentProviderClient(android.content.pm.ProviderInfo providerInfo) throws android.os.RemoteException;
  public android.os.IBinder peekService(android.content.Intent intent) throws android.os.RemoteException;
  public void stopService(android.content.Intent componentName) throws android.os.RemoteException;
  public void finishActivity(android.os.IBinder token) throws android.os.RemoteException;
  public void handleNewIntent(android.os.IBinder token, android.content.Intent intent) throws android.os.RemoteException;
  public void scheduleReceiver(top.niunaijun.blackbox.entity.am.ReceiverData data) throws android.os.RemoteException;
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
