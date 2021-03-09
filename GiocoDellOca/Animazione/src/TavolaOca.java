import java.awt.*;
import javax.swing.*;

/**
 * Pannello grafico che disegna la tavola da gioco con le caselle
 * disposte a spirale e i segnali dei due giocatori.
 */
public class TavolaOca extends JPanel {
    /**
     * Array che tiene il colore del primo e del secondo giocatore.
     */
    protected Color[] colore;

    /**
     * Classe interna i cui oggetti memorizzano le coordinate intere
     * di un pixel.
     */
    public class Pixel {
        /**
         * coordinata x del pixel.
         */
        public int x;
        /**
         * coordinata y del pixel.
         */
        public int y;

        /**
         * Crea il pixel e lo colloca alle coordinate (x,y).
         */
        public Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Colloca il pixel alle coordinate (x,y).
         */
        public void colloca(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Array delle posizioni delle caselle della tavola, inclusa
     * partenza e arrivo.
     * Le coordinate memorizzate sono quelle del punto di aggancio di
     * ogni casella, che e' un quadrato di lato latoCasella.
     */
    protected Pixel[] caselle;

    /**
     * Lunghezza in pixel del lato dei quadrati che costituiscono
     * le caselle.
     */
    protected int latoCasella;

    /**
     * coordinate minime di una casella della tavola.
     */
    protected Pixel minAng;

    /**
     * coordinate massime di una casella della tavola.
     */
    protected Pixel maxAng;

    /**
     * Array che tiene la posizione (indice di casella)
     * del primo giocatore e del secondo giocatore.
     */
    protected int[] posizione;

    /**
     * Ritorna il numero di caselle totali, incluse partenza e arrivo.
     */
    public int numeroCaselle() {
        return caselle.length;
    }

    /**
     * Ritorna il colore del segnale del giocatore g (g=0,1).
     */
    public Color coloreGiocatore(int g) {
        return colore[g];
    }

    /**
     * Ritorna la posizione del giocatore g (g=0,1).
     */
    public int posizioneGiocatore(int g) {
        return posizione[g];
    }

    /**
     * Assume numero>=2, dimensiona l'array caselle e vi mette le coordinate
     * attribuite alle varie caselle; nei pixel minAng e maxAng
     * mette le coordinate dell'angolo di x,y minime e di x,y massime del
     * rettangolo entro il quale sono state collocate le caselle.
     * La casella 0 e' la partenza, la casella numero-1 l'arrivo.
     */
    protected void collocaCaselle(int numero) {
        int i, j, x, y;
        int deltaX, deltaY;
        int lato;
        caselle = new Pixel[numero];
        minAng = new Pixel(0, 0);
        maxAng = new Pixel(0, 0);
        minAng.colloca(0, 0);
        maxAng.colloca(0, 0);
//System.out.println("metto 0 in 0,0");
//System.out.println("metto 1 in 1,0");
        caselle[numero - 1] = new Pixel(0, 0);
        caselle[numero - 2] = new Pixel(1, 0);
        i = 2;
        x = 1;
        y = 0;
        // prossimo lato verticale verso l'alto
        lato = 1;
        deltaX = 0;
        deltaY = 1;
        while (i < numero) {
            // colloca le lato+1 caselle del lato
            for (j = 0; (j < (lato + 1)) && (i < numero); j++) {
                x += deltaX;
                y += deltaY;
//System.out.println("metto "+i+" in  "+x + ", "+y);
                caselle[numero - i - 1] = new Pixel(x, y);
                i++;
                if (x < minAng.x) minAng.x = x;
                else if (x > maxAng.x) maxAng.x = x;
                if (y < minAng.y) minAng.y = y;
                else if (y > maxAng.y) maxAng.y = y;
            }
            // passa al prossimo lato
            lato++;
            if (deltaY == 1) {
                deltaX = -1;
                deltaY = 0;
            } else if (deltaX == -1) {
                deltaX = 0;
                deltaY = -1;
            } else if (deltaY == -1) {
                deltaX = 1;
                deltaY = 0;
            } else /* deltaX==1 */ {
                deltaX = 0;
                deltaY = 1;
            }
        }
    }

    /**
     * Sposta il giocatore gioc (=0,1) dalla posizione attuale fino alla
     * posizione destinazione, disegnando tutte le fasi intermedie.
     */
    public void mettiGiocatore(int gioc, int dest) {
        posizione[gioc] = dest;
//    repaint();
// uso questo perche' voglio che sia fatto aspettando
        paintComponent(getGraphics());
    }

    /**
     * Crea il pannello grafico per un gioco dell'oca con numCaselle
     * caselle (comprese partenza e arrivo) e dove le caselle sono
     * rappresentate da quadrati di lunghezzaLato pixel di lato.
     */
    public TavolaOca(int numCaselle, int lunghezzaLato) {
        super();
        colore = new Color[2];
        colore[0] = Color.red;
        colore[1] = Color.blue;
        // i giocatori sono alla partenza
        posizione = new int[2];
        posizione[0] = posizione[1] = 0;
        latoCasella = lunghezzaLato;
        // crea e colloca le caselle, determina minAng e maxAng
        collocaCaselle(numCaselle);
        // ora si occupa dell'interfaccia utente
        setPreferredSize(new Dimension(latoCasella * (1 + maxAng.x - minAng.x),
                latoCasella * (1 + maxAng.y - minAng.y)));
    }

    /**
     * Disegna il segnale del giocatore gioc (= 0,1) posizionato alla
     * casella posizione[gioc], usando il colore colore[gioc].
     */
    protected void disegnaSegnale(Graphics g, int gioc) {
        // angolo della casella
        int x = latoCasella * (caselle[posizione[gioc]].x - minAng.x);
        int y = latoCasella * (caselle[posizione[gioc]].y - minAng.y);
        x += (latoCasella / 2);
        if (gioc == 0) y += (latoCasella / 2);
        g.setColor(colore[gioc]);
        g.fillRect(x, y + 1, latoCasella / 2 - 2, latoCasella / 2 - 2);
        g.setColor(Color.white);
        g.drawString("" + (gioc + 1), x + 3, y + (latoCasella / 3));
    }

    /**
     * Disegna o ridisegna il pannello grafico.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x0, y0;
        int i;
        // primo ciclo per riempire
        for (i = 0; i < caselle.length; i++) {
            x0 = latoCasella * (caselle[i].x - minAng.x);
            y0 = latoCasella * (caselle[i].y - minAng.y);
            if (i == 0) g.setColor(Color.cyan);
            else if (i == caselle.length - 1) g.setColor(Color.green);
            else g.setColor(Color.white);
            g.fillRect(x0, y0, latoCasella, latoCasella);
        }
        // secondo ciclo per contorni e scritte
        for (i = 0; i < caselle.length; i++) {
            x0 = latoCasella * (caselle[i].x - minAng.x);
            y0 = latoCasella * (caselle[i].y - minAng.y);
            g.setColor(Color.black);
            g.drawRect(x0, y0, latoCasella, latoCasella);
            g.drawString("" + i, x0 + 2, y0 + latoCasella / 2);
        }
        disegnaSegnale(g, 0);
        disegnaSegnale(g, 1);
    }

}