package com.jsembly.extras;

import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position;
import javax.swing.text.Segment;

@SuppressWarnings("serial")
public class IntTextField extends JTextField {
	  public IntTextField(int defval, int size) {
	    super("" + defval, size);
	  }

	  protected Document createDefaultModel() {
	    return new IntTextDocument();
	  }

	  public boolean isValid() {
	    try {
	      Integer.parseInt(this.getText());
	      return true;
	    } catch (NumberFormatException e) {
	      return false;
	    }
	  }

	  public int getValue() {
	    try {
	      return Integer.parseInt(this.getText());
	    } catch (NumberFormatException e) {
	      return 0;
	    }
	  }
	  @SuppressWarnings("serial")
	class IntTextDocument extends PlainDocument implements Document {
	    public void insertString(int offs, String str, AttributeSet a)
	        throws BadLocationException {
	      if (str == null)
	        return;
	      String oldString = getText(0, getLength());
	      String newString = oldString.substring(0, offs) + str
	          + oldString.substring(offs);
	      try {
	        Integer.parseInt(newString + "0");
	        super.insertString(offs, str, a);
	      } catch (NumberFormatException e) {
	      }
	    }

		@Override
		public void addDocumentListener(DocumentListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addUndoableEditListener(UndoableEditListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Position createPosition(int arg0) throws BadLocationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Element getDefaultRootElement() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getLength() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Element[] getRootElements() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getText(int arg0, int arg1) throws BadLocationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void getText(int arg0, int arg1, Segment arg2)
				throws BadLocationException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void remove(int arg0, int arg1) throws BadLocationException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeDocumentListener(DocumentListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeUndoableEditListener(UndoableEditListener arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void render(Runnable arg0) {
			// TODO Auto-generated method stub
			
		}
	  }
}
