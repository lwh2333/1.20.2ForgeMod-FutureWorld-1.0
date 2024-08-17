package com.mcfire.autoplay.events;

import com.mcfire.autoplay.network.ModMessages;
import com.mcfire.autoplay.network.packet.skill.SkillCTSPacket;
import com.mcfire.autoplay.registry.ModKeys;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class InputEvents {
    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        if(ModKeys.FIRST_SKILL.isDown()){
            ModMessages.sendToServer(new SkillCTSPacket(1)); // the skill id
        }
        if(ModKeys.SECOND_SKILL.isDown()){
            ModMessages.sendToServer(new SkillCTSPacket(2));
        }
        if(ModKeys.THIRD_SKILL.isDown()){
            ModMessages.sendToServer(new SkillCTSPacket(3));
        }
        if(ModKeys.FOURTH_SKILL.isDown()){
            ModMessages.sendToServer(new SkillCTSPacket(4));
        }
    }
}
