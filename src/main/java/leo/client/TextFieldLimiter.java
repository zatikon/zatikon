package leo.client;

// imports

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


class TextFieldLimiter extends PlainDocument {
    private static final long serialVersionUID = 1L;
    int maxChar = -1;

    public TextFieldLimiter(int len) {
        maxChar = len;
    }

    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str != null && maxChar > 0 && this.getLength() + str.length() > maxChar) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            return;
        }
        super.insertString(offs, str, a);
    }
}
