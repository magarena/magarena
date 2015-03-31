[
    new MagicPlaneswalkerActivation(3) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_NONCREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target noncreature permanent\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
            });
        }
    },
    new MagicPlaneswalkerActivation(-2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
             return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target creature\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainControlAction(
                    event.getPlayer(),
                    it
                ));
            });
        }
    },
    new MagicPlaneswalkerActivation(-9) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(7),
                this,
                "SN deals 7 damage to target player\$. That player discards 7 cards, then sacrifices 7 permanents."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(), it, 7));
                game.addEvent(new MagicDiscardEvent(event.getSource(), it, 7));
                for (int i=7;i>0;i--) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(), 
                        it, 
                        MagicTargetChoice.SACRIFICE_PERMANENT
                    ));
                }
            });
        }
    }
]
