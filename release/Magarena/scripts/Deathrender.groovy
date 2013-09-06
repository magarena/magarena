[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return (permanent.getEquippedCreature() == died) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.CREATURE_CARD_FROM_HAND
                    ),
                    MagicGraveyardTargetPicker.PutOntoBattlefield,
                    this,
                    "PN may\$ put a creature card\$ from his or her hand " +
                    "onto the battlefield and attach SN to it."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                        final MagicPlayCardAction action = new MagicPlayCardAction(card,event.getPlayer());
                        game.doAction(action);
                        game.doAction(new MagicAttachAction(event.getPermanent(),action.getPermanent()));
                    }
                });
            }
        }
    }
]
