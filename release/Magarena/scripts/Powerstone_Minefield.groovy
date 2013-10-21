[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
				permanent,
				creature,
				this,
				"SN deals 2 damage to attacking creature."
			);
        }
		@Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(event.getSource(),event.getRefPermanent(),2);
            game.doAction(new MagicDealDamageAction(damage));
        }
        
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return new MagicEvent(
				permanent,
				creature,
				this,
				"SN deals 2 damage to blocking creature."
			);
        }
		@Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(event.getSource(),event.getRefPermanent(),2);
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
