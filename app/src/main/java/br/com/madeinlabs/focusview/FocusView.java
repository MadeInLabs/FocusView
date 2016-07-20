package br.com.madeinlabs.focusview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class FocusView extends RelativeLayout{
    protected static final float HOLE_RADIUS_DEFAULT = -1;
    protected Paint mBackgroundPaint;

    protected float mHoleRadius;
    protected float mHoleMargin;
    protected int mBackgroundColor;
    protected View mCircleAncor;

    protected float mRectHeigth;
    protected float mRectWidth;

    protected Path mPath;
    protected float mHoleX;
    protected float mHoleY;
    protected int mCircleAncorX;
    protected int mCircleAncorY;
    protected boolean mCoverHeight;

    protected RelativeLayout mLayoutHole;
    protected RelativeLayout mLayoutLeft;
    protected RelativeLayout mLayoutBelow;
    protected RelativeLayout mLayoutAbove;
    protected RelativeLayout mLayoutRight;

    public FocusView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public FocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        setWillNotDraw(false);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.FocusView, 0, 0);
        try {
            mHoleRadius = typedArray.getDimension(R.styleable.FocusView_holeRadius, HOLE_RADIUS_DEFAULT);
            mHoleMargin = typedArray.getDimension(R.styleable.FocusView_holeMargin, 30);
            mBackgroundColor = typedArray.getColor(R.styleable.FocusView_backgroundColor, ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            mCoverHeight = typedArray.getBoolean(R.styleable.FocusView_coverHeight, true);
            int resourceId = typedArray.getResourceId(R.styleable.FocusView_circleAncor, -1);
            mCircleAncor = findViewById(resourceId);
        } finally {
            typedArray.recycle();
        }

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(getBackgroundColor());
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mPath = new Path();

        setClickable(true);
        //setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));

        mLayoutHole = new RelativeLayout(getContext());
        LayoutParams layoutParamsHole = new LayoutParams(0, 0);
        mLayoutHole.setLayoutParams(layoutParamsHole);
        //mLayoutHole.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray));

        mLayoutAbove = new RelativeLayout(getContext());
        LayoutParams layoutParamsAbove = new LayoutParams(0, 0);
        mLayoutAbove.setLayoutParams(layoutParamsAbove);
        //mLayoutAbove.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));

        mLayoutRight = new RelativeLayout(getContext());
        LayoutParams layoutParamsRight = new LayoutParams(0, 0);
        mLayoutRight.setLayoutParams(layoutParamsRight);
        //mLayoutRight.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));

        mLayoutBelow = new RelativeLayout(getContext());
        LayoutParams layoutParamsBelow = new LayoutParams(0, 0);
        mLayoutBelow.setLayoutParams(layoutParamsBelow);
        //mLayoutBelow.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));

        mLayoutLeft = new RelativeLayout(getContext());
        LayoutParams layoutParamsLeft = new LayoutParams(0, 0);
        mLayoutLeft.setLayoutParams(layoutParamsLeft);
        //mLayoutLeft.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.yellow));

        addView(mLayoutHole);
        addView(mLayoutAbove);
        addView(mLayoutRight);
        addView(mLayoutBelow);
        addView(mLayoutLeft);
    }

    protected void updateHoleCircleAncor() {
        int[] circleAncorLocation = new int[2];
        mCircleAncor.getLocationOnScreen(circleAncorLocation);
        mCircleAncorX = circleAncorLocation[0];
        mCircleAncorY = circleAncorLocation[1];

        int[] myLocation = new int[2];
        getLocationOnScreen(myLocation);

        int circleAncorWidth = mCircleAncor.getWidth();
        mHoleX = mCircleAncorX + (circleAncorWidth / 2) - myLocation[0];
        int circleAncorHeight = mCircleAncor.getHeight();
        mHoleY = mCircleAncorY + (circleAncorHeight / 2) - myLocation[1];

        if(mCoverHeight) {
            mHoleRadius = mCircleAncor.getHeight() / 2 + mHoleMargin;
        } else {
            mHoleRadius = mCircleAncor.getWidth() / 2 + mHoleMargin;
        }

        setLayoutAroundHole();
    }

    protected void setLayoutAroundHole(){
        LayoutParams layoutParamsHole = (RelativeLayout.LayoutParams) mLayoutHole.getLayoutParams();
        layoutParamsHole.height = (int) mHoleRadius * 2;
        layoutParamsHole.width = (int) mHoleRadius * 2;
        mLayoutHole.setX(mHoleX - mHoleRadius);
        mLayoutHole.setY(mHoleY - mHoleRadius);

        setLayoutLeft();
        setLayoutAbove();
        setLayoutRight();
        setLayoutBelow();

        invalidate();
        requestLayout();
    }

    void setLayoutBelow() {
        LayoutParams layoutParamsBelow = (RelativeLayout.LayoutParams) mLayoutBelow.getLayoutParams();
        layoutParamsBelow.height = (int) (mRectHeigth - (mHoleY + mHoleRadius));
        layoutParamsBelow.width = LayoutParams.MATCH_PARENT;
        mLayoutBelow.setX(0);
        mLayoutBelow.setY(mHoleY + mHoleRadius);
    }

    void setLayoutRight() {
        LayoutParams layoutParamsRight = (RelativeLayout.LayoutParams) mLayoutRight.getLayoutParams();
        layoutParamsRight.height = (int) mHoleRadius * 2;
        layoutParamsRight.width = (int) (mRectWidth - (mHoleX + mHoleRadius));
        mLayoutRight.setX(mHoleX + mHoleRadius);
        mLayoutRight.setY(mHoleY - mHoleRadius);
    }

    void setLayoutAbove() {
        LayoutParams layoutParamsAbove = (RelativeLayout.LayoutParams) mLayoutAbove.getLayoutParams();
        layoutParamsAbove.height = (int) (mHoleY - mHoleRadius);
        layoutParamsAbove.width = LayoutParams.MATCH_PARENT;
        mLayoutAbove.setX(0);
        mLayoutAbove.setY(mHoleY - mHoleRadius - layoutParamsAbove.height);
    }

    void setLayoutLeft() {
        LayoutParams layoutParamsLeft = (RelativeLayout.LayoutParams) mLayoutLeft.getLayoutParams();
        layoutParamsLeft.height = (int) mHoleRadius * 2;
        layoutParamsLeft.width = (int) (mHoleX - mHoleRadius);
        mLayoutLeft.setX(mHoleX - mHoleRadius - layoutParamsLeft.width);
        mLayoutLeft.setY(mHoleY - mHoleRadius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Account for padding
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());

        // Figure out how big we can make the pie.
        mRectHeigth = ((float)h - ypad);
        mRectWidth = ((float)w - xpad);
    }

    public void setCircleAncor(View circleAncor) {
        this.mCircleAncor = circleAncor;
        updateHoleCircleAncor();
        this.mCircleAncor.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int[] circleAncorLocation = new int[2];
                mCircleAncor.getLocationOnScreen(circleAncorLocation);
                if(circleAncorLocation[0] != mCircleAncorX || circleAncorLocation[1] != mCircleAncorY) {
                    updateHoleCircleAncor();
                }
            }
        });
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();

        mPath.setFillType(Path.FillType.WINDING);
        mPath.moveTo(0, 0);
        mPath.addRect(0, 0, mRectWidth, mRectHeigth, Path.Direction.CW);
        mPath.close();

        mPath.moveTo(0, 0);
        mPath.addCircle(mHoleX, mHoleY, mHoleRadius, Path.Direction.CW);
        mPath.close();

        mPath.setFillType(Path.FillType.EVEN_ODD);
        canvas.drawPath(mPath, mBackgroundPaint);
    }

    public void addLayoutRight(int layoutRight) {
        //Set View right to the hole
        inflate(getContext(), layoutRight, mLayoutRight);
    }

    public void addLayoutAbove(int layoutAbove) {
        //Set View above to the hole
        inflate(getContext(), layoutAbove, mLayoutAbove);
    }

    public void addLayoutBelow(int layoutBelow) {
        //Set View below to the hole
        inflate(getContext(), layoutBelow, mLayoutBelow);
    }

    public View getLayoutBelow() {
        return mLayoutBelow;
    }

    public void addLayoutBelow(View viewBelow) {
        ViewGroup rootView = (ViewGroup) viewBelow.getRootView();
        rootView.removeView(viewBelow);

        mLayoutBelow.addView(viewBelow);
    }

    public void log() {
        Log.d("TAG", "view grandona visivel? " + (getVisibility() == View.VISIBLE));
        Log.d("TAG", "view grandona height = " + getLayoutParams().height + " width = " + getLayoutParams().width);
        Log.d("TAG", "x = " + mLayoutAbove.getX() + " y = " + mLayoutAbove.getY());
        LayoutParams layoutParamsAbove = (LayoutParams) mLayoutAbove.getLayoutParams();
        Log.d("TAG", "height = " + layoutParamsAbove.height + " width = " + layoutParamsAbove.width);
        Log.d("TAG", "visivel? " + (mLayoutAbove.getVisibility() == View.VISIBLE));
        for(int i = 0; i < mLayoutAbove.getChildCount(); i++) {
            Log.d("TAG", "\tx = " + mLayoutAbove.getChildAt(i).getX() + " y = " + mLayoutAbove.getChildAt(i).getY());
            Log.d("TAG", "\theight = " + mLayoutAbove.getChildAt(i).getLayoutParams().height + " width = " + mLayoutAbove.getChildAt(i).getLayoutParams().width);
            Log.d("TAG", "\tvisivel? " + (mLayoutAbove.getChildAt(i).getVisibility() == View.VISIBLE));
        }
    }

    public void setCoverHeight(boolean coverHeight) {
        this.mCoverHeight = coverHeight;
    }

    public void setHoleMargin(int holeMargin) {
        this.mHoleMargin = holeMargin;
    }
}