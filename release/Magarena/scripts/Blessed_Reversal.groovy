[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains 3 life for each creature attacking PN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = 3*event.getPlayer().getOpponent().getNrOfPermanents(ATTACKING_CREATURE_YOU_CONTROL);
            game.doAction(new ChangeLifeAction(event.getPlayer(),amount));
            game.logAppendValue(event.getPlayer(),amount);
        }
    }
]
