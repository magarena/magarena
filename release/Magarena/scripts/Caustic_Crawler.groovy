[
    new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    MagicTargetChoice.NEG_TARGET_CREATURE
                ),
                new MagicWeakenTargetPicker(1,1),
                this,
                "PN may\$ have target creature\$ get -1/-1 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicChangeTurnPTAction(creature,-1,-1));
                    }
                });
            }
        }
    }
]
