package com.paperreview.paperreview.common.email;

public class MailEditorFeedback extends MailBase {
    private final String nomeUtenteAutore;
    private final String nomePaper;
    private final String nomeConferenza;
    private final String contenutoFeedback;

    public MailEditorFeedback(String to, String nomeUtenteAutore, String nomePaper, String nomeConferenza, String contenutoFeedback) {
        super(to, "Feedback editoriale ricevuto");
        this.nomeUtenteAutore = nomeUtenteAutore;
        this.nomePaper = nomePaper;
        this.nomeConferenza = nomeConferenza;
        this.contenutoFeedback = contenutoFeedback;
    }

    @Override
    public String getBody() {
        return String.format("""
            <!DOCTYPE html>
            <html lang="it">
            <head>
                <meta charset="UTF-8">
                <title>Feedback Editoriale - Paper Review</title>
            </head>
            <body style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #fff; margin: 0; padding: 0;">
                <div style="max-width: 600px; margin: 40px auto; padding: 20px; text-align: center;">

                    <!-- Logo -->
                    <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">
                        Paper <span style="color: #13698A;">Review</span>
                    </div>

                    <!-- Titolo -->
                    <h2 style="font-weight: 700; color: #606878; margin-bottom: 40px; font-size: 24px;">Feedback Editoriale</h2>

                    <!-- Box blu -->
                    <div style="background-color: #0B7C9F; border-radius: 10px; padding: 30px; color: white; font-size: 18px; line-height: 1.4; margin-bottom: 60px; border: 3px solid #0a6e8b;">
                        <p style="margin: 0 0 15px 0;">Gentile <strong>%s</strong>,</p>
                        <p style="margin: 0 0 15px 0;">Le notifichiamo che ha ricevuto un feedback editoriale riguardo il paper <strong>%s</strong> per la conferenza <strong>%s</strong>.</p>
                        <p style="margin: 0 0 15px 0;">Il feedback è il seguente:</p>
                        <p style="margin: 15px 0 0 0; font-style: italic;">"%s"</p>
                    </div>

                    <!-- Footer -->
                    <div style="font-weight: 700; font-size: 28px; color: #44C8F4; margin-bottom: 6px;">Paper <span style="color: #13698A;">Review</span></div>
                    <div style="font-style: italic; color: #707E8C;">La conoscenza alla portata di un click</div>
                </div>
            </body>
            </html>
        """, nomeUtenteAutore, nomePaper, nomeConferenza, contenutoFeedback);
    }
}