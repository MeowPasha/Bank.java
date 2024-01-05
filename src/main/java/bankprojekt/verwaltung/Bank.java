package bankprojekt.verwaltung;

import bankprojekt.verarbeitung.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * This class represents a bank with the ability to manage various bank accounts.
 */
public class Bank implements Cloneable, Serializable {
    private long bankleitzahl;
    private final long baseNumber = 1000;
    private final long maxNumber = 10000;
    private long dispo = 1000;
    private long counter = 0;
    private Map<Long, Konto> kontenListe = new HashMap<>();

    /**
     * Constructor to initialize a bank with a specified routing number.
     *
     * @param bankleitzahl The bank's routing number.
     */
    public Bank(long bankleitzahl) {
        this.bankleitzahl = bankleitzahl;
    }

    /**
     * Returns the bank's routing number.
     *
     * @return The bank's routing number.
     */
    public long getBankleitzahl() {
        return this.bankleitzahl;
    }

    public long mockEinfuegen(Konto k) {
        if (k == null) {
            throw new NullPointerException();
        }
        long kontoNummer = kontoNummerErsteller();
        kontenListe.put(kontoNummer, k);
        return kontoNummer;
    }

    /**
     * Creates a checking account (Girokonto) for a given customer and returns the account number.
     *
     * @param inhaber The account holder (customer).
     * @return The account number.
     */
    public long girokontoErstellen(Kunde inhaber) {
        if (inhaber == null) {
            throw new NullPointerException();
        }
        long kontoNummer = kontoNummerErsteller();
        Konto giroKonto = new Girokonto(inhaber, kontoNummer, dispo);
        kontenListe.put(kontoNummer, giroKonto);
        return kontoNummer;
    }

    public long sparbuchErstellen(Kunde inhaber) {
        if (inhaber == null) {
            throw new NullPointerException();
        }
        long kontoNummer = kontoNummerErsteller();
        Konto giroKonto = new Sparbuch(inhaber, kontoNummer);
        kontenListe.put(kontoNummer, giroKonto);
        return kontoNummer;
    }

    /**
     * Retrieves information about all accounts in the bank, including their account numbers and balances, and returns it as a string.
     *
     * @return A string containing account information.
     */
    public String getAlleKonten() {
        StringBuilder ausgabe = new StringBuilder();
        for (Map.Entry<Long, Konto> entry : kontenListe.entrySet()) {
            Long kontoNummer = entry.getKey();
            Konto konto = entry.getValue();
            double kontoStand = konto.getKontostand();

            ausgabe.append("Kontonummer: ").append(kontoNummer).append(", Kontostand: ").append(kontoStand).append("\n");
        }
        return ausgabe.toString();
    }

    /**
     * Retrieves a list of all account numbers in the bank.
     *
     * @return A list of account numbers.
     */
    public List<Long> getAlleKontonummern() {
        List<Long> kontoNummerList = new ArrayList<>();
        for (Long kontonummer : kontenListe.keySet()) {
            kontoNummerList.add(kontonummer);
        }
        return kontoNummerList;
    }

    /**
     * Allows a customer to withdraw money from their account.
     *
     * @param von    The account number from which the money will be withdrawn.
     * @param betrag The amount to be withdrawn.
     * @return True if the withdrawal was successful, false if not.
     * @throws GesperrtException                  If the account is locked.
     * @throws KontonummerNichtVorhandenException If the account number is not found.
     * @throws IllegalArgumentException           If the withdrawal amount is non-positive.
     */
    public boolean geldAbheben(long von, double betrag) throws GesperrtException, KontonummerNichtVorhandenException {
        if (betrag <= 0) {
            throw new IllegalArgumentException("Sie können keinen negativen Geldbetrag abheben!");
        }
        if (kontoNummerChecker(von)) {
            Konto konto = kontenListe.get(von);
            return konto.abheben(betrag);
        } else {
            throw new KontonummerNichtVorhandenException();
        }
    }

    /**
     * Allows a customer to deposit money into their account.
     *
     * @param auf    The account number where the money will be deposited.
     * @param betrag The amount to be deposited.
     * @throws KontonummerNichtVorhandenException If the account number is not found.
     * @throws IllegalArgumentException           If the deposit amount is non-positive.
     */
    public void geldEinzahlen(long auf, double betrag) throws KontonummerNichtVorhandenException {
        if (betrag <= 0) {
            throw new IllegalArgumentException("Sie können keinen negativen Geldbetrag einzahlen!");
        }
        if (kontoNummerChecker(auf)) {
            Konto konto = kontenListe.get(auf);
            konto.einzahlen(betrag);
        } else {
            throw new KontonummerNichtVorhandenException();
        }

    }

    /**
     * Deletes an account with the specified account number.
     *
     * @param nummer The account number to be deleted.
     * @return True if the account was successfully deleted, false if the account number is not found.
     */
    public boolean kontoLoeschen(long nummer) {
        if (kontoNummerChecker(nummer)) {
            kontenListe.remove(nummer);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves the balance of an account with the specified account number.
     *
     * @param nummer The account number for which the balance is to be retrieved.
     * @return The account balance.
     * @throws KontonummerNichtVorhandenException If the account number is not found.
     */
    public double getKontostand(long nummer) throws KontonummerNichtVorhandenException {
        if (kontoNummerChecker(nummer)) {
            Konto konto = kontenListe.get(nummer);
            return konto.getKontostand();
        } else {
            throw new KontonummerNichtVorhandenException();
        }
    }

    /**
     * Allows a customer to transfer money between two accounts.
     *
     * @param vonKontonr       The account number from which the money will be sent.
     * @param nachKontonr      The account number to which the money will be sent.
     * @param betrag           The amount to be transferred.
     * @param verwendungszweck A description or purpose of the transfer.
     * @return True if the transfer was successful, false if not.
     * @throws KontonummerNichtVorhandenException If either account number is not found.
     * @throws GesperrtException                  If one of the accounts is locked.
     */
    public boolean geldUeberweisen(long vonKontonr, long nachKontonr, double betrag, String verwendungszweck)
            throws KontonummerNichtVorhandenException, GesperrtException {

        if (!kontoNummerChecker(vonKontonr)) {
            throw new KontonummerNichtVorhandenException();
        }
        if (!kontoNummerChecker(nachKontonr)) {
            throw new KontonummerNichtVorhandenException();
        }

        Konto vonKonto = kontenListe.get(vonKontonr);
        Konto nachKonto = kontenListe.get(nachKontonr);

        if (vonKonto instanceof Ueberweisungsfaehig && nachKonto instanceof Ueberweisungsfaehig) {
            Ueberweisungsfaehig konto1 = (Ueberweisungsfaehig) vonKonto;
            Ueberweisungsfaehig konto2 = (Ueberweisungsfaehig) nachKonto;
            konto1.ueberweisungAbsenden(betrag, vonKonto.getInhaber().getName(), vonKontonr, vonKonto.getKontonummer(), verwendungszweck);
            konto2.ueberweisungEmpfangen(betrag, nachKonto.getInhaber().getName(), nachKontonr, nachKonto.getKontonummer(), verwendungszweck);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Generates a new, unique account number.
     *
     * @return A new unique account number.
     */
    public long kontoNummerErsteller() {
        counter++;
        return baseNumber + counter;
    }

    /**
     * Checks if an account with the specified account number exists.
     *
     * @param nummer The account number to check.
     * @return True if an account with the given account number exists, false otherwise.
     */
    public boolean kontoNummerChecker(long nummer) {
        for (Long kontonummer : kontenListe.keySet()) {
            if (kontonummer == nummer) {
                return true;
            }
        }
        return false;
    }

    /**
     * Locks accounts with a negative balance.
     */
    public void pleitegeierSperren() {
        kontenListe.values()
                .stream()
                .filter(konto -> konto.getKontostand() < 0)
                .forEach(Konto::sperren);
    }

    /**
     * Retrieves a list of customers with an account balance equal to or greater than a specified minimum.
     *
     * @param minimum The minimum account balance.
     * @return A list of customers.
     */
    public List<Kunde> getKundenMitVollemKonto(double minimum) {
        List<Kunde> kundeList = kontenListe.values()
                .stream()
                .filter(konto -> konto.getKontostand() >= minimum)
                .map(Konto::getInhaber)
                .toList();
        return kundeList;
    }

    /**
     * Retrieves a string containing unique customer names and addresses.
     *
     * @return A string containing customer names and addresses.
     */
    public String getKundenadressen() {
        String kundenNamenUndAdressen = kontenListe.values()
                .stream()
                .map(konto -> {
                            Kunde kunde = konto.getInhaber();
                            return kunde.getName() + "," + kunde.getAdresse();
                        }
                )
                .distinct()
                .sorted()
                .toString();
        return kundenNamenUndAdressen;
    }

    /**
     * Retrieves a list of available account numbers without duplicates.
     *
     * @return A list of available account numbers.
     */
    public List<Long> getKontonummernLuecken() {
        List<Long> avalibleNumbers = Stream.concat(
                        LongStream.range(baseNumber, maxNumber).boxed(),
                        kontenListe.values().stream().map(Konto::getKontonummer))
                .collect(Collectors.toMap(
                        l -> l,
                        l -> l,
                        (existing, replacement) -> replacement))
                .values()
                .stream()
                .toList();
        return avalibleNumbers;
    }

    /**
     * Returns a cloned copy of the current object.
     *
     * @return A cloned copy of the object.
     */
    @Override
    public byte[] clone() {
        return serialize(this);
    }

    /**
     * Serializes the current object into a byte array.
     *
     * @return A byte array representing the serialized object.
     */
    public static byte[] serialize(Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Custom exception class to handle cases where an account number is not found.
     */
    public static class KontonummerNichtVorhandenException extends Exception {
        public KontonummerNichtVorhandenException() {
            super("Kontonummer ist nicht vorhanden!");
        }

    }

    @Override
    public String toString() {
        return "Bankleitzahl: " + getBankleitzahl() + ". \nAlle Konten: \n" + getAlleKonten();
    }
}
