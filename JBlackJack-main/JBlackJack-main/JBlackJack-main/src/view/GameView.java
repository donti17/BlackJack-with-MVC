package view;

import model.Model;


import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Interfaccia che rappresenta una view del gioco
 * Fornisce metodi per la creaizone dei pulsanti "Hit" e "Stay"
 * aggiornare la view con il modello del gioco e gestire gli events
 */
public interface GameView {

    /**
     * Restituisce il pulsante per chiedere una carta dal mazzo
     * @return JButton associato all'azione "Hit"
     */
    JButton getHitButton();

    /**
     * Restituisce il pulsante per passare il turno
     * @return JButton associato all'azione "Stay"
     */
    JButton getStayButton();

    /**
     * Aggiorna la vista con i dati forniti dal modello del gioco
     * @param model Il modello del gioco da visualizzare
     */
    void updateView(Model model);

    /**
     * Aggiunge un listener a "Hit"
     * @param listener ActionListener da associare al pulsante "Hit"
     */
    default public void addHitButtonListener(ActionListener listener) {
        getHitButton().addActionListener(listener);
    }

    /**
     * Aggiunge un listener a "Stay"
     * @param listener ActionListener da associare al pulsante "Stay"
     */
    default public void addStayButtonListener(ActionListener listener) {
        getStayButton().addActionListener(listener);
    }

    /**
     * Abilita o disabilita  "Hit"
     * @param enabled 
     */
    default public void setHitButtonEnabled(boolean enabled) {
        getHitButton().setEnabled(enabled);
    }

    /**
     * Abilita o disabilita  "Stay"
     * @param enabled
     */
    default public void setStayButtonEnabled(boolean enabled) {
        getStayButton().setEnabled(enabled);
    }

    /**
     * Crea un pulsante con il testo (che sarà hit o stay)
     * @param text Il testo da visualizzare sul pulsante
     * @return JButton creato con il testo fornito
     */
    default public JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        return button;
    }
}

