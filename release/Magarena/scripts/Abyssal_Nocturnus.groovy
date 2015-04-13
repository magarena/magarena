[
    new MagicWhenDiscardedTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent,final MagicCard card) {
            return permanent.isEnemy(card) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +2/+2 and gains fear until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getPermanent(),2,2));
            game.doAction(new MagicGainAbilityAction(event.getPermanent(),MagicAbility.Fear));
        }
    }
]
