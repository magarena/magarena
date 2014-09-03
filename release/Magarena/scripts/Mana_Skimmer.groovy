[
    new MagicWhenSelfCombatDamagePlayerTrigger() {     
	   @Override
           public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {    
	        return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_LAND,
                MagicTapTargetPicker.Tap,
                this,
                "Tap target land\$ defending player controls. It doesn't untap during its controller's next untap step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
		game.doAction(new MagicTapAction(it));
                game.doAction(MagicChangeStateAction.Set(
                    it,
                    MagicPermanentState.DoesNotUntapDuringNext
                ));
            });
        }
    }
]
