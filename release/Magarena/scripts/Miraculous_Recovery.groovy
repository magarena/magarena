[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN returns target creature card from his or her graveyard\$ to the battlefield. Put a +1/+1 counter on it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new ReturnCardAction(MagicLocationType.Graveyard,it,event.getPlayer(),{
                    final MagicPermanent perm ->
                    final MagicGame G = perm.getGame();
                    G.doAction(new ChangeCountersAction(event.getPlayer(),perm,MagicCounterType.PlusOne,1));
                }));
            });
        }
    }
]
