package com.mcfire.autoplay.registry;

import com.mcfire.autoplay.AutoPlay;
import com.mcfire.autoplay.blocks.ChatGPTDataBase;
import com.mcfire.autoplay.blocks.Protector;
import com.mcfire.autoplay.blocks.RefinedLubricatingOilPurifierBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS=DeferredRegister.create(ForgeRegistries.BLOCKS, AutoPlay.MODID);
    public static final RegistryObject<RefinedLubricatingOilPurifierBlock> REFINED_LUBRICATING_OIL_PURIFIER_BLOCK
            =BLOCKS.register("refined_lubricating_oil_purifier_block",
            ()->new RefinedLubricatingOilPurifierBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops().noOcclusion().strength(3.5F)
                    .instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE)));

    public static final RegistryObject<ChatGPTDataBase> CHAT_GPT_DATA_BASE
            =BLOCKS.register("chat_gpt_data_base",
            ()->new ChatGPTDataBase(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops().noOcclusion().strength(3F)
                    .instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_GRAY)
                    .randomTicks()));

    public static final RegistryObject<Protector> PROTECTOR
            =BLOCKS.register("protector",
            ()->new Protector(BlockBehaviour.Properties.of()
                    .noLootTable().isValidSpawn((I,J,K,L)->false).strength(-1.0F, 3600000.0F)
                    .instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE)));
}
