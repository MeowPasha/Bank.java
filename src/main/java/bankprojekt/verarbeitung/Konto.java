package bankprojekt.verarbeitung;

import com.google.common.primitives.Doubles;
import javafx.beans.property.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * stellt ein allgemeines Bank-Konto dar
 */
public abstract class Konto implements Comparable<Konto> {
    /**
     * der Kontoinhaber
     */
    private Kunde inhaber;

    /**
     * die Kontonummer
     */
    private final long nummer;

    /**
     * der aktuelle Kontostand
     */
    private DoubleProperty kontostand = new ReadOnlyDoubleWrapper();
    private Waehrung typ = Waehrung.EUR;

    private List<Aktie> aktienDepot;

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private transient final ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);

    private BooleanProperty minusPlus = new ReadOnlyBooleanWrapper();

    /**
     * Wenn das Konto gesperrt ist (gesperrt = true), können keine Aktionen daran mehr vorgenommen werden,
     * die zum Schaden des Kontoinhabers wären (abheben, Inhaberwechsel)
     */
    private BooleanProperty gesperrt = new SimpleBooleanProperty();

    /**
     * setzt den aktuellen Kontostand
     *
     * @param kontostand neuer Kontostand
     */
    public void setKontostand(double kontostand) {
        Double oldValue = this.getKontostand();
        this.kontostand.set(kontostand);

        if (getKontostand() < 0) {
            this.minusPlus.set(true);
        } else {
            this.minusPlus.set(false);
        }

        propertyChangeSupport.firePropertyChange("\n Your balance has been changed, ",
                ":" + oldValue + "" + getAktuelleWaehrung() + ". ", ":" + getKontostand() + "" + getAktuelleWaehrung() + "\n");
    }


    /**
     * Setzt die beiden Eigenschaften kontoinhaber und kontonummer auf die angegebenen Werte,
     * der anfängliche Kontostand wird auf 0 gesetzt.
     *
     * @param inhaber     der Inhaber
     * @param kontonummer die gewünschte Kontonummer
     * @throws IllegalArgumentException wenn der inhaber null ist
     */
    public Konto(Kunde inhaber, long kontonummer) {
        if (inhaber == null)
            throw new IllegalArgumentException("Inhaber darf nicht null sein!");
        this.inhaber = inhaber;
        this.nummer = kontonummer;
        this.kontostand.set(0);
        this.gesperrt.set(false);
        this.aktienDepot = new ArrayList<>();
    }

    /**
     * setzt alle Eigenschaften des Kontos auf Standardwerte
     */
    public Konto() {
        this(Kunde.MUSTERMANN, 1234567);
    }

    /**
     * liefert den Kontoinhaber zurück
     *
     * @return der Inhaber
     */
    public Kunde getInhaber() {
        return this.inhaber;
    }

    /**
     * setzt den Kontoinhaber
     *
     * @param kinh neuer Kontoinhaber
     * @throws GesperrtException        wenn das Konto gesperrt ist
     * @throws IllegalArgumentException wenn kinh null ist
     */
    public final void setInhaber(Kunde kinh) throws GesperrtException {
        if (kinh == null)
            throw new IllegalArgumentException("Der Inhaber darf nicht null sein!");
        if (this.gesperrt.get())
            throw new GesperrtException(this.nummer);
        this.inhaber = kinh;

    }

    /**
     * liefert den aktuellen Kontostand
     *
     * @return Kontostand
     */
    public double getKontostand() {
        return kontostand.get();
    }

    /**
     * liefert die Kontonummer zurück
     *
     * @return Kontonummer
     */
    public long getKontonummer() {
        return nummer;
    }

    /**
     * liefert zurück, ob das Konto gesperrt ist oder nicht
     *
     * @return true, wenn das Konto gesperrt ist
     */
    public boolean isGesperrt() {
        return gesperrt.get();
    }

    public ReadOnlyDoubleProperty kontostandProperty() {
        return kontostand;
    }

    public ReadOnlyBooleanProperty minusPlusProperty() {
        return minusPlus;
    }

    public ReadOnlyBooleanProperty gesperrtProperty() {
        return gesperrt;
    }

    /**
     * For adding propertyChangeListener
     *
     * @param listener, the listener which will get add.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * For removing propertyChangeListener
     *
     * @param listener, the listener which will get removed.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Erhöht den Kontostand um den eingezahlten Betrag.
     *
     * @param betrag double
     * @throws IllegalArgumentException wenn der betrag negativ ist
     */
    public void einzahlen(double betrag) {
        if (betrag < 0 || !Doubles.isFinite(betrag)) {
            throw new IllegalArgumentException("Falscher Betrag");
        }
        setKontostand(getKontostand() + betrag);
    }

    @Override
    public String toString() {
        String ausgabe;
        ausgabe = "Kontonummer: " + this.getKontonummerFormatiert()
                + System.getProperty("line.separator");
        ausgabe += "Inhaber: " + this.inhaber;
        ausgabe += "Aktueller Kontostand: " + getKontostandFormatiert() + " ";
        ausgabe += this.getGesperrtText() + System.getProperty("line.separator");
        return ausgabe;
    }

    /**
     * Mit dieser Methode wird der geforderte Betrag vom Konto abgehoben, wenn es nicht gesperrt ist
     * und die speziellen Abheberegeln des jeweiligen Kontotyps die Abhebung erlauben
     *
     * @param betrag double
     * @return true, wenn die Abhebung geklappt hat,
     * false, wenn sie abgelehnt wurde
     * @throws GesperrtException        wenn das Konto gesperrt ist
     * @throws IllegalArgumentException wenn der betrag negativ oder unendlich oder NaN ist
     */
    public boolean abheben(double betrag) throws GesperrtException {
        if (betrag <= 0 || Double.isNaN(betrag) || Double.isInfinite(betrag)) {
            return false;
        }
        if (this.geldCheck(betrag)) {
            setKontostand(getKontostand() - betrag);
            return true;
        }
        return false;
    }

    public abstract boolean geldCheck(double betrag);

    /**
     * sperrt das Konto, Aktionen zum Schaden des Benutzers sind nicht mehr möglich.
     */
    public void sperren() {
        this.gesperrt.set(true);
        propertyChangeSupport.firePropertyChange("Your account has been blocked! \n", ":unblocked, ", ":blocked!" + "\n");
    }

    /**
     * entsperrt das Konto, alle Kontoaktionen sind wieder möglich.
     */
    public final void entsperren() {
        this.gesperrt.set(false);
        propertyChangeSupport.firePropertyChange("Your account is unblocked! \n", ":blocked, ", ":unblocked!" + "\n");
    }


    /**
     * liefert eine String-Ausgabe, wenn das Konto gesperrt ist
     *
     * @return "GESPERRT", wenn das Konto gesperrt ist, ansonsten ""
     */
    public final String getGesperrtText() {
        if (this.gesperrt.get()) {
            return "GESPERRT";
        } else {
            return "";
        }
    }

    /**
     * liefert die ordentlich formatierte Kontonummer
     *
     * @return auf 10 Stellen formatierte Kontonummer
     */
    public String getKontonummerFormatiert() {
        return String.format("%10d", this.nummer);
    }

    /**
     * liefert den ordentlich formatierten Kontostand
     *
     * @return formatierter Kontostand mit 2 Nachkommastellen und Währungssymbol
     */
    public String getKontostandFormatiert() {
        if (getAktuelleWaehrung() == Waehrung.EUR) {
            return String.format("%10.2f €", this.getKontostand());
        }
        if (getAktuelleWaehrung() == Waehrung.BGN) {
            return String.format("%10.2f BGN", this.getKontostand());
        }
        if (getAktuelleWaehrung() == Waehrung.MKD) {
            return String.format("%10.2f MKD", this.getKontostand());
        }
        if (getAktuelleWaehrung() == Waehrung.DKK) {
            return String.format("%10.2f DKK", this.getKontostand());
        } else return "N/A";
    }

    /**
     * Tries to withdraw the specified amount in the given currency.
     *
     * @param betrag The amount to withdraw.
     * @param w      The currency of the withdrawal.
     * @return true if the withdrawal is successful; otherwise, false.
     * @throws GesperrtException Thrown if the account is locked.
     */

    public abstract boolean abheben(double betrag, Waehrung w) throws GesperrtException;

    /**
     * Deposits the specified amount in the given currency into the account.
     * If the depositing currency is different from the account's current currency, it performs the necessary conversion.
     *
     * @param betrag The amount to deposit.
     * @param w      The currency of the deposit.
     */

    public void einzahlen(double betrag, Waehrung w) {
        if (betrag <= 0) {
            return;
        }
        double tempBetrag = w.waehrungInEuroUmrechnen(betrag);
        tempBetrag = getAktuelleWaehrung().euroInWaehrungUmrechnen(tempBetrag);
        setKontostand(getKontostand() + tempBetrag);
    }

    /**
     * Gets the current currency of the account.
     *
     * @return The current currency.
     */

    public Waehrung getAktuelleWaehrung() {
        return typ;
    }

    /**
     * Changes the currency of the account and converts the balance if necessary.
     *
     * @param neu The new currency for the account.
     */

    public void waehrungswechsel(Waehrung neu) {
        Waehrung oldWaehrung = getAktuelleWaehrung();
        if (getAktuelleWaehrung() == Waehrung.EUR) {
            setKontostand(neu.euroInWaehrungUmrechnen(getKontostand()));
            typ = neu;
        }
        if (getAktuelleWaehrung() != Waehrung.EUR) {
            setKontostand(getAktuelleWaehrung().waehrungInEuroUmrechnen(getKontostand()));
            setKontostand(neu.euroInWaehrungUmrechnen(getKontostand()));
            typ = neu;
        }
        propertyChangeSupport.firePropertyChange("Your account currency has been changed, \n",
                ": " + oldWaehrung + " ", ":" + typ + "\n");
    }

    /**
     * Executes a buy order for a specified number of shares of a given stock (Aktie)
     * when its current price falls below a specified maximum price.
     *
     * @param a            The Aktie (stock) to be bought.
     * @param anzahl       The number of shares to buy.
     * @param hoechstpreis The maximum price at which the purchase is triggered.
     * @return A Future representing the completion of the buy order, returning the total purchase cost.
     */
    public Future<Double> kaufauftrag(Aktie a, int anzahl, double hoechstpreis) {
        service.submit(() -> {
            // Asynchronously wait for the stock price to fall below the specified maximum price
            double aktuellerPreis = a.getAktuellerPreis();
            while (aktuellerPreis > hoechstpreis) {
                try {
                    Thread.sleep(1000); // Wait 1 second between price checks
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                aktuellerPreis = a.getAktuellerPreis();
            }

            System.out.println("CurrentValue is under, " + hoechstpreis + "€. Now is the time to buy. Current Value: " + aktuellerPreis + "€");

            // Price has fallen, check if the account balance is sufficient
            double gesamtkaufpreis = aktuellerPreis * anzahl;
            if (getKontostand() >= gesamtkaufpreis) {
                System.out.println("Buying, " + anzahl + " " + a.getName() + "-stock for: " + gesamtkaufpreis + "€");

                // Perform the purchase
                this.setKontostand(getKontostand() - gesamtkaufpreis);
                System.out.println("Kontostand: " + kontostand + "€");

                for (int i = 0; i < anzahl; i++) {
                    aktienDepot.add(a);
                }
                return gesamtkaufpreis;
            }
            // Return 0.0 if the purchase was not successful
            return 0.0;
        });
        return null;
    }

    /**
     * Executes a sell order for all shares of a specified stock (Aktie) when its current price exceeds a specified minimum price.
     *
     * @param wkn          The Wertpapierkennnummer (stock ID) of the Aktie to be sold.
     * @param minimalpreis The minimum price at which the sale is triggered.
     * @return A Future representing the completion of the sell order, returning the total revenue from the sale.
     */
    public Future<Double> verkaufauftrag(String wkn, double minimalpreis) {
        List<Aktie> wantedAktie = new ArrayList<>();

        service.submit(() -> {
            for (Aktie aktie : aktienDepot) {
                if (Long.parseLong(wkn) == aktie.getWertpapierId()) {
                    wantedAktie.add(aktie);
                }
            }

            int howManyShares = wantedAktie.size();
            if (!wantedAktie.isEmpty() && wantedAktie.get(0).getAktuellerPreis() < minimalpreis) {
                einzahlen(howManyShares * minimalpreis);
            }

            System.out.println("Kontostand after the sell of: " + wantedAktie.get(0).getName() + " " + kontostand + "€");
            aktienDepot.removeAll(wantedAktie);

            return kontostand;
        });
        return null;
    }


    /**
     * Vergleich von this mit other; Zwei Konten gelten als gleich,
     * wen sie die gleiche Kontonummer haben
     *
     * @param other das Vergleichskonto
     * @return true, wenn beide Konten die gleiche Nummer haben
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (this.getClass() != other.getClass())
            return false;
        if (this.nummer == ((Konto) other).nummer)
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        return 31 + (int) (this.nummer ^ (this.nummer >>> 32));
    }

    @Override
    public int compareTo(Konto other) {
        if (other.getKontonummer() > this.getKontonummer())
            return -1;
        if (other.getKontonummer() < this.getKontonummer())
            return 1;
        return 0;
    }
}
