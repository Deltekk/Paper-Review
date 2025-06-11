package paperreviewserver.common.email;

public class MailRecuperoAccount extends MailBase {
    private String nomeUtente;
    private String passwordProvvisoria;

    public MailRecuperoAccount(String to, String nomeUtente, String passwordProvvisoria) {
        super(to, "NOTIFICA DI RECUPERO PASSWORD");
        this.nomeUtente = nomeUtente;
        this.passwordProvvisoria = passwordProvvisoria;
    }

    @Override
    public String getBody() {
        return String.format("""
            <!DOCTYPE html>
            <html lang="it">
            <head>
                <meta charset="UTF-8">
                <title>Recupero Password - Paper Review</title>
            </head>
            <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #fff; margin: 0; padding: 0;">
                <div style="max-width: 600px; margin: 40px auto; padding: 20px; text-align: center;">
                    <!-- Logo -->
                    <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">Paper <span style="color: #13698A;">Review</span></div>
            
                    <!-- Titolo -->
                    <h2 style="font-weight: 700; color: #606878; margin-bottom: 40px; font-size: 24px;">RECUPERO PASSWORD</h2>
            
                    <!-- Box blu -->
                    <div style="background-color: #0B7C9F; border-radius: 10px; padding: 30px; color: white; font-size: 18px; line-height: 1.4; margin-bottom: 60px; border: 3px solid #0a6e8b;">
                        <p style="margin: 0 0 15px 0;">Salve %s, le notifichiamo che è stato richiesto un reset della password per il suo account di Paper-Review.</p>
                        <p style="margin: 0 0 15px 0;"> La sua password è <span style="font-weight: 900;">%s</span>.</p>
                        <p style="margin: 0 0 15px 0;">Le consigliamo di cambiarla una volta che effettuerà il prossimo accesso.</p>
                    </div>
            
                    <!-- Footer -->
                    <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">Paper <span style="color: #13698A;">Review</span></div>
                    <div style="font-style: italic; color: #707E8C;">La conoscenza alla portata di un click</div>
                </div>
            </body>
            </html>
            """, nomeUtente, passwordProvvisoria);
    }
}