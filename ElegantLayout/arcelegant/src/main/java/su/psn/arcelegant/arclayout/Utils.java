package su.psn.arcelegant.arclayout;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Utils class.
 * Created by A-SC on 2016/12/23.
 */
class Utils {

    private Utils() {
    }

    /**
     * Convert dp 2 px.
     *
     * @param context context
     * @param dp      dp value
     * @return px value
     */
    static float dpToPx(Context context, int dp) {
        Resources resources = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
