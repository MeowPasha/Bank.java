package spielereien;

import bankprojekt.verarbeitung.Kunde;
import bankprojekt.verwaltung.Bank;

import java.io.*;
import java.time.LocalDate;

public class BankSpielereien {
    public static void main(String[] args) {
        Bank bank = new Bank(1234);
        Kunde kunde = new Kunde("Muster", "Mustermann", "MusterSTR", LocalDate.parse("2001-07-28"));
        Kunde kunde1 = new Kunde("Muster","Musterfrau","MusterSTR",LocalDate.parse("2007-01-18"));
        bank.girokontoErstellen(kunde);
        bank.girokontoErstellen(kunde1);

        byte [] clonedBank = bank.clone();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(clonedBank);

        for(int i= 0; i < clonedBank.length; i++) {
            int data = byteArrayInputStream.read();
            System.out.printf(Character.toString(data) + " ");
        }

        System.out.println("\n------------------------\n");


        try (ByteArrayInputStream byteArrayInputStream1 = new ByteArrayInputStream(clonedBank);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream1)) {

            Object o1 = objectInputStream.readObject();
            if (o1 instanceof Bank clonedBankObject) {
                System.out.println(clonedBankObject);
            } else {
                System.out.println("Not a Bank object");
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
