package spielereien;

import bankprojekt.verarbeitung.Aktie;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Konto;
import bankprojekt.verarbeitung.Kunde;

import java.time.LocalDate;

/**
 * AktieSpielereien is a class demonstrating simple operations with stock (Aktie) and account (Konto) objects.
 * It creates instances of Aktie, Kunde, and Konto to simulate buying and selling stock transactions.
 */
public class AktieSpielereien {
    /**
     * The main method that serves as an entry point for the AktieSpielereien program.
     */
    public static void main(String[] args) {
        Aktie audiAktie = new Aktie("Audi", 4444, 10000);
        Kunde kunde = new Kunde("Tarik", "Balci", "MusterStr", LocalDate.parse("2001-07-28"));
        Konto konto = new Girokonto(kunde, 1111, 0);

        konto.einzahlen(150000);
        konto.kaufauftrag(audiAktie, 5, 9800);
        konto.verkaufauftrag("4444",9900);

    }
}
