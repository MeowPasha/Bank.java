import bankprojekt.verarbeitung.Kunde;
import bankprojekt.verwaltung.Bank;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class BankTest {

    @Test
    public void checkIfCloneAndOriginalIsSame() {
        Bank bank = new Bank(1234);
        Kunde kunde = new Kunde("Tarik", "Balci", "MusterSTR", LocalDate.parse("2001-07-28"));
        bank.girokontoErstellen(kunde);

        byte[] bankSerialized = Bank.serialize(bank);
        byte[] cloneBank = bank.clone();

        Assert.assertArrayEquals(bankSerialized, cloneBank);
    }

    @Test
    public void checkIfCloneAndOriginalUnabhaengig() {
        Bank bank = new Bank(1234);
        Kunde kunde = new Kunde("Tarik", "Balci", "MusterSTR", LocalDate.parse("2001-07-28"));
        Kunde kunde1 = new Kunde("Muster","Mustermann","MusterSTR",LocalDate.parse("1999-10-01"));
        bank.girokontoErstellen(kunde);

        byte[] cloneBank = bank.clone();

        // add another Konto, after the clone method.
        bank.girokontoErstellen(kunde1);

        byte[] bankSerialized = Bank.serialize(bank);

        Assert.assertNotEquals(cloneBank,bankSerialized);
    }
}
