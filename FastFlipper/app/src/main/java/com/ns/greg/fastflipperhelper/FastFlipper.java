package com.ns.greg.fastflipperhelper;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import java.lang.ref.WeakReference;

/**
 * Created by Gregory on 2017/3/6.
 */
public class FastFlipper extends ViewFlipper {

    /**
     * The flip listener to receive the callback from {@link InAnimationListener}, {@link OutAnimationListener}.
     */
    public interface OnFlipListener {

        // When in animation is start;
        void onInAnimationStart();

        // When in animation is end.
        void onInAnimationEnd();

        // When out animation is start.
        void onOutAnimationStart();

        // When out animation is end.
        void onOutAnimationEnd();
    }

    // The in animation listener instance
    private final InAnimationListener inAnimationListener;

    // The out animation listener instance
    private final OutAnimationListener outAnimationListener;

    // The flip listener instance
    private OnFlipListener OnFlipListener;

    public FastFlipper(Context context) {
        this(context, null);
    }

    public FastFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        inAnimationListener = new InAnimationListener(this);
        outAnimationListener = new OutAnimationListener(this);
    }

    /**
     * Adds the new page and show.
     * [Noticed] This will reset animation listener.
     *
     * @param onFlipListener listener to received the callback from {@link android.view.animation.Animation.AnimationListener}
     * @param view the new target view
     * @param inResId the in animation resource id
     * @param outResId the out animation resource id
     */
    public void showNext(@Nullable OnFlipListener onFlipListener, View view, @AnimRes int inResId, @AnimRes int outResId) {
        Animation in = AnimationUtils.loadAnimation(getContext(), inResId);
        Animation out = AnimationUtils.loadAnimation(getContext(), outResId);
        setOnFlipListener(onFlipListener);
        addView(view);
        setInAnimation(in);
        setOutAnimation(out);
        setInAnimationListener(in);
        setOutAnimationListener(out);
        post(new Runnable() {
            @Override
            public void run() {
                FastFlipper.this.showNext();
            }
        });
    }

    /**
     * Adds the new page and show.
     * [Noticed] This will reset animation listener.
     *
     * @param onFlipListener listener to received the callback from {@link android.view.animation.Animation.AnimationListener}
     * @param view the new target view
     * @param in the in animation
     * @param out the out animation
     */
    public void showNext(@Nullable OnFlipListener onFlipListener, View view, @Nullable Animation in, @Nullable Animation out) {
        setOnFlipListener(onFlipListener);
        addView(view);
        setInAnimation(in);
        setOutAnimation(out);
        setInAnimationListener(in);
        setOutAnimationListener(out);
        post(new Runnable() {
            @Override
            public void run() {
                FastFlipper.this.showNext();
            }
        });
    }

    /**
     * Backs to the previous page and decides to remove or not current page.
     * [Noticed] This will reset animation listener.
     *
     * @param onFlipListener listener to received the callback from {@link android.view.animation.Animation.AnimationListener}
     * @param disposable remove current page if true, otherwise not
     * @param inResId the in animation resource id
     * @param outResId the out animation resource id
     */
    public void showPrevious(@Nullable OnFlipListener onFlipListener, boolean disposable, @AnimRes int inResId, @AnimRes int outResId) {
        Animation in = AnimationUtils.loadAnimation(getContext(), inResId);
        Animation out = AnimationUtils.loadAnimation(getContext(), outResId);
        setOnFlipListener(onFlipListener);
        setInAnimation(in);
        setOutAnimation(out);
        setInAnimationListener(in);
        setOutAnimationListener(out);
        outAnimationListener.setDisposable(disposable);
        post(new Runnable() {
            @Override
            public void run() {
                FastFlipper.this.showPrevious();
            }
        });
    }

    /**
     * Backs to the previous page and decides to remove or not current page.
     * [Noticed] This will reset animation listener.
     *
     * @param onFlipListener listener to received the callback from {@link android.view.animation.Animation.AnimationListener}
     * @param disposable remove current page if true, otherwise not
     * @param in the in animation
     * @param out the out animation
     */
    public void showPrevious(@Nullable OnFlipListener onFlipListener, boolean disposable, @Nullable Animation in, @Nullable Animation out) {
        setOnFlipListener(onFlipListener);
        setInAnimation(in);
        setOutAnimation(out);
        setInAnimationListener(in);
        setOutAnimationListener(out);
        outAnimationListener.setDisposable(disposable);
        post(new Runnable() {
            @Override
            public void run() {
                FastFlipper.this.showPrevious();
            }
        });
    }

    /**
     * Sets the flip listener if you need to do some action while
     * {@link android.view.animation.Animation.AnimationListener#onAnimationStart(Animation)}
     * {@link android.view.animation.Animation.AnimationListener#onAnimationEnd(Animation)}
     *
     * @param onFlipListener
     */
    private void setOnFlipListener(@Nullable OnFlipListener onFlipListener) {
        this.OnFlipListener = onFlipListener;
    }

    /**
     * Sets animation listener for in.
     *
     * @param in animation
     */
    private void setInAnimationListener(Animation in) {
        if (in != null) {
            in.setAnimationListener(inAnimationListener);
        }
    }

    /**
     * Sets animation listener for out.
     *
     * @param out animation
     */
    private void setOutAnimationListener(Animation out) {
        if (out != null) {
            out.setAnimationListener(outAnimationListener);
        }
    }

    private static class InAnimationListener implements Animation.AnimationListener {

        private final FastFlipper instance;

        public InAnimationListener(FastFlipper reference) {
            WeakReference<FastFlipper> weakReference = new WeakReference<FastFlipper>(reference);
            instance = weakReference.get();
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (instance.OnFlipListener != null) {
                instance.OnFlipListener.onInAnimationStart();
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (instance.OnFlipListener != null) {
                instance.OnFlipListener.onInAnimationEnd();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // Nothing to do.
        }
    }

    private static class OutAnimationListener implements Animation.AnimationListener {

        private final FastFlipper instance;

        private boolean disposable;

        public OutAnimationListener(FastFlipper reference) {
            WeakReference<FastFlipper> weakReference = new WeakReference<FastFlipper>(reference);
            instance = weakReference.get();
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (instance.OnFlipListener != null) {
                instance.OnFlipListener.onOutAnimationStart();
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (disposable) {
                removeView();
            }

            if (instance.OnFlipListener != null) {
                instance.OnFlipListener.onOutAnimationEnd();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // Nothing to do.
        }

        public void setDisposable(boolean disposable) {
            this.disposable = disposable;
        }

        private void removeView() {
            int currentIndex = 0;
            // Find current view's index in flipper
            for (; currentIndex < instance.getChildCount(); currentIndex++) {
                if (instance.getChildAt(currentIndex) == instance.getCurrentView()) {
                    break;
                }
            }

            // Remove view index greater than current index
            int removeIndex = instance.getChildCount() - 1;
            do {
                instance.removeViewAt(removeIndex);
                removeIndex = instance.getChildCount() - 1;
            } while (removeIndex > currentIndex);
        }
    }
}
