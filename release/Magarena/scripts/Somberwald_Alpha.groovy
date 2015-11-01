[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return permanent.isFriend(attacker) ?
                new MagicEvent(
                    permanent,
                    attacker,
                    this,
                    "RN gets +1/+1 until end of turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(
                event.getRefPermanent(),
                1,
                1
            ));
        }
    }
]
