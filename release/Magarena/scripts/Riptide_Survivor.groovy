[
    new SelfTurnedFaceUpTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return new MagicEvent(
                permanent,
                this,
                "PN discards two cards, then draws three cards."
            )
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getSource(),event.getPlayer(),2));
            game.addEvent(new MagicDrawEvent(event.getSource(),event.getPlayer(),3));
        }
    }
]
