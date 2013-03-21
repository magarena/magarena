[
	new MagicWhenPutIntoGraveyardTrigger() {
		@Override
        MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
            return (triggerData.fromLocation == MagicLocationType.Play) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Each player sacrifices a creature."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            for (final MagicPlayer player : game.getPlayers()) {
            	game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        player,
                        MagicTargetChoice.SACRIFICE_CREATURE));
            }
        }
    }
]