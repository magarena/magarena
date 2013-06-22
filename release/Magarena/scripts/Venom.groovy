[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedCreature();
            if (enchantedCreature != attacker) {
                return MagicEvent.NONE;
            }
            final MagicPermanentList plist = new MagicPermanentList();
            for (final MagicPermanent blocker : enchantedCreature.getBlockingCreatures()) {
                if (!blocker.hasSubType(MagicSubType.Wall)) {
                    plist.add(blocker);
                }
            }
            return !plist.isEmpty() ?
                new MagicEvent(
                    permanent,
                    plist,
                    this,
                    plist.size() == 1 ?
                        "Destroy " + plist.get(0) + " at end of combat." :
                        "Destroy blocking non-Wall creatures at end of combat."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanentList plist = event.getRefPermanentList();
            for (final MagicPermanent blocker : plist) {
                game.doAction(MagicChangeStateAction.Set(blocker,MagicPermanentState.DestroyAtEndOfCombat));
            }
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedCreature();
            final MagicPermanent blocked = enchantedCreature.getBlockedCreature();
            return (enchantedCreature == blocker &&
                    blocked.isValid() &&
                    !blocked.hasSubType(MagicSubType.Wall)) ?
                new MagicEvent(
                    permanent,
                    blocked,
                    this,
                    "Destroy RN at end of combat."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getRefPermanent();
            game.doAction(MagicChangeStateAction.Set(creature,MagicPermanentState.DestroyAtEndOfCombat));
        }
    }
]
