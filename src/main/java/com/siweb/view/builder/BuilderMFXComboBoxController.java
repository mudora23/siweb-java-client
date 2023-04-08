package com.siweb.view.builder;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.collections.ObservableList;

public class BuilderMFXComboBoxController<T> {

    private final MFXComboBox<T> mfxComboBox;

    public static class Builder<T> {

        private final ObservableList<T> items;
        private final String id;
        private String floatingText = "";
        private FloatMode floatMode = FloatMode.BORDER;
        private Boolean isAnimated = false;
        private Boolean isDisable = false;
        private String defaultText = "";
        private double prefWidth = Double.MAX_VALUE;

        public Builder(String id, String floatingText, ObservableList<T> items) {
            this.id = id;
            this.floatingText = id;
            this.items = items;
        }

        public Builder<T> setFloatingText(String floatingText) {
            this.floatingText = floatingText;
            return this;
        }
        public Builder<T> setFloatMode(FloatMode floatMode) {
            this.floatMode = floatMode;
            return this;
        }
        public Builder<T> setAnimated(Boolean isAnimated) {
            this.isAnimated = isAnimated;
            return this;
        }

        public Builder<T> setDisable(Boolean isDisable) {
            this.isDisable = isDisable;
            return this;
        }


        public Builder<T> setText(String defaultText) {
            this.defaultText = defaultText;
            return this;
        }

        public Builder<T> setPrefWidth(double prefWidth) {
            this.prefWidth = prefWidth;
            return this;
        }

        public BuilderMFXComboBoxController<T> build() {
            return new BuilderMFXComboBoxController<T>(this);
        }


    }

    private BuilderMFXComboBoxController(Builder<T> builder) {

        this.mfxComboBox = new MFXComboBox<T>(builder.items);

        this.mfxComboBox.setId(builder.id);
        this.mfxComboBox.setFloatingText(builder.floatingText);
        this.mfxComboBox.setFloatMode(builder.floatMode);
        this.mfxComboBox.setAnimated(builder.isAnimated);
        this.mfxComboBox.setText(builder.defaultText);
        this.mfxComboBox.setPrefWidth(builder.prefWidth);
        this.mfxComboBox.setDisable(builder.isDisable);
    }

    public MFXComboBox<T> get() {
        return this.mfxComboBox;
    }


}
