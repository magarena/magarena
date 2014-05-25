[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source = damage.getSource();
			final int amt = damage.getAmount();
            return (damage.isCombat() &&
                    damage.isTargetPlayer() &&
					permanent.isFriend(source)) ?
						new MagicEvent(
							source,
							amt,
							this,							
							"Whenever a creature PN control deals combat damage to a player, Put that many +1/+1 counters on SN."
						) :
						MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                event.getRefInt(),
                true
            ));		
        }
    }
]
