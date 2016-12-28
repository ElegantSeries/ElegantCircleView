package su.psn.arcelegant.arclayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import su.psn.arcelegant.R;

/**
 * Learned and copied from git project.
 * ArcLayoutSettings is a file for layout settings.
 * It configs some for arc layout.
 * <p>
 * Created by A-SC on 2016/12/23.
 */

public final class ArcLayoutSettings {
    private static final String TAG = "ArcLayoutSettings";

    /**
     * crop_convex for attrs enum cropConvex
     * value is 1
     */
    public static final int CROP_CONVEX = 1;

    /**
     * crop_convex for attrs enum cropConcave
     * value is 2
     */
    public static final int CROP_CONCAVE = 2;

    /**
     * Arc position BTM
     * value is 1
     */
    public static final int DIRECTION_BOTTOM = 1;

    /**
     * Arc position TOP
     * value is 2
     */
    public static final int DIRECTION_TOP = 2;

    /**
     * Default height for an arc.
     */
    private static final int DEFAULT_ARC_HEIGHT = 32;

    private boolean cropConvex = true;

    private boolean directionBottom = true;

    // arc height for settings any value.
    private float arcHeight;

    // elevation for arc shadow.
    private float elevation;

    /**
     * In constructor, do some initials.
     *
     * @param context context
     * @param attrs   attribute set
     */
    ArcLayoutSettings(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcHeader, 0, 0);
        arcHeight = a.getDimension(R.styleable.ArcHeader_arc_height, Utils.dpToPx(context, DEFAULT_ARC_HEIGHT));

        // get crop curve or convex. default is convex.
        final int cropDirection = a.getInt(R.styleable.ArcHeader_arc_cropCurve, CROP_CONVEX);
        cropConvex = (cropDirection & CROP_CONVEX) == CROP_CONVEX;

        final int arcPosition = a.getInt(R.styleable.ArcHeader_arc_position, DIRECTION_BOTTOM);
        directionBottom = (arcPosition & DIRECTION_BOTTOM) == DIRECTION_BOTTOM;

        // recycle
        a.recycle();
    }

    public float getElevation() {
        return this.elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public boolean isCropConvex() {
        return cropConvex;
    }

    public void setIsCropConvex(boolean cropConvex) {
        this.cropConvex = cropConvex;
    }

    public boolean isDirectionBottom() {
        return directionBottom;
    }

    public void setDirectionBottom(boolean directionBottom) {
        this.directionBottom = directionBottom;
    }

    public float getArcHeight() {
        return arcHeight;
    }

    public void setArcHeight(float arcHeight) {
        this.arcHeight = arcHeight;
    }
}
