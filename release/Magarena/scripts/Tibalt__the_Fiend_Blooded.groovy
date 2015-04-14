[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card, then discards a card at random."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer()));
            game.addEvent(MagicDiscardEvent.Random(event.getSource(), event.getPlayer()));
        }
    },
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
                game.logAppendMessage(event.getPlayer()," ("+amount+")");
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
            CREATURE.filter(game) each {
                game.doAction(new GainControlAction(event.getPlayer(),it,MagicStatic.UntilEOT));
                game.doAction(new MagicUntapAction(it));
                game.doAction(new GainAbilityAction(it,MagicAbility.Haste));
            }
        }
    }
]
