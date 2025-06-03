package com.paperreview.paperreview.common;

import com.paperreview.paperreview.entities.UtenteEntity;

public class UserContext {

    private static UtenteEntity utente;

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


}
