[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = permanent.getChosenPlayer().getGraveyard().getNrOf(MagicType.Creature);
            pt.set(amount,amount);
        }
    },
    
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (cardOnStack.getController() == permanent.getChosenPlayer()) ?
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    this,
                    "PN puts the top card of his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MillLibraryAction(event.getPlayer(), 1));
        }
    }
]
