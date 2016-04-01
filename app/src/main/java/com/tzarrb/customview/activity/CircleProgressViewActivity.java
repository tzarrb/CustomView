package com.tzarrb.customview.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tzarrb.customview.R;
import com.tzarrb.customview.view.CircleProgressView;

public class CircleProgressViewActivity extends AppCompatActivity {

    CircleProgressView circleProgressView;

    private int checkItemDuration = 10000;
    private int animatorStartTime = 1;
    private int animatorEndTime = checkItemDuration*3 - 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progress_view);
        circleProgressView = (CircleProgressView)findViewById(R.id.circle_progress_view);

        ValueAnimator animator = ValueAnimator.ofInt(animatorStartTime, animatorEndTime);
        animator.setDuration(6000);
        animator.addUpdateListener(new HealthCheckingListener());
        animator.addListener(new HealthCheckEndListener());
        animator.start();
    }


    private class HealthCheckingListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int value = (int) animation.getAnimatedValue();
            int item = value / checkItemDuration;
            circleProgressView.setPercent(value * 100f / animatorEndTime);

        }
    }

    private class HealthCheckEndListener implements ValueAnimator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {
            circleProgressView.setPercent(0);
            //circleProgressView.setFgColorStart(getResources().getColor(R.color.green_light));
            //circleProgressView.setFgColorEnd(getResources().getColor(R.color.green_light));
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            circleProgressView.setPercent(100);
            circleProgressView.setFgColorStart(getResources().getColor(R.color.red));
            circleProgressView.setFgColorEnd(getResources().getColor(R.color.red));
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
