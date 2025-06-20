package com.paperreview.paperreview.common;

import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.PaperEntity;
import com.paperreview.paperreview.entities.RevisioneEntity;
import com.paperreview.paperreview.entities.UtenteEntity;

public class UserContext {

    private static UtenteEntity utente;
    private static ConferenzaEntity conferenzaAttuale;
    private static boolean standaloneInteraction;
    private static boolean vieneDaRevisione;

    private static PaperEntity paperAttuale;
    private static RevisioneEntity revisioneCorrente;

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

    public static PaperEntity getPaperAttuale() {
        return paperAttuale;
    }

    public static void setPaperAttuale(PaperEntity paper) {
        paperAttuale = paper;
    }

    public static RevisioneEntity getRevisioneCorrente() {
        return revisioneCorrente;
    }

    public static void setRevisioneCorrente(RevisioneEntity revisioni) {
        revisioneCorrente = revisioni;
    }

    public static boolean getVieneDaRevisione() {
        return vieneDaRevisione;
    }

    public static void setVieneDaRevisione(boolean vieneDaRevisione) {
        UserContext.vieneDaRevisione = vieneDaRevisione;
    }
}
