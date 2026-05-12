package com.wachi.sable_sa_compat;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ALLOW_BLOCK_ENTITY_PICKING = BUILDER
            .comment("Whether to allow the Create SA 'block picker' to grab block entities (such as chests or create fluid tanks)")
            .comment("Note that this is disabled by default because grabbing this blocks usually results in data losses")
            .define("allowPickingBlockEntities", false);

    static final ModConfigSpec SPEC = BUILDER.build();
}

