[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            final List<MagicPermanent> creatures = CREATURE.filter(permanent.getController());
            final Set<MagicColor> creatureColors = new HashSet<MagicColor>();
            for (final MagicPermanent creature : creatures) {
                for (final MagicColor color : MagicColor.values()) {
                    if (creature.hasColor(color)) {
                        creatureColors.add(color);
                    }
                }
            }
            boolean hasColor = false;
            for (final MagicColor color : creatureColors) {
                if (cardOnStack.hasColor(color)) {
                    hasColor = true;
                    break;
                }
            }
            final int amount = cardOnStack.getConvertedCost();
            return (cardOnStack.hasType(MagicType.Creature) && cardOnStack.isEnemy(permanent) && hasColor == false) ?
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    new MagicMayChoice(
                        "Pay {"+amount+"}?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{"+amount+"}"))
                    ),
                    cardOnStack,
                    this,
                    "Counter RN unless PN pays {X}, where X is its converted mana cost. (X=${amount})"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isNo()) {
                game.doAction(new CounterItemOnStackAction(event.getRefItemOnStack()));
            }
        }
    }
]
