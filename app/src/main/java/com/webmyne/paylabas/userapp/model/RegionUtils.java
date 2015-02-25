package com.webmyne.paylabas.userapp.model;

import android.content.Context;
import android.os.AsyncTask;

import com.webmyne.paylabas.userapp.base.DatabaseWrapper;
import com.webmyne.paylabas.userapp.helpers.IService;

import java.util.ArrayList;

/**
 * Created by Android on 12-01-2015.
 */
public abstract class RegionUtils implements IService {

    public abstract void response(ArrayList response);
    static ArrayList<Country> countrylist;
    static ArrayList<State> statelist;


 public synchronized final RegionUtils fetchCountry(final Context ctx){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseWrapper db_wrapper = new DatabaseWrapper(ctx);
                try {
                    db_wrapper.openDataBase();
                     countrylist = db_wrapper.getCountryData();
                   // response(countrylist);
                    db_wrapper.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                response(countrylist);
            }
        }.execute();
        return this;
    }

 public synchronized final RegionUtils fetchState(final Context ctx,final int CountryID){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseWrapper  db_wrapper = new DatabaseWrapper(ctx);
                try {
                    db_wrapper.openDataBase();
                    statelist = db_wrapper.getStateData(CountryID);
                    db_wrapper.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                response(statelist);
            }
        }.execute();
        return this;
    }




}
