package com.example.michael.cs.Interfaces;

/**
 * Um die Fragmente über Änderungen an den Daten zu informieren, damit sie ihre Adapter aktualisieren können
 */

public interface OnDataChangedListener {

    public void onDataHasChanged();

    public void onDataHasChanged(int position);
}
