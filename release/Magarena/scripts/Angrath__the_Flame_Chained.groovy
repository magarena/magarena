[
    new MagicPlaneswalkerActivation(-3) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "PN untaps target creature\$ and gains control of it until end of turn. " +
                "That creature gains haste until end of turn. "+
                "Sacrifice it at the beginning of the next end step if it has converted mana cost 3 or less."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new GainControlAction(event.getPlayer(),it,MagicStatic.UntilEOT));
                game.doAction(new UntapAction(it));
                game.doAction(new GainAbilityAction(it,MagicAbility.Haste));
                if (it.getConvertedCost() <= 3) {
                game.doAction(new AddTriggerAction(it, AtEndOfTurnTrigger.Sacrifice));
                }
            });
        }
    },
    new MagicPlaneswalkerActivation(-8) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Each opponent loses life equal to the number of cards in his or her graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount=CARD_FROM_GRAVEYARD.filter(event.getSource().getOpponent()).size();
            game.logAppendValue(player,amount);
            game.doAction(new ChangeLifeAction(player.getOpponent(),-amount));
        }
    }
]
