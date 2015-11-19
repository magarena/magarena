[
    new MagicWhenClashTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer winner) {
            return new MagicEvent(
                permanent,
                TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                winner,
                this,
                "Tap target creature an opponent controls\$. If PN won, that creature doesn't untap during its controller's next untap step."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new TapAction(it));
                if (event.getRefPlayer() == event.getPlayer()) {
                    game.doAction(ChangeStateAction.Set(
                        it,
                        MagicPermanentState.DoesNotUntapDuringNext
                    ));
                }
            });
        }
    }
]
