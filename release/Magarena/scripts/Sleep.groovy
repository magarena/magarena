[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Tap all creatures target player\$ controls. Those creatures don't untap during their controller's next untap step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final Collection<MagicPermanent> targets = game.filterPermanents(it,CREATURE_YOU_CONTROL);
                for (final MagicPermanent creature : targets) {
                    game.doAction(new TapAction(creature));
                    game.doAction(ChangeStateAction.Set(
                        creature,
                        MagicPermanentState.DoesNotUntapDuringNext
                    ));
                }
            });
        }
    }
]
