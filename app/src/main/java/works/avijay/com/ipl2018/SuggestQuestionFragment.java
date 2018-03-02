package works.avijay.com.ipl2018;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
    String email;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_suggest_question, container, false);
        question = view.findViewById(R.id.question);
        askButton = view.findViewById(R.id.askButton);

        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSignedIn()){
                    if(isNetworkConnected()){
                        BackendHelper.ask_question ask_question = new BackendHelper.ask_question();
                        ask_question.execute(context, question.getText().toString(), email);
                    }else{
                        Snackbar.make(view, "Oops, No Internet connection!", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }

                }else{
                    Snackbar.make(view, "Sorry!\n You have't signedIn", Snackbar.LENGTH_SHORT)
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

    boolean isSignedIn(){
        return false;
    }

}
