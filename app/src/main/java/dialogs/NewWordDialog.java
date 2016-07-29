package dialogs;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.study.xps.projectdictionary.R;

import helpers.GlobalStorage;
import models.Languages;
import models.Tags;
import models.Word;
import activities.WordsActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by XPS on 4/26/2016.
 */
public class NewWordDialog extends AppCompatDialogFragment {

    private final String SERVICE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
    private final String API_KEY =
            "trnsl.1.1.20160509T111252Z.507a5ce07b028de4.6b97d5b3cab733a6906bf0acf91baa3320b7f9e8";
    private final String ENCODING = "UTF-8";
    private final String PARAM_API_KEY = "key=";
    private final String PARAM_LANG_PAIR = "&lang=";
    private final String PARAM_TEXT = "&text=";
    private final long TIMEOUT = 500;

    private TextView mOkView;
    private TextView mCancelView;
    private EditText mWordTextEdit;
    private AutoCompleteTextView mTranslationTextView;
    private Word mEditWord;
    private Boolean mIsOnline = false;
    private boolean mIsUpdate;
    private long mWordId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        new IsOnlineTask().execute();

        try {
            mIsUpdate = bundle.getBoolean(Tags.SUCCESS_QUERY_TAG);
            mWordId = bundle.getLong(Tags.WORD_VALUE_TAG);
        } catch (Exception e){
            mIsUpdate = false;
            mWordId = 0;
        }

       return prepareDialog(inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private View prepareDialog(LayoutInflater inflater) {

        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        View topicDialog = inflater.inflate(R.layout.dialog_word_add,null);
        mOkView = (TextView) topicDialog.findViewById(R.id.newWordOK);
        mCancelView = (TextView) topicDialog.findViewById(R.id.newWordCancel);
        mWordTextEdit = (EditText) topicDialog.findViewById(R.id.wordTextBox);
        mTranslationTextView = (AutoCompleteTextView)
                topicDialog.findViewById(R.id.translationTextBox);

        mWordTextEdit.addTextChangedListener(mWordValueWatcher);

        if(mIsUpdate) {
            mEditWord = Word.findById(Word.class, mWordId);
            String wordValue = mEditWord.getValue();
            mWordTextEdit.setText(wordValue);
            mWordTextEdit.setSelection(wordValue.length());
            String translationValue = mEditWord.getTranslation();
            mTranslationTextView.setText(translationValue);
            mTranslationTextView.setSelection(translationValue.length());
        } else mEditWord = new Word();

       /* InputFilter filter = new InputFilter() {
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
        mTranslationTextView.setFilters(new InputFilter[] { filter });*/

        mOkView.setOnClickListener(mOkClickListener);
        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return topicDialog;
    }

    private View.OnClickListener mOkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WordsActivity parent = (WordsActivity) getActivity();
            if ((!mWordTextEdit.getText().toString().equals("")) &&
                    (!mTranslationTextView.getText().toString().equals(""))){

                if(!mIsUpdate) {
                    long currentTopicId = GlobalStorage.getStorage().getCurrentTopicId();
                    new Word(currentTopicId, mWordTextEdit.getText().toString(),
                            mTranslationTextView.getText().toString()).save();
                }
                if (mIsUpdate){
                    mEditWord.setValue(mWordTextEdit.getText().toString());
                    mEditWord.setTranslation(mTranslationTextView.getText().toString());
                    mEditWord.save();
                }
            } else return;

            parent.updateData();
            dismiss();
        }
    };

    private TextWatcher mWordValueWatcher = new TextWatcher() {
        private DelayedTranslation mDelayedTranslationTask;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            if(mDelayedTranslationTask != null){
                mDelayedTranslationTask.cancel(true);
            }
            mDelayedTranslationTask = new DelayedTranslation(s.toString());
            mDelayedTranslationTask.execute();
        }
    };

    private void getTranslationHelp(String word){
        String restURL = null;

        if (mIsOnline){
            try {
                String fromLan = Languages.UKRAINIAN.toString();
                String toLan   = Languages.ENGLISH  .toString();

                restURL = SERVICE_URL + PARAM_API_KEY + URLEncoder.encode(API_KEY,ENCODING)
                        + PARAM_LANG_PAIR + URLEncoder.encode(fromLan,ENCODING)
                        + URLEncoder.encode("-",ENCODING)
                        + URLEncoder.encode(toLan,ENCODING)
                        + PARAM_TEXT + URLEncoder.encode(word,ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            fetchJsonResponse(restURL);
        } else {
            //Toast.makeText(getActivity(),
            // getString(R.string.device_offline), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchJsonResponse(String url){
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());;
        JsonObjectRequest request = new JsonObjectRequest(url, null, mTranslateResponce,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            VolleyLog.e(error.getMessage());
                        } catch (NullPointerException e){
                            VolleyLog.e("Error getting message");
                        }

                    }
                });
        mRequestQueue.add(request);
    }

    private Response.Listener<JSONObject> mTranslateResponce =
            new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            String [] translation = new String[1];
            try {
                String result = response.getString("text");
                translation[0] = result.replaceAll("[\\[\\]\"]","");
                Log.i("###### TRANSLATION ", translation[0]);

                if(translation[0] != null){
                    ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                            android.R.layout.simple_list_item_1,translation);
                    mTranslationTextView.setAdapter(adapter);
                    mTranslationTextView.setThreshold(1);
                    mTranslationTextView.showDropDown();
                }
            } catch (JSONException e) {
                    Toast.makeText(getActivity(), "TRANSLATING ERROR",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };

    private class IsOnlineTask extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int exitValue = ipProcess.waitFor();
                return (exitValue == 0);
            } catch (IOException e) {
                e.printStackTrace(); }
            catch (InterruptedException e) {
                e.printStackTrace(); }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mIsOnline = aBoolean.booleanValue();
        }
    }

    private class DelayedTranslation extends AsyncTask {
        private String mText;
        DelayedTranslation(String text){
            this.mText = text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            int timeCount = 0;
            while (timeCount <= TIMEOUT) {
                try {
                    Thread.sleep(100);
                    timeCount+=100;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isCancelled()) break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            getTranslationHelp(mText);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.e(Tags.LOG_TAG,"CANCELED");
        }
    }
}

