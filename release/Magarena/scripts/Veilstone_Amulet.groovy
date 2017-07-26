[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return spell.isFriend(permanent) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Creatures PN controls can't be the targets of spells or abilities PN's opponents control this turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent event) {
            final int pIdx = event.getPlayer().getIndex();
            outerGame.doAction(new AddStaticAction(new MagicStatic(MagicLayer.Game, MagicStatic.UntilEOT) {
                @Override
                public void modGame(final MagicPermanent source, final MagicGame game) {
                    final player = game.getPlayer(pIdx);
                    CREATURE_YOU_CONTROL.filter(player) each {
                        it.addAbility(MagicAbility.CannotBeTheTarget(player.getOpponent()));
                    }
                }
            }));
        }
    }
]

