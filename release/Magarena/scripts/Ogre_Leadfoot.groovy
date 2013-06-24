[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            if (permanent != attacker) {
                return MagicEvent.NONE;
            }
            final MagicPermanentList plist = new MagicPermanentList();
            for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
                if (blocker.isArtifact() && blocker.isCreature()) {
                    plist.add(blocker);
                }
            }
            return !plist.isEmpty() ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    plist,
                    this,
                    plist.size() == 1 ?
                        "Destroy " + plist.get(0) + "." :
                        "Destroy blocking artifact creatures."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanentList plist = event.getRefPermanentList();
            for (final MagicPermanent blocker : plist) {
                game.doAction(new MagicDestroyAction(blocker));
            }
        }
    }
]
