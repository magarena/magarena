[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.getTarget().isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
					permanent.getController(),
                    damage.getTarget(),
                    this,
                    "RN sacrifices two permanents."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {			
			game.addEvent(new MagicSacrificePermanentEvent(event.getRefPlayer(),MagicTargetChoice.SACRIFICE_PERMANENT));
			game.addEvent(new MagicSacrificePermanentEvent(event.getRefPlayer(),MagicTargetChoice.SACRIFICE_PERMANENT));
        }
    }
]