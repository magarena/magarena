[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Draw a card, then discard a card at random."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicDrawAction(event.getPlayer(), 1));
            game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer(), 1, true));
        }
    },
    new MagicPlaneswalkerActivation(-4) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "SN deals damage equal to the number of cards in target player's\$ hand to that player."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final MagicDamage damage = new MagicDamage(event.getSource(), player, player.getHandSize());
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    },
    new MagicPlaneswalkerActivation(-6) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Gain control of all creatures until end of turn. Untap them. They gain haste until end of turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE);
            for (final MagicPermanent perm : targets) {
                game.doAction(new MagicGainControlAction(event.getPlayer(),perm,MagicStatic.UntilEOT));
                game.doAction(new MagicUntapAction(perm));
                game.doAction(new MagicSetAbilityAction(perm,MagicAbility.Haste));
            }
        }
    }
]
