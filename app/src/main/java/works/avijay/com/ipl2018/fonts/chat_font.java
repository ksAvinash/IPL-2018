package works.avijay.com.ipl2018.fonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class chat_font extends TextView {

    public chat_font(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public chat_font(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public chat_font(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/QuattrocentoSans-Regular.ttf" );
        setTypeface(tf);
    }

}