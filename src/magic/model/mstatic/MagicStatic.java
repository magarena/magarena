package magic.model.mstatic;

import java.util.Set;

import magic.model.MagicAbility;
import magic.model.MagicAbilityList;
import magic.model.MagicAmount;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.action.PutStateTriggerOnStackAction;
import magic.model.action.RemoveStaticAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicPermanentTargetFilter;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;

public abstract class MagicStatic extends MagicDummyModifier implements MagicChangeCardDefinition {

    public static final boolean UntilEOT = true;
    public static final boolean Forever = !UntilEOT;

    //permanents affected by the static effect
    private MagicTargetFilter<MagicPermanent> filter;

    //layer where this effect operate
    private final MagicLayer layer;

    private final boolean isUntilEOT;

    protected MagicStatic(final MagicLayer aLayer, final MagicTargetFilter<MagicPermanent> aFilter, final boolean aIsUntilEOT) {
        filter = aFilter;
        layer = aLayer;
        isUntilEOT = aIsUntilEOT;
    }

    protected MagicStatic(final MagicLayer aLayer, final MagicTargetFilter<MagicPermanent> aFilter) {
        this(aLayer, aFilter, Forever);
    }

    protected MagicStatic(final MagicLayer aLayer, final boolean aIsUntilEOT) {
        this(aLayer, MagicTargetFilterFactory.NONE, aIsUntilEOT);
    }

    protected MagicStatic(final MagicLayer aLayer) {
        this(aLayer, MagicTargetFilterFactory.NONE, Forever);
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addStatic(this);
    }

    public final MagicLayer getLayer() {
        return layer;
    }

    public final boolean isUntilEOT() {
        return isUntilEOT;
    }

    public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
        if (filter == MagicTargetFilterFactory.NONE) {
            return source == target && condition(game, source, target);
        } else {
            return filter.accept(source, source.getController(), target) && condition(game, source, target);
        }
    }

    public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
        return true;
    }

    public static boolean acceptLinked(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
        if (source.isEquipment()) {
            return source.getEquippedCreature() == target;
        } else if (source.isEnchantment()) {
            return source.getEnchantedPermanent() == target;
        } else {
            return source.getPairedCreature() == target ||
                (source == target && source.isPaired());
        }
    }

    public static MagicStatic genPTStatic(final MagicTargetFilter<MagicPermanent> affected, final MagicAmount count, final MagicPowerToughness given) {
        return new MagicStatic(MagicLayer.ModPT, affected) {
            @Override
            public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
                final int amt = count.getAmount(source, source.getController());
                pt.add(given.power() * amt, given.toughness() * amt);
            }
        };
    }

    public static MagicStatic genPTStatic(final MagicTargetFilter<MagicPermanent> affected, final int givenPower, final int givenToughness) {
        return new MagicStatic(MagicLayer.ModPT, affected) {
            @Override
            public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
                pt.add(givenPower, givenToughness);
            }
        };
    }

    public static MagicStatic genPTStatic(final MagicCondition condition, final int givenPower, final int givenToughness) {
        return new MagicStatic(MagicLayer.ModPT) {
            @Override
            public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
                pt.add(givenPower, givenToughness);
            }
            @Override
            public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return condition.accept(source);
            }
        };
    }

    public static MagicStatic genPTStatic(final MagicCondition condition, final MagicTargetFilter<MagicPermanent> filter, final int givenPower, final int givenToughness) {
        return new MagicStatic(MagicLayer.ModPT, filter) {
            @Override
            public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
                pt.add(givenPower, givenToughness);
            }
            @Override
            public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return condition.accept(source);
            }
        };
    }

    public static MagicStatic genPTStatic(final int givenPower, final int givenToughness) {
        return new MagicStatic(MagicLayer.ModPT) {
            @Override
            public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
                pt.add(givenPower, givenToughness);
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target);
            }
        };
    }

    public static MagicStatic genPTSetStatic(final int givenPower, final int givenToughness) {
        return new MagicStatic(MagicLayer.SetPT) {
            @Override
            public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
                pt.set(givenPower, givenToughness);
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target);
            }
        };
    }

    public static MagicStatic genABStatic(final MagicTargetFilter<MagicPermanent> filter, final MagicAbilityList abilityList) {
        return new MagicStatic(MagicLayer.Ability, filter) {
            @Override
            public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
                abilityList.giveAbility(permanent, flags);
            }
        };
    }

    public static MagicStatic genABStatic(final MagicCondition condition, final MagicTargetFilter<MagicPermanent> filter, final MagicAbilityList abilityList) {
        return new MagicStatic(MagicLayer.AbilityCond, filter) {
            @Override
            public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
                abilityList.giveAbility(permanent, flags);
            }
            @Override
            public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return condition.accept(source);
            }
        };
    }

    public static MagicStatic genABStatic(final MagicCondition condition, final MagicAbilityList abilityList) {
        return new MagicStatic(MagicLayer.AbilityCond) {
            @Override
            public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
                abilityList.giveAbility(permanent, flags);
            }
            @Override
            public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return condition.accept(source);
            }
        };
    }

    public static MagicStatic linkedABStatic(final MagicCondition condition, final MagicAbilityList abilityList) {
        return new MagicStatic(MagicLayer.Ability) {
            @Override
            public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
                abilityList.giveAbility(permanent, flags);
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target) && condition.accept(source);
            }
        };
    }

    public static MagicStatic linkedABStatic(final MagicAbilityList abilityList) {
        return new MagicStatic(MagicLayer.Ability) {
            @Override
            public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
                abilityList.giveAbility(permanent, flags);
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target);
            }
        };
    }

    public static MagicStatic genABGameStatic(final MagicTargetFilter<MagicPermanent> filter, final MagicAbilityList abilityList) {
        return new MagicStatic(MagicLayer.Ability, filter) {
            @Override
            public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
                abilityList.giveAbility(permanent, flags);
            }
        };
    }

    public static MagicStatic genTypeStatic(final int givenTypeFlags) {
        return new MagicStatic(MagicLayer.Type) {
            @Override
            public int getTypeFlags(final MagicPermanent permanent,final int flags) {
                return flags | givenTypeFlags;
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target);
            }
        };
    }

    public static MagicStatic genSTStatic(final Set<MagicSubType> givenSubTypeFlags) {
        return new MagicStatic(MagicLayer.Type) {
            @Override
            public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
                flags.addAll(givenSubTypeFlags);
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target);
            }
        };
    }

    public static MagicStatic AddLinkedColor(final int givenColorFlags) {
        return new MagicStatic(MagicLayer.Color) {
            @Override
            public int getColorFlags(final MagicPermanent permanent, final int flags) {
                return flags | givenColorFlags;
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target);
            }
        };
    }

    public static MagicStatic SetLinkedColor(final int givenColorFlags) {
        return new MagicStatic(MagicLayer.Color) {
            @Override
            public int getColorFlags(final MagicPermanent permanent, final int flags) {
                return givenColorFlags;
            }
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return MagicStatic.acceptLinked(game, source, target);
            }
        };
    }

    public static MagicStatic ControlEnchanted = new MagicStatic(MagicLayer.Control) {
        @Override
        public MagicPlayer getController(final MagicPermanent source, final MagicPermanent target, final MagicPlayer player) {
            return source.getController();
        }
        @Override
        public boolean accept(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return source.getEnchantedPermanent() == target;
        }
    };

    public static MagicStatic ControlAsLongAsSourceIsOnBattlefield(final MagicPlayer you, final MagicPermanent target) {
        final MagicTargetFilter<MagicPermanent> filter = new MagicPermanentTargetFilter(target);
        return new MagicStatic(MagicLayer.Control,filter) {
            @Override
            public MagicPlayer getController(final MagicPermanent source, final MagicPermanent permanent, final MagicPlayer player) {
                return source.getController();
            }
        };
    }

    public static MagicStatic ControlAsLongAsYouControlSource(final MagicPlayer you, final MagicPermanent target) {
        final MagicTargetFilter<MagicPermanent> filter = new MagicPermanentTargetFilter(target);
        return new MagicStatic(MagicLayer.Control,filter) {
            @Override
            public MagicPlayer getController(final MagicPermanent source, final MagicPermanent permanent, final MagicPlayer player) {
                return source.getController();
            }
            @Override
            public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
                if (you.getIndex() != source.getController().getIndex()) {
                    //remove this static after the update
                    game.addDelayedAction(new RemoveStaticAction(source, this));
                    return false;
                } else {
                    return true;
                }
            }
        };
    }

    public static MagicStatic ControlAsLongAsYouControlSourceAndSourceIsTapped(final MagicPlayer you, final MagicPermanent target) {
        final MagicTargetFilter<MagicPermanent> filter = new MagicPermanentTargetFilter(target);
        return new MagicStatic(MagicLayer.Control,filter) {
            @Override
            public MagicPlayer getController(final MagicPermanent source, final MagicPermanent permanent, final MagicPlayer player) {
                return source.getController();
            }
            @Override
            public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
                if (you.getIndex() != source.getController().getIndex() || source.isUntapped()) {
                    //remove this static after the update
                    game.addDelayedAction(new RemoveStaticAction(source, this));
                    return false;
                } else {
                    return true;
                }
            }
        };
    }

    public static MagicStatic ControlAsLongAsSourceIsTapped(final MagicPlayer you, final MagicPermanent target) {
        final MagicTargetFilter<MagicPermanent> filter = new MagicPermanentTargetFilter(target);
        return new MagicStatic(MagicLayer.Control,filter) {
            @Override
            public MagicPlayer getController(final MagicPermanent source, final MagicPermanent permanent, final MagicPlayer player) {
                return source.getController();
            }
            @Override
            public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
                if (source.isUntapped()) {
                    //remove this static after the update
                    game.addDelayedAction(new RemoveStaticAction(source, this));
                    return false;
                } else {
                    return true;
                }
            }
        };
    }

    public static MagicStatic Unleash = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            flags.add(MagicAbility.CannotBlock);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.hasCounters(MagicCounterType.PlusOne);
        }
    };

    public static MagicStatic SwitchPT = new MagicStatic(MagicLayer.SwitchPT, MagicStatic.UntilEOT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.set(pt.toughness(),pt.power());
        }
    };

    public static MagicStatic Zombie = new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
            flags.add(MagicSubType.Zombie);
        }
    };

    public static MagicStatic Vampire = new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
            flags.add(MagicSubType.Vampire);
        }
    };

    public static MagicStatic Spirit = new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.add(MagicSubType.Spirit);
        }
    };

    public static MagicStatic IsBlack = new MagicStatic(MagicLayer.Color) {
        @Override
        public int getColorFlags(final MagicPermanent permanent,final int flags) {
            return MagicColor.Black.getMask();
        }
    };

    public static MagicStatic IsWhite = new MagicStatic(MagicLayer.Color) {
        @Override
        public int getColorFlags(final MagicPermanent permanent, final int flags) {
            return MagicColor.White.getMask();
        }
    };

    public static MagicStatic AddBlack = new MagicStatic(MagicLayer.Color) {
        @Override
        public int getColorFlags(final MagicPermanent permanent,final int flags) {
            return flags | MagicColor.Black.getMask();
        }
    };

    public static MagicStatic Artifact = new MagicStatic(MagicLayer.Type) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags | MagicType.Artifact.getMask();
        }
    };

    public static MagicStatic AllCreatureTypesUntilEOT = new MagicStatic(MagicLayer.Type, UntilEOT) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.addAll(MagicSubType.ALL_CREATURES);
        }
    };

    public static MagicStatic Bestowed = new MagicStatic(MagicLayer.Type) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags & ~MagicType.Creature.getMask();
        }
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
            flags.removeAll(MagicSubType.ALL_CREATURES);
            flags.add(MagicSubType.Aura);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getEnchantedPermanent().isValid();
        }
    };
    public static MagicStatic Nightmare = new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
            flags.add(MagicSubType.Nightmare);
        }
    };

    public static MagicStatic AsLongAsCond(final MagicPermanent chosen, final int P, final int T, final MagicCondition cond) {
        final long id = chosen.getId();
        return new MagicStatic(MagicLayer.ModPT) {
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return target.getId() == id && condition(game, source, target);
            }
            @Override
            public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
                pt.add(P,T);
            }
            @Override
            public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                if (cond.accept(source)) {
                    return true;
                } else {
                    //remove this static after the update
                    game.addDelayedAction(new RemoveStaticAction(source, this));
                    return false;
                }
            }
        };
    }

    public static MagicStatic AsLongAsCond(final MagicPermanent chosen, final MagicAbility ability, final MagicCondition cond) {
        final long id = chosen.getId();
        return new MagicStatic(MagicLayer.Ability) {
            @Override
            public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return target.getId() == id && condition(game, source, target);
            }
            @Override
            public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
                permanent.addAbility(ability, flags);
            }
            @Override
            public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                if (cond.accept(source)) {
                    return true;
                } else {
                    //remove this static after the update
                    game.addDelayedAction(new RemoveStaticAction(source, this));
                    return false;
                }
            }
        };
    }

    public static MagicStatic StateTrigger(final MagicCondition cond, final MagicSourceEvent effect) {
        return new MagicStatic(MagicLayer.Game) {
            @Override
            public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
                return cond.accept(source);
            }
            @Override
            public void modGame(final MagicPermanent source, final MagicGame game) {
                final MagicEvent event = effect.getTriggerEvent(source);
                if (event.isValid()) {
                    game.doAction(new PutStateTriggerOnStackAction(event));
                }
            }
        };
    }

    public static MagicStatic YourCostReduction(final MagicTargetFilter<MagicCard> filter, int n) {
        return new MagicStatic(MagicLayer.CostReduction) {
            @Override
            public MagicManaCost reduceCost(final MagicPermanent source, final MagicCard card, final MagicManaCost cost) {
                if (filter.accept(source, source.getController(), card) && source.isFriend(card)) {
                    return cost.reduce(n);
                } else {
                    return cost;
                }
            }
        };
    }
}
