[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ATTACKING_CREATURE,
                this,
                "PN gains 3 life for each creature attacking PN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = 3*event.getPlayer().getNrOfPermanents(ATTACKING_CREATURE_YOUR_OPPONENT_CONTROLS);
            game.doAction(new ChangeLifeAction(event.getPlayer(),amount));
        }
    }
]
