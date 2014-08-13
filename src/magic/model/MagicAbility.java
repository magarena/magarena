package magic.model;

import magic.data.EnglishToInt;
import magic.model.event.*;
import magic.model.mstatic.MagicCDA;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.trigger.*;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionParser;
import magic.model.trigger.MagicThiefTrigger.Player;
import magic.model.trigger.MagicThiefTrigger.Type;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MagicAbility {
  
    AttacksEachTurnIfAble("(SN )?attacks each turn if able\\.",-10),
    CannotBlock("(SN )?can't block(\\.)?",-50),
    CannotAttack("(SN )?can't attack(\\.)?",-50),
    CannotAttackOrBlock("(SN )?can't attack or block(\\.)?",-200),
    CannotBlockWithoutFlying("(SN )?can block only creatures with flying\\.",-40),
    CannotBeBlockedByFlying("(SN )?can't be blocked by creatures with flying\\.",20),
    CannotBeBlockedExceptWithFlying("(SN )?can't be blocked except by creatures with flying\\.",30),
    CannotBeBlockedExceptWithFlyingOrReach("(SN )?can't be blocked except by creatures with flying or reach\\.",25),
    CannotBeBlockedExceptBySliver("(SN )?can't be blocked except by Slivers\\.",90),
    CannotBeBlockedExceptByWalls("(SN )?can't be blocked except by Walls\\.",80),
    CannotBeBlockedByWalls("(SN )?can't be blocked by Walls\\.",10),
    CannotBeBlockedByHumans("(SN )?can't be blocked by Humans\\.",10),
    CannotBeBlockedByBlack("(SN )?can't be blocked by black creatures\\.",10),
    CannotBeBlockedByBlue("(SN )?can't be blocked by blue creatures\\.",10),
    CannotBeBlockedByGreen("(SN )?can't be blocked by green creatures\\.",10),
    CannotBeBlockedByRed("(SN )?can't be blocked by red creatures\\.",10),
    CannotBeBlockedByWhite("(SN )?can't be blocked by white creatures\\.",10),
    CannotBeBlockedByTokens("(SN )?can't be blocked by creature tokens\\.",10),
    CanBlockShadow("(SN )?can block creatures with shadow as though (they didn't have shadow|SN had shadow)\\.",10),
    CannotBeCountered("(SN )?can't be countered( by spells or abilities)?\\.",0),
    Hexproof("hexproof(\\.)?",80),
    CannotBeTheTarget0("can't be the target of spells or abilities your opponents control",80),
    CannotBeTheTarget1("can't be the target of spells or abilities your opponents control",80),
    CannotBeTheTargetOfNonGreen("(SN )?can't be the target of nongreen spells or abilities from nongreen sources\\.",10),
    CannotBeTheTargetOfBlackOrRedOpponentSpell("(SN )?can't be the target of black or red spells your opponents control\\.",10),
    Deathtouch("deathtouch(\\.)?",60),
    Defender("defender(\\.)?",-100),
    DoesNotUntap("(SN )?doesn't untap during (your|its controller's) untap step(\\.)?",-30),
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
    ProtectionFromBlack("(protection )?from black(\\.)?",20),
    ProtectionFromBlue("(protection )?from blue(\\.)?",20),
    ProtectionFromGreen("(protection )?from green(\\.)?",20),
    ProtectionFromRed("(protection )?from red(\\.)?",20),
    ProtectionFromWhite("(protection )?from white(\\.)?",20),
    ProtectionFromMonoColored("protection from monocolored",50),
    ProtectionFromAllColors("protection from all colors(\\.)?",150),
    ProtectionFromCreatures("protection from creatures(\\.)?",100),
    ProtectionFromArtifacts("protection from artifacts",50),
    ProtectionFromBeasts("protection from beasts",10),
    ProtectionFromClerics("protection from Clerics",10),
    ProtectionFromDemons("protection from Demons",10),
    ProtectionFromDragons("protection from Dragons",10),
    ProtectionFromVampires("protection from Vampires",10),
    ProtectionFromWerewolves("protection from Werewolves",10),
    ProtectionFromColoredSpells("protection from colored spells",100),
    ProtectionFromEverything("protection from everything",200),
    ProtectionFromZombies("protection from Zombies(\\.)?",10),
    ProtectionFromLands("protection from lands",10),
    ProtectionFromSpirits("protection from Spirits",10),
    ProtectionFromArcane("protection from Arcane",10),
    ProtectionFromElves("protection from Elves",10),
    ProtectionFromGoblins("protection from Goblins",10),
    ProtectionFromKavu("protection from Kavu",10),
    ProtectionFromSnow("protection from snow",10),
    ProtectionFromLegendaryCreatures("protection from legendary creatures",10),
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
    CantActivateAbilities("can't activate abilities(\\.)?|its activated abilities can't be activated(\\.)?",-20),

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
            card.add(new MagicBecomesBlockedPumpTrigger(n,n,false));
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
            card.add(new MagicFadeVanishCounterTrigger(MagicCounterType.Fade));
        }
    },
    Vanishing("vanishing " + ARG.NUMBER,-20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            if (n > 0) {
                card.add(new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Time,n));
            }
            card.add(new MagicFadeVanishCounterTrigger(MagicCounterType.Time));
        }
    },
    CumulativeUpkeep("cumulative upkeep " + ARG.COST,-30) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicCumulativeUpkeepTrigger(manaCost));
        }
    },
    LevelUp("level up " + ARG.COST + " " + ARG.NUMBER, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost cost = MagicManaCost.create(ARG.cost(arg));
            final int maxLevel = ARG.number(arg);
            card.add(new MagicLevelUpActivation(cost, maxLevel));
        }
    },
    BlockedByPump("Whenever SN becomes blocked, it gets " + ARG.PT + " until end of turn for each creature blocking it\\.", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String[] pt = ARG.pt(arg).replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(new MagicBecomesBlockedPumpTrigger(power,toughness,true));
        }
    },
    BlockedByCreaturePump("Whenever SN becomes blocked by a creature, SN gets " + ARG.PT + " until end of turn\\.", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String[] pt = ARG.pt(arg).replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(new MagicBlockedByCreaturePumpTrigger(power,toughness));
        }
    },
    ShockLand("As SN enters the battlefield, you may pay 2 life\\. If you don't, SN enters the battlefield tapped\\.", -10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicRavnicaLandTrigger.create());
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
    AttacksEffect("Whenever SN attacks, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenSelfAttacksTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    BlocksEffect("Whenever SN blocks, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenSelfBlocksTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AttacksOrBlocksEffect("When(ever)? SN attacks or blocks, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(ARG.effect(arg));
            card.add(MagicWhenSelfAttacksTrigger.create(sourceEvent));
            card.add(MagicWhenSelfBlocksTrigger.create(sourceEvent));
        }
    },
    BecomesBlockedEffect("Whenever SN becomes blocked, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenSelfBecomesBlockedTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    BlocksOrBlockedEffect("Whenever SN blocks or becomes blocked, " + ARG.EFFECT, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(ARG.effect(arg));
            card.add(MagicWhenSelfBlocksTrigger.create(sourceEvent));
            card.add(MagicWhenSelfBecomesBlockedTrigger.create(sourceEvent));
        }
    },
    UntappedEffect("Whenever SN becomes untapped, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenSelfBecomesUntappedTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    TappedEffect("Whenever SN becomes tapped, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenSelfBecomesTappedTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SelfOrAnotherYouControlEntersEffect("Whenever SN or another " + ARG.WORDRUN + " enters the battlefield under your control, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherComesIntoPlayTrigger.createSelfOrAnother(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg) + " you control"),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AnotherYouControlEntersEffect("Whenever another " + ARG.WORDRUN + " enters the battlefield under your control, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherComesIntoPlayTrigger.createAnother(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg) + " you control"),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OtherYouControlEntersEffect("Whenever a(n)? " + ARG.WORDRUN + " enters the battlefield under your control, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherComesIntoPlayTrigger.create(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg) + " you control"),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SelfOrAnotherEntersEffect("Whenever SN or another " + ARG.WORDRUN + " enters the battlefield, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherComesIntoPlayTrigger.createSelfOrAnother(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AnotherEntersEffect("Whenever another " + ARG.WORDRUN + " enters the battlefield, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherComesIntoPlayTrigger.createAnother(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OtherEntersEffect("Whenever a(n)? " + ARG.WORDRUN + " enters the battlefield, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherComesIntoPlayTrigger.create(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SelfOrAnotherLeavesEffect("Whenever SN or another " + ARG.WORDRUN + " leaves the battlefield, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenLeavesPlayTrigger.createSelfOrAnother(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AnotherLeavesEffect("Whenever another " + ARG.WORDRUN + " leaves the battlefield, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenLeavesPlayTrigger.createAnother(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    LeavesEffect("Whenever a(n)? " + ARG.WORDRUN + " leaves the battlefield, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenLeavesPlayTrigger.create(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    TapAddMana("\\{T\\}: Add " + ARG.MANA + " to your mana pool\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(new MagicTapManaActivation(manatype));
        }
    },
    TapDrainAddMana("\\{T\\}, Remove a charge counter from SN: Add " + ARG.MANA + " to your mana pool\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(new MagicVividManaActivation(manatype));
        }
    },
    TapPainAddMana("\\{T\\}: Add " + ARG.MANA + " to your mana pool\\. SN deals 1 damage to you\\.", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(new MagicPainTapManaActivation(manatype));
        }
    },
    TapPayLifeAddMana("\\{T\\}, Pay 1 life: Add " + ARG.MANA + " to your mana pool\\.", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(new MagicPayLifeTapManaActivation(manatype));
        }
    },
    SacAddMana("Sacrifice SN: Add " + ARG.MANA + " to your mana pool\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(new MagicSacrificeManaActivation(manatype));
        }
    },
    SacAtEnd("At the beginning of the end step, sacrifice SN\\.",-100) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtEndOfTurnTrigger.Sacrifice);
        }
    },
    ExileAtEnd("At the beginning of the end step, exile SN\\.",-100) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtEndOfTurnTrigger.ExileAtEnd);
        }
    },
    TapSacAddMana("\\{T\\}, Sacrifice SN: Add " + ARG.MANA + " to your mana pool\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicManaType> manatype = MagicManaType.getList(ARG.mana(arg));
            card.add(new MagicSacrificeTapManaActivation(manatype));
        }
    },
    DamageDiscardCard("Whenever SN deals damage to a player, that player discards " + ARG.AMOUNT + " card(s)?\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.amount(arg);
            card.add(new MagicSpecterTrigger(Type.Any, Player.Any, n));
        }
    },
    DamageOpponentDiscardCard("Whenever SN deals damage to an opponent, that (opponent|player) discards " + ARG.AMOUNT + " card(s)?\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.amount(arg);
            card.add(new MagicSpecterTrigger(Type.Any, Player.Opponent, n));
        }
    },
    CombatDamageDiscardCard("Whenever SN deals combat damage to a player, that player discards " + ARG.AMOUNT + " card(s)?\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.amount(arg);
            card.add(new MagicSpecterTrigger(Type.Combat, Player.Any, n));
        }
    },
    CombatDamageDiscardRandomCard("Whenever SN deals combat damage to a player, that player discards " + ARG.AMOUNT + " card(s)? at random\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.amount(arg);
            card.add(MagicSpecterTrigger.Random(Type.Combat, Player.Any, n));
        }
    },
    DamageOpponentDiscardRandomCard("Whenever SN deals damage to an opponent, that (opponent|player) discards " + ARG.AMOUNT + " card(s)? at random\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.amount(arg);
            card.add(MagicSpecterTrigger.Random(Type.Any, Player.Opponent, n));
        }
    },
    DamageCreatureGrow("Whenever SN deals damage to a creature, put a \\+1/\\+1 counter on SN\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicDamageGrowTrigger(false, false));
        }
    },
    CombatDamageCreatureGrow("Whenever SN deals combat damage to a creature, put a \\+1/\\+1 counter on SN\\.",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicDamageGrowTrigger(true, false));
        }
    },
    DamageToOpponent("Whenever SN deals damage to an opponent, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenDamageIsDealtTrigger.DamageToOpponent(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    DamageToPlayer("Whenever SN deals damage to a player, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenDamageIsDealtTrigger.DamageToPlayer(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CombatDamageToPlayer("Whenever SN deals combat damage to a player, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenDamageIsDealtTrigger.CombatDamageToPlayer(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
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
    Champion("champion " + ARG.ANY,-10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicChampionTrigger(ARG.any(arg)));
            card.add(MagicLeavesReturnExileTrigger.create());
        }
    },
    LeavesReturnExile("When SN leaves the battlefield, (each player returns|return) (the exiled card(s)? |all cards exiled with it )?to the battlefield (under (its|their) owner('s|s') control|all cards he or she owns exiled with SN).", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicLeavesReturnExileTrigger.create());
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
    Echo("echo " + ARG.COST,-20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicEchoTrigger(MagicManaCost.create(ARG.cost(arg))));
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
    Multicounter("enters with counter \\+1/\\+1 for each kick " + ARG.COST, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicPlayMulticounterEvent(manaCost));
        }
    },
    Miracle("miracle " + ARG.COST, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicMiracleTrigger(manaCost));
        }
    },
    Kicker("kicker " + ARG.COST, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost cost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicKickerCost(cost));
        }
    },
    Buyback("buyback " + ARG.COST, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost cost = MagicManaCost.create(ARG.cost(arg));
            card.add(MagicKickerCost.Buyback(cost));
            card.add(MagicWhenSpellIsCastTrigger.Buyback);
        }
    },
    Multikicker("multikicker " + ARG.COST, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost cost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicMultikickerCost(cost));
        }
    },
    Replicate("replicate " + ARG.COST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost cost = MagicManaCost.create(ARG.cost(arg));
            card.add(MagicMultikickerCost.Replicate(cost));
            card.add(MagicReplicateTrigger.create());
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
    EndStepEffect("At the beginning of the end step, " + ARG.EFFECT, 0) {
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
    SelfDiesEffect("When SN (dies|is put into a graveyard from the battlefield), " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenDiesTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SelfOrAnotherDiesEffect("Whenever SN or another " + ARG.WORDRUN + " (dies|is put into a graveyard from the battlefield), " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherDiesTrigger.createSelfOrAnother(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    AnotherDiesEffect("Whenever another " + ARG.WORDRUN + " (dies|is put into a graveyard from the battlefield), " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherDiesTrigger.createAnother(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OtherDiesEffect("Whenever a(n)? " + ARG.WORDRUN + " (dies|is put into a graveyard from the battlefield), " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherDiesTrigger.create(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    SelfLeavesEffect("When SN leaves the battlefield, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenSelfLeavesPlayTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    ControlEnchanted("You control enchanted " + ARG.ANY + "\\.", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicStatic.ControlEnchanted);
        }
    },
    Evoke("evoke " + ARG.COST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicEvokeActivation(manaCost));
            card.add(MagicWhenComesIntoPlayTrigger.Evoke);
        }
    },
    Transmute("transmute " + ARG.COST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
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
    Cycling("cycling " + ARG.COST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicCyclingActivation(manaCost));
        }
    },
    TypeCycling(ARG.WORDRUN + "cycling " + ARG.COST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
            final String type = ARG.wordrun(arg);
            card.add(new MagicTypeCyclingActivation(manaCost,type));
        }
    },
    Reinforce("reinforce " + ARG.NUMBER + " - " + ARG.COST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicReinforceActivation(n, manaCost));
        }
    },
    Unleash("unleash", 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicUnleashTrigger.create());
            card.add(MagicStatic.Unleash);
        }
    },
    Ninjutsu("ninjutsu " + ARG.COST, 20) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicNinjutsuActivation(manaCost));
        }
    },
    Cascade("cascade", 50) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCascadeTrigger.create());
        }
    },
    AttachedPumpGain("(Equipped|Enchanted) creature gets " + ARG.PT + "((,)? and (has )?|, has )" + ARG.ANY + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String[] pt = ARG.pt(arg).replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(MagicStatic.genPTStatic(power, toughness));
            card.add(MagicStatic.genABStatic(
                MagicAbility.getAbilityList(
                    ARG.any(arg)
                )
            ));
        }
    },
    AttachedPump("(Equipped|Enchanted) creature gets " + ARG.PT + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String[] pt = ARG.pt(arg).replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(MagicStatic.genPTStatic(power, toughness));
        }
    },
    AttachedCreatureGain("(Equipped|Enchanted) (creature|artifact|land|permanent) " + ARG.ANY + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicStatic.genABStatic(
                MagicAbility.getAbilityList(
                    ARG.any(arg)
                )
            ));
        }
    },
    PairedPump("As long as SN is paired with another creature, each of those creatures gets " + ARG.PT + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String[] pt = ARG.pt(arg).replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(MagicStatic.genPTStatic(power, toughness));
        }
    },
    PairedGain("As long as SN is paired with another creature, (both creatures have|each of those creatures has) " + ARG.ANY + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicStatic.genABStatic(
                MagicAbility.getAbilityList(
                    ARG.any(arg)
                )
            ));
        }
    },
    ConditionPumpGain("SN (gets " + ARG.PT + " )?(and )?(" + ARG.ANY + " )?as long as " + ARG.WORDRUN + "\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCondition condition = MagicConditionParser.build(ARG.wordrun(arg));
            if (arg.group("pt") != null) {
                final String[] pt = ARG.pt(arg).replace("+","").split("/");
                final int power = Integer.parseInt(pt[0]);
                final int toughness = Integer.parseInt(pt[1]);
                card.add(MagicStatic.genPTStatic(condition, power, toughness));
            }
            if (arg.group("any") != null) {
                card.add(MagicStatic.genABStatic(
                    condition,
                    MagicAbility.getAbilityList(
                        ARG.any(arg)
                    )
                ));
            }
        }
    },
    ConditionPumpGainAlt("As long as (?<wordrun>[^\\,]*), (SN|it) (gets " + ARG.PT + "(.| ))?(and )?(" + ARG.ANY + ")?(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            ConditionPumpGain.addAbilityImpl(card, arg);
        }
    },
    ConditionPumpGroup("As long as " + ARG.WORDRUN + ", " + ARG.WORDRUN2 + " get " + ARG.PT + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCondition condition = MagicConditionParser.build(ARG.wordrun(arg));
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(ARG.wordrun2(arg));
            final String[] pt = ARG.pt(arg).replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            card.add(MagicStatic.genPTStatic(condition, filter, power, toughness));
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
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(ARG.wordrun2(arg));
            card.add(MagicStatic.genABStatic(
                condition,
                filter,
                MagicAbility.getAbilityList(
                    ARG.any(arg)
                )
            ));
        }
    },
    ConditionGainGroupAlt(ARG.WORDRUN2 + " have " + ARG.ANY + " as long as " + ARG.WORDRUN + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            ConditionGainGroup.addAbilityImpl(card, arg);
        }
    },
    Equip("Equip " + ARG.COST, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost cost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicEquipActivation(cost));
        }
    },
/*    EnchantDual("Enchant " + ARG.WORD1 + " or "+ ARG.WORD2, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicPlayAuraEvent.create("default," + ARG.word1(arg) + " or " + ARG.word2(arg)));
        }
    },
    Enchant("Enchant " + ARG.WORDRUN, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicPlayAuraEvent.create("default," + ARG.wordrun(arg)));
        }
    },*/
    //Cannot implement target pickers
    Poisonous("poisonous " + ARG.NUMBER + "(\\.)?", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            card.add(MagicWhenDamageIsDealtTrigger.Poisonous(n));
        }
    },
    Monstrosity("monstrosity " + ARG.NUMBER + " " + ARG.COST, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicMonstrosityActivation(manaCost, n));
        }
    },
    WhenMonstrous("When SN becomes monstrous, " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenBecomesStateTrigger.create(
                MagicPermanentState.Monstrous,
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    Tribute("tribute " + ARG.NUMBER + " " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int n = ARG.number(arg);
            final String effect  = ARG.effect(arg).replaceFirst("^effect ", "");
            card.add(MagicTributeTrigger.create(n,  MagicRuleEventAction.create(effect)));
        }
    },
    Bestow("bestow " + ARG.COST, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicBestowActivation(manaCost));
        }
    },
    Channel("Channel \u2014 " + ARG.ANY, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCardAbilityActivation.create(ARG.any(arg), "Channel"));
        }
    },
    Bloodrush("Bloodrush \u2014 " + ARG.ANY, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicCardAbilityActivation.create(ARG.any(arg), "Bloodrush"));
        }
    },
    ActivatedAbilityRestricted("[^\"]+:(?! Add)" + ARG.ANY + " Activate this ability.*", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            String text = arg.group();
            for (final Restriction r : Restriction.values()) {
                if (text.endsWith(r.text)) {
                    text = r.cost + "," + text.replace(r.text, "");
                }
            }
            card.add(MagicPermanentActivation.create(text));
        }
    },
    ActivatedAbility("[^\"]+:(?! Add)" + ARG.ANY, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicPermanentActivation.create(arg.group()));
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
    AlternateCost2("You may (pay )?" + ARG.ANY + " rather than pay SN's mana cost\\.", 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher matcher) {
            final String arg = matcher.group("any").replace(" and ",", ");
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(MagicCardActivation.create(cardDef, arg, "Alt"));
        }
    },
    AdditionalCost("As an additional cost to cast SN, " + ARG.COST, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAdditionalCost.create(new MagicMatchedCostEvent(ARG.cost(arg))));
        }
    },
    HeroicEffect("Whenever you cast a spell that targets SN, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicHeroicTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    EachUpkeepNoSpellTransform("At the beginning of each upkeep, if no spells were cast last turn, transform SN.", 10){
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtUpkeepTrigger.NoSpellsTransform);
        }
    },
    EachUpkeepTwoPlusSpellTransform("At the beginning of each upkeep, if a player cast two or more spells last turn, transform SN.",10){
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtUpkeepTrigger.TwoOrMoreSpellsTransform);
        }
    },
    EachUpkeepEffect("At the beginning of each upkeep, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtUpkeepTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    YourUpkeepEffect("At the beginning of your upkeep, " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicAtUpkeepTrigger.createYour(
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
    PlayerCastSpellEffect("Whenever a player casts a(n)? (?<wordrun>[^\\.]*spell[^,]*), " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherSpellIsCastTrigger.create(
                MagicTargetFilterFactory.singleItemOnStack(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    YouCastSpellEffect("When(ever)? you cast a(n)? (?<wordrun>[^\\.]*spell[^,]*), " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherSpellIsCastTrigger.createYou(
                MagicTargetFilterFactory.singleItemOnStack(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    OpponentCastSpellEffect("Whenever an opponent casts a(n)? (?<wordrun>[^\\.]*spell[^,]*), " + ARG.EFFECT, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenOtherSpellIsCastTrigger.createOpponent(
                MagicTargetFilterFactory.singleItemOnStack(ARG.wordrun(arg)),
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
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
    Flashback("flashback " + ARG.COST,10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicMatchedCostEvent.build(ARG.cost(arg));
            card.add(new MagicFlashbackActivation(cardDef, matchedCostEvents));
        }
    },
    Scavenge("scavenge " + ARG.COST,10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
            final MagicCardDefinition cardDef = (MagicCardDefinition)card;
            card.add(new MagicScavengeActivation(cardDef, manaCost));
        }
    },
    Unearth("unearth " + ARG.COST,10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final MagicManaCost manaCost = MagicManaCost.create(ARG.cost(arg));
            card.add(new MagicUnearthActivation(manaCost));
        }
    },
    SacWhenTargeted("When SN becomes the target of a spell or ability, sacrifice it\\.",-10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenSelfTargetedTrigger.SacWhenTargeted);
        }
    },
    WhenTargeted("When(ever)? SN becomes the target of a(n)? (?<wordrun>[^\\,]*), " + ARG.EFFECT, 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenSelfTargetedTrigger.create(
                MagicTargetFilterFactory.singleItemOnStack(ARG.wordrun(arg)),
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
    LordPumpGain("(?<other>other )?" + ARG.WORDRUN + " get(s)? " + ARG.PT + " and (have|has) " + ARG.ANY + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String[] pt = ARG.pt(arg).replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            final boolean other = arg.group("other") != null; 
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(ARG.wordrun(arg));
            if (other) {
                card.add(MagicStatic.genPTStaticOther(filter, power, toughness));
                card.add(MagicStatic.genABStaticOther(
                    filter,
                    MagicAbility.getAbilityList(
                        ARG.any(arg)
                    )
                ));
            } else {
                card.add(MagicStatic.genPTStatic(filter, power, toughness));
                card.add(MagicStatic.genABStatic(
                    filter,
                    MagicAbility.getAbilityList(
                        ARG.any(arg)
                    )
                ));
            }
        }
    },
    LordPump("(?<other>other )?" + ARG.WORDRUN + " get(s)? " + ARG.PT + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final String[] pt = ARG.pt(arg).replace("+","").split("/");
            final int power = Integer.parseInt(pt[0]);
            final int toughness = Integer.parseInt(pt[1]);
            final boolean other = arg.group("other") != null; 
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(ARG.wordrun(arg));
            if (other) {
                card.add(MagicStatic.genPTStaticOther(filter, power, toughness));
            } else {
                card.add(MagicStatic.genPTStatic(filter, power, toughness));
            }
        }
    },
    LordGainCant("(?<other>other )?" + ARG.WORDRUN + " can't " + ARG.ANY + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final boolean other = arg.group("other") != null;
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(ARG.wordrun(arg));
            final MagicAbility ability = MagicAbility.getAbility("can't " + ARG.any(arg));
            if (other) {
                card.add(MagicStatic.genABGameStaticOther(
                    filter,
                    ability
                ));
            } else {
                card.add(MagicStatic.genABGameStatic(
                    filter,
                    ability
                ));
            }
        }
    },
    LordGain("(?<other>other )?" + ARG.WORDRUN + " (have|has) " + ARG.ANY + "(\\.)?", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final boolean other = arg.group("other") != null;
            final MagicTargetFilter<MagicPermanent> filter = MagicTargetFilterFactory.multiple(ARG.wordrun(arg));
            if (other) {
                card.add(MagicStatic.genABStaticOther(
                    filter,
                    MagicAbility.getAbilityList(
                        ARG.any(arg)
                    )
                ));
            } else {
                card.add(MagicStatic.genABStatic(
                    filter,
                    MagicAbility.getAbilityList(
                        ARG.any(arg)
                    )
                ));
            }
        }
    },
    ChooseNotUntap("You may choose not to untap SN during your untap step\\.",0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.addAbility(DoesNotUntap);
            card.add(MagicAtUntapTrigger.createYour(
                MagicRuleEventAction.create("You may untap sn.")
            ));
        }
    },
    Dethrone("dethrone(\\.)?",10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicDethroneTrigger.create());
        }
    },
    Madness("madness " + ARG.COST,0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(new MagicMadnessTrigger(MagicManaCost.create(ARG.cost(arg))));
        }
    },
    Morph("morph " + ARG.COST, 10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final List<MagicMatchedCostEvent> matchedCostEvents = MagicMatchedCostEvent.build(ARG.cost(arg));
            card.add(new MagicMorphActivation(matchedCostEvents));
            card.add(MagicMorphCastActivation.create());
        }
    },
    TurnedFaceUpEffect("When SN is turned face up, " + ARG.EFFECT,10) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenSelfTurnedFaceUpTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    WhenOtherFaceUpEffect("Whenever a permanent is turned face up, " + ARG.EFFECT,0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            card.add(MagicWhenTurnedFaceUpTrigger.create(
                MagicRuleEventAction.create(ARG.effect(arg))
            ));
        }
    },
    CDAPT("SN's power and toughness are each equal to (" + ARG.NUMBER + " plus )?the number of " + ARG.WORDRUN + "\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int base = (arg.group("number") != null) ? ARG.number(arg) : 0;
            card.add(MagicCDA.setPT(
                base,
                MagicTargetFilterFactory.multipleTargets(ARG.wordrun(arg))
            ));
        }
    },
    CDAPower("SN's power is equal to (" + ARG.NUMBER + " plus )?the number of " + ARG.WORDRUN + "\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int base = (arg.group("number") != null) ? ARG.number(arg) : 0;
            card.add(MagicCDA.setPower(
                base,
                MagicTargetFilterFactory.multipleTargets(ARG.wordrun(arg))
            ));
        }
    },
    CDAToughness("SN's toughness is equal to (" + ARG.NUMBER + " plus )?the number of " + ARG.WORDRUN + "\\.", 0) {
        protected void addAbilityImpl(final MagicAbilityStore card, final Matcher arg) {
            final int base = (arg.group("number") != null) ? ARG.number(arg) : 0;
            card.add(MagicCDA.setToughness(
                base,
                MagicTargetFilterFactory.multipleTargets(ARG.wordrun(arg))
            ));
        }
    },
    ;

    public static final Set<MagicAbility> PROTECTION_FLAGS = EnumSet.range(ProtectionFromBlack, ProtectionFromZombies);
    
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
        throw new RuntimeException("unknown ability \"" + name + "\"");
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
        "(?:has )?\"([^\"]+)\"(?:, and | and )?|" + 
        "(?:has )?([A-Za-z][^,]+)(?:, and | and )|" + 
        "(?:has )?([A-Za-z][^,]+)"
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
    
    private enum Restriction {
        Once(" Activate this ability only once each turn.", "{Once}"),
        YourTurn(" Activate this ability only during your turn.", "{YourTurn}"),
        YourUpkeep(" Activate this ability only during your upkeep.", "{YourUpkeep}"),
        OpponentsUpkeep(" Activate this ability only during an opponent's upkeep.", "{OpponentsUpkeep}"),
        Sorcery(" Activate this ability only any time you could cast a sorcery.", "{Sorcery}"),
        BeforeYourAttack(" Activate this ability only during your turn, before attackers are declared.", "{BeforeYourAttack}"),
        Threshold(" Activate this ability only if seven or more cards are in your graveyard.", "{Threshold}"),
        Hellbent(" Activate this ability only if you have no cards in hand.", "{Hellbent}"),
        YourTurnOnce(" Activate this ability only during your turn and only once each turn.", "{YourTurn},{Once}"),
        YourUpkeepOnce(" Activate this ability only during your upkeep and only once each turn.", "{YourUpkeep},{Once}"),
        ;
        
        public final String text;
        public final String cost;

        private Restriction(final String aText, final String aCost) {
            text = aText;
            cost = aCost;
        }
    }
}
