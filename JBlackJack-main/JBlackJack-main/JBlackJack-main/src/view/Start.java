package view;

import controller.Controller;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Schermata iniziale
 */
public class Start extends JPanel {

    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JLabel avatarLabel;
    private JButton enterButton;
    private JButton leftButton;
    private JButton rightButton;
    private int avatar = 1;

    /**
     * Costruttore della classe Start
     * Inizializza i componenti
     */
    public Start() {
        setLayout(null);
        setBackground(Color.BLACK);

        // Titolo "BENVENUTO!"
        titleLabel = new JLabel("BENVENUTO!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 50, Controller.BOARD_WIDTH, 60);
        add(titleLabel);

        // Sottotitolo "Scegli il tuo personaggio"
        subtitleLabel = new JLabel("Scegli il tuo personaggio", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        subtitleLabel.setForeground(Color.WHITE); // Colore del testo bianco
        subtitleLabel.setBounds(0, 120, Controller.BOARD_WIDTH, 30);
        add(subtitleLabel);

        // Immagine dell'avatar
        avatarLabel = new JLabel();
        avatarLabel.setBounds((Controller.BOARD_WIDTH - 200) / 2, 200, 200, 200);
        setAvatarImage("avatar1.png");
        add(avatarLabel);

        // Pulsante "Enter"
        enterButton = new JButton("Enter");
        enterButton.setBounds(Controller.BOARD_WIDTH / 2 - 50, Controller.BOARD_HEIGHT * 3 / 4, 100, 30);
        add(enterButton);

        // Pulsante "Left"
        leftButton = new JButton("<");
        leftButton.setBounds(Controller.BOARD_WIDTH / 2 - 100, Controller.BOARD_HEIGHT * 3 / 4, 50, 30);
        add(leftButton);

        // Pulsante "Right"
        rightButton = new JButton(">");
        rightButton.setBounds(Controller.BOARD_WIDTH / 2 + 50, Controller.BOARD_HEIGHT * 3 / 4, 50, 30);
        add(rightButton);
    }

    /**
     * Imposta l'immagine dell'avatar
     * 
     * @param imageName Il nome del file immagine dell'avatar
     */
    public void setAvatarImage(String imageName) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/BlackJack/resources/images/avatars/" + imageName));
        Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        avatarLabel.setIcon(new ImageIcon(image));
    }

    /**
     * Aggiunge un listener al pulsante "Enter"
     * 
     * @param listener L'ActionListener da aggiungere
     */
    public void addEnterButtonListener(ActionListener listener) {
        enterButton.addActionListener(listener);
    }

    /**
     * Aggiunge un listener al pulsante "Left"
     * 
     * @param listener L'ActionListener da aggiungere
     */
    public void addLeftButtonListener(ActionListener listener) {
        leftButton.addActionListener(listener);
    }

    /**
     * Aggiunge un listener al pulsante "Right"
     * 
     * @param listener L'ActionListener da aggiungere
     */
    public void addRightButtonListener(ActionListener listener) {
        rightButton.addActionListener(listener);
    }

    /**
     * Restituisce il nickname inserito dall'utente
     * 
     * @return Il nickname inserito
     */
    public String getNickname() {
        return JOptionPane.showInputDialog(this, "Inserisci il tuo nickname:");
    }
}

