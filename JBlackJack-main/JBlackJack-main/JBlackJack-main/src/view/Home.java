package view;

import controller.Controller;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Schermata Home
 */
public class Home extends JPanel {

    private String nickname;
    private int winCount;
    private int defeatCount;
    private int drawCount;
    private int importo;
    private Image avatarImage;

    private JButton onePlayerButton;
    private JButton twoPlayersButton;
    private JButton threePlayersButton;

    /**
     * Costruttore della classe Home
     * 
     * @param nickname 
     * @param avatar     
     * @param winCount   
     * @param defeatCount
     * @param drawCount
     * @param importo
     */
    public Home(String nickname, int avatar, int winCount, int defeatCount, int drawCount, int importo) {
        this.nickname = nickname;
        this.winCount = winCount;
        this.defeatCount = defeatCount;
        this.drawCount = drawCount;
        this.importo = importo;
        this.avatarImage = loadAvatarImage(avatar);

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Pannello superiore con le informazioni del giocatore
        JPanel playerInfoPanel = createPlayerInfoPanel();
        add(playerInfoPanel, BorderLayout.NORTH);

        // Pannello centrale con la scritta "Gioca contro:" e i bottoni
        JPanel playersPanel = createPlayersPanel();
        add(playersPanel, BorderLayout.CENTER);

        // Pannello inferiore con la frase "Sei pronto, [nickname]?"
        JPanel readyPanel = createReadyPanel();
        add(readyPanel, BorderLayout.SOUTH);
    }

    /**
     * Crea il pannello con le informazioni del giocatore
     * 
     * @return Il pannello creato
     */
    private JPanel createPlayerInfoPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Pannello con i dati di vittorie, sconfitte, pareggi, soldi
        JPanel dataPanel = new JPanel();
        dataPanel.setBackground(Color.BLACK);
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

        JLabel winsLabel = new JLabel("Vittorie: " + winCount);
        winsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        winsLabel.setForeground(Color.WHITE);
        dataPanel.add(winsLabel);

        JLabel defeatsLabel = new JLabel("Sconfitte: " + defeatCount);
        defeatsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        defeatsLabel.setForeground(Color.WHITE);
        dataPanel.add(defeatsLabel);

        JLabel drawsLabel = new JLabel("Pareggi: " + drawCount);
        drawsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        drawsLabel.setForeground(Color.WHITE);
        dataPanel.add(drawsLabel);

        JLabel moneyLabel = new JLabel("Soldi: $" + importo);
        moneyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        moneyLabel.setForeground(Color.WHITE);
        dataPanel.add(moneyLabel);

        // Aggiungi il pannello con questi dati
        panel.add(dataPanel);

        // Aggiungi l'avatar
        JLabel avatarLabel = new JLabel(new ImageIcon(avatarImage));
        panel.add(avatarLabel);

        return panel;
    }

    /**
     * Crea il pannello con la scritta "Gioca contro:" e i 3 bottoni
     * 
     * @return Il pannello creato
     */
    private JPanel createPlayersPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new GridBagLayout());

        // Crea un pannello interno per la scritta e i bottoni
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // scritta "Gioca contro:"
        JLabel playAgainstLabel = new JLabel("Gioca contro:");
        playAgainstLabel.setFont(new Font("Arial", Font.BOLD, 24));
        playAgainstLabel.setForeground(Color.WHITE);
        playAgainstLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(playAgainstLabel);

        // spazio tra la scritta e i bottoni
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Pannello per i bottoni
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.BLACK);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Bottone per 1 giocatore
        onePlayerButton = createRoundButton("1");
        buttonsPanel.add(onePlayerButton);

        // Bottone per 2 giocatori
        twoPlayersButton = createRoundButton("2");
        buttonsPanel.add(twoPlayersButton);

        // Bottone per 3 giocatori
        threePlayersButton = createRoundButton("3");
        buttonsPanel.add(threePlayersButton);

        // Aggiungi il pannello dei bottoni
        centerPanel.add(buttonsPanel);

        // Aggiungi il pannello centrale al pannello principale
        panel.add(centerPanel);
        return panel;
    }

    /**
     * Crea il pannello con la frase "Sei pronto, [nickname]?"
     * 
     * @return Il pannello creato
     */
    private JPanel createReadyPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        JLabel readyLabel = new JLabel("Sei pronto, " + nickname + "?");
        readyLabel.setFont(new Font("Arial", Font.BOLD, 24));
        readyLabel.setForeground(Color.WHITE);
        panel.add(readyLabel);

        return panel;
    }

    /**
     * Crea un bottone rotondo con il testo specificato
     * 
     * @param text Il testo del bottone.
     * @return Il bottone creato
     */
    private JButton createRoundButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isArmed()) {
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                g.setColor(Color.DARK_GRAY);
                g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(80, 80);
            }
        };
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.BLACK);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    /**
     * Carica l'immagine dell'avatar
     * 
     * @param avatar L'indice dell'avatar
     * @return L'immagine dell'avatar
     */
    private Image loadAvatarImage(int avatar) {
        String imagePath = String.format("/BlackJack/resources/images/avatars/avatar%d.png", avatar);
        Image image = new ImageIcon(getClass().getResource(imagePath)).getImage();
        return image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
    }

    /**
     * Aggiunge un listener al pulsante "1 Player"
     * 
     * @param listener
     */
    public void addOnePlayerButtonListener(ActionListener listener) {
        onePlayerButton.addActionListener(listener);
    }

    /**
     * Aggiunge un listener al pulsante "2 Players"
     * 
     * @param listener
     */
    public void addTwoPlayersButtonListener(ActionListener listener) {
        twoPlayersButton.addActionListener(listener);
    }

    /**
     * Aggiunge un listener al pulsante "3 Players"
     * 
     * @param listener
     */
    public void addThreePlayersButtonListener(ActionListener listener) {
        threePlayersButton.addActionListener(listener);
    }
}

