[
    new MagicManaActivation(
        MagicManaType.ALL_TYPES,
        2
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent permanent) {
            return [
                    new MagicTapEvent(permanent) 
                ];    
        }
      @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
           return  new MagicEvent(
            permanent,
            MagicTargetChoice.TARGET_OPPONENT,
            this,
            "Target opponent\$ puts a 1/1 colorless Spirit creature token onto the battlefield."
         );
        }   
      @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.doAction(new MagicPlayTokenAction(
                  player,
                  TokenCardDefinitions.get("Spirit1")
               ));
                }
            });
         
        }
    }
]
