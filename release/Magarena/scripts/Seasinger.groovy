def TARGET_CREATURE_WITH_ISLAND = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasType(MagicType.Creature) &&
            target.getController().getNrOfPermanents(MagicSubType.Island) >= 1
    }
};

def NEG_TARGET_CREATURE_WITH_ISLAND = new MagicTargetChoice(
    TARGET_CREATURE_WITH_ISLAND,
    MagicTargetHint.Negative,
    "target creature whose controller controls an Island"
);
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_WITH_ISLAND,
                MagicExileTargetPicker.create(),
                this,
                "PN gains control of target creature whose controller controls an Island for as long as PN controls SN and SN remains tapped."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new AddStaticAction(
                    event.getPermanent(),
                    MagicStatic.ControlAsLongAsYouControlSourceAndSourceIsTapped(
                        event.getPlayer(),
                        it
                    )
                ));
            });
        }
    }
]
