package magic.model;

import magic.data.EnglishToInt;
import magic.model.event.*;
import magic.model.mstatic.MagicCDA;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.trigger.*;
import magic.model.target.MagicTarget;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.condition.MagicConditionParser;
import magic.exception.ScriptParseException;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MagicAbility {

    // intrinsic abilities (implemented directly in the game engine)
    AttacksEachTurnIfAble("(SN )?attack(s)? each (turn|combat) if able(\\.)?",-10),
    CannotBlock("(SN )?can't block(\\.)?",-50),
    CannotAttack("(SN )?can't attack(\\.)?",-50),
    CannotAttackOrBlock("(SN )?can't attack or block(\\.)?",-200),
    CannotBlockWithoutFlying("(SN )?can block only creatures with flying\\.",-40),
    CannotBeCountered("(SN )?can't be countered( by spells or abilities)?\\.",0),
    CannotBeTheTarget0("can't be the target of spells or abilities your opponents control",80),
    CannotBeTheTarget1("can't be the target of spells or abilities your opponents control",80),
    CannotBeTheTargetOfNonGreen("(SN )?can't be the target of nongreen spells or abilities from nongreen sources\\.",10),
    CannotBeTheTargetOfBlackOrRedOpponentSpell("(SN )?can't be the target of black or red spells your opponents control\\.",10),
    CanBlockShadow("(SN )?can block creatures with shadow as though (they didn't have shadow|SN had shadow)\\.",10),
    CanAttackWithDefender("can attack as though (it|they) didn't have defender", 10),
    Hexproof("hexproof(\\.)?",80),
    Deathtouch("deathtouch(\\.)?",60),
    Defender("defender(\\.)?",-100),
    DoesNotUntap("(SN )?(doesn't|don't) untap during (your|its controller's|their controllers') untap step(s)?(\\.)?",-30),
    DoubleStrike("double strike(\\.)?",100),
    Fear("fear(\\.)?",50),
    Flash("flash(\\.)?",0),
    Flying("flying(\\.)?",50),
    FirstStrike("first strike(\\.)?",50),
    Plainswalk("plainswalk(\\.)?",10),
    Islandwalk("islandwalk(\\.)?",10),
    Swampwalk("swampwalk(\\.)?",10),
    Mountainwalk("mountainwalk(\\.)?",10),
    Forestwalk("forestwalk(\\.)?",10),
    NonbasicLandwalk("nonbasic landwalk(\\.)?",10),
    LegendaryLandwalk("legendary landwalk(\\.)?",10),
    Indestructible("indestructible(\\.)?",150),
    Haste("haste(\\.)?",0),
    Lifelink("lifelink(\\.)?",40),
    Reach("reach(\\.)?",20),
    Shadow("shadow(\\.)?",30),
    Shroud("shroud(\\.)?",60),
    Trample("trample(\\.)?",30),
    Unblockable("(SN )?can't be blocked(\\.)?",100),
    Vigilance("vigilance(\\.)?",20),
    Wither("wither(\\.)?",30),
    TotemArmor("totem armor",0),
    Intimidate("intimidate(\\.)?",45),
    Infect("infect(\\.)?",35),
    Horsemanship("horsemanship(\\.)?",60),
    Soulbond("soulbond",30),
    SplitSecond("split second\\.",10),
    CantActivateAbilities("can't activate abilities(\\.)?|its activated abilities can't be activated(\\.)?",-20),
    ProtectionFromBlack("(protection )?from black(\\.)?",20),
    ProtectionFromBlue("(protection )?from blue(\\.)?",20),
    ProtectionFromGreen("(protection )?from green(\\.)?",20),
    ProtectionFromRed("(protection )?from red(\\.)?",20),
    ProtectionFromWhite("(protection )?from white(\\.)?",20),
    ProtectionFromMonoColored("protection from monocolored",50),
    ProtectionFromAllColors("protection from all colors(\\.)?",150),
    ProtectionFromColoredSpells("protection from colored spells",100),
    ProtectionFromEverything("protection from everything",200),

    // generalize intrinsic abilities (store a filter inside a trigger)
    ProtectionFromPermanent("protection from " + ARG.WORDRUN + "(\\.)?", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicProtectionTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg))
            ));
        }
    },
    CannotBeBlockedByPermanent("(SN )?can't be blocked by " + ARG.WORDRUN + "(\\.)?", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCannotBeBlockedTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg))
            ));
        }
    },
    CannotBeBlockedExceptByPermanent("(SN )?can't be blocked except by " + ARG.WORDRUN + "(\\.)?", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCannotBeBlockedTrigger.createExcept(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg))
            ));
        }
    },

    // keyword abilities
    Undying("undying",60) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicUndyingTrigger.create());
        }
    },
    Persist("persist",60) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicPersistTrigger.create());
        }
    },
    Modular("modular " + ARG.NUMBER, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.PlusOne,n));
            card.add(MagicModularTrigger.create());
        }
    },
    Flanking("flanking(\\.)?",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicFlankingTrigger.create());
        }
    },
    Changeling("changeling",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCDA.Changeling);
        }
    },
    Exalted("exalted",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicExaltedTrigger.create());
        }
    },
    BattleCry("battle cry",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicBattleCryTrigger.create());
        }
    },
    LivingWeapon("living weapon", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicLivingWeaponTrigger.create());
        }
    },
    Bushido("bushido " + ARG.NUMBER,20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new MagicBecomesBlockedPumpTrigger(n,n));
            card.add(new MagicWhenBlocksPumpTrigger(n,n));
        }
    },
    Soulshift("soulshift " + ARG.NUMBER,20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new MagicSoulshiftTrigger(n));
        }
    },
    Fading("fading " + ARG.NUMBER,-20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Fade,n));
            card.add(MagicFadeVanishCounterTrigger.Fade);
        }
    },
    Vanishing("vanishing " + ARG.NUMBER,-20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            if (n > 0) {
                card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Time,n));
            }
            card.add(MagicFadeVanishCounterTrigger.Time);
        }
    },
    CumulativeUpkeep("cumulative upkeep " + ARG.MANACOST,-30) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicCumulativeUpkeepTrigger(manaCost));
        }
    },
    LevelUp("level up " + ARG.MANACOST + " " + ARG.NUMBER, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost cost = MagicManaCost.create(ARG.manacost(arg));
            final int maxLevel = ARG.number(arg);
            card.add(new MagicLevelUpActivation(cost, maxLevel));
        }
    },
    Devour("devour " + ARG.NUMBER,10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new MagicDevourTrigger(n));
        }
    },
    Rampage("rampage " + ARG.NUMBER,20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new MagicRampageTrigger(n));
        }
    },
    Champion("champion " + ARG.ANY,-10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicChampionTrigger(ARG.any(arg)));
            card.add(MagicLeavesReturnExileTrigger.create());
        }
    },
    Echo("echo( |—)" + ARG.COST,-20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicEchoTrigger(new MagicRegularCostEvent(ARG.cost(arg))));
        }
    },
    Bloodthirst("bloodthirst " + ARG.NUMBER,10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new MagicBloodthirstTrigger(n));
        }
    },
    Storm("storm", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicStormTrigger.create());
        }
    },
    Annihilator("annihilator " + ARG.NUMBER + "(\\.)?", 80) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new MagicAnnihilatorTrigger(n));
        }
    },
    Miracle("miracle " + ARG.MANACOST, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicMiracleTrigger(manaCost));
        }
    },
    Kicker("kicker( |—)" + ARG.COST, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicKickerCost(new MagicRegularCostEvent(ARG.cost(arg))));
        }
    },
    Buyback("buyback( |—)" + ARG.COST, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicKickerCost.Buyback(new MagicRegularCostEvent(ARG.cost(arg))));
        }
    },
    Entwine("entwine( |—)" + ARG.COST, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicKickerCost.Entwine(new MagicRegularCostEvent(ARG.cost(arg))));
        }
    },
    Multikicker("multikicker " + ARG.MANACOST, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost cost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicMultikickerCost(cost));
        }
    },
    Replicate("replicate " + ARG.MANACOST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost cost = MagicManaCost.create(ARG.manacost(arg));
            card.add(MagicMultikickerCost.Replicate(cost));
            card.add(MagicReplicateTrigger.create());
        }
    },
    Evoke("evoke " + ARG.MANACOST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicEvokeActivation(manaCost));
            card.add(MagicWhenComesIntoPlayTrigger.Evoke);
        }
    },
    Transmute("transmute " + ARG.MANACOST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicTransmuteActivation(manaCost));
        }
    },
    Evolve("evolve", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherComesIntoPlayTrigger.Evolve);
        }
    },
    Extort("extort", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicExtortTrigger.create());
        }
    },
    Cycling("cycling( |—)" + ARG.COST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicCyclingActivation(new MagicRegularCostEvent(ARG.cost(arg))));
        }
    },
    TypeCycling(ARG.WORDRUN + "cycling( |—)" + ARG.COST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicMatchedCostEvent matchedCost = new MagicRegularCostEvent(ARG.cost(arg));
            final String type = ARG.wordrun(arg);
            card.add(new MagicTypeCyclingActivation(matchedCost,type));
        }
    },
    Reinforce("reinforce " + ARG.NUMBER + "—" + ARG.MANACOST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicReinforceActivation(n, manaCost));
        }
    },
    Unleash("unleash", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicUnleashTrigger.create());
            card.add(MagicStatic.Unleash);
        }
    },
    Ninjutsu("ninjutsu " + ARG.MANACOST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicNinjutsuActivation(manaCost));
        }
    },
    Dash("dash " + ARG.MANACOST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicDashActivation(manaCost));
        }
    },
    Cascade("cascade", 50) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCascadeTrigger.create());
        }
    },
    Graft("graft " + ARG.NUMBER,10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.PlusOne,n));
            card.add(MagicWhenOtherComesIntoPlayTrigger.Graft);
        }
    },
    Loyalty("loyalty " + ARG.NUMBER,10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Loyalty,n));
        }
    },
    Retrace("retrace",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(new MagicRetraceActivation(cardDef));
        }
    },
    Flashback("flashback( |—)" + ARG.COST,10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.build(ARG.cost(arg));
            card.add(new MagicFlashbackActivation(cardDef, matchedCostEvents));
        }
    },
    Scavenge("scavenge " + ARG.MANACOST,10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(new MagicScavengeActivation(cardDef, manaCost));
        }
    },
    Unearth("unearth " + ARG.MANACOST,10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicUnearthActivation(manaCost));
        }
    },
    Equip("Equip( |—)" + ARG.COST, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.build(ARG.cost(arg));
            card.add(new MagicEquipActivation(matchedCostEvents));
        }
    },
    Megamorph("megamorph " + ARG.COST, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.build(ARG.cost(arg));
            card.add(new MagicMegamorphActivation(matchedCostEvents));
            card.add(MagicMorphCastActivation.Megamorph);
        }
    },
    Affinity("affinity for " + ARG.WORDRUN + "(\\.)?", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(MagicCardActivation.affinity(cardDef, MagicTargetFilterFactory.Permanent(ARG.wordrun(arg))));
        }
    },
    Outlast("outlast "+ARG.COST,10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.build(ARG.cost(arg));
            card.add(new MagicOutlastActivation(matchedCostEvents));
        }
    },
    Prowess("prowess",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicProwessTrigger.create());
        }
    },
    Exploit("exploit", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenComesIntoPlayTrigger.Exploit);
        }
    },
    Poisonous("poisonous " + ARG.NUMBER + "(\\.)?", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(MagicWhenDamageIsDealtTrigger.Poisonous(n));
        }
    },
    Tribute("tribute " + ARG.NUMBER + " " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            final String effect  = ARG.effect(arg).replaceFirst("^effect ", "");
            card.add(MagicTributeTrigger.create(n,  MagicRuleEventAction.create(effect)));
        }
    },
    Bestow("bestow " + ARG.MANACOST, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.manacost(arg));
            card.add(new MagicBestowActivation(manaCost));
        }
    },
    Dethrone("dethrone(\\.)?",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicDethroneTrigger.create());
        }
    },
    Madness("madness " + ARG.MANACOST,0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicMadnessTrigger(MagicManaCost.create(ARG.manacost(arg))));
        }
    },
    Morph("morph( |—)" + ARG.COST, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.build(ARG.cost(arg));
            card.add(new MagicMorphActivation(matchedCostEvents));
            card.add(MagicMorphCastActivation.Morph);
        }
    },
    
    // abilities that involve SN
    ShockLand("As SN enters the battlefield, you may pay 2 life\\. If you don't, SN enters the battlefield tapped\\.", -10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicRavnicaLandTrigger.create());
        }
    },
    BlocksOrBlockedByEffect("Whenever SN blocks or becomes blocked by " + ARG.WORDRUN + ", " + ARG.EFFECT, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(ARG.effect(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            card.add(MagicWhenSelfBlocksTrigger.create(filter, sourceEvent));
            card.add(MagicWhenSelfBecomesBlockedByTrigger.create(filter, sourceEvent));
        }
    },
    BecomesBlockedByEffect("Whenever SN becomes blocked by " + ARG.WORDRUN + ", " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenSelfBecomesBlockedByTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EntersKickedEffect("When SN enters the battlefield, if it was kicked, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenComesIntoPlayTrigger.createKicked(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EntersEffect("When SN enters the battlefield, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenComesIntoPlayTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EntersAttackEffect("Whenever SN enters the battlefield or attacks, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenComesIntoPlayTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
            card.add(MagicWhenAttacksTrigger.create(
                MagicTargetFilterFactory.SN,
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    BattalionEffect("Whenever SN and at least two other creatures attack, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicBattalionTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    LeavesReturnExile("When SN leaves the battlefield, (each player returns|return) (the exiled card(s)? |all cards exiled with it )?to the battlefield (under (its|their) owner('s|s') control|all cards he or she owns exiled with SN).", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicLeavesReturnExileTrigger.create());
        }
    },
    OpponentDiscardOntoBattlefield("If a spell or ability an opponent controls causes you to discard SN, put it onto the battlefield instead of putting it into your graveyard\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenPutIntoGraveyardTrigger.OpponentDiscardOntoBattlefield);
        }
    },
    RecoverGraveyard("When SN is put into a graveyard from anywhere, its owner shuffles his or her graveyard into his or her library\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenPutIntoGraveyardTrigger.RecoverGraveyard);
        }
    },
    GraveyardToLibrary("When SN is put into a graveyard from anywhere, shuffle it into its owner's library\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicFromGraveyardToLibraryTrigger.create());
        }
    },
    LibraryInteadOfGraveyard("If SN would be put into a graveyard from anywhere, reveal SN and shuffle it into its owner's library instead\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenPutIntoGraveyardTrigger.LibraryInsteadOfGraveyard);
        }
    },
    EntersChooseOpponent("As SN enters the battlefield, choose an opponent\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenComesIntoPlayTrigger.ChooseOpponent);
        }
    },
    EntersTapped("SN enters the battlefield tapped\\.", -10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicTappedIntoPlayTrigger.create());
        }
    },
    EntersWithCounter("SN enters the battlefield with " + ARG.WORD1 + " " + ARG.WORD2 + " counter(s)? on it\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String name = ARG.word2(arg);
            final String amount = ARG.word1(arg);
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(name);
            if (amount.equalsIgnoreCase("X")) {
                card.add(MagicComesIntoPlayWithCounterTrigger.XCounters(counterType));
            } else {
                final int n = EnglishToInt.convert(amount);
                card.add(new MagicComesIntoPlayWithCounterTrigger(counterType,n));
            }
        }
    },
    EntersWithCounterMultiKick("SN enters the battlefield with a " + ARG.WORDRUN + " counter on it for each time it was kicked\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String name = ARG.wordrun(arg);
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(name);
            card.add(MagicComesIntoPlayWithCounterTrigger.MultiKicker(counterType));
        }
    },
    EntersTappedUnlessTwo("SN enters the battlefield tapped unless you control two or fewer other lands\\.", -10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicTappedIntoPlayUnlessTwoTrigger.create());
        }
    },
    EntersTappedUnless("SN enters the battlefield tapped unless you control a(n)? " + ARG.WORD1 + " or a(n)? " + ARG.WORD2 + "\\.", -10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicSubType t1 = MagicSubType.getSubType(ARG.word1(arg));
            final MagicSubType t2 = MagicSubType.getSubType(ARG.word2(arg));
            card.add(new MagicTappedIntoPlayUnlessTrigger(t1,t2));
        }
    },
    WhenMonstrous("When SN becomes monstrous, " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenBecomesStateTrigger.createSelf(
                MagicPermanentState.Monstrous,
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AlternateCost("alt cost " + ARG.ANY, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher matcher) {
            final String arg = matcher.group("any");
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            final String[] tokens = arg.split(" named ");
            card.add(MagicCardActivation.create(cardDef, tokens[0], tokens[1]));
        }
    },
    AlternateCost2("You may " + ARG.ANY + " rather than pay SN's mana cost\\.", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher matcher) {
            final String arg = matcher.group("any").replace(" and ",", ");
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(MagicCardActivation.create(cardDef, arg, "Alt"));
        }
    },
    CastRestriction("Cast SN " + ARG.ANY, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCondition[] conds = MagicConditionParser.buildCast(ARG.any(arg));  
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(MagicCardActivation.castOnly(cardDef, conds));
        }
    },
    AdditionalCost("As an additional cost to cast SN, " + ARG.COST, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAdditionalCost.create(new MagicRegularCostEvent(ARG.cost(arg))));
        }
    },
    HeroicEffect("Whenever you cast a spell that targets SN, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicHeroicTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CycleEffect("When you cycle SN, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenCycleTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    Kinship("At the beginning of your upkeep, you may look at the top card of your library. If it shares a creature type with SN, you may reveal it. If you do, " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String effect = ARG.effect(arg);
            card.add(MagicAtYourUpkeepTrigger.kinship(effect, MagicRuleEventAction.create(effect).getAction()));
        }
    },
    CDAPT("SN's power and toughness are each equal to (" + ARG.NUMBER + " plus )?the number of " + ARG.ANY + "\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int base = (arg.group("number") != null) ? ARG.number(arg) : 0;
            card.add(MagicCDA.setPT(
                base,
                MagicTargetFilterFactory.Target(ARG.any(arg))
            ));
        }
    },
    CDAPower("SN's power is equal to (" + ARG.NUMBER + " plus )?the number of " + ARG.ANY + "\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int base = (arg.group("number") != null) ? ARG.number(arg) : 0;
            card.add(MagicCDA.setPower(
                base,
                MagicTargetFilterFactory.Target(ARG.any(arg))
            ));
        }
    },
    CDAToughness("SN's toughness is equal to (" + ARG.NUMBER + " plus )?the number of " + ARG.ANY + "\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int base = (arg.group("number") != null) ? ARG.number(arg) : 0;
            card.add(MagicCDA.setToughness(
                base,
                MagicTargetFilterFactory.Target(ARG.any(arg))
            ));
        }
    },
    WhenExploit("When SN exploits a creature, " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenBecomesStateTrigger.createSelf(
                MagicPermanentState.Exploit,
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    ChooseNotUntap("You may choose not to untap SN during your untap step\\.",0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.addAbility(DoesNotUntap);
            card.add(MagicAtUntapTrigger.createYour(
                MagicRuleEventAction.create("If SN is tapped, you may untap SN.")
            ));
        }
    },
    PairedPump("As long as SN is paired with another creature, each of those creatures gets " + ARG.PT + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int[] pt = ARG.pt(arg);
            card.add(MagicStatic.genPTStatic(pt[0], pt[1]));
        }
    },
    PairedGain("As long as SN is paired with another creature, (both creatures have|each of those creatures has) " + ARG.ANY + "(\\.)?", 0) {
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
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenAttacksTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AnyAttacksYouEffect("When(ever)? " + ARG.WORDRUN + " attacks you( or a planeswalker you control)?, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenAttacksTrigger.createYou(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    BlocksOrBlockedEffect("Whenever " + ARG.WORDRUN + " blocks or becomes blocked, " + ARG.EFFECT, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(ARG.effect(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            card.add(MagicWhenBlocksTrigger.create(filter, sourceEvent));
            card.add(MagicWhenBecomesBlockedTrigger.create(filter, sourceEvent));
        }
    },
    BlocksCreatureEffect("When(ever)? SN blocks " + ARG.WORDRUN + ", " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenSelfBlocksTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CreatureAttacksOrBlocksEffect("When(ever)? " + ARG.WORDRUN + " attacks or blocks, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(ARG.effect(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            card.add(MagicWhenAttacksTrigger.create(filter, sourceEvent));
            card.add(MagicWhenBlocksTrigger.create(filter, sourceEvent));
        }
    },
    CreatureBlocksEffect("When(ever)? " + ARG.WORDRUN + " blocks, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(ARG.effect(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            card.add(MagicWhenBlocksTrigger.create(filter, sourceEvent));
        }
    },
    BecomesBlockedEffect("Whenever " + ARG.WORDRUN + " becomes blocked, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenBecomesBlockedTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    UntappedEffect("When(ever)? " + ARG.WORDRUN + " becomes untapped, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenBecomesUntappedTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AnyTappedEffect("When(ever)? " + ARG.WORDRUN + " becomes tapped, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenBecomesTappedTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SelfOrAnotherYouControlEntersEffect("Whenever SN or another " + ARG.WORDRUN + " enters the battlefield under your control, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherComesIntoPlayTrigger.createSelfOrAnother(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg) + " you control"),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OtherYouControlEntersEffect("Whenever " + ARG.WORDRUN + " enters the battlefield under your control, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherComesIntoPlayTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg) + " you control"),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SelfOrAnotherEntersEffect("Whenever SN or another " + ARG.WORDRUN + " enters the battlefield, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherComesIntoPlayTrigger.createSelfOrAnother(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OtherEntersEffect("When(ever)? " + ARG.WORDRUN + " enters the battlefield, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherComesIntoPlayTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SelfOrAnotherLeavesEffect("Whenever SN or another " + ARG.WORDRUN + " leaves the battlefield, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenLeavesPlayTrigger.createSelfOrAnother(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    LeavesEffect("When(ever)? " + ARG.WORDRUN + " leaves the battlefield, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenLeavesPlayTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    DamageCreature("Whenever " + ARG.WORDRUN + " deals damage to a creature, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenDamageIsDealtTrigger.DamageToCreature(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CombatDamageCreature("Whenever " + ARG.WORDRUN + " deals combat damage to a creature, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenDamageIsDealtTrigger.CombatDamageToCreature(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    DamageToYou("When(ever)? " + ARG.WORDRUN + " deals damage to you, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenDamageIsDealtTrigger.DamageToYou(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    DamageToOpponent("When(ever)? " + ARG.WORDRUN + " deals damage to an opponent, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenDamageIsDealtTrigger.DamageToOpponent(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CombatDamageToOpponent("When(ever)? " + ARG.WORDRUN + " deals combat damage to an opponent, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenDamageIsDealtTrigger.CombatDamageToOpponent(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    DamageToPlayer("When(ever)? " + ARG.WORDRUN + " deals damage to a player, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenDamageIsDealtTrigger.DamageToPlayer(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CombatDamageToPlayer("When(ever)? " + ARG.WORDRUN + " deals combat damage to a player, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenDamageIsDealtTrigger.CombatDamageToPlayer(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CombatDamageToAny("When(ever)? " + ARG.WORDRUN + " deals combat damage, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenDamageIsDealtTrigger.CombatDamageToAny(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SelfOrAnotherDiesEffect("Whenever SN or another " + ARG.WORDRUN + " (dies|is put into a graveyard from the battlefield), " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherDiesTrigger.createSelfOrAnother(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OtherDiesEffect("When(ever)? " + ARG.WORDRUN + " (dies|is put into a graveyard from the battlefield), " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherDiesTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EachUpkeepEffect("At the beginning of each (player's )?upkeep, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtUpkeepTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    YourUpkeepEffect("At the beginning of your upkeep, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtYourUpkeepTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EachDrawStepEffect("At the beginning of each player's draw step, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtDrawTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    YourDrawStepEffect("At the beginning of your draw step, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtDrawTrigger.createYour(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OppDrawStepEffect("At the beginning of each (other player|opponent)'s draw step, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtDrawTrigger.createOpp(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    BeginCombatEffect("At the beginning of combat on your turn, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtBeginOfCombatTrigger.createYour(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EndStepEffect("At the beginning of (the|each) end step, " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtEndOfTurnTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    YourEndStepEffect("At the beginning of your end step, " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtEndOfTurnTrigger.createYour(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    PlayerCastSpellEffect("Whenever a player casts a(n)? (?<wordrun>[^\\.]*spell[^,]*), " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherSpellIsCastTrigger.create(
                MagicTargetFilterFactory.ItemOnStack(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    YouCastSpellEffect("When(ever)? you cast a(n)? (?<wordrun>[^\\.]*spell[^,]*), " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherSpellIsCastTrigger.createYou(
                MagicTargetFilterFactory.ItemOnStack(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OpponentCastSpellEffect("When(ever)? an opponent casts a(n)? (?<wordrun>[^\\.]*spell[^,]*), " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherSpellIsCastTrigger.createOpponent(
                MagicTargetFilterFactory.ItemOnStack(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SacWhenTargeted("When SN becomes the target of a spell or ability, sacrifice it\\.",-10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenSelfTargetedTrigger.SacWhenTargeted);
        }
    },
    WhenTargeted("When(ever)? " + ARG.WORDRUN2 + " becomes the target of (?<wordrun>[^\\,]*), " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenTargetedTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun2(arg)),
                MagicTargetFilterFactory.ItemOnStack(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenGainLife("Whenever you gain life, " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenLifeIsGainedTrigger.createYou(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenDrawCard("Whenever you draw a card, " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherDrawnTrigger.createYou(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenOppDrawCard("Whenever an opponent draws a card, " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherDrawnTrigger.createOpp(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenPlayerDrawCard("Whenever a player draws a card, " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherDrawnTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenOtherFaceUpEffect("When(ever)? " + ARG.WORDRUN + " is turned face up, " + ARG.EFFECT,0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenTurnedFaceUpTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CycleOtherEffect("Whenever a player cycles a card, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherCycleTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenYouScry("Whenever you scry, " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenYouScryTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenConditionEffect("When " + ARG.COND + ", " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicStatic.StateTrigger(
                MagicConditionParser.build(ARG.cond(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    ControlEnchanted("You control enchanted " + ARG.ANY + "\\.", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicStatic.ControlEnchanted);
        }
    },

/*  
    EnchantDual("Enchant " + ARG.WORD1 + " or "+ ARG.WORD2, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicPlayAuraEvent.create("default," + ARG.word1(arg) + " or " + ARG.word2(arg)));
        }
    },
    Enchant("Enchant " + ARG.WORDRUN, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicPlayAuraEvent.create("default," + ARG.wordrun(arg)));
        }
    },
    //Cannot implement target pickers
*/

    // activated card abilities
    CardAbility(".*Discard SN:.*", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCardAbilityActivation.create(arg.group(), MagicLocationType.OwnersHand));
        }
    },
    RecoverSelf(".*: Return SN from your graveyard .*", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCardAbilityActivation.create(arg.group(), MagicLocationType.Graveyard));
        }
    },
    ExileCardSelf(".*Exile SN from your graveyard:.*", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCardAbilityActivation.create(arg.group(), MagicLocationType.Graveyard));
        }
    },

    // mana abilities
    SacAddMana("Sacrifice SN: Add " + ARG.MANA + " to your mana pool\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(new MagicSacrificeManaActivation(manatype));
        }
    },
    TapSacAddMana("\\{T\\}, Sacrifice SN: Add " + ARG.MANA + " to your mana pool\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(new MagicSacrificeTapManaActivation(manatype));
        }
    },
    ManaActivation("(?<cost>[^\"]+): Add " + ARG.MANA + " to your mana pool\\.", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(MagicManaActivation.create(ARG.cost(arg), manatype));
        }
    },
    ManaActivationEffect("(?<cost>[^\"]+): Add " + ARG.MANA + " to your mana pool\\. " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(MagicManaActivation.create(ARG.cost(arg) + ", " + ARG.effect(arg), manatype));
        }
    },

    // activated permanent abilities
    ActivatedAbility("[^\"]+:(?! Add)" + ARG.ANY, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicPermanentActivation.create(arg.group()));
        }
    },

    // static ability
    PreventDamageDealtTo("prevent all damage that would be dealt to " + ARG.WORDRUN + "\\.", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicPreventDamageTrigger.PreventDamageDealtTo(
                MagicTargetFilterFactory.Target(ARG.wordrun(arg))
            ));
        }
    },
    PreventCombatDamageDealtTo("prevent all combat damage that would be dealt to " + ARG.WORDRUN + "\\.", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicPreventDamageTrigger.PreventCombatDamageDealtTo(
                MagicTargetFilterFactory.Target(ARG.wordrun(arg))
            ));
        }
    },
    PreventNonCombatDamageDealtTo("prevent all noncombat damage that would be dealt to " + ARG.WORDRUN + "\\.", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicPreventDamageTrigger.PreventNonCombatDamageDealtTo(
                MagicTargetFilterFactory.Target(ARG.wordrun(arg))
            ));
        }
    },
    ConditionPumpGainGroup("As long as " + ARG.WORDRUN + ", " + ARG.WORDRUN2 + " get " + ARG.PT + " and " + ARG.ANY + "\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int[] pt = ARG.pt(arg);
            final MagicCondition condition = MagicConditionParser.build(ARG.wordrun(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun2(arg));
            final MagicAbilityList abList = MagicAbility.getAbilityList(ARG.any(arg));
            card.add(MagicStatic.genPTStatic(condition, filter, pt[0], pt[1]));
            card.add(MagicStatic.genABStatic(condition, filter, abList));
        }
    },
    ConditionPumpGroup("As long as " + ARG.WORDRUN + ", " + ARG.WORDRUN2 + " get " + ARG.PT + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCondition condition = MagicConditionParser.build(ARG.wordrun(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun2(arg));
            final int[] pt = ARG.pt(arg);
            card.add(MagicStatic.genPTStatic(condition, filter, pt[0], pt[1]));
        }
    },
    ConditionPumpGroupAlt(ARG.WORDRUN2 + " get(s)? " + ARG.PT + " as long as " + ARG.WORDRUN + "(\\.)?", 0){
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            ConditionPumpGroup.addAbilityImpl(card, arg);
        }
    },
    ConditionGainGroup("As long as " + ARG.WORDRUN + ", " + ARG.WORDRUN2 + " have " + ARG.ANY + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCondition condition = MagicConditionParser.build(ARG.wordrun(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun2(arg));
            final MagicAbilityList abList = MagicAbility.getAbilityList(ARG.any(arg));
            card.add(MagicStatic.genABStatic(condition, filter, abList));
        }
    },
    ConditionGainGroupAlt(ARG.WORDRUN2 + " (?<any>(has|have|can).+) as long as " + ARG.WORDRUN + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            ConditionGainGroup.addAbilityImpl(card, arg);
        }
    },
    ConditionPumpGain(ARG.WORDRUN2 + " (gets " + ARG.PT + " )?(and )?(" + ARG.ANY + " )?as long as " + ARG.WORDRUN + "\\.", 0) {
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
    ConditionPumpGainAlt("As long as (?<wordrun>[^\\,]*), " + ARG.WORDRUN2 + " (gets " + ARG.PT + "(.| ))?(and )?(" + ARG.ANY + ")?(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            ConditionPumpGain.addAbilityImpl(card, arg);
        }
    },
    ConditionPumpGainUnless(ARG.WORDRUN + " (gets " + ARG.PT + " )?(and )?(" + ARG.ANY + " )?unless " + ARG.WORDRUN2 + "\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            final MagicCondition condition = MagicConditionFactory.Unless(MagicConditionParser.build(ARG.wordrun2(arg)));
            if (arg.group("pt") != null) {
                final int [] pt = ARG.pt(arg);
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
    CantBlockPermanent("(SN )?can't block " + ARG.WORDRUN + "(\\.)?", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCantBlockTrigger.create(
                MagicTargetFilterFactory.Permanent(ARG.wordrun(arg))
            ));
        }
    },
    LordPumpGain(ARG.WORDRUN + " get(s)? " + ARG.PT + "(,)? (and|has) " + ARG.ANY + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int[] pt = ARG.pt(arg);
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            final MagicAbilityList abList = MagicAbility.getAbilityList(ARG.any(arg));
            card.add(MagicStatic.genPTStatic(filter, pt[0], pt[1]));
            card.add(MagicStatic.genABStatic(filter, abList));
        }
    },
    LordPump(ARG.WORDRUN + " get(s)? " + ARG.PT + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int[] pt = ARG.pt(arg);
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            card.add(MagicStatic.genPTStatic(filter, pt[0], pt[1]));
        }
    },
    LordPumpEach(ARG.WORDRUN + " get(s)? " + ARG.PT + " for each " + ARG.WORDRUN2 + "\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicPowerToughness pt = ARG.mpt(arg);
            final MagicTargetFilter<MagicPermanent> affected = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            final MagicTargetFilter<MagicTarget> counted = MagicTargetFilterFactory.Target(ARG.wordrun2(arg));
            card.add(MagicStatic.genPTStatic(affected, counted, pt));
        }
    },
    LordGain(ARG.WORDRUN + " (have|has) " + ARG.ANY + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            final MagicAbilityList abList = MagicAbility.getAbilityList(ARG.any(arg));
            card.add(MagicStatic.genABStatic(filter, abList));
        }
    },
    LordGainCan(ARG.WORDRUN + " (?<any>(can|can't|doesn't|attack(s)?) .+)(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.Permanent(ARG.wordrun(arg));
            final MagicAbilityList abList = MagicAbility.getAbilityList(ARG.any(arg));
            card.add(MagicStatic.genABGameStatic(filter, abList));
        }
    },
    ;

    public static final Set<MagicAbility> PROTECTION_FLAGS = EnumSet.range(ProtectionFromBlack, ProtectionFromEverything);
    
    public static final Set<MagicAbility> LANDWALK_FLAGS = EnumSet.range(Plainswalk, NonbasicLandwalk);


    private final Pattern pattern;
    private final String name;
    private final int score;

    private MagicAbility(final String regex,final int aScore) {
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
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
        "(?:has |have )?([A-Za-z][^,]+)(?:, and | and )|" + 
        "(?:has |have )?([A-Za-z][^,]+)"
    );
    
    public static MagicAbilityList getAbilityList(final String names) {
        final MagicAbilityList abilityList = new MagicAbilityList();
        final Matcher m = SUB_ABILITY_LIST.matcher(names);
        while (m.find()) {
            final String name = m.group(1) != null ? m.group(1) : 
                                m.group(2) != null ? m.group(2) :
                                m.group(3);
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
