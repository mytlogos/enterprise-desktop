package com.mytlogos.enterprisedesktop.tools;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Supplier;
import java.util.logging.*;

/**
 *
 */
public class Log {
    private final static Log log = new Log();
    private final Logger logger;

    public Log() {
        this.logger = Logger.getAnonymousLogger();
        this.addHandler();
    }

    private void addHandler() {
        this.logger.setUseParentHandlers(false);
        final ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new Formatter() {
            public static final String ANSI_RESET = "\u001B[0m";
            public static final String ANSI_BLACK = "\u001B[30m";
            public static final String ANSI_RED = "\u001B[31m";
            public static final String ANSI_GREEN = "\u001B[32m";
            public static final String ANSI_YELLOW = "\u001B[33m";
            public static final String ANSI_BLUE = "\u001B[34m";
            public static final String ANSI_PURPLE = "\u001B[35m";
            public static final String ANSI_CYAN = "\u001B[36m";
            public static final String ANSI_WHITE = "\u001B[37m";

            @Override
            public String format(LogRecord record) {
                return String.format(
                        "%s%s %s%s: %s%s%s%n",
                        ANSI_WHITE,
                        LocalDateTime.now().toString(),
                        getLevelColor(record.getLevel()),
                        record.getLevel(),
                        ANSI_WHITE,
                        record.getMessage(),
                        ANSI_RESET
                );
            }

            private String getLevelColor(Level level) {
                if (level == Level.SEVERE) {
                    return ANSI_RED;
                } else if (level == Level.WARNING) {
                    return ANSI_YELLOW;
                } else if (level == Level.INFO) {
                    return ANSI_GREEN;
                } else if (level == Level.CONFIG) {
                    return ANSI_BLUE;
                } else if (level == Level.FINE || level == Level.FINER || level == Level.FINEST) {
                    return ANSI_CYAN;
                } else {
                    return ANSI_WHITE;
                }
            }
        });
        this.logger.addHandler(handler);
        try {
            final FileHandler fileHandler = new FileHandler(
                    System.getProperty("user.dir") + "/enterprise.log",
                    10_000_000,
                    10,
                    true
            );
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format(
                            "{level: \"%s\", message:\"%s\", timestamp:\"%s\"}%n",
                            record.getLevel(),
                            record.getMessage(),
                            LocalDateTime.now().toString()
                    );
                }
            });
            this.logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void severe(String msg, Supplier<Object[]> supplier) {
        log(Level.SEVERE, msg, supplier);
    }

    public static void log(Level level, String msg, Supplier<Object[]> supplier) {
        if (log.logger.isLoggable(level)) {
            log.logger.log(level, String.format(msg, supplier.get()));
        }
    }

    public static void severe(String msg, Object... supplier) {
        log(Level.SEVERE, msg, supplier);
    }

    public static void log(Level level, String msg, Object... supplier) {
        if (log.logger.isLoggable(level)) {
            log.logger.log(level, String.format(msg, supplier));
        }
    }

    public static void severe(Supplier<String> supplier) {
        log(Level.SEVERE, supplier);
    }

    public static void log(Level level, Supplier<String> supplier) {
        if (log.logger.isLoggable(level)) {
            log.logger.log(level, supplier.get());
        }
    }

    public static void severe(String msg) {
        log(Level.SEVERE, msg);
    }

    public static void log(Level level, String msg) {
        if (log.logger.isLoggable(level)) {
            log.logger.log(level, msg);
        }
    }

    public static void warning(String msg, Supplier<Object[]> supplier) {
        log(Level.WARNING, msg, supplier);
    }

    public static void warning(String msg, Object... supplier) {
        log(Level.WARNING, msg, supplier);
    }

    public static void warning(Supplier<String> supplier) {
        log(Level.WARNING, supplier);
    }

    public static void warning(String msg) {
        log(Level.WARNING, msg);
    }

    public static void info(String msg, Supplier<Object[]> supplier) {
        log(Level.INFO, msg, supplier);
    }

    public static void info(String msg, Object... supplier) {
        log(Level.INFO, msg, supplier);
    }

    public static void info(Supplier<String> supplier) {
        log(Level.INFO, supplier);
    }

    public static void info(String msg) {
        log(Level.INFO, msg);
    }

    public static void config(String msg, Supplier<Object[]> supplier) {
        log(Level.CONFIG, msg, supplier);
    }

    public static void config(String msg, Object... supplier) {
        log(Level.CONFIG, msg, supplier);
    }

    public static void config(Supplier<String> supplier) {
        log(Level.CONFIG, supplier);
    }

    public static void config(String msg) {
        log(Level.CONFIG, msg);
    }

    public static void fine(String msg, Supplier<Object[]> supplier) {
        log(Level.FINE, msg, supplier);
    }

    public static void fine(String msg, Object... supplier) {
        log(Level.FINE, msg, supplier);
    }

    public static void fine(Supplier<String> supplier) {
        log(Level.FINE, supplier);
    }

    public static void fine(String msg) {
        log(Level.FINE, msg);
    }

    public static void finer(String msg, Supplier<Object[]> supplier) {
        log(Level.FINER, msg, supplier);

    }

    public static void finer(String msg, Object... supplier) {
        log(Level.FINER, msg, supplier);

    }

    public static void finer(Supplier<String> supplier) {
        log(Level.FINER, supplier);
    }

    public static void finer(String msg) {
        log(Level.FINER, msg);
    }

    public static void finest(String msg, Supplier<Object[]> supplier) {
        log(Level.FINEST, msg, supplier);
    }

    public static void finest(String msg, Object... supplier) {
        log(Level.FINEST, msg, supplier);
    }

    public static void finest(Supplier<String> supplier) {
        log(Level.FINEST, supplier);
    }

    public static void finest(String msg) {
        log(Level.FINEST, msg);
    }
}
