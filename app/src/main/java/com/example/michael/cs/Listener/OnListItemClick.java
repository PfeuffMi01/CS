package com.example.michael.cs.Listener;

/**
 * Um Fragmente zu informieren, dass für ein geklicktes Element der Dialog aufgehen soll
 */

public interface OnListItemClick {

    public void openDialog(int adapterPosition, int listItemType);
}
