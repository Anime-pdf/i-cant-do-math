package me.animepdf;

import me.animepdf.utils.CursorScreen;
import me.animepdf.utils.ItemCalculator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static me.animepdf.utils.Localization.*;

@Environment(EnvType.CLIENT)
public class CalculatorScreen extends CursorScreen {
    private final Screen parent;
    private final BiConsumer<Integer, Integer> onExitCallback;

    private int items;
    private Pair<Integer, Integer> itemsTooltip;
    private final AtomicBoolean itemsValid;

    private int stack;
    private Pair<Integer, Integer> stackTooltip;
    private final AtomicBoolean stackValid;

    public CalculatorScreen(Text title, Screen parent, BiConsumer<Integer, Integer> onExit, Pair<Integer, Integer> data) {
        super(title);
        this.parent = parent;
        this.onExitCallback = onExit;

        this.stackValid = new AtomicBoolean(data != null);
        this.itemsValid = new AtomicBoolean(data != null);
        if(data == null) {
            data = new Pair<>(0, 64);
        }

        this.items = data.getLeft();
        this.stack = data.getRight();
    }

    @Override
    public void close() {
        onExitCallback.accept(items, stack);
        this.client.setScreen(this.parent);
    }

    /// Validate inputs
    private int validateInput(String input, int fallback, boolean allowZero, AtomicBoolean validityFlag) {
        validityFlag.set(true);

        try {
            int result = Integer.parseInt(input);
            if (!allowZero && result == 0) {
                throw new NumberFormatException();
            }
            return result;
        } catch (NumberFormatException e) {
            if (!input.isEmpty()) {
                validityFlag.set(false);
            }
            return fallback;
        }
    }

    /// Simplify rendering
    private TextWidget drawLabel(Text text) {
        var labelWidth = textRenderer.getWidth(text) + 10;
        var itemsLabel = new TextWidget(getCursorX(), getCursorY(), labelWidth, 20, text, textRenderer);
        this.addDrawableChild(itemsLabel);
        itemsLabel.alignCenter();
        MoveCursor(labelWidth, 0);
        return itemsLabel;
    }
    private NumberTextFieldWidget drawNumberTextBox(String text, Consumer<String> validation, String placeholder) {
        var BoxWidth = textRenderer.getWidth(text) + 8;
        var Box = new NumberTextFieldWidget(textRenderer, getCursorX(), getCursorY(), BoxWidth, 20, Text.literal("input"));
        this.addDrawableChild(Box);
        Box.setMaxLength(text.length());
        Box.setPlaceholder(Text.literal(placeholder));
        Box.setChangedListener(validation);
        MoveCursor(BoxWidth, 0);
        return Box;
    }

    private void drawItemCount(DrawContext context, String item, int amount, int visualAmount) {
        ItemStack itemStack = Registries.ITEM.get(Identifier.of(Identifier.DEFAULT_NAMESPACE, item)).getDefaultStack();
        itemStack.setCount(visualAmount);
        context.drawItemWithoutEntity(itemStack, getCursorX(), getCursorY());

        //? if <1.21.2 {
        context.drawItemInSlot(textRenderer, itemStack, getCursorX(), getCursorY());
        //?} else
        /*context.drawStackOverlay(textRenderer, itemStack, getCursorX(), getCursorY());*/

        MoveCursor(20, 0);
        context.drawText(textRenderer, String.valueOf(amount), getCursorX(), getCursorY()+4, 0xFFFFFFFF, true);
    }

    /// Render parts
    private void renderShulkers(DrawContext context) {
        var shulkerItems = ItemCalculator.calculate(items, new int[]{stack * 27, stack});

        context.drawText(textRenderer, SHULKERS, getCursorX(), getCursorY()+4, 0xFFFFFFFF, true);
        NextLine();

        drawItemCount(context, "shulker_box", shulkerItems.get(0), 1);
        NextLine();
        drawItemCount(context, "iron_ingot", shulkerItems.get(1), stack);
        NextLine();
        drawItemCount(context, "iron_ingot", shulkerItems.get(2), 1);
    }

    private void renderChests(DrawContext context) {
        var chestItems = ItemCalculator.calculate(items, new int[]{stack * 54, stack});

        NextColumn(this.width);
        UpdateFallbacks();

        context.drawText(textRenderer, DOUBLE_CHESTS, getCursorX(), getCursorY()+4, 0xFFFFFFFF, true);
        NextLine();

        drawItemCount(context, "chest", chestItems.get(0), 2);
        NextLine();
        drawItemCount(context, "iron_ingot", chestItems.get(1), stack);
        NextLine();
        drawItemCount(context, "iron_ingot", chestItems.get(2), 1);
    }

    private void renderStacks(DrawContext context) {
        var stacksItems = ItemCalculator.calculate(items, new int[]{stack});

        NextColumn(this.width);
        UpdateFallbacks();

        context.drawText(textRenderer, STACKS, getCursorX(), getCursorY()+4, 0xFFFFFFFF, true);
        NextLine();

        drawItemCount(context, "iron_ingot", stacksItems.get(0), stack);
        NextLine();
        drawItemCount(context, "iron_ingot", stacksItems.get(1), 1);
    }

    /// Hooks
    @Override
    protected void init() {
        InitCursor(40, 40);
        SetLineHeight(20);
        SetColumns(3);
        SetCurColumn(0);

        var itemsWidget = drawNumberTextBox("000000000", s -> this.items = validateInput(s, this.items, true, itemsValid), String.valueOf(this.items));
        drawLabel(TITLE1);
        var stackWidget = drawNumberTextBox("000", s -> this.stack = validateInput(s, this.stack, false, stackValid), String.valueOf(this.stack));
        drawLabel(TITLE2);
        NextLine();
        NextLine();
        UpdateOrigin();

        itemsTooltip = new Pair<>(itemsWidget.getX(), itemsWidget.getY());
        stackTooltip = new Pair<>(stackWidget.getX(), stackWidget.getY());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        ResetCursor(true);

        if(!itemsValid.get())
        {
            context.drawTooltip(textRenderer, TOOLTIP_ITEMS, itemsTooltip.getLeft(), itemsTooltip.getRight() - 5);
        }
        if(!stackValid.get())
        {
            context.drawTooltip(textRenderer, TOOLTIP_STACK, stackTooltip.getLeft(), stackTooltip.getRight() - 5);
        }

        renderShulkers(context);
        renderChests(context);
        renderStacks(context);
    }
}
