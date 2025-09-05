package model;

import java.util.ArrayList;

import java.util.List;
import java.util.Observable;
import java.util.Random;

/**
 * Model del progetto
 * Gestisce la logica del gioco, il mazzo, le mani dei giocatori e i punteggi di essi
 */
public class Model extends Observable{
	// Il mazzo di carte
    private List<Card> deck;
    
    // Oggetto per generare numeri casuali, utilizzato per mescolare le carte nel mazzo
    private Random random;

    // Carta nascosta del dealer
    private Card hiddenCard;

    // Le mani di ogni giocatore
    private List<Card> dealerHand;
    private List<Card> playerHand;
    private List<Card> bot1Hand;
    private List<Card> bot2Hand;

    // I punteggi totali dei giocatori (la somma dei valori delle loro carte)
    private int dealerSum;
    private int playerSum;
    private int bot1Sum;
    private int bot2Sum;

    // Il conteggio degli assi nelle mani di ciascun giocatore
    private int dealerAceCount;
    private int playerAceCount;
    private int bot1AceCount;
    private int bot2AceCount;

    // Numero di giocatori (giocatore + bot + mazziere)
    private int players;

    /**
     * Costruttore del modello
     * @param players Il numero di giocatori nella partita
     */
    public Model(int players) {
        this.players = players;
        random = new Random();
    }

    /**
     * Notifica gli osservatori di un cambiamento nel modello
     */
    @Override
    public void notifyObservers(){
        super.setChanged();
        super.notifyObservers();
    }

    /**
     * Inizia una nuova partita
     * Crea e mischia il mazzo, distribuisce le carte iniziali
     */
    public void startNewGame() {
    	// Costruisce il mazzo di carte
        buildDeck();
        
        // Mischia il mazzo
        shuffleDeck();

        if (players > 0) {
        	// Inizializza le mani del dealer e del giocatore come liste vuote
            dealerHand = new ArrayList<>();
            playerHand = new ArrayList<>();

            // Inizializza i punteggi e il conteggio degli assi sia per il dealer che per il giocatore
            dealerSum = playerSum = dealerAceCount = playerAceCount = 0;

            // Pesca la carta nascosta per il dealer
            hiddenCard = drawCard();
            // Aggiunge il valore della carta nascosta al punteggio del dealer
            dealerSum += hiddenCard.getValue();
            // Incrementa il conteggio degli assi del dealer se la carta è un asso
            dealerAceCount += hiddenCard.isAce() ? 1 : 0;

            // Pesca una seconda carta per il dealer e aggiorna il punteggio e il conteggio degli assi
            dealerHand.add(drawCard());
            dealerSum += dealerHand.get(0).getValue();
            dealerAceCount += dealerHand.get(0).isAce() ? 1 : 0;

         // Distribuisce 2 carte al giocatore, aggiornando il punteggio e il conteggio degli assi
        for (int i = 0; i < 2; i++) {
            Model.Card card = drawCard();
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }
    }

    if (players > 1) {
        // Inizializza la mano del primo bot come una lista vuota
        bot1Hand = new ArrayList<>();
        // Inizializza il punteggio e il conteggio degli assi per il bot1
        bot1Sum = bot1AceCount = 0;

        // Distribuisce 2 carte al primo bot, aggiornando il punteggio e il conteggio degli assi
        for (int i = 0; i < 2; i++) {
            Model.Card card = drawCard();
            bot1Sum += card.getValue();
            bot1AceCount += card.isAce() ? 1 : 0;
            bot1Hand.add(card);
        }
    }
    
    // Verifica se ci sono più di 2 giocatori (ad esempio un altro bot)
    if (players > 2) {
        // Inizializza la mano del secondo bot come una lista vuota
        bot2Hand = new ArrayList<>();
        // Inizializza il punteggio e il conteggio degli assi per il bot2
        bot2Sum = bot2AceCount = 0;

        // Distribuisce 2 carte al secondo bot, aggiornando il punteggio e il conteggio degli assi
        for (int i = 0; i < 2; i++) {
            Model.Card card = drawCard();
            bot2Sum += card.getValue();
            bot2AceCount += card.isAce() ? 1 : 0;
            bot2Hand.add(card);
        }
    }
}

// Metodo per costruire il mazzo di carte
private void buildDeck() {
    deck = new ArrayList<>();
    String[] types = {"C", "D", "H", "S"};
    String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    
    for (String type : types) {
        for (String value : values) {
            deck.add(new Card(value, type));
        }
    }
}

    /**
     * Mischia il mazzo di carte
     */
    private void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card temp = deck.get(i);
            deck.set(i, deck.get(j));
            deck.set(j, temp);
        }
    }

    /**
     * Pesca una carta dal mazzo
     * @return La carta pescata
     */
    public Card drawCard() {
        return deck.remove(deck.size() - 1);
    }

    /**
     * Riduce il valore degli assi se la somma supera 21
     * @param sum La somma corrente
     * @param aceCount Il numero di assi
     * @return La nuova somma dopo la riduzione degli assi
     */
    public int reduceAce(int sum, int aceCount) {
        while (sum > 21 && aceCount > 0) {
            sum -= 10;
            aceCount--;
        }
        return sum;
    }

    // Metodi getter
    public Card getHiddenCard() { return hiddenCard; }

    public List<Card> getDealerHand() { return dealerHand; }
    public List<Card> getPlayerHand() { return playerHand; }
    public List<Card> getBot1Hand() { return bot1Hand; }
    public List<Card> getBot2Hand() { return bot2Hand; }

    public int getDealerSum() { return dealerSum; }
    public int getPlayerSum() { return playerSum; }
    public int getBot1Sum() { return bot1Sum; }
    public int getBot2Sum() { return bot2Sum; }

    public int getDealerAceCount() { return dealerAceCount; }
    public int getPlayerAceCount() { return playerAceCount; }
    public int getBot1AceCount() { return bot1AceCount; }
    public int getBot2AceCount() { return bot2AceCount; }

    public int getPlayers() { return players; }

    // Metodi setter
    public void setDealerSum(int sum) { this.dealerSum = sum; }
    public void setPlayerSum(int sum) { this.playerSum = sum; }
    public void setBot1Sum(int sum) { this.bot1Sum = sum; }
    public void setBot2Sum(int sum) { this.bot2Sum = sum; }

    public void setDealerAceCount(int count) { this.dealerAceCount = count; }
    public void setPlayerAceCount(int count) { this.playerAceCount = count; }
    public void setBot1AceCount(int count) { this.bot1AceCount = count; }
    public void setBot2AceCount(int count) { this.bot2AceCount = count; }

    /**
     * Classe interna che rappresenta una carta da gioco
     */
    public class Card {
        private final String value;
        private final String type;
    
        /**
         * Costruttore della carta
         * @param value Il valore della carta
         * @param type Il seme della carta
         */
        public Card(String value, String type) {
            this.value = value;
            this.type = type;
        }
    
        /**
         * Ottiene il valore numerico della carta
         * @return Il valore numerico della carta
         */
        public int getValue() {
            if ("AJQK".contains(value)) {
                return value.equals("A") ? 11 : 10;
            }
            return Integer.parseInt(value);
        }
    
        /**
         * Verifica se la carta è un asso
         * @return true se la carta è un asso, false altrimenti
         */
        public boolean isAce() {
            return value.equals("A");
        }
    
        /**
         * Ottiene il percorso dell'immagine della carta
         * @return Il percorso dell'immagine della carta
         */
        public String getImagePath() {
            return "/BlackJack/resources/images/cards/" + value + "-" + type + ".png";
        }
    
        /**
         * Restituisce una rappresentazione testuale della carta
         * @return Una stringa che rappresenta la carta
         */
        @Override
        public String toString() {
            return value + "-" + type;
        }
    }
}

