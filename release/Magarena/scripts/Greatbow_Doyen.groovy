[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source = damage.getSource();
			final int amount = damage.getDealtAmount();
            return (source.isPermanent() &&
                    damage.getTarget().isCreature() &&
                    source.hasSubType(MagicSubType.Archer)) ?
                new MagicEvent(
                    source,
                    damage.getTarget().getController(),
                    amount,
                    this,
                    "SN deals RN damage to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(
                event.getSource(),
                event.getPlayer(),
                event.getRefInt()
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
