package org.green.base.domain;

import android.os.Bundle;

import org.green.base.components.GenericDialog;
import org.green.utilities.IntentUtil;


/**
 * Created by johan on 14/01/15.
 */
public class GenericMessageDialog extends GenericDialog {


  public  static GenericMessageDialog newInstance(Bundle bundle){
    GenericMessageDialog genericMessageDialog = new GenericMessageDialog();
    genericMessageDialog.setArguments(bundle);
    return genericMessageDialog;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Builder builder = getObjects();
    this.mainTitle = builder.getMainTitle();
    this.message = builder.getMessage();
    this.nameButtonAccept = builder.getNameButtonAccept();
    this.nameButtonCancel = builder.getNameButtonCancel();
    this.isVisibleAccept = builder.isVisibleAccept();
    this.isVisibleCancel = builder.isVisibleCancel();
    this.view = builder.getContentView();
    this.isCancelable = builder.isCancelable();
  }

  public Builder getObjects(){
    return IntentUtil.mapper.fromJson(getArguments().getString(IntentUtil.KEY_ID), Builder.class);
  }

  public GenericMessageDialog() {
    this.getArguments();

  }

  public String getMainTitle() {
    return mainTitle;
  }

  public String getMessage() {
    return message;
  }

  public boolean isVisibleAccept() {
    return isVisibleAccept;
  }

  public boolean isVisibleCancel() {
    return isVisibleCancel;
  }

  public int getContentView() {
    return view;
  }

  /**
   * Patter builder to save data
   */
  public static class Builder {
    private String mainTitle;
    private String message;
    private String nameButtonAccept;
    private String nameButtonCancel;
    private boolean isVisibleAccept = true;
    private boolean isVisibleCancel = true;
    private boolean cancelable = true;
    private int contentView;
    private OnClickListenerButtons eventListener;

    public Builder() {
    }

    public Builder setMainTitle(String mainTitle) {
      this.mainTitle = mainTitle;
      return this;
    }

    public Builder setMessage(String message) {
      this.message = message;
      return this;
    }

    public Builder setVisibleAccept(boolean isVisibleAccept) {
      this.isVisibleAccept = isVisibleAccept;
      return this;
    }

    public Builder setVisibleCancel(boolean isVisibleCancel) {
      this.isVisibleCancel = isVisibleCancel;
      return this;
    }

    public Builder setContentView(int contentView) {
      this.contentView = contentView;
      return this;
    }

    public Builder setCancelable(boolean cancelable) {
      this.cancelable = cancelable;
      return this;
    }

    public boolean isCancelable() {
      return cancelable;
    }

    public String getMainTitle() {
      return mainTitle;
    }

    public String getMessage() {
      return message;
    }

    public boolean isVisibleAccept() {
      return isVisibleAccept;
    }

    public boolean isVisibleCancel() {
      return isVisibleCancel;
    }

    public int getContentView() {
      return contentView;
    }

    public GenericMessageDialog create(){
      Bundle bundle = new Bundle();
      bundle.putString(IntentUtil.KEY_ID, IntentUtil.mapper.toJson(this));
      return GenericMessageDialog.newInstance(bundle);
    }

    public String getNameButtonAccept() {
      return nameButtonAccept;
    }

    public Builder setNameButtonAccept(String nameButtonAccept) {
      this.nameButtonAccept = nameButtonAccept;
      return this;
    }

    public String getNameButtonCancel() {
      return nameButtonCancel;
    }

    public Builder setNameButtonCancel(String nameButtonCancel) {
      this.nameButtonCancel = nameButtonCancel;
      return this;
    }
  }
}
