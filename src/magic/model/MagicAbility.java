package magic.model;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import magic.data.EnglishToInt;
import magic.exception.ScriptParseException;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.condition.MagicConditionParser;
import magic.model.event.*;
import magic.model.mstatic.MagicCDA;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.trigger.*;

public enum MagicAbility {

    // intrinsic abilities (implemented directly in the game engine)
    AttacksEachTurnIfAble("(SN )?attack(s)? (each|this) (turn|combat) if able",-10),
    CannotBlock("(SN )?can't block",-50),
    CannotAttack("(SN )?can't attack",-50),
    CannotAttackOrBlock("(SN )?can't attack or block",-200),
    CannotBlockWithoutFlying("(SN )?can block only creatures with flying\\.",-40),
    CannotBeCountered("(SN )?can't be countered( by spells or abilities)?\\.",0),
    CannotBeTheTarget0("can't be the target of spells or abilities your opponents control",80),
    CannotBeTheTarget1("can't be the target of spells or abilities your opponents control",80),
    CannotBeTheTargetOfNonGreen("(SN )?can't be the target of nongreen spells or abilities from nongreen sources\\.",10),
    CannotBeTheTargetOfBlackOrRedOpponentSpell("(SN )?can't be the target of black or red spells your opponents control\\.",10),
    CanBlockShadow("(SN )?can block creatures with shadow as though (they didn't have shadow|SN had shadow)\\.",10),
    CanAttackWithDefender("can attack (this turn )?as though (it|they) didn't have defender", 10),
    Hexproof("hexproof",80),
    Deathtouch("deathtouch",60),
    Defender("defender",-100),
    DoesNotUntap("(SN )?(doesn't|don't) untap during (your|its controller's|their controllers') untap step(s)?",-30),
    DoubleStrike("double strike",100),
    Fear("fear",50),
    WhiteFear("(SN )?can't be blocked except by artifact creatures and/or white creatures",50),
    RedFear("(SN )?can't be blocked except by artifact creatures and/or red creatures",50),
    Flash("flash",0),
    Flying("flying",50),
    FirstStrike("first strike",50),

    Plainswalk("plainswalk",10),
    Islandwalk("islandwalk",10),
    Swampwalk("swampwalk",10),
    Mountainwalk("mountainwalk",10),
    Forestwalk("forestwalk",10),
    Snowlandwalk("snow landwalk",10),
    Snowswampwalk("snow swampwalk",10),
    Snowforestwalk("snow forestwalk",10),
    LegendaryLandwalk("legendary landwalk",10),
    NonbasicLandwalk("nonbasic landwalk",10),

    Indestructible("indestructible",150),
    Haste("haste",0),
    Lifelink("lifelink",40),
    Reach("reach",20),
    Shadow("shadow",30),
    Shroud("shroud",60),
    Trample("trample",30),
    Unblockable("(SN )?can't be blocked",100),
    Vigilance("vigilance",20),
    Wither("wither",30),
    TotemArmor("totem armor",0),
    Intimidate("intimidate",45),
    Infect("infect",35),
    Horsemanship("horsemanship",60),
    Soulbond("soulbond",30),
    SplitSecond("split second",10),
    CantActivateAbilities("(can't activate abilities|its activated abilities can't be activated)",-20),

    ProtectionFromBlack("(protection )?from black",20),
    ProtectionFromBlue("(protection )?from blue",20),
    ProtectionFromGreen("(protection )?from green",20),
    ProtectionFromRed("(protection )?from red",20),
    ProtectionFromWhite("(protection )?from white",20),
    ProtectionFromMonoColored("protection from monocolored",50),
    ProtectionFromMultiColored("protection from multicolored",50),
    ProtectionFromAllColors("protection from all colors",150),
    ProtectionFromColoredSpells("protection from colored spells",100),
    ProtectionFromEverything("protection from everything",200),

    // generalize intrinsic abilities (store a filter inside a trigger)
    ProtectionFromPermanent("protection from " + ARG.WORDRUN, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ProtectionTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg))
            ));
        }
    },
    CannotBeBlockedByPermanent("(SN )?can't be blocked by " + ARG.WORDRUN, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(CantBeBlockedTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg))
            ));
        }
    },
    CannotBeBlockedByPermanent2(ARG.WORDRUN + " can't block it", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(CantBeBlockedTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg))
            ));
        }
    },
    CannotBeBlockedExceptByPermanent("(SN )?can't be blocked except by " + ARG.WORDRUN, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(CantBeBlockedTrigger.createExcept(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg))
            ));
        }
    },
    Skulk("skulk", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(CantBeBlockedTrigger.Skulk);
        }
    },

    // keyword abilities
    Undying("undying",60) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(UndyingTrigger.create());
        }
    },
    Persist("persist",60) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(PersistTrigger.create());
        }
    },
    Modular("modular " + ARG.NUMBER, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new EntersWithCounterTrigger(MagicCounterType.PlusOne,n));
            card.add(ModularTrigger.create());
        }
    },
    Flanking("flanking",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(FlankingTrigger.create());
        }
    },
    Changeling("changeling",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCDA.Changeling);
        }
    },
    Devoid("devoid",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCDA.Devoid);
        }
    },
    Ingest("ingest",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ThisCombatDamagePlayerTrigger.Ingest);
        }
    },
    Exalted("exalted",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ExaltedTrigger.create());
        }
    },
    BattleCry("battle cry",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(BattleCryTrigger.create());
        }
    },
    LivingWeapon("living weapon", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(LivingWeaponTrigger.create());
        }
    },
    Bushido("bushido " + ARG.NUMBER,20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new BecomesBlockedPumpTrigger(n,n));
            card.add(new BlocksPumpTrigger(n,n));
        }
    },
    Soulshift("soulshift " + ARG.NUMBER,20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new SoulshiftTrigger(n));
        }
    },
    Fading("fading " + ARG.NUMBER,-20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new EntersWithCounterTrigger(MagicCounterType.Fade,n));
            card.add(FadeVanishCounterTrigger.Fade);
        }
    },
    Vanishing("vanishing " + ARG.NUMBER,-20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            if (n > 0) {
                card.add(new EntersWithCounterTrigger(MagicCounterType.Time,n));
            }
            card.add(FadeVanishCounterTrigger.Time);
        }
    },
    CumulativeUpkeep("cumulative upkeep " + ARG.MANACOST,-30) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new CumulativeUpkeepTrigger(manaCost));
        }
    },
    LevelUp("level up " + ARG.MANACOST + " " + ARG.NUMBER, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost cost = MagicManaCost.create(ARG.manacost(arg));
            final int maxLevel = ARG.number(arg);
            card.add(new MagicLevelUpActivation(cost, maxLevel));
        }
    },
    Devour("devour " + ARG.NUMBER,10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new DevourTrigger(n));
        }
    },
    Rampage("rampage " + ARG.NUMBER,20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new RampageTrigger(n));
        }
    },
    Champion("champion (a |an )?" + ARG.ANY,-10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new ChampionTrigger(ARG.any(arg)));
            card.add(LeavesReturnExiledTrigger.create());
        }
    },
    Echo("echo( |—)" + ARG.COST,-20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new EchoTrigger(new MagicRegularCostEvent(ARG.cost(arg))));
        }
    },
    Bloodthirst("bloodthirst " + ARG.NUMBER,10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new BloodthirstTrigger(n));
        }
    },
    Storm("storm", 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(StormTrigger.create());
        }
    },
    Annihilator("annihilator " + ARG.NUMBER, 80) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new AnnihilatorTrigger(n));
        }
    },
    Miracle("miracle " + ARG.MANACOST, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MiracleTrigger(manaCost));
        }
    },
    Kicker("kicker( |—)" + ARG.COST, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicKickerCost(new MagicRegularCostEvent(ARG.cost(arg))));
        }
    },
    Buyback("buyback( |—)" + ARG.COST, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicKickerCost.Buyback(new MagicRegularCostEvent(ARG.cost(arg))));
        }
    },
    Entwine("entwine( |—)" + ARG.COST, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicKickerCost.Entwine(new MagicRegularCostEvent(ARG.cost(arg))));
        }
    },
    Multikicker("multikicker " + ARG.MANACOST, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost cost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicMultikickerCost(cost));
        }
    },
    Replicate("replicate " + ARG.MANACOST, 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost cost = MagicManaCost.create(ARG.manacost(arg));
            card.add(MagicMultikickerCost.Replicate(cost));
            card.add(ThisSpellIsCastTrigger.Replicate);
        }
    },
    Conspire("conspire", 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicKickerCost.Conspire(new MagicRegularCostEvent(
                "Tap two untapped creatures you control that each share a color with it"
            )));
            card.add(ThisSpellIsCastTrigger.Conspire);
        }
    },
    Evoke("evoke " + ARG.MANACOST, 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicEvokeActivation(manaCost));
            card.add(EntersBattlefieldTrigger.Evoke);
        }
    },
    Transmute("transmute " + ARG.MANACOST, 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicTransmuteActivation(manaCost));
        }
    },
    Evolve("evolve", 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherEntersBattlefieldTrigger.Evolve);
        }
    },
    Extort("extort", 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ExtortTrigger.create());
        }
    },
    Cycling("cycling( |—)" + ARG.COST, 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicCyclingActivation(new MagicRegularCostEvent(ARG.cost(arg))));
        }
    },
    TypeCycling(ARG.WORDRUN + "cycling( |—)" + ARG.COST, 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicMatchedCostEvent matchedCost = new MagicRegularCostEvent(ARG.cost(arg));
            final String type = ARG.wordrun(arg);
            card.add(new MagicTypeCyclingActivation(matchedCost,type));
        }
    },
    Reinforce("reinforce " + ARG.NUMBER + "—" + ARG.MANACOST, 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicReinforceActivation(n, manaCost));
        }
    },
    Unleash("unleash", 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(UnleashTrigger.create());
            card.add(MagicStatic.Unleash);
        }
    },
    Ninjutsu("ninjutsu " + ARG.MANACOST, 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicNinjutsuActivation(manaCost));
        }
    },
    Dash("dash " + ARG.MANACOST, 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicDashActivation(manaCost));
        }
    },
    Surge("surge " + ARG.MANACOST, 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicSurgeActivation(cardDef, manaCost));
        }
    },
    Cascade("cascade", 50) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(CascadeTrigger.create());
        }
    },
    Graft("graft " + ARG.NUMBER,10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new EntersWithCounterTrigger(MagicCounterType.PlusOne,n));
            card.add(OtherEntersBattlefieldTrigger.Graft);
        }
    },
    Retrace("retrace",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(new MagicRetraceActivation(cardDef));
        }
    },
    Flashback("flashback( |—)" + ARG.COST,10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.buildCast(ARG.cost(arg));
            card.add(new MagicFlashbackActivation(cardDef, matchedCostEvents));
        }
    },
    Embalm("embalm " + ARG.MANACOST, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicEmbalmActivation(manaCost));
        }
    },
    Eternalize("eternalize " + ARG.MANACOST, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicEternalizeActivation(manaCost));
        }
    },
    Scavenge("scavenge " + ARG.MANACOST,10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(new MagicScavengeActivation(cardDef, manaCost));
        }
    },
    Unearth("unearth " + ARG.MANACOST,10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicUnearthActivation(manaCost));
        }
    },
    EquipCond("Equip( |—)" + ARG.COST + " to a " + ARG.WORDRUN, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.build(ARG.cost(arg));
            card.add(new MagicEquipActivation(matchedCostEvents, MagicTargetFilterFactory.Permanent(ARG.wordrun(arg) + " you control")));
        }
    },
    Equip("Equip( |—)" + ARG.COST, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.build(ARG.cost(arg));
            card.add(new MagicEquipActivation(matchedCostEvents));
        }
    },
    Megamorph("megamorph " + ARG.COST, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.build(ARG.cost(arg));
            card.add(new MagicMegamorphActivation(matchedCostEvents));
            card.add(MagicMorphCastActivation.Megamorph);
        }
    },
    Affinity("affinity for " + ARG.WORDRUN, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(MagicHandCastActivation.affinity(cardDef, MagicTargetFilterFactory.Permanent(ARG.wordrun(arg))));
        }
    },
    SelfLessToCastEach("SN costs \\{1\\} less to cast for each " + ARG.ANY + "\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(MagicHandCastActivation.reduction(cardDef, MagicAmountParser.build(ARG.any(arg))));
        }
    },
    SelfLessToCastCond("SN costs \\{" + ARG.NUMBER + "\\} less to cast if " + ARG.COND + "\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            final MagicCondition cond = MagicConditionParser.build(ARG.cond(arg));
            card.add(MagicHandCastActivation.reduction(cardDef, ARG.number(arg), cond));
        }
    },
    YourCardLessToCast(ARG.WORDRUN + " you cast cost \\{" + ARG.NUMBER + "\\} less to cast\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String cards = ARG.wordrun(arg).replaceAll("[Ss]pells", "cards") + " from your hand";
            card.add(MagicStatic.YourCostReduction(MagicTargetFilterFactory.Card(cards), ARG.number(arg)));
        }
    },
    YourCardMoreToCast(ARG.WORDRUN + " you cast cost " + ARG.MANACOST + " more to cast\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String cards = ARG.wordrun(arg).replaceAll("[Ss]pells", "cards") + " from your hand";
            card.add(MagicStatic.YourCostIncrease(MagicTargetFilterFactory.Card(cards), MagicManaCost.create(ARG.manacost(arg))));
        }
    },
    CardMoreToCast(ARG.WORDRUN + " cost " + ARG.MANACOST + " more to cast\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String cards = ARG.wordrun(arg).replaceAll("[Ss]pells", "cards") + " from your hand";
            card.add(MagicStatic.CostIncrease(MagicTargetFilterFactory.Card(cards), MagicManaCost.create(ARG.manacost(arg))));
        }
    },
    Emerge("emerge " + ARG.MANACOST, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(MagicHandCastActivation.emerge(cardDef, manaCost));
        }
    },
    Outlast("outlast "+ARG.COST,10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.build(ARG.cost(arg));
            card.add(new MagicOutlastActivation(matchedCostEvents));
        }
    },
    Prowess("prowess",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ProwessTrigger.create());
        }
    },
    Exploit("exploit", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(EntersBattlefieldTrigger.Exploit);
        }
    },
    Poisonous("poisonous " + ARG.NUMBER, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(DamageIsDealtTrigger.Poisonous(n));
        }
    },
    Tribute("tribute " + ARG.NUMBER + " effect " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(TributeTrigger.create(n, MagicRuleEventAction.create(ARG.effect(arg))));
        }
    },
    Bestow("bestow " + ARG.MANACOST, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicBestowActivation(manaCost));
        }
    },
    Dethrone("dethrone",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(DethroneTrigger.create());
        }
    },
    Madness("madness " + ARG.MANACOST,0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MadnessTrigger(MagicManaCost.create(ARG.manacost(arg))));
        }
    },
    Morph("morph( |—)" + ARG.COST, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.build(ARG.cost(arg));
            card.add(new MagicMorphActivation(matchedCostEvents));
            card.add(MagicMorphCastActivation.Morph);
        }
    },
    Myriad("myriad", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            //do nothing there is only one opponent
        }
    },
    Melee("melee", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(
                AttacksTrigger.create(
                    MagicTargetFilterFactory.SN,
                    MagicRuleEventAction.create("SN gets +1/+1 until end of turn.")
                )
            );
        }
    },
    Crew("crew " + ARG.NUMBER, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicCrewActivation(ARG.number(arg)));
        }
    },
    Afflict("afflict " + ARG.NUMBER, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(BecomesBlockedTrigger.create(
                MagicTargetFilterFactory.Permanent("SN"),
                MagicRuleEventAction.create("Defending player loses " + ARG.number(arg) + " life.")
            ));
        }
    },

    // abilities that involve SN
    ShockLand("As SN enters the battlefield, you may pay 2 life\\. If you don't, SN enters the battlefield tapped\\.", -10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(RavnicaLandTrigger.create());
        }
    },
    BlocksOrBlockedByEffect("Whenever SN blocks or becomes blocked by " + ARG.WORDRUN + ", " + ARG.EFFECT, 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(ARG.effect(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            card.add(ThisBlocksTrigger.create(filter, sourceEvent));
            card.add(ThisBecomesBlockedByTrigger.create(filter, sourceEvent));
        }
    },
    BecomesBlockedByEffect("Whenever SN becomes blocked by " + ARG.WORDRUN + ", " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ThisBecomesBlockedByTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EntersKickedEffect("When SN enters the battlefield, if it was kicked, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(EntersBattlefieldTrigger.createKicked(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EntersSurgeEffect("When SN enters the battlefield, if its surge cost was paid, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            EntersKickedEffect.addAbilityImpl(card, arg);
        }
    },
    EntersEffect("When SN enters the battlefield, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(EntersBattlefieldTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EntersDiesEffect("When SN enters the battlefield or dies, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(EntersBattlefieldTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
            card.add(ThisDiesTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EntersAttackEffect("Whenever SN enters the battlefield or attacks, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(EntersBattlefieldTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
            card.add(ThisAttacksTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    BattalionEffect("Whenever SN and at least two other creatures attack, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(BattalionTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    LeavesReturnExile("When SN leaves the battlefield, (each player returns|return) (the exiled card(s)? |all cards exiled with it )?to the battlefield (under (its|their) owner('s|s') control|all cards he or she owns exiled with SN).", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(LeavesReturnExiledTrigger.create());
        }
    },
    OpponentDiscardOntoBattlefield("If a spell or ability an opponent controls causes you to discard SN, put it onto the battlefield instead of putting it into your graveyard\\.",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ThisPutIntoGraveyardTrigger.OpponentDiscardOntoBattlefield);
        }
    },
    RecoverGraveyard("When SN is put into a graveyard from anywhere, its owner shuffles his or her graveyard into his or her library\\.",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ThisPutIntoGraveyardTrigger.RecoverGraveyard);
        }
    },
    GraveyardToLibrary("When SN is put into a graveyard from anywhere, shuffle it into its owner's library\\.",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(FromGraveyardIntoLibraryTrigger.create());
        }
    },
    LibraryInteadOfGraveyard("If SN would be put into a graveyard from anywhere, reveal SN and shuffle it into its owner's library instead\\.",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ThisPutIntoGraveyardTrigger.LibraryInsteadOfGraveyard);
        }
    },
    EntersChooseOpponent("As SN enters the battlefield, choose an opponent\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(EntersBattlefieldTrigger.ChooseOpponent);
        }
    },
    EntersChoosePlayer("As SN enters the battlefield, choose a player\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(EntersBattlefieldTrigger.ChoosePlayer);
        }
    },
    EntersTapped("SN enters the battlefield tapped\\.", -10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(EntersTappedTrigger.create());
        }
    },
    OtherEntersTapped(ARG.WORDRUN + " enter the battlefield tapped\\.", -10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            card.add(OtherEntersBattlefieldTrigger.Tapped(filter));
        }
    },
    EntersWithCounter("SN enters the battlefield with " + ARG.WORD1 + " " + ARG.WORD2 + " counter(s)? on it( for each " + ARG.WORDRUN + ")?\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String amount = ARG.word1(arg);
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(ARG.word2(arg));
            final String each = ARG.wordrun(arg);
            if ("a".equalsIgnoreCase(amount) && "time it was kicked".equalsIgnoreCase(each)) {
                card.add(EntersWithCounterTrigger.MultiKicker(counterType));
            } else if (amount.equalsIgnoreCase("X")) {
                card.add(EntersWithCounterTrigger.XCounters(counterType));
            } else {
                final int n = EnglishToInt.convert(amount);
                final MagicAmount count = MagicAmountParser.build(each);
                card.add(new EntersWithCounterTrigger(counterType,n,count));
            }
        }
    },
    EntersWithCounterCond("SN enters the battlefield with " + ARG.WORD1 + " " + ARG.WORD2 + " counter(s)? on it if " + ARG.COND + "\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = EnglishToInt.convert(ARG.word1(arg));
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(ARG.word2(arg));
            final MagicCondition condition = MagicConditionParser.build(ARG.cond(arg));
            card.add(new EntersWithCounterTrigger(counterType, n, condition));
        }
    },
    EntersTappedUnless("SN enters the battlefield tapped unless "+ARG.COND+"\\.", -10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCondition condition = MagicConditionParser.build(ARG.cond(arg));
            card.add(new EntersTappedUnlessTrigger(MagicConditionFactory.Unless(condition)));
        }
    },
    EntersAsCopy("You may have SN enter the battlefield as a copy of any " + ARG.WORDRUN + "\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicETBEvent.copyOf(ARG.wordrun(arg)));
        }
    },
    WhenMonstrous("When SN becomes monstrous, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(BecomesStateTrigger.createSelf(
                MagicPermanentState.Monstrous,
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenTransform("When(ever)? this creature transforms into SN, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(WhenTransformsTrigger.createSelf(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenRenowned("When SN becomes renowned, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(BecomesStateTrigger.createSelf(
                MagicPermanentState.Renowned,
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AlternateCost("alt cost " + ARG.ANY, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher matcher) {
            final String arg = matcher.group("any");
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            final String[] tokens = arg.split(" named ");
            card.add(MagicHandCastActivation.create(cardDef, tokens[0], tokens[1]));
        }
    },
    AlternateCost2("You may " + ARG.ANY + " rather than pay SN's mana cost\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher matcher) {
            final String arg = matcher.group("any").replace(" and ",", ");
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(MagicHandCastActivation.create(cardDef, arg, "Alt"));
        }
    },
    AlternateCostIf("If " + ARG.COND + ", you may " + ARG.ANY + " rather than pay SN's mana cost\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher matcher) {
            final MagicCondition condition = MagicConditionParser.build(ARG.cond(matcher));
            final String arg = matcher.group("any").replace(" and ",", ");
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(MagicHandCastActivation.create(cardDef, condition, arg, "Alt"));
        }
    },
    Awaken("awaken " + ARG.NUMBER + "—" + ARG.COST, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(MagicHandCastActivation.awaken(cardDef, ARG.number(arg), ARG.cost(arg)));
        }
    },
    CastRestriction("Cast SN " + ARG.ANY, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            for (final MagicMatchedCostEvent mce : MagicConditionParser.buildCost(ARG.any(arg))) {
                card.add(MagicAdditionalCost.create(mce));
            }
        }
    },
    AdditionalCost("As an additional cost to cast SN, " + ARG.COST, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAdditionalCost.create(new MagicRegularCostEvent(ARG.cost(arg))));
        }
    },
    HeroicEffect("Whenever you cast a spell that targets SN, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(HeroicTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CycleEffect("When you cycle SN, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ThisCycleTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    Kinship("At the beginning of your upkeep, you may look at the top card of your library. If it shares a creature type with SN, you may reveal it. If you do, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String effect = ARG.effect(arg);
            card.add(AtYourUpkeepTrigger.kinship(effect, MagicRuleEventAction.create(effect).getAction()));
        }
    },
    CDAPT("SN's power and toughness are each equal to( " + ARG.NUMBER + " plus)? " + ARG.ANY + "\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int base = (arg.group("number") != null) ? ARG.number(arg) : 0;
            card.add(MagicCDA.setPT(
                base,
                MagicAmountParser.build(ARG.any(arg))
            ));
        }
    },
    CDAPower("SN's power is equal to( " + ARG.NUMBER + " plus)? " + ARG.ANY + "\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int base = (arg.group("number") != null) ? ARG.number(arg) : 0;
            card.add(MagicCDA.setPower(
                base,
                MagicAmountParser.build(ARG.any(arg))
            ));
        }
    },
    CDAToughness("SN's toughness is equal to( " + ARG.NUMBER + " plus)? " + ARG.ANY + "\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int base = (arg.group("number") != null) ? ARG.number(arg) : 0;
            card.add(MagicCDA.setToughness(
                base,
                MagicAmountParser.build(ARG.any(arg))
            ));
        }
    },
    WhenExploit("When SN exploits a creature, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(BecomesStateTrigger.createSelf(
                MagicPermanentState.Exploit,
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    ChooseNotUntap("You may choose not to untap SN during your untap step\\.",0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.addAbility(DoesNotUntap);
            card.add(AtUntapTrigger.createYour(
                MagicRuleEventAction.create("If SN is tapped, you may untap SN.")
            ));
        }
    },
    PairedPump("As long as SN is paired with another creature, each of those creatures gets " + ARG.PT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int[] pt = ARG.pt(arg);
            card.add(MagicStatic.genPTStatic(pt[0], pt[1]));
        }
    },
    PairedGain("As long as SN is paired with another creature, (both creatures have|each of those creatures has) " + ARG.ANY, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicStatic.linkedABStatic(
                MagicAbility.getAbilityList(
                    ARG.any(arg)
                )
            ));
        }
    },

    // triggered abilities
    AnyAttacksEffect("When(ever)? " + ARG.WORDRUN + " attacks, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AttacksTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AnyAttacksAloneEffect("When(ever)? " + ARG.WORDRUN + " attacks alone, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AttacksTrigger.createAlone(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AnyAttacksYouEffect("When(ever)? " + ARG.WORDRUN + " attacks you( or a planeswalker you control)?, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AttacksTrigger.createYou(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AnyAttacksNotBlockedEffect("When(ever)? " + ARG.WORDRUN + " attacks and isn't blocked, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AttacksUnblockedTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    BlocksOrBlockedEffect("Whenever " + ARG.WORDRUN + " blocks or becomes blocked, " + ARG.EFFECT, 20) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(ARG.effect(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            card.add(BlocksTrigger.create(filter, sourceEvent));
            card.add(BecomesBlockedTrigger.create(filter, sourceEvent));
        }
    },
    BlocksCreatureEffect("When(ever)? SN blocks " + ARG.WORDRUN + ", " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ThisBlocksTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CreatureAttacksOrBlocksEffect("When(ever)? " + ARG.WORDRUN + " attacks or blocks, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(ARG.effect(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            card.add(AttacksTrigger.create(filter, sourceEvent));
            card.add(BlocksTrigger.create(filter, sourceEvent));
        }
    },
    ExertWhenAttack("You may exert SN as it attacks\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ThisAttacksTrigger.exert(null));
        }
    },
    ExertTrigger("You may exert SN as it attacks\\. When you do, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(ARG.effect(arg));
            card.add(ThisAttacksTrigger.exert(sourceEvent));
        }
    },
    WhenTrigger("Whenever you exert a creature, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(BecomesStateTrigger.create(
                MagicPermanentState.Exerted,
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CreatureBlocksEffect("When(ever)? " + ARG.WORDRUN + " blocks, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(ARG.effect(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            card.add(BlocksTrigger.create(filter, sourceEvent));
        }
    },
    BecomesBlockedEffect("When(ever)? " + ARG.WORDRUN + " becomes blocked, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(BecomesBlockedTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    UntappedEffect("When(ever)? " + ARG.WORDRUN + " becomes untapped, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(BecomesUntappedTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AnyTappedEffect("When(ever)? " + ARG.WORDRUN + " becomes tapped, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(BecomesTappedTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    Haunt("haunt", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            //Does nothing but allows text to be part of ability property
            //HauntAbility contains actual effects
            //Not currently compatable with Instants or Sorceries
        }
    },
    HauntAbility("When SN enters the battlefield or the creature it haunts dies, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(MagicAbilityStore card, Matcher arg) {
            MagicSourceEvent hauntEffect = MagicRuleEventAction.create(ARG.effect(arg));
            card.add(EntersBattlefieldTrigger.create(hauntEffect));
            card.add(ThisDiesTrigger.createHaunt(hauntEffect));
        }
    },
    HauntSpell("When the creature SN haunts dies, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(MagicAbilityStore card, Matcher arg) {
            //Does nothing Haunt for spells is determined from MagicSpellEventAction
        }
    },
    SelfOrAnotherYouControlEntersEffect("Whenever SN or another " + ARG.WORDRUN + " enters the battlefield under your control, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherEntersBattlefieldTrigger.createSelfOrAnother(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg) + " you control"),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OtherYouControlEntersEffect("When(ever)? " + ARG.WORDRUN + " enters the battlefield under your control, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherEntersBattlefieldTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg) + " you control"),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OtherOppControlEntersEffect("Whenever " + ARG.WORDRUN + " enters the battlefield under an opponent's control, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherEntersBattlefieldTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg) + " an opponent controls"),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SelfOrAnotherEntersEffect("Whenever SN or another " + ARG.WORDRUN + " enters the battlefield, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherEntersBattlefieldTrigger.createSelfOrAnother(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OtherEntersEffect("When(ever)? " + ARG.WORDRUN + " enters the battlefield, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherEntersBattlefieldTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SelfOrAnotherLeavesEffect("Whenever SN or another " + ARG.WORDRUN + " leaves the battlefield, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(LeavesBattlefieldTrigger.createSelfOrAnother(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    LeavesEffect("When(ever)? " + ARG.WORDRUN + " leaves the battlefield, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(LeavesBattlefieldTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    YouSacrificeEffect("When(ever)? you sacrifice " + ARG.WORDRUN + ", " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(SacrificeTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg) + " you control"),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    PlayerSacrificeEffect("When(ever)? a player sacrifices " + ARG.WORDRUN + ", " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(SacrificeTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    DamageToYou("When(ever)? " + ARG.WORDRUN + " deals damage to you, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(DamageIsDealtTrigger.DamageToYou(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg)),
                MagicDamage.Any
            ));
        }
    },
    CombatDamageToYou("When(ever)? " + ARG.WORDRUN + " deals combat damage to you, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(DamageIsDealtTrigger.DamageToYou(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg)),
                MagicDamage.Combat
            ));
        }
    },
    DamageToTarget("When(ever)? " + ARG.WORDRUN + " deals damage to " + ARG.WORDRUN2 + ", " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(DamageIsDealtTrigger.DamageToTarget(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicTargetFilterFactory.Target(ARG.wordrun2(arg)),
                MagicRuleEventAction.create(ARG.effect(arg)),
                MagicDamage.Any
            ));
        }
    },
    CombatDamageToTarget("When(ever)? " + ARG.WORDRUN + " deals combat damage to " + ARG.WORDRUN2 + ", " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(DamageIsDealtTrigger.DamageToTarget(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicTargetFilterFactory.Target(ARG.wordrun2(arg)),
                MagicRuleEventAction.create(ARG.effect(arg)),
                MagicDamage.Combat
            ));
        }
    },
    DamageToAny("When(ever)? " + ARG.WORDRUN + " deals damage, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(DamageIsDealtTrigger.DamageToAny(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg)),
                MagicDamage.Any
            ));
        }
    },
    CombatDamageToAny("When(ever)? " + ARG.WORDRUN + " deals combat damage, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(DamageIsDealtTrigger.DamageToAny(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg)),
                MagicDamage.Combat
            ));
        }
    },
    DealtDamage("When(ever)? " + ARG.WORDRUN + " is dealt damage, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(DamageIsDealtTrigger.DealtDamage(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg)),
                MagicDamage.Any
            ));
        }
    },
    SelfOrAnotherDiesEffect("Whenever SN or another " + ARG.WORDRUN + " (dies|is put into a graveyard from the battlefield), " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherDiesTrigger.createSelfOrAnother(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OtherDiesEffect("When(ever)? " + ARG.WORDRUN + " (dies|is put into a graveyard from the battlefield), " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherDiesTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EachUpkeepEffect("At the beginning of each (player's )?upkeep, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AtUpkeepTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    YourUpkeepEffect("At the beginning of your upkeep, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AtYourUpkeepTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OppUpkeepEffect("At the beginning of each opponent's upkeep, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AtUpkeepTrigger.createOpp(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EachDrawStepEffect("At the beginning of each player's draw step, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AtDrawTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    YourDrawStepEffect("At the beginning of your draw step, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AtDrawTrigger.createYour(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OppDrawStepEffect("At the beginning of each (other player|opponent)'s draw step, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AtDrawTrigger.createOpp(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    BeginCombatEffect("At the beginning of combat on your turn, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AtBeginOfCombatTrigger.createYour(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EndStepEffect("At the beginning of (the|each|each player's) end step, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AtEndOfTurnTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    YourEndStepEffect("At the beginning of your end step, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(AtEndOfTurnTrigger.createYour(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    PlayerCastSpellEffect("Whenever a player casts a(n)? (?<wordrun>[^\\.]*spell[^,]*), " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherSpellIsCastTrigger.create(
                MagicTargetFilterFactory.ItemOnStack(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    YouCastSN("When you cast SN, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ThisSpellIsCastTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    YouCastSpellEffect("When(ever)? you cast a(n)? (?<wordrun>[^\\.]*spell[^,]*), " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherSpellIsCastTrigger.createYou(
                MagicTargetFilterFactory.ItemOnStack(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OpponentCastSpellEffect("When(ever)? an opponent casts a(n)? (?<wordrun>[^\\.]*spell[^,]*), " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherSpellIsCastTrigger.createOpponent(
                MagicTargetFilterFactory.ItemOnStack(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SacWhenTargeted("When SN becomes the target of a spell or ability, sacrifice it\\.",-10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(ThisBecomesTargetTrigger.SacWhenTargeted);
        }
    },
    WhenSNTargeted("When(ever)? SN becomes the target of (?<wordrun>[^\\,]*), " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(BecomesTargetTrigger.createThis(
                MagicTargetFilterFactory.ItemOnStack(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenTargeted("When(ever)? " + ARG.WORDRUN2 + " becomes the target of (?<wordrun>[^\\,]*), " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(BecomesTargetTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun2(arg)),
                MagicTargetFilterFactory.ItemOnStack(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenGainLife("Whenever you gain life, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(LifeIsGainedTrigger.createYou(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenDrawCard("Whenever you draw a card, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherDrawnTrigger.createYou(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenOppDrawCard("Whenever an opponent draws a card, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherDrawnTrigger.createOpp(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenPlayerDrawCard("Whenever a player draws a card, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherDrawnTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenOtherFaceUpEffect("When(ever)? " + ARG.WORDRUN + " is turned face up, " + ARG.EFFECT,0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(TurnedFaceUpTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CycleOtherEffect("Whenever a player cycles a card, " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(OtherCycleTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenYouScry("Whenever you scry, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(YouScryTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WheneverYouCycleOrDiscard("Whenever you cycle or discard a(nother)? card, " + ARG.EFFECT, 0) {
      @Override
      protected  void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
          card.add(CardIsDiscardedTrigger.you(
              MagicRuleEventAction.create(ARG.effect(arg))
          ));
      }
    },
    WheneverPlayerDiscard("Whenever a player discards a card, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(CardIsDiscardedTrigger.player(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WheneverOpponentDiscard("Whenever an opponent discards a card, " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(CardIsDiscardedTrigger.opponent(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenConditionEffect("When " + ARG.COND + ", " + ARG.EFFECT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicStatic.StateTrigger(
                MagicConditionParser.build(ARG.cond(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    ControlEnchanted("You control enchanted " + ARG.ANY + "\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicStatic.ControlEnchanted);
        }
    },
    Enchant("Enchant " + ARG.WORDRUN, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            //Cannot implement target pickers
            //Does nothing but it allows text to be part of ability property
        }
    },

    // activated card abilities
    CardAbility(".*Discard SN:.*", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCardAbilityActivation.create(arg.group(), MagicLocationType.OwnersHand));
        }
    },
    RecoverSelf(".*: Return SN from your graveyard .*", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCardAbilityActivation.create(arg.group(), MagicLocationType.Graveyard));
        }
    },
    ExileCardSelf(".*Exile SN from your graveyard:.*", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCardAbilityActivation.create(arg.group(), MagicLocationType.Graveyard));
        }
    },

    // mana abilities
    SacAddMana("Sacrifice SN: Add " + ARG.MANA + " to your mana pool\\.",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(new MagicSacrificeManaActivation(manatype));
        }
    },
    TapSacAddMana("\\{T\\}, Sacrifice SN: Add " + ARG.MANA + " to your mana pool\\.",10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(new MagicSacrificeTapManaActivation(manatype));
        }
    },
    ManaActivation("(?<cost>[^\"]+): Add " + ARG.MANA + " to your mana pool\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(MagicManaActivation.create(ARG.cost(arg), manatype));
        }
    },
    ManaActivationEffect("(?<cost>[^\"]+): Add " + ARG.MANA + " to your mana pool\\. " + ARG.EFFECT, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(MagicManaActivation.create(ARG.cost(arg) + ", " + ARG.effect(arg), manatype));
        }
    },

    // planeswalker loyalty ability
    PlaneswalkerAbility("[+−]?[0-9]+: " + ARG.ANY, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicPlaneswalkerActivation.create(arg.group()));
        }
    },

    // activated permanent abilities
    ActivatedAbility("[^\"‘]+:(?! Add)" + ARG.ANY, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicPermanentActivation.create(arg.group()));
        }
    },

    // static ability
    PreventDamageDealtTo("prevent all damage that would be dealt to " + ARG.WORDRUN + "\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(PreventDamageTrigger.PreventDamageDealtTo(
                MagicTargetFilterFactory.Target(ARG.wordrun(arg))
            ));
        }
    },
    PreventCombatDamageDealtTo("prevent all combat damage that would be dealt to " + ARG.WORDRUN + "\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(PreventDamageTrigger.PreventCombatDamageDealtTo(
                MagicTargetFilterFactory.Target(ARG.wordrun(arg))
            ));
        }
    },
    PreventNonCombatDamageDealtTo("prevent all noncombat damage that would be dealt to " + ARG.WORDRUN + "\\.", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(PreventDamageTrigger.PreventNonCombatDamageDealtTo(
                MagicTargetFilterFactory.Target(ARG.wordrun(arg))
            ));
        }
    },
    ConditionPumpGainGroup("As long as " + ARG.WORDRUN + ", " + ARG.WORDRUN2 + " get " + ARG.PT + " and " + ARG.ANY + "\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int[] pt = ARG.pt(arg);
            final MagicCondition condition = MagicConditionParser.build(ARG.wordrun(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun2(arg));
            final MagicAbilityList abList = MagicAbility.getAbilityList(ARG.any(arg));
            card.add(MagicStatic.genPTStatic(condition, filter, pt[0], pt[1]));
            card.add(MagicStatic.genABStatic(condition, filter, abList));
        }
    },
    ConditionPumpGroup("As long as " + ARG.WORDRUN + ", " + ARG.WORDRUN2 + " get " + ARG.PT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCondition condition = MagicConditionParser.build(ARG.wordrun(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun2(arg));
            final int[] pt = ARG.pt(arg);
            card.add(MagicStatic.genPTStatic(condition, filter, pt[0], pt[1]));
        }
    },
    ConditionPumpGroupAlt(ARG.WORDRUN2 + " get(s)? " + ARG.PT + " as long as " + ARG.WORDRUN, 0){
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            ConditionPumpGroup.addAbilityImpl(card, arg);
        }
    },
    ConditionPumpGain(ARG.WORDRUN2 + " (?=(get|has|can))(gets " + ARG.PT + " )?(and )?(" + ARG.ANY + " )?as long as " + ARG.WORDRUN + "\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun2(arg));
            final MagicCondition condition = MagicConditionParser.build(ARG.wordrun(arg));
            if (arg.group("pt") != null) {
                final int[] pt = ARG.pt(arg);
                card.add(MagicStatic.genPTStatic(condition, filter, pt[0], pt[1]));
            }
            if (arg.group("any") != null) {
                card.add(MagicStatic.genABStatic(
                    condition,
                    filter,
                    MagicAbility.getAbilityList(
                        ARG.any(arg)
                    )
                ));
            }
        }
    },
    ConditionPumpGainAlt("As long as (?<wordrun>[^\\,]*), " + ARG.WORDRUN2 + " (?=(get|has|can))(gets " + ARG.PT + "(.| ))?(and )?(" + ARG.ANY + ")?", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            ConditionPumpGain.addAbilityImpl(card, arg);
        }
    },
    ConditionPumpGainUnless(ARG.WORDRUN + " (gets " + ARG.PT + " )?(and )?(" + ARG.ANY + " )?unless " + ARG.WORDRUN2 + "\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            final MagicCondition condition = MagicConditionFactory.Unless(MagicConditionParser.build(ARG.wordrun2(arg)));
            if (arg.group("pt") != null) {
                final int [] pt = ARG.pt(arg);
                card.add(MagicStatic.genPTStatic(condition, filter, pt[0], pt[1]));
            }
            final String abText = ARG.any(arg);
            if (abText != null) {
                card.add(MagicStatic.genABStatic(
                    condition,
                    filter,
                    MagicAbility.getAbilityList(abText)
                ));
            }
        }
    },
    ConditionGainGroup("As long as " + ARG.WORDRUN + ", " + ARG.WORDRUN2 + " (has|have) " + ARG.ANY, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCondition condition = MagicConditionParser.build(ARG.wordrun(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun2(arg));
            final MagicAbilityList abList = MagicAbility.getAbilityList(ARG.any(arg));
            card.add(MagicStatic.genABStatic(condition, filter, abList));
        }
    },
    ConditionGainGroupAlt(ARG.WORDRUN2 + " (?<any>(has|have|can).+) as long as " + ARG.WORDRUN, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            ConditionGainGroup.addAbilityImpl(card, arg);
        }
    },
    CantBlockPermanent("(SN )?can't block " + ARG.WORDRUN, 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(CantBlockTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg))
            ));
        }
    },
    LordPumpGain(ARG.WORDRUN + " get(s)? " + ARG.PT + "(,)? (and|has|and have) " + ARG.ANY, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int[] pt = ARG.pt(arg);
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            final MagicAbilityList abList = MagicAbility.getAbilityList(ARG.any(arg));
            card.add(MagicStatic.genPTStatic(filter, pt[0], pt[1]));
            card.add(MagicStatic.genABStatic(filter, abList));
        }
    },
    LordPump(ARG.WORDRUN + " get(s)? " + ARG.PT, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int[] pt = ARG.pt(arg);
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            card.add(MagicStatic.genPTStatic(filter, pt[0], pt[1]));
        }
    },
    LordPumpEach(ARG.WORDRUN + " get(s)? " + ARG.PT + " for each " + ARG.WORDRUN2 + "\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicPowerToughness pt = ARG.mpt(arg);
            final MagicTargetFilter<MagicPermanent> affected = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun2(arg));
            card.add(MagicStatic.genPTStatic(affected, count, pt));
        }
    },
    LordPumpX(ARG.WORDRUN + " get(s)? " + ARG.XPT + ", where X is " + ARG.WORDRUN2 + "\\.", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String[] ptStr = ARG.ptStr(arg);
            final MagicPowerToughness pt = new MagicPowerToughness(
                MagicAmountParser.getX(ptStr[0], 1),
                MagicAmountParser.getX(ptStr[1], 1)
            );
            final MagicTargetFilter<MagicPermanent> affected = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            final MagicAmount count = MagicAmountParser.build(ARG.wordrun2(arg));
            card.add(MagicStatic.genPTStatic(affected, count, pt));
        }
    },
    LordGain(ARG.WORDRUN + " (have|has) " + ARG.ANY, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            final MagicAbilityList abList = MagicAbility.getAbilityList(ARG.any(arg));
            card.add(MagicStatic.genABStatic(filter, abList));
        }
    },
    LordGainCan(ARG.WORDRUN + " (?<any>(can|can't|doesn't|don't untap|attack(s)?) .+)", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            final MagicAbilityList abList = MagicAbility.getAbilityList(ARG.any(arg));
            card.add(MagicStatic.genABGameStatic(filter, abList));
        }
    },
    Renown("renown " + ARG.NUMBER,10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(DamageIsDealtTrigger.Renown(n));
        }
    },
    Fabricate("fabricate " + ARG.NUMBER,10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(EntersBattlefieldTrigger.Fabricate(n));
        }
    },
    Partner("Partner", 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            //Does nothing as commander rules are not implemented
        }
    },
    Undaunted("Undaunted", 10) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(MagicHandCastActivation.reduction(cardDef, MagicAmountFactory.One));
        }
    },
    Suspend("suspend " + ARG.NUMBER + "( |—)" + ARG.MANACOST, 0) {
        @Override
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicSuspendActivation(n, manaCost));
        }
    },
    ;

    public static final Set<MagicAbility> PROTECTION_FLAGS = EnumSet.range(ProtectionFromBlack, ProtectionFromEverything);

    public static final Set<MagicAbility> LANDWALK_FLAGS = EnumSet.range(Plainswalk, NonbasicLandwalk);


    private final Pattern pattern;
    private final String name;
    private final int score;

    private MagicAbility(final String regex,final int aScore) {
        pattern = Pattern.compile(regex + "(\\.)?", Pattern.CASE_INSENSITIVE);
        name  = regex.replace("\\", "");
        score = aScore;
    }

    public String getName() {
        return name;
    }

    protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
    }

    public void addAbility(final MagicAbilityStore card, final String ability) {
        card.addAbility(this);
        addAbilityImpl(card, matched(ability));
    }

    public void addAbility(final MagicAbilityStore card) {
        card.addAbility(this);
        addAbilityImpl(card, null);
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

    public boolean matches(final String rule) {
        return pattern.matcher(rule).matches();
    }

    public Matcher matched(final String rule) {
        final Matcher matcher = pattern.matcher(rule);
        final boolean matches = matcher.matches();
        if (!matches) {
            throw new RuntimeException("unknown ability: \"" + rule + "\"");
        }
        return matcher;
    }

    private static String renameThis(final String text) {
        return text.replaceAll("\\b(T|t)his (creature|land|artifact|enchantment|permanent)( |\\.|'s|\\b)", "SN$3");
    }

    public static MagicAbility getAbility(final String name) {
        for (final MagicAbility ability : values()) {
            if (ability.matches(name)) {
                return ability;
            }
        }
        throw new ScriptParseException("unknown ability \"" + name + "\"");
    }

    public static MagicAbilityList getAbilityList(final String[] names) {
        final MagicAbilityList abilityList = new MagicAbilityList();
        for (final String name : names) {
            getAbility(name).addAbility(abilityList, name);
        }
        return abilityList;
    }

    public static MagicAbilityList getAbilityList(final Set<MagicAbility> abilities) {
        final MagicAbilityList abilityList = new MagicAbilityList();
        for (final MagicAbility ability : abilities) {
            ability.addAbility(abilityList);
        }
        return abilityList;
    }

    private static final Pattern SUB_ABILITY_LIST = Pattern.compile(
        "(?:has |have )?\"([^\"]+)\"(?:, and | and )?|" +
        "(?:has |have )?‘([^']+)'(?:, and | and )?|" +
        "(?:has |have )?([A-Za-z][^,]+)(?:, and | and )|" +
        "(?:has |have )?([A-Za-z][^,]+)"
    );

    public static MagicAbilityList getAbilityList(final String names) {
        final MagicAbilityList abilityList = new MagicAbilityList();
        final Matcher m = SUB_ABILITY_LIST.matcher(names);
        while (m.find()) {
            final String part = m.group(1) != null ? m.group(1) :
                                m.group(2) != null ? m.group(2) :
                                m.group(3) != null ? m.group(3) :
                                m.group(4);
            final String name = renameThis(part);
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
