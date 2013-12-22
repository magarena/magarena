[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    MagicTargetChoice.NEG_TARGET_NONCREATURE
                ),
                MagicDestroyTargetPicker.Destroy,
                this,
                "PN you may destroy target noncreature permanents. "+
                "If target permanent is put into a graveyard this way, "+
                "its controller puts a 3/3 green Elephant creature token onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent targetPermanent ->
                game.doAction(new MagicDestroyAction(targetPermanent));                
            });            
            if (targetPermanent.isInGraveyard() ||
            (targetPermanent.isToken() && !targetPermanent.getOwner().getPermanents().contains(target))) {
                game.doAction(new MagicPlayTokenAction(
                    targetPermanent.getController(),
                    TokenCardDefinitions.get("3/3 green Elephant creature token")
                ));                
            }            
        }
    },
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    MagicTargetChoice.NEG_TARGET_NONCREATURE
                ),
                MagicDestroyTargetPicker.Destroy,
                this,
                "PN you may destroy target noncreature permanents. "+
                "If target permanent is put into a graveyard this way, "+
                "its controller puts a 3/3 green Elephant creature token onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent targetPermanent ->
                game.doAction(new MagicDestroyAction(targetPermanent));                
            });            
            if (targetPermanent.isInGraveyard() ||
            (targetPermanent.isToken() && !targetPermanent.getOwner().getPermanents().contains(target))) {
                game.doAction(new MagicPlayTokenAction(
                    targetPermanent.getController(),
                    TokenCardDefinitions.get("3/3 green Elephant creature token")
                ));                
            }            
        }
    },
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {      
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    MagicTargetChoice.NEG_TARGET_NONCREATURE
                ),
                MagicDestroyTargetPicker.Destroy,
                this,
                "PN you may destroy target noncreature permanents. "+
                "If target permanent is put into a graveyard this way, "+
                "its controller puts a 3/3 green Elephant creature token onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent targetPermanent ->
                game.doAction(new MagicDestroyAction(targetPermanent));                
            });            
            if (targetPermanent.isInGraveyard() ||
            (targetPermanent.isToken() && !targetPermanent.getOwner().getPermanents().contains(target))) {
                game.doAction(new MagicPlayTokenAction(
                    targetPermanent.getController(),
                    TokenCardDefinitions.get("3/3 green Elephant creature token")
                ));                
            }            
        }
    }    
]
