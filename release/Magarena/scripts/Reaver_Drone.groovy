[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return COLORLESS_CREATURE_YOU_CONTROL.except(permanent).filter(upkeepPlayer).size() == 0 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN loses 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), -1));
        }
    }
]
