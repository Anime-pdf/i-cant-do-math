package me.animepdf;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import org.lwjgl.glfw.GLFW;

public class ICantDoMathModClient implements ClientModInitializer {
    private Pair<Integer, Integer> persistentData = null;

    private void saveData(Integer items, Integer stack) {
        persistentData = new Pair<>(items, stack);
    }

    private void registerKeyBinds() {
        var openBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.icantdomath.open", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "category.icantdomath.main"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openBinding.wasPressed()) {
                Screen currScreen = MinecraftClient.getInstance().currentScreen;
                MinecraftClient.getInstance().setScreen(new CalculatorScreen(Text.empty(), currScreen, this::saveData, persistentData));
            }
        });
    }

    @Override
    public void onInitializeClient() {
        registerKeyBinds();
    }
}