package view;

import model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Classe astratta che rappresenta una carta animata.
 */
public abstract class AnimationCard {
    //la carta da animare
    protected Model.Card card;
    // Coordinate attuali
    protected float x, y;
    // Coordinate target
    protected float targetX, targetY;
    // Indica se la carta è visibile o meno
    protected boolean visible;

    /**
     * Costruttore della classe AnimationCard
     * 
     * @param card la carta da animare
     * @param x La posizione iniziale x
     * @param y La posizione iniziale y
     */
    public AnimationCard(Model.Card card, float x, float y) {
        this.card = card;
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.visible = false;
    }

    /**
     * Imposta le coordinate target
     * 
     * @param x La posizione target x
     * @param y La posizione target y
     */
    public void setTarget(float x, float y) {
        this.targetX = x;
        this.targetY = y;
    }

    /**
     * Aggiorna la posizione attuale della carta avvicinandola alla posizione target
     * La velocità di movimento dipende dalla distanza dalla posizione target
     */
    public void update() {
        x += (targetX - x) * 0.1f;
        y += (targetY - y) * 0.1f;
    }

    /**
     * Imposta la visibilità della carta
     * 
     * @param visible la carta è visibile o nascosta
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Restituisce l'immagine della carta
     * 
     * @param faceUp true viene disegnata la carta, false viene disegnato il retro
     * @return L'immagine della carta
     */
    protected Image getCardImage(boolean faceUp) {
        String imagePath = faceUp ? card.getImagePath() : "/BlackJack/resources/images/cards/BACK.png";
        return new ImageIcon(getClass().getResource(imagePath)).getImage();
    }

    /**
     * Metodo astratto per disegnare la carta. Deve essere implementato nelle sottoclassi
     * 
     * @param g L'oggetto Graphics su cui disegnare
     * @param faceUp true viene disegnata la carta, false viene disegnato il retro
     */
    public abstract void draw(Graphics g, boolean faceUp);

    /**
     * Sottoclasse che rappresenta una carta animata con dimensioni predefinite
     */
    public static class NormalCard extends AnimationCard {
        // Larghezza della carta
        private static final int CARD_WIDTH = 110;
        // Altezza della carta
        private static final int CARD_HEIGHT = 154;

        /**
         * Costruttore della classe AnimatedCard1
         * 
         * @param card La carta da animare
         * @param x La posizione iniziale x
         * @param y La posizione iniziale y
         */
        public NormalCard(Model.Card card, float x, float y) {
            super(card, x, y);
        }

        /**
         * Disegna la carta con dimensioni predefinite
         * 
         * @param g L'oggetto Graphics su cui disegnare
         * @param faceUp true viene disegnata la carta, false viene disegnato il retro
         */
        @Override
        public void draw(Graphics g, boolean faceUp) {
            if (!visible) return;
            Image cardImage = getCardImage(faceUp);
            g.drawImage(cardImage, (int) x, (int) y, CARD_WIDTH, CARD_HEIGHT, null);
        }
    }

    /**
     * Sottoclasse che rappresenta una carta animata più piccola e che può essere ruotata
     */
    public static class SmallerCard extends AnimationCard {
        // Larghezza della carta
        private static final int CARD_WIDTH = 78;
        // Altezza della carta
        private static final int CARD_HEIGHT = 110;
        // Indica se la carta deve essere ruotata di 90 gradi
        private boolean rotated;

        /**
         * Costruttore della classe AnimatedCard2
         * 
         * @param card La carta del modello associata a questa animazione
         * @param x La posizione iniziale x
         * @param y La posizione iniziale y
         * @param rotated Se true, la carta verrà disegnata ruotata di 90 gradi
         */
        public SmallerCard(Model.Card card, float x, float y, boolean rotated) {
            super(card, x, y);
            this.rotated = rotated;
        }

        /**
         * Disegna la carta con dimensioni ridotte e, se specificato, ruotata di 90 gradi
         * 
         * @param g L'oggetto Graphics su cui disegnare
         * @param faceUp true viene disegnata la carta, false viene disegnato il retro
         */
        @Override
        public void draw(Graphics g, boolean faceUp) {
            if (!visible) return;
            Image cardImage = getCardImage(faceUp);
            Graphics2D g2d = (Graphics2D) g;

            if (rotated) {
                AffineTransform originalTransform = g2d.getTransform();
                g2d.rotate(Math.toRadians(90), x + CARD_HEIGHT / 2, y + CARD_WIDTH / 2);
                g2d.drawImage(cardImage, (int) x, (int) y, CARD_WIDTH, CARD_HEIGHT, null);
                g2d.setTransform(originalTransform);
            } else {
                g2d.drawImage(cardImage, (int) x, (int) y, CARD_WIDTH, CARD_HEIGHT, null);
            }
        }
    }

    /**
     * Sottoclasse che rappresenta una carta animata che può essere ruotata di un angolo arbitrario
     */
    public static class RotationCard extends AnimationCard {
        // Larghezza della carta
        private static final int CARD_WIDTH = 78;
        // Altezza della carta
        private static final int CARD_HEIGHT = 110;
        // Indica se la carta deve essere ruotata
        private boolean rotated;
        // Angolo di rotazione della carta in gradi
        private int rotationDegrees;

        /**
         * Costruttore della classe AnimatedCard3
         * 
         * @param card La carta del modello associata a questa animazione
         * @param x La posizione iniziale x
         * @param y La posizione iniziale y
         * @param rotated Se true, la carta verrà ruotata
         * @param rotationDegrees L'angolo di rotazione
         */
        public RotationCard(Model.Card card, float x, float y, boolean rotated, int rotationDegrees) {
            super(card, x, y);
            this.rotated = rotated;
            this.rotationDegrees = rotationDegrees;
        }

        /**
         * Disegna la carta con dimensioni ridotte e, se specificato, ruotata dell'angolo specificato
         * 
         * @param g L'oggetto Graphics su cui disegnare
         * @param faceUp true viene disegnata la carta, false viene disegnato il retro
         */
        @Override
        public void draw(Graphics g, boolean faceUp) {
            if (!visible) return;
            Image cardImage = getCardImage(faceUp);
            Graphics2D g2d = (Graphics2D) g;

            if (rotated) {
                AffineTransform originalTransform = g2d.getTransform();
                g2d.rotate(Math.toRadians(rotationDegrees), x + CARD_HEIGHT / 2, y + CARD_WIDTH / 2);
                g2d.drawImage(cardImage, (int) x, (int) y, CARD_WIDTH, CARD_HEIGHT, null);
                g2d.setTransform(originalTransform);
            } else {
                g2d.drawImage(cardImage, (int) x, (int) y, CARD_WIDTH, CARD_HEIGHT, null);
            }
        }
    }
}

