[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
		 return (otherPermanent != permanent &&
                    otherPermanent.isCreature()) ?
            new MagicEvent(
                permanent,
                this,
                "Exile SN. Return it to the battlefield under its owner's control at the beginning of the next end step."
            ):
	    MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicExileUntilEndOfTurnAction(event.getPermanent()));
        }
    }
]
