[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            if (permanent != attacker) {
                return MagicEvent.NONE;
            }
            int amount = 0;
            for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
                if (blocker.isArtifact() && blocker.isCreature()) {
                    amount += 3;
                }
            }
            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "SN gets +RN/+RN until end of turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(
                event.getPermanent(),
                event.getRefInt(),
                event.getRefInt()
            ));
        }
    }
]
