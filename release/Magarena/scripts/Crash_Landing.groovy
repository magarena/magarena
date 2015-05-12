[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_WITH_FLYING,
                new MagicLoseAbilityTargetPicker(MagicAbility.Flying),
                this,
                "Target creature with flying\$ loses flying until end of turn. "+
                "SN deals damage to that creature equal to the number of Forests PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPlayer player = event.getPlayer();
                final int amount = player.getNrOfPermanents(MagicSubType.Forest);
                game.logAppendMessage(player,"("+amount+")");
                game.doAction(new LoseAbilityAction(it,MagicAbility.Flying));
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
