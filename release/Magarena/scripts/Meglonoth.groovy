[
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent defender) {
            final MagicPermanent blocked=permanent.getBlockedCreature();
            return (permanent==defender && blocked.isValid()) ?
                new MagicEvent(
                    permanent,
                    blocked.getController(),
                    this,
                    "SN deals damage to the blocked creature's controller equal to SN's power."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            game.doAction(new MagicDealDamageAction(permanent,event.getPlayer(),permanent.getPower()));
        }
    }
]
