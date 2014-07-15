[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(MagicTargetChoice.SACRIFICE_FOREST),
                MagicSacrificeTargetPicker.create(),
                this,
                "PN may\$ sacrifice a Forest\$. If you don't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent target ->
                    game.doAction(new MagicSacrificeAction(target));
                });
            } else {
                game.doAction(new MagicSacrificeAction(permanent));
            }
        }
    }
]
