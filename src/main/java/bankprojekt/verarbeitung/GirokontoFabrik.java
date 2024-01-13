package bankprojekt.verarbeitung;

import bankprojekt.verwaltung.Kontofabrik;

public class GirokontoFabrik implements Kontofabrik {
    @Override
    public Konto createKonto(Kunde inhaber, long kontonummer) {
        return new Girokonto(inhaber, kontonummer,1000);
    }
}
