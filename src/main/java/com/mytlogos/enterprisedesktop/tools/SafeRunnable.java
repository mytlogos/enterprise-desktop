package com.mytlogos.enterprisedesktop.tools;

/**
 *
 */
public class SafeRunnable implements Runnable {
    private final Runnable runnable;

    public SafeRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        if (this.runnable == null) {
            System.err.println("invalid runnable: null");
            return;
        }

        try {
            runnable.run();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
