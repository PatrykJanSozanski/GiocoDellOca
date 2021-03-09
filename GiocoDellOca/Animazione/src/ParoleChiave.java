/**
 * Questa interfaccia contiene come costanti tutte le parole
 * chiave previste nei file di input e di output del gioco
 * dell'oca. Una classe che implementi questa interfaccia ha
 * automaticamente a disposizione tutte le parole chiave.
 */
public interface ParoleChiave {
    /* Parole chiave nel file della tavola da gioco (input). */
    String PAROLA_OCA = "OCA";
    String PAROLA_TAVOLA = "TAVOLA";
    String PAROLA_CASELLE = "CASELLE";
    String PAROLA_COMMENTO = "COMMENTO";
    String PAROLA_INIZIO = "INIZIO";
    String PAROLA_FINE = "FINE";
    String PAROLA_VAI = "VAI";
    String PAROLA_AVANTI = "AVANTI";
    String PAROLA_INDIETRO = "INDIETRO";
    String PAROLA_FERMO = "FERMO";
    String PAROLA_TIRA = "TIRA";
    String PAROLA_DOPPIO = "DOPPIO";
    /* Ulteriori parole chiave nel file delle azioni di gioco (output). */
    String PAROLA_GIOCO = "GIOCO";
    String PAROLA_GIOCATORE = "GIOCATORE";
    String PAROLA_MESSAGGIO = "MESSAGGIO";
    String PAROLA_VINCE = "VINCE";
    String PAROLA_DADO = "DADO";
    String PAROLA_SPOSTA = "SPOSTA";
    String PAROLA_TURNO = "TURNO";
}