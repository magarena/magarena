[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(permanent.getPower()),
                this,
                "SN deals damage equal to its power to target creature or player\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            event.processTarget(game, {
                game.doAction(new DealDamageAction(permanent,it,permanent.getPower()));
            });
        }
    }
]
