package bankprojekt.verarbeitung;

import bankprojekt.verwaltung.Kontofabrik;

public class SparbuchFabrik implements Kontofabrik {
    @Override
    public Konto createKonto(Kunde inhaber, long kontonummer) {
        return new Sparbuch(inhaber,kontonummer);
    }
}
