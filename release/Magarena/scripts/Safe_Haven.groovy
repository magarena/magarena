[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Exile"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_YOU_CONTROL,
                this,
                "Exile target creature you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ExileLinkAction(
                    event.getPermanent(),
                    it
                )); 
            });
        }
    },
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(            
                permanent,
                new MagicMayChoice("Sacrifice SN?"),
                this,
                "PN may\$ sacrifice SN. If you do, return each card exiled with SN to the battlefield under its owner's control"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
                game.doAction(new MagicReturnLinkedExileAction(
                    event.getPermanent(),
                    MagicLocationType.Play,
                    event.getPlayer()
                ));
            }
        }
    }
]
