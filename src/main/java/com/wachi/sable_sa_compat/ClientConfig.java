package com.wachi.sable_sa_compat;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue HIDE_FIRST_PERSON_ARMOR = BUILDER
            .comment("Whether to prevent Create SA armors to modify the first person player's hand")
            .define("hideFirstPersonArmor", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}

