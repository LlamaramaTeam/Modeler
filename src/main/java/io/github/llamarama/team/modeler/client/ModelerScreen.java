package io.github.llamarama.team.modeler.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.llamarama.team.modeler.Modeler;
import io.github.llamarama.team.modeler.common.screen_handler.ModelerScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModelerScreen extends HandledScreen<ModelerScreenHandler> {

    private static final Identifier TEXTURE = Modeler.id("textures/gui/modeler.png");
    private static final Text NUMBER_WARNING_MSG = Text.translatable("modeler.number_warning");
    private TextFieldWidget textField;
    private int wrongInputTicks;

    public ModelerScreen(ModelerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.wrongInputTicks = 0;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            if (this.client != null && this.client.player != null) {
                this.client.player.closeHandledScreen();
            }
        }

        if (this.textField.isActive()) {
            return this.textField.keyPressed(keyCode, scanCode, modifiers);
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        this.drawTexture(matrices, this.x, this.y, 0, 0, 256, 256);
    }

    @Override
    protected void init() {
        super.init();

        this.titleX = (backgroundWidth - textRenderer.getWidth(this.title)) / 2;

        int xPos = this.x + this.backgroundWidth / 2 + 5;
        int yPos = this.y + 25;
        TextFieldWidget textField = new TextFieldWidget(this.textRenderer, xPos, yPos, 60, 20, Text.empty());
        this.textField = this.addDrawableChild(textField);

        this.addDrawableChild(new ButtonWidget(xPos + 5, yPos + 30, 50, 20,
                Text.literal("Apply"), button -> {
            try {
                if (this.client != null) {
                    this.client.execute(() -> {
                        int customModelDataValue = Integer.parseInt(this.textField.getText());
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(customModelDataValue);
                        ClientPlayNetworking.send(Modeler.MODELER_CHANNEL, buf);
                    });
                }
            } catch (NumberFormatException ignored) {
                this.triggerWrongInputText();
            }
        }));

        this.textField.active = true;
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
        if (this.wrongInputTicks != 0) {
            int width = this.textRenderer.getWidth(NUMBER_WARNING_MSG);
            this.textRenderer.draw(matrices, NUMBER_WARNING_MSG,
                    this.backgroundWidth / 2f - width / 2f,
                    this.titleY - 20, 0x4F4F4F);

            this.wrongInputTicks--;
        }
    }

    private void triggerWrongInputText() {
        this.wrongInputTicks = 120;
    }

}
