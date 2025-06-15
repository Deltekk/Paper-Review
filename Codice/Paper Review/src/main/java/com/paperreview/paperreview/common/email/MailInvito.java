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
            <head>
                <meta charset="UTF-8">
                <title>Invito %s - Paper Review</title>
            </head>
            <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #fff; margin: 0; padding: 0;">
                <div style="max-width: 600px; margin: 40px auto; padding: 20px; text-align: center;">

                    <!-- Logo -->
                    <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">
                        Paper <span style="color: #13698A;">Review</span>
                    </div>

                    <!-- Titolo -->
                    <h2 style="font-weight: 700; color: #606878; margin-bottom: 40px; font-size: 24px;">INVITO %s</h2>

                    <!-- Box blu -->
                    <div style="background-color: #0B7C9F; border-radius: 10px; padding: 30px; color: white; font-size: 18px; line-height: 1.4; margin-bottom: 60px; border: 3px solid #0a6e8b;">
                        <p style="margin: 0 0 15px 0;">Salve <strong>%s</strong>,</p>
                        <p style="margin: 0 0 15px 0;">Le notifichiamo che è stato invitato a partecipare alla conferenza <strong>%s</strong> in qualità di <strong>%s</strong>.</p>
                        <p style="margin: 0 0 15px 0;">La preghiamo di accettare o rifiutare l’invito al più presto.</p>
                    </div>

                    <!-- Footer -->
                    <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">Paper <span style="color: #13698A;">Review</span></div>
                    <div style="font-style: italic; color: #707E8C;">La conoscenza alla portata di un click</div>
                </div>
            </body>
            </html>
        """, ruolo, ruolo.toUpperCase(), nomeUtente, nomeConferenza, ruolo);
        } else {
            return String.format("""
            <!DOCTYPE html>
            <html lang="it">
            <head>
                <meta charset="UTF-8">
                <title>Invito %s - Paper Review</title>
            </head>
            <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #fff; margin: 0; padding: 0;">
                <div style="max-width: 600px; margin: 40px auto; padding: 20px; text-align: center;">

                    <!-- Logo -->
                    <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">
                        Paper <span style="color: #13698A;">Review</span>
                    </div>

                    <!-- Titolo -->
                    <h2 style="font-weight: 700; color: #606878; margin-bottom: 40px; font-size: 24px;">INVITO %s</h2>

                    <!-- Box blu -->
                    <div style="background-color: #0B7C9F; border-radius: 10px; padding: 30px; color: white; font-size: 18px; line-height: 1.4; margin-bottom: 60px; border: 3px solid #0a6e8b;">
                        <p style="margin: 0 0 15px 0;">Salve,</p>
                        <p style="margin: 0 0 15px 0;">Le notifichiamo che è stato invitato a partecipare alla conferenza <strong>%s</strong> in qualità di <strong>%s</strong>.</p>
                        <p style="margin: 0 0 15px 0;">Per accettare l’invito, deve inserire il codice <strong>%s</strong> all’interno dell’applicazione una volta registrato.</p>
                        <p style="margin: 0 0 15px 0;">La preghiamo di accettare o rifiutare l’invito al più presto.</p>
                    </div>

                    <!-- Footer -->
                    <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">Paper <span style="color: #13698A;">Review</span></div>
                    <div style="font-style: italic; color: #707E8C;">La conoscenza alla portata di un click</div>
                </div>
            </body>
            </html>
        """, ruolo, ruolo.toUpperCase(), nomeConferenza, ruolo, codiceInvito);
        }
    }
}