def T = new MagicWhenOtherPutIntoGraveyardTrigger() {
	 @Override
    public boolean accept(final MagicPermanent permanent, final MagicMoveCardAction act) {
        return act.getToLocation() == MagicLocationType.Graveyard;
    }
	
	@Override
	public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction act) {
		final MagicCard card = act.card;
		return (card.isEnemy(permanent) &&
                act.to(MagicLocationType.Graveyard)) ?
			new MagicEvent(
				permanent,
				card.getOwner(),
				this,
				"PN loses 1 life."
			) :
			MagicEvent.NONE;
	}
	@Override
	public void executeEvent(final MagicGame game, final MagicEvent event) {
		game.doAction(new MagicChangeLifeAction(event.getPlayer(), -1));
	}
};
[
	new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Mill"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{2}{U}{B}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_PLAYER,
                this,
                "Target player\$ puts the top two cards of his or her library " +
                "into his or her graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.doAction(new MagicMillLibraryAction(player,2));
                }
            });
        }
    },
	new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{U}{B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Whenever a card is put into an opponent's graveyard from anywhere this turn, that player loses 1 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            final MagicWhenOtherPutIntoGraveyardTrigger trigger = T;
            outerGame.doAction(new MagicAddTriggerAction(outerEvent.getPermanent(), trigger));
            // remove the trigger at player's end of the turn
            MagicAtUpkeepTrigger cleanup = new MagicAtUpkeepTrigger() {
                @Override
                public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                    if (upkeepPlayer.getId() != outerEvent.getPlayer().getId()) {
                        game.addDelayedAction(new MagicRemoveTriggerAction(permanent, trigger));
                        game.addDelayedAction(new MagicRemoveTriggerAction(permanent, this));
                    }
                    return MagicEvent.NONE;
                }
            };
            outerGame.doAction(new MagicAddTriggerAction(outerEvent.getPermanent(), cleanup));

        }
    }
]
