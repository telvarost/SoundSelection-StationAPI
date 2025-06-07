package com.github.telvarost.soundselection;

import com.periut.cryonicconfig.CryonicConfig;

public class ModHelperCryonicConfig {
    public static boolean loadDisplayGui() {
        return CryonicConfig.getConfig(ModHelper.MOD_ID).getBoolean("displayGui", ModHelper.ModHelperFields.displayGui);
    }
}
