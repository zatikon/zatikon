package leo.client;

import javax.swing.*;

public class KeycodeField extends JTextField {
    private final AccountFrame frame;

    public KeycodeField(AccountFrame newFrame) {
        super();
        frame = newFrame;
    }

    public void paste() {
        frame.paste(Client.getClipboard());
    }
}
