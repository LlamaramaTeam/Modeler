package io.github.llamarama.team.modeler.client;

import io.github.llamarama.team.modeler.Modeler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class ModelerClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(Modeler.MODELER_HANDLER, ModelerScreen::new);
    }

}
