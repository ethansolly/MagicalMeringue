package com.ethylol.magical_meringue.utils;

public class Utils {
    /**Determine the max amount of mana of a certain tier a player at a given level has.
     *
     * @param tier
     * @param level
     * @return
     */
    public static int maxMana(int tier, int level) {
        return (tier > level)? 0 : 12 << (level-tier-1);
    }
}
