def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game,new MagicPermanentAction() {
        public void doAction(final MagicPermanent permanent) {
            game.doAction(new MagicSacrificeAction(permanent));
        }
    });
} as MagicEventAction;

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostTapEvent(source,"{1}{U}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts a 2/2 blue Homunculus creature token " +
                "onto the battlefield, then sacrifices a creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Homunculus2")));
            game.addEvent(new MagicEvent(
                event.getSource(),
                player,
                MagicTargetChoice.SACRIFICE_CREATURE,
                MagicSacrificeTargetPicker.create(),
                action,
                "Choose a creature to sacrifice\$."
            ));
        }
    }
]
