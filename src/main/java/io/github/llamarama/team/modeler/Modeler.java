package io.github.llamarama.team.modeler;

import io.github.llamarama.team.modeler.common.block.ModelerBlock;
import io.github.llamarama.team.modeler.common.block_entity.ModelerBlockEntity;
import io.github.llamarama.team.modeler.common.screen_handler.ModelerScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Modeler implements ModInitializer {

    public static final Block MODELER =
            new ModelerBlock(AbstractBlock.Settings.copy(Blocks.CRAFTING_TABLE));
    public static final String MDOID = "modeler";
    public static final Identifier MODELER_CHANNEL = id("modeler");
    public static final BlockEntityType<ModelerBlockEntity> MODELER_BE =
            FabricBlockEntityTypeBuilder.create(ModelerBlockEntity::new, MODELER).build();
    public static final ScreenHandlerType<ModelerScreenHandler> MODELER_HANDLER =
            ScreenHandlerRegistry.registerSimple(id("modeler"), ModelerScreenHandler::new);

    @Contract("_ -> new")
    public static @NotNull Identifier id(String path) {
        return new Identifier(MDOID, path);
    }

    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, id("modeler"), MODELER);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, id("modeler"), MODELER_BE);

        ServerPlayNetworking.registerGlobalReceiver(MODELER_CHANNEL, (server, player, handler, buf, responseSender) -> {
            int modelDataValue = buf.readInt();

            server.execute(() -> {
                if (player.currentScreenHandler instanceof ModelerScreenHandler modelerHandler) {
                    modelerHandler.setCustomModelData(modelDataValue);
                }
            });
        });
    }

}
