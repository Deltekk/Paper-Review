package com.paperreview.paperreview.common.email;

public abstract class MailBase {
    protected String to;
    protected String subject;

    public MailBase(String to, String subject) {
        this.to = to;
        this.subject = subject;
    }

    public String getTo() { return to; }
    public String getSubject() { return subject; }

    // Metodo per generare il contenuto html o testo dell'email
    public abstract String getBody();
}
