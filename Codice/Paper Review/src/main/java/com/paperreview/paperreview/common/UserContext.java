package com.paperreview.paperreview.common;

import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.UtenteEntity;

public class UserContext {

    private static UtenteEntity utente;
    private static ConferenzaEntity conferenzaAttuale;
    private static boolean standaloneInteraction;

    public static void setUtente(UtenteEntity utente) {
        UserContext.utente = utente;
    }

    public static UtenteEntity getUtente() {
        return utente;
    }

    public static void login(UtenteEntity user) {
        setUtente(user);
    }

    public static void logout() {
        setUtente(null);
    }

    public static ConferenzaEntity getConferenzaAttuale() {
        return conferenzaAttuale;
    }

    public static void setConferenzaAttuale(ConferenzaEntity conferenzaAttuale) {
        UserContext.conferenzaAttuale = conferenzaAttuale;
    }

    public static boolean isStandaloneInteraction() {
        return standaloneInteraction;
    }

    public static void setStandaloneInteraction(boolean standaloneInteraction) {
        UserContext.standaloneInteraction = standaloneInteraction;
    }
}
