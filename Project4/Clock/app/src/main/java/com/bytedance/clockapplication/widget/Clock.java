package com.bytedance.clockapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Locale;

public class Clock extends View {

    private final static String TAG = Clock.class.getSimpleName();

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;
    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;
    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;
    private static final float DEFAULT_HOURNEEDLES_STROKE_WIDTH = 0.10f;
    private static final float DEFAULT_MINUTENEEDLES_STROKE_WIDTH = 0.070f;
    private static final float DEFAULT_SECONDNEEDLES_STROKE_WIDTH = 0.050f;
    private static final float DEFAULT_HOURNEEDLES_STROKE_LENGTH = 0.30f;
    private static final float DEFAULT_MINUTENEEDLES_STROKE_LENGTH = 0.70f;
    private static final float DEFAULT_SECONDNEEDLES_STROKE_LENGTH = 0.90f;


    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    private int mWidth, mCenterX, mCenterY, mRadius;

    /**
     * properties
     */
    private int centerInnerColor;
    private int centerOuterColor;

    private int secondsNeedleColor;
    private int hoursNeedleColor;
    private int minutesNeedleColor;

    private int degreesColor;

    private int hoursValuesColor;

    private int numbersColor;

    private boolean mShowAnalog = true;

    public Clock(Context context) {
        super(context);
        init(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        size = Math.min(widthWithoutPadding, heightWithoutPadding);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    private void init(Context context, AttributeSet attrs) {

        this.centerInnerColor = Color.LTGRAY;
        this.centerOuterColor = DEFAULT_PRIMARY_COLOR;

        this.secondsNeedleColor = DEFAULT_SECONDARY_COLOR;
        this.hoursNeedleColor = DEFAULT_PRIMARY_COLOR;
        this.minutesNeedleColor = DEFAULT_PRIMARY_COLOR;

        this.degreesColor = DEFAULT_PRIMARY_COLOR;

        this.hoursValuesColor = DEFAULT_PRIMARY_COLOR;

        numbersColor = Color.WHITE;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        mWidth = Math.min(getWidth(), getHeight());

        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;

        if (mShowAnalog) {
            drawDegrees(canvas);
            drawHoursValues(canvas);
            drawNeedles(canvas);
            drawCenter(canvas);
        } else {
            drawNumbers(canvas);
        }
        postInvalidateDelayed(1000);
    }

    private void drawDegrees(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.05f);

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0)
                paint.setAlpha(CUSTOM_ALPHA);
            else {
                paint.setAlpha(FULL_ALPHA);
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));

            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
    }

    /**
     * @param canvas
     */
    private void drawNumbers(Canvas canvas) {

        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.2f);
        textPaint.setColor(numbersColor);
//        textPaint.setColor(numbersColor);
        textPaint.setAntiAlias(true);

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);

        String time = String.format("%s:%s:%s%s",
                String.format(Locale.getDefault(), "%02d", hour),
                String.format(Locale.getDefault(), "%02d", minute),
                String.format(Locale.getDefault(), "%02d", second),
                amPm == AM ? "AM" : "PM");

        SpannableStringBuilder spannableString = new SpannableStringBuilder(time);
        spannableString.setSpan(new RelativeSizeSpan(0.3f), spannableString.toString().length() - 2, spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // se superscript percent

        StaticLayout layout = new StaticLayout(spannableString, textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_CENTER, 1, 1, true);
        canvas.translate(mCenterX - layout.getWidth() / 2f, mCenterY - layout.getHeight() / 2f);
        layout.draw(canvas);
    }

    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(hoursValuesColor);
        textPaint.setTextSize(mRadius*0.1f);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        float distance = mRadius - mRadius*0.1f-textPaint.getTextSize();
        float x,y;
        String[] HoursValues = {"12","1","2","3","4","5","6","7","8","9","10","11"};

        for(int i = 0; i < 12; i++){
            x = (float)(distance * Math.sin(Math.toRadians(i*30)) + mCenterX);
            y = (float)(mCenterY - distance * Math.cos(Math.toRadians(i*30)));
            canvas.drawText(HoursValues[i],x,y+textPaint.getTextSize()/2,textPaint);
        }

    }

    /**
     * Draw hours, minutes needles
     * Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawNeedles(final Canvas canvas) {
        // Default Color:
        // - secondsNeedleColor
        // - hoursNeedleColor
        // - minutesNeedleColor
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        int hourAngle = hour * 30 + minute/2;
        int minuteAngle = minute * 6 + second/10;
        int secondAngle = second * 6;

        //设置hour指针的Paint信息
        Paint hourNeedles = new Paint(Paint.ANTI_ALIAS_FLAG);
        hourNeedles.setStyle(Paint.Style.FILL_AND_STROKE);
        hourNeedles.setStrokeCap(Paint.Cap.ROUND);
        hourNeedles.setStrokeWidth(mRadius*DEFAULT_HOURNEEDLES_STROKE_WIDTH);
        hourNeedles.setColor(hoursNeedleColor);
        //指针都是从中心点画出的，只需指定结束位置的坐标
        int hourEndX = (int)(mCenterX + (mRadius*DEFAULT_HOURNEEDLES_STROKE_LENGTH) * Math.sin(Math.toRadians(hourAngle)));
        int hourEndY = (int)(mCenterY - (mRadius*DEFAULT_HOURNEEDLES_STROKE_LENGTH) * Math.cos(Math.toRadians(hourAngle)));
        canvas.drawLine(mCenterX,mCenterY,hourEndX,hourEndY,hourNeedles);

        //设置minute指针的Paint信息
        Paint minuteNeedles = new Paint(Paint.ANTI_ALIAS_FLAG);
        minuteNeedles.setStyle(Paint.Style.FILL_AND_STROKE);
        minuteNeedles.setStrokeCap(Paint.Cap.ROUND);
        minuteNeedles.setStrokeWidth(mRadius*DEFAULT_MINUTENEEDLES_STROKE_WIDTH);
        minuteNeedles.setColor(minutesNeedleColor);
        //指针都是从中心点画出的，只需指定结束位置的坐标
        int minuteEndX = (int)(mCenterX + (mRadius*DEFAULT_MINUTENEEDLES_STROKE_LENGTH) * Math.sin(Math.toRadians(minuteAngle)));
        int minuteEndY = (int)(mCenterY - (mRadius*DEFAULT_MINUTENEEDLES_STROKE_LENGTH) * Math.cos(Math.toRadians(minuteAngle)));
        canvas.drawLine(mCenterX,mCenterY,minuteEndX,minuteEndY,minuteNeedles);

        //设置hour指针的Paint信息
        Paint secondNeedles = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondNeedles.setStyle(Paint.Style.FILL_AND_STROKE);
        secondNeedles.setStrokeCap(Paint.Cap.ROUND);
        secondNeedles.setStrokeWidth(mRadius*DEFAULT_SECONDNEEDLES_STROKE_WIDTH);
        secondNeedles.setColor(secondsNeedleColor);
        //指针都是从中心点画出的，只需指定结束位置的坐标
        int secondEndX = (int)(mCenterX + (mRadius*DEFAULT_SECONDNEEDLES_STROKE_LENGTH) * Math.sin(Math.toRadians(secondAngle)));
        int secondEndY = (int)(mCenterY - (mRadius*DEFAULT_SECONDNEEDLES_STROKE_LENGTH) * Math.cos(Math.toRadians(secondAngle)));
        canvas.drawLine(mCenterX,mCenterY,secondEndX,secondEndY,secondNeedles);
    }

    /**
     * Draw Center Dot
     *
     * @param canvas
     */
    private void drawCenter(Canvas canvas) {
        // Default Color:
        // - centerInnerColor
        // - centerOuterColor
        Paint paintInner = new Paint();
        paintInner.setColor(centerInnerColor);
        paintInner.setAntiAlias(true);

        Paint paintOuter = new Paint();
        paintOuter.setColor(centerOuterColor);
        paintOuter.setAntiAlias(true);
        paintOuter.setStyle(Paint.Style.STROKE);
        paintOuter.setStrokeWidth(mRadius*0.015f);

        canvas.drawCircle(mCenterX,mCenterY,mRadius*0.10f,paintInner);
        canvas.drawCircle(mCenterX,mCenterY,mRadius,paintOuter);

    }

    public void setShowAnalog(boolean showAnalog) {
        mShowAnalog = showAnalog;
        invalidate();
    }

    public boolean isShowAnalog() {
        return mShowAnalog;
    }

}