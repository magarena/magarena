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
           final MagicSource source=event.getSource();
            for (final MagicPlayer player : game.getAPNAP()) {
                final int amount = event.getRefInt();
                final MagicDamage damage=new MagicDamage(source,player,amount);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
