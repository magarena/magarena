[
    new MagicPlaneswalkerActivation(-8) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gets an emblem with \"Whenever you cast a spell, exile target permanent.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            final long pId = outerEvent.getPlayer().getId();
            outerGame.doAction(new AddTriggerAction(
                new OtherSpellIsCastTrigger() {
                    @Override
                    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
                        return cardOnStack.getController().getId() == pId ?
                            new MagicEvent(
                                cardOnStack,
                                NEG_TARGET_PERMANENT,
                                MagicExileTargetPicker.create(),
                                this,
                                "Exile target permanent\$."
                            ):
                            MagicEvent.NONE;
                    }
                    @Override
                    public void executeEvent(final MagicGame game,final MagicEvent event) {
                        event.processTargetPermanent(game, {
                            game.doAction(new RemoveFromPlayAction(it,MagicLocationType.Exile));
                        });
                    }
                }
            ));
        }
    }
]
