[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player loses a third of his or her life, discards a " +
                "third of his or her cards, sacrifices a third of his or " +
                "her creatures, then sacrifices a third of his or her lands;" +
                " rounded up each time."
            );
        }

        private int oneThird(int value) {
            if (value <= 0) {
                // Don't lose negative life.
                return 0;
            } else {
                return (value + 2).intdiv(3).abs();
            }
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.getAPNAP() each {
                game.doAction(new ChangeLifeAction(
                    it,
                    -oneThird(it.getLife())
                ));
            }
            game.getAPNAP() each {
                game.addEvent(new MagicDiscardEvent(
                    event.getSource(),
                    it,
                    oneThird(it.getHandSize())
                ));
            }
            game.getAPNAP() each {
                final int numCreatures = CREATURE_YOU_CONTROL.filter(it).size();
                for (int i = 0; i < oneThird(numCreatures); i++) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        it,
                        SACRIFICE_CREATURE
                    ));
                }
            }
            game.getAPNAP() each {
                final int numLands = LAND_YOU_CONTROL.filter(it).size();
                for (int i = 0; i < oneThird(numLands); ++i) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        it,
                        SACRIFICE_LAND
                    ));
                }
            }
        }
    }
]
