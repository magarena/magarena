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
                final int amt = TAPPED_CREATURE_YOU_CONTROL.filter(it).size();
                game.doAction(new DrawAction(event.getPlayer(),amt));
            });
        }
    } 
]
