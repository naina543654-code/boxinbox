/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl/top/niunaijun/blackbox/core/system/pm/IBPackageManagerService.aidl
 */
package top.niunaijun.blackbox.core.system.pm;
public interface IBPackageManagerService extends android.os.IInterface
{
  /** Default implementation for IBPackageManagerService. */
  public static class Default implements top.niunaijun.blackbox.core.system.pm.IBPackageManagerService
  {
    @Override public android.content.pm.ResolveInfo resolveService(android.content.Intent intent, int flags, java.lang.String resolvedType, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.content.pm.ResolveInfo resolveActivity(android.content.Intent intent, int flags, java.lang.String resolvedType, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.content.pm.ProviderInfo resolveContentProvider(java.lang.String authority, int flag, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.content.pm.ResolveInfo resolveIntent(android.content.Intent intent, java.lang.String resolvedType, int flags, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName component, int flags, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.content.pm.ActivityInfo getReceiverInfo(android.content.ComponentName componentName, int flags, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName component, int flags, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.content.pm.ProviderInfo getProviderInfo(android.content.ComponentName component, int flags, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.content.pm.ApplicationInfo> getInstalledApplications(int flags, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.content.pm.PackageInfo> getInstalledPackages(int flags, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent intent, int flags, java.lang.String resolvedType, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.content.pm.ResolveInfo> queryBroadcastReceivers(android.content.Intent intent, int flags, java.lang.String resolvedType, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.content.pm.ResolveInfo> queryIntentServices(android.content.Intent intent, int flags, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.util.List<android.content.pm.ProviderInfo> queryContentProviders(java.lang.String processName, int uid, int flags, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public top.niunaijun.blackbox.entity.pm.InstallResult installPackageAsUser(java.lang.String file, top.niunaijun.blackbox.entity.pm.InstallOption option, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void uninstallPackageAsUser(java.lang.String packageName, int userId) throws android.os.RemoteException
    {
    }
    @Override public void uninstallPackage(java.lang.String packageName) throws android.os.RemoteException
    {
    }
    @Override public void clearPackage(java.lang.String packageName, int userId) throws android.os.RemoteException
    {
    }
    @Override public void stopPackage(java.lang.String packageName, int userId) throws android.os.RemoteException
    {
    }
    @Override public void deleteUser(int userId) throws android.os.RemoteException
    {
    }
    @Override public boolean isInstalled(java.lang.String packageName, int userId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public java.util.List<top.niunaijun.blackbox.entity.pm.InstalledPackage> getInstalledPackagesAsUser(int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String[] getPackagesForUid(int uid, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements top.niunaijun.blackbox.core.system.pm.IBPackageManagerService
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an top.niunaijun.blackbox.core.system.pm.IBPackageManagerService interface,
     * generating a proxy if needed.
     */
    public static top.niunaijun.blackbox.core.system.pm.IBPackageManagerService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof top.niunaijun.blackbox.core.system.pm.IBPackageManagerService))) {
        return ((top.niunaijun.blackbox.core.system.pm.IBPackageManagerService)iin);
      }
      return new top.niunaijun.blackbox.core.system.pm.IBPackageManagerService.Stub.Proxy(obj);
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
        case TRANSACTION_resolveService:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _arg3;
          _arg3 = data.readInt();
          android.content.pm.ResolveInfo _result = this.resolveService(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_resolveActivity:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _arg3;
          _arg3 = data.readInt();
          android.content.pm.ResolveInfo _result = this.resolveActivity(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_resolveContentProvider:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          android.content.pm.ProviderInfo _result = this.resolveContentProvider(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_resolveIntent:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          int _arg3;
          _arg3 = data.readInt();
          android.content.pm.ResolveInfo _result = this.resolveIntent(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getApplicationInfo:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          android.content.pm.ApplicationInfo _result = this.getApplicationInfo(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getPackageInfo:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          android.content.pm.PackageInfo _result = this.getPackageInfo(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getServiceInfo:
        {
          android.content.ComponentName _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.ComponentName.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          android.content.pm.ServiceInfo _result = this.getServiceInfo(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getReceiverInfo:
        {
          android.content.ComponentName _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.ComponentName.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          android.content.pm.ActivityInfo _result = this.getReceiverInfo(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getActivityInfo:
        {
          android.content.ComponentName _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.ComponentName.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          android.content.pm.ActivityInfo _result = this.getActivityInfo(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getProviderInfo:
        {
          android.content.ComponentName _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.ComponentName.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          android.content.pm.ProviderInfo _result = this.getProviderInfo(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getInstalledApplications:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.util.List<android.content.pm.ApplicationInfo> _result = this.getInstalledApplications(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getInstalledPackages:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.util.List<android.content.pm.PackageInfo> _result = this.getInstalledPackages(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_queryIntentActivities:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _arg3;
          _arg3 = data.readInt();
          java.util.List<android.content.pm.ResolveInfo> _result = this.queryIntentActivities(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_queryBroadcastReceivers:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _arg3;
          _arg3 = data.readInt();
          java.util.List<android.content.pm.ResolveInfo> _result = this.queryBroadcastReceivers(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_queryIntentServices:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          java.util.List<android.content.pm.ResolveInfo> _result = this.queryIntentServices(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_queryContentProviders:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          int _arg3;
          _arg3 = data.readInt();
          java.util.List<android.content.pm.ProviderInfo> _result = this.queryContentProviders(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_installPackageAsUser:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          top.niunaijun.blackbox.entity.pm.InstallOption _arg1;
          _arg1 = _Parcel.readTypedObject(data, top.niunaijun.blackbox.entity.pm.InstallOption.CREATOR);
          int _arg2;
          _arg2 = data.readInt();
          top.niunaijun.blackbox.entity.pm.InstallResult _result = this.installPackageAsUser(_arg0, _arg1, _arg2);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_uninstallPackageAsUser:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          this.uninstallPackageAsUser(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_uninstallPackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.uninstallPackage(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_clearPackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          this.clearPackage(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_stopPackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          this.stopPackage(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_deleteUser:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.deleteUser(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_isInstalled:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          boolean _result = this.isInstalled(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getInstalledPackagesAsUser:
        {
          int _arg0;
          _arg0 = data.readInt();
          java.util.List<top.niunaijun.blackbox.entity.pm.InstalledPackage> _result = this.getInstalledPackagesAsUser(_arg0);
          reply.writeNoException();
          _Parcel.writeTypedList(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getPackagesForUid:
        {
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String[] _result = this.getPackagesForUid(_arg0, _arg1);
          reply.writeNoException();
          reply.writeStringArray(_result);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements top.niunaijun.blackbox.core.system.pm.IBPackageManagerService
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
      @Override public android.content.pm.ResolveInfo resolveService(android.content.Intent intent, int flags, java.lang.String resolvedType, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.pm.ResolveInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          _data.writeInt(flags);
          _data.writeString(resolvedType);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_resolveService, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.pm.ResolveInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.content.pm.ResolveInfo resolveActivity(android.content.Intent intent, int flags, java.lang.String resolvedType, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.pm.ResolveInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          _data.writeInt(flags);
          _data.writeString(resolvedType);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_resolveActivity, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.pm.ResolveInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.content.pm.ProviderInfo resolveContentProvider(java.lang.String authority, int flag, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.pm.ProviderInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(authority);
          _data.writeInt(flag);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_resolveContentProvider, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.pm.ProviderInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.content.pm.ResolveInfo resolveIntent(android.content.Intent intent, java.lang.String resolvedType, int flags, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.pm.ResolveInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          _data.writeString(resolvedType);
          _data.writeInt(flags);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_resolveIntent, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.pm.ResolveInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.pm.ApplicationInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(flags);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getApplicationInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.pm.ApplicationInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.pm.PackageInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(flags);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackageInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.pm.PackageInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName component, int flags, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.pm.ServiceInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, component, 0);
          _data.writeInt(flags);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getServiceInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.pm.ServiceInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.content.pm.ActivityInfo getReceiverInfo(android.content.ComponentName componentName, int flags, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.pm.ActivityInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, componentName, 0);
          _data.writeInt(flags);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getReceiverInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.pm.ActivityInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName component, int flags, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.pm.ActivityInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, component, 0);
          _data.writeInt(flags);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getActivityInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.pm.ActivityInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.content.pm.ProviderInfo getProviderInfo(android.content.ComponentName component, int flags, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.pm.ProviderInfo _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, component, 0);
          _data.writeInt(flags);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getProviderInfo, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.pm.ProviderInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.content.pm.ApplicationInfo> getInstalledApplications(int flags, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.content.pm.ApplicationInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(flags);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getInstalledApplications, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(android.content.pm.ApplicationInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.content.pm.PackageInfo> getInstalledPackages(int flags, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.content.pm.PackageInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(flags);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getInstalledPackages, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(android.content.pm.PackageInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent intent, int flags, java.lang.String resolvedType, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.content.pm.ResolveInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          _data.writeInt(flags);
          _data.writeString(resolvedType);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_queryIntentActivities, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(android.content.pm.ResolveInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.content.pm.ResolveInfo> queryBroadcastReceivers(android.content.Intent intent, int flags, java.lang.String resolvedType, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.content.pm.ResolveInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          _data.writeInt(flags);
          _data.writeString(resolvedType);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_queryBroadcastReceivers, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(android.content.pm.ResolveInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.content.pm.ResolveInfo> queryIntentServices(android.content.Intent intent, int flags, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.content.pm.ResolveInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          _data.writeInt(flags);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_queryIntentServices, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(android.content.pm.ResolveInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<android.content.pm.ProviderInfo> queryContentProviders(java.lang.String processName, int uid, int flags, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<android.content.pm.ProviderInfo> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(processName);
          _data.writeInt(uid);
          _data.writeInt(flags);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_queryContentProviders, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(android.content.pm.ProviderInfo.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public top.niunaijun.blackbox.entity.pm.InstallResult installPackageAsUser(java.lang.String file, top.niunaijun.blackbox.entity.pm.InstallOption option, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        top.niunaijun.blackbox.entity.pm.InstallResult _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(file);
          _Parcel.writeTypedObject(_data, option, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_installPackageAsUser, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, top.niunaijun.blackbox.entity.pm.InstallResult.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void uninstallPackageAsUser(java.lang.String packageName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_uninstallPackageAsUser, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void uninstallPackage(java.lang.String packageName) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          boolean _status = mRemote.transact(Stub.TRANSACTION_uninstallPackage, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void clearPackage(java.lang.String packageName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_clearPackage, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void stopPackage(java.lang.String packageName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_stopPackage, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void deleteUser(int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_deleteUser, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean isInstalled(java.lang.String packageName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isInstalled, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.util.List<top.niunaijun.blackbox.entity.pm.InstalledPackage> getInstalledPackagesAsUser(int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.List<top.niunaijun.blackbox.entity.pm.InstalledPackage> _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getInstalledPackagesAsUser, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArrayList(top.niunaijun.blackbox.entity.pm.InstalledPackage.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String[] getPackagesForUid(int uid, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(uid);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackagesForUid, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    static final int TRANSACTION_resolveService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_resolveActivity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_resolveContentProvider = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_resolveIntent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_getApplicationInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_getPackageInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_getServiceInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_getReceiverInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_getActivityInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_getProviderInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_getInstalledApplications = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_getInstalledPackages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_queryIntentActivities = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_queryBroadcastReceivers = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_queryIntentServices = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_queryContentProviders = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_installPackageAsUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_uninstallPackageAsUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_uninstallPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_clearPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_stopPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    static final int TRANSACTION_deleteUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
    static final int TRANSACTION_isInstalled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
    static final int TRANSACTION_getInstalledPackagesAsUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
    static final int TRANSACTION_getPackagesForUid = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "top.niunaijun.blackbox.core.system.pm.IBPackageManagerService";
  public android.content.pm.ResolveInfo resolveService(android.content.Intent intent, int flags, java.lang.String resolvedType, int userId) throws android.os.RemoteException;
  public android.content.pm.ResolveInfo resolveActivity(android.content.Intent intent, int flags, java.lang.String resolvedType, int userId) throws android.os.RemoteException;
  public android.content.pm.ProviderInfo resolveContentProvider(java.lang.String authority, int flag, int userId) throws android.os.RemoteException;
  public android.content.pm.ResolveInfo resolveIntent(android.content.Intent intent, java.lang.String resolvedType, int flags, int userId) throws android.os.RemoteException;
  public android.content.pm.ApplicationInfo getApplicationInfo(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException;
  public android.content.pm.PackageInfo getPackageInfo(java.lang.String packageName, int flags, int userId) throws android.os.RemoteException;
  public android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName component, int flags, int userId) throws android.os.RemoteException;
  public android.content.pm.ActivityInfo getReceiverInfo(android.content.ComponentName componentName, int flags, int userId) throws android.os.RemoteException;
  public android.content.pm.ActivityInfo getActivityInfo(android.content.ComponentName component, int flags, int userId) throws android.os.RemoteException;
  public android.content.pm.ProviderInfo getProviderInfo(android.content.ComponentName component, int flags, int userId) throws android.os.RemoteException;
  public java.util.List<android.content.pm.ApplicationInfo> getInstalledApplications(int flags, int userId) throws android.os.RemoteException;
  public java.util.List<android.content.pm.PackageInfo> getInstalledPackages(int flags, int userId) throws android.os.RemoteException;
  public java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent intent, int flags, java.lang.String resolvedType, int userId) throws android.os.RemoteException;
  public java.util.List<android.content.pm.ResolveInfo> queryBroadcastReceivers(android.content.Intent intent, int flags, java.lang.String resolvedType, int userId) throws android.os.RemoteException;
  public java.util.List<android.content.pm.ResolveInfo> queryIntentServices(android.content.Intent intent, int flags, int userId) throws android.os.RemoteException;
  public java.util.List<android.content.pm.ProviderInfo> queryContentProviders(java.lang.String processName, int uid, int flags, int userId) throws android.os.RemoteException;
  public top.niunaijun.blackbox.entity.pm.InstallResult installPackageAsUser(java.lang.String file, top.niunaijun.blackbox.entity.pm.InstallOption option, int userId) throws android.os.RemoteException;
  public void uninstallPackageAsUser(java.lang.String packageName, int userId) throws android.os.RemoteException;
  public void uninstallPackage(java.lang.String packageName) throws android.os.RemoteException;
  public void clearPackage(java.lang.String packageName, int userId) throws android.os.RemoteException;
  public void stopPackage(java.lang.String packageName, int userId) throws android.os.RemoteException;
  public void deleteUser(int userId) throws android.os.RemoteException;
  public boolean isInstalled(java.lang.String packageName, int userId) throws android.os.RemoteException;
  public java.util.List<top.niunaijun.blackbox.entity.pm.InstalledPackage> getInstalledPackagesAsUser(int userId) throws android.os.RemoteException;
  public java.lang.String[] getPackagesForUid(int uid, int userId) throws android.os.RemoteException;
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
