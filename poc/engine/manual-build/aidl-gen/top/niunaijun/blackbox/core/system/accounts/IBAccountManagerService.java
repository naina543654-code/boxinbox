/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl/top/niunaijun/blackbox/core/system/accounts/IBAccountManagerService.aidl
 */
package top.niunaijun.blackbox.core.system.accounts;
public interface IBAccountManagerService extends android.os.IInterface
{
  /** Default implementation for IBAccountManagerService. */
  public static class Default implements top.niunaijun.blackbox.core.system.accounts.IBAccountManagerService
  {
    @Override public java.lang.String getPassword(android.accounts.Account account, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public java.lang.String getUserData(android.accounts.Account account, java.lang.String key, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.accounts.AuthenticatorDescription[] getAuthenticatorTypes(int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.accounts.Account[] getAccountsForPackage(java.lang.String packageName, int uid, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.accounts.Account[] getAccountsByTypeForPackage(java.lang.String type, java.lang.String packageName, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public android.accounts.Account[] getAccountsAsUser(java.lang.String accountType, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void getAccountByTypeAndFeatures(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String[] features, int userId) throws android.os.RemoteException
    {
    }
    @Override public void getAccountsByFeatures(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String[] features, int userId) throws android.os.RemoteException
    {
    }
    @Override public boolean addAccountExplicitly(android.accounts.Account account, java.lang.String password, android.os.Bundle extras, int userId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void removeAccountAsUser(android.accounts.IAccountManagerResponse response, android.accounts.Account account, boolean expectActivityLaunch, int userId) throws android.os.RemoteException
    {
    }
    @Override public boolean removeAccountExplicitly(android.accounts.Account account, int userId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void copyAccountToUser(android.accounts.IAccountManagerResponse response, android.accounts.Account account, int userFrom, int userTo) throws android.os.RemoteException
    {
    }
    @Override public void invalidateAuthToken(java.lang.String accountType, java.lang.String authToken, int userId) throws android.os.RemoteException
    {
    }
    @Override public java.lang.String peekAuthToken(android.accounts.Account account, java.lang.String authTokenType, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void setAuthToken(android.accounts.Account account, java.lang.String authTokenType, java.lang.String authToken, int userId) throws android.os.RemoteException
    {
    }
    @Override public void setPassword(android.accounts.Account account, java.lang.String password, int userId) throws android.os.RemoteException
    {
    }
    @Override public void clearPassword(android.accounts.Account account, int userId) throws android.os.RemoteException
    {
    }
    @Override public void setUserData(android.accounts.Account account, java.lang.String key, java.lang.String value, int userId) throws android.os.RemoteException
    {
    }
    @Override public void updateAppPermission(android.accounts.Account account, java.lang.String authTokenType, int uid, boolean value) throws android.os.RemoteException
    {
    }
    @Override public void getAuthToken(android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, android.os.Bundle options, int userId) throws android.os.RemoteException
    {
    }
    @Override public void addAccount(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, boolean expectActivityLaunch, android.os.Bundle options, int userId) throws android.os.RemoteException
    {
    }
    @Override public void addAccountAsUser(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, boolean expectActivityLaunch, android.os.Bundle options, int userId) throws android.os.RemoteException
    {
    }
    @Override public void updateCredentials(android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String authTokenType, boolean expectActivityLaunch, android.os.Bundle options, int userId) throws android.os.RemoteException
    {
    }
    @Override public void editProperties(android.accounts.IAccountManagerResponse response, java.lang.String accountType, boolean expectActivityLaunch, int userId) throws android.os.RemoteException
    {
    }
    @Override public void confirmCredentialsAsUser(android.accounts.IAccountManagerResponse response, android.accounts.Account account, android.os.Bundle options, boolean expectActivityLaunch, int userId) throws android.os.RemoteException
    {
    }
    @Override public boolean accountAuthenticated(android.accounts.Account account, int userId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public void getAuthTokenLabel(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, int userId) throws android.os.RemoteException
    {
    }
    /** Returns Map<String, Integer> from package name to visibility with all values stored for given account */
    @Override public java.util.Map getPackagesAndVisibilityForAccount(android.accounts.Account account, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean addAccountExplicitlyWithVisibility(android.accounts.Account account, java.lang.String password, android.os.Bundle extras, java.util.Map visibility, int userId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public boolean setAccountVisibility(android.accounts.Account a, java.lang.String packageName, int newVisibility, int userId) throws android.os.RemoteException
    {
      return false;
    }
    @Override public int getAccountVisibility(android.accounts.Account a, java.lang.String packageName, int userId) throws android.os.RemoteException
    {
      return 0;
    }
    /** Type may be null returns Map <Account, Integer> */
    @Override public java.util.Map getAccountsAndVisibilityForPackage(java.lang.String packageName, java.lang.String accountType, int userId) throws android.os.RemoteException
    {
      return null;
    }
    @Override public void registerAccountListener(java.lang.String[] accountTypes, java.lang.String opPackageName, int userId) throws android.os.RemoteException
    {
    }
    @Override public void unregisterAccountListener(java.lang.String[] accountTypes, java.lang.String opPackageName, int userId) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements top.niunaijun.blackbox.core.system.accounts.IBAccountManagerService
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an top.niunaijun.blackbox.core.system.accounts.IBAccountManagerService interface,
     * generating a proxy if needed.
     */
    public static top.niunaijun.blackbox.core.system.accounts.IBAccountManagerService asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof top.niunaijun.blackbox.core.system.accounts.IBAccountManagerService))) {
        return ((top.niunaijun.blackbox.core.system.accounts.IBAccountManagerService)iin);
      }
      return new top.niunaijun.blackbox.core.system.accounts.IBAccountManagerService.Stub.Proxy(obj);
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
        case TRANSACTION_getPassword:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          java.lang.String _result = this.getPassword(_arg0, _arg1);
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_getUserData:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          java.lang.String _result = this.getUserData(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_getAuthenticatorTypes:
        {
          int _arg0;
          _arg0 = data.readInt();
          android.accounts.AuthenticatorDescription[] _result = this.getAuthenticatorTypes(_arg0);
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getAccountsForPackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          int _arg2;
          _arg2 = data.readInt();
          android.accounts.Account[] _result = this.getAccountsForPackage(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getAccountsByTypeForPackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          android.accounts.Account[] _result = this.getAccountsByTypeForPackage(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getAccountsAsUser:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          int _arg1;
          _arg1 = data.readInt();
          android.accounts.Account[] _result = this.getAccountsAsUser(_arg0, _arg1);
          reply.writeNoException();
          reply.writeTypedArray(_result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        case TRANSACTION_getAccountByTypeAndFeatures:
        {
          android.accounts.IAccountManagerResponse _arg0;
          _arg0 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String[] _arg2;
          _arg2 = data.createStringArray();
          int _arg3;
          _arg3 = data.readInt();
          this.getAccountByTypeAndFeatures(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAccountsByFeatures:
        {
          android.accounts.IAccountManagerResponse _arg0;
          _arg0 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String[] _arg2;
          _arg2 = data.createStringArray();
          int _arg3;
          _arg3 = data.readInt();
          this.getAccountsByFeatures(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addAccountExplicitly:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          android.os.Bundle _arg2;
          _arg2 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          int _arg3;
          _arg3 = data.readInt();
          boolean _result = this.addAccountExplicitly(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_removeAccountAsUser:
        {
          android.accounts.IAccountManagerResponse _arg0;
          _arg0 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
          android.accounts.Account _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          boolean _arg2;
          _arg2 = (0!=data.readInt());
          int _arg3;
          _arg3 = data.readInt();
          this.removeAccountAsUser(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_removeAccountExplicitly:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          boolean _result = this.removeAccountExplicitly(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_copyAccountToUser:
        {
          android.accounts.IAccountManagerResponse _arg0;
          _arg0 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
          android.accounts.Account _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          int _arg2;
          _arg2 = data.readInt();
          int _arg3;
          _arg3 = data.readInt();
          this.copyAccountToUser(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_invalidateAuthToken:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          this.invalidateAuthToken(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_peekAuthToken:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          java.lang.String _result = this.peekAuthToken(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeString(_result);
          break;
        }
        case TRANSACTION_setAuthToken:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _arg3;
          _arg3 = data.readInt();
          this.setAuthToken(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setPassword:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          this.setPassword(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_clearPassword:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          this.clearPassword(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_setUserData:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _arg3;
          _arg3 = data.readInt();
          this.setUserData(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_updateAppPermission:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          boolean _arg3;
          _arg3 = (0!=data.readInt());
          this.updateAppPermission(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAuthToken:
        {
          android.accounts.IAccountManagerResponse _arg0;
          _arg0 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
          android.accounts.Account _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg2;
          _arg2 = data.readString();
          boolean _arg3;
          _arg3 = (0!=data.readInt());
          boolean _arg4;
          _arg4 = (0!=data.readInt());
          android.os.Bundle _arg5;
          _arg5 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          int _arg6;
          _arg6 = data.readInt();
          this.getAuthToken(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addAccount:
        {
          android.accounts.IAccountManagerResponse _arg0;
          _arg0 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          java.lang.String[] _arg3;
          _arg3 = data.createStringArray();
          boolean _arg4;
          _arg4 = (0!=data.readInt());
          android.os.Bundle _arg5;
          _arg5 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          int _arg6;
          _arg6 = data.readInt();
          this.addAccount(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addAccountAsUser:
        {
          android.accounts.IAccountManagerResponse _arg0;
          _arg0 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          java.lang.String[] _arg3;
          _arg3 = data.createStringArray();
          boolean _arg4;
          _arg4 = (0!=data.readInt());
          android.os.Bundle _arg5;
          _arg5 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          int _arg6;
          _arg6 = data.readInt();
          this.addAccountAsUser(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_updateCredentials:
        {
          android.accounts.IAccountManagerResponse _arg0;
          _arg0 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
          android.accounts.Account _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg2;
          _arg2 = data.readString();
          boolean _arg3;
          _arg3 = (0!=data.readInt());
          android.os.Bundle _arg4;
          _arg4 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          int _arg5;
          _arg5 = data.readInt();
          this.updateCredentials(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_editProperties:
        {
          android.accounts.IAccountManagerResponse _arg0;
          _arg0 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
          java.lang.String _arg1;
          _arg1 = data.readString();
          boolean _arg2;
          _arg2 = (0!=data.readInt());
          int _arg3;
          _arg3 = data.readInt();
          this.editProperties(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_confirmCredentialsAsUser:
        {
          android.accounts.IAccountManagerResponse _arg0;
          _arg0 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
          android.accounts.Account _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          android.os.Bundle _arg2;
          _arg2 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          boolean _arg3;
          _arg3 = (0!=data.readInt());
          int _arg4;
          _arg4 = data.readInt();
          this.confirmCredentialsAsUser(_arg0, _arg1, _arg2, _arg3, _arg4);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_accountAuthenticated:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          boolean _result = this.accountAuthenticated(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getAuthTokenLabel:
        {
          android.accounts.IAccountManagerResponse _arg0;
          _arg0 = android.accounts.IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          int _arg3;
          _arg3 = data.readInt();
          this.getAuthTokenLabel(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getPackagesAndVisibilityForAccount:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          int _arg1;
          _arg1 = data.readInt();
          java.util.Map _result = this.getPackagesAndVisibilityForAccount(_arg0, _arg1);
          reply.writeNoException();
          reply.writeMap(_result);
          break;
        }
        case TRANSACTION_addAccountExplicitlyWithVisibility:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          android.os.Bundle _arg2;
          _arg2 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          java.util.Map _arg3;
          java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
          _arg3 = data.readHashMap(cl);
          int _arg4;
          _arg4 = data.readInt();
          boolean _result = this.addAccountExplicitlyWithVisibility(_arg0, _arg1, _arg2, _arg3, _arg4);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_setAccountVisibility:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          int _arg3;
          _arg3 = data.readInt();
          boolean _result = this.setAccountVisibility(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        case TRANSACTION_getAccountVisibility:
        {
          android.accounts.Account _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          int _result = this.getAccountVisibility(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeInt(_result);
          break;
        }
        case TRANSACTION_getAccountsAndVisibilityForPackage:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          java.util.Map _result = this.getAccountsAndVisibilityForPackage(_arg0, _arg1, _arg2);
          reply.writeNoException();
          reply.writeMap(_result);
          break;
        }
        case TRANSACTION_registerAccountListener:
        {
          java.lang.String[] _arg0;
          _arg0 = data.createStringArray();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          this.registerAccountListener(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_unregisterAccountListener:
        {
          java.lang.String[] _arg0;
          _arg0 = data.createStringArray();
          java.lang.String _arg1;
          _arg1 = data.readString();
          int _arg2;
          _arg2 = data.readInt();
          this.unregisterAccountListener(_arg0, _arg1, _arg2);
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
    private static class Proxy implements top.niunaijun.blackbox.core.system.accounts.IBAccountManagerService
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
      @Override public java.lang.String getPassword(android.accounts.Account account, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPassword, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public java.lang.String getUserData(android.accounts.Account account, java.lang.String key, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(key);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getUserData, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.accounts.AuthenticatorDescription[] getAuthenticatorTypes(int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.accounts.AuthenticatorDescription[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAuthenticatorTypes, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArray(android.accounts.AuthenticatorDescription.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.accounts.Account[] getAccountsForPackage(java.lang.String packageName, int uid, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.accounts.Account[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeInt(uid);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAccountsForPackage, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArray(android.accounts.Account.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.accounts.Account[] getAccountsByTypeForPackage(java.lang.String type, java.lang.String packageName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.accounts.Account[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(type);
          _data.writeString(packageName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAccountsByTypeForPackage, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArray(android.accounts.Account.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public android.accounts.Account[] getAccountsAsUser(java.lang.String accountType, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.accounts.Account[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(accountType);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAccountsAsUser, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createTypedArray(android.accounts.Account.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void getAccountByTypeAndFeatures(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String[] features, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _data.writeString(accountType);
          _data.writeStringArray(features);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAccountByTypeAndFeatures, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void getAccountsByFeatures(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String[] features, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _data.writeString(accountType);
          _data.writeStringArray(features);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAccountsByFeatures, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean addAccountExplicitly(android.accounts.Account account, java.lang.String password, android.os.Bundle extras, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(password);
          _Parcel.writeTypedObject(_data, extras, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addAccountExplicitly, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void removeAccountAsUser(android.accounts.IAccountManagerResponse response, android.accounts.Account account, boolean expectActivityLaunch, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeInt(((expectActivityLaunch)?(1):(0)));
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeAccountAsUser, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean removeAccountExplicitly(android.accounts.Account account, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_removeAccountExplicitly, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void copyAccountToUser(android.accounts.IAccountManagerResponse response, android.accounts.Account account, int userFrom, int userTo) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeInt(userFrom);
          _data.writeInt(userTo);
          boolean _status = mRemote.transact(Stub.TRANSACTION_copyAccountToUser, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void invalidateAuthToken(java.lang.String accountType, java.lang.String authToken, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(accountType);
          _data.writeString(authToken);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_invalidateAuthToken, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String peekAuthToken(android.accounts.Account account, java.lang.String authTokenType, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(authTokenType);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_peekAuthToken, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void setAuthToken(android.accounts.Account account, java.lang.String authTokenType, java.lang.String authToken, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(authTokenType);
          _data.writeString(authToken);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setAuthToken, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setPassword(android.accounts.Account account, java.lang.String password, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(password);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setPassword, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void clearPassword(android.accounts.Account account, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_clearPassword, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void setUserData(android.accounts.Account account, java.lang.String key, java.lang.String value, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(key);
          _data.writeString(value);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setUserData, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void updateAppPermission(android.accounts.Account account, java.lang.String authTokenType, int uid, boolean value) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(authTokenType);
          _data.writeInt(uid);
          _data.writeInt(((value)?(1):(0)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_updateAppPermission, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void getAuthToken(android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, android.os.Bundle options, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(authTokenType);
          _data.writeInt(((notifyOnAuthFailure)?(1):(0)));
          _data.writeInt(((expectActivityLaunch)?(1):(0)));
          _Parcel.writeTypedObject(_data, options, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAuthToken, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addAccount(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, boolean expectActivityLaunch, android.os.Bundle options, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _data.writeString(accountType);
          _data.writeString(authTokenType);
          _data.writeStringArray(requiredFeatures);
          _data.writeInt(((expectActivityLaunch)?(1):(0)));
          _Parcel.writeTypedObject(_data, options, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addAccount, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void addAccountAsUser(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, boolean expectActivityLaunch, android.os.Bundle options, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _data.writeString(accountType);
          _data.writeString(authTokenType);
          _data.writeStringArray(requiredFeatures);
          _data.writeInt(((expectActivityLaunch)?(1):(0)));
          _Parcel.writeTypedObject(_data, options, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addAccountAsUser, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void updateCredentials(android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String authTokenType, boolean expectActivityLaunch, android.os.Bundle options, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(authTokenType);
          _data.writeInt(((expectActivityLaunch)?(1):(0)));
          _Parcel.writeTypedObject(_data, options, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_updateCredentials, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void editProperties(android.accounts.IAccountManagerResponse response, java.lang.String accountType, boolean expectActivityLaunch, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _data.writeString(accountType);
          _data.writeInt(((expectActivityLaunch)?(1):(0)));
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_editProperties, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void confirmCredentialsAsUser(android.accounts.IAccountManagerResponse response, android.accounts.Account account, android.os.Bundle options, boolean expectActivityLaunch, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _Parcel.writeTypedObject(_data, account, 0);
          _Parcel.writeTypedObject(_data, options, 0);
          _data.writeInt(((expectActivityLaunch)?(1):(0)));
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_confirmCredentialsAsUser, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public boolean accountAuthenticated(android.accounts.Account account, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_accountAuthenticated, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void getAuthTokenLabel(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _data.writeString(accountType);
          _data.writeString(authTokenType);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAuthTokenLabel, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /** Returns Map<String, Integer> from package name to visibility with all values stored for given account */
      @Override public java.util.Map getPackagesAndVisibilityForAccount(android.accounts.Account account, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.Map _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getPackagesAndVisibilityForAccount, _data, _reply, 0);
          _reply.readException();
          java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
          _result = _reply.readHashMap(cl);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean addAccountExplicitlyWithVisibility(android.accounts.Account account, java.lang.String password, android.os.Bundle extras, java.util.Map visibility, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(password);
          _Parcel.writeTypedObject(_data, extras, 0);
          _data.writeMap(visibility);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addAccountExplicitlyWithVisibility, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean setAccountVisibility(android.accounts.Account a, java.lang.String packageName, int newVisibility, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, a, 0);
          _data.writeString(packageName);
          _data.writeInt(newVisibility);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setAccountVisibility, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public int getAccountVisibility(android.accounts.Account a, java.lang.String packageName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, a, 0);
          _data.writeString(packageName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAccountVisibility, _data, _reply, 0);
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /** Type may be null returns Map <Account, Integer> */
      @Override public java.util.Map getAccountsAndVisibilityForPackage(java.lang.String packageName, java.lang.String accountType, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.util.Map _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(packageName);
          _data.writeString(accountType);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAccountsAndVisibilityForPackage, _data, _reply, 0);
          _reply.readException();
          java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
          _result = _reply.readHashMap(cl);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void registerAccountListener(java.lang.String[] accountTypes, java.lang.String opPackageName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStringArray(accountTypes);
          _data.writeString(opPackageName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerAccountListener, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unregisterAccountListener(java.lang.String[] accountTypes, java.lang.String opPackageName, int userId) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStringArray(accountTypes);
          _data.writeString(opPackageName);
          _data.writeInt(userId);
          boolean _status = mRemote.transact(Stub.TRANSACTION_unregisterAccountListener, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_getPassword = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getUserData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_getAuthenticatorTypes = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_getAccountsForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_getAccountsByTypeForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_getAccountsAsUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_getAccountByTypeAndFeatures = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_getAccountsByFeatures = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_addAccountExplicitly = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_removeAccountAsUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
    static final int TRANSACTION_removeAccountExplicitly = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
    static final int TRANSACTION_copyAccountToUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
    static final int TRANSACTION_invalidateAuthToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
    static final int TRANSACTION_peekAuthToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
    static final int TRANSACTION_setAuthToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
    static final int TRANSACTION_setPassword = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
    static final int TRANSACTION_clearPassword = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
    static final int TRANSACTION_setUserData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
    static final int TRANSACTION_updateAppPermission = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
    static final int TRANSACTION_getAuthToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
    static final int TRANSACTION_addAccount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
    static final int TRANSACTION_addAccountAsUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
    static final int TRANSACTION_updateCredentials = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
    static final int TRANSACTION_editProperties = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
    static final int TRANSACTION_confirmCredentialsAsUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
    static final int TRANSACTION_accountAuthenticated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
    static final int TRANSACTION_getAuthTokenLabel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
    static final int TRANSACTION_getPackagesAndVisibilityForAccount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
    static final int TRANSACTION_addAccountExplicitlyWithVisibility = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
    static final int TRANSACTION_setAccountVisibility = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
    static final int TRANSACTION_getAccountVisibility = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
    static final int TRANSACTION_getAccountsAndVisibilityForPackage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
    static final int TRANSACTION_registerAccountListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
    static final int TRANSACTION_unregisterAccountListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
  }
  /** @hide */
  public static final java.lang.String DESCRIPTOR = "top.niunaijun.blackbox.core.system.accounts.IBAccountManagerService";
  public java.lang.String getPassword(android.accounts.Account account, int userId) throws android.os.RemoteException;
  public java.lang.String getUserData(android.accounts.Account account, java.lang.String key, int userId) throws android.os.RemoteException;
  public android.accounts.AuthenticatorDescription[] getAuthenticatorTypes(int userId) throws android.os.RemoteException;
  public android.accounts.Account[] getAccountsForPackage(java.lang.String packageName, int uid, int userId) throws android.os.RemoteException;
  public android.accounts.Account[] getAccountsByTypeForPackage(java.lang.String type, java.lang.String packageName, int userId) throws android.os.RemoteException;
  public android.accounts.Account[] getAccountsAsUser(java.lang.String accountType, int userId) throws android.os.RemoteException;
  public void getAccountByTypeAndFeatures(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String[] features, int userId) throws android.os.RemoteException;
  public void getAccountsByFeatures(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String[] features, int userId) throws android.os.RemoteException;
  public boolean addAccountExplicitly(android.accounts.Account account, java.lang.String password, android.os.Bundle extras, int userId) throws android.os.RemoteException;
  public void removeAccountAsUser(android.accounts.IAccountManagerResponse response, android.accounts.Account account, boolean expectActivityLaunch, int userId) throws android.os.RemoteException;
  public boolean removeAccountExplicitly(android.accounts.Account account, int userId) throws android.os.RemoteException;
  public void copyAccountToUser(android.accounts.IAccountManagerResponse response, android.accounts.Account account, int userFrom, int userTo) throws android.os.RemoteException;
  public void invalidateAuthToken(java.lang.String accountType, java.lang.String authToken, int userId) throws android.os.RemoteException;
  public java.lang.String peekAuthToken(android.accounts.Account account, java.lang.String authTokenType, int userId) throws android.os.RemoteException;
  public void setAuthToken(android.accounts.Account account, java.lang.String authTokenType, java.lang.String authToken, int userId) throws android.os.RemoteException;
  public void setPassword(android.accounts.Account account, java.lang.String password, int userId) throws android.os.RemoteException;
  public void clearPassword(android.accounts.Account account, int userId) throws android.os.RemoteException;
  public void setUserData(android.accounts.Account account, java.lang.String key, java.lang.String value, int userId) throws android.os.RemoteException;
  public void updateAppPermission(android.accounts.Account account, java.lang.String authTokenType, int uid, boolean value) throws android.os.RemoteException;
  public void getAuthToken(android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, android.os.Bundle options, int userId) throws android.os.RemoteException;
  public void addAccount(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, boolean expectActivityLaunch, android.os.Bundle options, int userId) throws android.os.RemoteException;
  public void addAccountAsUser(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, boolean expectActivityLaunch, android.os.Bundle options, int userId) throws android.os.RemoteException;
  public void updateCredentials(android.accounts.IAccountManagerResponse response, android.accounts.Account account, java.lang.String authTokenType, boolean expectActivityLaunch, android.os.Bundle options, int userId) throws android.os.RemoteException;
  public void editProperties(android.accounts.IAccountManagerResponse response, java.lang.String accountType, boolean expectActivityLaunch, int userId) throws android.os.RemoteException;
  public void confirmCredentialsAsUser(android.accounts.IAccountManagerResponse response, android.accounts.Account account, android.os.Bundle options, boolean expectActivityLaunch, int userId) throws android.os.RemoteException;
  public boolean accountAuthenticated(android.accounts.Account account, int userId) throws android.os.RemoteException;
  public void getAuthTokenLabel(android.accounts.IAccountManagerResponse response, java.lang.String accountType, java.lang.String authTokenType, int userId) throws android.os.RemoteException;
  /** Returns Map<String, Integer> from package name to visibility with all values stored for given account */
  public java.util.Map getPackagesAndVisibilityForAccount(android.accounts.Account account, int userId) throws android.os.RemoteException;
  public boolean addAccountExplicitlyWithVisibility(android.accounts.Account account, java.lang.String password, android.os.Bundle extras, java.util.Map visibility, int userId) throws android.os.RemoteException;
  public boolean setAccountVisibility(android.accounts.Account a, java.lang.String packageName, int newVisibility, int userId) throws android.os.RemoteException;
  public int getAccountVisibility(android.accounts.Account a, java.lang.String packageName, int userId) throws android.os.RemoteException;
  /** Type may be null returns Map <Account, Integer> */
  public java.util.Map getAccountsAndVisibilityForPackage(java.lang.String packageName, java.lang.String accountType, int userId) throws android.os.RemoteException;
  public void registerAccountListener(java.lang.String[] accountTypes, java.lang.String opPackageName, int userId) throws android.os.RemoteException;
  public void unregisterAccountListener(java.lang.String[] accountTypes, java.lang.String opPackageName, int userId) throws android.os.RemoteException;
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
