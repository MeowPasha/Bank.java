
package bankprojekt.oberflaeche;

import bankprojekt.verarbeitung.GesperrtException;
import bankprojekt.verarbeitung.Girokonto;
import bankprojekt.verarbeitung.Konto;
import bankprojekt.verarbeitung.Kunde;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class KontoOberflaecheController extends BorderPane {
    private Konto konto;

    @FXML
    private Text ueberschrift;

    @FXML
    private Text nummer;

    @FXML
    private Text stand;

    @FXML
    private CheckBox gesperrt;

    @FXML
    private TextArea adresse;

    @FXML
    private Text meldung;

    @FXML
    private TextField betrag;

    @FXML
    private Button einzahlen;

    @FXML
    private Button abheben;

    @FXML
    private void initialize(Konto konto) {
        nummer.textProperty().set(konto.getKontonummerFormatiert());

    }


    @FXML
    void setKonto(Konto konto) {
        this.konto = konto;
    }

    @FXML
    private void einzahlen() {
        try {
            konto.einzahlen(Double.parseDouble(betrag.getText()));
            meldung.setText("Einzahlung erfolgreich");
            meldung.setFill(Color.GREEN);
        } catch (NumberFormatException ex) {
            meldung.setText("Bitte einen gültigen Betrag eingeben");
            meldung.setFill(Color.RED);
        }
    }

    @FXML
    private void abheben() {
        try {
            konto.abheben(Double.parseDouble(betrag.getText()));
            meldung.setText("Abhebung erfolgreich");
            meldung.setFill(Color.GREEN);
        } catch (NumberFormatException | GesperrtException ex) {
            meldung.setText("Bitte einen gültigen Betrag eingeben");
            meldung.setFill(Color.RED);
        }
    }
}
