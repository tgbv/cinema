package com.example.application.views.shared;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.function.Consumer;
import java.util.function.Function;

public class AreYouSureDialog {
    private Dialog dialog;

    public AreYouSureDialog(Consumer<Dialog> yesCallback, Consumer<Dialog> noCallback) {
        dialog = new Dialog();
        dialog.setHeaderTitle("Are you sure?");

        HorizontalLayout hl = new HorizontalLayout(
            new Button("Yes", e -> yesCallback.accept(dialog)),
            new Button("No", e -> noCallback.accept(dialog))
        );

        dialog.add(hl);
    }

    public Dialog getDialog() {
        return dialog;
    }

}
