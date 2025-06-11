package paperreviewserver.common;

import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.ansi;

public class ConsoleLogger {

    public static void init() {
        // Inizializza Jansi una sola volta
        AnsiConsole.systemInstall();
    }

    public static void info(String message) {
        System.out.println(ansi().fgCyan().a("[INFO]    ").reset().a(message));
    }

    public static void success(String message) {
        System.out.println(ansi().fgGreen().a("[SUCCESS] ").reset().a(message));
    }

    public static void warning(String message) {
        System.out.println(ansi().fgYellow().a("[WARNING] ").reset().a(message));
    }

    public static void error(String message) {
        System.out.println(ansi().fgRed().a("[ERROR]   ").reset().a(message));
    }

    public static void job(String jobName, String message) {
        System.out.println(ansi().fgBlue().a("[" + jobName + "] ").reset().a(message));
    }

    public static void line() {
        System.out.println(ansi().fgBrightBlack().a("────────────────────────────────────────────────").reset());
    }

    public static void uninstall() {
        AnsiConsole.systemUninstall();
    }
}
