package com.pawcare.util;
import javax.swing.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.awt.Component;
/** JDBC, HTTP and report generation run off Swing's event thread. */
public final class Tasks {
    private Tasks() { }
    public static <T> void run(Component owner, JButton button, Callable<T> work, Consumer<T> success) {
        if(button!=null) button.setEnabled(false);
        new SwingWorker<T,Void>() {
            @Override protected T doInBackground() throws Exception { return work.call(); }
            @Override protected void done() {
                if(button!=null) button.setEnabled(true);
                try { T value=get(); if(owner.isDisplayable()) success.accept(value); }
                catch(InterruptedException ex) { Thread.currentThread().interrupt(); }
                catch(ExecutionException ex) { if(owner.isDisplayable()) Ui.error(owner,ex.getCause()); }
                catch(RuntimeException ex) { if(owner.isDisplayable()) Ui.error(owner,ex); }
            }
        }.execute();
    }
}
