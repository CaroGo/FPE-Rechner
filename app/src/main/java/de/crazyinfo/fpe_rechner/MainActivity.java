package de.crazyinfo.fpe_rechner;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity

        implements OnClickListener {

    /**
     * Wird aufgerufen, wenn die Aktivität erstellt wird.
     */

    /* Variablen für Button, EditText und TextView */
    private Button buttonCalc;                                                                      // Button "Berechnung"
    private EditText editTextCho;                                                                   // Kohlenhydrate
    private EditText editTextKcal;                                                                  // KCAL
    private EditText editTextFactor;                                                                // Faktor
    private TextView textViewResult;                                                                // Ergebnis

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Definition App (Button, Text etc. mit Hinweisausgabe)
        buttonCalc = (Button) findViewById(R.id.buttonCalc);
        editTextCho = (EditText) findViewById(R.id.editTextCho);
        editTextKcal = (EditText) findViewById(R.id.editTextKcal);
        editTextFactor = (EditText) findViewById(R.id.editTextFactor);
        textViewResult = (TextView) findViewById(R.id.textViewResult);

        buttonCalc.setOnClickListener(this);

            // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // OnClick wird aufgerufen, wenn geklickt wird

    @Override
    public void onClick(View v) {

        /* Prüfen ob Leereingabe vorliegt und als Toast Message ausgeben */
        if (("".equals(editTextCho.getText().toString().trim()) || "".equals(editTextKcal.getText().toString().trim()) || "".equals(editTextFactor.getText().toString().trim()))){
            Toast.makeText(this, getString(R.string.noValue), Toast.LENGTH_LONG).show();
            return;
        }

        if (v == buttonCalc) {

            /* Variablen deklarieren */
            double cho;                 // Kohlenhydrate
            double kcal;                // Kcal
            double factor;              // Faktor
            double insulin;             // Insulin
            double fpu;                 // Fett-Protein-Einheiten gerundet

            /* Diese erhält den Wert des Feldes als Zahl (Kohlenhydrate & KCAL & Faktor) */
            cho = Float.valueOf(editTextCho.getText().toString());
            kcal = Float.valueOf(editTextKcal.getText().toString());
            factor = Float.valueOf(editTextFactor.getText().toString());

            /* Berechnungen */
            cho = Math.abs(cho * 4);                                                                // Kohlenhydrate * 4
            fpu = (double) Math.round(((kcal - cho) / 100 * 10) * 1) / 10;                          // FPE-Gehalt auf eine Nachkommastelle gerundet
            insulin = (double) Math.round(((kcal - cho) / 100 * factor * 10) * 1) / 10;             // Insulinmenge auf eine Nachkommastelle gerundet

            /*  Stundenvergleich für die Insulinabgabe */
            String[] arrayCheckHours = {"3", "4", "5", "7 - 8", "0"};
            String checkHours = "0";

            if (kcal >= 100 && kcal < 200) {
                System.out.println(arrayCheckHours[0]);         // 3 Stunden
                checkHours = arrayCheckHours[0].toString();
            }
            if (kcal >= 200 && kcal < 300) {
                System.out.println(arrayCheckHours[1]);         // 4 Stunden
                checkHours = arrayCheckHours[1].toString();
            }
            if (kcal >= 300 && kcal < 400) {
                System.out.println(arrayCheckHours[2]);         // 5 Stunden
                checkHours = arrayCheckHours[2].toString();
            }
            if (kcal >= 400) {
                System.out.println(arrayCheckHours[3]);         // 7 - 8 Stunden
                checkHours = arrayCheckHours[3].toString();
            }
            if (insulin <= 0) {
                System.out.println(arrayCheckHours[4]);         // Wenn Insulin kleiner gleich 0, dann auch 0 Stunden
                checkHours = arrayCheckHours[4].toString();
            }
            if (fpu <= 0) {
                System.out.println(arrayCheckHours[4]);
                checkHours = arrayCheckHours[4].toString();     // Wenn Fett-Protein-Einheiten kleiner gleich 0, dann auch 0 Stunden
            }

            /*  Insulinabgabe prüfen*/
            double[] arrayInsulin = {0};
            if (insulin < 0) {
                insulin = arrayInsulin[0];                      // Wenn Insulin kleiner 0, dann auch Insulin 0
            }
            if (kcal < 100) {
                insulin = arrayInsulin[0];                      // Wenn Kcal kleiner 0, dann auch Insulin 0
            }

            /*  FPE-Gehalt prüfen*/
            double[] arrayFpu = {0};
            if (fpu < 0) {
                fpu = arrayFpu[0];                              // Wenn FPE kleiner 0, dann auch FPE 0
            }
            if (insulin <= 0) {
                fpu = arrayFpu[0];                              // Wenn Insulin kleiner gleich 0, dann auch FPE 0
            }

            /* Ergebnisausgabe */
            textViewResult.setText(getString(R.string.calcResult) + fpu + getString(R.string.textFpu) + insulin + getString(R.string.amountOfInsulin) + checkHours + getString(R.string.hoursText));
        }
    }
}
