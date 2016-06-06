[
    new MagicPlaneswalkerActivation(-2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PLAYER,
                this,
                "Draw a card for each tapped creature target player\$ controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amt = TAPPED_CREATURE_YOU_CONTROL.filter(it).size();
                game.doAction(new DrawAction(event.getPlayer(),amt));
            });
        }
    },
    new MagicPlaneswalkerActivation(-8) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gets an emblem with " +
                "\"You have no maximum hand size\" and " +
                "\"Whenever a card is put into your graveyard from anywhere, you may return it to your hand.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            final MagicPlayer you = outerEvent.getPlayer();
            outerGame.doAction(new AddStaticAction(
                new MagicStatic(MagicLayer.Player) {
                    @Override
                    public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
                        if (player.getId() == you.getId()) {
                            player.noMaxHandSize();
                        }
                    }
                }
            ));
            outerGame.doAction(new AddTriggerAction(
                new OtherPutIntoGraveyardTrigger() {
                    @Override
                    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
                        return act.card.getOwner().getId() == you.getId() ?
                            // HACK: As emblem is not represented, source of event is the card
                            new MagicEvent(
                                act.card,
                                new MagicMayChoice(),
                                this,
                                "PN may\$ return SN to his or her hand."
                            ):
                            MagicEvent.NONE
                    }

                    @Override
                    public void executeEvent(final MagicGame game, final MagicEvent event) {
                        if (event.isYes()) {
                            game.doAction(new ShiftCardAction(event.getCard(),MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                        }
                    }
                }
            ));
        }
    }
]
