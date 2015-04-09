[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Draw a card for each tapped creature target opponent\$ controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amt = game.filterPermanents(it, TAPPED_CREATURE_YOU_CONTROL).size();
                game.doAction(new MagicDrawAction(event.getPlayer(),amt));
            });
        }
    } 
]
