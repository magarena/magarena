[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            if (permanent != attacker) {
                return MagicEvent.NONE;
            }

            final MagicPermanentList plist = new MagicPermanentList(permanent.getBlockingCreatures());
            return new MagicEvent(
                permanent,
                permanent.getController(),
                plist,
                this,
                plist.size() == 1 ?
                    "Destroy " + plist.get(0) + "." :
                    "Destroy blocking creatures."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPermanent blocker : event.getRefPermanentList()) {
                game.doAction(new MagicDestroyAction(blocker));
            }
        }
    }
]
