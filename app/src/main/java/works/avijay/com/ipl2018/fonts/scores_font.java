package works.avijay.com.ipl2018.fonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class scores_font extends TextView {

    public scores_font(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public scores_font(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public scores_font(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/ConcertOne-Regular.ttf" );
        setTypeface(tf);
    }

}