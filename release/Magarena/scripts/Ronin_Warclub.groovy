[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player=permanent.getController();
            return (otherPermanent.isCreature() && 
                    otherPermanent.getController()==player) ?
                new MagicEvent(
                    permanent,
                    player,
                    otherPermanent,
                    this,
                    "Attach SN to " + otherPermanent + "."
                ) :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicAttachEquipmentAction(
                event.getPermanent(),
                event.getRefPermanent()
            ));
        }
    }
]
