[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.SACRIFICE_CREATURE,
                    MagicSacrificeTargetPicker.create(),
                    this,
                    "Sacrifice a creature. " +
                    "PN gains life equal to that creature's toughness."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final int toughness=creature.getToughness();
                    game.doAction(new MagicSacrificeAction(creature));
                    game.doAction(new MagicChangeLifeAction(event.getPlayer(),toughness));
                }
            });
        }
    }
]
