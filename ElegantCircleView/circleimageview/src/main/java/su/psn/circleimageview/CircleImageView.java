package su.psn.circleimageview;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * CircleImageView core codes.
 * Created by SUCONG on 2016/12/28.
 */

public class CircleImageView extends ImageView {

    // print tag.
    private final String TAG = "CircleImageView";

    // 默认shadow颜色
    private static final int DEFAULT_SHADOW_COLOR = Color.BLACK;

    // 默认边线的颜色
    private static final int DEFAULT_BORDER_COLOR = Color.WHITE;

    // 默认选中的边线颜色
    private static final int DEFAULT_BORDER_SELECTED_COLOR = Color.RED;

    private static final float DEFAULT_SHADOW_RADIUS = 0.0f;
    private static final float DEFAULT_SHADOW_DX = 0.0f;
    private static final float DEFAULT_SHADOW_DY = 0.0f;
    private static final int DEFAULT_BORDER_WIDTH = 0;

    private static final float DEFAULT_PRESSED_RING_WIDTH = 0.0f;
    private static final int DEFAULT_PRESSED_RING_COLOR = Color.CYAN;

    private static final int ANIMATION_TIME = android.R.integer.config_shortAnimTime;
    private static final String ANIMATION_PROGRESS_ATTR = "animationProgress";

    // 画布的大小
    private int mCanvasSize;

    // 带有shadow的半径大小
    private float mShadowRadius;

    // shadow的颜色
    private int mShadowColor;

    // ??
    private float mShadowDx;

    // ??
    private float mShadowDy;

    // ImageView上设置的Bitmap
    private Bitmap mImageBitmap;

    // ImageView上设置的Drawable
    private Drawable mDrawable;

    // 图片资源的画笔
    private Paint mPaintImage;

    // 边框的画笔
    private Paint mPaintBorder;

    // 背景的画笔
    private Paint mPaintBackground;

    // 边框的宽度
    private int mBorderWidth;

    // 边框的颜色
    private int mBorderColor;
    private int mBorderSelectedColor;

    // canvas的中心位置 x
    private int mCenterX;

    // canvas的中心位置 y
    private int mCenterY;

    // 绘制的半径
    private int mRadius;

    // private ItemSelectedListener mListener;
    private boolean mIsSelectedState;

    // View的背景颜色
    private int mBackgroundColor;

    // ??
    private float mAnimationProgress;

    private ObjectAnimator mPressedAnimator;
    private int mPressedRingColor;
    private int mPressedRingWidth;
    private int mCurrentPressedRingWidth;

    public CircleImageView(Context context) {
        this(context, null, R.styleable.CircleImageViewStyle_circleImageViewDefault);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, R.styleable.CircleImageViewStyle_circleImageViewDefault);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setClickable(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0);
        mBackgroundColor = a.getColor(R.styleable.CircleImageView_backgroundColor,
                context.getResources().getColor(android.R.color.transparent));
        mBorderColor = a.getColor(R.styleable.CircleImageView_borderColor, DEFAULT_BORDER_COLOR);
        mBorderWidth = a.getDimensionPixelOffset(R.styleable.CircleImageView_borderWidth, DEFAULT_BORDER_WIDTH);
        mBorderSelectedColor = a.getColor(R.styleable.CircleImageView_selectedColor, DEFAULT_BORDER_SELECTED_COLOR);
        mShadowRadius = a.getDimension(R.styleable.CircleImageView_shadowRadius, DEFAULT_SHADOW_RADIUS);
        mShadowColor = a.getColor(R.styleable.CircleImageView_shadowColor, DEFAULT_SHADOW_COLOR);
        mShadowDx = a.getDimension(R.styleable.CircleImageView_shadowDx, DEFAULT_SHADOW_DX);
        mShadowDy = a.getDimension(R.styleable.CircleImageView_shadowDy, DEFAULT_SHADOW_DY);
        mPressedRingWidth = (int) a.getDimension(R.styleable.CircleImageView_pressedRingWidth, DEFAULT_PRESSED_RING_WIDTH);
        mPressedRingColor = a.getColor(R.styleable.CircleImageView_pressedRingColor, DEFAULT_PRESSED_RING_COLOR);
        a.recycle();

        /* 设置动画 */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPressedAnimator = ObjectAnimator.ofFloat(this, ANIMATION_PROGRESS_ATTR, 0f, 0f);
            mPressedAnimator.setDuration(getResources().getInteger(ANIMATION_TIME));
        }

        /* 创建3个画笔 */
        mPaintImage = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);

        // 设置背景颜色
        setBackgroundColor(mBackgroundColor);

        // 设置边线颜色
        setBorderColor(mBorderColor);

        // 设置边线宽度
        setBorderWidth(mBorderWidth);

        // 绘制view的shadow
        drawShadow(mShadowRadius, mShadowColor, mShadowDx, mShadowDy);
    }

    private void drawShadow(float shadowRadius, int shadowColor, float shadowDx, float shadowDy) {
        if (mShadowRadius != shadowRadius) {
            this.mShadowRadius = shadowRadius;
        }
        if (mShadowColor != shadowColor) {
            this.mShadowColor = shadowColor;
        }
        if (mShadowDx != shadowDx) {
            this.mShadowDx = shadowDx;
        }
        if (mShadowDy != shadowDy) {
            this.mShadowDy = shadowDy;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, mPaintBorder);
        }
        mPaintBorder.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
    }

    /**
     * 设置边线宽度
     *
     * @param borderWidth 边线宽度
     */
    public void setBorderWidth(int borderWidth) {
        if (mBackgroundColor != mBorderWidth) {
            this.mBorderWidth = borderWidth;
        }

        requestLayout();
        invalidate();
    }

    /**
     * 设置边线的颜色
     *
     * @param borderColor 边线颜色
     */
    public void setBorderColor(int borderColor) {
        if (mPaintBorder != null) {
            mPaintBorder.setColor(borderColor);
        }

        invalidate();
    }

    /**
     * 设置View的背景颜色
     *
     * @param backgroundColor 背景颜色
     */
    public void setBackgroundColor(int backgroundColor) {
        if (mPaintBackground != null) {
            mPaintBackground.setColor(backgroundColor);
        }

        invalidate();
    }

    public void addShadow() {
        if (mShadowRadius == 0)
            mShadowRadius = DEFAULT_SHADOW_RADIUS;
        drawShadow(mShadowRadius, mShadowColor, mShadowDx, mShadowDy);
        invalidate();
    }

    public void setShadowRadius(float shadowRadius) {
        drawShadow(shadowRadius, mShadowColor, mShadowDx, mShadowDy);
        invalidate();
    }

    public void setShadowColor(int shadowColor) {
        drawShadow(mShadowRadius, shadowColor, mShadowDx, mShadowDy);
        invalidate();
    }

    public void setShadowDx(float shadowDx) {
        drawShadow(mShadowRadius, mShadowColor, shadowDx, mShadowDy);
        invalidate();
    }

    public void setShadowDy(float shadowDy) {
        drawShadow(mShadowRadius, mShadowColor, mShadowDx, shadowDy);
        invalidate();
    }

    // 1.first step: Measures the view and its children
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /* get measured width and height. */
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        Log.i(TAG, "onMeasuring and width is " + width + ", height is " + height);

        // the minimum of width or width will be the image size.
        int imageSize = (width < height) ? width : height;

        // 存储被测量的宽和高
        setMeasuredDimension(imageSize, imageSize);
    }

    private int measureWidth(int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else {
            result = mCanvasSize;
        }
        return result;
    }

    private int measureHeight(int measureSpecHeight) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = mCanvasSize;
        }
        return (result + 2);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i(TAG, "onLayout, View is changed?" + changed + ", and its four points are (ltrb)" + left + "_" + top + "_" + right + "_" + bottom + ".");
        super.onLayout(changed, left, top, right, bottom);
    }

    // 3.onSizeChanged
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i(TAG, "onSizedChanged, and the former is " + oldw + "_" + oldh + ", the latter is " + w + "_" + h);
        super.onSizeChanged(w, h, oldw, oldh);

        // get min value from w and h as Canvas size.
        mCanvasSize = Math.min(w, h);

        // circle view radius is 0.
        mRadius = 0;

        if (mImageBitmap != null)
            updateShader();
    }

    private void updateShader() {
        if (this.mImageBitmap == null) {
            return;
        }

        // 创建一个Matrix矩阵实现图像的变化
        Matrix matrix = new Matrix();

        // 剪裁bitmap
        mImageBitmap = cropBitmap(mImageBitmap);

        // ??? BitmapShader是个什么鬼
        BitmapShader shader = new BitmapShader(mImageBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        RectF bitmapRect = new RectF(
                0,
                0,
                mImageBitmap.getWidth(),
                mImageBitmap.getHeight()
        );

        RectF viewRect = new RectF(
                0,
                0,
                mCanvasSize - Utils.twiceValue(Utils.getBorderMax(mPressedRingWidth, mBorderWidth)),
                mCanvasSize - Utils.twiceValue(Utils.getBorderMax(mPressedRingWidth, mBorderWidth))
        );

        matrix.setRectToRect(bitmapRect, viewRect, Matrix.ScaleToFit.CENTER);
        matrix.postTranslate(
                Utils.getBorderMax(mPressedRingWidth, mBorderWidth) + getPaddingLeft(),
                Utils.getBorderMax(mPressedRingWidth, mBorderWidth) + getPaddingTop()
        );

        shader.setLocalMatrix(matrix);
        mPaintImage.setShader(shader);
    }

    /**
     * 对现有的bitmap进行裁切。
     *
     * @param bitmap source bitmap
     * @return 剪裁后的new bitmap
     */
    private Bitmap cropBitmap(Bitmap bitmap) {
        Bitmap bmp;
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            bmp = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                    // 0,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight());
        } else {
            bmp = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                    bitmap.getWidth(),
                    bitmap.getWidth());
        }
        return bmp;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Log.i(TAG, "Canvas is being drawn...");

        initImage();

        if (mImageBitmap == null) {
            return;
        }

        calculateCircleData(canvas);

        canvas.drawCircle(
                mCenterX + mBorderWidth,
                mCenterY + mBorderWidth,
                mRadius + mBorderWidth - (mShadowRadius + mShadowRadius) / 2 + mAnimationProgress,
                mPaintBorder);

        canvas.drawCircle(
                mCenterX + mBorderWidth,
                mCenterY + mBorderWidth,
                mRadius - (mShadowRadius + mShadowRadius / 2),
                mPaintBackground);

        canvas.drawCircle(
                mCenterX + mBorderWidth,
                mCenterY + mBorderWidth,
                mRadius - (mShadowRadius + mShadowRadius / 2),
                mPaintImage
        );
    }

    private void initImage() {
        if (this.mDrawable == getDrawable()) {
            return;
        }

        this.mDrawable = getDrawable();
        this.mImageBitmap = Utils.drawableToBitmap(this.mDrawable);
    }

    private void calculateCircleData(Canvas canvas) {
        if (mRadius != 0) {
            return;
        }

        if (!isInEditMode()) {
            mCanvasSize = canvas.getWidth() - getPaddingLeft() - getPaddingRight() - Utils.twiceValue(Utils.getBorderMax(mPressedRingWidth, mBorderWidth));
            boolean isHeightSmallerThanCanvas =
                    canvas.getHeight() - getPaddingTop() - getPaddingBottom() - Utils.twiceValue(Utils.getBorderMax(mPressedRingWidth, mBorderWidth)) < mCanvasSize;
            if (isHeightSmallerThanCanvas) {
                mCanvasSize = canvas.getHeight() - getPaddingTop() - getPaddingBottom() - Utils.twiceValue(Utils.getBorderMax(mPressedRingWidth, mBorderWidth));
            }
        }

        // 计算半径
        mRadius = (mCanvasSize - (Utils.getBorderMax(mPressedRingWidth, mBorderWidth) * 2)) / 2;

        // 计算中心位置
        mCenterX = getPaddingLeft() + mRadius + Math.abs(Utils.getBorderMax(mPressedRingWidth, mBorderWidth));
        mCenterY = getPaddingTop() + mRadius + Math.abs(Utils.getBorderMax(mPressedRingWidth, mBorderWidth));
        // mCenterX = getPaddingLeft() + mRadius + mBorderWidth;
        // mCenterY = canvas.getHeight() / 2;

        // 更新shader
        updateShader();
    }
}
