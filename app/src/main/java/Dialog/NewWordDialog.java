package Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.study.xps.projectdictionary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;

import Adapters.TopicsSpinnerAdapter;
import Models.Language;
import Models.Tags;
import Models.Topic;
import Models.Word;
import activities.TopicsActivity;
import activities.WordsActivity;

/**
 * Created by XPS on 4/26/2016.
 */
public class NewWordDialog extends DialogFragment {

    final String SERVICE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
    final String API_KEY = "trnsl.1.1.20160509T111252Z.507a5ce07b028de4.6b97d5b3cab733a6906bf0acf91baa3320b7f9e8";
    final String ENCODING = "UTF-8";
    final String PARAM_API_KEY = "key=",
            PARAM_LANG_PAIR = "&lang=",
            PARAM_TEXT = "&text=";


    private TextView okButton;
    private TextView cancelButton;
    private EditText valueTextBox;
    private AutoCompleteTextView translationTextBox;
    private long wordID;

    private boolean edit;
    private boolean isOnline = false;
    private RequestQueue requestQueue;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //getDialog().setTitle("New topic");
        requestQueue = Volley.newRequestQueue(getActivity());
        Bundle bundle = getArguments();
        IsOnlineTask onlineStatus = new IsOnlineTask();
        onlineStatus.execute();
        final Word editWord;
        try {
            edit = bundle.getBoolean(Tags.SUCCESS_QUERY_TAG);
            wordID = bundle.getLong(Tags.WORD_VALUE_TAG);
        } catch (Exception e){
            edit = false;
            wordID = 0;
        }
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final View topicDialog = inflater.inflate(R.layout.dialog_word_add,null);

        okButton = (TextView) topicDialog.findViewById(R.id.newWordOK);
        cancelButton = (TextView) topicDialog.findViewById(R.id.newWordCancel);
        valueTextBox = (EditText) topicDialog.findViewById(R.id.wordTextBox);
        translationTextBox = (AutoCompleteTextView) topicDialog.findViewById(R.id.translationTextBox);

        valueTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String translation = getTranslation(s.toString());
                if(translation != null)
                Log.i("###### TRANSLATION ", translation);
            }
        });

        if(edit) {
            editWord = Word.findById(Word.class, wordID);
            valueTextBox.setText(editWord.getValue());
            translationTextBox.setText(editWord.getTranslation());
        } else editWord = new Word();

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        translationTextBox.setFilters(new InputFilter[] { filter });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordsActivity parent = (WordsActivity) getActivity();
                if ((!valueTextBox.getText().toString().equals("")) && (!translationTextBox.getText().toString().equals(""))){

                        if(!edit)
                        new Word(WordsActivity.currentTopicId,valueTextBox.getText().toString(),translationTextBox.getText().toString()).save();
                        if (edit){
                            editWord.setValue(valueTextBox.getText().toString());
                            editWord.setTranslation(translationTextBox.getText().toString());
                            editWord.save();
                        }
                } else return;

                        parent.updateData();
                        //parent.loadAppropriateFragment();
                        //parent.updateViewData();
                        dismiss();
                    }
                });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return topicDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private String getTranslation(String word){
        String result;
        String restURL;

        if (isOnline){
            try {
                String fromLan = Language.UKRAINIAN.toString();
                String toLan   = Language.ENGLISH  .toString();

                restURL = SERVICE_URL + PARAM_API_KEY + URLEncoder.encode(API_KEY,ENCODING)
                        + PARAM_LANG_PAIR + URLEncoder.encode(fromLan,ENCODING) + URLEncoder.encode("-",ENCODING)
                        + URLEncoder.encode(toLan,ENCODING)
                        + PARAM_TEXT + URLEncoder.encode(word,ENCODING);
                Log.i("REQUEST URL ", restURL);
                }
                catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            result = fetchJsonResponse(restURL);
        }
        else {
            Toast.makeText(getActivity(),"OFFLINE", Toast.LENGTH_SHORT).show();
            result = null;
        }
       return result;
    }

private String fetchJsonResponse(String url){

    final String[] translation = new String[1];

        JsonObjectRequest request = new JsonObjectRequest(url, null,
        new Response.Listener<JSONObject>() {
@Override
public void onResponse(JSONObject response) {
        String result = null;
        try {
        Log.i("RESPONCE",response.toString());
        result = response.getString("text");
        translation[0] = result.replaceAll("[\\[\\]\"]","");
            Log.i("###### TRANSLATION ", translation[0]);

            if(translation[0] != null){
            ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,translation);
            translationTextBox.setAdapter(adapter);
            translationTextBox.setThreshold(1);
            translationTextBox.showDropDown();}

        } catch (JSONException e) {
        Toast.makeText(getActivity(), "TRANSLATING ERROR", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
        }
        }},
        new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {
        VolleyLog.e(error.getMessage());
        }});
        requestQueue.add(request);
    return translation[0];
}

    private class IsOnlineTask extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... params) {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            } catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isOnline = aBoolean;
        }
    }
}

