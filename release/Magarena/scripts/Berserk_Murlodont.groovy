[
    new BecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            return blocked.hasSubType(MagicSubType.Beast) ?
                new MagicEvent(
                    permanent,
                    blocked,
                    this,
                    "RN gets +1/+1 until end of turn for each creature blocking it."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent blocked = event.getRefPermanent();
            final int amount = blocked.getBlockingCreatures().size();
            game.doAction(new ChangeTurnPTAction(
                blocked,
                amount,
                amount
            ));
        }
    }
]
