    package com.example.archana.stocks;

    import android.app.Fragment;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentTransaction;
    import android.content.Intent;
    import android.net.Uri;
    import android.os.AsyncTask;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
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
    public class MainActivity extends AppCompatActivity implements Others.frag {

        final String URL1 = "http://query.yahooapis.com/v1/public/yql?q=select%20LastTradePriceOnly%20from%20yahoo.finance.quote%20where%20symbol=%22";
        final String URL2 = "%22&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

        TextView textView;
        EditText editText;
        Button button;
        TextView textView4;
        Button button2;
        Button button1;
        Button button3;
        Button button4;
        String KEY_LAST_TRADE_PRICE ="LastTradePriceOnly";
        String KEY_DAYS_RANGE = "DaysRange";

        String[][] xmlPullParserArray= {{"LastTradePriceOnly", "0"},{"DaysRange", "0"}};
        String sname;
        int parserArrayIncrement = 0;
        private String TAG="STOCK";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            textView = (TextView) findViewById(R.id.textView);
            editText = (EditText) findViewById(R.id.editText);
            textView4 = (TextView) findViewById(R.id.textView4);
            button3 = (Button) findViewById(R.id.button3);
            button1 = (Button)findViewById(R.id.button1);
            button4 = (Button)findViewById(R.id.button4);

            Log.d(TAG, "BEFORE URL CREATION" + sname);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sname = "APPLE";
                    final String yqlurl = URL1 + "AAPL" + URL2;

                    new task().execute(yqlurl);

                }
            });
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sname = "YAHOO";
                    final String yqlurl = URL1 + "YHOO" + URL2;

                    new task().execute(yqlurl);

                }
            });
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView4.setText("");
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Others others = new Others();
                    fragmentTransaction.add(R.id.fragmentOthers,others);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });
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
                //editText.setText("");
                textView4.setText("Stock Price of "+sname+" " + xmlPullParserArray[1][1] );
                Log.d("TEST", "result");
            }
        }

        public void someEvent(String s) {
            textView4.setText(s);
        }
            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
                    return true;
                }

                return super.onOptionsItemSelected(item);
            }

    }

