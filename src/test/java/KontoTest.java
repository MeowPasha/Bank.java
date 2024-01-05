import static org.junit.jupiter.api.Assertions.*;

import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Waehrung;
import org.junit.jupiter.api.Test;

import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Konto;

public class KontoTest {

    @Test
    public void konstruktorTest() {
        Konto k = new Girokonto();
        assertEquals(0.0, k.getKontostand(), "Falscher Kontostand");
    }

    //Abheben Tests mit zwei Parameter:

    @Test
    public void negativeBetrag() {
        Konto k = new Girokonto();

        assertThrows(IllegalArgumentException.class, () -> {
            k.abheben(-500, Waehrung.EUR);
        });
    }

    @Test
    public void infinityPositiveBetrag() {
        Konto k = new Girokonto();
        double positiveInfinity = Double.POSITIVE_INFINITY;

        assertThrows(IllegalArgumentException.class, () -> {
            k.abheben(positiveInfinity, Waehrung.EUR);
        });
    }

    @Test
    public void betragIsNaN() {
        Konto k = new Girokonto();
        double nanBetrag = Double.NaN;

        assertThrows(IllegalArgumentException.class, () -> {
            k.abheben(nanBetrag, Waehrung.EUR);
        });
    }

    @Test
    public void infinityNegativeBetrag() {
        Konto k = new Girokonto();
        double negativeInfinity = Double.NEGATIVE_INFINITY;

        assertThrows(IllegalArgumentException.class, () -> {
            k.abheben(negativeInfinity, Waehrung.EUR);
        });
    }

    @Test
    public void kontoIsGesprrtWaehrungAbheben() {
        Konto k = new Girokonto();
        k.sperren();

        assertThrows(GesperrtException.class, () -> {
            k.abheben(100, Waehrung.EUR);
        });
    }

    //Abheben Tests mit einem Parameter:
    @Test
    public void kontoIstNichtGesprrtAbheben() throws GesperrtException {
        Konto k = new Girokonto();

        boolean abhebenResult = k.abheben(10);
        assertTrue(abhebenResult);
    }

    @Test
    public void kontoIstGesprrtAbheben() {
        Konto k = new Girokonto();
        k.sperren();

        assertThrows(GesperrtException.class, () -> {
            k.abheben(100);
        });
    }

    @Test
    public void notSmallerThanNegativeDispo() throws GesperrtException {
        Konto k = new Girokonto();
        boolean abhebenResult = k.abheben(1500);

        assertFalse(abhebenResult);
    }

    //Waehrung tests:
    @Test
    public void eurToWaehrung() {
        Konto k = new Girokonto();

        k.waehrungswechsel(Waehrung.EUR);
        k.waehrungswechsel(Waehrung.DKK);
        assertEquals(Waehrung.DKK, k.getAktuelleWaehrung());
    }

    @Test
    public void waehrungToEur() {
        Konto k = new Girokonto();

        k.waehrungswechsel(Waehrung.DKK);
        k.waehrungswechsel(Waehrung.EUR);
        assertEquals(Waehrung.EUR, k.getAktuelleWaehrung());
    }

    @Test
    public void waehrungToWaehrung() {
        Konto k = new Girokonto();

        k.waehrungswechsel(Waehrung.BGN);
        k.waehrungswechsel(Waehrung.DKK);

        assertEquals(Waehrung.DKK, k.getAktuelleWaehrung());
    }

    @Test
    public void waehrungswechselWaehrungToEur() {
        Konto k = new Girokonto();
        k.waehrungswechsel(Waehrung.DKK);

        assertNotEquals(Waehrung.EUR, k.getAktuelleWaehrung());
        k.waehrungswechsel(Waehrung.EUR);
        assertEquals(Waehrung.EUR, k.getAktuelleWaehrung());
    }


}