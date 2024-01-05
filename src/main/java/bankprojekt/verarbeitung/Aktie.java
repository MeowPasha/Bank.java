package bankprojekt.verarbeitung;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The Aktie class represents a stock with a dynamic stock price that updates periodically.
 * @author tsblc
 */
public class Aktie implements Serializable {
    private final String name;
    private final long wertpapierId;
    private double kurs;

    /**
     * Constructs an Aktie object with the specified name, stock ID, and initial stock price.
     * The stock price is dynamically updated at fixed intervals.
     *
     * @param name        The name of the stock.
     * @param wertpapierId The unique identifier for the stock.
     * @param kurs        The initial stock price.
     */
    public Aktie(String name, long wertpapierId, double kurs) {
        this.name = name;
        this.wertpapierId = wertpapierId;
        this.kurs = kurs;

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(this::updateStockPrice, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Updates the stock price at fixed intervals with a random percentage change.
     */
    private void updateStockPrice() {
        // Generate a random number between -3 and 3
        double randomChange = -3 + 6 * new Random().nextDouble();

        // Update the stock price by the corresponding percentage
        BigDecimal kursBigDecimal = BigDecimal.valueOf(kurs);
        BigDecimal randomChangeBigDecimal = BigDecimal.valueOf(randomChange);
        BigDecimal updatedKursBigDecimal = kursBigDecimal.add(kursBigDecimal.multiply(
                randomChangeBigDecimal.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)));

        // Format the updated stock price to have only two decimal places
        kurs = updatedKursBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();

        System.out.println("New Stock Price for " + name + ": " + kurs);
    }

    /**
     * Gets the current stock price.
     *
     * @return The current stock price.
     */
    public double getAktuellerPreis() {
        return kurs;
    }

    /**
     * Gets the name of the stock.
     *
     * @return The name of the stock.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the unique identifier for the stock.
     *
     * @return The unique identifier for the stock.
     */
    public long getWertpapierId() {
        return wertpapierId;
    }
}



