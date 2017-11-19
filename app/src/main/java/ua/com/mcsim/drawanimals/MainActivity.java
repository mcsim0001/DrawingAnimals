package ua.com.mcsim.drawanimals;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import ua.com.mcsim.drawanimals.drawing.PixelPaintView;
import ua.com.mcsim.drawanimals.utils.BannerActivity;
import ua.com.mcsim.drawanimals.utils.MultiSlide;


public class MainActivity extends BannerActivity implements View.OnClickListener {


    private PixelPaintView mPaintView;
    private ConstraintLayout deskLayout, sizeLayout;
    private SharedPreferences preferences;
    private final int HOME_ANIMALS = 1000;
    private final int BRUSH_COLOR = 2000;
    private final int BRUSH_WEIGHT = 150;
    private int brushSize = 30;
    private final float BRUSH_SCALE = 1.3f;
    private ImageButton btnErase, btnPalette, btnRecycler, btnAnimals, btnSize, btnUndo, btnRedo;
    private ImageView animalImage, sizeCircle;
    private SeekBar sizeBar;
    private MultiSlide slideBottomPanel, slideLeftPanel;
    private View bottomPanel, leftPanel, lastView;
    private final String STATUS = "rate_status";
    private final int[] animal = {
            R.drawable.cow150x200,
            R.drawable.cat150x200,
            R.drawable.cat2_150x200,
            R.drawable.dog1_150x200,
            R.drawable.dog2_150x200,
            R.drawable.pig150x200,
            R.drawable.pig2_150x200,
            R.drawable.pig3_150x200,
            R.drawable.sheep1_150x200,
            R.drawable.sheep2_150x200,
            R.drawable.horse1_150x200};
    private final int[] trafaret = {
            R.drawable.cow,
            R.drawable.cat,
            R.drawable.cat2,
            R.drawable.dog1,
            R.drawable.dog2,
            R.drawable.pig,
            R.drawable.pig2,
            R.drawable.pig3,
            R.drawable.sheep1,
            R.drawable.sheep2,
            R.drawable.horse1};

    private final int[] brush = {
            R.drawable.br1,
            R.drawable.br2,
            R.drawable.br3,
            R.drawable.br4,
            R.drawable.br5,
            R.drawable.br6,
            R.drawable.br7,
            R.drawable.br8,
            R.drawable.br9,
            R.drawable.br10,
            R.drawable.br11,
            R.drawable.br12,
            R.drawable.br13,
            R.drawable.br14};
    private final int[] color = {
            R.color.brush1,
            R.color.brush2,
            R.color.brush3,
            R.color.brush4,
            R.color.brush5,
            R.color.brush6,
            R.color.brush7,
            R.color.brush8,
            R.color.brush9,
            R.color.brush10,
            R.color.brush11,
            R.color.brush12,
            R.color.brush13,
            R.color.brush14};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("mLog", "onCreate");

        //***********Fullscreen without action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            return;

        }

        setContentView(R.layout.drawerdesk);

        //Creating Paint desk with letter
        mPaintView = (PixelPaintView) findViewById(R.id.paintView);

        //Seekbar
        sizeBar = (SeekBar) findViewById(R.id.size_bar);
        sizeBar.setProgress(brushSize);
        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int prog;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                prog=progress+20;
                sizeCircle.getLayoutParams().height = progress+20;
                sizeCircle.getLayoutParams().width = progress+20;
                sizeCircle.requestLayout();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPaintView.setBrushSize(prog);
                brushSize = prog;
            }
        });

        //Buttons
        btnErase = (ImageButton) findViewById(R.id.btn_erase);
        btnErase.setOnClickListener(this);
        btnPalette = (ImageButton) findViewById(R.id.btn_palette);
        btnPalette.setOnClickListener(this);
        btnRecycler = (ImageButton) findViewById(R.id.btn_recycler);
        btnRecycler.setOnClickListener(this);
        btnAnimals = (ImageButton) findViewById(R.id.btn_animals);
        btnAnimals.setOnClickListener(this);
        btnSize = (ImageButton) findViewById(R.id.btn_size);
        btnSize.setOnClickListener(this);
        /*btnUndo = (ImageButton) findViewById(R.id.btn_undo);
        btnUndo.setOnClickListener(this);
        btnRedo = (ImageButton) findViewById(R.id.btn_redo);
        btnRedo.setOnClickListener(this);*/

        //Images
        sizeCircle = (ImageView) findViewById(R.id.size_circle);
        sizeCircle.getLayoutParams().height = brushSize;
        sizeCircle.getLayoutParams().width = brushSize;
        sizeCircle.setImageResource(R.drawable.size_example);

        //Trafarete Image
        animalImage = (ImageView) findViewById(R.id.animalImage);
        animalImage.setImageResource(R.drawable.cow);

        //Panels
        bottomPanel = findViewById(R.id.bottom_panel);
        leftPanel = findViewById(R.id.left_panel);

        slideBottomPanel = new MultiSlide(bottomPanel, MultiSlide.BOTTOM);
        slideBottomPanel.hideImmediately();

        slideLeftPanel = new MultiSlide(leftPanel,MultiSlide.LEFT);
        slideLeftPanel.hideImmediately();

        //Layouts
        deskLayout = (ConstraintLayout) findViewById(R.id.back_layout);
        sizeLayout = (ConstraintLayout) findViewById(R.id.size_layout);
        sizeLayout.setVisibility(View.GONE);

        //initialize bottom panel
        initializeBottomPanel();
        initializeLeftPanel();

        //Initialize ADs
        View bannerHolder = findViewById(R.id.banner_view);
        initializeBanner(bannerHolder);

    }

    private void initializeBottomPanel() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.bottom_panel_layout);
        ImageView menuItem;

        for (int i = 0; i < animal.length; i++ ) {
            menuItem = new ImageView(this);
            menuItem.setId(animal[i]+HOME_ANIMALS);
            menuItem.setImageResource(animal[i]);
            menuItem.setOnClickListener(this);
            linearLayout.addView(menuItem,200,150);
        }
    }

    private void initializeLeftPanel() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.left_panel_layout);
        ImageView menuItem = new ImageView(this);
        linearLayout.addView(menuItem,BRUSH_WEIGHT+20,0);  //

        for (int i = 0; i < brush.length; i++ ) {
            menuItem = new ImageView(this);
            menuItem.setId(brush[i]+BRUSH_COLOR);
            menuItem.setImageResource(brush[i]);
            menuItem.setOnClickListener(this);
            linearLayout.addView(menuItem,BRUSH_WEIGHT,88);
            if (i==0) {
                onClick(menuItem);
            }
        }
    }

    private void eraserButtonSwitch() {

        if (mPaintView.isEraserEnable()) {
            btnErase.setBackgroundResource(R.drawable.brush40x40);
        } else {
            btnErase.setBackgroundResource(R.drawable.clean40x40);
        }
    }
    private void panelSwitch(MultiSlide panel) {
        if (!panel.isVisible()) {

            panel.animateIn();
        } else {
            panel.animateOut();

        }
    }

    private boolean isNotRatedApp() {

        return !getPreferences(MODE_PRIVATE).getBoolean(STATUS, false);
    }

    private void gotoMarket() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("amzn://apps/android?p=ua.com.mcsim.drawanimals_new20")));
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=ua.com.mcsim.drawanimals")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=ua.com.mcsim.drawanimals_new20")));
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=ua.com.mcsim.drawanimals")));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (isNotRatedApp()) {
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MainActivity.this);
            dialog.setTitle(getResources().getString(R.string.dialog_title));
            dialog.setPositiveButton(R.string.btn_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    gotoMarket();
                    preferences = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor ed = preferences.edit();
                    ed.putBoolean(STATUS, true);
                    ed.commit();
                    finish();
                }
            });
            dialog.setNegativeButton(R.string.btn_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog.show();
        } else finish();
    }

    @Override
    public void onClick(View view) {

        //Touch Animation
        touchAnimation(view);

        //Drawerscreen Buttons
        switch (view.getId()){
            case R.id.btn_erase:{
                mPaintView.setErase(!mPaintView.isEraserEnable());
                eraserButtonSwitch();
                break;
            }
            /*case R.id.btn_letter:{
                showhideLetter();
                break;
            }*/
            case R.id.btn_palette:{

                panelSwitch(slideLeftPanel);
                break;
            }
            case R.id.btn_recycler:{
                mPaintView.startNew();
                break;
            }
            case R.id.btn_animals:{
                panelSwitch(slideBottomPanel);
                break;
            }
            case R.id.btn_size:{
                if (sizeLayout.getVisibility() == View.VISIBLE) {
                    sizeLayout.setVisibility(View.GONE);
                }else sizeLayout.setVisibility(View.VISIBLE);
                break;
            }
            /*case R.id.btn_undo:{
                mPaintView.performUndo();
                break;
            }
            case R.id.btn_redo:{
                mPaintView.performRedo();
                break;
            }*/
        }
        //Animals
        for (int i = 0; i < animal.length; i++) {
            if (animal[i]+HOME_ANIMALS == view.getId()) {
                animalImage.setImageResource(trafaret[i]);
                slideBottomPanel.animateOut();
                mPaintView.startNew();
                break;
            }
        }
        //Brushes + colors
        for (int i = 0; i < brush.length; i++) {
            if (brush[i]+BRUSH_COLOR == view.getId()) {
                view.animate().scaleX(BRUSH_SCALE);
                view.animate().scaleY(BRUSH_SCALE);
                mPaintView.setColor(getResources().getColor(color[i]));
                if (lastView!=null&&lastView!=view) {
                    lastView.animate().scaleX(1.0f);
                    lastView.animate().scaleY(1.0f);
                }
                lastView = view;
                break;
            }
        }
    }

    private void touchAnimation(View view) {
        Animation anim1 = new ScaleAnimation(1.0f,0.9f,1.0f,0.9f);
        anim1.setDuration(100);
        view.startAnimation(anim1);
    }



}
