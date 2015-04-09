[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return payedCost.isKicked() ?
                new MagicEvent(
                    permanent,
                    TARGET_OPPONENT,
                    payedCost.getKicker(),
                    this,
                    "Target opponent\$ discards a card for each time SN was kicked. (RN)"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicDiscardEvent(event.getSource(),it,event.getRefInt()));
            });
        }
    }
]
