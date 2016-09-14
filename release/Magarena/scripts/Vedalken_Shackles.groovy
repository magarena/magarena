def POWER_LESS_THAN_ISLANDS = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        final int amount = player.getNrOfPermanents(MagicSubType.Island);
        return target.hasType(MagicType.Creature) && target.getPower() <= amount;
    }
}

def TARGET_POWER_LESS_THAN_ISLANDS = new MagicTargetChoice(
        POWER_LESS_THAN_ISLANDS,
        MagicTargetHint.Negative,
        "target creature with power less than or equal to the number of Islands you control"
    );

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_POWER_LESS_THAN_ISLANDS,
                this,
                "PN gains control of target creature with power less than or equal to the number of Islands PN controls\$ "+
                "for as long as SN remains tapped."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new AddStaticAction(
                    event.getPermanent(),
                    MagicStatic.ControlAsLongAsSourceIsTapped(
                        event.getPlayer(),
                        it
                    )
                ));
            });
        }
    }
]
