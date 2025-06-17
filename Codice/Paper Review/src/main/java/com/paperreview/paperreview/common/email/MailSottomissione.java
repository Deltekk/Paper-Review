package com.paperreview.paperreview.common.email;

public class MailSottomissione extends MailBase {

    private final String nomeConferenza;
    private final String titoloPaper;
    private final String emailAutore;

    public MailSottomissione(String to, String nomeConferenza, String titoloPaper, String emailAutore) {
        super(to, "COAUTORE PAPER [" + nomeConferenza + "]");
        this.nomeConferenza = nomeConferenza;
        this.titoloPaper = titoloPaper;
        this.emailAutore = emailAutore;
    }

    @Override
    public String getBody() {
        return String.format("""
            <!DOCTYPE html>
            <html lang="it">
            <head>
                <meta charset="UTF-8">
                <title>Coautore Aggiunto - Paper Review</title>
            </head>
            <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #fff; margin: 0; padding: 0;">
                <div style="max-width: 600px; margin: 40px auto; padding: 20px; text-align: center;">
                    <!-- Logo -->
                    <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">Paper <span style="color: #13698A;">Review</span></div>
            
                    <!-- Titolo -->
                    <h2 style="font-weight: 700; color: #606878; margin-bottom: 40px; font-size: 24px;">NOTIFICA COAUTORE</h2>
            
                    <!-- Box blu -->
                    <div style="background-color: #0B7C9F; border-radius: 10px; padding: 30px; color: white; font-size: 18px; line-height: 1.4; margin-bottom: 60px; border: 3px solid #0a6e8b;">
                        <p style="margin: 0 0 15px 0;">Sei stato aggiunto da <strong>%s</strong> nel ruolo di <strong>coautore</strong> per il paper:</p>
                        <p style="margin: 0 0 15px 0;"><strong>%s</strong></p>
                        <p style="margin: 0 0 15px 0;">all'interno della conferenza <strong>%s</strong>.</p>
                        <p style="margin: 0;">Se non riconosci questa sottomissione, ti invitiamo a contattarci.</p>
                    </div>
            
                    <!-- Footer -->
                    <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">Paper <span style="color: #13698A;">Review</span></div>
                    <div style="font-style: italic; color: #707E8C;">La conoscenza alla portata di un click</div>
                </div>
            </body>
            </html>
        """, emailAutore, titoloPaper, nomeConferenza);
    }
}