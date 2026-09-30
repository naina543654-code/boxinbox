/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl/android/content/pm/IPackageInstallerSession.aidl
 */
package android.content.pm;
public interface IPackageInstallerSession extends android.os.IInterface
{
  /** Default implementation for IPackageInstallerSession. */
  public static class Default implements android.content.pm.IPackageInstallerSession
  {
    @Override public void setClientProgress(float progress) throws android.os.RemoteException
    {
    }
    @Override public void addClientProgress(float progress) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String[] getNames() throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.os.ParcelFileDescriptor openWrite(java.lang.String name, long offsetBytes, long lengthBytes) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.os.ParcelFileDescriptor openRead(java.lang.String name) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void removeSplit(java.lang.String splitName) throws android.os.RemoteException
    {
    }
    @Override public void close() throws android.os.RemoteException
    {
    }
    @Override public void commit(android.content.IntentSender statusReceiver) throws android.os.RemoteException
    {
    }
    @Override public void abandon() throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.content.pm.IPackageInstallerSession
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.content.pm.IPackageInstallerSession interface,
     * generating a proxy if needed.
     */
    public static android.content.pm.IPackageInstallerSession asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.content.pm.IPackageInstallerSession))) {
        return ((android.content.pm.IPackageInstallerSession)iin);
      }
      return new android.content.pm.IPackageInstallerSession.Stub.Proxy(obj);
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
        case TRANSACTION_setClientProgress:
        {
          float _arg0;
          _arg0 = data.readFloat();
          this.setClientProgress(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addClientProgress:
        {
          float _arg0;
          _arg0 = data.readFloat();
          this.addClientProgress(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getNames:
        {
          java.lang.String[] _result = this.getNames();
          reply.writeNoException();
          reply.writeStringArray(_result);
          break;
        }
        case TRANSACTION_openWrite:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          long _arg1;
          _arg1 = data.readLong();
          long _arg2;
          _arg2 = data.readLong();
          android.os.ParcelFileDescriptor _result = this.openWrite(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_openRead:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          android.os.ParcelFileDescriptor _result = this.openRead(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_removeSplit:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.removeSplit(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_close:
        {
          this.close();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_commit:
        {
          android.content.IntentSender _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.IntentSender.CREATOR);
          this.commit(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_abandon:
        {
          this.abandon();
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
    private static class Proxy implements android.content.pm.IPackageInstallerSession
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
      @Override public void setClientProgress(float progress) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeFloat(progress);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setClientProgress, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addClientProgress(float progress) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeFloat(progress);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addClientProgress, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String[] getNames() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getNames, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.os.ParcelFileDescriptor openWrite(java.lang.String name, long offsetBytes, long lengthBytes) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.ParcelFileDescriptor _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          _data.writeLong(offsetBytes);
          _data.writeLong(lengthBytes);
          boolean _status = mRemote.transact(Stub.TRANSACTION_openWrite, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.os.ParcelFileDescriptor.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.os.ParcelFileDescriptor openRead(java.lang.String name) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.os.ParcelFileDescriptor _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(name);
          boolean _status = mRemote.transact(Stub.TRANSACTION_openRead, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.os.ParcelFileDescriptor.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void removeSplit(java.lang.String splitName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(splitName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeSplit, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void close() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_close, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void commit(android.content.IntentSender statusReceiver) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, statusReceiver, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_commit, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void abandon() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_abandon, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "android.content.pm.IPackageInstallerSession";
    static final int TRANSACTION_setClientProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_addClientProgress = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_getNames = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_openWrite = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_openRead = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_removeSplit = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_close = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_commit = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_abandon = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
  }
  public void setClientProgress(float progress) throws android.os.RemoteException;
  public void addClientProgress(float progress) throws android.os.RemoteException;
  public java.lang.String[] getNames() throws android.os.RemoteException;
  public android.os.ParcelFileDescriptor openWrite(java.lang.String name, long offsetBytes, long lengthBytes) throws android.os.RemoteException;
  public android.os.ParcelFileDescriptor openRead(java.lang.String name) throws android.os.RemoteException;
  public void removeSplit(java.lang.String splitName) throws android.os.RemoteException;
  public void close() throws android.os.RemoteException;
  public void commit(android.content.IntentSender statusReceiver) throws android.os.RemoteException;
  public void abandon() throws android.os.RemoteException;
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
