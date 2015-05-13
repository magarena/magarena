[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature an opponent controls.\$ "+
                "Each other creature that player controls gets -2/-0 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                CREATURE_YOU_CONTROL.except(it).filter(it) each {
                    game.doAction(new ChangeTurnPTAction(it, -2, 0));
                }
            });
        }
    }
]
