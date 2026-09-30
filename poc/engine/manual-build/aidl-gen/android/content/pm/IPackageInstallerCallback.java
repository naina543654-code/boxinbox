/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl/android/content/pm/IPackageInstallerCallback.aidl
 */
package android.content.pm;
public interface IPackageInstallerCallback extends android.os.IInterface
{
  /** Default implementation for IPackageInstallerCallback. */
  public static class Default implements android.content.pm.IPackageInstallerCallback
  {
    @Override public void onSessionCreated(int sessionId) throws android.os.RemoteException
    {
    }
    @Override public void onSessionBadgingChanged(int sessionId) throws android.os.RemoteException
    {
    }
    @Override public void onSessionActiveChanged(int sessionId, boolean active) throws android.os.RemoteException
    {
    }
    @Override public void onSessionProgressChanged(int sessionId, float progress) throws android.os.RemoteException
    {
    }
    @Override public void onSessionFinished(int sessionId, boolean success) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.content.pm.IPackageInstallerCallback
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.content.pm.IPackageInstallerCallback interface,
     * generating a proxy if needed.
     */
    public static android.content.pm.IPackageInstallerCallback asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.content.pm.IPackageInstallerCallback))) {
        return ((android.content.pm.IPackageInstallerCallback)iin);
      }
      return new android.content.pm.IPackageInstallerCallback.Stub.Proxy(obj);
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
        case TRANSACTION_onSessionCreated:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.onSessionCreated(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_onSessionBadgingChanged:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.onSessionBadgingChanged(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_onSessionActiveChanged:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.onSessionActiveChanged(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_onSessionProgressChanged:
        {
          int _arg0;
          _arg0 = data.readInt();
          float _arg1;
          _arg1 = data.readFloat();
          this.onSessionProgressChanged(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_onSessionFinished:
        {
          int _arg0;
          _arg0 = data.readInt();
          boolean _arg1;
          _arg1 = (0!=data.readInt());
          this.onSessionFinished(_arg0, _arg1);
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
    private static class Proxy implements android.content.pm.IPackageInstallerCallback
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
      @Override public void onSessionCreated(int sessionId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(sessionId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSessionCreated, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onSessionBadgingChanged(int sessionId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(sessionId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSessionBadgingChanged, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onSessionActiveChanged(int sessionId, boolean active) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(sessionId);
          _data.writeInt(((active)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSessionActiveChanged, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onSessionProgressChanged(int sessionId, float progress) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(sessionId);
          _data.writeFloat(progress);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSessionProgressChanged, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void onSessionFinished(int sessionId, boolean success) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(sessionId);
          _data.writeInt(((success)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_onSessionFinished, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "android.content.pm.IPackageInstallerCallback";
    static final int TRANSACTION_onSessionCreated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onSessionBadgingChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_onSessionActiveChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_onSessionProgressChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_onSessionFinished = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
  }
  public void onSessionCreated(int sessionId) throws android.os.RemoteException;
  public void onSessionBadgingChanged(int sessionId) throws android.os.RemoteException;
  public void onSessionActiveChanged(int sessionId, boolean active) throws android.os.RemoteException;
  public void onSessionProgressChanged(int sessionId, float progress) throws android.os.RemoteException;
  public void onSessionFinished(int sessionId, boolean success) throws android.os.RemoteException;
}
