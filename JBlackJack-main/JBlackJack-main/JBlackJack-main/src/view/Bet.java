package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Interfaccia per puntare.
 */
public class Bet extends JPanel {

    // importo attuale
    private int importo;

    // puntata attuale
    public int puntata = 0;

    // Bottone per piazzare la puntata
    private JButton betButton;

    // Mappa per associare il valore della chip a pulsanti
    private final Map<Integer, CircularButton> chipButtons = new LinkedHashMap<>();

    // Mappa per associare il valore della chip alle immagini delle chip
    private final Map<Integer, Image> chipImages = new LinkedHashMap<>();

    // Costanti per dimensioni e posizionamento delle chip
    private static final int CHIP_WIDTH = 70;
    private static final int CHIP_HEIGHT = 70;
    private static final int SPACE_BETWEEN_CHIPS = 20;
    private static final int TOTAL_WIDTH = (CHIP_WIDTH * 6) + SPACE_BETWEEN_CHIPS * 5;
    private static final int START_X = (Controller.BOARD_WIDTH - TOTAL_WIDTH) / 2;
    private static final int START_Y = Controller.BOARD_HEIGHT * 11 / 16;

    /**
     * Costruttore della classe Bet
     * 
     * @param importo L'importo iniziale del giocatore
     */
    public Bet(int importo) {
        this.importo = importo;
        setBackground(Color.BLACK);
        setLayout(null);

        initBetButton();
        loadChipImages();
        initChipButtons();
        createAndAddChipButtons();
    }

    /**
     * inizializza il bottone per puntare
     */
    private void initBetButton() {
        betButton = new JButton("Vai!");
        betButton.setBounds(Controller.BOARD_WIDTH / 2 - 50, Controller.BOARD_HEIGHT * 6 / 7 - 25, 100, 50);
        add(betButton);
    }

    /**
     * carica le immagini delle chip
     */
    private void loadChipImages() {
        int[] values = {1, 5, 25, 100, 500, 1000};
        for (int value : values) {
            URL chipImageURL = getClass().getResource("/BlackJack/resources/images/chips/" + value + ".png");
            ImageIcon chipIcon = new ImageIcon(chipImageURL);
            Image chipImage = chipIcon.getImage().getScaledInstance(CHIP_WIDTH, CHIP_HEIGHT, Image.SCALE_SMOOTH);
            chipImages.put(value, chipImage);
        }
    }

    /**
     * Inizializza i pulsanti circolari per ogni chip e associa i valori delle chip
     */
    private void initChipButtons() {
        for (Map.Entry<Integer, Image> entry : chipImages.entrySet()) {
            CircularButton button = createChipButton(entry.getValue());
            chipButtons.put(entry.getKey(), button);
        }
    }

    /**
     * Crea un pulsante circolare con l'immagine della chip
     * 
     * @param chipImage L'immagine della chip da utilizzare nel pulsante
     * @return Un pulsante circolare con l'immagine della chip
     */
    private CircularButton createChipButton(Image chipImage) {
        return new CircularButton(new ImageIcon(chipImage));
    }

    /**
     * Posiziona i pulsanti delle chip nel pannello e assegna i relativi action listener
     */
    private void createAndAddChipButtons() {
        List<Map.Entry<Integer, CircularButton>> entries = new ArrayList<>(chipButtons.entrySet());
        int startX = Controller.BOARD_WIDTH - CHIP_WIDTH - 20;
        int startY = Controller.BOARD_HEIGHT / 4;

        for (int i = 0; i < entries.size(); i++) {
            CircularButton button = entries.get(i).getValue();
            int chipValue = entries.get(i).getKey();

            // Posiziona le chip una sotto l'altra
            button.setBounds(startX, startY + (CHIP_HEIGHT + 10) * i, CHIP_WIDTH, CHIP_HEIGHT);
            button.addActionListener(e -> handleChipClick(chipValue));
            add(button);
        }
    }

    /**
     * Gestisce la parte audio e aggiorna importo e puntata toale
     * 
     * @param chipValue Il valore della chip selezionata
     */
    private void handleChipClick(int chipValue) {
        if (importo < chipValue) {
            JOptionPane.showMessageDialog(this, "Importo insufficiente per la chip selezionata.");
            return;
        }

        importo -= chipValue;
        puntata += chipValue;

        AudioManager.getInstance().play("/BlackJack/resources/audio/chip.wav");

        // Ridisegna il pannello per aggiornare la visualizzazione
        repaint();
    }

    /**
     * Override del metodo paintComponent per disegnare le informazioni dell'importo e della puntata
     * 
     * @param g L'oggetto Graphics per disegnare nel pannello
     */
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("Importo: " + importo, 60, 300);
        g.drawString("Stai puntando: " + puntata, 60, 350);
    }

    /**
     * Aggiunge un listener al bottone della puntata
     * 
     * @param listener L'ActionListener da aggiungere
     */
    public void addBetButtonListener(ActionListener listener) {
        removeBetButtonListeners();
        betButton.addActionListener(listener);
    }

    /**
     * Rimuove tutti i listener associati al bottone della puntata
     */
    public void removeBetButtonListeners() {
        for (ActionListener al : betButton.getActionListeners()) {
            betButton.removeActionListener(al);
        }
    }

    /**
     * Classe interna CircularButton che rappresenta un pulsante circolare
     */
    public class CircularButton extends JButton {
        public CircularButton(Icon icon) {
            super(icon);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
        }

        /**
         * Override del metodo paintComponent per disegnare il pulsante circolare
         * 
         * @param g L'oggetto Graphics per disegnare il pulsante
         */
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getModel().isArmed() ? Color.LIGHT_GRAY : getBackground());
            g.fillOval(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }
}

