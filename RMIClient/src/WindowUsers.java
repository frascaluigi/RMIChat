

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;



public class WindowUsers extends JFrame{
	
	private static final long serialVersionUID = 2366999729813388025L;

	private Applicazione applicazione;
	
	private JPanel windowDisplay = new JPanel();
	
	private LinkedList<String> listUsers = new LinkedList<String>();
	
	
	private DefaultListModel<String> model;
	
	private JList<String> list;
	
	private String username;
	
	

	private void initUI(){
		
		this.setTitle("["+ username +"] - Utenti Connessi ");
		
		this.setSize(250, 450);
		
		this.setLocation(100, 50);
		
		this.setResizable(false);
		
		
		JPanel iconPanel = new JPanel();
		ImageIcon image = new ImageIcon("img/utente.jpg");
		iconPanel.add(new JLabel(image));
		
		JPanel dock = new JPanel();
		dock.setLayout(new BorderLayout());
		dock.setBackground(Color.GRAY);
		dock.setVisible(true);
		
		
		
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		

		dock.add(iconPanel,BorderLayout.WEST);
		dock.add(new JLabel("Benvenuto  "+this.username),BorderLayout.EAST);
		
		contentPane.add(dock,BorderLayout.NORTH);
		contentPane.add(drawGUI(),BorderLayout.CENTER);
		
		//contentPane.add(WindowDisplay,BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				WindowUsers.this.applicazione.exit();
			}
		});
		

		this.setVisible(false);
	}
	
	private JPanel drawGUI(){
		
		windowDisplay.setLayout(new BorderLayout());
		
		this.model.clear();

		for(String user : listUsers) this.model.addElement(user);
		
		list = new JList<String>(model);

		list.addMouseListener(new ClicOpenChat(list));
		
		JScrollPane scrollPane = new JScrollPane(list);
		
		windowDisplay.add(scrollPane);
		
		return windowDisplay;
		
	}
	
	private void updateGUI(){
		
		windowDisplay.removeAll();
		
		drawGUI();
		
		windowDisplay.repaint();
		
		windowDisplay.revalidate();
		
	}
	
	public WindowUsers(Applicazione applicazione, String userName, LinkedList<String> list ) {
		
		this.applicazione = applicazione;
		
		this.model = new DefaultListModel<String>();
		
		this.username = userName;
		
		this.listUsers =  list;
		this.listUsers.remove(username);
		
		initUI();
		
	}
	
	public void updateUsersOn(LinkedList<String> list){
		
		System.out.println("updateUsersOn() WindowUsers");
		
		listUsers = list;
		this.listUsers.remove(username);
		
		updateGUI();
	
	}
	
	
	
	
	/**************************/
	
	private abstract class DubleClic extends MouseAdapter{
		  
		protected JList<Object> list;
		    
		public DubleClic(JList l){
		   list = l;
		}

		public abstract void DBClic(int index, Object item, ListModel<Object> listModel );
		
		public void mouseClicked(MouseEvent e){
		   if(e.getClickCount() == 2){
		     int index = list.locationToIndex(e.getPoint());
		     ListModel<Object> listModel = list.getModel();
		     Object item = listModel.getElementAt(index);
		     
		     list.ensureIndexIsVisible(index);   // da vedere
		     
		     DBClic(index, item, listModel);
		   }
		}
	}
	
	private class ClicOpenChat extends DubleClic{

		public ClicOpenChat(JList<String> list) {
			super(list);
		}

		@Override
		public void DBClic(int index, Object item, ListModel<Object> listModel) {
			
			String dest = (String) item.toString();
			System.out.println(dest);
			WindowUsers.this.applicazione.openChat(dest);
		}
	}
	
}
