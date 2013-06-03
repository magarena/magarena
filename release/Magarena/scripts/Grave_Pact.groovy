[
    new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player = permanent.getController();
            return (otherPermanent.isCreature() &&
                    otherPermanent.getController() == player) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Each other player sacrifices a creature."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent = event.getPlayer().getOpponent();
            if (opponent.controlsPermanent(MagicType.Creature)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    opponent,
                    MagicTargetChoice.SACRIFICE_CREATURE
                ));
            }
        }
    }
]
