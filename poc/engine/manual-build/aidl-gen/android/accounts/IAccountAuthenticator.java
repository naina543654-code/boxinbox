/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Using: /home/hatch/android-sdk/build-tools/35.0.0/aidl --lang=java -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/Bcore/src/main/aidl -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-overlay -I/home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-prelude -o /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engine/manual-build/aidl-gen /home/hatch/workspace/goals/privacy-sandbox-android-build/poc/engines/zitanioi-blackbox/android-mirror/src/main/aidl/android/accounts/IAccountAuthenticator.aidl
 */
package android.accounts;
/**
 * Service that allows the interaction with an authentication server.
 * @hide
 */
public interface IAccountAuthenticator extends android.os.IInterface
{
  /** Default implementation for IAccountAuthenticator. */
  public static class Default implements android.accounts.IAccountAuthenticator
  {
    /** prompts the user for account information and adds the result to the IAccountManager */
    @Override public void addAccount(android.accounts.IAccountAuthenticatorResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, android.os.Bundle options) throws android.os.RemoteException
    {
    }
    /** prompts the user for the credentials of the account */
    @Override public void confirmCredentials(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, android.os.Bundle options) throws android.os.RemoteException
    {
    }
    /** gets the password by either prompting the user or querying the IAccountManager */
    @Override public void getAuthToken(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String authTokenType, android.os.Bundle options) throws android.os.RemoteException
    {
    }
    /** Gets the user-visible label of the given authtoken type. */
    @Override public void getAuthTokenLabel(android.accounts.IAccountAuthenticatorResponse response, java.lang.String authTokenType) throws android.os.RemoteException
    {
    }
    /** prompts the user for a new password and writes it to the IAccountManager */
    @Override public void updateCredentials(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String authTokenType, android.os.Bundle options) throws android.os.RemoteException
    {
    }
    /** launches an activity that lets the user edit and set the properties for an authenticator */
    @Override public void editProperties(android.accounts.IAccountAuthenticatorResponse response, java.lang.String accountType) throws android.os.RemoteException
    {
    }
    /**
     * returns a Bundle where the boolean value BOOLEAN_RESULT_KEY is set if the account has the
     * specified features
     */
    @Override public void hasFeatures(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String[] features) throws android.os.RemoteException
    {
    }
    /** Gets whether or not the account is allowed to be removed. */
    @Override public void getAccountRemovalAllowed(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account) throws android.os.RemoteException
    {
    }
    /** Returns a Bundle containing the required credentials to copy the account across users. */
    @Override public void getAccountCredentialsForCloning(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account) throws android.os.RemoteException
    {
    }
    /**
     * Uses the Bundle containing credentials from another instance of the authenticator to create
     * a copy of the account on this user.
     */
    @Override public void addAccountFromCredentials(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, android.os.Bundle accountCredentials) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements android.accounts.IAccountAuthenticator
  {
    /** Construct the stub at attach it to the interface. */
    @SuppressWarnings("this-escape")
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an android.accounts.IAccountAuthenticator interface,
     * generating a proxy if needed.
     */
    public static android.accounts.IAccountAuthenticator asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof android.accounts.IAccountAuthenticator))) {
        return ((android.accounts.IAccountAuthenticator)iin);
      }
      return new android.accounts.IAccountAuthenticator.Stub.Proxy(obj);
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
        case TRANSACTION_addAccount:
        {
          android.accounts.IAccountAuthenticatorResponse _arg0;
          _arg0 = android.accounts.IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
          java.lang.String _arg1;
          _arg1 = data.readString();
          java.lang.String _arg2;
          _arg2 = data.readString();
          java.lang.String[] _arg3;
          _arg3 = data.createStringArray();
          android.os.Bundle _arg4;
          _arg4 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          this.addAccount(_arg0, _arg1, _arg2, _arg3, _arg4);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_confirmCredentials:
        {
          android.accounts.IAccountAuthenticatorResponse _arg0;
          _arg0 = android.accounts.IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
          android.accounts.Account _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          android.os.Bundle _arg2;
          _arg2 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          this.confirmCredentials(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAuthToken:
        {
          android.accounts.IAccountAuthenticatorResponse _arg0;
          _arg0 = android.accounts.IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
          android.accounts.Account _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg2;
          _arg2 = data.readString();
          android.os.Bundle _arg3;
          _arg3 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          this.getAuthToken(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAuthTokenLabel:
        {
          android.accounts.IAccountAuthenticatorResponse _arg0;
          _arg0 = android.accounts.IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.getAuthTokenLabel(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_updateCredentials:
        {
          android.accounts.IAccountAuthenticatorResponse _arg0;
          _arg0 = android.accounts.IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
          android.accounts.Account _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String _arg2;
          _arg2 = data.readString();
          android.os.Bundle _arg3;
          _arg3 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          this.updateCredentials(_arg0, _arg1, _arg2, _arg3);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_editProperties:
        {
          android.accounts.IAccountAuthenticatorResponse _arg0;
          _arg0 = android.accounts.IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
          java.lang.String _arg1;
          _arg1 = data.readString();
          this.editProperties(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_hasFeatures:
        {
          android.accounts.IAccountAuthenticatorResponse _arg0;
          _arg0 = android.accounts.IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
          android.accounts.Account _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          java.lang.String[] _arg2;
          _arg2 = data.createStringArray();
          this.hasFeatures(_arg0, _arg1, _arg2);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAccountRemovalAllowed:
        {
          android.accounts.IAccountAuthenticatorResponse _arg0;
          _arg0 = android.accounts.IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
          android.accounts.Account _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          this.getAccountRemovalAllowed(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getAccountCredentialsForCloning:
        {
          android.accounts.IAccountAuthenticatorResponse _arg0;
          _arg0 = android.accounts.IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
          android.accounts.Account _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          this.getAccountCredentialsForCloning(_arg0, _arg1);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_addAccountFromCredentials:
        {
          android.accounts.IAccountAuthenticatorResponse _arg0;
          _arg0 = android.accounts.IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
          android.accounts.Account _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.accounts.Account.CREATOR);
          android.os.Bundle _arg2;
          _arg2 = _Parcel.readTypedObject(data, android.os.Bundle.CREATOR);
          this.addAccountFromCredentials(_arg0, _arg1, _arg2);
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
    private static class Proxy implements android.accounts.IAccountAuthenticator
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
      /** prompts the user for account information and adds the result to the IAccountManager */
      @Override public void addAccount(android.accounts.IAccountAuthenticatorResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, android.os.Bundle options) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _data.writeString(accountType);
          _data.writeString(authTokenType);
          _data.writeStringArray(requiredFeatures);
          _Parcel.writeTypedObject(_data, options, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addAccount, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /** prompts the user for the credentials of the account */
      @Override public void confirmCredentials(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, android.os.Bundle options) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _Parcel.writeTypedObject(_data, account, 0);
          _Parcel.writeTypedObject(_data, options, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_confirmCredentials, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /** gets the password by either prompting the user or querying the IAccountManager */
      @Override public void getAuthToken(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String authTokenType, android.os.Bundle options) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(authTokenType);
          _Parcel.writeTypedObject(_data, options, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAuthToken, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /** Gets the user-visible label of the given authtoken type. */
      @Override public void getAuthTokenLabel(android.accounts.IAccountAuthenticatorResponse response, java.lang.String authTokenType) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _data.writeString(authTokenType);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAuthTokenLabel, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /** prompts the user for a new password and writes it to the IAccountManager */
      @Override public void updateCredentials(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String authTokenType, android.os.Bundle options) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeString(authTokenType);
          _Parcel.writeTypedObject(_data, options, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_updateCredentials, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /** launches an activity that lets the user edit and set the properties for an authenticator */
      @Override public void editProperties(android.accounts.IAccountAuthenticatorResponse response, java.lang.String accountType) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _data.writeString(accountType);
          boolean _status = mRemote.transact(Stub.TRANSACTION_editProperties, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
       * returns a Bundle where the boolean value BOOLEAN_RESULT_KEY is set if the account has the
       * specified features
       */
      @Override public void hasFeatures(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String[] features) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _Parcel.writeTypedObject(_data, account, 0);
          _data.writeStringArray(features);
          boolean _status = mRemote.transact(Stub.TRANSACTION_hasFeatures, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /** Gets whether or not the account is allowed to be removed. */
      @Override public void getAccountRemovalAllowed(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _Parcel.writeTypedObject(_data, account, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAccountRemovalAllowed, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /** Returns a Bundle containing the required credentials to copy the account across users. */
      @Override public void getAccountCredentialsForCloning(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _Parcel.writeTypedObject(_data, account, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getAccountCredentialsForCloning, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      /**
       * Uses the Bundle containing credentials from another instance of the authenticator to create
       * a copy of the account on this user.
       */
      @Override public void addAccountFromCredentials(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, android.os.Bundle accountCredentials) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(response);
          _Parcel.writeTypedObject(_data, account, 0);
          _Parcel.writeTypedObject(_data, accountCredentials, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_addAccountFromCredentials, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
    }
    /** @hide */
    public static final java.lang.String DESCRIPTOR = "android.accounts.IAccountAuthenticator";
    static final int TRANSACTION_addAccount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_confirmCredentials = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_getAuthToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_getAuthTokenLabel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_updateCredentials = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_editProperties = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
    static final int TRANSACTION_hasFeatures = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
    static final int TRANSACTION_getAccountRemovalAllowed = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
    static final int TRANSACTION_getAccountCredentialsForCloning = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
    static final int TRANSACTION_addAccountFromCredentials = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
  }
  /** prompts the user for account information and adds the result to the IAccountManager */
  public void addAccount(android.accounts.IAccountAuthenticatorResponse response, java.lang.String accountType, java.lang.String authTokenType, java.lang.String[] requiredFeatures, android.os.Bundle options) throws android.os.RemoteException;
  /** prompts the user for the credentials of the account */
  public void confirmCredentials(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, android.os.Bundle options) throws android.os.RemoteException;
  /** gets the password by either prompting the user or querying the IAccountManager */
  public void getAuthToken(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String authTokenType, android.os.Bundle options) throws android.os.RemoteException;
  /** Gets the user-visible label of the given authtoken type. */
  public void getAuthTokenLabel(android.accounts.IAccountAuthenticatorResponse response, java.lang.String authTokenType) throws android.os.RemoteException;
  /** prompts the user for a new password and writes it to the IAccountManager */
  public void updateCredentials(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String authTokenType, android.os.Bundle options) throws android.os.RemoteException;
  /** launches an activity that lets the user edit and set the properties for an authenticator */
  public void editProperties(android.accounts.IAccountAuthenticatorResponse response, java.lang.String accountType) throws android.os.RemoteException;
  /**
   * returns a Bundle where the boolean value BOOLEAN_RESULT_KEY is set if the account has the
   * specified features
   */
  public void hasFeatures(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, java.lang.String[] features) throws android.os.RemoteException;
  /** Gets whether or not the account is allowed to be removed. */
  public void getAccountRemovalAllowed(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account) throws android.os.RemoteException;
  /** Returns a Bundle containing the required credentials to copy the account across users. */
  public void getAccountCredentialsForCloning(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account) throws android.os.RemoteException;
  /**
   * Uses the Bundle containing credentials from another instance of the authenticator to create
   * a copy of the account on this user.
   */
  public void addAccountFromCredentials(android.accounts.IAccountAuthenticatorResponse response, android.accounts.Account account, android.os.Bundle accountCredentials) throws android.os.RemoteException;
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
