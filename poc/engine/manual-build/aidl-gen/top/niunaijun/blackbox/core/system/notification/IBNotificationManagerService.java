/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl/top/niunaijun/blackbox/core/system/notification/IBNotificationManagerService.aidl
 */
package top.niunaijun.blackbox.core.system.notification;
public interface IBNotificationManagerService extends android.os.IInterface
{
  /** Default implementation for IBNotificationManagerService. */
  public static class Default implements top.niunaijun.blackbox.core.system.notification.IBNotificationManagerService
  {
    @Override public android.app.NotificationChannel getNotificationChannel(java.lang.String channelId, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.app.NotificationChannel> getNotificationChannels(java.lang.String packageName, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.app.NotificationChannelGroup> getNotificationChannelGroups(java.lang.String packageName, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void createNotificationChannel(android.app.NotificationChannel notificationChannel, int userId) throws android.os.RemoteException
    {
    }
    @Override public void deleteNotificationChannel(java.lang.String channelId, int userId) throws android.os.RemoteException
    {
    }
    @Override public void createNotificationChannelGroup(android.app.NotificationChannelGroup notificationChannelGroup, int userId) throws android.os.RemoteException
    {
    }
    @Override public void deleteNotificationChannelGroup(java.lang.String groupId, int userId) throws android.os.RemoteException
    {
    }
    @Override public void enqueueNotificationWithTag(int id, java.lang.String tag, android.app.Notification notification, int userId) throws android.os.RemoteException
    {
    }
    @Override public void cancelNotificationWithTag(int id, java.lang.String tag, int userId) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements top.niunaijun.blackbox.core.system.notification.IBNotificationManagerService
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an top.niunaijun.blackbox.core.system.notification.IBNotificationManagerService interface,
     * generating a proxy if needed.
     */
    public static top.niunaijun.blackbox.core.system.notification.IBNotificationManagerService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof top.niunaijun.blackbox.core.system.notification.IBNotificationManagerService))) {
        return ((top.niunaijun.blackbox.core.system.notification.IBNotificationManagerService)iin);
      }
      return new top.niunaijun.blackbox.core.system.notification.IBNotificationManagerService.Stub.Proxy(obj);
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
        case TRANSACTION_getNotificationChannel:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          android.app.NotificationChannel _result = this.getNotificationChannel(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getNotificationChannels:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          java.util.List<android.app.NotificationChannel> _result = this.getNotificationChannels(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getNotificationChannelGroups:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          java.util.List<android.app.NotificationChannelGroup> _result = this.getNotificationChannelGroups(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_createNotificationChannel:
        {
          android.app.NotificationChannel _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.app.NotificationChannel.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          this.createNotificationChannel(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_deleteNotificationChannel:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          this.deleteNotificationChannel(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_createNotificationChannelGroup:
        {
          android.app.NotificationChannelGroup _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.app.NotificationChannelGroup.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          this.createNotificationChannelGroup(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_deleteNotificationChannelGroup:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          this.deleteNotificationChannelGroup(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_enqueueNotificationWithTag:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          android.app.Notification _arg2;
          _arg2 = _Parcel.readTypedObject(data, android.app.Notification.CREATOR);
          int _arg3;
          _arg3 = data.readInt();
          this.enqueueNotificationWithTag(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_cancelNotificationWithTag:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          this.cancelNotificationWithTag(_arg0, _arg1, _arg2);
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
    private static class Proxy implements top.niunaijun.blackbox.core.system.notification.IBNotificationManagerService
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
      @Override public android.app.NotificationChannel getNotificationChannel(java.lang.String channelId, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.app.NotificationChannel _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(channelId);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getNotificationChannel, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.app.NotificationChannel.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.app.NotificationChannel> getNotificationChannels(java.lang.String packageName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.app.NotificationChannel> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getNotificationChannels, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(android.app.NotificationChannel.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.app.NotificationChannelGroup> getNotificationChannelGroups(java.lang.String packageName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.app.NotificationChannelGroup> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getNotificationChannelGroups, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(android.app.NotificationChannelGroup.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void createNotificationChannel(android.app.NotificationChannel notificationChannel, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, notificationChannel, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_createNotificationChannel, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void deleteNotificationChannel(java.lang.String channelId, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(channelId);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteNotificationChannel, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void createNotificationChannelGroup(android.app.NotificationChannelGroup notificationChannelGroup, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, notificationChannelGroup, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_createNotificationChannelGroup, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void deleteNotificationChannelGroup(java.lang.String groupId, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(groupId);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteNotificationChannelGroup, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void enqueueNotificationWithTag(int id, java.lang.String tag, android.app.Notification notification, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(id);
          _data.writeString(tag);
          _Parcel.writeTypedObject(_data, notification, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_enqueueNotificationWithTag, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void cancelNotificationWithTag(int id, java.lang.String tag, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(id);
          _data.writeString(tag);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_cancelNotificationWithTag, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_getNotificationChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getNotificationChannels = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_getNotificationChannelGroups = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_createNotificationChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_deleteNotificationChannel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_createNotificationChannelGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_deleteNotificationChannelGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_enqueueNotificationWithTag = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_cancelNotificationWithTag = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "top.niunaijun.blackbox.core.system.notification.IBNotificationManagerService";
  public android.app.NotificationChannel getNotificationChannel(java.lang.String channelId, int userId) throws android.os.RemoteException;
  public java.util.List<android.app.NotificationChannel> getNotificationChannels(java.lang.String packageName, int userId) throws android.os.RemoteException;
  public java.util.List<android.app.NotificationChannelGroup> getNotificationChannelGroups(java.lang.String packageName, int userId) throws android.os.RemoteException;
  public void createNotificationChannel(android.app.NotificationChannel notificationChannel, int userId) throws android.os.RemoteException;
  public void deleteNotificationChannel(java.lang.String channelId, int userId) throws android.os.RemoteException;
  public void createNotificationChannelGroup(android.app.NotificationChannelGroup notificationChannelGroup, int userId) throws android.os.RemoteException;
  public void deleteNotificationChannelGroup(java.lang.String groupId, int userId) throws android.os.RemoteException;
  public void enqueueNotificationWithTag(int id, java.lang.String tag, android.app.Notification notification, int userId) throws android.os.RemoteException;
  public void cancelNotificationWithTag(int id, java.lang.String tag, int userId) throws android.os.RemoteException;
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
