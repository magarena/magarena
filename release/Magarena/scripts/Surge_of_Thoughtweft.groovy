[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Creatures PN controls get +1/+1 until end of turn. " +
                "If you control a Kithkin, draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer you = event.getPlayer();
            final Collection<MagicPermanent> creatures = you.filterPermanents(CREATURE_YOU_CONTROL);
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicChangeTurnPTAction(creature,1,1));
            }
            if (you.controlsPermanent(MagicSubType.Kithkin)) {
                game.doAction(new MagicDrawAction(you));
            }
        }
    }
]
