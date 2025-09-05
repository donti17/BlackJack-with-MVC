package controller;

import model.Model;


import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller del progetto
 * gestisce la logica di gioco, gli ActionListener, il Game Loop
 */
public class Controller {
    // Costanti per le dimensioni del gioco
    public static final int BOARD_WIDTH = 1000;
    public static final int BOARD_HEIGHT = 800;
    public static final Color BACKGROUND_COLOR = new Color(53, 101, 77);
    
    // Componenti grafici
    public JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // views del gioco
    private Start startView;
    private Home homeView;
    private Bet betView;
    private GameView gameView;
    
    // il model del gioco
    private Model model;
    
    // Dati del giocatore
    private String nickname;
    private int avatar = 1;
    private int winCount = 0;
    private int defeatCount = 0;
    private int drawCount = 0;
    private int importo = 1000;
    private int puntata = 0;
    
    // Timer per il game loop
    private Timer gameLoopTimer;
    
    /**
     * Costruttore del controller
     * Inizializza: frame e le views
     */
    public Controller() {
        initializeFrame();
        initializeViews();
        setupActionListeners();
        showStartView();
    }
    
    /**
     * Inizializza il frame principale
     */
    private void initializeFrame() {
        frame = new JFrame("BlackJack");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);
    }
    
    /**
     * Inizializza solo lo start
     */
    private void initializeViews() {
        startView = new Start();
        mainPanel.add(startView, "start");
        
    }
    
    /**
     * Configura gli ActionListener per i 3 pulsanti dello start: freccia sx, freccia dx e Enter
     */
    private void setupActionListeners() {
        // ActionListener per lo Start
        startView.addEnterButtonListener(e -> handleEnterButton());
        startView.addLeftButtonListener(e -> handleLeftButton());
        startView.addRightButtonListener(e -> handleRightButton());
    }
    
    /**
     * Gestisce l'evento del pulsante Enter dello Start
     */
    private void handleEnterButton() {
        playClickSound();
        nickname = startView.getNickname();
        if (nickname != null && !nickname.trim().isEmpty()) {
            showHomeView();
        } else {
            JOptionPane.showMessageDialog(frame, "Inserisci un nickname valido!");
        }
    }
    
    /**
     * Gestisce l'evento del pulsante Left  dello Start
     */
    private void handleLeftButton() {
        playClickSound();
        avatar = (avatar > 1) ? avatar - 1 : 6;
        startView.setAvatarImage("avatar" + avatar + ".png");
    }
    
    /**
     * Gestisce l'evento del pulsante Right dello Start
     */
    private void handleRightButton() {
        playClickSound();
        avatar = (avatar < 6) ? avatar + 1 : 1;
        startView.setAvatarImage("avatar" + avatar + ".png");
    }
    
    /**
     * Mostra lo Start
     */
    private void showStartView() {
        cardLayout.show(mainPanel, "start");
    }
    
    /**
     * Mostra la Home
     */
    private void showHomeView() {
    	//se non esiste, dunque questo è il primo accesso,
        if (homeView == null) 
        {
        	//viene Crea una nuova istanza di Home passando i dati del giocatore,
            homeView = new Home(nickname, avatar, winCount, defeatCount, drawCount, importo);
            mainPanel.add(homeView, "home");
            
            //configura gli ActionListener per la vista Home: parita contro 1 giocatore, parita contro 2 giocatori, parita contro 3 giocatori
            homeView.addOnePlayerButtonListener(e -> startOnePlayerGame());
            homeView.addTwoPlayersButtonListener(e -> startTwoPlayersGame());
            homeView.addThreePlayersButtonListener(e -> startThreePlayersGame());
        } 
        
        //Se homeView esiste già, cioè è appena finita una partita e il giocatore è tornato alla home,
        else 
        {
            // elimina la view precedente,
            mainPanel.remove(homeView);
            // aggiorna la Home con i dati aggiornati,
            homeView = new Home(nickname, avatar, winCount, defeatCount, drawCount, importo);
            mainPanel.add(homeView, "home");
            
            // riconfigura gli ActionListener
            homeView.addOnePlayerButtonListener(e -> startOnePlayerGame());
            homeView.addTwoPlayersButtonListener(e -> startTwoPlayersGame());
            homeView.addThreePlayersButtonListener(e -> startThreePlayersGame());
        }
        cardLayout.show(mainPanel, "home");
    }
    
    /**
     * Avvia una partita con un giocatore
     */
    private void startOnePlayerGame() {
        playClickSound();
        showBetView(1);
    }
    
    /**
     * Avvia una partita con due giocatori
     */
    private void startTwoPlayersGame() {
        playClickSound();
        showBetView(2);
    }
    
    /**
     * Avvia una partita con tre giocatori
     */
    private void startThreePlayersGame() {
        playClickSound();
        showBetView(3);
    }
    
    /**
     * Mostra Bet
     * 
     * @param players Il numero di giocatori
     */
    private void showBetView(int players) {
        if (betView == null) {
            betView = new Bet(importo);
            mainPanel.add(betView, "bet");
        } else {
            // Aggiorna l'importo nel caso sia cambiato
            mainPanel.remove(betView);
            betView = new Bet(importo);
            mainPanel.add(betView, "bet");
        }
        
        // ActionListener per il pulsante di conferma della puntata
        betView.addBetButtonListener(e -> startGame(players));
        
        cardLayout.show(mainPanel, "bet");
    }
    
    /**
     * Avvia una partita.
     * 
     * @param players Il numero di giocatori
     */
    private void startGame(int players) {
        playClickSound();
        puntata = betView.puntata;
        
        if (puntata <= 0) {
            JOptionPane.showMessageDialog(frame, "Devi fare una puntata!");
            return;
        }
        
        // Inizializza il modello
        model = new Model(players);
        model.startNewGame();
        
        // Inizializza la giusta view di gioco :
        switch (players) {
        	// OnePlayer se il giocatore ha deciso di sfidare solo il banco
            case 1:
                gameView = new OnePlayer();
                break;
             // TwoPlayers se il giocatore ha deciso di sfidare 2 avversari
            case 2:
                gameView = new TwoPlayers();
                break;
             // ThreePlayers se il giocatore ha deciso di sfidare 3 avversi
            case 3:
                gameView = new ThreePlayers();
                break;
        }
        
        // Aggiungi la gameView al pannello principale
        mainPanel.add((JPanel) gameView, "game");
        
        // Configura il controller di gioco
        setupGameController();
        
        // Mostra la view del gioco
        cardLayout.show(mainPanel, "game");
        
        // Avvia il Game Loop
        startGameLoop();
    }
    
    /**
     * Configura il controller di gioco
     */
    private void setupGameController() {
        // Aggiungi il modello come Observable alla vista
        model.addObserver((java.util.Observer) gameView);
        
        // Configura gli ActionListener per i pulsanti della vista di gioco
        gameView.addHitButtonListener(e -> handleHitButton());
        gameView.addStayButtonListener(e -> handleStayButton());
        
        // Configura l'ActionListener per il pulsante Home
        if (gameView instanceof OnePlayer) {
            ((OnePlayer) gameView).gamePanel.addHomeButtonListener(e -> handleHomeButton());
        } else if (gameView instanceof TwoPlayers) {
            ((TwoPlayers) gameView).gamePanel.addHomeButtonListener(e -> handleHomeButton());
        } else if (gameView instanceof ThreePlayers) {
            ((ThreePlayers) gameView).gamePanel.addHomeButtonListener(e -> handleHomeButton());
        }
    }
    
    /**
     * Gestisce l'evento del pulsante 'Hit'
     */
    private void handleHitButton() {
        playClickSound();
        
        // Pesca una carta dal mazzo
        Model.Card card = model.drawCard();
        model.getPlayerHand().add(card);
        
        // Aggiorna la somma e il conteggio degli assi
        model.setPlayerSum(model.getPlayerSum() + card.getValue());
        model.setPlayerAceCount(model.getPlayerAceCount() + (card.isAce() ? 1 : 0));
        
        // Verifica se il giocatore ha superato 21
        if (model.reduceAce(model.getPlayerSum(), model.getPlayerAceCount()) > 21) {
            gameView.setHitButtonEnabled(false);
            handleStayButton();
        }
        
        // Notifica gli osservatori
        model.notifyObservers();
    }
    
    /**
     * Gestisce l'evento del pulsante Stay
     */
    private void handleStayButton() {
        playClickSound();
        
        // Disabilita i pulsanti
        gameView.setHitButtonEnabled(false);
        gameView.setStayButtonEnabled(false);
        
        // Turno del dealer
        dealerTurn();
        
        // Turno dei bot, in base al loro numero
        if (model.getPlayers() > 1) {
            bot1Turn();
        }
        
        if (model.getPlayers() > 2) {
            bot2Turn();
        }
        
        // Aggiorna i risultati
        updateResults();
        
        // Notifica gli osservatori
        model.notifyObservers();
    }
    
    /**
     * Gestisce il turno del dealer
     */
    private void dealerTurn() {
        while (model.getDealerSum() < 17) {
            Model.Card card = model.drawCard();
            model.getDealerHand().add(card);
            model.setDealerSum(model.getDealerSum() + card.getValue());
            model.setDealerAceCount(model.getDealerAceCount() + (card.isAce() ? 1 : 0));
        }
    }
    
    /**
     * Gestisce il turno del primo bot
     */
    private void bot1Turn() {
        while (model.getBot1Sum() < 17) {
            Model.Card card = model.drawCard();
            model.getBot1Hand().add(card);
            model.setBot1Sum(model.getBot1Sum() + card.getValue());
            model.setBot1AceCount(model.getBot1AceCount() + (card.isAce() ? 1 : 0));
        }
    }
    
    /**
     * Gestisce il turno del secondo bot
     */
    private void bot2Turn() {
        while (model.getBot2Sum() < 17) {
            Model.Card card = model.drawCard();
            model.getBot2Hand().add(card);
            model.setBot2Sum(model.getBot2Sum() + card.getValue());
            model.setBot2AceCount(model.getBot2AceCount() + (card.isAce() ? 1 : 0));
        }
    }
    
    /**
     * Aggiorna i risultati della partita
     */
    private void updateResults() {
        int playerFinalSum = model.reduceAce(model.getPlayerSum(), model.getPlayerAceCount());
        int dealerFinalSum = model.reduceAce(model.getDealerSum(), model.getDealerAceCount());
        
        // Determina il risultato del giocatore
        int result = determineResult(playerFinalSum, dealerFinalSum);
        
        // Aggiorna le statistiche
        updateStats(result);
        
        // Aggiorna l'importo in base al risultato
        updateImporto(result);
    }
    
    /**
     * Determina il risultato confrontando due somme
     * 
     * @param sum La somma del giocatore
     * @param dealerSum La somma del dealer
     * @return Il codice del risultato (1: vittoria, 2: sconfitta, 3: pareggio)
     */
    private int determineResult(int sum, int dealerSum) {
        if (sum > 21) return 2; // Sconfitta
        if (dealerSum > 21) return 1; // Vittoria
        if (sum == dealerSum) return 3; // Pareggio
        return (sum > dealerSum) ? 1 : 2; // Vittoria o sconfitta
    }
    
    /**
     * Aggiorna le statistiche in base al risultato
     * 
     * @param result Il codice del risultato
     */
    private void updateStats(int result) {
        switch (result) {
            case 1: // Vittoria
                winCount++;
                break;
            case 2: // Sconfitta
                defeatCount++;
                break;
            case 3: // Pareggio
                drawCount++;
                break;
        }
    }
    
    /**
     * Aggiorna l'importo in base al risultato
     * 
     * @param result Il codice del risultato
     */
    private void updateImporto(int result) {
        switch (result) {
            case 1: // Vittoria
                importo += puntata;
                break;
            case 2: // Sconfitta
                // L'importo è già stato sottratto nella vista Bet
                break;
            case 3: // Pareggio
                importo += puntata; // Restituisce la puntata
                break;
        }
    }
    
    /**
     * Gestisce l'evento del pulsante Home
     */
    private void handleHomeButton() {
        playClickSound();
        stopGameLoop();
        showHomeView();
    }
    
    /**
     * Avvia il game loop
     */
    private void startGameLoop() {
        if (gameLoopTimer != null) {
            gameLoopTimer.cancel();
        }
        
        gameLoopTimer = new Timer();
        gameLoopTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Aggiorna il modello periodicamente
                SwingUtilities.invokeLater( () -> { if (model != null) { model.notifyObservers();} } );
            }
        }, 0, 16); // Circa 60 FPS
    }
    
    /**
     * Ferma il game loop
     */
    private void stopGameLoop() {
        if (gameLoopTimer != null) {
            gameLoopTimer.cancel();
            gameLoopTimer = null;
        }
    }
    
    /**
     * Riproduce il click
     */
    private void playClickSound() {
        AudioManager.getInstance().play("/BlackJack/resources/audio/click2.wav");
    }
    
}

