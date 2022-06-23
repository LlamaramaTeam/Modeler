package io.github.llamarama.team.modeler.client;

import io.github.llamarama.team.modeler.Modeler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class ModelerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(Modeler.MODELER_HANDLER, ModelerScreen::new);
    }

}
