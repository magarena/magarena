[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getTarget() == permanent) ?
                new MagicEvent(
                    permanent,
                    damage.getDealtAmount(),
                    this,
                    "SN deals RN damage to each player."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new MagicDealDamageAction(event.getSource(),player,event.getRefInt()));
            }
        }
    }
]
