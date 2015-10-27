package sc.shortcut.deeplinkingdemo.common;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sc.shortcut.sdk.android.deeplinking.SCDeepLinking;
import sc.shortcut.sdk.android.deeplinking.SCShortLinkCreateListener;
import sc.shortcut.sdk.android.deeplinking.SCShortLinkItem;

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

                // Get a SCDeepLinking instance if not already done
                SCDeepLinking deepLinking = SCDeepLinking.getInstance();

                // Describe the Short
                SCShortLinkItem item = new SCShortLinkItem();
                item.setWebDeepLink(Uri.parse("https://www.pinterest.com/meissnerceramic/allein-alone"));
                item.setAndroidDeepLink(Uri.parse("pinterest://board/meissnerceramic/allein-alone"));
                item.setGooglePlayStore(Uri.parse("http://play.google.com/store/apps/details?id=com.pinterest"));
                item.setIOSDeepLink(Uri.parse("pinterest://board/meissnerceramic/allein-alone"));
                item.setAppleAppStore(Uri.parse("http://itunes.apple.com/app/id429047995?mt=8"));

                // Request the short link
                deepLinking.createShortLink(item, new SCShortLinkCreateListener() {
                    @Override
                    public void onLinkCreated(Uri shortLink) {
                        TextView shortLinkTextView = (TextView) rootView.findViewById(R.id.short_link_id);
                        shortLinkTextView.setText(shortLink.toString());
                    }
                });

            }
        });

        TextView activityTextView = (TextView) rootView.findViewById(R.id.activity_id);
        activityTextView.setText("Activity id=" + getActivity().hashCode());

        return rootView;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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
