package works.avijay.com.ipl2018.fonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by avinashk on 28/02/18.
 */

@SuppressLint("AppCompatCustomView")
public class description_font extends TextView {

    public description_font(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public description_font(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public description_font(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/baloo_tamma_regular.ttf" );
        setTypeface(tf);
    }

}