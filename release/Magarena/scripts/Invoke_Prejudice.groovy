[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            final List<MagicPermanent> creatures = CREATURE.filter(permanent);
            final Set<MagicColor> colors = EnumSet.noneOf(MagicColor.class);
            for (final MagicColor color : MagicColor.values()) {
                for (final MagicPermanent creature : creatures) {
                    if (creature.hasColor(color)) {
                        colors.add(color);
                        break;
                    }
                }
            }
            boolean hasColor = false;
            for (final MagicColor color : colors) {
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
                        new MagicPayManaCostChoice(MagicManaCost.create(amount))
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
