package sc.shortcut.deeplinkingdemo.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sc.shortcut.sdk.SCConfig;
import sc.shortcut.sdk.Shortcut;
import sc.shortcut.sdk.SCShortLinkBuilder;
import sc.shortcut.sdk.SCShortLinkCreateListener;

public class MainFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        rootView.findViewById(R.id.emulate_install_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SCExtPreference(getActivity()).reset();
                if (mListener != null) {
                    mListener.onRequestAppRestart();
                }
            }
        });

        rootView.findViewById(R.id.open_deep_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRequestStartWithDeepLinking();
                }
            }
        });

        rootView.findViewById(R.id.create_deep_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SCShortLinkBuilder builder = new SCShortLinkBuilder(getActivity())
                        .addWebLink("https://www.pinterest.com/meissnerceramic/allein-alone")
                        .addAndroidDeepLink("pinterest://board/meissnerceramic/allein-alone")
                        .addGooglePlayStoreUrl("http://play.google.com/store/apps/details?id=com.pinterest")
                        .addIosDeepLink("pinterest://board/meissnerceramic/allein-alone")
                        .addAppStoreUrl("http://itunes.apple.com/app/id429047995?mt=8");

                builder.createShortLink(new SCShortLinkCreateListener() {
                    @Override
                    public void onLinkCreated(String shortLink) {
                        TextView shortLinkTextView = (TextView) rootView.findViewById(R.id.short_link_id);
                        shortLinkTextView.setText(shortLink);
                    }
                });
            }
        });

        rootView.findViewById(R.id.create_short_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCShortLinkBuilder builder = new SCShortLinkBuilder(getActivity())
                        .addWebLink("https://www.pinterest.com/meissnerceramic/allein-alone")
                        .addAndroidDeepLink("pinterest://board/meissnerceramic/allein-alone")
                        .addGooglePlayStoreUrl("http://play.google.com/store/apps/details?id=com.pinterest")
                        .addIosDeepLink("pinterest://board/meissnerceramic/allein-alone")
                        .addAppStoreUrl("http://itunes.apple.com/app/id429047995?mt=8");

                String shortLink = builder.createShortLink();
                TextView shortLinkTextView = (TextView) rootView.findViewById(R.id.short_link_id);
                shortLinkTextView.setText(shortLink);

            }
        });

        rootView.findViewById(R.id.create_short_link_custom_url).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set up base url first
                SCConfig config = Shortcut.getInstance().getConfig();
                config.setBaseUrl("http://short.com");

                SCShortLinkBuilder builder = new SCShortLinkBuilder(getActivity())
                        .addWebLink("https://www.pinterest.com/meissnerceramic/allein-alone")
                        .addAndroidDeepLink("pinterest://board/meissnerceramic/allein-alone")
                        .addGooglePlayStoreUrl("http://play.google.com/store/apps/details?id=com.pinterest")
                        .addIosDeepLink("pinterest://board/meissnerceramic/allein-alone")
                        .addAppStoreUrl("http://itunes.apple.com/app/id429047995?mt=8");
                String shortLink = builder.createShortLink();
                TextView shortLinkTextView = (TextView) rootView.findViewById(R.id.short_link_id);
                shortLinkTextView.setText(shortLink);

                // reset baes url again
                config.setBaseUrl(null);
            }
        });

        rootView.findViewById(R.id.share_short_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SCShortLinkBuilder builder = new SCShortLinkBuilder(getActivity())
                        .addWebLink("https://www.pinterest.com/meissnerceramic/allein-alone")
                        .addAndroidDeepLink("pinterest://board/meissnerceramic/allein-alone")
                        .addIosDeepLink("pinterest://board/meissnerceramic/allein-alone");

                String shortLink = builder.createShortLink();


                // Create the new Intent using the 'Send' action.
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shortLink);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        TextView activityTextView = (TextView) rootView.findViewById(R.id.activity_id);
        activityTextView.setText("Activity id=" + getActivity().hashCode());

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onRequestStartWithDeepLinking();
        void onRequestAppRestart();
    }

}
