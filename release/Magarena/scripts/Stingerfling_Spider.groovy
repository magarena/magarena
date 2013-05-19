[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    MagicTargetChoice.NEG_TARGET_CREATURE_WITH_FLYING
                ),
                new MagicDestroyTargetPicker(false),
                this,
                "PN may\$ destroy target creature\$ with flying."
            );
        }
                    
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent permanent) {
                        game.doAction(new MagicDestroyAction(permanent));
                    }
                });
            }
        }
    }
]
