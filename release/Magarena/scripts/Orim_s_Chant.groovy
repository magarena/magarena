[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ can't cast spells this turn. If SN was kicked, creatures can't attack this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicChangePlayerStateAction(it, MagicPlayerState.CantCastSpells));
                if (event.isKicked()) {
                    CREATURE.filter(game) each {
                        game.doAction(new MagicGainAbilityAction(it, MagicAbility.CannotAttack));
                    }
                }
            });
        }
    }
]
