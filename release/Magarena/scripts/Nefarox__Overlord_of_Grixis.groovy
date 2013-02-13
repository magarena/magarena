[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player=permanent.getController();
            return (creature == permanent && player.getNrOfAttackers()==1) ?
                new MagicEvent(
                    permanent,
                    player.getOpponent(),
                    this,
                    "PN sacrifices a creature."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getSource(),
                event.getPlayer(),
                MagicTargetChoice.SACRIFICE_CREATURE
            ));
        }
    }
]
