package com.mcfire.autoplay.entity.model;

import com.mcfire.autoplay.AutoPlay;
import com.mcfire.autoplay.entity.monster.RobotEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class RobotModel extends DefaultedEntityGeoModel<RobotEntity> {
    public RobotModel() {
        super(new ResourceLocation(AutoPlay.MODID, "robot"), true);
    }
}
