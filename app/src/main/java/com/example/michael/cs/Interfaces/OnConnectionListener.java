package com.example.michael.cs.Interfaces;

import com.example.michael.cs.Handler.MQTTHandler;

/**
 * Wird vom {@link MQTTHandler} aufgerufen, um die Listener über
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
     * @param connectionIP
     */
    void onMQTTConnection(boolean isConnectionSuccessful, boolean forcedAppEntering, String connectionIP);

}
