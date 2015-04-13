package org.green.base.components;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import org.green.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by johan on 13/01/15.
 */
public class GenericDialog extends DialogFragment {
  @InjectView(R.id.textViewMainTitle)
  TextView textViewMainTitle;
  @InjectView(R.id.textViewMessage)
  TextView textViewMessage;
  @InjectView(R.id.viewContent)
  View viewContent;
  @InjectView(R.id.buttonAccept)
  Button buttonAccept;
  @InjectView(R.id.buttonCancel)
  Button buttonCancel;

  protected String mainTitle;
  protected String message;
  protected String nameButtonAccept;
  protected String nameButtonCancel;
  protected boolean isVisibleAccept;
  protected boolean isVisibleCancel;
  protected boolean isCancelable;
  protected int view;
  protected Dialog dialog;
  private OnClickListenerButtons onClickListenerButtonsEventListener;

  public GenericDialog setOnClickListenerButtonsEventListener(OnClickListenerButtons onClickListenerButtonsEventListener) {
    this.onClickListenerButtonsEventListener = onClickListenerButtonsEventListener;
    return this;
  }

  public interface OnClickListenerButtons{
    public void onClickListenerAccept(Dialog dialog, View v);
    public void onClickListenerCancel(Dialog dialog, View v);
  }

  public GenericDialog() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    View rootView = inflater.inflate(R.layout.dialog_alert_generic,container, false);
    ButterKnife.inject(this, rootView);
    dialog = getDialog();
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    InitComponents();
    return rootView;
  }

  private void InitComponents(){
    textViewMainTitle.setText(mainTitle);
    textViewMessage.setText(message);
    buttonAccept.setVisibility((isVisibleAccept)? View.VISIBLE: View.GONE);
    dialog.setCancelable(isCancelable);
    if(nameButtonAccept != null){
      buttonAccept.setText(nameButtonAccept);
    }
    buttonAccept.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(onClickListenerButtonsEventListener != null) {
          onClickListenerButtonsEventListener.onClickListenerAccept(dialog, v);
        }
        dialog.cancel();
      }
    });
    if(nameButtonCancel != null){
      buttonCancel.setText(nameButtonCancel);
    }
    buttonCancel.setVisibility((isVisibleCancel)? View.VISIBLE: View.GONE);
    buttonCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(onClickListenerButtonsEventListener != null) {
          onClickListenerButtonsEventListener.onClickListenerCancel(dialog, v);
        }
        dialog.cancel();
      }
    });
    if(view > 0) {
      viewContent = View.inflate(getActivity(), view, null);
    }
  }


}
