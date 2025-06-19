package com.paperreview.paperreview.common.email;

public class MailSegnalazione extends MailBase {
    private final String nomeSegnalante;
    private final String cognomeSegnalante;
    private final String nomeConferenza;
    private final String motivo;
    private final String titoloPaper;

    public MailSegnalazione(String to, String nomeSegnalante, String cognomeSegnalante, String nomeConferenza, String motivo, String titoloPaper) {
        super(to, "SEGNALAZIONE CONFLITTO");
        this.nomeSegnalante = nomeSegnalante;
        this.cognomeSegnalante = cognomeSegnalante;
        this.nomeConferenza = nomeConferenza;
        this.motivo = motivo;
        this.titoloPaper = titoloPaper;
    }

    @Override
    public String getBody() {
        return String.format("""
    <!DOCTYPE html>
    <html lang="it">
    <head>
        <meta charset="UTF-8">
        <title>Segnalazione - Paper Review</title>
    </head>
    <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #fff; margin: 0; padding: 0;">
        <div style="max-width: 600px; margin: 40px auto; padding: 20px; text-align: center;">

            <!-- Logo -->
            <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">
                Paper <span style="color: #13698A;">Review</span>
            </div>

            <!-- Titolo -->
            <h2 style="font-weight: 700; color: #606878; margin-bottom: 40px; font-size: 24px;">SEGNALAZIONE</h2>

            <!-- Box rosso -->
            <div style="background-color: #B62020; border-radius: 10px; padding: 30px; color: white; font-size: 18px; line-height: 1.4; margin-bottom: 60px; border: 3px solid #9f1818;">
                <p style="margin: 0 0 15px 0;"><strong>%s %s</strong> ha segnalato un problema per l’articolo <strong>%s</strong> nella conferenza <strong>%s</strong>.</p>
                <p style="margin: 0 0 15px 0;">Motivo della segnalazione: <em>%s</em></p>
            </div>

            <!-- Footer -->
            <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">Paper <span style="color: #13698A;">Review</span></div>
            <div style="font-style: italic; color: #707E8C;">La conoscenza alla portata di un click</div>
        </div>
    </body>
    </html>
    """, nomeSegnalante, cognomeSegnalante, titoloPaper, nomeConferenza, motivo);
    }
}