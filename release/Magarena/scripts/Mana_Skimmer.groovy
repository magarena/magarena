[
    new MagicWhenSelfDamagePlayerTrigger() {     
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {    
            final MagicPlayer player = damage.getTargetPlayer();
            return new MagicEvent(
                permanent,
                new MagicTargetChoice(
                    permanent.isController(player) ?
                        MagicTargetFilterFactory.permanent(MagicType.Land, MagicTargetFilterFactory.Control.You):
                        MagicTargetFilterFactory.permanent(MagicType.Land, MagicTargetFilterFactory.Control.Opp),
                    "target land ${player} controls"
                ),
                MagicTapTargetPicker.Tap,
                this,
                "Tap target land\$ ${player} controls. It doesn't untap during its controller's next untap step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new TapAction(it));
                game.doAction(ChangeStateAction.Set(
                    it,
                    MagicPermanentState.DoesNotUntapDuringNext
                ));
            });
        }
    }
]
