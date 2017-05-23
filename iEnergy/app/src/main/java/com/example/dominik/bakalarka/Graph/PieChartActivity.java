package com.example.dominik.bakalarka.Graph;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.example.dominik.bakalarka.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.io.InputStream;
import java.util.ArrayList;
import cn.refactor.lib.colordialog.PromptDialog;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by Dominik on 10.3.2017.
 * Graf distribucie
 */

public class PieChartActivity extends AppCompatActivity implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private PieChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    protected String[] mParties = new String[] {
            "Pevna mesačná zlozka tarify za 1 OM", "Platba za prevadzkovanie systému",
            "Platba za straty elektr. pri distr. elek.", "Platba za systémové služby",
            "Variabilná zložka tarify za distribúciu", "Odvod do jadrového fondu"
    };
    int tariffNumber;
    String tariff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);

        /* Zablokovanie pretacania obrazovky */
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         /* Nastavenie titulku */
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout_distribucia);

        /* Prepojenie komponentov a ziskanie pristupu k nim a ich datam, nastavenie jednotlivych dat */
        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        /* Povolenie ratcie grafu pomocou tahu prsta */
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        mChart.setOnChartValueSelectedListener(this);

        /* Nastavenie dat a uvidnej animacie */
        setData(6, 100);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        /* Nastavenie stylu pisma */
        mChart.setEntryLabelColor(Color.BLACK);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText("" + (mSeekBarX.getProgress()));
        tvY.setText("" + (mSeekBarY.getProgress()));

        setData(mSeekBarX.getProgress(), mSeekBarY.getProgress());
    }

    /* Nastavenie dat pre graf */
    private void setData(int count, float range) {

        float mult = range, jadFond =0, preSystemu =0, sysSluzby =0, straty =0, distMesacne = 0, distKWH = 0;
        /* Ziskanie udajov */
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        float payment = preferences.getFloat("payment", 0);
        float usageNT = preferences.getFloat("usageNT", 0);
        float usageVT = preferences.getFloat("usageVT", 0);
        tariff = preferences.getString("tariff", "");
        int r;
        Sheet s;
        Workbook wb = null;
        try {
            AssetManager am = getAssets();
            InputStream is = am.open("fixneceny.xls");
            wb = Workbook.getWorkbook(is);
        } catch(Exception e) {
        }
        s = wb.getSheet(0);

        /* Vypocitanie podielu jednotlivzhc poloziek na grafe z udajov zo suboru */
        Cell cell =s.getCell(1,1);
        jadFond = (usageNT+usageVT) * Float.parseFloat(cell.getContents());
        cell =s.getCell(1,2);
        preSystemu = (usageNT+usageVT) * Float.parseFloat(cell.getContents());
        cell =s.getCell(1,3);
        sysSluzby = (usageNT+usageVT) * Float.parseFloat(cell.getContents());
        cell =s.getCell(1,4);
        straty = (usageNT+usageVT) * Float.parseFloat(cell.getContents());
        findNumberOfTariff();
        cell =s.getCell(tariffNumber,5);
        distKWH = (usageNT+usageVT) * Float.parseFloat(cell.getContents());
        cell =s.getCell(tariffNumber,6);
        distMesacne = 12 * Float.parseFloat(cell.getContents());

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        float spolu = jadFond+preSystemu+sysSluzby+straty+distKWH+distMesacne;

        /* Urcenie podielov jednotlivych poloziek na grafe */
        entries.add(new PieEntry(distMesacne*100 / spolu, mParties[0]));
        entries.add(new PieEntry(preSystemu*100 / spolu, mParties[1]));
        entries.add(new PieEntry(straty*100 / spolu, mParties[2]));
        entries.add(new PieEntry(sysSluzby*100 / spolu, mParties[3]));
        entries.add(new PieEntry(distKWH*100 / spolu, mParties[4]));
        entries.add(new PieEntry(jadFond*100 / spolu, mParties[5]));

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        /* Pridanie farieb pre jednotlive polozky */
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        mChart.highlightValues(null);

        mChart.invalidate();
    }

    /* Nastavenie centralneho nazvu grafu */
    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Distribucia\nfixne polozky faktury");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 12, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 12, s.length() - 12, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 12, s.length() - 12, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 12, s.length() - 12, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 12, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 21, s.length(), 0);
        return s;
    }

    /* Posluchac na jednotlive polozky grafu */
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        int msg = getStrings((int) h.getX());

        showDialog((int) h.getX(), msg);

    }

    PromptDialog pd;
    /* Dialog s vysvetlivkou pre jednotlive polozky */
    public void showDialog(int index, int msg) {

         pd = new PromptDialog(this)
                .setDialogType(PromptDialog.BUTTON_NEUTRAL)
                .setAnimationEnable(true)
                .setTitleText(mParties[index])
                .setContentText(msg)
                .setPositiveListener("Ok", new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                });
        pd.show();

    }
    /* Ziskanie vysvetliviek */
    public int getStrings(int index){

        if(index == 0)
            return R.string.odberne_miesto;
        if(index == 1)
            return R.string.prevadzka;
        if(index == 2)
            return R.string.straty;
        if(index == 3)
            return R.string.sluzby;
        if(index == 4)
            return R.string.zlozka_tarify;
        if(index == 5)
            return R.string.fond;
        return R.string.fond;
    }

    @Override
    public void onNothingSelected() {
        pd = null;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    /* Najdenie tarify kvoli vypoctu distribucie */
    public void findNumberOfTariff() {
        int row, col;
        String tariffType;
        Sheet s;
        Workbook wb = null;
        try {
            AssetManager am = getAssets();
            InputStream is = am.open("nazvy.xls");
            wb = Workbook.getWorkbook(is);
        } catch(Exception e) {
        }
        s = wb.getSheet(0);
        row = s.getRows();
        col = s.getColumns();

        for (int i=0; i<row; i++) {
            for (int j=1; j< col; j++) {
                Cell c =s.getCell(j,i);
                tariffType = c.getContents();
                if(tariff.equals(tariffType))
                    tariffNumber = j;
            }
        }
    }
    /* Ukoncenie aktivity v pripade stlacenia tlacidla spat */
    public void onBackPressed() {
        this.finish();
    }
}
