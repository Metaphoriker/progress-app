package de.metaphoriker.progress;

import com.google.inject.Guice;
import de.metaphoriker.progress.ui.ProgressApplication;
import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        Guice.createInjector(new ProgressModule());
        Application.launch(ProgressApplication.class, args);
    }
}