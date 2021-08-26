package io.github.llamarama.team.modeler.client;

import io.github.llamarama.team.modeler.Modeler;
import io.github.llamarama.team.modeler.common.screen_handler.ModelerScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.model.json.ModelOverride;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class ModelerScreen extends HandledScreen<ModelerScreenHandler> {

    private TextFieldWidget textField;
    private ButtonWidget applyButton;

    public ModelerScreen(ModelerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

    }

    @Override
    protected void init() {
        super.init();

        this.titleX = (backgroundWidth - textRenderer.getWidth(this.title)) / 2;
        int xPos = this.x;
        int yPos = this.y;
        TextFieldWidget textField = new TextFieldWidget(this.textRenderer, xPos, yPos, 60, 20,
                new LiteralText("Pog"));
        this.textField = this.addDrawableChild(textField);

        this.applyButton = this.addDrawableChild(new ButtonWidget(xPos + 5, yPos + 30, 50, 20,
                new LiteralText("Apply"), button -> {
            try {
                int customModelDataValue = Integer.parseInt(this.textField.getText());
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeInt(customModelDataValue);
                ClientPlayNetworking.send(Modeler.MODELER_CHANNEL, buf);
            } catch (NumberFormatException ignored) {

            }
        }));
    }

}
