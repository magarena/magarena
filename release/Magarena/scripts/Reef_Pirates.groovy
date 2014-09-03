[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (permanent == damage.getSource() &&
                    permanent.isOpponent(damage.getTarget())) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Opponent puts the top card of his or her library into his or her graveyard."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicMillLibraryAction(event.getPlayer().getOpponent(),1));
        }
    }
]
