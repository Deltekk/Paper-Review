package paperreviewserver.common.email;

public class NotificaScadenzaMail extends MailBase {
    private final String body;

    public NotificaScadenzaMail(String to, String subject, String body) {
        super(to, subject);
        this.body = body;
    }

    @Override
    public String getBody() {
        return body;
    }
}