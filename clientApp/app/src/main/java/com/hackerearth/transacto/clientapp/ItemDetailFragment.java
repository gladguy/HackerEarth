package com.hackerearth.transacto.clientapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hackerearth.transacto.clientapp.dummy.DummyContent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment implements View.OnClickListener{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }
    String String_url;
    String strAmount = "";
    String Payment_id = "";

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scanner, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            //((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.content + " " + "Thank you");
        }
        Button scanButton;
        scanButton = (Button) rootView.findViewById(R.id.btnScan);
        scanButton.setOnClickListener(this);

        Button payButton;
        payButton = (Button) rootView.findViewById(R.id.btnPay);
        payButton.setOnClickListener(this);


        return rootView;
    }


    public void payScreen(View v)
    {

    }
    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(getActivity(), "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        Log.v("Waheed", "FragmentResult" + requestCode + "and" + resultCode);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                TextView mTextView;
                mTextView = (TextView) getView().findViewById(R.id.amount);

                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                CharSequence toastText = "Content:" + contents + " Format:" + format;
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), toastText, Toast.LENGTH_LONG);
                toast.show();

                StringTokenizer token = new StringTokenizer(contents,"|");
                int i = 0;
                while(token.hasMoreTokens())
                {
                    if(i ==0)
                        Payment_id = token.nextToken();
                    else
                        strAmount  = token.nextToken();

                    i++;
                }
                mTextView.setText(strAmount);
            }
        }
    }


    public void onClick(final View v) { //check for what button is pressed
        switch (v.getId()) {
            case R.id.btnScan: {
                Log.v("Waheed", "Button Scan Clicked");
                scanQR(v);
                break;
            }
            case R.id.btnPay:
            {
                String result="";
                InputStream in = null;
                String lastname,firstname,creditcard;

                TextView firstNameV;
                TextView lastNameV;
                TextView creditcardV;
                firstNameV = (TextView) getView().findViewById(R.id.firstname);
                lastNameV = (TextView) getView().findViewById(R.id.lastname);
                creditcardV = (TextView) getView().findViewById(R.id.creditcard);

                lastname = lastNameV.getText().toString().trim();
                firstname = firstNameV.getText().toString().trim();
                creditcard = creditcardV.getText().toString().trim();


                if(firstname.length() < 5 || lastname.length() < 5 ||  creditcard.length() < 5)
                {
                    CharSequence toastText = "Please fill in the details";
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), toastText, Toast.LENGTH_LONG);
                    toast.show();
                }
                else
                {
                    String_url = "http://www.puliyal.com/hackerearth/UpdatePayment.php?firstname=" + firstname + "&lastname=" + lastname + "&payment_id=" + Payment_id;

                    try {
                        new DownloadMusicfromInternet().execute(String_url);
                        //  in = openHttpConnection(url);
                        CharSequence toastText = " Payment Received" + firstname + " " + lastname + "Thank you !";
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), toastText, Toast.LENGTH_LONG);
                        toast.show();

//                    in.close();
                    } catch (Exception e1) {
                        Log.v("Waheed open", e1.toString());
                    }
                }
                Log.v("Waheed", "Button Pay Clicked");
                break;
            }
        }
    }


    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    // Async Task Class
    class DownloadMusicfromInternet extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {

            InputStream in = null;
            int resCode = -1;
            try {

                Log.v("Waheed URL ", String_url);
                URL url = new URL(String_url);
                //URL url = new URL(f_url.toString());
                //HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                URLConnection conection = url.openConnection();
                conection.connect();
                // Get Music file length
                int lenghtOfFile = conection.getContentLength();
                Log.v("Waheed Lengt File",lenghtOfFile + "");
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(),10*1024);
                // Output stream to write file in SD card
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/jai_ho.mp3");
                /*byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress which triggers onProgressUpdate method
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // Write data to file
                    output.write(data, 0, count);
                }*/
                // Flush output
                output.flush();
                // Close streams
                output.close();
                input.close();
                Log.v("Waheed",f_url.toString());


            }
            catch(Exception ee)
            {
                Log.v("Waheed open eee", ee.toString());

            }

            return null;
        }

        // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage

        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded

        }
    }


}
