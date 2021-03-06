package com.example.coronareport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

public class GlobalFragment extends Fragment implements LoaderManager.LoaderCallbacks<GlobalData> {

    public static final String LOG_TAG = GlobalFragment.class.getName();

    private static final int GLOBAL_LOADER_ID = 4;

    public static String GLOBAL_REQUEST_URL =
            "https://disease.sh/v3/covid-19/all?yesterday=false&twoDaysAgo=false&allowNull=false\n";

    private TextView worldCases,worldDeaths, countryName,cityName, countryCases,countryRecovered,countryCritical,countryDeaths;
    private ImageView countryFlag, google,github,instagram,facebook;

    private LinearLayout globalLayout, countryLayout, aboutLayout,symptomsLayout,preventionLayout;

    private ShimmerFrameLayout shimmerFrameLayout;

    public GlobalFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.global_fragment, container, false);

        globalLayout = rootView.findViewById(R.id.global_box);
        countryLayout = rootView.findViewById(R.id.gcountry_box);
        aboutLayout = rootView.findViewById(R.id.about_box);
        symptomsLayout = rootView.findViewById(R.id.symptoms_box);
        preventionLayout = rootView.findViewById(R.id.prevention_box);

        shimmerFrameLayout = rootView.findViewById(R.id.global_shimmer);
        globalLayout.setVisibility(View.GONE);
        countryLayout.setVisibility(View.GONE);

        worldCases = rootView.findViewById(R.id.wcasesCount);
        worldDeaths = rootView.findViewById(R.id.wdeathCount);

        countryName = rootView.findViewById(R.id.gcountry_name);
        cityName = rootView.findViewById(R.id.gcity_name);

        countryCases = rootView.findViewById(R.id.gcasesCount);
        countryRecovered = rootView.findViewById(R.id.grecoveredCount);
        countryCritical = rootView.findViewById(R.id.gcriticalCount);
        countryDeaths = rootView.findViewById(R.id.gdeathCount);

        countryFlag = rootView.findViewById(R.id.gflag);

        google = rootView.findViewById(R.id.ggoogle);
        github = rootView.findViewById(R.id.ggithub);
        instagram = rootView.findViewById(R.id.ginstagram);
        facebook = rootView.findViewById(R.id.gfacebook);

        android.app.LoaderManager loaderManager = getActivity().getLoaderManager();
        loaderManager.initLoader(GLOBAL_LOADER_ID, null, this);

        aboutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });

        symptomsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), SymptomsActivity.class));
            }
        });

        preventionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), PreventionActivity.class));
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                intent.setData(Uri.parse("mail to:"));
//                intent.putExtra(Intent.EXTRA_SUBJECT, "COVID REPORT: <Add Subject Here>");
//                intent.putExtra(Intent.EXTRA_TEXT, "Hey there wtfarooq!\n\n\n\n\nRegards,\n<Insert your name here>");
//                if(intent.resolveActivity(getActivity().getPackageManager()) != null)
//                    startActivity(intent);
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"developer@covidreport.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "COVID REPORT: <Add Subject Here>");
                email.setType("message/rfc822");

                getActivity().startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/wtfarooq/")));
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/wtfarooq/")));
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/mirzamohdfarooq/")));
            }
        });

        return rootView;
    }

    @Override
    public Loader<GlobalData> onCreateLoader(int id, @Nullable Bundle args) {
        String url1 = "http://ip-api.com/json";
        return new GlobalLoader(getActivity(), url1, GLOBAL_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<GlobalData> loader, final GlobalData data) {

        Picasso.get()
                .load(data.flagUrl)
                .placeholder(R.drawable.world_icon)
                .into(countryFlag, new com.squareup.picasso.Callback(){
                    @Override
                    public void onSuccess() {
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        globalLayout.setVisibility(View.VISIBLE);
                        countryLayout.setVisibility(View.VISIBLE);
                        //loadingIndicator.setVisibility(View.GONE);
                        //countryInfo.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(LOG_TAG, "Problem loading Flag");
                    }
                });

        CoronaAdapter.setText(worldCases, data.worldCases);
        CoronaAdapter.setText(worldDeaths, data.worldDeaths);

        countryName.setText(data.countryName);
        cityName.setText(data.cityName);

        CoronaAdapter.setText(countryCases, data.countryCases);
        CoronaAdapter.setText(countryRecovered, data.countryRecovered);
        CoronaAdapter.setText(countryCritical, data.countryCriticalCases);
        CoronaAdapter.setText(countryDeaths, data.countryDeaths);

        countryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getContext(), CountryActivity.class);
                bundle.putString("COUNTRY_NAME", data.countryName);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });

    }

    @Override
    public void onLoaderReset(@NonNull Loader<GlobalData> loader) {

    }
}