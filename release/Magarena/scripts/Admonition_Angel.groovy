[
    new MagicLandfallTrigger() {
        @Override
        protected MagicEvent getEvent(final MagicPermanent permanent) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_NONLAND_PERMANENT,
                    permanent
                ),
                MagicTargetHint.None,
                "another target nonland permanent to exile"
            );
            return new MagicEvent(
                permanent,
                new MagicMayChoice(targetChoice),
                MagicExileTargetPicker.create(),
                this,
                "PN may\$ exile another target nonland permanent\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent target) {
                        game.doAction(new MagicExileUntilThisLeavesPlayAction(event.getPermanent(),target));
                    }
                });
            }
        }
    }
]
