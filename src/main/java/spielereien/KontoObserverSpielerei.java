package spielereien;

import bankprojekt.verarbeitung.*;

public class KontoObserverSpielerei {
    public static void main(String[] args) throws GesperrtException {
        PropertyChangeListenerKonto propertyChangeListenerKonto = new PropertyChangeListenerKonto();

        Konto konto = new Girokonto() {};
        konto.addPropertyChangeListener(propertyChangeListenerKonto);

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
