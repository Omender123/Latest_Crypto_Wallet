package com.crypto.croytowallet.Extra_Class;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DrawableTextView extends androidx.appcompat.widget.AppCompatTextView {
    private Drawable drawableRight;
    private Drawable drawableLeft;
    private Drawable drawableTop;
    private Drawable drawableBottom;

    int actionX, actionY;

    private DrawableClickListenerr clickListener;

    public DrawableTextView (Context context, AttributeSet attrs) {
        super(context, attrs);
        // this Contructure required when you are using this view in xml
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {
        if (left != null) {
            drawableLeft = left;
        }
        if (right != null) {
            drawableRight = right;
        }
        if (top != null) {
            drawableTop = top;
        }
        if (bottom != null) {
            drawableBottom = bottom;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Rect bounds;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            actionX = (int) event.getX();
            actionY = (int) event.getY();
            if (drawableBottom != null
                    && drawableBottom.getBounds().contains(actionX, actionY)) {
                clickListener.onClick(DrawableClickListenerr.DrawablePosition.BOTTOM);
                return super.onTouchEvent(event);
            }

            if (drawableTop != null
                    && drawableTop.getBounds().contains(actionX, actionY)) {
                clickListener.onClick(DrawableClickListenerr.DrawablePosition.TOP);
                return super.onTouchEvent(event);
            }

            // this works for left since container shares 0,0 origin with bounds
            if (drawableLeft != null) {
                bounds = null;
                bounds = drawableLeft.getBounds();

                int x, y;
                int extraTapArea = (int) (13 * getResources().getDisplayMetrics().density  + 0.5);

                x = actionX;
                y = actionY;

                if (!bounds.contains(actionX, actionY)) {
                    /** Gives the +20 area for tapping. */
                    x = (int) (actionX - extraTapArea);
                    y = (int) (actionY - extraTapArea);

                    if (x <= 0)
                        x = actionX;
                    if (y <= 0)
                        y = actionY;

                    /** Creates square from the smallest value */
                    if (x < y) {
                        y = x;
                    }
                }

                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener
                            .onClick(DrawableClickListenerr.DrawablePosition.LEFT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;

                }
            }

            if (drawableRight != null) {

                bounds = null;
                bounds = drawableRight.getBounds();

                int x, y;
                int extraTapArea = 13;

                /**
                 * IF USER CLICKS JUST OUT SIDE THE RECTANGLE OF THE DRAWABLE
                 * THAN ADD X AND SUBTRACT THE Y WITH SOME VALUE SO THAT AFTER
                 * CALCULATING X AND Y CO-ORDINATE LIES INTO THE DRAWBABLE
                 * BOUND. - this process help to increase the tappable area of
                 * the rectangle.
                 */
                x = (int) (actionX + extraTapArea);
                y = (int) (actionY - extraTapArea);

                /**Since this is right drawable subtract the value of x from the width
                 * of view. so that width - tappedarea will result in x co-ordinate in drawable bound.
                 */
                x = getWidth() - x;

                /*x can be negative if user taps at x co-ordinate just near the width.
                 * e.g views width = 300 and user taps 290. Then as per previous calculation
                 * 290 + 13 = 303. So subtract X from getWidth() will result in negative value.
                 * So to avoid this add the value previous added when x goes negative.
                 */

                if(x <= 0){
                    x += extraTapArea;
                }

                /* If result after calculating for extra tappable area is negative.
                 * assign the original value so that after subtracting
                 * extratapping area value doesn't go into negative value.
                 */

                if (y <= 0)
                    y = actionY;

                /**If drawble bounds contains the x and y points then move ahead.*/
                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener
                            .onClick(DrawableClickListenerr.DrawablePosition.RIGHT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;
                }
                return super.onTouchEvent(event);
            }

        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        drawableRight = null;
        drawableBottom = null;
        drawableLeft = null;
        drawableTop = null;
        super.finalize();
    }

    public void setDrawableClickListenerr(DrawableClickListenerr listener) {
        this.clickListener = listener;
    }

    public interface DrawableClickListenerr {

        public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
        public void onClick(DrawableClickListenerr.DrawablePosition target);
    }
  /*  private DrawableRightClickListener mDrawableRightClickListener;
    private DrawableLeftClickListener mDrawableLeftClickListener;
    final int DRAWABLE_LEFT = 0;//Left picture
    final int DRAWABLE_TOP = 1;//Picture above
    final int DRAWABLE_RIGHT = 2;//Right picture
    final int DRAWABLE_BOTTOM = 3;//The following picture
    public DrawableTextView(Context context) {
        this(context,null);
    }
    public DrawableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }
    public DrawableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //Define an interface to listen on the right
    public interface DrawableRightClickListener{
        void onDrawableRightClickListener(View view);
    }
    //Define an interface to listen on the left
    public interface DrawableLeftClickListener{
        void onDrawableLeftClickListener(View view);
    }
    //Define a set method for right listening
    public void setDrawableRightClickListener(DrawableRightClickListener listener) {
        this.mDrawableRightClickListener = listener;
    }
    //Define a set method for left listening
    public void setDrawableLeftClickListener(DrawableLeftClickListener listener) {
        this.mDrawableLeftClickListener = listener;
    }
    //Define a right-hand back listening method
    public DrawableRightClickListener getDrawableRightClick() {
        return mDrawableRightClickListener;
    }
    //Define a method to return to listening on the left
    public DrawableLeftClickListener getDrawableLeftClick() {
        return mDrawableLeftClickListener;
    }

    //Rewriting onTouchEvent Method
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mDrawableRightClickListener != null) {
                    Drawable drawableRight = getCompoundDrawables()[DRAWABLE_RIGHT] ;
                    //The judgment is based on obtaining that the x value of the click area relative to the screen is greater than the rightmost boundary of the control minus the width of the right control.
                    if (drawableRight != null && event.getRawX() >= (getRight() - drawableRight.getBounds().width())) {
                        mDrawableRightClickListener.onDrawableRightClickListener(this);
                        return true ;//The return value must be true, otherwise it cannot respond.
                    }
                }
                if (mDrawableLeftClickListener != null) {
                    Drawable drawableLeft = getCompoundDrawables()[DRAWABLE_LEFT] ;
                    //The judgment is based on the fact that the x value of the click area relative to the screen is less than the leftmost boundary of the control plus the width of the left control.
                    if (drawableLeft != null && event.getRawX() <= (getLeft() + drawableLeft.getBounds().width())) {
                        mDrawableLeftClickListener.onDrawableLeftClickListener(this) ;
                        return true ;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }*/
}