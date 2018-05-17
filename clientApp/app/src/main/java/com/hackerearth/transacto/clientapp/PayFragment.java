package com.hackerearth.transacto.clientapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hackerearth.transacto.clientapp.dummy.DummyContent;



import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class PayFragment extends Fragment implements View.OnClickListener{

    public static final String ARG_PAYMENT_ID = "payment_id";
    public static final String ARG_AMOUNT = "amount";
    private DummyContent.DummyItem Payment_id;
    private DummyContent.DummyItem Amount;

    public PayFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_PAYMENT_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Payment_id = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_PAYMENT_ID));
        }

        if (getArguments().containsKey(ARG_AMOUNT)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Amount = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_AMOUNT));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scanner, container, false);
        Button payButton;
        payButton = (Button) rootView.findViewById(R.id.btnPay);
        payButton.setOnClickListener(this);

       return rootView;
    }

    public void onClick(final View v) { //check for what button is pressed
        switch (v.getId()) {
            case R.id.btnPay:
            {

                try {
                    String url = "http://www.puliyal.com/hackerearth/UpdatePayment.php?firstname=Android&lastname=rahuman&payment_id=1011";
                    InputStream in = null;
                    in = openHttpConnection(url);
                    in.close();
                }

                catch (IOException e1) {
                    e1.printStackTrace();
                }
                Log.v("Waheed", "Button Pay Clicked");
                break;
            }
        }
    }

    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

}
