[
    new LifeIsLostTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicLifeChangeTriggerData lifeChange) {
            final int amount = lifeChange.amount;
            return permanent.isOpponent(lifeChange.player) ?
                new MagicEvent(
                    permanent,
                    lifeChange.player,
                    amount,
                    this,
                    "PN puts the top RN cards from PN's library into PN's graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MillLibraryAction(
                event.getPlayer(),
                event.getRefInt()
            ));
        }
    }
]
