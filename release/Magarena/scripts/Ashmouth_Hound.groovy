[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return (permanent == attacker) ?
                new MagicEvent(
                    permanent,
                    new MagicPermanentList(permanent.getBlockingCreatures()),
                    this,
                    "SN deals 1 damage to each blocking creature."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanentList plist = event.getRefPermanentList();
            for (final MagicPermanent blocker : plist) {
                final MagicDamage damage = new MagicDamage(event.getPermanent(),blocker,1);
                game.doAction(new MagicDealDamageAction(damage));
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
                    "SN deals 1 damage to RN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(
                event.getSource(),
                event.getRefPermanent(),
                1
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
