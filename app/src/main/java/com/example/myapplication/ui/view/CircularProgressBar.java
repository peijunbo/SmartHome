package com.example.myapplication.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class CircularProgressBar extends View {
    public static final int MAX_PROGRESS = 100;
    public static final int MIN_PROGRESS = 0;
    private static final int MAX_ARC_POSITION = 240;
    private static final int START_ARC_POSITION = 120;
    private static final int NO_CENTER_ICON = -1;
    private static final int DEFAULT_SURFACE_COLOR = 0xFF509CFE;
    private int progress = 0;

    private final Paint paint = new Paint();
    private final Paint shadowPaint = new Paint();
    private final Paint textPaint = new TextPaint();
    private final Paint linePaint = new Paint();
    private final Matrix rotate = new Matrix();
    private final int defaultPadding = 16;

    /**
     * 背景色，外围圆环色
     */
    private int outerColor = Color.WHITE;
    /**
     * 内部阴影
     */
    private int innerElevation;
    /**
     * 主标题
     */
    private String upperTitle = "";

    /**
     * 副标题
     */
    private String lowerTitle = "";
    /**
     * 中心图标
     */
    private Drawable centerIcon;
    /**
     * 中心图标颜色
     */
    private int centerIconTint = 0xFFFFFFFF;

    private int surfaceColor = DEFAULT_SURFACE_COLOR;//中心圆以及外部进度线色彩
    private int onSurfaceColor = Color.WHITE; // 文字颜色
    private boolean activated = true;

    public CircularProgressBar(Context context) {
        super(context);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadAttr(context, attrs);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttr(context, attrs);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadAttr(context, attrs);
    }

    private void loadAttr(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0);
        innerElevation = a.getInt(R.styleable.CircularProgressBar_innerElevation, 8);
        upperTitle = a.getString(R.styleable.CircularProgressBar_upperTitle);
        if (upperTitle == null) upperTitle = "";
        lowerTitle = a.getString(R.styleable.CircularProgressBar_lowerTitle);
        if (lowerTitle == null) lowerTitle = "";
        centerIcon = a.getDrawable(R.styleable.CircularProgressBar_centerIcon);
        centerIconTint = a.getColor(R.styleable.CircularProgressBar_centerIconTint, Color.WHITE);
        surfaceColor = a.getColor(R.styleable.CircularProgressBar_surfaceColor, DEFAULT_SURFACE_COLOR);
        progress = a.getInt(R.styleable.CircularProgressBar_android_progress, 0);
        activated = a.getBoolean(R.styleable.CircularProgressBar_activated, true);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        width = width - paddingLeft - paddingRight - defaultPadding;
        height = height - paddingTop - paddingBottom - defaultPadding;
        float cx = width / 2 + paddingLeft + defaultPadding; // 圆心位置
        float cy = height / 2 + paddingTop + defaultPadding;
        float min = Math.min(width, height); // 宽高最小值为准保证画在范围内

        float outerWidth = min / 12; // 背景与中心圆的半径差值
        float strokeWidth = outerWidth / 3; // 线宽
        float radius = min / 2 - strokeWidth / 2; // 减去线宽
        // 绘制两个圆
        shadowPaint.setShadowLayer(innerElevation, 0, 0, Color.LTGRAY);
        shadowPaint.setColor(Color.TRANSPARENT);
        shadowPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx, cy, min / 2, shadowPaint); // 减三是为了让颜色最深的部分不显示

        paint.setAntiAlias(true); // 抗锯齿
        paint.setStyle(Paint.Style.FILL); // 填充
        paint.setColor(outerColor); // 背景色
        canvas.drawCircle(cx, cy, min / 2, paint);
        paint.setColor(surfaceColor);
        canvas.drawCircle(cx, cy, min / 2 - outerWidth, paint);

        // 绘制进度线
        float arcPosition = (progress) / 100f * MAX_ARC_POSITION; // 将0~100的progress转化为0~max度的角
        float gradientStartPos = arcPosition;
        int[] gradientColors = new int[]{Color.TRANSPARENT, surfaceColor};
        float[] positions = new float[]{ arcPosition / 360.0f, (arcPosition + 60) / 360.0f};
        // float[] positions = new float[]{ 0f, 1f};
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            SweepGradient sweepGradient = new SweepGradient(cx, cy, gradientColors, positions);
            rotate.setRotate(START_ARC_POSITION, cx, cy);
            sweepGradient.setLocalMatrix(rotate);
            linePaint.setShader(sweepGradient);
        }
        linePaint.setStyle(Paint.Style.STROKE); // 空心
        linePaint.setStrokeWidth(strokeWidth);
        linePaint.setColor(surfaceColor);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        RectF rectF = new RectF(cx - radius, cy - radius, cx + radius, cy + radius);
        canvas.drawArc(rectF, arcPosition + START_ARC_POSITION + 5, 60, false, linePaint);
        // 绘制进度点


        // 绘制文本
        float textSize = min / 8;
        float subTextSize = min / 10;
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(onSurfaceColor);
        textPaint.setTextSize(textSize);
        canvas.drawText(upperTitle, cx, cy - textSize / 1.5f, textPaint);
        textPaint.setTextSize(subTextSize);
        canvas.drawText(lowerTitle, cx, cy + textSize, textPaint);

        // 绘制中心图标
        if (centerIcon != null) {
            centerIcon.setTint(centerIconTint);
            centerIcon.setAlpha((int) ((float) arcPosition / MAX_ARC_POSITION * 0xff)); // 计算透明度
            centerIcon.setBounds((int) (cx - radius + outerWidth), (int) (cy - radius + outerWidth), (int) (cx + radius - outerWidth), (int) (cy + radius - outerWidth));
            centerIcon.draw(canvas);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setSurfaceColor(int surfaceColor) {
        this.surfaceColor = surfaceColor;
        invalidate();
    }

    public void setOnSurfaceColor(int onSurfaceColor) {
        this.onSurfaceColor = onSurfaceColor;
        invalidate();
    }

    public void setOuterColor(int outerColor) {
        this.outerColor = outerColor;
        invalidate();
    }

    public void setInnerElevation(int innerElevation) {
        this.innerElevation = innerElevation;
        invalidate();
    }

    public void setUpperTitle(String upperTitle) {
        this.upperTitle = upperTitle;
        invalidate();
    }

    public void setLowerTitle(String lowerTitle) {
        this.lowerTitle = lowerTitle;
        invalidate();
    }
}
