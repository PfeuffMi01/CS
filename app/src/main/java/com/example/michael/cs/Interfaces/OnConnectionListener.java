package com.example.michael.cs.Interfaces;

/**
 * Wird vom {@link com.example.michael.cs.MQTTHandler} aufgerufen, um die Listener über
 * Erfolg oder Misserfolg des Verbindungsaufbaus zu informieren
 */
public interface OnConnectionListener {

    /**
     * Wenn die Verbindung erfolgreich war
     * - ggf die Snackbar entfernen
     * - Das Lade-Layout ausblenden
     * - Das richtige Layout einblenden
     *
     * Abhängig vom Ergebnis den Subtitel der Toolbar ändern
     *
     * Wird auch durch langen Klick auf das "Verbindungsfehler"-Image zwangsweise mit true aufgerufen
     * @param isConnectionSuccessful
     * @param forcedAppEntering Indikator ob man per langen Klick auf das "Verbindungsfehler"
     * Image die App trotzdem betreten hat (die Toolbar muss dann trotzdem "Keine Verbindung" anzeigen)
     */
    public void onMQTTConnection(boolean isConnectionSuccessful, boolean forcedAppEntering);

}
