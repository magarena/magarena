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
                    "Destroy " + plist.get(0) + ". You gain life equal to its toughness." :
                    "Destroy blocking creatures. You gain life equal to those creatures toughness."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanentList plist = event.getRefPermanentList();
            for (final MagicPermanent blocker : plist) {
                game.doAction(new MagicDestroyAction(blocker));
                game.doAction(new MagicChangeLifeAction(
                    event.getPlayer(),
                    blocker.getToughness()
                ));
            }
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent attacker = permanent.getBlockedCreature();
            return (permanent == blocker && attacker.isValid()) ?
                new MagicEvent(
                    permanent,
                    attacker,
                    this,
                    "Destroy RN. You gain life equal to its toughness."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent attacker = event.getRefPermanent();
            game.doAction(new MagicDestroyAction(attacker));
            game.doAction(new MagicChangeLifeAction(
                event.getPlayer(),
                attacker.getToughness()
            ));
        }
    }
]
