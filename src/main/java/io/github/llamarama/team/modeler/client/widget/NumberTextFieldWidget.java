package io.github.llamarama.team.modeler.client.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class NumberTextFieldWidget extends TextFieldWidget {

    public NumberTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, text);
    }

    @Override
    public void write(String text) {
        super.write(text);
    }

}
