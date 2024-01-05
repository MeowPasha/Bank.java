package bankprojekt.verarbeitung;

import org.decimal4j.util.DoubleRounder;

import java.io.Serializable;

/**
 * Enum to represent different currencies and their exchange rates to Euro (EUR).
 * Supported currencies:
 * - EUR: Euro (1 EUR = 1 EUR)
 * - BGN: Bulgarian Lev (1 BGN = 1.9558 EUR)
 * - MKD: Macedonian Denar (1 MKD = 61.62 EUR)
 * - DKK: Danish Krone (1 DKK = 7.4604 EUR)
 */
public enum Waehrung implements Serializable {
    EUR(1),
    BGN(1.9558),
    MKD(61.62),
    DKK(7.4604);

    private final double wert;

    /**
     * Constructor to initialize the exchange rate for each currency.
     *
     * @param zahl The exchange rate of the currency to Euro (EUR).
     */
    Waehrung(double zahl) {
        this.wert = zahl;
    }

    /**
     * Converts the given amount from Euro (EUR) to the specific currency.
     *
     * @param betrag The amount in Euro to be converted.
     * @return The equivalent amount in the specific currency.
     */
    public double euroInWaehrungUmrechnen(double betrag) {
        return DoubleRounder.round((betrag * this.wert), 2);
    }

    /**
     * Converts the given amount from the specific currency to Euro (EUR).
     *
     * @param betrag The amount in the specific currency to be converted.
     * @return The equivalent amount in Euro.
     */
    public double waehrungInEuroUmrechnen(double betrag) {
        return DoubleRounder.round((betrag / this.wert),2);
    }
}
