/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl/top/niunaijun/blackbox/core/system/location/IBLocationManagerService.aidl
 */
package top.niunaijun.blackbox.core.system.location;
public interface IBLocationManagerService extends android.os.IInterface
{
  /** Default implementation for IBLocationManagerService. */
  public static class Default implements top.niunaijun.blackbox.core.system.location.IBLocationManagerService
  {
    @Override public int getPattern(int userId, java.lang.String pkg) throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void setPattern(int userId, java.lang.String pkg, int mode) throws android.os.RemoteException
    {
    }
    @Override public void setCell(int userId, java.lang.String pkg, top.niunaijun.blackbox.entity.location.BCell cell) throws android.os.RemoteException
    {
    }
    @Override public void setAllCell(int userId, java.lang.String pkg, java.util.List<top.niunaijun.blackbox.entity.location.BCell> cell) throws android.os.RemoteException
    {
    }
    @Override public void setNeighboringCell(int userId, java.lang.String pkg, java.util.List<top.niunaijun.blackbox.entity.location.BCell> cells) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<top.niunaijun.blackbox.entity.location.BCell> getNeighboringCell(int userId, java.lang.String pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setGlobalCell(top.niunaijun.blackbox.entity.location.BCell cell) throws android.os.RemoteException
    {
    }
    @Override public void setGlobalAllCell(java.util.List<top.niunaijun.blackbox.entity.location.BCell> cell) throws android.os.RemoteException
    {
    }
    @Override public void setGlobalNeighboringCell(java.util.List<top.niunaijun.blackbox.entity.location.BCell> cell) throws android.os.RemoteException
    {
    }
    @Override public java.util.List<top.niunaijun.blackbox.entity.location.BCell> getGlobalNeighboringCell() throws android.os.RemoteException
    {
      return null;
    }
    @Override public top.niunaijun.blackbox.entity.location.BCell getCell(int userId, java.lang.String pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<top.niunaijun.blackbox.entity.location.BCell> getAllCell(int userId, java.lang.String pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setLocation(int userId, java.lang.String pkg, top.niunaijun.blackbox.entity.location.BLocation location) throws android.os.RemoteException
    {
    }
    @Override public top.niunaijun.blackbox.entity.location.BLocation getLocation(int userId, java.lang.String pkg) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setGlobalLocation(top.niunaijun.blackbox.entity.location.BLocation location) throws android.os.RemoteException
    {
    }
    @Override public top.niunaijun.blackbox.entity.location.BLocation getGlobalLocation() throws android.os.RemoteException
    {
      return null;
    }
    @Override public void requestLocationUpdates(android.os.IBinder listener, java.lang.String packageName, int userId) throws android.os.RemoteException
    {
    }
    @Override public void removeUpdates(android.os.IBinder listener) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements top.niunaijun.blackbox.core.system.location.IBLocationManagerService
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an top.niunaijun.blackbox.core.system.location.IBLocationManagerService interface,
     * generating a proxy if needed.
     */
    public static top.niunaijun.blackbox.core.system.location.IBLocationManagerService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof top.niunaijun.blackbox.core.system.location.IBLocationManagerService))) {
        return ((top.niunaijun.blackbox.core.system.location.IBLocationManagerService)iin);
      }
      return new top.niunaijun.blackbox.core.system.location.IBLocationManagerService.Stub.Proxy(obj);
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
        case TRANSACTION_getPattern:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _result = this.getPattern(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_setPattern:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          this.setPattern(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setCell:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          top.niunaijun.blackbox.entity.location.BCell _arg2;
          _arg2 = _Parcel.readTypedObject(data, top.niunaijun.blackbox.entity.location.BCell.CREATOR);
          this.setCell(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setAllCell:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.util.List<top.niunaijun.blackbox.entity.location.BCell> _arg2;
          _arg2 = data.createTypedArrayList(top.niunaijun.blackbox.entity.location.BCell.CREATOR);
          this.setAllCell(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setNeighboringCell:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.util.List<top.niunaijun.blackbox.entity.location.BCell> _arg2;
          _arg2 = data.createTypedArrayList(top.niunaijun.blackbox.entity.location.BCell.CREATOR);
          this.setNeighboringCell(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getNeighboringCell:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.util.List<top.niunaijun.blackbox.entity.location.BCell> _result = this.getNeighboringCell(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_setGlobalCell:
        {
          top.niunaijun.blackbox.entity.location.BCell _arg0;
          _arg0 = _Parcel.readTypedObject(data, top.niunaijun.blackbox.entity.location.BCell.CREATOR);
          this.setGlobalCell(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setGlobalAllCell:
        {
          java.util.List<top.niunaijun.blackbox.entity.location.BCell> _arg0;
          _arg0 = data.createTypedArrayList(top.niunaijun.blackbox.entity.location.BCell.CREATOR);
          this.setGlobalAllCell(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setGlobalNeighboringCell:
        {
          java.util.List<top.niunaijun.blackbox.entity.location.BCell> _arg0;
          _arg0 = data.createTypedArrayList(top.niunaijun.blackbox.entity.location.BCell.CREATOR);
          this.setGlobalNeighboringCell(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getGlobalNeighboringCell:
        {
          java.util.List<top.niunaijun.blackbox.entity.location.BCell> _result = this.getGlobalNeighboringCell();
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getCell:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          top.niunaijun.blackbox.entity.location.BCell _result = this.getCell(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getAllCell:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.util.List<top.niunaijun.blackbox.entity.location.BCell> _result = this.getAllCell(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_setLocation:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          top.niunaijun.blackbox.entity.location.BLocation _arg2;
          _arg2 = _Parcel.readTypedObject(data, top.niunaijun.blackbox.entity.location.BLocation.CREATOR);
          this.setLocation(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getLocation:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          top.niunaijun.blackbox.entity.location.BLocation _result = this.getLocation(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_setGlobalLocation:
        {
          top.niunaijun.blackbox.entity.location.BLocation _arg0;
          _arg0 = _Parcel.readTypedObject(data, top.niunaijun.blackbox.entity.location.BLocation.CREATOR);
          this.setGlobalLocation(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getGlobalLocation:
        {
          top.niunaijun.blackbox.entity.location.BLocation _result = this.getGlobalLocation();
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_requestLocationUpdates:
        {
          android.os.IBinder _arg0;
          _arg0 = data.readStrongBinder();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          this.requestLocationUpdates(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_removeUpdates:
        {
          android.os.IBinder _arg0;
          _arg0 = data.readStrongBinder();
          this.removeUpdates(_arg0);
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
    private static class Proxy implements top.niunaijun.blackbox.core.system.location.IBLocationManagerService
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
      @Override public int getPattern(int userId, java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPattern, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setPattern(int userId, java.lang.String pkg, int mode) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(pkg);
          _data.writeInt(mode);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPattern, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setCell(int userId, java.lang.String pkg, top.niunaijun.blackbox.entity.location.BCell cell) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(pkg);
          _Parcel.writeTypedObject(_data, cell, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setCell, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setAllCell(int userId, java.lang.String pkg, java.util.List<top.niunaijun.blackbox.entity.location.BCell> cell) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(pkg);
          _Parcel.writeTypedList(_data, cell, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setAllCell, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setNeighboringCell(int userId, java.lang.String pkg, java.util.List<top.niunaijun.blackbox.entity.location.BCell> cells) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(pkg);
          _Parcel.writeTypedList(_data, cells, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setNeighboringCell, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<top.niunaijun.blackbox.entity.location.BCell> getNeighboringCell(int userId, java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<top.niunaijun.blackbox.entity.location.BCell> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getNeighboringCell, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(top.niunaijun.blackbox.entity.location.BCell.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setGlobalCell(top.niunaijun.blackbox.entity.location.BCell cell) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, cell, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setGlobalCell, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setGlobalAllCell(java.util.List<top.niunaijun.blackbox.entity.location.BCell> cell) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedList(_data, cell, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setGlobalAllCell, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setGlobalNeighboringCell(java.util.List<top.niunaijun.blackbox.entity.location.BCell> cell) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedList(_data, cell, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setGlobalNeighboringCell, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.util.List<top.niunaijun.blackbox.entity.location.BCell> getGlobalNeighboringCell() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<top.niunaijun.blackbox.entity.location.BCell> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getGlobalNeighboringCell, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(top.niunaijun.blackbox.entity.location.BCell.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public top.niunaijun.blackbox.entity.location.BCell getCell(int userId, java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        top.niunaijun.blackbox.entity.location.BCell _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getCell, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, top.niunaijun.blackbox.entity.location.BCell.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<top.niunaijun.blackbox.entity.location.BCell> getAllCell(int userId, java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<top.niunaijun.blackbox.entity.location.BCell> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAllCell, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(top.niunaijun.blackbox.entity.location.BCell.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setLocation(int userId, java.lang.String pkg, top.niunaijun.blackbox.entity.location.BLocation location) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(pkg);
          _Parcel.writeTypedObject(_data, location, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setLocation, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public top.niunaijun.blackbox.entity.location.BLocation getLocation(int userId, java.lang.String pkg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        top.niunaijun.blackbox.entity.location.BLocation _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          _data.writeString(pkg);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getLocation, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, top.niunaijun.blackbox.entity.location.BLocation.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setGlobalLocation(top.niunaijun.blackbox.entity.location.BLocation location) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, location, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setGlobalLocation, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public top.niunaijun.blackbox.entity.location.BLocation getGlobalLocation() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        top.niunaijun.blackbox.entity.location.BLocation _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getGlobalLocation, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, top.niunaijun.blackbox.entity.location.BLocation.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void requestLocationUpdates(android.os.IBinder listener, java.lang.String packageName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder(listener);
          _data.writeString(packageName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_requestLocationUpdates, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void removeUpdates(android.os.IBinder listener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder(listener);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeUpdates, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_getPattern = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_setPattern = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_setCell = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_setAllCell = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_setNeighboringCell = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_getNeighboringCell = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_setGlobalCell = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_setGlobalAllCell = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_setGlobalNeighboringCell = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_getGlobalNeighboringCell = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_getCell = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_getAllCell = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_setLocation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_getLocation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_setGlobalLocation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_getGlobalLocation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_requestLocationUpdates = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_removeUpdates = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "top.niunaijun.blackbox.core.system.location.IBLocationManagerService";
  public int getPattern(int userId, java.lang.String pkg) throws android.os.RemoteException;
  public void setPattern(int userId, java.lang.String pkg, int mode) throws android.os.RemoteException;
  public void setCell(int userId, java.lang.String pkg, top.niunaijun.blackbox.entity.location.BCell cell) throws android.os.RemoteException;
  public void setAllCell(int userId, java.lang.String pkg, java.util.List<top.niunaijun.blackbox.entity.location.BCell> cell) throws android.os.RemoteException;
  public void setNeighboringCell(int userId, java.lang.String pkg, java.util.List<top.niunaijun.blackbox.entity.location.BCell> cells) throws android.os.RemoteException;
  public java.util.List<top.niunaijun.blackbox.entity.location.BCell> getNeighboringCell(int userId, java.lang.String pkg) throws android.os.RemoteException;
  public void setGlobalCell(top.niunaijun.blackbox.entity.location.BCell cell) throws android.os.RemoteException;
  public void setGlobalAllCell(java.util.List<top.niunaijun.blackbox.entity.location.BCell> cell) throws android.os.RemoteException;
  public void setGlobalNeighboringCell(java.util.List<top.niunaijun.blackbox.entity.location.BCell> cell) throws android.os.RemoteException;
  public java.util.List<top.niunaijun.blackbox.entity.location.BCell> getGlobalNeighboringCell() throws android.os.RemoteException;
  public top.niunaijun.blackbox.entity.location.BCell getCell(int userId, java.lang.String pkg) throws android.os.RemoteException;
  public java.util.List<top.niunaijun.blackbox.entity.location.BCell> getAllCell(int userId, java.lang.String pkg) throws android.os.RemoteException;
  public void setLocation(int userId, java.lang.String pkg, top.niunaijun.blackbox.entity.location.BLocation location) throws android.os.RemoteException;
  public top.niunaijun.blackbox.entity.location.BLocation getLocation(int userId, java.lang.String pkg) throws android.os.RemoteException;
  public void setGlobalLocation(top.niunaijun.blackbox.entity.location.BLocation location) throws android.os.RemoteException;
  public top.niunaijun.blackbox.entity.location.BLocation getGlobalLocation() throws android.os.RemoteException;
  public void requestLocationUpdates(android.os.IBinder listener, java.lang.String packageName, int userId) throws android.os.RemoteException;
  public void removeUpdates(android.os.IBinder listener) throws android.os.RemoteException;
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
    static private <T extends android.os.Parcelable> void writeTypedList(
        android.os.Parcel parcel, java.util.List<T> value, int parcelableFlags) {
      if (value == null) {
        parcel.writeInt(-1);
      } else {
        int N = value.size();
        int i = 0;
        parcel.writeInt(N);
        while (i < N) {
    writeTypedObject(parcel, value.get(i), parcelableFlags);
          i++;
        }
      }
    }
  }
}
