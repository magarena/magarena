[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            game.doAction(new MagicChangeCountersAction(
                permanent,
                MagicCounterType.Charge,
                10,
                true
            ));		
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump,true),
        "Remove an ice counter"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{3}")];
        }

        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
			new MagicEvent(
				permanent,
				this,
				"Remove an ice counter from SN."
			);
        }


        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent perm = event.getPermanent()
            game.doAction(new MagicChangeCountersAction(
                perm,
                MagicCounterType.Charge,
                -1,
                true
            ));            
			perm.getCounters(MagicCounterType.Charge) =0 ?
				game.doAction(new MagicPlayTokensAction(
                event.getPlayer(), TokenCardDefinitions.get("Marit Lage"), 1)):
				NONE;  
		}
    }
]
