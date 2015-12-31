[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player returns a creature he or she controls to its owner's hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                game.addEvent(new MagicBounceChosenPermanentEvent(
                    event.getSource(),
                    player,
                    TARGET_CREATURE_YOU_CONTROL
                ));
            }
        }
    }
]
