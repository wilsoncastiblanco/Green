package org.green.utilities;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.view.View;

import org.green.R;
import org.green.api.base.NetworkError;
import org.green.base.components.GenericDialog;
import org.green.base.domain.GenericMessageDialog;


/**
 * Created by rokk3r26 on 3/4/15.
 */
public class NetworkErrorUtil {

  private static Context mContext;

  public interface NetworkErrorEventListener{
    public void OnRetryNetworkConnection();
    public void OnEmptyResponse();
    public void OnCancelConnection();
  }

  private NetworkErrorEventListener listener;

  public void setOnNetworkErrorEventListener(NetworkErrorEventListener listener) {
    this.listener = listener;
  }

  public NetworkErrorUtil(Context context, FragmentManager fragmentManager, NetworkError.NetworkErrorType type, NetworkErrorEventListener listener){
    if(mContext == null) {
      this.mContext = context;
    }
    this.listener = listener;
    switch (type){
      case TIMEOUT:
        showRetryDialog(fragmentManager);
        break;
      case EMPTY:
        if(this.listener != null)  {
          listener.OnEmptyResponse();
        }
        break;
      case NOCONNECTION:
        showRetryDialog(fragmentManager);
        break;
    }
  }

  private void showRetryDialog(FragmentManager fragmentManager) {
    if(fragmentManager != null) {
      GenericMessageDialog.Builder fail = new GenericMessageDialog.Builder();
      fail.setCancelable(false)
          .setMainTitle(mContext.getString(R.string.alert_dialog_title_error))
          .setMessage(mContext.getString(R.string.alert_dialog_message_error_connection_retry))
          .setNameButtonAccept(mContext.getString(R.string.dialog_generic_retry))
          .create()
          .setOnClickListenerButtonsEventListener(new GenericDialog.OnClickListenerButtons() {
            @Override
            public void onClickListenerAccept(Dialog dialog, View v) {
              if (listener != null) {
                listener.OnRetryNetworkConnection();
              }
            }

            @Override
            public void onClickListenerCancel(Dialog dialog, View v) {
              if (listener != null) {
                listener.OnCancelConnection();
              }
            }
          })
          .show(fragmentManager, null);
    }
  }

}
