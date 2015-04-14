[
    new MagicWhenSelfBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            final MagicPermanentList plist = new MagicPermanentList(permanent.getBlockingCreatures());
            return new MagicEvent(
                permanent,
                plist,
                this,
                plist.size() == 1 ?
                    "Put " + plist.get(0) + " on top of its owner's library." :
                    "Put blocking creatures on top of their owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanentList plist = event.getRefPermanentList();
            for (final MagicPermanent blocker : plist) {
                game.doAction(new RemoveFromPlayAction(blocker,MagicLocationType.TopOfOwnersLibrary));
            }
        }
    }
]
