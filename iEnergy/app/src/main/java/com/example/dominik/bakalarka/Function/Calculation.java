package com.example.dominik.bakalarka.Function;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.InputStream;
import java.util.ArrayList;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by Dominik on 30.03.2017.
 * Vypocet najlepsieho distributora a najlepsej tarify podla dat
 */

public class Calculation{

    private String tariff, distributor, house;
    private float payment;
    private float nTariff, vTariff;
    private Sheet s;
    private int row, col;
    private int tariffNumber;
    public static String najDistributor;
    public static double najCena;
    public static String najTarifa;

    Context context;
    SharedPreferences preferences = null;
    /* Konstruktor pre ulozenie dat */
    public Calculation(Context context, String distributor, String tariff, String house, float nTariff, float vTariff, float payment) {
        this.context = context;
        this.distributor = distributor;
        this.tariff = tariff;
        this.house = house;
        this.nTariff = nTariff;
        this.vTariff = vTariff;
        this.payment = payment;
    }

    public void Calculate() {

        float distribution;
        Workbook wb = null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open("dodavatelia.xls");
            wb = Workbook.getWorkbook(is);
        } catch(Exception e) {
        }
        s = wb.getSheet(0);
        row = s.getRows();
        col = s.getColumns();

        findNumberOfTariff();
        int d = findDistributor();
        distribution = calculateDistribution();
        findBestDistributor();

    }

    /* Najdenie najlepsieho distributora a tarify prejdenim vsetkych ponuk zo suboru a vybratim najlepsieho */
    public void findBestDistributor() {

        findNumberOfTariff();
        Log.i("Tarifa ", String.valueOf(tariffNumber));
        String pole[] = new String[2];
        /* Arraylisty pre ulozenie najlepsich ponuk od roznych dodavatelov */
        ArrayList<String> distributors = new ArrayList<>();
        ArrayList<Double> bestPrice = new ArrayList<>();
        ArrayList<Integer> tariff = new ArrayList<>();
        int row,col;
        String distributorName = "";

        Workbook wb = null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open("dodavatelia2.xls");
            wb = Workbook.getWorkbook(is);
        } catch(Exception e) {
        }
        s = wb.getSheet(0);

        row = s.getRows();
        col = s.getColumns();

        double price = 0;
        double bestofPrice = 100000;
        double poplatok = 0;
        int index = 0;
        int pom = 0;
        /* Prechadzanie jednotlivych udajov zo suboru udaj po udaji a pocitanie celkovej ceny */
        for (int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                Cell c =s.getCell(j,i);
                if (i % 6 == 0) {
                    if (i != 0 && j ==0) {
                        pom = 0;
                        bestPrice.add(bestofPrice);
                        tariff.add(index % 6);
                        bestofPrice = 100000;
                        price = 0;
                    }
                    if (j == 0) {
                        distributorName = c.getContents();
                        distributors.add(distributorName);
                    }
                    if (j == 1) {
                        poplatok = Float.parseFloat(c.getContents()) * 12;
                    }
                }
                else {
                    if (tariffNumber < 3) {
                        if (pom < 3) {
                            if (j == 0) {
                                price += nTariff * Float.parseFloat(c.getContents());
                            }
                            if (j == 1) {
                                price += vTariff * Float.parseFloat(c.getContents());
                            }
                            if (j == 2) {
                                price += (vTariff + nTariff) * Float.parseFloat(c.getContents());
                            }
                            if (j == 3) {
                                price += Float.parseFloat(c.getContents()) * 12;
                            }
                        }
                    }
                    if (tariffNumber >= 3) {
                        if (pom > 2) {
                            if (j == 0) {
                                price += nTariff * Float.parseFloat(c.getContents());
                            }
                            if (j == 1) {
                                price += vTariff * Float.parseFloat(c.getContents());
                            }
                            if (j == 2) {
                                price += (vTariff + nTariff) * Float.parseFloat(c.getContents());
                            }
                            if (j == 3) {
                                price += Float.parseFloat(c.getContents()) * 12;
                            }
                        }
                    }
                }
            }
            /* Ulozenie najlepsi ceny */
            if (tariffNumber >=3 && pom > 2) {
                Log.i("C ", String.valueOf(price + poplatok));
                Log.i("B ", String.valueOf(bestofPrice));
                if (price + poplatok < bestofPrice) {
                        bestofPrice = price + poplatok;
                        index = i;
                        price = 0;
                }

            }
            if (tariffNumber < 3 && pom < 3) {
                    if (price + poplatok < bestofPrice) {
                        bestofPrice = price + poplatok;
                        index = i;
                        price = 0;
                    }

            }
            pom++;
        }

        /* Vyber najlepsej ceny z najlepsich */
        bestofPrice = bestPrice.get(0);
        int pam= 0;
        for (int i=0; i<4;i++){
            if (bestPrice.get(i) < bestofPrice) {
                bestofPrice = bestPrice.get(i);
                pam = i;
            }
        }

        najDistributor = distributors.get(pam);
        najCena = bestPrice.get(pam);
        nameOfTariff(pam, tariff.get(pam));
    }
    /* Zistenie nazvy tarify podla cisla a distributora */
    public void nameOfTariff(int dist, int tarif){
        Workbook wb = null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open("nazvy.xls");
            wb = Workbook.getWorkbook(is);
        } catch(Exception e) {
        }
        s = wb.getSheet(0);

        Cell c =s.getCell(tarif+1,dist);
        najTarifa = c.getContents();
    }

    /* Najdenie cisla tarify podla nazvy kvoli vypoctom */
    public void findNumberOfTariff() {
        int row, col;
        String tariffType;
        Workbook wb = null;
        try {
            AssetManager am = context.getAssets();
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

    /* Porovnanie priemernej platby v rovnakom dome */

    public float comparePrice() {
        String houseType;
        Workbook wb = null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open("ceny.xls");
            wb = Workbook.getWorkbook(is);
        } catch(Exception e) {
        }
        s = wb.getSheet(0);
        row = s.getRows();
        col = s.getColumns();

        for (int i=1; i < row; i++) {
            Cell c =s.getCell(0,i);
            houseType = c.getContents();
            if (house.equals(houseType)) {
                c = s.getCell(1,i);
                return Float.parseFloat(c.getContents()) * 12;
            }

        }
        return 0;
    }

    /* Porovnanie spotreby elektrickej energie podla typu domu */
    public float compareUsage() {
        String houseType;
        Workbook wb = null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open("ceny.xls");
            wb = Workbook.getWorkbook(is);
        } catch(Exception e) {
        }
        s = wb.getSheet(0);
        row = s.getRows();
        col = s.getColumns();

        for (int i=1; i < row; i++) {
            Cell c =s.getCell(0,i);
            houseType = c.getContents();
            if (house.equals(houseType)) {
                c = s.getCell(2,i);
                return Float.parseFloat(c.getContents());
            }

        }
        return 0;
    }

    /* Najdenie cisla distributora podla nazvu kvoli vypoctom */
    public int findDistributor() {

        String distributorType;

        for (int i=1; i < row; i++) {
            Cell c =s.getCell(2,i);
            distributorType = c.getContents();
            if (distributorType.equals(distributor)) {
                return i;
            }
        }
        return 0;
    }

    /* Vypocitanie ceny za distribuciu */
    public float calculateDistribution() {

        int r, c;
        float sum = 0;
        Workbook wb = null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open("fixneceny.xls");
            wb = Workbook.getWorkbook(is);
        } catch(Exception e) {
        }
        s = wb.getSheet(0);
        r = s.getRows();
        c = s.getColumns();

        for (int i=1; i<r; i++) {
            for (int j=1; j<c-1; j++) {
                Cell cell =s.getCell(j,i);
                if (i <= 4) {
                    sum += ((nTariff + vTariff) * Float.parseFloat(cell.getContents()));
                    break;
                }
            }
        }
        return sum;
    }

    /* Cista cena bez cistribucie */
    public void cistaCena() {
        int r, c;
        Workbook wb = null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open("fixneceny.xls");
            wb = Workbook.getWorkbook(is);
        } catch(Exception e) {
        }
        s = wb.getSheet(0);
        r = s.getRows();
        c = s.getColumns();

        for (int i=1; i<r-2; i++) {
            Cell cell =s.getCell(2,i);
            payment -= ((nTariff+vTariff) * Float.parseFloat(cell.getContents()));
            if (i <= 4)
                break;
        }
    }

}
