package com.example.android.proyectomovil;

import android.os.AsyncTask;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Random;

public class SimpleAsyncTask extends AsyncTask<Void, Void, String> {
    private WeakReference<TextView> mTextView;

    SimpleAsyncTask(TextView tv) {
        mTextView = new WeakReference<>(tv);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String info="DIA: Calle tesla, n22, cp66774\n" +
                "GADIS: Calle rocha, n33, cp44232\n" +
                "MERCADONA: Calle pi, n44, cp66678\n" +
                "FROIZ: Rua nova, n67, cp88844;";

        // CODIGO TAREA ASYNCTASK
        //MODIFICAR POR VISUALIZACION DE INFORMACION DE SUPERMERCADO
        Random r = new Random();
        int n = r.nextInt(11);

        // QUE DE TIEMPO A ROTAR TELEFONO MIENTRAS CORRE ASYNCTASK
        int s = n * 300;

        // DORMIR HILO
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // DEVOLVER STRING
        return info;
    }

    protected void onPostExecute(String result) {
        mTextView.get().setText(result);
    }
}
