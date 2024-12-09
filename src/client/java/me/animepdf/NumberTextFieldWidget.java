package me.animepdf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class NumberTextFieldWidget extends TextFieldWidget {
    String allowedChars = "0123456789";

    public NumberTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, text);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (!allowedChars.contains(String.valueOf(chr))) return false;

        return super.charTyped(chr, modifiers);
    }
}
