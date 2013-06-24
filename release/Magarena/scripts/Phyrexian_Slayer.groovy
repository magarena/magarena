[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            if (permanent != attacker) {
                return MagicEvent.NONE;
            }
            final MagicPermanentList plist = new MagicPermanentList();
            for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
                if (blocker.hasColor(MagicColor.White)) {
                    plist.add(blocker);
                }
            }
            return !plist.isEmpty() ?
                new MagicEvent(
                    permanent,
                    plist,
                    this,
                    plist.size() == 1 ?
                        "Destroy " + plist.get(0) + ". It can't be regenerated." :
                        "Destroy blocking white creatures. They can't be regenerated."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanentList plist = event.getRefPermanentList();
            for (final MagicPermanent blocker : plist) {
                game.doAction(MagicChangeStateAction.Set(blocker,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(blocker));
            }
        }
    }
]
