[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.TARGET_PERMANENT,
                MagicTapTargetPicker.TapOrUntap, 
                payedCost,
                this,
                "PN may tap or untap target permanent\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicMayChoice((it.isTapped() ? "Untap " : "Tap ") + it + "?"),
                    it,
                    {
                        final MagicGame G, final MagicEvent E ->
                        final MagicPermanent P = E.getRefPermanent();
                        if (E.isYes()) {
                            if (P.isTapped()) {
                                G.doAction(new MagicUntapAction(P));
                            } else {
                                G.doAction(new MagicTapAction(P, true))
                            }
                        }
                    },
                    "PN may\$ " + (it.isTapped() ? "untap" : "tap") + " RN."
                ));
            });
        }
    }
]
