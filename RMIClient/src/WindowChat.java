

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class WindowChat extends JFrame implements Chat {
	
	private static final Color BG = Color.GRAY;

	public static final int MESSAGE = 2;
	public static final int CRYPTO = 3;
	
	private JTextArea jTextAreaMessage = new JTextArea();
	private JScrollPane jScrollText;
	private JTextField jTextSendMessage = new JTextField(20);
	private JButton jSendButton = new JButton("Send");
	
	private InvioKeyListener invioListener;
	
	private BarraMenu barraMenu;
	
	private String dest;
	private String userName;
	private Applicazione applicazione;
	
	private boolean sendCrypt;

	
	/***   PANNELLI   ***/
	
	private JPanel messagePanel(){
		
		JPanel panel = new JPanel();
		
		jTextAreaMessage.setColumns(20);
        jTextAreaMessage.setEditable(false);
        jTextAreaMessage.setRows(5);
        jTextAreaMessage.setAutoscrolls(true);
        
        JPanel left = new JPanel();
        JPanel right = new JPanel();
		left.setBackground(BG);
        right.setBackground(BG);
        
        jScrollText = new JScrollPane(jTextAreaMessage);
        
        panel.setLayout(new BorderLayout());
        panel.add(jScrollText,BorderLayout.CENTER);
        panel.add(left,BorderLayout.WEST);
        panel.add(right,BorderLayout.EAST);
        
        return panel;
        
	}
	
	private JPanel sendPanel(){
		
		JPanel panel = new JPanel();
		panel.setBackground(BG);
		
		jSendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WindowChat.this.sendMessage();
			}
		});
		
		jTextSendMessage.addKeyListener(invioListener);
		
		panel.setLayout(new BorderLayout());
        panel.add(jTextSendMessage,BorderLayout.CENTER);
        panel.add(jSendButton, BorderLayout.EAST);
		
		return panel;
	}
	
	protected void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch(Throwable t) {
			// Vuoto
		}
	}

	protected void initUI(){
		this.setTitle("Conversazione con - "+this.dest);
		this.setSize(400, 360);
		this.setResizable(true);
		this.setLocation(200, 150);
		
		barraMenu = new BarraMenu();
		this.setJMenuBar(barraMenu);
		
		invioListener = new InvioListener();
		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(messagePanel(),BorderLayout.CENTER);
		contentPane.add(sendPanel(),BorderLayout.SOUTH);
		
		this.addWindowListener( new WindowAdapter() {
		    public void windowOpened( WindowEvent e ){
		    	jTextSendMessage.requestFocus();
		    }
		}); 
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				WindowChat.this.setVisible(false);
			}
		});
		
	//	setLookAndFeel();
		this.setVisible(true);
	}

	/***   COSTRUTTORE   ***/
	
	public WindowChat(String dest, String userName) {
		this.dest = dest;
		this.userName = userName;
		initUI();
	}
	
	
	public WindowChat(String dest, Applicazione applicazione, String userName) {
		this.dest = dest;
		this.applicazione = applicazione;
		this.userName = userName;
		initUI();
	}

	
	
	@Override
	public void showMessage(String mittente, String msg){
		jTextAreaMessage.append("["+mittente+"]: "+ msg + '\n');
		jScrollText.setAlignmentY(BOTTOM_ALIGNMENT);
	}
	
	@Override
	public void sendMessage(){
		
		String msg = jTextSendMessage.getText();
		jTextSendMessage.requestFocus();			/* il focus rimane sempre su jTextSendMessage  */
		
		if ( msg.trim().equals("") ) return; 
		
		jTextSendMessage.setText("");
		
		int type = this.sendCrypt ? CRYPTO : MESSAGE; 
		
		this.applicazione.sendMessageTo(type,this.dest, msg );
		
		showMessage(userName,msg);
	}
	
	
	
	private class InvioListener extends InvioKeyListener{
		
		private boolean invio;
		
		public InvioListener() {
			invio = true;
		}
		
		@SuppressWarnings("unused")
		public void setInvia(boolean b) {
			invio = b;
		}
		
		@Override
		protected boolean conditition() {
			return invio;
		}

		@Override
		protected void doAction() {
			WindowChat.this.sendMessage();
		}
	}
	
	
/***************************************************************
	public static void main(String args[]){
		WindowChat w = new WindowChat("marco", "giovanni");
		
	}
*/
	
	private class BarraMenu extends JMenuBar {
		
		JMenuItem fileInizio;
		JMenuItem fileFine;
		
		public BarraMenu() {
			
			JMenuItem fileChiudi = new JMenuItem("Chiudi conversazione");

			fileChiudi.setMnemonic(KeyEvent.VK_C);

			fileChiudi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent eventi) {
					BarraMenu.this.chiudiConv();
				}
			});
			
			JMenu fileMenu = new JMenu("File");
			fileMenu.setMnemonic(KeyEvent.VK_F);
			
			
			fileInizio = new JMenuItem("Iniza chat criptata");

			fileInizio.setMnemonic(KeyEvent.VK_I);

			fileInizio.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent eventi) {
					BarraMenu.this.cryptChat(true);
				}
			});
			
			fileFine = new JMenuItem("Fine chat criptata");
			
			fileFine.setMnemonic(KeyEvent.VK_N);

			fileFine.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent eventi) {
					BarraMenu.this.cryptChat(false);
				}
			});
			
			fileFine.setEnabled(false);
			
			JMenu cryptMenu = new JMenu("Chat Privata");
			fileMenu.setMnemonic(KeyEvent.VK_P);
			
			fileMenu.add(fileChiudi);
			cryptMenu.add(fileInizio);
			cryptMenu.add(fileFine);
			
			add(fileMenu);
			add(cryptMenu);
		}
		
		public void cryptChat(boolean crypt) {
			WindowChat.this.sendCrypt = crypt;
			fileInizio.setEnabled(!crypt);
			fileFine.setEnabled(crypt);
		}
		
		public void chiudiConv(){
			WindowChat.this.setVisible(false);
		}
	}
}
