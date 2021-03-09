import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;

/**
 * Visualizzatore per il gioco dell'oca. Legge un file contenente
 * le azioni del gioco e lo anima graficamente. Il file viene
 * letto riga per riga e le varie azioni vengono rese graficamente.
 * L'utente manda avanti il gioco premendo un bottone.
 */
public class AnimazioneOca extends JFrame implements ParoleChiave {
    /**
     * File da cui leggere le azioni del gioco.
     */
    protected BufferedReader in;

    /**
     * Ultima riga letta dal file in.
     */
    int riga;

    /**
     * Pannello col disegno della tavola da gioco.
     */
    protected TavolaOca tavola;

    /**
     * Array con 2 posti che mostra i messaggi per i due giocatori.
     */
    protected JTextField[] messaggi;

    /**
     * Array con 2 posti con i bottoni per avanzare il gioco.
     */
    protected JButton[] avanti;

    /**
     * Array con 2 posti che tiene i nomi dei due giocatori.
     */
    protected JLabel[] nome;

    /**
     * Bottone per terminare il programma.
     */
    protected JButton termina;

    /**
     * Mostra il numero della riga corrente sul file di input.
     */
    protected JLabel mostraRiga;

    /**
     * Indice, 0 oppure 1, del giocatore corrente.
     */
    protected int giocatore;

    /**
     * Vero quando il gioco e' in corso, falso altrimenti.
     */
    protected boolean giocoInCorso;

    /**
     * Numero di secondi da aspettare nella funzione "aspetta".
     */
    public static int secondiDaAspettare = 1200;

    /**
     * Classe di eccezioni nate leggendo le azioni del gioco.
     */
    public class OcaException extends Exception {
        public OcaException(int linea, String msg) {
            super("Errore nel file di input, linea " + linea + ": " + msg);
        }
    }

    /**
     * Se il gioco e' attivo, mostra il messaggio nella finestra,
     * altrimenti lo mostra sullo standard output.
     */
    public void trattaEccezione(Exception e) {
        String msg;
        if (e instanceof OcaException) msg = e.getMessage();
        else msg = "Errore: " + e.getClass().getName() + ": " + e.getMessage();
        if (in != null)
            msg = msg + "\nLinea corrente sul file di input: " + riga;
        if (giocoInCorso) {
            msg = msg + "\nFine del gioco\nVuoi vedere i dettagli dell'eccezione?";
            avanti[0].setEnabled(false);
            avanti[1].setEnabled(false);
            giocoInCorso = false;
            int k = JOptionPane.showConfirmDialog(null, msg, "Errore", JOptionPane.YES_NO_OPTION);
            if (k == JOptionPane.YES_OPTION) e.printStackTrace();
        } else // errore durante l'inizializzazione
        {
            System.out.println(msg + ", il programma termina");
            System.out.println("Vuoi vedere i dettagli dell'eccezione? (n=no, altro=si)");
            try {
                BufferedReader aux = new BufferedReader(new InputStreamReader(System.in));
                String s = aux.readLine().trim();
                if (!s.equals("n")) e.printStackTrace();
            } catch (IOException ee) {
            }
        }
        System.exit(1);
    }

    /**
     * Aspetta un numero di secondi pari a secondiDaAspettare.
     */
    public void aspetta() {
        try {
            Thread.sleep(secondiDaAspettare);
        } catch (InterruptedException exc) {
            trattaEccezione(exc);
        }
    }

    /**
     * Legge la prossima riga dal file di input e la ritorna
     * come una sola stringa. Aggiorna la visualizzazione
     * del numero di riga corrente.
     */
    protected String leggiRiga() throws Exception {
        String s = in.readLine();
        if (s == null)
            throw new OcaException(riga, "File di input finisce inaspettatamente");
        riga++;
        if (mostraRiga != null) mostraRiga.setText("" + riga);
        return s;
    }

    /**
     * Funzione ausiliaria, legge dal file il nome di un giocatore.
     */
    protected void leggiNome() throws Exception {
        int n;
        String s = leggiRiga();
        if (!s.startsWith(PAROLA_GIOCATORE))
            throw new OcaException(riga, "Manca parola chiave " + PAROLA_GIOCATORE);
        s = s.substring(PAROLA_GIOCATORE.length()).trim();
        int i = s.indexOf(' ');
        n = Integer.parseInt(s.substring(0, i));
        if ((n != 1) && (n != 2))
            throw new OcaException(riga, "Numero di giocatore " + n +
                    " non valido, deve essere 1 o 2");
        s = s.substring(i).trim();
        if (s.length() == 0)
            throw new OcaException(riga, "Manca il nome del giocatore " + n);
        if (nome[n - 1] != null)
            throw new OcaException(riga, "Nome del giocatore " + n +
                    " specificato due volte");
        nome[n - 1] = new JLabel("Giocatore " + n + ": " + s);
    }

    /**
     * Apre il file e compie l'inizializzazione della tavola da gioco.
     * Legge intestazione, numero di caselle e nomi dei giocatori.
     * Crea la tavola e le etichette coi nomi dei due giocatori.
     */
    protected boolean apri(String nomeFile) throws Exception {
        in = new BufferedReader(new FileReader(nomeFile));
        String s = null;
        int n, m;
        riga = 0;

        // intestazione
        s = leggiRiga();
        if (!s.startsWith(PAROLA_OCA))
            throw new OcaException(riga, "Manca parola chiave " + PAROLA_OCA);
        s = s.substring(PAROLA_OCA.length()).trim();
        if (!s.startsWith(PAROLA_GIOCO))
            throw new OcaException(riga, "Manca parola chiave " + PAROLA_GIOCO);

        // numero di caselle
        s = leggiRiga();
        if (!s.startsWith(PAROLA_CASELLE))
            throw new OcaException(riga, "Manca parola chiave " + PAROLA_CASELLE);
        s = s.substring(PAROLA_CASELLE.length()).trim();
        n = Integer.parseInt(s);
        if (n <= 0)
            throw new OcaException(riga, "Numero di caselle non valido: " + n);

        // crea la tavola
        tavola = new TavolaOca(n + 2, 50);

        // nomi dei giocatori
        nome = new JLabel[2];
        leggiNome();
        leggiNome();
        if (nome[0] == null)
            throw new OcaException(riga, "Manca il nome del giocatore 1");
        if (nome[1] == null)
            throw new OcaException(riga, "Manca il nome del giocatore 2");
        return true;
    }

    /**
     * Ritorna un pannello con dentro, allineati orizzontalmente,
     * i componenti indicati.
     */
    public static JPanel pannelloConAllineati(JComponent... c) {
        JPanel temp = new JPanel(new FlowLayout());
        for (int i = 0; i < c.length; i++) temp.add(c[i]);
        return temp;
    }

    /**
     * Inizializza il gioco, collegandolo al file di input.
     */
    public void init(String nomeFile) throws Exception {
        // apre il file, legge numero di caselle e nomi dei giocatori,
        // crea tavola, nome[0], nome[1]
        apri(nomeFile);

        JPanel aux;
        int i;
        setTitle("Gioco dell'oca da file " + nomeFile);
        setLayout(new BorderLayout());
        aux = new JPanel(new FlowLayout());
        aux.add(tavola);
        add(BorderLayout.CENTER, aux);
        messaggi = new JTextField[2];
        avanti = new JButton[2];
        for (i = 0; i < 2; i++) {
            aux = new JPanel(new GridLayout(3, 1));
            aux.setBorder(new LineBorder(tavola.coloreGiocatore(i)));
            messaggi[i] = new JTextField(80); // larghezza 100 caratteri
            messaggi[i].setEditable(false);
            avanti[i] = new JButton("avanti " + (i + 1));
            aux.add(nome[i]);
            aux.add(messaggi[i]);
            aux.add(avanti[i]);
            if (i == 1) add(BorderLayout.SOUTH, aux);
            else add(BorderLayout.NORTH, aux);
        }

        termina = new JButton("Esci");
        mostraRiga = new JLabel("" + riga);

        aux = new JPanel(new FlowLayout());
        JPanel temp = new JPanel(new GridLayout(4, 1));
        temp.add(termina);
        temp.add(pannelloConAllineati(
                new JLabel("Linea sul file di input:"), mostraRiga));
        temp.add(new JLabel());
        aux.add(temp);
        add(BorderLayout.EAST, aux);
        pack();

        ActionListener ac = new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    azione();
                } catch (Exception e) {
                    trattaEccezione(e);
                }
            }
        };
        avanti[0].addActionListener(ac);
        avanti[1].addActionListener(ac);

        termina.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                System.exit(0);
            }
        });

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /** Rende visibile la tavola da gioco. */
        setVisible(true);
    }

    /**
     * Svolge la prossima azione relava al giocatore corrente,
     * leggendola dal file. Questo include il cambio turno con
     * passaggio all'altro giocatore.
     */
    void azione() throws Exception {
        String s = leggiRiga();
        if (s.startsWith(PAROLA_TURNO)) {
            s = s.substring(PAROLA_TURNO.length()).trim();
            int f = Integer.parseInt(s) - 1;
            if ((f != 0) && (f != 1))
                throw new OcaException(riga, "Numero di giocatore " + (f + 1) +
                        " non valido, deve essere 1 o 2");
            if (giocatore == f)
                messaggi[giocatore].setText("Tocca di nuovo al giocatore " + (giocatore + 1));
            else {
                messaggi[giocatore].setText("Il giocatore " + (giocatore + 1) + " ha finito");
                giocatore = f;
                messaggi[giocatore].setText("Adesso tocca al giocatore " + (giocatore + 1));
                avanti[giocatore].setEnabled(true);
                avanti[1 - giocatore].setEnabled(false);
            }
        } else if (s.startsWith(PAROLA_DADO)) {
            s = s.substring(PAROLA_DADO.length()).trim();
            int d = Integer.parseInt(s);
            messaggi[giocatore].setText("Il giocatore " + (giocatore + 1) +
                    " tirando il dado ha fatto " + d);
        } else if (s.startsWith(PAROLA_SPOSTA)) {
            s = s.substring(PAROLA_SPOSTA.length()).trim();
            String t = "";
//      messaggi[giocatore].setText("Il giocatore si sposta");
            int vecchia = tavola.posizioneGiocatore(giocatore);
            int n = vecchia;
            while (s.length() > 0) {
                int i = s.indexOf(' ');
                if (i == -1) {
                    t = s;
                    s = "";
                } else {
                    t = s.substring(0, i);
                    s = s.substring(i + 1).trim();
                }
                n = Integer.parseInt(t);
                tavola.mettiGiocatore(giocatore, n);
                if (s.length() == 0)
                    messaggi[giocatore].setText("Il giocatore dalla posizione " +
                            vecchia + " ha raggiunto la posizione " + n);
                else {
                    messaggi[giocatore].setText("Il giocatore dalla posizione " +
                            vecchia + " va in " + n + " e continua...");
                    aspetta();
                }
            }
        } else if (s.startsWith(PAROLA_VINCE)) {
            messaggi[giocatore].setText("Il giocatore " + (giocatore + 1) + " ha vinto!");
            avanti[giocatore].setEnabled(false);
            giocoInCorso = false;
        } else if (s.startsWith(PAROLA_MESSAGGIO)) {
            s = s.substring(PAROLA_MESSAGGIO.length()).trim();
            messaggi[giocatore].setText(s);
        } else throw new OcaException(riga, "Parola chiave non riconosciuta: " + s);
    }

    /**
     * Ridefinisce la funzione ereditata da JFrame. Se
     * il gioco non e' stato inizializzato, non e' possibile
     * renderlo visibile.
     */
    public void setVisible(boolean b) {
        if (b && (tavola == null))
            trattaEccezione(new Exception("Gioco non ancora inizializzato"));
        super.setVisible(b);
    }

    /**
     * Legge dal fila la prima riga del tipo "Turno N" con
     * N=1,2. Ritorna 0 oppure 1 secondo il valore di N-1.
     */
    protected int leggiPrimoTurno() throws Exception {
        String s = leggiRiga();
        if (!s.startsWith(PAROLA_TURNO))
            throw new OcaException(riga, "Manca parola chiave " + PAROLA_TURNO);
        s = s.substring(PAROLA_TURNO.length()).trim();
        return Integer.parseInt(s) - 1;
    }

    /**
     * Inizia l'animazione del gioco. L'utente deve fare avanzare
     * il gioco  premendo un bottone.
     */
    public void gioca() throws Exception {
        if (!isVisible())
            trattaEccezione(new Exception("Finestra non ancora visibile"));
        giocoInCorso = true;
        giocatore = leggiPrimoTurno();
        avanti[giocatore].setEnabled(true);
        avanti[1 - giocatore].setEnabled(false);
    }

    /**
     * Costruttore.
     */
    public AnimazioneOca() {
        super();
    }

    /**
     * Main. Sulla command-line vuole il nome del file
     * da cui leggere le azioni del gioco.
     */
    public static void main(String[] arg) {
        if (arg.length != 1) {
            System.out.println("Manca il nome del file da cui leggere il gioco");
            return;
        }
        AnimazioneOca og = new AnimazioneOca();
        try {
            og.init(arg[0]);
            og.setVisible(true);
            og.gioca();
        } catch (Exception e) {
            og.trattaEccezione(e);
        }
    }
}