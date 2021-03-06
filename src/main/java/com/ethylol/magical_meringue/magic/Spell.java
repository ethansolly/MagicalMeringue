package com.ethylol.magical_meringue.magic;

import com.ethylol.magical_meringue.magic.effects.*;

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
    public static final Spell changeColor = new Spell(new ChangeColor());
    public static final Spell doNothing = new Spell(new DoNothing());
    public static final Spell invisibilityFlash = new Spell(new InvisibilityFlash());
    public static final Spell levitate = new Spell(new Levitate());
    public static final Spell makeMorsel = new Spell(new MakeMorsel());
    public static final Spell smeltItem = new Spell(new SmeltItem());
    public static final Spell summonUselessCat = new Spell(new SummonUselessCat());
    public static final List<Spell> list = new ArrayList<>();
    static {
        list.add(breakBlock);
        list.add(changeColor);
        list.add(doNothing);
        list.add(invisibilityFlash);
        list.add(levitate);
        list.add(makeMorsel);
        list.add(smeltItem);
        list.add(summonUselessCat);
    }
}
