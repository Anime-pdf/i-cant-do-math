package me.animepdf;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
//? if >=1.21.9 {
import net.minecraft.client.input.CharInput;
//?}
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class NumberTextFieldWidget extends TextFieldWidget {
    String allowedChars = "0123456789";

    public NumberTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(textRenderer, x, y, width, height, text);
    }

    @Override
    //? if <1.21.9 {
    /*public boolean charTyped(char chr, int modifiers) {
    *///?} else {
    public boolean charTyped(CharInput input) {
        char chr = (char)input.codepoint();
    //?}
        if (!allowedChars.contains(String.valueOf(chr))) return false;

        //? if <1.21.9 {
        /*return super.charTyped(chr, modifiers);
        *///?} else {
        return super.charTyped(input);
        //?}
    }
}
