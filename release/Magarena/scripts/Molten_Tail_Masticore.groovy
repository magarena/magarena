def choice = new MagicTargetChoice("a creature card from your graveyard");

def EFFECT = MagicRuleEventAction.create("SN deals 4 damage to target creature or player.");

[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Discard a card?"),
                this,
                "PN may\$ discard a card. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent discard = new MagicDiscardEvent(event.getSource(), event.getPlayer());
            if (event.isYes() && discard.isSatisfied()) {
                game.addEvent(discard);
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    },
    new MagicPermanentActivation(
        [MagicConditionFactory.GraveyardAtLeast(1)],
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{4}"),
                new MagicExileCardEvent(source, choice)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return EFFECT.getEvent(source);
        }
    }
]
