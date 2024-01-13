package bankprojekt.verarbeitung;

public class KontoObserver implements Observer {
    private String name;

    public KontoObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(name + " is notified: " + message);
    }
}
