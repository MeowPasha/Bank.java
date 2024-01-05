import bankprojekt.verarbeitung.*;
import bankprojekt.verwaltung.Bank;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BankTestMitMockito {
    private Bank bank;
    private Konto mockKonto;
    private Girokonto girokontoMock;

    @Test
    @DisplayName("KontoLöschen Normalcase.")
    public void kontoLoeschenNormalCase() {
        //SetUp:
        bank = mock(Bank.class);
        mockKonto = mock(Konto.class);
        long nummer = 123456789;
        when(bank.kontoNummerChecker(123456789L)).thenReturn(true);
        when(bank.mockEinfuegen(mockKonto)).thenReturn(123456789L);
        when(bank.kontoLoeschen(123456789L)).thenReturn(true);
        //Exercise:
        boolean result = bank.kontoLoeschen(nummer);
        //Verify:
        assertTrue(result);
    }

    @Test
    @DisplayName("KontoLöschen NummerNichtVorhanden.")
    public void kontoLoeschenNummerNichtVorhanden() {
        //SetUp:
        bank = mock(Bank.class);
        mockKonto = mock(Konto.class);
        long nummer = 123456789;
        when(bank.kontoNummerChecker(123456789L)).thenReturn(false);
        when(bank.mockEinfuegen(mockKonto)).thenReturn(123456789L);
        when(bank.kontoLoeschen(123456789L)).thenReturn(false);
        //Exercise:
        boolean result = bank.kontoLoeschen(nummer);
        //Verify:
        assertFalse(result);
    }

    @Test
    @DisplayName("GeldUeberweisenNormalCase")
    public void geldUeberweisenNormalCase() throws GesperrtException, Bank.KontonummerNichtVorhandenException {
        //SetUp:
        long kontonummer1, kontonummer2;
        bank = new Bank(1234);
        mockKonto = mock(Konto.class, withSettings().extraInterfaces(Ueberweisungsfaehig.class));
        girokontoMock = mock(Girokonto.class);

        kontonummer1 = bank.mockEinfuegen(mockKonto);
        kontonummer2 = bank.mockEinfuegen(girokontoMock);

        when(mockKonto.getInhaber()).thenReturn(new Kunde());
        when(girokontoMock.getInhaber()).thenReturn(new Kunde());

        when(((girokontoMock).ueberweisungAbsenden(
                anyDouble(), anyString(), anyLong(), anyLong(), anyString()))).thenReturn(true);
        //Exercise:
        boolean result = bank.geldUeberweisen(kontonummer2,
                kontonummer1, 100, "100€");
        //Verify:
        assertTrue(result);
    }

    @Test
    @DisplayName("Verifying that the mockKonto atleast called once in getInhaber(). ")
    public void mockKontoAtLeastVerify() throws GesperrtException, Bank.KontonummerNichtVorhandenException {
        //SetUp:
        long kontonummer1, kontonummer2;
        bank = new Bank(1234);
        mockKonto = mock(Konto.class, withSettings().extraInterfaces(Ueberweisungsfaehig.class));
        girokontoMock = mock(Girokonto.class);

        kontonummer1 = bank.mockEinfuegen(mockKonto);
        kontonummer2 = bank.mockEinfuegen(girokontoMock);

        when(mockKonto.getInhaber()).thenReturn(new Kunde());
        when(girokontoMock.getInhaber()).thenReturn(new Kunde());

        when(((girokontoMock).ueberweisungAbsenden(
                anyDouble(), anyString(), anyLong(), anyLong(), anyString()))).thenReturn(true);
        //Exercise:
        boolean result = bank.geldUeberweisen(kontonummer2,
                kontonummer1, 100, "100€");
        //Verify:
        verify(mockKonto,atLeast(1)).getInhaber();
    }

    @Test
    @DisplayName("Verifying that the girokontoMock atleast called once in getInhaber(). ")
    public void girokontoMockAtLeastVerify() throws GesperrtException, Bank.KontonummerNichtVorhandenException {
        //SetUp:
        long kontonummer1, kontonummer2;
        bank = new Bank(1234);
        mockKonto = mock(Konto.class, withSettings().extraInterfaces(Ueberweisungsfaehig.class));
        girokontoMock = mock(Girokonto.class);

        kontonummer1 = bank.mockEinfuegen(mockKonto);
        kontonummer2 = bank.mockEinfuegen(girokontoMock);

        when(mockKonto.getInhaber()).thenReturn(new Kunde());
        when(girokontoMock.getInhaber()).thenReturn(new Kunde());

        when(((girokontoMock).ueberweisungAbsenden(
                anyDouble(), anyString(), anyLong(), anyLong(), anyString()))).thenReturn(true);
        //Exercise:
        boolean result = bank.geldUeberweisen(kontonummer2,
                kontonummer1, 100, "100€");
        //Verify:
        verify(girokontoMock,atLeast(1)).getInhaber();
    }

    @Test
    @DisplayName("Verifying that the girokontoMock atleast called once in UeberweisungsAbsenden(). ")
    public void girokontoMockAtLeastVerifyUeberweisung() throws GesperrtException, Bank.KontonummerNichtVorhandenException {
        //SetUp:
        long kontonummer1, kontonummer2;
        bank = new Bank(1234);
        mockKonto = mock(Konto.class, withSettings().extraInterfaces(Ueberweisungsfaehig.class));
        girokontoMock = mock(Girokonto.class);

        kontonummer1 = bank.mockEinfuegen(mockKonto);
        kontonummer2 = bank.mockEinfuegen(girokontoMock);

        when(mockKonto.getInhaber()).thenReturn(new Kunde());
        when(girokontoMock.getInhaber()).thenReturn(new Kunde());

        when(((girokontoMock).ueberweisungAbsenden(
                anyDouble(), anyString(), anyLong(), anyLong(), anyString()))).thenReturn(true);
        //Exercise:
        boolean result = bank.geldUeberweisen(kontonummer2,
                kontonummer1, 100, "100€");
        //Verify:
        verify(girokontoMock,atLeast(1)).ueberweisungAbsenden(anyDouble(), anyString(), anyLong(), anyLong(), anyString());
    }

    @Test
    @DisplayName("Verifying that GesperrtException is thrown. ")
    public void gesperrtExceptionThrown() throws GesperrtException, Bank.KontonummerNichtVorhandenException {
        //SetUp:
        long kontonummer1, kontonummer2;
        bank = new Bank(1234);
        mockKonto = mock(Konto.class, withSettings().extraInterfaces(Ueberweisungsfaehig.class));
        girokontoMock = mock(Girokonto.class);

        kontonummer1 = bank.mockEinfuegen(mockKonto);
        kontonummer2 = bank.mockEinfuegen(girokontoMock);

        when(mockKonto.getInhaber()).thenReturn(new Kunde());
        when(girokontoMock.getInhaber()).thenReturn(new Kunde());

        when(((girokontoMock).ueberweisungAbsenden(
                anyDouble(), anyString(), anyLong(), anyLong(), anyString()))).thenThrow(new GesperrtException(kontonummer2));
        //Exercise:
        GesperrtException exception = assertThrows(GesperrtException.class, () -> {
            bank.geldUeberweisen(kontonummer2, kontonummer1, 100, "100€");
        });
        //Verify:
        assertNotNull(exception.getMessage());
        //assertEquals(kontonummer2, exception.getKontonummer());
    }
}
