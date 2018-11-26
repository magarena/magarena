[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return (permanent.getEquippedCreature() == died) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        A_CREATURE_CARD_FROM_HAND
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
                event.processTargetCard(game, {
                    final MagicPermanent sn = event.getPermanent();
                    game.doAction(new PutOntoBattlefieldAction(MagicLocationType.OwnersHand,it,event.getPlayer(), {
                        final MagicPermanent perm ->
                        final MagicGame G = perm.getGame();
                        G.doAction(new AttachAction(sn.map(G), perm));
                    }));
                });
            }
        }
    }
]
