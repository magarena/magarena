def Zombie = new MagicStatic(MagicLayer.Type) {
     @Override
     public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
         flags.add(MagicSubType.Zombie);
     }
};
    
def Black = new MagicStatic(MagicLayer.Color) {
     @Override
     public int getColorFlags(final MagicPermanent permanent,final int flags) {
         return flags|MagicColor.Black.getMask();
     }
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                new MagicGraveyardTargetPicker(true),
                this,
                "Put target creature\$ card from a graveyard onto the " +
                "battlefield under your control. That creature is a black " +
                "Zombie in addition to its other colors and types."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicCard targetCard ->
                final MagicPlayer player = event.getPlayer();
                
                final MagicPlayCardAction action = new MagicPlayCardAction(targetCard,player,MagicPlayCardAction.NONE);
                game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                game.doAction(action);

                final MagicPermanent permanent = action.getPermanent();
                game.doAction(new MagicAddStaticAction(permanent, Zombie));
                game.doAction(new MagicAddStaticAction(permanent, Black));
            } as MagicCardAction);
        }
    }
]
