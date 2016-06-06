[
    new MagicPlaneswalkerActivation(-4) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                this,
                "SN deals damage equal to the number of cards in target player's\$ hand to that player."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount=it.getHandSize();
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
            });
        }
    },
    new MagicPlaneswalkerActivation(-6) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gains control of all creatures until end of turn. Untap them. They gain haste until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE.filter(event) each {
                game.doAction(new GainControlAction(event.getPlayer(),it,MagicStatic.UntilEOT));
                game.doAction(new UntapAction(it));
                game.doAction(new GainAbilityAction(it,MagicAbility.Haste));
            }
        }
    }
]
