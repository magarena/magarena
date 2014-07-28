[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller discards his or her hand."
            );
        }
        public MagicEvent perplexChoice(final MagicSource source, final MagicCardOnStack card) {
            return new MagicEvent(
                source,
                card.getController(),
                new MagicMayChoice(),
                card,
                new MagicEventAction() {
                    @Override
                    public void executeEvent(final MagicGame game, final MagicEvent event) {
                        if (event.isYes()) {
                            game.addEvent(new MagicDiscardEvent(event.getSource(),event.getPlayer(),event.getPlayer().getHandSize()));
                        } else {
                            game.doAction(new MagicCounterItemOnStackAction(
                                event.getRefItemOnStack(),
                                MagicLocationType.Graveyard
                            ));
                        }
                    } 
                },
                ""+card+" is countered, unless PN discards his or her hand.\$ "
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.addEvent(perplexChoice(event.getSource(),it));
            });
        }
    }
]
