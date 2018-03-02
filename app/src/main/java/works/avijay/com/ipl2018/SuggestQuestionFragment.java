package works.avijay.com.ipl2018;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import works.avijay.com.ipl2018.helper.BackendHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestQuestionFragment extends Fragment {


    public SuggestQuestionFragment() {
        // Required empty public constructor
    }

    View view;
    EditText question;
    Button askButton;
    String card_question;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_suggest_question, container, false);
        question = view.findViewById(R.id.question);
        askButton = view.findViewById(R.id.askButton);

        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(isNetworkConnected()){
                card_question = question.getText().toString();
                if(card_question.length() > 0){
                    BackendHelper.ask_question ask_question = new BackendHelper.ask_question();
                    ask_question.execute(context, card_question);
                    Snackbar.make(view, "Okay, We heard you!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            
                        }
                    }, 500);


                }else {
                    Snackbar.make(view, "Only valid questions Please!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }

            }else{
                Snackbar.make(view, "Oops, No Internet connection!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

            }
        });

        return view;
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
