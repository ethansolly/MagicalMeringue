package com.ethylol.magical_meringue.magic;

import com.ethylol.magical_meringue.magic.effects.BreakBlock;

import java.util.ArrayList;
import java.util.List;

public class Spell {

    //Object

    private ISpellEffect effect;

    public Spell(ISpellEffect effect) {
        this.effect = effect;
    }

    public ISpellEffect getEffect() {
        return effect;
    }

    //Static

    public static final Spell breakBlock = new Spell(new BreakBlock());
    public static final List<Spell> list = new ArrayList<>();
    static {
        list.add(breakBlock);
    }
}
