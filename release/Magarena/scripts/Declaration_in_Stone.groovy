[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature\$ and all other creatures its controller controls with the same name as that creature. " +
                "That player investigates for each nontoken creature exiled this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, { MagicPermanent it ->
                int count = 0;
                new MagicNameTargetFilter(
                    CREATURE,
                    it.getName()
                ).filter(event) each { that ->
                    if (that.isFriend(it)) {
                        game.doAction(new RemoveFromPlayAction(that, MagicLocationType.Exile));
                        if (that.getCard().isInExile()) {
                            count++;
                        }
                    }
                }
                game.doAction(new PlayTokensAction(
                    it.getController(),
                    CardDefinitions.getToken("colorless Clue artifact token"),
                    count
                ))
            });
        }
    }
]
