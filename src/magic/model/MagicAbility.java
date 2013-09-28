package magic.model;

import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicGainActivation;
import magic.model.event.MagicLevelUpActivation;
import magic.model.event.MagicPingActivation;
import magic.model.event.MagicPlayCardEvent;
import magic.model.event.MagicPlayMulticounterEvent;
import magic.model.event.MagicPumpActivation;
import magic.model.event.MagicRegenerationActivation;
import magic.model.event.MagicSacrificeManaActivation;
import magic.model.event.MagicSacrificeTapManaActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicPainTapManaActivation;
import magic.model.event.MagicTiming;
import magic.model.event.MagicVividManaActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicCyclingActivation;
import magic.model.event.MagicReinforceActivation;
import magic.model.event.MagicNinjutsuActivation;
import magic.model.event.MagicEvokeActivation;
import magic.model.event.MagicKickerCost;
import magic.model.event.MagicMultikickerCost;
import magic.model.mstatic.MagicCDA;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicAllyGrowTrigger;
import magic.model.trigger.MagicAnnihilatorTrigger;
import magic.model.trigger.MagicAttacksPumpTrigger;
import magic.model.trigger.MagicBattleCryTrigger;
import magic.model.trigger.MagicBecomesBlockedPumpTrigger;
import magic.model.trigger.MagicBloodthirstTrigger;
import magic.model.trigger.MagicComesIntoPlayWithCounterTrigger;
import magic.model.trigger.MagicCumulativeUpkeepTrigger;
import magic.model.trigger.MagicDamageGrowTrigger;
import magic.model.trigger.MagicDevourTrigger;
import magic.model.trigger.MagicDieDrawCardTrigger;
import magic.model.trigger.MagicEchoTrigger;
import magic.model.trigger.MagicEntersDamageTargetTrigger;
import magic.model.trigger.MagicEntersExileCreatureOrSacrificeTrigger;
import magic.model.trigger.MagicExaltedTrigger;
import magic.model.trigger.MagicFadeVanishCounterTrigger;
import magic.model.trigger.MagicFlankingTrigger;
import magic.model.trigger.MagicFromGraveyardToLibraryTrigger;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;
import magic.model.trigger.MagicLandfallPumpTrigger;
import magic.model.trigger.MagicLeavesDamageTargetTrigger;
import magic.model.trigger.MagicLeavesGainLifeTrigger;
import magic.model.trigger.MagicLeavesReturnExileTrigger;
import magic.model.trigger.MagicLivingWeaponTrigger;
import magic.model.trigger.MagicMiracleTrigger;
import magic.model.trigger.MagicModularTrigger;
import magic.model.trigger.MagicRampageTrigger;
import magic.model.trigger.MagicRavnicaLandTrigger;
import magic.model.trigger.MagicRefugeLandTrigger;
import magic.model.trigger.MagicReplicateTrigger;
import magic.model.trigger.MagicSoulshiftTrigger;
import magic.model.trigger.MagicSpecterTrigger;
import magic.model.trigger.MagicStormTrigger;
import magic.model.trigger.MagicTappedIntoPlayTrigger;
import magic.model.trigger.MagicTappedIntoPlayUnlessTrigger;
import magic.model.trigger.MagicTappedIntoPlayUnlessTwoTrigger;
import magic.model.trigger.MagicThiefTrigger;
import magic.model.trigger.MagicThiefTrigger.Type;
import magic.model.trigger.MagicThiefTrigger.Choice;
import magic.model.trigger.MagicThiefTrigger.Player;
import magic.model.trigger.MagicWhenBlocksPumpTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;
import magic.model.trigger.MagicWhenDiesTrigger;
import magic.model.trigger.MagicAtEndOfTurnTrigger;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;
import magic.model.trigger.MagicExtortTrigger;
import magic.model.trigger.MagicUnleashTrigger;
import magic.model.trigger.MagicUndyingTrigger;
import magic.model.trigger.MagicPersistTrigger;
import magic.model.trigger.MagicLandfallTrigger;
import magic.model.trigger.MagicCascadeTrigger;
import magic.model.trigger.MagicWhenTargetedTrigger;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

import java.util.EnumSet;
import java.util.Set;
import java.util.List;

public enum MagicAbility {

    AttacksEachTurnIfAble("attacks each turn if able",-10),
    CannotBlock("can't block",-50),
    CannotAttackOrBlock("can't attack or block",-200),
    CannotBlockWithoutFlying("can't block creatures without flying",-40),
    CannotBeBlockedByFlying("can't be blocked by creatures with flying",20),
    CannotBeBlockedExceptWithFlying("can't be blocked except by creatures with flying",30),
    CannotBeBlockedExceptWithFlyingOrReach("can't be blocked except by creatures with flying or reach",25),
    CannotBeBlockedExceptBySliver("can't be blocked except by slivers", 90),
    CannotBeBlockedByHumans("can't be blocked by humans",10),
    CannotBeBlockedByBlack("can't be blocked by black creatures",10),
    CannotBeBlockedByBlue("can't be blocked by blue creatures",10),
    CannotBeBlockedByGreen("can't be blocked by green creatures",10),
    CannotBeBlockedByRed("can't be blocked by red creatures",10),
    CannotBeBlockedByWhite("can't be blocked by white creatures",10),
    CannotBeBlockedByTokens("can't be blocked by creature tokens",10),
    CanBlockShadow("can block creatures with shadow",10),
    CannotBeCountered("can't be countered",0),
    Hexproof("hexproof",80),
    CannotBeTheTarget0("can't be the target of spells or abilities your opponents control",80),
    CannotBeTheTarget1("can't be the target of spells or abilities your opponents control",80),
    CannotBeTheTargetOfNonGreen("can't be the target of nongreen spells or abilities from nongreen sources",10),
    CannotBeTheTargetOfBlackOrRedOpponentSpell("can't be the target of black or red spells your opponents control",10),
    Deathtouch("deathtouch",60),
    Defender("defender",-100),
    DoesNotUntap("doesn't untap during untap step",-30),
    DoubleStrike("double strike",100),
    Fear("fear",50),
    Flash("flash",0),
    Flying("flying",50),
    FirstStrike("first strike",50),
    Plainswalk("plainswalk",10),
    Islandwalk("islandwalk",10),
    Swampwalk("swampwalk",10),
    Mountainwalk("mountainwalk",10),
    Forestwalk("forestwalk",10),
    Indestructible("indestructible",150),
    Haste("haste",0),
    Lifelink("lifelink",40),
    ProtectionFromBlack("protection from black",20),
    ProtectionFromBlue("protection from blue",20),
    ProtectionFromGreen("protection from green",20),
    ProtectionFromRed("protection from red",20),
    ProtectionFromWhite("protection from white",20),
    ProtectionFromMonoColored("protection from monocolored",50),
    ProtectionFromAllColors("protection from all colors",150),
    ProtectionFromCreatures("protection from creatures",100),
    ProtectionFromArtifacts("protection from artifacts",50),
    ProtectionFromDemons("protection from Demons",10),
    ProtectionFromDragons("protection from Dragons",10),
    ProtectionFromVampires("protection from Vampires",10),
    ProtectionFromWerewolves("protection from Werewolves",10),
    ProtectionFromColoredSpells("protection from colored spells",100),
    ProtectionFromEverything("protection from everything",200),
    ProtectionFromZombies("protection from Zombies",10),
    Reach("reach",20),
    Shadow("shadow",30),
    Shroud("shroud",60),
    Trample("trample",30),
    Unblockable("unblockable",100),
    Vigilance("vigilance",20),
    Wither("wither",30),
    TotemArmor("totem armor",0),
    Intimidate("intimidate",45),
    Infect("infect",35),
    Soulbond("soulbond",30),
    CantActivateAbilities("can't activate abilities",-20),

    Undying("undying",60) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicUndyingTrigger.create());
        }
    },
    Persist("persist",60) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicPersistTrigger.create());
        }
    },
    Modular("modular", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.PlusOne,"+1/+1",n));
            card.add(MagicModularTrigger.create());
        }
    },
    Flanking("flanking",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicFlankingTrigger.create());
        }
    },
    Changeling("changeling",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicCDA.Changeling);
        }
    },
    Exalted("exalted",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicExaltedTrigger.create());
        }
    },
    BattleCry("battle cry",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicBattleCryTrigger.create());
        }
    },
    LivingWeapon("living weapon", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicLivingWeaponTrigger.create());
        }
    },
    Regenerate("regenerate",30) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost manaCost = MagicManaCost.create(arg);
            card.add(new MagicRegenerationActivation(manaCost));
        }
    },
    Bushido("bushido",20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicBecomesBlockedPumpTrigger(n,n,false));
            card.add(new MagicWhenBlocksPumpTrigger(n,n));
        }
    },
    Soulshift("soulshift",20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicSoulshiftTrigger(n));
        }
    },
    Fading("fading",-20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Charge,"fade",n));
            card.add(new MagicFadeVanishCounterTrigger("fade"));
        }
    },
    Vanishing("vanishing",-20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            if (n > 0) {
                card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Charge,"time",n));
            }
            card.add(new MagicFadeVanishCounterTrigger("time"));
        }
    },
    Pump("pump", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final String[] token = arg.split(" ");
            final MagicManaCost cost = MagicManaCost.create(token[0]);
            final String[] pt = token[1].replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(new MagicPumpActivation(cost,power,toughness));
        }
    },
    CumulativeUpkeep("cumulative upkeep",-30) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost manaCost = MagicManaCost.create(arg);
            card.add(new MagicCumulativeUpkeepTrigger(manaCost));
        }
    },
    LevelUp("level up", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final String[] token = arg.split(" ");
            final MagicManaCost cost = MagicManaCost.create(token[0]);
            final int maxLevel = Integer.parseInt(token[1]);
            card.add(new MagicLevelUpActivation(cost, maxLevel));
        }
    },
    BlockedPump("blocked pump", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final String[] pt = arg.replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(new MagicBecomesBlockedPumpTrigger(power,toughness,false));
        }
    },
    BlockedByPump("blocked by pump", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final String[] pt = arg.replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(new MagicBecomesBlockedPumpTrigger(power,toughness,true));
        }
    },
    BlocksOrBlockedPump("blocks or blocked pump", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final String[] pt = arg.replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(new MagicWhenBlocksPumpTrigger(power,toughness));
            card.add(new MagicBecomesBlockedPumpTrigger(power,toughness,false));
        }
    },
    ShockLand("shock land", -10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicRavnicaLandTrigger.create());
        }
    },
    Devour("devour",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicDevourTrigger(n));
        }
    },
    Ping("ping",20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicPingActivation(n));
        }
    },
    Rampage("rampage",20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicRampageTrigger(n));
        }
    },
    AttacksPump("attacks pump", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final String[] pt = arg.replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(new MagicAttacksPumpTrigger(power,toughness));
        }
    },
    AllyGrow("ally grow",20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicAllyGrowTrigger.create());
        }
    },
    LandfallPump("landfall pump",20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final String[] pt = arg.replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(new MagicLandfallPumpTrigger(power,toughness));
        }
    },
    LandfallQuest("landfall quest",0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicLandfallTrigger.Quest);
        }
    },
    TapAddMana("tap add mana",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(arg);
            card.add(new MagicTapManaActivation(manatype));
        }
    },
    TapAddCharge("tap add charge",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicPermanentActivation.TapAddCharge);
        }
    },
    TapDrainAddMana("tap drain add mana",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(arg);
            card.add(new MagicVividManaActivation(manatype));
        }
    },
    TapPainAddMana("tap pain add mana", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(arg);
            card.add(new MagicPainTapManaActivation(manatype));
        }
    },
    TapPreventDamage("tap prevent damage", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(MagicPermanentActivation.PreventDamage(n));
        }
    },
    SacAddMana("sac add mana",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(arg);
            card.add(new MagicSacrificeManaActivation(manatype));
        }
    },
    SacAtEnd("sac at end",-100) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicAtEndOfTurnTrigger.Sacrifice);
        }
    },
    SacWhenTargeted("sac when targeted",-10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicWhenTargetedTrigger.SacWhenTargeted);
        }
    },
    ChargeAtUpkeep("charge at upkeep",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicAtUpkeepTrigger.MayCharge);
        }
    },
    TapSacAddMana("tap sac add mana",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(arg);
            card.add(new MagicSacrificeTapManaActivation(manatype));
        }
    },
    UntapSelf("untap",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost manaCost = MagicManaCost.create(arg);
            card.add(MagicPermanentActivation.Untap(manaCost));
        }
    },
    GainAbility("gains",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int idx = arg.indexOf(' ');
            final String[] token = {arg.substring(0,idx), arg.substring(idx+1)};
            final MagicManaCost cost = MagicManaCost.create(token[0]);
            final MagicAbility ability = MagicAbility.getAbility(token[1]);
            final MagicTiming timing = (ability == Haste || ability == Vigilance) ?
                MagicTiming.FirstMain :
                MagicTiming.Pump;
            card.add(new MagicGainActivation(
                cost,
                ability,
                new MagicActivationHints(timing,1),
                token[1]
            ));
        }
    },
    DamageDiscardCard("damage discard",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicSpecterTrigger(Type.Any, Player.Any, n));
        }
    },
    CombatDamageDiscardCard("combat damage discard",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicSpecterTrigger(Type.Combat, Player.Any, n));
        }
    },
    CombatDamageDiscardRandomCard("combat damage discard random",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(MagicSpecterTrigger.Random(Type.Combat, Player.Any, n));
        }
    },
    DamageOpponentDiscardRandomCard("damage opponent discard random",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(MagicSpecterTrigger.Random(Type.Any, Player.Opponent, n));
        }
    },
    DiesReturnToOwnersHand("dies return to owner's hand",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicWhenDiesTrigger.ReturnToOwnersHand);
        }
    },
    DamageOpponentDrawCard("damage opponent draw card",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(new MagicThiefTrigger(Type.Any, Choice.Must, Player.Opponent));
        }
    },
    CombatDamageDrawCard("combat damage draw card",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(new MagicThiefTrigger(Type.Combat, Choice.Must, Player.Any));
        }
    },
    CombatDamageMayDrawCard("combat damage may draw card",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(new MagicThiefTrigger(Type.Combat, Choice.May, Player.Any));
        }
    },
    DamagePlayerGrow("damage player grow",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(new MagicDamageGrowTrigger(false, true));
        }
    },
    DamageCreatureGrow("damage creature grow",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(new MagicDamageGrowTrigger(false, false));
        }
    },
    CombatDamagePlayerGrow("combat damage player grow",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(new MagicDamageGrowTrigger(true, true));
        }
    },
    CombatDamageCreatureGrow("combat damage creature grow",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(new MagicDamageGrowTrigger(true, false));
        }
    },
    OpponentDiscardOntoBattlefield("opponent discard onto battlefield",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicWhenPutIntoGraveyardTrigger.OpponentDiscardOntoBattlefield);
        }
    },
    RecoverGraveyard("dead recover graveyard",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicWhenPutIntoGraveyardTrigger.RecoverGraveyard);
        }
    },
    GraveyardToLibrary("graveyard to library",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicFromGraveyardToLibraryTrigger.create());
        }
    },
    LibraryInteadOfGraveyard("library instead of graveyard",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicWhenPutIntoGraveyardTrigger.LibraryInsteadOfGraveyard);
        }
    },
    Champion("champion",-10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            card.add(new MagicEntersExileCreatureOrSacrificeTrigger(arg));
            card.add(MagicLeavesReturnExileTrigger.create());
        }
    },
    EntersGainLife("enters gain life", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicRefugeLandTrigger(n));
        }
    },
    EntersLoseLife("enters lose life", -10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicRefugeLandTrigger(-n));
        }
    },
    LeavesGainLife("leaves gain life", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicLeavesGainLifeTrigger(n));
        }
    },
    LeavesLoseLife("leaves lose life", -10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicLeavesGainLifeTrigger(-n));
        }
    },
    LeavesReturnExile("leaves return exile", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicLeavesReturnExileTrigger.create());
        }
    },
    EntersChooseOpponent("enters choose opponent", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicWhenComesIntoPlayTrigger.ChooseOpponent);
        }
    },
    EntersTapped("enters tapped", -10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicTappedIntoPlayTrigger.create());
        }
    },
    EntersCharged("enters with charge", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Charge,"charge",n));
        }
    },
    EntersMining("enters with mining", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Charge,"mining",n));
        }
    },
    EntersArrowhead("enters with arrowhead", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Charge,"arrowhead",n));
        }
    },
    EntersXPlus("enters with X +1/+1", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicWhenComesIntoPlayTrigger.XPlusOneCounters);
        }
    },
    EntersPlus("enters with +1/+1", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.PlusOne,"+1/+1",n));
        }
    },
    EntersMinus("enters with -1/-1", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.MinusOne,"-1/-1",n));
        }
    },
    EntersTappedUnlessTwo("enters tapped unless two", -10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicTappedIntoPlayUnlessTwoTrigger.create());
        }
    },
    EntersTappedUnless("enters tapped unless", -10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final String[] token = arg.split(" ");
            final MagicSubType t1 = MagicSubType.getSubType(token[0]);
            final MagicSubType t2 = MagicSubType.getSubType(token[1]);
            card.add(new MagicTappedIntoPlayUnlessTrigger(t1,t2));
        }
    },
    Echo("echo",-20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() == false: this + " does not accept empty arg";
            card.add(new MagicEchoTrigger(MagicManaCost.create(arg)));
        }
    },
    Bloodthirst("bloodthirst",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicBloodthirstTrigger(n));
        }
    },
    Storm("storm", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicStormTrigger.create());
        }
    },
    Annihilator("annihilator", 80) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(new MagicAnnihilatorTrigger(n));
        }
    },
    Multicounter("enters with +1/+1 for each kick", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost manaCost = MagicManaCost.create(arg);
            card.add(new MagicPlayMulticounterEvent(manaCost));
        }
    },
    Miracle("miracle", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost manaCost = MagicManaCost.create(arg);
            card.add(new MagicMiracleTrigger(manaCost));
        }
    },
    Kicker("kicker", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost cost = MagicManaCost.create(arg);
            card.add(new MagicKickerCost(cost));
        }
    },
    Buyback("buyback", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost cost = MagicManaCost.create(arg);
            card.add(MagicKickerCost.Buyback(cost));
        }
    },
    Multikicker("multikicker", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost cost = MagicManaCost.create(arg);
            card.add(new MagicMultikickerCost(cost));
        }
    },
    Replicate("replicate", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost cost = MagicManaCost.create(arg);
            card.add(MagicMultikickerCost.Replicate(cost));
            card.add(MagicReplicateTrigger.create());
        }
    },
    EntersEffect("enters effect", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            card.add(MagicWhenComesIntoPlayTrigger.create(arg));
        }
    },
    EntersMayEffect("enters effect PN may", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            card.add(MagicWhenComesIntoPlayTrigger.createMay(arg));
        }
    },
    DiesEffect("dies effect", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            card.add(MagicWhenDiesTrigger.create(arg));
        }
    },
    DiesMayEffect("dies effect PN may", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            card.add(MagicWhenDiesTrigger.createMay(arg));
        }
    },
    LeavesEffect("leaves effect", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            card.add(MagicWhenLeavesPlayTrigger.create(arg));
        }
    },
    LeavesMayEffect("leaves effect PN may", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            card.add(MagicWhenLeavesPlayTrigger.createMay(arg));
        }
    },
    ControlEnchanted("control enchanted", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicStatic.ControlEnchanted);
        }
    },
    ReturnAtEnd("return at end", -50) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicAtEndOfTurnTrigger.ReturnAtEnd);
        }
    },
    ReturnToOwnersHand("return to owner's hand", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost manaCost = MagicManaCost.create(arg);
            card.add(MagicPermanentActivation.ReturnToOwnersHand(manaCost));
        }
    },
    SwitchPT("switch pt", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost manaCost = MagicManaCost.create(arg);
            card.add(MagicPermanentActivation.SwitchPT(manaCost));
        }
    },
    Evoke("evoke", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost manaCost = MagicManaCost.create(arg);
            card.add(new MagicEvokeActivation(manaCost));
            card.add(MagicWhenComesIntoPlayTrigger.Evoke);
        }
    },
    Evolve("evolve", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicWhenOtherComesIntoPlayTrigger.Evolve);
        }
    },
    Extort("extort", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicExtortTrigger.create());
        }
    },
    Cycling("cycling", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost manaCost = MagicManaCost.create(arg);
            card.add(new MagicCyclingActivation(manaCost));
        }
    },
    Reinforce("reinforce", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final String[] token = arg.split(" ");
            final int n = Integer.parseInt(token[0]);
            final MagicManaCost manaCost = MagicManaCost.create(token[1]);
            card.add(new MagicReinforceActivation(n, manaCost));
        }
    },
    Unleash("unleash", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicUnleashTrigger.create());
            card.add(MagicStatic.Unleash);
        }
    },
    Ninjutsu("ninjutsu", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final MagicManaCost manaCost = MagicManaCost.create(arg);
            card.add(new MagicNinjutsuActivation(manaCost));
        }
    },
    Cascade("cascade", 50) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            assert arg.isEmpty() : this + " does not accept arg = " + arg;
            card.add(MagicCascadeTrigger.create());
        }
    },
    Lord("lord", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final String prefix = "other ";
            final boolean other = arg.startsWith(prefix);
            final String rest = arg.replaceFirst(prefix, "");
            final String[] tokens = rest.split(" get | have | has ");
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.build(tokens[0]);
            if (rest.contains(" get ")) {
                final String[] pt = tokens[1].replace('+','0').split("/");
                final int power = Integer.parseInt(pt[0]);
                final int toughness = Integer.parseInt(pt[1]);
                if (other) {
                    card.add(MagicStatic.genPTStaticOther(filter, power, toughness));
                } else {
                    card.add(MagicStatic.genPTStatic(filter, power, toughness));
                }
            } else {
                final MagicAbilityList abilityList = MagicAbility.getAbilityList(tokens[1].split(", "));
                if (other) {
                    card.add(MagicStatic.genABStaticOther(filter, abilityList));
                } else {
                    card.add(MagicStatic.genABStatic(filter, abilityList));
                }
            }
        }
    },
    Poisonous("poisonous", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
            final int n = Integer.parseInt(arg);
            card.add(MagicWhenDamageIsDealtTrigger.Poisonous(n));
        }
    },
    None("",0);

    public static final Set<MagicAbility> PROTECTION_FLAGS = EnumSet.range(ProtectionFromBlack, ProtectionFromZombies);
    
    public static final Set<MagicAbility> LANDWALK_FLAGS = EnumSet.range(Plainswalk, Forestwalk);
    
    private final String name;
    private final int score;

    private MagicAbility(final String aName,final int aScore) {
        name  = aName;
        score = aScore;
    }

    public String getName() {
        return name;
    }

    protected void addAbilityImpl(final MagicAbilityStore card, final String arg) {
        assert arg.isEmpty() : this + " does not accept arg = " + arg;
    }

    public void addAbility(final MagicAbilityStore card, final String ability) {
        final String arg = ability.substring(getName().length()).trim();
        card.addAbility(this);
        addAbilityImpl(card, arg);
    }

    @Override
    public String toString() {
        return name;
    }

    private int getScore() {
        return score;
    }

    public static int getScore(final Set<MagicAbility> flags) {
        int score=0;
        for (final MagicAbility ability : flags) {
            score+=ability.getScore();
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

    public static MagicAbilityList getAbilityList(final String[] names) {
        final MagicAbilityList abilityList = new MagicAbilityList();
        for (final String name : names) {
            getAbility(name).addAbility(abilityList, name);
        }
        return abilityList;
    }
    
    public static Set<MagicAbility> of(final MagicAbility first, MagicAbility... rest) {
        return EnumSet.of(first, rest);
    }

    public static Set<MagicAbility> noneOf() {
        return EnumSet.noneOf(MagicAbility.class);
    }
    
    public static MagicAbility CannotBeTheTarget(final MagicPlayer player) {
        return player.getIndex() == 0 ?
            CannotBeTheTarget0 :
            CannotBeTheTarget1;
    }
}
