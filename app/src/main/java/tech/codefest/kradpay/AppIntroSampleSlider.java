//Android App Intro Slider
package tech.codefest.kradpay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dominicneeraj on 9/9/2016.
 */
public class AppIntroSampleSlider extends Fragment {
    //Layout id
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";

    public static AppIntroSampleSlider newInstance(int layoutResId) {
        AppIntroSampleSlider sampleSlide = new AppIntroSampleSlider();

        Bundle bundleArgs = new Bundle();
        bundleArgs.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        sampleSlide.setArguments(bundleArgs);

        return sampleSlide;
    }

    private int layoutResId;

    public AppIntroSampleSlider() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID))
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutResId, container, false);
    }

}
