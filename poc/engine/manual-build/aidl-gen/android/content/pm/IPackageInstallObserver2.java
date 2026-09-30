/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl/android/content/pm/IPackageInstallObserver2.aidl
 */
package android.content.pm;
/**
 * API for installation callbacks from the Package Manager.  In certain result cases
 * additional information will be provided.
 */
public interface IPackageInstallObserver2 extends android.os.IInterface
{
  /** Default implementation for IPackageInstallObserver2. */
  public static class Default implements android.content.pm.IPackageInstallObserver2
  {
    @Override public void onUserActionRequired(android.content.Intent intent) throws android.os.RemoteException
    {
    }
    /**
     * The install operation has completed.  {@code returnCode} holds a numeric code
     * indicating success or failure.  In certain cases the {@code extras} Bundle will
     * contain additional details:
     * 
     * <p><table>
     * <tr>
     *   <td>INSTALL_FAILED_DUPLICATE_PERMISSION</td>
     *   <td>Two strings are provided in the extras bundle: EXTRA_EXISTING_PERMISSION
     *       is the name of the permission that the app is attempting to define, and
     *       EXTRA_EXISTING_PACKAGE is the package name of the app which has already
     *       defined the permission.</td>
     * </tr>
     * </table>
     */
    @Override public void onPackageInstalled(java.lang.String basePackageName, int returnCode, java.lang.String msg, android.os.Bundle extras) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.content.pm.IPackageInstallObserver2
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.content.pm.IPackageInstallObserver2 interface,
     * generating a proxy if needed.
     */
    public static android.content.pm.IPackageInstallObserver2 asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.content.pm.IPackageInstallObserver2))) {
        return ((android.content.pm.IPackageInstallObserver2)iin);
      }
      return new android.content.pm.IPackageInstallObserver2.Stub.Proxy(obj);
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
        case TRANSACTION_onUserActionRequired:
        {
          android.content.Intent _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.content.Intent.CREATOR);
          this.onUserActionRequired(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_onPackageInstalled:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _arg2;
          _arg2 = data.readString();
          android.os.Bundle _arg3;
          _arg3 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          this.onPackageInstalled(_arg0, _arg1, _arg2, _arg3);
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
    private static class Proxy implements android.content.pm.IPackageInstallObserver2
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
      @Override public void onUserActionRequired(android.content.Intent intent) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, intent, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onUserActionRequired, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
       * The install operation has completed.  {@code returnCode} holds a numeric code
       * indicating success or failure.  In certain cases the {@code extras} Bundle will
       * contain additional details:
       * 
       * <p><table>
       * <tr>
       *   <td>INSTALL_FAILED_DUPLICATE_PERMISSION</td>
       *   <td>Two strings are provided in the extras bundle: EXTRA_EXISTING_PERMISSION
       *       is the name of the permission that the app is attempting to define, and
       *       EXTRA_EXISTING_PACKAGE is the package name of the app which has already
       *       defined the permission.</td>
       * </tr>
       * </table>
       */
      @Override public void onPackageInstalled(java.lang.String basePackageName, int returnCode, java.lang.String msg, android.os.Bundle extras) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(basePackageName);
          _data.writeInt(returnCode);
          _data.writeString(msg);
          _Parcel.writeTypedObject(_data, extras, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_onPackageInstalled, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "android.content.pm.IPackageInstallObserver2";
    static final int TRANSACTION_onUserActionRequired = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_onPackageInstalled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  public void onUserActionRequired(android.content.Intent intent) throws android.os.RemoteException;
  /**
   * The install operation has completed.  {@code returnCode} holds a numeric code
   * indicating success or failure.  In certain cases the {@code extras} Bundle will
   * contain additional details:
   * 
   * <p><table>
   * <tr>
   *   <td>INSTALL_FAILED_DUPLICATE_PERMISSION</td>
   *   <td>Two strings are provided in the extras bundle: EXTRA_EXISTING_PERMISSION
   *       is the name of the permission that the app is attempting to define, and
   *       EXTRA_EXISTING_PACKAGE is the package name of the app which has already
   *       defined the permission.</td>
   * </tr>
   * </table>
   */
  public void onPackageInstalled(java.lang.String basePackageName, int returnCode, java.lang.String msg, android.os.Bundle extras) throws android.os.RemoteException;
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
