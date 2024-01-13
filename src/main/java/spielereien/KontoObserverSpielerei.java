package spielereien;

import bankprojekt.verarbeitung.*;

public class KontoObserverSpielerei {
    public static void main(String[] args) throws GesperrtException {
        // Gözlemcileri ve gözlemlenecek nesneyi oluşturalım
        KontoObserver observer1 = new KontoObserver("Observer 1");

        Konto konto = new Girokonto() {};

        // Gözlemcileri gözlemlenecek nesneye ekleyelim
        konto.addObserver(observer1);

        konto.einzahlen(1903);
        konto.einzahlen(3423);
        konto.abheben(213);
        konto.sperren();
        konto.entsperren();
        konto.einzahlen(1000, Waehrung.EUR);
        konto.waehrungswechsel(Waehrung.DKK);
        konto.waehrungswechsel(Waehrung.BGN);
        konto.waehrungswechsel(Waehrung.EUR);
    }
}
