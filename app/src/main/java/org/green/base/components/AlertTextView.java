package org.green.base.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.neopixl.pixlui.components.textview.TextView;

import org.green.R;
import org.green.utilities.AppUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by johan on 16/01/15.
 */
public class AlertTextView extends RelativeLayout {
  @InjectView(R.id.textViewAlertSearch)
  TextView textViewAlertSearch;
  @InjectView(R.id.relativeLayoutTitleAlert)
  RelativeLayout relativeLayoutTitleAlert;
  private Animation animBounceIn;
  private Animation animBounceOut;

  public AlertTextView(Context context) {
    super(context);
    init(context);
  }

  public AlertTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public AlertTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  /**
   * init componentes of view
   * @param context
   */
  public void init(Context context){
    this.removeAllViews();
    inflateView(context);
    ButterKnife.inject(this, this);
    animBounceIn = AnimationUtils.loadAnimation(context, R.anim.bounce_in);
    animBounceOut = AnimationUtils.loadAnimation(context, R.anim.bounce_out);
    animBounceIn.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        AppUtil.showViews(relativeLayoutTitleAlert);
      }
      @Override
      public void onAnimationEnd(Animation animation) {

      }
      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });

    animBounceOut.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
      }
      @Override
      public void onAnimationEnd(Animation animation) {
        AppUtil.hideGoneViews(relativeLayoutTitleAlert);
      }
      @Override
      public void onAnimationRepeat(Animation animation) {
      }
    });

  }

  /**
   * Inflate view with text alert view
   * @param context application context
   * */
  public void inflateView(Context context){
    LayoutInflater.from(context).inflate(R.layout.view_alert_text, this, true);
  }

  /**
   * change text message of the textview
   * @param title
   */
  public void setTitle(String title){
    textViewAlertSearch.setText(title);
  }

  /**
   * show the alert text view
   */
  public void show(){
    relativeLayoutTitleAlert.startAnimation(animBounceIn);
  }

  /**
   * hide the alert text view
   */
  public void hide(){
    relativeLayoutTitleAlert.startAnimation(animBounceIn);
  }
}
