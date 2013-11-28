[
    new MagicPlaneswalkerActivation(3) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_NONCREATURE,
                new MagicDestroyTargetPicker(true),
                this,
                "Destroy target noncreature permanent\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDestroyAction(creature));
                }
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
                final MagicPermanent perm ->
                game.doAction(new MagicGainControlAction(
                    event.getPlayer(),
                    perm
                ));
            } as MagicPermanentAction);
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
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage = new MagicDamage(event.getSource(), target, 7);
                    final MagicPlayer player = damage.getTargetPlayer(); 
                    game.doAction(new MagicDealDamageAction(damage));
                    game.addEvent(new MagicDiscardEvent(event.getSource(), player, 7));
                    for (int i=7;i>0;i--) {
                        game.addEvent(new MagicSacrificePermanentEvent( event.getSource(), player, MagicTargetChoice.SACRIFICE_PERMANENT ));
                    }
                }
            });
        }
    }
]
