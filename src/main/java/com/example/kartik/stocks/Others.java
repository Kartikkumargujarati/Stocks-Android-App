package com.example.archana.stocks;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
/**
 * @author Kartik
 *
 */


public class Others extends Fragment  {
    private OnFragmentInteractionListener mListener;
    TextView textView;
    EditText editText;
    Button button;
    TextView textView2;

    final String URL1 = "http://query.yahooapis.com/v1/public/yql?q=select%20LastTradePriceOnly%20from%20yahoo.finance.quote%20where%20symbol=%22";
    final String URL2 = "%22&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
    String sname;
    String[][] xmlPullParserArray= {{"LastTradePriceOnly", "0"},{"DaysRange", "0"}};
    private String TAG="STOCK";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_others, container, false);
         textView = (TextView) rootView.findViewById(R.id.textView);
         editText = (EditText) rootView.findViewById(R.id.editText);
         button = (Button) rootView.findViewById(R.id.button);

         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sname = editText.getText().toString();
                 final String yqlurl = URL1 + sname + URL2;

                 new task().execute(yqlurl);

             }
         });

         return rootView;
    }
    private class task extends AsyncTask< String,String,String> {


        @Override
        protected String doInBackground(String... args) {
            try {
                Log.d("TEST", "IN XMLPULLPARSER");
                XmlPullParserFactory xpf = XmlPullParserFactory.newInstance();
                xpf.setNamespaceAware(true);
                XmlPullParser xp = xpf.newPullParser();
                xp.setInput(new InputStreamReader(getUrlData(args[0])));
                Log.d("TEST", "Before begin");
                beginDocument(xp,"quote "  );
                Log.d("TEST", "After begin");

                int eventType=xp.getEventType();
                do {
                    nextElement(xp);
                    xp.next();
                    eventType = xp.getEventType();
                    Log.d("TEST", "Before event");
                    if (eventType == XmlPullParser.TEXT) {
                        Log.d("Test","Inside XML" );
                        String valueFromXML = xp.getText();
                        Log.d("Test","Inside get text" );
                        xmlPullParserArray[1][1] = valueFromXML;
                        Log.d("Test","After getting data" );
                    }
                    Log.d("Test","Before while" );
                } while (eventType != XmlPullParser.END_DOCUMENT);
                Log.d("Test","After while" );
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }
        public InputStream getUrlData(String url) throws URISyntaxException, IOException {
            Log.d("URL", url);
            URL u = new URL(url);
            URLConnection connect;
            connect = u.openConnection();
            HttpURLConnection hc = (HttpURLConnection) connect;
            int rescode = hc.getResponseCode();
            if (rescode == HttpURLConnection.HTTP_OK) {
                InputStream in = hc.getInputStream();
                Log.d("URLDONE",url);
                return in;
            }((HttpURLConnection) connect).disconnect();
            return null;
        }
        private void beginDocument(XmlPullParser xp, String abc ) throws XmlPullParserException, IOException {
            int type;
            Log.d("abc","in begin");
            while ((type = xp.next()) != xp.START_TAG && type != xp.END_DOCUMENT) {
            }
            if(type!= xp.START_TAG)
                if (!xp.getName().equalsIgnoreCase(abc )) {
                    throw new XmlPullParserException("Unexpected start tag"+ xp.getName()+",expected"+abc);
                }
        }
        public final void nextElement(XmlPullParser xp)throws XmlPullParserException, IOException{
            int type;
            Log.d("cba","in Next element");
            while ((type=xp.next())!= xp.START_TAG && type != xp.END_DOCUMENT){;
            }
        }
        @Override
        public void onPostExecute(String result) {
            Log.d("TEST", "IN xmlPullParserArray");
            editText.setText("");
            someEventListener.someEvent("Stock Price of " + sname + " " + xmlPullParserArray[1][1]);
            //textView2.setText("Stock Price of "+sname+" " + xmlPullParserArray[1][1] );
            Log.d("TEST", "result");
        }
    }
    public interface frag{
        public void someEvent(String s);
    }
    frag someEventListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            someEventListener = (frag) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSomeEventListener");
        }
    }
    final String LOG_TAG = "myLogs";

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
