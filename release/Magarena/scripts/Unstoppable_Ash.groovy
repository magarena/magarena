[
    new BecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return permanent.isFriend(attacker) ?
                new MagicEvent(
                    permanent,
                    attacker,
                    this,
                    "RN gets +0/+5 until end of turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(
                event.getRefPermanent(),
                0,
                5
            ));
        }
    }
]
