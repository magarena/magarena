[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
            return !MagicCondition.DELIRIUM_CONDITION.accept(player) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN loses 4 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), -4));
        }
    }
]
