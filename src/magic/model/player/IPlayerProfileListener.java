package magic.model.player;

public interface IPlayerProfileListener {
    void playerProfileUpdated(final PlayerProfile player);
    void playerProfileDeleted(final PlayerProfile player);
    void PlayerProfileSelected(final PlayerProfile player);
}
