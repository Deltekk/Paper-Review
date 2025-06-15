package com.paperreview.paperreview.common.email;

public class MailInvito extends MailBase {
    private final String ruolo;
    private final String nomeConferenza;
    private final String nomeUtente; // può essere null se non registrato
    private final String codiceInvito; // solo per non registrati

    public MailInvito(String to, String ruolo, String nomeConferenza, String nomeUtente, String codiceInvito) {
        super(to, "INVITO " + ruolo.toUpperCase());
        this.ruolo = ruolo;
        this.nomeConferenza = nomeConferenza;
        this.nomeUtente = nomeUtente;
        this.codiceInvito = codiceInvito;
    }

    @Override
    public String getBody() {
        // Se nomeUtente è presente, l'utente è già registrato
        if (nomeUtente != null && !nomeUtente.isBlank()) {
            return String.format("""
                <!DOCTYPE html>
                <html lang="it">
                <head><meta charset="UTF-8"><title>Invito %s</title></head>
                <body style="font-family: Arial, sans-serif; background: #fff;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px; text-align: center;">
                        <div style="font-size: 28px; color: #44C8F4; font-weight: bold;">Paper <span style="color: #13698A;">Review</span></div>
                        <h2 style="color: #606878;">INVITO %s</h2>
                        <p style="font-size: 18px;">Salve <strong>%s</strong>!<br>
                        Le notifichiamo che è stato invitato a partecipare alla conferenza <strong>%s</strong> in qualità di <strong>%s</strong>.<br>
                        La preghiamo di accettare o rifiutare l’invito al più presto.</p>
                    </div>
                </body>
                </html>
                """, ruolo, ruolo.toUpperCase(), nomeUtente, nomeConferenza, ruolo);
        } else {
            // Utente non registrato
            return String.format("""
                <!DOCTYPE html>
                <html lang="it">
                <head><meta charset="UTF-8"><title>Invito %s</title></head>
                <body style="font-family: Arial, sans-serif; background: #fff;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px; text-align: center;">
                        <div style="font-size: 28px; color: #44C8F4; font-weight: bold;">Paper <span style="color: #13698A;">Review</span></div>
                        <h2 style="color: #606878;">INVITO %s</h2>
                        <p style="font-size: 18px;">Salve!<br>
                        Le notifichiamo che è stato invitato a partecipare alla conferenza <strong>%s</strong> in qualità di <strong>%s</strong>.<br>
                        Per accettare l’invito, deve inserire il codice <strong>%s</strong> all’interno dell’applicazione una volta registrato.<br>
                        La preghiamo di accettare o rifiutare l’invito al più presto.</p>
                    </div>
                </body>
                </html>
                """, ruolo, ruolo.toUpperCase(), nomeConferenza, ruolo, codiceInvito);
        }
    }
}