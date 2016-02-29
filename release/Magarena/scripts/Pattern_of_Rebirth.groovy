[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
            return (died == permanent.getEnchantedPermanent()) ?
                new MagicEvent(
                    permanent,
                    died.getController(),
                    new MagicMayChoice(A_CREATURE_CARD_FROM_LIBRARY),
                    MagicGraveyardTargetPicker.PutOntoBattlefield,
                    this,
                    "PN may\$ search his or her library for a creature card\$ and put that card onto the battlefield. "+
                    "If PN does, he or she shuffles his or her library."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,{
                    game.doAction(new ReturnCardAction(MagicLocationType.OwnersLibrary, it, event.getPlayer()));
                    game.doAction(new ShuffleLibraryAction(event.getPlayer()));
                });
            }
        }
    }
]
