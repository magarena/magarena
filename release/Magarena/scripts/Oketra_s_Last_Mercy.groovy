[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN's life total becomes equal to PN's starting life total. " +
                "Lands PN controls don't untap during PN's next untap step."
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getStartingLife() - player.getLife();
            game.doAction(new ChangeLifeAction(player, amount));
            LAND_YOU_CONTROL.filter(player).each({
                game.doAction(ChangeStateAction.DoesNotUntapDuringNext(it, player));
            });
        }
    }
]

