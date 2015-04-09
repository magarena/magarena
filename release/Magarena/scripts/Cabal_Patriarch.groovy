def choice = new MagicTargetChoice("a creature card from your graveyard");

[
    // If 'Weaken' was used as an activation hint description, both abilities
    // would appear as a 'Weaken' choice with no idea of which would be
    // activated. 'Exile' describes the activation cost, to separate it from
    // the scripted ability which is also labelled 'Weaken'
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Exile" 
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}{B}"),
                new MagicExileCardEvent(source, choice)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(2,2),
                this,
                "Target creature\$ gets -2/-2 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicChangeTurnPTAction(it,-2,-2));
            });
        }
    }
]
