package magic.ui.screen.interfaces;

import magic.ui.screen.widget.MenuButton;

import java.util.List;

public interface IActionBar {
    MenuButton getLeftAction();
    MenuButton getRightAction();
    List<MenuButton> getMiddleActions();
}
