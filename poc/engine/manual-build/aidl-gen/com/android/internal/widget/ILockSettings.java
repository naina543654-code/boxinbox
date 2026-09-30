/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl/com/android/internal/widget/ILockSettings.aidl
 */
package com.android.internal.widget;
public interface ILockSettings extends android.os.IInterface
{
  /** Default implementation for ILockSettings. */
  public static class Default implements com.android.internal.widget.ILockSettings
  {
    @Override public void setRecoverySecretTypes(int[] secretTypes) throws android.os.RemoteException
    {
    }
    @Override public int[] getRecoverySecretTypes() throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.android.internal.widget.ILockSettings
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.android.internal.widget.ILockSettings interface,
     * generating a proxy if needed.
     */
    public static com.android.internal.widget.ILockSettings asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.android.internal.widget.ILockSettings))) {
        return ((com.android.internal.widget.ILockSettings)iin);
      }
      return new com.android.internal.widget.ILockSettings.Stub.Proxy(obj);
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
        case TRANSACTION_setRecoverySecretTypes:
        {
          int[] _arg0;
          _arg0 = data.createIntArray();
          this.setRecoverySecretTypes(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getRecoverySecretTypes:
        {
          int[] _result = this.getRecoverySecretTypes();
          reply.writeNoException();
          reply.writeIntArray(_result);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements com.android.internal.widget.ILockSettings
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
      @Override public void setRecoverySecretTypes(int[] secretTypes) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeIntArray(secretTypes);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setRecoverySecretTypes, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public int[] getRecoverySecretTypes() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getRecoverySecretTypes, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createIntArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "com.android.internal.widget.ILockSettings";
    static final int TRANSACTION_setRecoverySecretTypes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getRecoverySecretTypes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  public void setRecoverySecretTypes(int[] secretTypes) throws android.os.RemoteException;
  public int[] getRecoverySecretTypes() throws android.os.RemoteException;
}
