def NONBLACK_CREATURE_YOUR_OPPONENT_CONTROLS=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isCreature() && !target.hasColor(MagicColor.Black) && target.isOpponent(player);
    } 
};

def TARGET_NONBLACK_CREATURE_YOUR_OPPONENT_CONTROLS = new MagicTargetChoice(
    NONBLACK_CREATURE_YOUR_OPPONENT_CONTROLS,
    "a nonblack creature your opponent controls"
);

[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
            damage.getTarget().isPlayer() &&
            damage.isCombat()) ?
        new MagicEvent(
            permanent,
            TARGET_NONBLACK_CREATURE_YOUR_OPPONENT_CONTROLS,
            MagicDestroyTargetPicker.Destroy,
            this,
            "Destroy target nonblack creature defending player controls."
        ):
        MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicDestroyAction(permanent));
            });
        }
    }
]