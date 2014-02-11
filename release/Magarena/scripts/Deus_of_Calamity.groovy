def LAND_YOUR_OPPONENT_CONTROLS = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
		return target.isLand() && target.isOpponent(player);
	}
};

def TARGET_LAND_YOUR_OPPONENT_CONTROLS = new MagicTargetChoice(
    LAND_YOUR_OPPONENT_CONTROLS,
    "a land opponent controls"
);

[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    permanent.isOpponent(damage.getTarget()) &&
					damage.getAmount() >= 6) ?
				new MagicEvent(
					permanent,
					TARGET_LAND_YOUR_OPPONENT_CONTROLS,
					MagicDestroyTargetPicker.Destroy,
					this,
					"Destroy target land\$."
				):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
			event.processTargetPermanent(game, {
                final MagicPermanent land ->
                game.doAction(new MagicDestroyAction(land));
            });
        }
    }
]
