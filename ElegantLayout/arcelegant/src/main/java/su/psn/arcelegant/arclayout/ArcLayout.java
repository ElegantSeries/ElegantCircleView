package su.psn.arcelegant.arclayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

/**
 * ArcLayout for learning.
 * Thanks to Git project ArcLayout.
 * <p>
 * Created by A-SC on 2016/12/23.
 */

public class ArcLayout extends FrameLayout {

    private final String TAG = "ArcLayout";

    // arc settings object.
    private ArcLayoutSettings settings;

    // layout height.
    private int height = 0;

    // layout width.
    private int width = 0;

    // curve path
    private Path curvePath;

    public ArcLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ArcLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        settings = new ArcLayoutSettings(context, attrs);
        settings.setElevation(ViewCompat.getElevation(this));

        // TODO: 2016/12/23 I don't know it means what. to find out answer.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    /**
     * create curve path with some settings value.
     *
     * @return a curve path.
     */
    private Path createClipPath() {
        // create a new path
        final Path path = new Path();

        // get default arc height or height of user settings.
        float arcHeight = settings.getArcHeight();

        // direction is bottom
        if (settings.isDirectionBottom()) {
            // ( convex
            if (settings.isCropConvex()) {
                path.moveTo(0, 0);
                path.lineTo(0, height - arcHeight);
                path.quadTo(width / 2, height + arcHeight, width, height - arcHeight);
                path.lineTo(width, 0);
                path.close();
            } else {
                // ) concave
                path.moveTo(0, 0);
                path.lineTo(0, height);
                path.quadTo(width / 2, height - 2 * arcHeight, width, height);
                path.lineTo(width, 0);
                path.close();
            }
        } else {
            if (settings.isCropConvex()) {
                path.moveTo(0, arcHeight);
                path.lineTo(0, height);
                path.lineTo(width, height);
                path.lineTo(width, arcHeight);
                path.quadTo(width / 2, (-1 * arcHeight), 0, arcHeight);
                path.close();
            } else {
                path.moveTo(0, 0);
                path.lineTo(0, height);
                path.lineTo(width, height);
                path.lineTo(width, 0);
                path.quadTo(width / 2, 2 * arcHeight, 0, 0);
                path.close();
            }
        }

        return path;
    }

    /**
     * The biggest issue is path can only be set on LOLLIPOP or higher version.
     * min sdk > 14 limits.
     */
    private void calculateLayout() {
        if (settings == null)
            return;

        /* get measured width and height of arc layout that the layout wants to be. */
        height = getMeasuredHeight();
        width = getMeasuredWidth();

        if (width > 0 && height > 0) {
            curvePath = createClipPath();

            // set elevation
            ViewCompat.setElevation(this, settings.getElevation());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    && settings.isCropConvex()) {
                ViewCompat.setElevation(this, settings.getElevation());
                setOutlineProvider(new ViewOutlineProvider() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void getOutline(View view, Outline outline) {
                        // use ViewOutlineProvider to clip view outline.
                        // default view outline is the background of view.
                        outline.setConvexPath(curvePath);
                    }
                });
            }
        }
    }

    public ArcLayoutSettings getSettings() {
        return settings;
    }

    public void setSettings(final ArcLayoutSettings settings) {
        this.settings = settings;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.i(TAG, "ArcLayout is dispatching draw.");
        if (canvas != null) {
            // first saving
            canvas.save();

            canvas.clipPath(curvePath);
            super.dispatchDraw(canvas);

            // last restoring
            canvas.restore();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i(TAG, "ArcLayout is laying out and layout is changed? " + changed);
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            // if view changed, re-calculate view again.
            calculateLayout();
        }
    }
}
