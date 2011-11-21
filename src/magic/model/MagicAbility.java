package magic.model;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.model.MagicManaType;
import magic.model.MagicSubType;

import magic.model.event.MagicActivationHints;
import magic.model.event.MagicTiming;

import magic.model.event.MagicRegenerationActivation;
import magic.model.event.MagicPumpActivation;
import magic.model.event.MagicPingActivation;
import magic.model.event.MagicLevelUpActivation;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicSacrificeTapManaActivation;
import magic.model.event.MagicGainActivation;

import magic.model.trigger.MagicExaltedTrigger;
import magic.model.trigger.MagicBattleCryTrigger;
import magic.model.trigger.MagicLivingWeaponTrigger;
import magic.model.trigger.MagicFlankingTrigger;
import magic.model.trigger.MagicEchoTrigger;
import magic.model.trigger.MagicTappedIntoPlayTrigger;
import magic.model.trigger.MagicModularTrigger;
import magic.model.trigger.MagicComesIntoPlayWithCounterTrigger;
import magic.model.trigger.MagicBecomesBlockedPumpTrigger;
import magic.model.trigger.MagicWhenBlocksPumpTrigger;
import magic.model.trigger.MagicSoulshiftTrigger;
import magic.model.trigger.MagicFadeVanishCounterTrigger;
import magic.model.trigger.MagicCumulativeUpkeepTrigger;
import magic.model.trigger.MagicBecomesBlockedPumpTrigger;
import magic.model.trigger.MagicWhenBlocksPumpTrigger;
import magic.model.trigger.MagicRavnicaLandTrigger;
import magic.model.trigger.MagicDevourTrigger;
import magic.model.trigger.MagicRefugeLandTrigger;
import magic.model.trigger.MagicTappedIntoPlayUnlessTrigger;
import magic.model.trigger.MagicTappedIntoPlayUnlessTwoTrigger;
import magic.model.trigger.MagicRampageTrigger;
import magic.model.trigger.MagicAttacksPumpTrigger;
import magic.model.trigger.MagicEntersDrawCardTrigger;
import magic.model.trigger.MagicAllyGrowTrigger;
import magic.model.trigger.MagicLandfallPumpTrigger;
import magic.model.trigger.MagicSpecterTrigger;
import magic.model.trigger.MagicDieDrawCardTrigger;
import magic.model.trigger.MagicThiefTrigger;
import magic.model.trigger.MagicVeteranTrigger;
import magic.model.trigger.MagicFromGraveyardToLibraryTrigger;

public enum MagicAbility {

    Modular("modular", 10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final int n = Integer.parseInt(arg);
			card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.PlusOne,"+1/+1",n));
            card.add(MagicModularTrigger.create());
        }
    },
    EntersGainLife("enters gain life", 10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicRefugeLandTrigger(n));
        }
    },
    EntersLoseLife("enters lose life", 10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicRefugeLandTrigger(-n));
        }
    },
    EntersTapped("enters tapped", -10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(MagicTappedIntoPlayTrigger.create());
        }
    },
    EntersTappedUnlessTwo("enters tapped unless two", -10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(MagicTappedIntoPlayUnlessTwoTrigger.create());
        }
    },
    EntersTappedUnless("enters tapped unless", -10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final String[] token = arg.split(" ");
            final MagicSubType t1 = MagicSubType.getSubType(token[0]);
            final MagicSubType t2 = MagicSubType.getSubType(token[1]);
            card.add(new MagicTappedIntoPlayUnlessTrigger(t1,t2));
        }
    },
    EntersDrawCard("enters draw card", 10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(MagicEntersDrawCardTrigger.create());
        }
    },
    Echo("echo",-20) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            if (arg.isEmpty()) {
                card.add(MagicEchoTrigger.create());
            } else {
	            card.add(new MagicEchoTrigger(MagicManaCost.createCost(arg)));
            }
        }
    },
	AttacksEachTurnIfAble("attacks each turn if able",-10),
	CannotBlock("can't block",-50),
	CannotAttackOrBlock("can't attack or block",-200),
	CannotBlockWithoutFlying("can't block creatures without flying",-40),
	CannotBeBlockedByFlying("can't be blocked by creatures with flying",20),
	CannotBeBlockedExceptWithFlying("can't be blocked except by creatures with flying",30),
	CannotBeBlockedExceptWithFlyingOrReach("can't be blocked except by creatures with flying or reach",25),
	CannotBeBlockedByHumans("can't be blocked by humans",10),
	CannotBeBlockedByBlack("can't be blocked by black creatures",10),
	CannotBeBlockedByBlue("can't be blocked by blue creatures",10),
	CannotBeBlockedByGreen("can't be blocked by green creatures",10),
	CannotBeBlockedByRed("can't be blocked by red creatures",10),
	CannotBeBlockedByWhite("can't be blocked by white creatures",10),
	CanBlockShadow("can block creatures with shadow",10),
	CannotBeCountered("can't be countered",0),
	CannotBeTheTarget("hexproof",80),
	CannotBeTheTarget0("can't be the target of spells or abilities your opponents control",80),
	CannotBeTheTarget1("can't be the target of spells or abilities your opponents control",80),
	Changeling("changeling",10),
	Deathtouch("deathtouch",60),
	Defender("defender",-100),
	DoesNotUntap("doesn't untap during untap step",-30),
	DoubleStrike("double strike",100),
	Exalted("exalted",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(MagicExaltedTrigger.getInstance());
        }
    },
	Fear("fear",50),
	Flash("flash",0),
	Flying("flying",50),
	FirstStrike("first strike",50),
	Forestwalk("forestwalk",10),
	Indestructible("indestructible",150),
	Islandwalk("islandwalk",10),
	Haste("haste",0),
	LifeLink("lifelink",40),
	Mountainwalk("mountainwalk",10),
	Persist("persist",60),
	PlainsWalk("plainswalk",10),
	ProtectionFromBlack("protection from black",20),
	ProtectionFromBlue("protection from blue",20),
	ProtectionFromGreen("protection from green",20),
	ProtectionFromRed("protection from red",20),
	ProtectionFromWhite("protection from white",20),
	ProtectionFromMonoColored("protection from monocolored",50),
	ProtectionFromAllColors("protection from all colors",150),
	ProtectionFromCreatures("protection from creatures",100),
	ProtectionFromDemons("protection from Demons",10),
	ProtectionFromDragons("protection from Dragons",10),
	ProtectionFromVampires("protection from Vampires",10),
	ProtectionFromWerewolves("protection from Werewolves",10),
	ProtectionFromZombies("protection from Zombies",10),
	Reach("reach",20),
	Shadow("shadow",30),
	Shroud("shroud",60),
	Swampwalk("swampwalk",10),
	Trample("trample",30),
	Unblockable("unblockable",100),
	Vigilance("vigilance",20),
	Wither("wither",30),
	TotemArmor("totem armor",0),
	Intimidate("intimidate",45),
	BattleCry("battle cry",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
			card.add(MagicBattleCryTrigger.getInstance());
        }
    },
	Infect("infect",35),
    LivingWeapon("living weapon", 10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(MagicLivingWeaponTrigger.getInstance());
        }
    },
    Flanking("flanking",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(MagicFlankingTrigger.create());
        }
    },
    Regenerate("regenerate",30) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final MagicManaCost manaCost = MagicManaCost.createCost(arg);
            card.add(new MagicRegenerationActivation(manaCost));
        }
    },
    Bushido("bushido",20) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final int n = Integer.parseInt(arg);
	        card.add(new MagicBecomesBlockedPumpTrigger(n,n,false));
            card.add(new MagicWhenBlocksPumpTrigger(n,n));
        }
    },
    Soulshift("soulshift",20) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final int n = Integer.parseInt(arg);
	        card.add(new MagicSoulshiftTrigger(n));
        }
    },
    Fading("fading",-20) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final int n = Integer.parseInt(arg);
	        card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Charge,"fade",n));
	        card.add(new MagicFadeVanishCounterTrigger("fade"));
        }
    },
    Vanishing("vanishing",-20) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final int n = Integer.parseInt(arg);
			card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Charge,"time",n));
            card.add(new MagicFadeVanishCounterTrigger("time"));
        }
    },
    Pump("pump", 10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final String[] token = arg.split(" ");
            final MagicManaCost cost = MagicManaCost.createCost(token[0]);
            final String[] pt = token[1].replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
	        card.add(new MagicPumpActivation(cost,power,toughness));
        }
    },
    CumulativeUpkeep("cumulative upkeep",-30) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final MagicManaCost manaCost = MagicManaCost.createCost(arg);
	        card.add(new MagicCumulativeUpkeepTrigger(manaCost));
        }
    },
    LevelUp("level up", 10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final String[] token = arg.split(" ");
            final MagicManaCost cost = MagicManaCost.createCost(token[0]);
            final int maxLevel = Integer.parseInt(token[1]);
            card.add(new MagicLevelUpActivation(cost, maxLevel));
        }
    },
    BlockedPump("blocked pump", 10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final String[] pt = arg.replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
	        card.add(new MagicBecomesBlockedPumpTrigger(power,toughness,false));
        }
    },
    BlockedByPump("blocked by pump", 20) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final String[] pt = arg.replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
	        card.add(new MagicBecomesBlockedPumpTrigger(power,toughness,true));
        }
    },
    BlocksOrBlockedPump("blocks or blocked pump", 20) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final String[] pt = arg.replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(new MagicWhenBlocksPumpTrigger(power,toughness));
	        card.add(new MagicBecomesBlockedPumpTrigger(power,toughness,false));
        }
    },
    ShockLand("shock land", -10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(MagicRavnicaLandTrigger.create());
        }
    },
    Devour("devour",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicDevourTrigger(n));
        }
    },
    Ping("ping",20) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(MagicPingActivation.create());
        }
    },
    Rampage("rampage",20) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final int n = Integer.parseInt(arg);
	        card.add(new MagicRampageTrigger(n));
        }
    },
    AttacksPump("attacks pump", 10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final String[] pt = arg.replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
	        card.add(new MagicAttacksPumpTrigger(power,toughness));
        }
    },
    AllyGrow("ally grow",20) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
	        card.add(MagicAllyGrowTrigger.create());
        }
    },
    LandfallPump("landfall pump",20) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
	        card.add(MagicLandfallPumpTrigger.create());
        }
    },
    TapAddMana("tap add mana",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(arg);
            card.add(new MagicTapManaActivation(manatype, manatype.size() - 1));
        }
    },
    SacAddManaAny("sac add mana any",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(new MagicSacrificeTapManaActivation(MagicManaType.ALL_TYPES));
        }
    },
    GainAbility("gains",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            final int idx = arg.indexOf(' ');
            final String[] token = {arg.substring(0,idx), arg.substring(idx+1)};
            final MagicManaCost cost = MagicManaCost.createCost(token[0]);
            final MagicAbility ability = MagicAbility.getAbility(token[1]);
            final MagicTiming timing = (ability == Haste || ability == Vigilance) ?
                MagicTiming.FirstMain :
                MagicTiming.Pump;
            card.add(new MagicGainActivation(
                cost,
                ability,
                new MagicActivationHints(timing,false,1),
                token[1]));
        }
    },
    DamageDiscardCard("damage discard card",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(new MagicSpecterTrigger(false,false,false));
        }
    },
    CombatDamageDiscardCard("combat damage discard card",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(new MagicSpecterTrigger(true,false,false));
        }
    },
    DamageOpponentDiscardRandomCard("damage opponent discard random card",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(new MagicSpecterTrigger(false,true,true));
        }
    },
    DieDrawCard("die draw card",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(new MagicDieDrawCardTrigger(true));
        }
    },
    DieMayDrawCard("die may draw card",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(new MagicDieDrawCardTrigger(false));
        }
    },
    DamageDrawCard("damage draw card",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(new MagicThiefTrigger(false,true,1));
        }
    },
    CombatDamageMayDrawCard("combat damage may draw card",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(new MagicThiefTrigger(true,false,1));
        }
    },
    CombatDamageGrow("combat damage grow",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(new MagicVeteranTrigger(true));
        }
    },
    DamageGrow("damage grow",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(new MagicVeteranTrigger(false));
        }
    },
    GraveyardToLibrary("graveyard to library",10) {
        public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
            card.add(MagicFromGraveyardToLibraryTrigger.create());
        }
    },
    None("",0);

	public static final long PROTECTION_FLAGS=
		ProtectionFromBlack.getMask()|
		ProtectionFromBlue.getMask()|
		ProtectionFromGreen.getMask()|
		ProtectionFromRed.getMask()|
		ProtectionFromWhite.getMask()|
		ProtectionFromMonoColored.getMask()|
		ProtectionFromAllColors.getMask()|
		ProtectionFromCreatures.getMask()|
		ProtectionFromDemons.getMask()|
		ProtectionFromDragons.getMask()|
		ProtectionFromVampires.getMask()|
		ProtectionFromWerewolves.getMask()|
		ProtectionFromZombies.getMask();
	
	public static final long LANDWALK_FLAGS=
		Forestwalk.getMask()|
		Islandwalk.getMask()|
		Mountainwalk.getMask()|
		PlainsWalk.getMask()|
		Swampwalk.getMask();
	
    public static final long EXCLUDE_MASK = 
        Long.MAX_VALUE-Flash.getMask()-CannotBeCountered.getMask()-TotemArmor.getMask();
	
	private final String name;
	private final int score;
	private final long mask;
	
	private MagicAbility(final String name,final int score) {
		this.name=name;
		this.score=score;
		mask=1L<<ordinal();
	}
	
	public String getName() {
		return name;
	}

    public void addAbilityImpl(final MagicCardDefinition card, final String arg) {
        return;
    }
	
	@Override
	public String toString() {
		return name;
	}

	private int getScore() {
		return score;
	}
	
	public long getMask() {
		return mask;
	}

	public boolean hasAbility(final long flags) {
		return (flags&mask)!=0;
	}
	
	public static int getScore(final long flags) {
		int score=0;
		for (final MagicAbility ability : values()) {
			if (ability.hasAbility(flags)) {
				score+=ability.getScore();
			}
		}
		return score;
	}
	
	public static MagicAbility getAbility(final String name) {
        MagicAbility match = None;
		for (final MagicAbility ability : values()) {
			if (name.startsWith(ability.getName()) && ability.getName().length() > match.getName().length()) {
                match = ability;
			}
		}
        if (match == None) {
            throw new RuntimeException("Unable to convert " + name + " to an ability");
        } else {
            return match;
        }
	}
	
    public static long getAbilities(final String[] names) {
        long flags = 0;
		for (final String name : names) {
            flags |= getAbility(name).getMask();
		}
        return flags;
	}
}
