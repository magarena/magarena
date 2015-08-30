[
    new MagicWhenLifeIsLostTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicLifeChangeTriggerData lifeChange) {
            final int amount = lifeChange.amount;
            return permanent.isOpponent(lifeChange.player) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "PN gains RN life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(
                event.getPlayer(),
                event.getRefInt()
            ));
        }
    }
]
