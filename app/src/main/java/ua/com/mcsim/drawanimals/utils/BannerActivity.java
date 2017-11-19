package ua.com.mcsim.drawanimals.utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import ua.com.mcsim.drawanimals.R;

/**
 * Created by mcsim on 19.11.2017.
 */

public class BannerActivity extends Activity {
    private AdView bannerView ;
    private View bannerHolder;
    //private final String APPLICATION_ID = "ca-app-pub-4631424539736494~3163537086"; //For Playmarket
    private final String APPLICATION_ID = "ca-app-pub-4631424539736494~5851040485"; //For Amazon "Draw and write Letters. Free Multilingual ABC"
    AdRequest adRequest;
    private boolean isFirstUse = true;
    private final int AD_CLOSED_TIMEOUT = 100000;
    private final int FIRST_TIMEOUT = 20000;
    private final int CLOSED_TIMEOUT = 40000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, APPLICATION_ID);
    }

    public void initializeBanner(View bannerholder) {

        this.bannerHolder = bannerholder;
        TextView closeText = (TextView) findViewById(R.id.tv_close);
        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideBanner(CLOSED_TIMEOUT);
            }
        });

        this.bannerHolder.setVisibility(View.GONE);
        bannerView = (AdView) findViewById(R.id.adView);

        adRequest = new AdRequest.Builder()
                .addTestDevice("5A42917EE89D460CB80107FD2425D377")
                .addTestDevice("214C48AE89B8C4E1A14883599CC9B132")
                .build();
        bannerView.loadAd(adRequest); // Comment this string for kill Ad banner
        bannerView.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                hideBanner(AD_CLOSED_TIMEOUT);
            }


            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (isFirstUse) {
                    showBannerWithDelay(FIRST_TIMEOUT);
                    isFirstUse = false;
                }
            }
        });
    }

    private void showBannerWithDelay(long delay) {

        if (bannerView!=null) {
            bannerHolder.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation scaleIn = AnimationUtils.loadAnimation(BannerActivity.this,R.anim.slide_up);
                    bannerHolder.setVisibility(View.VISIBLE);
                    bannerHolder.startAnimation(scaleIn);
                }
            },delay);

        }
    }
    public void hideBanner(long delay) {
        Animation scaleOut = AnimationUtils.loadAnimation(this,R.anim.slide_down);
        if (bannerHolder!=null) {
            bannerHolder.startAnimation(scaleOut);
            bannerHolder.setVisibility(View.GONE);
            showBannerWithDelay(delay);
        }

    }

    @Override
    protected void onDestroy() {
        if (bannerView!=null)
            bannerView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (bannerView!=null)
            bannerView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bannerView!=null)
            bannerView.resume();
    }

}
