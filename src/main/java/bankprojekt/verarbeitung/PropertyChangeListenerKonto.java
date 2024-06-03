package bankprojekt.verarbeitung;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PropertyChangeListenerKonto implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        System.out.println("Something changed: " + event.getPropertyName()
                + "old value " + event.getOldValue() + "new value " + event.getNewValue());
    }
}
