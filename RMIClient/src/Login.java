import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Login extends JFrame{

	private Applicazione applicazione;
	
	private static int tentativi = 0;
	
	private JPanel display;
	private JTextField nomeField;
	private JPasswordField pwdField;
	private JLabel errorLable;
	private JButton okButton; 
	
	
	private void doLogin(){
		
		System.out.println("tentativo:"+ tentativi );
		++tentativi;
		
		System.out.println("Login:"+nomeField.getText().trim()+":"+pwdField.getPassword().toString().trim()+".");
		
		if( Login.this.applicazione.validate( nomeField.getText(), pwdField.getPassword().toString().trim() ) ) {
			Login.this.applicazione.setUserName(nomeField.getText().trim());
			Login.this.setVisible(false);
			Login.this.applicazione.openListUsers();
		}else{
			errorLable.setForeground(Color.RED);
			errorLable.setText("user o password errati");
		}
		
		if(tentativi>=3){
			Login.this.applicazione.exitOnErr();
		}
	}
	
	/*** pannelli ***/

	private JPanel centerPanel(){
		
		JPanel centerPanel = new JPanel();	
		nomeField = new JTextField();
		pwdField = new JPasswordField();
		errorLable = new JLabel();
		
		centerPanel.setLayout(new GridLayout(7, 1));
		
		NotNullDocumentListener listener = new NotNullDocumentListener();
		nomeField.getDocument().addDocumentListener(listener);
		pwdField.getDocument().addDocumentListener(listener);
		
		InvioKeyListener l = new InvioListener();
		nomeField.addKeyListener(l);
		pwdField.addKeyListener(l);
		
		centerPanel.add(new JLabel());					// riga vuota
		centerPanel.add(new JLabel("user"));
		centerPanel.add(nomeField);
		centerPanel.add(new JLabel("password"));
		centerPanel.add(pwdField);
		centerPanel.add(new JLabel());					// riga vuota
		centerPanel.add(errorLable);					
		
		return centerPanel;
	}
	
	private JPanel bottomPanel(){
		
		JPanel bottomPanel = new JPanel();
		okButton = new JButton("ENTRA");
		JButton cancelButton = new JButton("ANNULLA");
		
		okButton.setEnabled(false);
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Login.this.doLogin();
				return;
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Login.this.applicazione.exitOnErr();
			}
		});
		
		bottomPanel.add(okButton);
		bottomPanel.add(cancelButton);
		
		return bottomPanel;
	}
	
	private JPanel display(){
		display.setLayout(new BorderLayout());
	
		display.add(centerPanel(),BorderLayout.CENTER);
		display.add(bottomPanel(),BorderLayout.SOUTH);
		
		return display;
	}
	
	
	private void initUI(){

		setTitle("LOGIN");
		setSize(300, 250);
		setLocation(400, 200);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(display(),BorderLayout.CENTER);

		setVisible(true);
	}
	
	
	public Login(Applicazione applicazione) {
		super();
		this.applicazione = applicazione;
		this.display = new JPanel();
		initUI();
	}
	
	
	
	
	
	
	private class InvioListener extends InvioKeyListener{
		
		public InvioListener() { }
		
		private boolean campiVuoti(){
		
			return ( 
					(nomeField.getText() == null) || 
					(nomeField.getText().trim().equals("")) ||
					(pwdField.getPassword().toString() == null) ||
					(pwdField.getPassword().toString().trim().equals("")) 
				);
		}
		
		
		
		@Override
		protected boolean conditition() {
			return !campiVuoti();
		}

		@Override
		protected void doAction() {
			Login.this.doLogin();
		}
	}
	
	
	
	private class NotNullDocumentListener implements DocumentListener{
		
		private boolean campiVuoti(){
			return ( 
					(nomeField.getText() == null) || 
					(nomeField.getText().trim().equals("")) ||
					(pwdField.getPassword().toString()== null) ||
					(pwdField.getPassword().toString().trim().equals("")) 
				);
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			errorLable.setText("");
			if(!campiVuoti() )
				okButton.setEnabled(true);
			else 
				okButton.setEnabled(false);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			errorLable.setText("");
			if(!campiVuoti() )
				okButton.setEnabled(true);
			else 
				okButton.setEnabled(false);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			errorLable.setText("");
			if(!campiVuoti() )
				okButton.setEnabled(true);
			else 
				okButton.setEnabled(false);
		}
	}
}



