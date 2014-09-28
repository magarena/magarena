[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent ) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Other creatures you control gets +X/+X until end of turn, where X is SN's power."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final int power = permanent.getPower();
            final Collection targets = game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                if (target != permanent) {
                    game.doAction(new MagicChangeTurnPTAction(target,power,power));
                }
            }        
        }
    }
]
