package bankprojekt.verwaltung;

import bankprojekt.verarbeitung.Konto;
import bankprojekt.verarbeitung.Kunde;

public interface Kontofabrik {
    Konto createKonto(Kunde inhaber,long kontonummer);
}
