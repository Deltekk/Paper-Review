package com.paperreview.paperreview.common.email;

public class MailEditorInvito extends MailBase {
    private final String nomeConferenza;
    private final String nomeUtente;

    public MailEditorInvito(String to, String nomeConferenza, String nomeUtente) {
        super(to, "NOMINA EDITORE - " + nomeConferenza);
        this.nomeConferenza = nomeConferenza;
        this.nomeUtente = nomeUtente;
    }

    @Override
    public String getBody() {
        return String.format("""
            <!DOCTYPE html>
            <html lang="it">
            <head>
                <meta charset="UTF-8">
                <title>Nomina Editore - Paper Review</title>
            </head>
            <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #fff; margin: 0; padding: 0;">
                <div style="max-width: 600px; margin: 40px auto; padding: 20px; text-align: center;">

                    <!-- Logo -->
                    <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">
                        Paper <span style="color: #13698A;">Review</span>
                    </div>

                    <!-- Titolo -->
                    <h2 style="font-weight: 700; color: #606878; margin-bottom: 40px; font-size: 24px;">NOMINA EDITORE</h2>

                    <!-- Box blu -->
                    <div style="background-color: #0B7C9F; border-radius: 10px; padding: 30px; color: white; font-size: 18px; line-height: 1.4; margin-bottom: 60px; border: 3px solid #0a6e8b;">
                        <p style="margin: 0 0 15px 0;">Salve <strong>%s</strong>,</p>
                        <p style="margin: 0 0 15px 0;">Le notifichiamo che è stato <strong>nominato automaticamente</strong> come editore per la conferenza <strong>%s</strong>.</p>
                        <p style="margin: 0 0 15px 0;">Potrà gestire la conferenza direttamente all’interno della piattaforma Paper Review.</p>
                    </div>

                    <!-- Footer -->
                    <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">Paper <span style="color: #13698A;">Review</span></div>
                    <div style="font-style: italic; color: #707E8C;">La conoscenza alla portata di un click</div>
                </div>
            </body>
            </html>
        """, nomeUtente, nomeConferenza);
    }
}