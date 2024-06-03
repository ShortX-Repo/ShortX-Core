/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package tornaco.apps.shortx.core.notification;
public interface INotificationPoster extends android.os.IInterface
{
  /** Default implementation for INotificationPoster. */
  public static class Default implements tornaco.apps.shortx.core.notification.INotificationPoster
  {
    // PostNotification
    @Override public void post(tornaco.apps.shortx.core.rule.action.ByteArrayWrapper data) throws android.os.RemoteException
    {
    }
    @Override public void cancel(java.lang.String tag) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements tornaco.apps.shortx.core.notification.INotificationPoster
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an tornaco.apps.shortx.core.notification.INotificationPoster interface,
     * generating a proxy if needed.
     */
    public static tornaco.apps.shortx.core.notification.INotificationPoster asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof tornaco.apps.shortx.core.notification.INotificationPoster))) {
        return ((tornaco.apps.shortx.core.notification.INotificationPoster)iin);
      }
      return new tornaco.apps.shortx.core.notification.INotificationPoster.Stub.Proxy(obj);
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
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
      }
      switch (code)
      {
        case TRANSACTION_post:
        {
          tornaco.apps.shortx.core.rule.action.ByteArrayWrapper _arg0;
          _arg0 = _Parcel.readTypedObject(data, tornaco.apps.shortx.core.rule.action.ByteArrayWrapper.CREATOR);
          this.post(_arg0);
          break;
        }
        case TRANSACTION_cancel:
        {
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.cancel(_arg0);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements tornaco.apps.shortx.core.notification.INotificationPoster
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
      // PostNotification
      @Override public void post(tornaco.apps.shortx.core.rule.action.ByteArrayWrapper data) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, data, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_post, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
      @Override public void cancel(java.lang.String tag) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(tag);
          boolean _status = mRemote.transact(Stub.TRANSACTION_cancel, _data, null, android.os.IBinder.FLAG_ONEWAY);
        }
        finally {
          _data.recycle();
        }
      }
    }
    static final int TRANSACTION_post = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_cancel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
  }
  public static final java.lang.String DESCRIPTOR = "tornaco.apps.shortx.core.notification.INotificationPoster";
  // PostNotification
  public void post(tornaco.apps.shortx.core.rule.action.ByteArrayWrapper data) throws android.os.RemoteException;
  public void cancel(java.lang.String tag) throws android.os.RemoteException;
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
