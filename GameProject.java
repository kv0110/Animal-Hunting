/*
Kavyamridula Venkatesan
4/24/23
GameProject.java
Creates project.
*/

import java.awt.*; import java.awt.event.*;
import javax.swing.*; import javax.swing.event.*;
import java.io.* ; // for classes File, IOException, ImageIO
import java.io.FileWriter; import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.io.File; import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

class BasicBackgroundPanel extends JPanel { //sets background as an image
    private Image background;
 
    public BasicBackgroundPanel(Image background) { //sets background to passed in image
        this.background = background;
        setLayout(new BorderLayout());
    }
 
    //Override
    protected void paintComponent(Graphics g) { //draws background with passed in image
        super.paintComponent(g);
        g.drawImage(background, 0, 0, 700, 633, null); // image scaled
    }
 
    //Override
    public Dimension getPreferredSize() {//gets dimensions of passed in image
        return new Dimension(background.getWidth(this), background.getHeight(this));
    }
}

public class GameProject {	//project class contains all panels and cards
	JPanel panel, front, panel1, bg, tp, showLevs, instruct, levPanels[]; //ti
	JLabel title, enterName, welcome;
	JTextField name;
	static JTextArea jt;
	CardLayout cardLayout;
	static String username;
	TPanel ti;
	LPanel lp;
	WinPanel wp;
	LosePanel pl;
	UserPanel TBG;
	MenuPanel menu;
	IntroPanel ip;
	LeaderBoardPanel lbp;
	FinalPanel last;
	PausePanel ps;
	int ct;
	boolean[] levChains;
	static int totalScore;
	String[] levelPanelNames;
	static int currentLevel;
	static HashMap<String,Integer> userScoreMap;
    ValueComparator bvc;
    static TreeMap<String,Integer> sortedScoreMap;
	
	public GameProject() throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException { //constructor
		playSound("tunetank.com_5719_invincible_by_alex-gl.wav");
		userScoreMap = new HashMap<String,Integer>();
		bvc =  new ValueComparator(userScoreMap);
		sortedScoreMap = new TreeMap<String,Integer>(bvc);
		readLeaderBoardFile();
		
		username = "";
		levChains = new boolean[5];
		ct = 0;
		currentLevel = 0;
		levelPanelNames = new String[11]; //4
		levelPanelNames[1] = "Level1Pan";
		levelPanelNames[2] = "Level2Pan";
		levelPanelNames[3] = "Level3Pan";
		levelPanelNames[4] = "Level4Pan";
		levelPanelNames[5] = "Level5Pan";
		levelPanelNames[6] = "Level6Pan";
		levelPanelNames[7] = "Level7Pan";//
		levelPanelNames[8] = "Level8Pan";//
		levelPanelNames[9] = "Level9Pan";//
		levelPanelNames[10] = "Level10Pan";//
		
		levPanels = new JPanel[11];
		
		JFrame frame = new JFrame("GameProject.java");
		frame.setSize(700, 633);
		Practice pc = new Practice();
		frame.setContentPane(pc);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public static void main(String[] args) throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException { //main method
		GameProject gp = new GameProject();
	}
	
	class ValueComparator implements Comparator<String> { //compares scores, sorts from highest to lowest within sorted map
	    Map<String, Integer> base;
	    public ValueComparator(Map<String, Integer> base) {
	        this.base = base;
	    }
	  
	    public int compare(String a, String b) {
	        if (base.get(a).equals(base.get(b))) {
	            return a.compareTo(b);
	        }
	        else {
	            return base.get(b)-base.get(a);
	        }
	    }
	}
	
	public static void readLeaderBoardFile() { //reads leaderboard file at beginning of game and instantiates map
		File rFile = new File("leaderboard.txt");
		Scanner sn = null;
		try {
			sn = new Scanner(rFile);
		}
		catch (FileNotFoundException e) { 
			return;
		}
		while (sn.hasNext()) {
			String username = sn.next();
			String score = sn.next();
			userScoreMap.put(username, Integer.parseInt(score));
		}
		sn.close();
		sortedScoreMap.putAll(userScoreMap);
		//System.out.println("Not Sorted: " + userScoreMap);
		//System.out.println("Sorted: " + sortedScoreMap);
	}
	
	public static void addToLeaderboard(int score) { //updates map and score of current player
		Integer userScore = userScoreMap.get(username);
		if (userScore == null) {
			userScoreMap.put(username, score);
		}
		else {
			if (userScore < score)
			userScoreMap.put(username, score);
		}
		sortedScoreMap.clear();
		sortedScoreMap.putAll(userScoreMap);
		String fileName = "leaderboard.txt";
		File wFile = new File(fileName);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(wFile);
		} 
		catch (IOException e) {
			System.err.println("Cannot write to " + fileName);
			System.exit(1);
		}
		for (String u : sortedScoreMap.keySet()) {
			pw.println(u + " " + sortedScoreMap.get(u));
		}
		pw.close();
		System.out.println("Not Sorted: " + userScoreMap);
		System.out.println("Sorted: " + sortedScoreMap);
	}
	
	public void playSound(String soundFile) throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException {
	    File f = new File("./" + soundFile);
	    AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());  
	    Clip clip = AudioSystem.getClip();
	    clip.open(audioIn);
	    
	    try {
	    	clip.start();
	    	clip.loop(clip.LOOP_CONTINUOUSLY);
	    }
	    catch(Exception e){
	    	System.out.println("Cannot loop song");
	    }
	}

	class Practice extends JPanel { //where all panels are created
		public Practice() {
			runIt();
		}
		
		public void paintComponent(Graphics g) {//calls constructor of class
			super.paintComponent(g);
		}
		
		public void runIt() {//runs card methods and adds them to main panel
			panel = new JPanel();
			panel.setOpaque(false);
			cardLayout = new CardLayout();
			panel.setLayout(cardLayout);
			
			titleScreen();
			userScreen();
			card1();
			levels(levChains);
			instructions();
			leaderboard();
			finalScreen();
			pauseScreen();

			panel.add(tp, "title");//ti
			panel.add(TBG, "fr");
		    panel.add(menu, "me");
		    panel.add(ip, "intro");
		    panel.add(lbp, "lead");
		    panel.add(showLevs, "levPanel");//lp
		    panel.add(ps, "pause");
		    levPanels[1] = addLevel(1, levelPanelNames[1]);
		    levPanels[2] = addLevel(2, levelPanelNames[2]);
		    levPanels[3] = addLevel(3, levelPanelNames[3]);
		    levPanels[4] = addLevel(4, levelPanelNames[4]);
		    levPanels[5] = addLevel(5, levelPanelNames[5]);
		    levPanels[6] = addLevel(6, levelPanelNames[6]);
		    levPanels[7] = addLevel(7, levelPanelNames[7]);//
		    levPanels[8] = addLevel(8, levelPanelNames[8]);//
		    levPanels[9] = addLevel(9, levelPanelNames[9]);//
		    levPanels[10] = addLevel(10, levelPanelNames[10]);//
		    panel.add(levPanels[1], levelPanelNames[1]);
		    panel.add(levPanels[2], levelPanelNames[2]);
		    panel.add(levPanels[3], levelPanelNames[3]);
		    panel.add(levPanels[4], levelPanelNames[4]);
		    panel.add(levPanels[5], levelPanelNames[5]);
		    panel.add(levPanels[6], levelPanelNames[6]);
		    panel.add(levPanels[7], levelPanelNames[7]);//
		    panel.add(levPanels[8], levelPanelNames[8]);//
		    panel.add(levPanels[9], levelPanelNames[9]);//
		    panel.add(levPanels[10], levelPanelNames[10]);//
		    panel.add(last, "final");
			
			//win panel
			wp = new WinPanel();
			JPanel wbPanel = new JPanel(new GridLayout(1,2));
			wbPanel.setOpaque(false);
			JButton back1 = new JButton("Back");
			JButton next = new JButton("Next");
			wbPanel.add(back1);
			back1.addActionListener(e -> cardLayout.show(panel, "levPanel"));
			wbPanel.add(next);
			wbPanel.setBounds(300,400,150,50);
			wp.add(wbPanel);
			panel.add(wp, "winP");
			
			//lose panel
			pl = new LosePanel();
			pl.setLayout(null);
			JPanel lbPanel = new JPanel(new GridLayout(1,2,50,0));
			lbPanel.setOpaque(false);
			
			ResetGame rg = new ResetGame();
			JButton playAg = new JButton("Play Again");
			playAg.addActionListener(rg);
			JButton back2 = new JButton("Back");
			back2.addActionListener(rg);
			lbPanel.add(playAg);
			lbPanel.add(back2);
			lbPanel.setBounds(220,370,250,40);
			pl.add(lbPanel);
		    panel.add(pl, "loseP");
		    
		    add(panel, BorderLayout.CENTER);
		}
		
		public void titleScreen() { //creates title screen (not done)
			ti = new TPanel();
			ti.setOpaque(false);
			
			JButton next2 = new JButton(new ImageIcon("next-removebg-preview-(1).png"));
			next2.setOpaque(false);
			next2.setBorderPainted(false);
			next2.setFocusPainted(false);
			Reset r2t = new Reset();
			next2.addActionListener(r2t);
			next2.addActionListener(e -> cardLayout.next(panel));
			
			tp = new JPanel();
			tp.setLayout(null);
		    tp.setOpaque(false);			
		    ti.setLayout(null);
		    next2.setBounds(270,515,150,66);
			ti.add(next2);
			ti.setBounds(0,0,700,633);
			tp.add(ti);
		}
	
		public void userScreen() { //asks for username		
			TBG = new UserPanel();
			TBG.setLayout(null);
			
			Reset rt = new Reset();
			JButton next = new JButton(new ImageIcon("next-removebg-preview(2).png"));
			next.setActionCommand("Next");
			next.setOpaque(false);
			next.setBorderPainted(false);
			next.setFocusPainted(false);
			next.addActionListener(rt);
			next.setBounds(290,420,105,32);
			TBG.add(next);
			
			next.addActionListener(e -> cardLayout.next(panel));
			enterName = new JLabel("ENTER YOUR USERNAME");
			enterName.setPreferredSize(new Dimension(700,300));
			enterName.setForeground(Color.WHITE);
			enterName.setHorizontalAlignment(SwingConstants.CENTER);
			enterName.setFont(new Font("Roboto", Font.BOLD, 30));
			enterName.setOpaque(false);
			name = new JTextField();
			name.setBounds(255,260,180,40);
			TBG.add(name);
			
			JButton west = new JButton();
			west.setPreferredSize(new Dimension(200,600));
			west.setOpaque(false);
			west.setContentAreaFilled(false);
			west.setBorderPainted(false);
			JButton east = new JButton();
			east.setPreferredSize(new Dimension(200,600));
			east.setOpaque(false);
			east.setContentAreaFilled(false);
			east.setBorderPainted(false);
		}
		
		public void card1() { //welcomes user, shows instructions+practice+play panels (not done)
			menu = new MenuPanel();
			panel1 = new JPanel(new BorderLayout());
			panel1.setOpaque(false);

			welcome = new JLabel();
			welcome.setHorizontalAlignment(SwingConstants.CENTER);
			welcome.setFont(new Font("Courier New", Font.BOLD, 30)); 
			welcome.setOpaque(false);
			welcome.setForeground(Color.WHITE);
			
			JPanel center = new JPanel(new GridLayout(5,1,0,50));
			center.setOpaque(false);
			C1 a1 = new C1();
			JButton btn1 = new JButton(new ImageIcon("instruct.png"));
			btn1.setActionCommand("Instructions");
			btn1.setPreferredSize(new Dimension(100,50));
			btn1.setOpaque(false);
			btn1.setContentAreaFilled(false);
			btn1.setBorderPainted(false);
			btn1.setFocusPainted(false);
			btn1.addActionListener(a1);
			JButton btn3 = new JButton(new ImageIcon("play.png"));
			btn3.setActionCommand("Play");
			btn3.setPreferredSize(new Dimension(100,50));
			btn3.setOpaque(false);
			btn3.setContentAreaFilled(false);
			btn3.setBorderPainted(false);
			btn3.setFocusPainted(false);
			btn3.addActionListener(a1);
			JButton lb = new JButton(new ImageIcon("leader.png"));
			lb.setActionCommand("Leaderboard");
			lb.setPreferredSize(new Dimension(100,50));
			lb.setOpaque(false);
			lb.setContentAreaFilled(false);
			lb.setBorderPainted(false);
			lb.setFocusPainted(false);
			lb.addActionListener(a1);
			
			JButton north = new JButton();
			north.setPreferredSize(new Dimension(200,20));
			north.setOpaque(false);
			north.setContentAreaFilled(false);
			north.setBorderPainted(false);
			north.setFocusPainted(false);
			JButton west = new JButton();
			west.setPreferredSize(new Dimension(200,600));
			west.setOpaque(false);
			west.setContentAreaFilled(false);
			west.setBorderPainted(false);
			west.setFocusPainted(false);
			JButton east = new JButton();
			east.setPreferredSize(new Dimension(200,600));
			east.setOpaque(false);
			east.setContentAreaFilled(false);
			east.setBorderPainted(false);
			east.setFocusPainted(false);
			JButton south = new JButton();
			south.setPreferredSize(new Dimension(700,75));
			south.setOpaque(false);
			south.setContentAreaFilled(false);
			south.setBorderPainted(false);
			south.setFocusPainted(false);

			center.add(welcome);
			center.add(btn1);
			center.add(btn3);
			center.add(lb);
			panel1.add(center, BorderLayout.CENTER);
			panel1.add(west, BorderLayout.WEST);
			panel1.add(east, BorderLayout.EAST);
			panel1.add(south, BorderLayout.SOUTH);
			panel1.add(north, BorderLayout.NORTH);
			menu.add(panel1);
		}
		
		public void instructions() { //creates instructions panel
			//make rectangles with text for instructions
			ip = new IntroPanel();
			ip.setLayout(null);
			
			JButton back = new JButton(new ImageIcon("back-removebg-preview (1).png"));
			back.setOpaque(false);
			back.setContentAreaFilled(false);
			back.setBorderPainted(false);
			back.setFocusPainted(false);
			back.setBounds(300,430,100,38);
			back.setActionCommand("Back");
			IntroBack ib = new IntroBack();
			back.addActionListener(ib);
			ip.add(back);
		}
		
		public void levels(boolean[]levC) {//panel that shows all levels, want to add scrollbar if more levels
			showLevs = new JPanel();
			showLevs.setLayout(null);
			
			lp = new LPanel();
			lp.setBounds(0,0,700,633);
			LevelLis ls = new LevelLis();
			
			JButton l1 = new JButton("Level 1");
			JButton l2 = new JButton("Level 2");
			JButton l3 = new JButton("Level 3");
			JButton l4 = new JButton("Level 4");//
			JButton l5 = new JButton("Level 5");//
			JButton l6 = new JButton("Level 6");//
			JButton l7 = new JButton("Level 7");//
			JButton l8 = new JButton("Level 8");//
			JButton l9 = new JButton("Level 9");//
			JButton l10 = new JButton("Level 10");//
			
			if (levC[0]==true) {
				l2 = new JButton();
				l2.setActionCommand("Level 2");
			}
			if(levC[1]==true) {
				l3 = new JButton();
				l3.setActionCommand("Level 3");
			}
			if(levC[2]==true) {
				l4 = new JButton();
				l4.setActionCommand("Level 4");
			}
			if(levC[3]==true) {
				l5 = new JButton();
				l5.setActionCommand("Level 5");
			}
			if(levC[4]==true) {
				l6 = new JButton();
				l6.setActionCommand("Level 6");
			}
			
			JButton backMenu = new JButton(new ImageIcon("image.png"));
			backMenu.setActionCommand("back");
			backMenu.setOpaque(false);
			backMenu.setContentAreaFilled(false);
			backMenu.setBorderPainted(false);
		
			l1.setOpaque(false);
			l1.setContentAreaFilled(false);
			l1.setBorderPainted(false);
			l2.setOpaque(false);
			l2.setContentAreaFilled(false);
			l2.setBorderPainted(false);
			l3.setOpaque(false);
			l3.setContentAreaFilled(false);
			l3.setBorderPainted(false);
			l4.setOpaque(false);
			l4.setContentAreaFilled(false);
			l4.setBorderPainted(false);
			l5.setOpaque(false);
			l5.setContentAreaFilled(false);
			l5.setBorderPainted(false);
			l6.setOpaque(false);
			l6.setContentAreaFilled(false);
			l6.setBorderPainted(false);
			l7.setOpaque(false);
			l7.setContentAreaFilled(false);
			l7.setBorderPainted(false);
			l8.setOpaque(false);
			l8.setContentAreaFilled(false);
			l8.setBorderPainted(false);
			l9.setOpaque(false);
			l9.setContentAreaFilled(false);
			l9.setBorderPainted(false);
			l10.setOpaque(false);
			l10.setContentAreaFilled(false);
			l10.setBorderPainted(false);
			
			l1.addActionListener(ls);
			l2.addActionListener(ls);
			l3.addActionListener(ls);
			l4.addActionListener(ls);//
			l5.addActionListener(ls);//
			l6.addActionListener(ls);//
			l7.addActionListener(ls);//
			l8.addActionListener(ls);//
			l9.addActionListener(ls);//
			l10.addActionListener(ls);//
			backMenu.addActionListener(ls);
			
			Font ft = new Font("Courier New", Font.BOLD, 25);
			l1.setFont(ft);
			l2.setFont(ft);
			l3.setFont(ft);
			l4.setFont(ft);
			l5.setFont(ft);
			l6.setFont(ft);
			l7.setFont(ft);
			l8.setFont(ft);
			l9.setFont(ft);
			l10.setFont(ft);
			
			l1.setBounds(85,50,200,50); //10 at 2nd parameter and +90 as goes down
			l2.setBounds(430,100,200,50); //100,
			l3.setBounds(85,150,200,50); //190, etc.
			l4.setBounds(430,200,200,50);
			l5.setBounds(85,250,200,50);
			l6.setBounds(430,300,200,50);
			l7.setBounds(85,350,200,50);
			l8.setBounds(430,400,200,50);
			l9.setBounds(85,450,200,50);
			l10.setBounds(430,500,200,50);
			backMenu.setBounds(7,0,45,45);
			
			showLevs.add(l1);
			showLevs.add(l2);
			showLevs.add(l3);
			showLevs.add(l4);
			showLevs.add(l5);
			showLevs.add(l6);
			showLevs.add(l7);
			showLevs.add(l8);
			showLevs.add(l9);
			showLevs.add(l10);
			showLevs.add(backMenu);
			
			showLevs.add(lp);
		}
		
		public JPanel addLevel(int level, String pName) {//adds game panels to every level
			//add to level 2 panel
			PigHunt ph = new PigHunt(cardLayout, panel, wp, pl, level, levPanels, levelPanelNames);
			JPanel lev = new JPanel(new BorderLayout());
			lev.add(ph, BorderLayout.CENTER);
			return(lev);
		}
		
		public void resetLevel(PigHunt pan) { //resets game when "play again" pressed
			//remove previous game panel and reset new game panel
			BorderLayout layout = (BorderLayout)levPanels[currentLevel].getLayout(); //gets layout of game panel
			levPanels[currentLevel].remove(layout.getLayoutComponent(BorderLayout.CENTER)); //removes panel in center of game panel
			levPanels[currentLevel].add(pan, BorderLayout.CENTER); //replaces panel with reset game panel to play again
		}
		
		public void leaderboard() {
			lbp = new LeaderBoardPanel();
			lbp.setLayout(null);
			
			JButton back = new JButton(new ImageIcon("back-removebg-preview (1).png"));
			back.setActionCommand("back");
			LeadBack lb = new LeadBack();
			back.addActionListener(lb);
			back.setBounds(300,540,100,38);
			back.setBackground(Color.GRAY);
			back.setOpaque(true);
			//back.setContentAreaFilled(false);
			back.setBorderPainted(false);
			back.setFocusPainted(false);
			lbp.add(back);
		}
		
		public void finalScreen() {
			last = new FinalPanel();
			last.setLayout(null);
			
			JButton back = new JButton(new ImageIcon("back-removebg-preview (1).png"));
			back.setOpaque(false);
			back.setContentAreaFilled(false);
			back.setBorderPainted(false);
			back.setFocusPainted(false);
			back.setActionCommand("back");
			FinalBack fb = new FinalBack();
			back.addActionListener(fb);
			back.setBounds(400,410,100,38);
			last.add(back);
		}
		
		public void pauseScreen() {
			ps = new PausePanel();
			ps.setLayout(null);
			JPanel pauseGrid = new JPanel(new GridLayout(3,1,0,50));
			pauseGrid.setOpaque(false);
			pauseGrid.setBounds(199,255,300,170);
			PauseListener pb = new PauseListener();
			
			JButton back = new JButton(new ImageIcon("back-removebg-preview (1).png"));
			back.setActionCommand("back");
			back.setOpaque(false);
			back.setContentAreaFilled(false);
			back.setBorderPainted(false);
			back.setFocusPainted(false);
			back.addActionListener(pb);
			pauseGrid.add(back);

			JButton resume = new JButton(new ImageIcon("resume-PhotoRoom.png-PhotoRoom(1).png"));
			resume.setActionCommand("resume");
			resume.setOpaque(false);
			resume.setContentAreaFilled(false);
			resume.setBorderPainted(false);
			resume.setFocusPainted(false);
			resume.addActionListener(pb);
			pauseGrid.add(resume);
			
			JButton end = new JButton(new ImageIcon("end.png"));
			end.setActionCommand("end game");
			end.setOpaque(false);
			end.setContentAreaFilled(false);
			end.setBorderPainted(false);
			end.setFocusPainted(false);
			end.addActionListener(pb);
			pauseGrid.add(end);
			
			ps.add(pauseGrid);
		}
		
		class ResetGame implements ActionListener { //reset level if play again, back to lev page if back
			public void actionPerformed(ActionEvent e) {
				String cm = e.getActionCommand();
				System.out.println(totalScore);
				PigHunt res = new PigHunt(cardLayout, panel, wp, pl, currentLevel, levPanels, levelPanelNames);
				resetLevel(res);
				
				if (cm.equals("Play Again")) {
					cardLayout.show(panel, levelPanelNames[currentLevel]);
					//PigHunt.time = 0;
				}
				else if (cm.equals("Back")) {
					cardLayout.show(panel, "levPanel");
				}
			}
		}
		
		class C1 implements ActionListener { //takes user to instructions, play, or back (from panel1 new destination)
			public void actionPerformed(ActionEvent e) {
				String cm = e.getActionCommand();
				if (cm.equals("Leaderboard")) {
					cardLayout.show(panel, "lead");
				}
				if (cm.equals("Play")) {
					cardLayout.show(panel, "levPanel");
				}
				else if (cm.equals("Instructions")){
					cardLayout.show(panel, "intro");
				}
			}
		}
		
		class LevelLis implements ActionListener { //takes user to instructions, play, or back (from panel1 new destination)
			public void actionPerformed(ActionEvent e) {
				String cm = e.getActionCommand();
				if (cm.equals("Level 2")) {
					currentLevel = 2;
				}
				else if (cm.equals("Level 1")) {
					currentLevel = 1;
				}
				else if (cm.equals("Level 3")) {
					currentLevel = 3;
				}
				else if (cm.equals("Level 4")) {
					currentLevel = 4;
				}
				else if (cm.equals("Level 5")) {
					currentLevel = 5;
				}
				else if (cm.equals("Level 6")) {
					currentLevel = 6;
				}
				else if (cm.equals("Level 7")) {
					currentLevel = 7;
				}
				else if (cm.equals("Level 8")) {
					currentLevel = 8;
				}
				else if (cm.equals("Level 9")) {
					currentLevel = 9;
				}
				else if (cm.equals("Level 10")) {
					currentLevel = 10;
				}
				
				if (currentLevel<11) {
					cardLayout.show(panel, levelPanelNames[currentLevel]);
				}
				
				if (cm.equals("back")) {
					cardLayout.show(panel, "me");
				}
			}
		}
		
		class Reset implements ActionListener { //stores entered username
			public void actionPerformed(ActionEvent evt) {
				username = name.getText();
				welcome.setText("Welcome " + username);
				if ((name.getText()).length()==0) {
					cardLayout.show(panel, "fr");
				}
			}
		}
		
		class IntroBack implements ActionListener { //listener class to go back from instructions page
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(panel, "me");
			}
		}
		
		class LeadBack implements ActionListener { //listener class to go back from leaderboard
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(panel, "me");
			}
		}
		
		class FinalBack implements ActionListener { //listener class to go from final page to leaderboard
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(panel, "lead");
			}
		}
		
		class PauseListener implements ActionListener { //listener class to go from final page to leaderboard
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				if (command.equals("back")) {
					//PigHunt.time = 0;
					PigHunt res = new PigHunt(cardLayout, panel, wp, pl, currentLevel, levPanels, levelPanelNames);
					resetLevel(res);
					cardLayout.show(panel, "levPanel");
				}
				else if (command.equals("resume")){
					cardLayout.show(panel, levelPanelNames[currentLevel]);
				}
				else if (command.equals("end game")) {
					cardLayout.show(panel, "final");
				}
			}
		}
	}
}

class PausePanel extends JPanel { //draws final page with total score
	public void paintComponent(Graphics g) {
		Image bg = new ImageIcon("pas.jpeg").getImage();
		g.drawImage(bg, 0, 0, 700, 650, null); 
		
		Color lightGray = new Color(211, 211, 211);
		g.setColor(Color.GRAY);
		g.fillRect(293,242,110,47);
		g.fillRect(272,317,153,47);
		g.fillRect(254,392,193,42);
		g.setColor(lightGray);
		g.fillRect(298,247,100,37);
		g.fillRect(277,322,143,37);
		g.fillRect(259,397,183,32);
	}
}

class FinalPanel extends JPanel { //draws final page with total score
	public void paintComponent(Graphics g) {
		Image bg = new ImageIcon("fin3.jpeg").getImage();
		g.drawImage(bg, 0, 0, 700, 650, null); 
		Font ft = new Font("Courier New", Font.PLAIN, 40);
		g.setFont(ft);
		Color gold = new Color(197, 179, 88);
		g.setColor(gold);
		g.drawString("Final Score: "+GameProject.totalScore,300,300);
		
		g.setColor(gold);
		g.fillRect(395,405,110,48);
		g.setColor(Color.BLACK);
		g.fillRect(400,410,100,38);
	}
}

class LeaderBoardPanel extends JPanel { //draws leader board
	public void paintComponent(Graphics g) {
		
		Image bg = new ImageIcon("MOSHED-2023-5-14-21-31-5.jpg").getImage(); //MOSHED-2023-5-14-21-22-25.jpg
		g.drawImage(bg, 0, 0, 700, 650, null); 
		g.setColor(Color.WHITE);
		g.fillRect(90,90,520,430);
		g.fillRect(295,535,110,48);
		g.setColor(Color.GRAY);
		g.fillRect(100,100,500,410);
		
		g.setColor(Color.WHITE);
		Font ft = new Font("Courier New", Font.PLAIN, 25);
		g.setFont(ft);
		
		int up = 180;
		g.drawString("Username            Score",160,130);
		for (String u : GameProject.sortedScoreMap.keySet()) {
			int numSpaces = 20;
			numSpaces-=u.length();
			String addSpaces = "";
			for (int i = 0; i<numSpaces; i++) {
				addSpaces+=" ";
			}
			g.drawString(u + addSpaces + GameProject.sortedScoreMap.get(u),160,up);
			up+=50;
		}
	}
}

class WinPanel extends JPanel { //buttons for "next" or "back" to levels
	public void paintComponent(Graphics g) {
		Image win = new ImageIcon("gameover.jpg").getImage();
		g.drawImage(win, 0, 0, 700, 650, null);
		
		Image winText = new ImageIcon("winText.png").getImage();//change line 687
		g.drawImage(winText, 25, 288, 630, 45, null); //298 for 2nd parameter
	}
}

class LosePanel extends JPanel { //buttons for "play again" or "back" to levels
	public void paintComponent(Graphics g) {
		Image lose = new ImageIcon("redScreen.jpeg").getImage();
		g.drawImage(lose, 0, 0, 700, 650, null);
		
		Image loseText = new ImageIcon("loseText.png").getImage();
		g.drawImage(loseText, 25, 288, 650, 60, null); //298 for 2nd parameter
	}
}

class IntroPanel extends JPanel { //draws instruction panel
	Timer textTimer;
	TextTime tt;
	int timerCount, ct;
	public IntroPanel() {
		tt = new TextTime();
		textTimer = new Timer(500,tt);
		timerCount = 0;
	}
	
	class TextTime implements ActionListener { //creates timer to create typewriter effect on words(doesn't work)
		public void actionPerformed(ActionEvent e) {
			timerCount++;
			if (timerCount%2==0) {
				ct = 0;
			}
			else {
				ct = 1;
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		Image userBg = new ImageIcon("MOSHED-2023-5-14-21-31-5.jpg").getImage();
		g.drawImage(userBg, 0, 0, 700, 650, null); 
		g.setColor(Color.WHITE);
		g.fillRect(90,90,520,430);
		g.setColor(Color.GRAY);
		g.fillRect(100,100,500,410);
		
		String t1 = "Objective: Score points to advance on";
		String t12 = "leaderboard";
		String t2 = "Press space to start before every level and";
		String t22 = "use arrow keys to navigate";
		String t3 = "Avoid spikes/fireballs and hit pigs to score";
		String t32 = "points";
		String t4 = "Release avatar above hole on other side to";
		String t42 = "proceed to next level";
		String t5 = "Good luck ";
		int up = 120;
		int right = 108;
				
		g.setColor(Color.WHITE);
		Font ft = new Font("Courier New", Font.PLAIN, 18);
		g.setFont(ft);
		//20 between broken lines, 50 between every instruction
		g.drawString(t1,right,up);
		g.drawString(t12,right,up+20);
		g.drawString(t2,right,up+70);
		g.drawString(t22,right,up+90);
		g.drawString(t3,right,up+140);
		g.drawString(t32,right,up+160);
		g.drawString(t4,right,up+210);
		g.drawString(t42,right,up+230);
		g.drawString(t5,right,up+280);	
		
		g.setColor(Color.WHITE);
		g.fillRect(295,426,110,44);
		g.setColor(Color.GRAY);
		g.fillRect(300,431,100,34);
	}
}

class UserPanel extends JPanel { //draw bg for username panel
	public void paintComponent(Graphics g) {
		Image enterName = new ImageIcon("user-PhotoRoom.png-PhotoRoom (1).png").getImage(); 
		Image userBg = new ImageIcon("MOSHED-2023-5-14-21-22-25.jpg").getImage();

		g.drawImage(userBg, 0, 0, 700, 650, null); 
		g.drawImage(enterName, 100, 130, 492, 43, null); 
		
		Color lightGray = new Color(211, 211, 211);
		Color darkGray = new Color(	90, 90, 90);
		g.setColor(darkGray);
		g.fillRect(285,415,115,42);
		g.setColor(lightGray);
		g.fillRect(290,420,105,32);
	}
}

class MenuPanel extends JPanel { //draw bg for menu panel
	public void paintComponent(Graphics g) {
		Image menuBg = new ImageIcon("MOSHED-2023-5-27-16-4-5.jpg").getImage(); 
		g.drawImage(menuBg, 0, 0, 700, 650, null); 
		
		Color lightGray = new Color(211, 211, 211);
		g.setColor(Color.GRAY);
		g.fillRoundRect(195,170,320,50,45,45);
		g.fillRoundRect(292,300,128,50,45,45);
		g.fillRoundRect(212,430,286,50,45,45);
		g.setColor(lightGray);
		g.fillRoundRect(200,175,310,40,45,45);
		g.fillRoundRect(297,305,118,40,45,45);
		g.fillRoundRect(217,435,276,40,45,45);
	}
}

class TPanel extends JPanel {//images for title page
	private Timer kTimer;
	KnifeTimer kt;
	int ktCount, kCheck;
	public TPanel() { //sets background color to black for title, creates timer for knife to blink
		setBackground(Color.BLACK);
		setLayout(null);
		kt = new KnifeTimer();
		kTimer = new Timer(200, kt);
		ktCount = 0;
	}
	
	class KnifeTimer implements ActionListener { //every 400 milliseconds knife blinks once
		public void actionPerformed(ActionEvent e) {
			ktCount++;
			if (ktCount%2==0) {
				kCheck = 1;
			}
			else {
				kCheck = 0;
			}
			repaint();
		}
	}
	
	public void paintComponent(Graphics g) { //draws title page
		super.paintComponent(g);
		
		Image titleBG = new ImageIcon("boxBG.jpeg").getImage();
		g.drawImage(titleBG, 0, 0, 710, 650, null);
		Image titleBG2 = new ImageIcon("MOSHED-2023-5-14-20-0-14.jpg").getImage();
		Image piggy = new ImageIcon("piggy-removebg-preview-transformed.png").getImage();
		Image knife2 = new ImageIcon("knifep-removebg-preview.png").getImage();
		
		if (kCheck==1) {
			g.drawImage(titleBG2, 0, 0, 720, 650, null);
			g.drawImage(piggy, 90, 267, 468, 261, null);
		}
		else {
			g.drawImage(titleBG, 0, 0, 720, 650, null);
			g.drawImage(piggy, 90, 267, 468, 261, null);
			g.drawImage(knife2, 235, 80, 228, 334, null);
		}
		if (ktCount==8) {
			kTimer.stop();
			g.drawImage(titleBG, 0, 0, 720, 650, null);
			g.drawImage(piggy, 90, 267, 468, 261, null);
			g.drawImage(knife2, 235, 80, 228, 334, null);
		}
		else {
			kTimer.start();
		}
		
		Image title = new ImageIcon("titleImage.png").getImage();
		g.drawImage(title, 25, 10, 650, 67, null);
		
		Color lightGray = new Color(211, 211, 211);
		g.setColor(Color.GRAY);
		g.fillRect(279,522,130,50);
		g.setColor(lightGray);
		g.fillRect(284,527,120,40);
	}
}

class LPanel extends JPanel {//background image for levels page
	public void paintComponent(Graphics g) {
		Image bg = new ImageIcon("honey.jpg").getImage();
		g.drawImage(bg, 0, 0, 700, 633, null);
		
		g.setColor(Color.WHITE);
		g.fillRoundRect(80,45,210,60,45,45);
		g.fillRoundRect(425,95,210,60,45,45);
		g.fillRoundRect(80,145,210,60,45,45);
		g.fillRoundRect(425,195,210,60,45,45);
		g.fillRoundRect(80,245,210,60,45,45);
		g.fillRoundRect(425,295,210,60,45,45);
		g.fillRoundRect(80,345,210,60,45,45);
		g.fillRoundRect(425,395,210,60,45,45);
		g.fillRoundRect(80,445,210,60,45,45);
		g.fillRoundRect(425,495,210,60,45,45);
		
		g.setColor(Color.GRAY);
		g.fillRoundRect(85,50,200,50,45,45);
		g.fillRoundRect(430,100,200,50,45,45);
		g.fillRoundRect(85,150,200,50,45,45);
		g.fillRoundRect(430,200,200,50,45,45);
		g.fillRoundRect(85,250,200,50,45,45);
		g.fillRoundRect(430,300,200,50,45,45);
		g.fillRoundRect(85,350,200,50,45,45);
		g.fillRoundRect(430,400,200,50,45,45);
		g.fillRoundRect(85,450,200,50,45,45);
		g.fillRoundRect(430,500,200,50,45,45);
	}
}

class PigHunt extends JPanel implements KeyListener, MouseListener, MouseMotionListener{ //game panel with all 3 levels
	Rectangle[] recs, precs, frecs;
	Rectangle r1, r2, r3, r4, start, end, cave, ps, r5, r6;
	int[] vYcoors;
	int[] hXcoors;
	int[] vXcoors;
	int[] hYcoors;
	int[] xcoors;
	int[] vDirec;
	int[] hDirec;
	int[] ycoors;
	int[] pxcoors;
	int[] pycoors;
	int[] pMils;
	int[] pxDirec, pyDirec;
	boolean[] grabbed;
	int[] fxcoors;
	int[] fycoors;
	int[] fMils;
	int fxDirec[];
	int fyDirec[];
	Timer[] pTimers;
	Timer[] fTimers;
	private Timer hTimer, vTimer, timer;
	VRecs vr;
	HRecs hr;
	int xc, yc, hit, score, lev;
	int caveX, caveY, ct;
	Image caveman;
	Color sR, eR;
	
	int recArrayLength, pArrayLength, fArrayLength, time;
	TimeLimit tL;
	CardLayout cl;
	JPanel win, lose, pan;
	JPanel[] allLevels;
	String[] levelNames;
	
	public PigHunt(CardLayout cl2, JPanel pn, JPanel win2, JPanel lose2, int level, JPanel[]levels, String[]levNames) { //initialize all field vars for game
		cl = cl2;
		win = win2;
		lose = lose2;
		pan = pn;
		lev = level;
		allLevels = new JPanel[levels.length];
		levelNames = new String[levels.length];
		
		for (int i = 0; i<levels.length; i++) {
			allLevels[i] = levels[i];
			levelNames[i] = levNames[i];
		}
		
		recs = new Rectangle[5];
		ct = 0;
		if (lev<10) {
			caveX = 95;
			caveY = 460;
		}
		else {
			caveX = 50;
			caveY=460;
		}
		hit = 0;
		sR = Color.GREEN;
		eR = Color.RED;
		score = 0;
		time = 0;
		
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		recArrayLength = 5;
		vYcoors = new int[recArrayLength];
		hXcoors = new int[recArrayLength];
		vXcoors = new int[recArrayLength];
		hYcoors = new int[recArrayLength];
		vDirec = new int[recArrayLength];
		hDirec = new int[recArrayLength];
		xcoors = new int[recArrayLength];
		ycoors = new int[recArrayLength];
		
		pArrayLength = 8;
		pxcoors = new int[pArrayLength];
		pycoors = new int[pArrayLength];
		precs = new Rectangle[pArrayLength];
		grabbed = new boolean[pArrayLength];
		if (lev==3 || lev==5 || lev==6 ||lev==9) {
			pMils = new int[pArrayLength/2];
			pxDirec = new int[pArrayLength/2];
			pyDirec = new int[pArrayLength/2];
			pTimers = new Timer[pArrayLength/2];
		}
		else if (lev==10) {
			pMils = new int[pArrayLength];
			pxDirec = new int[pArrayLength];
			pyDirec = new int[pArrayLength];
			pTimers = new Timer[pArrayLength];
		}
		
		if (lev<6) {
			fArrayLength = 4;
		}
		else {
			fArrayLength = 3; //make 2 for all or some levels instead of 3 to make little easier
			fxDirec = new int[fArrayLength];
			fyDirec = new int[fArrayLength];
		}
		fxcoors = new int[fArrayLength];
		fycoors = new int[fArrayLength];
		frecs = new Rectangle[fArrayLength];
		fMils = new int[fArrayLength];
		fTimers = new Timer[fArrayLength];
		
		vr = new VRecs();	
		vTimer = new Timer(10, vr);
		
		hr = new HRecs();
		hTimer = new Timer(10, hr);
		
		tL = new TimeLimit();
		timer = new Timer(1000, tL);
	}
	
	class FLis implements ActionListener {
		private int fi;
		public FLis(int fIndex) {
			fi = fIndex;
		}
 		public void actionPerformed(ActionEvent e) {
 			if (lev<6) {
				fycoors[fi]++;
				if (fycoors[fi]>495) {
					fycoors[fi] = 100;
					fxcoors[fi] = randXY();
				}
				
				if (frecs[0].intersects(cave) 
						|| frecs[1].intersects(cave) 
						|| frecs[2].intersects(cave)
						|| frecs[3].intersects(cave)) {
					repaint();
					hit = 2;
				}
 			}
 			else {
 				if (fxDirec[fi]==1) {
 					fxcoors[fi]++;
 				}
 				else {
 					fxcoors[fi]--;
 				}
 				if (fyDirec[fi]==1) {
 					fycoors[fi]++;
 				}
 				else {
 					fycoors[fi]--;
 				}
 				
 				if (fxcoors[fi]>597) { 
	 					fxcoors[fi] = 597;//151
	 					pickFireDirecs(fi);
	 				}
 				else if (fxcoors[fi]<101) {
 					fxcoors[fi] = 101;//549
 					pickFireDirecs(fi);
 				}
 				
 				if (lev<10) {
	 				if (fxcoors[fi]>539) { 
	 					fxcoors[fi] = 539;//151
	 					pickFireDirecs(fi);
	 				}
	 				else if (fxcoors[fi]<151) {
	 					fxcoors[fi] = 151;//549
	 					pickFireDirecs(fi);
	 				}
	 				if (fycoors[fi]>489) {
	 					fycoors[fi] = 489;//101
	 					pickFireDirecs(fi);
	 				}
	 				else if (fycoors[fi]<101) {
	 					fycoors[fi] = 101;//459
	 					pickFireDirecs(fi);
	 				}
 				}
 				else {
 					if (fi==0) {
 		 				if (fycoors[fi]>213) {
 		 					fycoors[fi] = 213;
 		 					pickFireDirecs(fi);
 		 				}
 		 				else if (fycoors[fi]<80) {
 		 					fycoors[fi] = 80;
 		 					pickFireDirecs(fi);
 		 				}
 					}
 					else if (fi==1) {
 						if (fycoors[fi]>356) {
 		 					fycoors[fi] = 356;
 		 					pickFireDirecs(fi);
 		 				}
 		 				else if (fycoors[fi]<223) {
 		 					fycoors[fi] = 223;
 		 					pickFireDirecs(fi);
 		 				}
 					}
 					else {
 						if (fycoors[fi]>509) {
 		 					fycoors[fi] = 509;
 		 					pickFireDirecs(fi);
 		 				}
 		 				else if (fycoors[fi]<366) {
 		 					fycoors[fi] = 366;
 		 					pickFireDirecs(fi);
 		 				}
 					}
 				}
 				
 				if (frecs[0].intersects(cave) 
						|| frecs[1].intersects(cave) 
						|| frecs[2].intersects(cave)) {
					repaint();
					hit = 2;
				}
 			}
			repaint();
	 		grabFocus();
		}
	}
	
	class PLis implements ActionListener { //makes half of the pigs move
		private int pi;
		public PLis(int pIndex) { //get index of moving pig after timer started
			pi = pIndex;
		}
		public void actionPerformed(ActionEvent e) {
			if (lev<10) {
				if (pxDirec[pi/2]==1) {
					pxcoors[pi]++;
				}
				else {
					pxcoors[pi]--;
				}
				if (pyDirec[pi/2]==1) {
					pycoors[pi]++;
				}
				else {
					pycoors[pi]--;
				}
				if (pxcoors[pi]>539) { 
					pxcoors[pi] = 539;//151
					pickDirecs(pi/2);
				}
				else if (pxcoors[pi]<151) {
					pxcoors[pi] = 151;//549
					pickDirecs(pi/2);
				}
				if (pycoors[pi]>489) {
					pycoors[pi] = 489;//101
					pickDirecs(pi/2);
				}
				else if (pycoors[pi]<101) {
					pycoors[pi] = 101;//459
					pickDirecs(pi/2);
				}
			}
			else {
				if (pxDirec[pi]==1) {
					pxcoors[pi]++;
				}
				else {
					pxcoors[pi]--;
				}
				if (pyDirec[pi]==1) {
					pycoors[pi]++;
				}
				else {
					pycoors[pi]--;
				}
				if (pxcoors[pi]>590) { 
					pxcoors[pi] = 590;//151
					pickDirecs(pi);
				}
				else if (pxcoors[pi]<101) {
					pxcoors[pi] = 101;//549
					pickDirecs(pi);
				}
				if (pycoors[pi]>509) {
					pycoors[pi] = 509;//101
					pickDirecs(pi);
				}
				else if (pycoors[pi]<80) {
					pycoors[pi] = 80;//459
					pickDirecs(pi);
				}
			}
			
			for (int i = 0; i<pxcoors.length; i++) { //check if avatar intersects pigs
				if (precs[i].intersects(cave)) {
					grabbed[i] = true;
					if (i%2==0 && (lev==3 || lev==5 || lev==6 ||lev==9)) {
						pTimers[i/2].stop();
					}
					else if (lev==10) {
						pTimers[i].stop();
					}
				}
			}
			repaint();
			grabFocus();
		}
	}
	
	class TimeLimit implements ActionListener { //handler for time displayed on screen 
		//(starts at 60 seconds, counts down to 0)
		public void actionPerformed(ActionEvent e) {
			time++;
			repaint();
		}
	}
	
	class VRecs implements ActionListener { //if vertical recs reach end of walls start again
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i<vYcoors.length; i++) { //make vert recs move
				if (vDirec[i]==1 && vYcoors[i]!=0) { //i%2==0
					vYcoors[i]++;
				}
				else { //lose 0s
					vYcoors[i]--;
				}
				//check if reached end of outer rectangle
				if (vYcoors[i]>459) {
					vYcoors[i] = 459;
					vDirec[i] = 2;
				}
				else if (vYcoors[i]<101) {
					vYcoors[i] = 101;
					vDirec[i] = 1;
				}
			}
			if ((lev<4 || lev>6) && lev!=10) {
				if (recs[0].intersects(cave) 
						|| recs[1].intersects(cave) 
						|| recs[2].intersects(cave)
						|| recs[3].intersects(cave)
						|| recs[4].intersects(cave)){
					repaint();
					hit = 2;
				}
			}
			repaint();
			grabFocus();
		}
	}
	
	private void printDebug(String fnName) { //prints coors of rectangles used for debugging
		System.out.println(fnName);
		System.out.println("Cave: " + cave.getX() + " " + cave.getY() + " " + cave.getMaxX() + " " + cave.getMaxY());
		System.out.println("Rectangle 1: " + recs[0].getX() + " " + recs[0].getY() + " " + recs[0].getMaxX() + " " + recs[0].getMaxY() + " " + recs[0].intersects(cave));
		System.out.println("Rectangle 2: " + recs[1].getX() + " " + recs[1].getY() + " " + recs[1].getMaxX() + " " + recs[1].getMaxY() + " " + recs[1].intersects(cave));
		System.out.println("Rectangle 3: " + recs[2].getX() + " " + recs[2].getY() + " " + recs[2].getMaxX() + " " + recs[2].getMaxY() + " " + recs[2].intersects(cave));
		System.out.println("Rectangle 4: " + recs[3].getX() + " " + recs[3].getY() + " " + recs[3].getMaxX() + " " + recs[3].getMaxY() + " " + recs[3].intersects(cave));
		System.out.println("Rectangle 5: " + recs[4].getX() + " " + recs[4].getY() + " " + recs[4].getMaxX() + " " + recs[4].getMaxY() + " " + recs[4].intersects(cave));
	}
	
	class HRecs implements ActionListener { //if horizontal rectangles reach end of walls start again
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i<hXcoors.length; i++) { //make horizontal recs move
				if (hDirec[i]==1 && hXcoors[i]!=0) { //i%2==0
					hXcoors[i]++;
				}
				else { //lose 0s in array
					hXcoors[i]--;
				}
				//check if reached end of outer rectangle
				if (hXcoors[i]>509) { 
					hXcoors[i] = 509;
					hDirec[i] = 2;
				}
				else if (hXcoors[i]<151) {
					hXcoors[i] = 151;
					hDirec[i] = 1;
				}
			}
			if ((lev<4 || lev>6) && lev!=10) {
				if (recs[0].intersects(cave) 
						|| recs[1].intersects(cave) 
						|| recs[2].intersects(cave)
						|| recs[3].intersects(cave)
						|| recs[4].intersects(cave)){
					repaint();
					hit = 2;
				}
			}
			repaint();
			grabFocus();
		}
	}
	
	public void paintComponent(Graphics g) { //paints first level of game with all components, score and timer
		Image bg;
		if (lev<4) {
			g.setColor(Color.WHITE);
			bg = new ImageIcon("boxBG.jpeg").getImage();
		}
		else if (lev<7) {
			g.setColor(Color.BLACK);
			bg = new ImageIcon("whBG.jpeg").getImage();
		}
		else if (lev<10) {
			g.setColor(Color.WHITE);
			bg = new ImageIcon("bricks.jpeg").getImage();
		}
		else {
			g.setColor(Color.WHITE);
			bg = new ImageIcon("fin2.jpeg").getImage();
		}
		
		g.drawImage(bg, 0, 0, 700, 633, null);
		
		Image pig2 = new ImageIcon("pig2-removebg-preview(1).png").getImage();
		Image blood = new ImageIcon("blood-removebg-preview.png").getImage();
		Image hSpikes = new ImageIcon("spikes-removebg-preview-(1).png").getImage();
		Image vKnife = new ImageIcon("image-removebg-preview.png").getImage();
		Image portal = new ImageIcon("hole3-removebg-preview.png").getImage();
		Image pressSpace = new ImageIcon("press(1)-PhotoRoom.png-PhotoRoom(1).png").getImage();
		Image pause = new ImageIcon("pause-removebg-preview(1).png").getImage();
		Image fireball = new ImageIcon("fire.png").getImage();
		
		Font ft = new Font("Roboto", Font.PLAIN, 15); 
		g.setFont(ft);
		g.drawString("Score: " + score, 630, 20);
		int timeGiven = 60;
		if (timeGiven-time>=10) { 
			g.drawString("Time Left: 00:" + (timeGiven-time), 570, 40);
		}
		else {
			g.drawString("Time Left: 00:0" + (timeGiven-time), 570, 40);
		}
		if (timeGiven-time==0) {//stops timer at 0 seconds
			timer.stop();
			
			if (!(cave.intersects(end))) {
				cl.show(pan, "loseP");
			}
		}
		
		g.drawString("Total Score: " + (score+GameProject.totalScore), 60, 20);
		
		g.setColor(Color.GRAY);
		if (ct==0) {
			ct = 1;
			int vh = 0;
		
			for (int i = 0; i<recArrayLength; i++) {
				xc = randXY();
				yc = randXY();
				
				if (lev==1 || lev==7) { //4
					vh=2;
				}
				else if (lev==2 || lev==8) { //5
					vh = 1;
				}
				else if (lev==3 || lev==9) { //6
					vh = randVH();
				}
			
				for (int j = 0; j<i+1; j++) {//need to fix, make sure rectangles of same side do not overlap
					if (xcoors[j]-xc<=20 || xcoors[j]-xc<=-20) {
						xc = randXY();
					}
					if (ycoors[j]-yc<=20 || ycoors[j]-yc<=-20) {
						yc = randXY();
					}
				}
			
				if (vh==1) { //vertical rec
					recs[i] = new Rectangle(xc, yc, 10, 50);
					vXcoors[i] = xc;
					vYcoors[i] = yc;
				}
				else if (vh==2) { //horizontal rec
					recs[i] = new Rectangle(xc, yc, 50, 10);
					hXcoors[i] = xc;
					hYcoors[i] = yc;
				}
				xcoors[i] = xc;
				ycoors[i] = yc;
			}
			
			for (int i = 0; i<pxcoors.length; i++) { //draws rectangles at coordinates of pigs
				int px = randXY();
				int py = randXY();
				pxcoors[i] = px;
				pycoors[i] = py;
				precs[i] = new Rectangle(px+5,py+5,10,10);
			}
			
			if (lev>3) {
				for (int i = 0; i<fxcoors.length; i++) { //draws rectangles at coordinates of fireballs
					int fx;
					if (lev<10) {
						fx = randFireXY();
						fycoors[i] = 100;
					}
					else {
						fx = randFX3();
						if (i==0) {
							fycoors[i] = 100;
						}
						else if (i==1) {
							fycoors[i] = 223;
						}
						else {
							fycoors[i] = 366;
						}
					}
					fxcoors[i] = fx;
					frecs[i] = new Rectangle(fx+4,fycoors[i]+2,14,10);
				}
			}
			
			for (int i = 0; i<recArrayLength; i++) { //make rec directions different
				vDirec[i] = randVH();
				hDirec[i] = randVH();
			}

			
			if (lev==3 || lev==5 || lev==6 ||lev==9 || lev==10) {
				for (int i = 0; i<pMils.length; i++) { //make pig speeds and directions different 
					pMils[i] = randMil();
					pxDirec[i] = randVH();
					pyDirec[i] = randVH();
				}
			
				for (int i = 0; i<pMils.length; i++) { //create new timer for every moving pig
					if (lev<10) {
						pTimers[i] = new Timer(pMils[i], new PLis(i*2));
					}
					else {
						pTimers[i] = new Timer(pMils[i], new PLis(i));
					}
					pTimers[i].start();
				}
			}
			
			if (lev>3) {
				for (int i = 0; i<fMils.length; i++) { //make fire speeds different 
					fMils[i] = randFireMil();
					if (lev>5) {
						fxDirec[i] = randVH();
						fyDirec[i] = randVH();
					}
				}
			
				for (int i = 0; i<fMils.length; i++) { //create new timer for every fireball
					fTimers[i] = new Timer(fMils[i], new FLis(i));
					fTimers[i].start();
				}
			}
		}
		
		if (lev==3 || lev==5 || lev==6 ||lev==9) {
			for (int i = 0; i<pxcoors.length; i+=2) { //draws rectangles at coordinates of moving pigs
				precs[i] = new Rectangle(pxcoors[i]+5,pycoors[i]+5,10,10);
			}
		}
		else if (lev==10) {
			for (int i = 0; i<pxcoors.length; i++) { //draws rectangles at coordinates of moving pigs
				precs[i] = new Rectangle(pxcoors[i]+5,pycoors[i]+5,10,10);
			}
		}
		
		if (lev>3) {
			for (int i = 0; i<fxcoors.length; i++) { //draws rectangles at coordinates of moving pigs
				frecs[i] = new Rectangle(fxcoors[i]+4,fycoors[i]+2,14,10);
			}
		}
		
		score = 0;
		for (int i = 0; i<pxcoors.length; i++) { //draws pigs
			if (grabbed[i]==false) {
				g.drawImage(pig2, pxcoors[i], pycoors[i], 20, 20, null);
			}
			else {
				g.drawImage(blood, pxcoors[i], pycoors[i], 20, 20, null);
				score++;
				if ((lev==3 || lev==5 || lev==6 ||lev==9) && i%2==0) {
					score++;
				}
				else if (lev==10) {
					score++;
				}
			}
		}
		
		if (lev>3) {
			for (int i = 0; i<fxcoors.length; i++) { //draws fireballs
				g.drawImage(fireball, fxcoors[i], fycoors[i], 17, 20, null);
			}
		}
		
		if (lev<10) {
			end = new Rectangle(560,100,50,50); //end rec (change to portal)
		}
		else {
			end = new Rectangle(610,90,50,50);
		}
		
		g.drawImage(pause, 9, 7, 45, 45, null);
		ps = new Rectangle(9,7,45,45);
		
		caveman = new ImageIcon("caveman-removebg-preview.png").getImage();
		if (hit==0) { //draw caveman (avatar)
			cave = new Rectangle(caveX+5, caveY+2, 30, 36);
			
			if (lev<10) {
				g.drawImage(portal, 60, 435, 110, 101, null); //closeDoor 65,90
				g.drawImage(portal, 530, 79, 110, 101, null);
			}
			else {
				g.drawImage(portal, 15, 440, 110, 101, null);
				g.drawImage(portal, 580, 65, 110, 101, null);
			}
		}
		else if (hit==1) {
			if (lev<10) {
				g.drawImage(portal, 60, 435, 110, 101, null); //closeDoor 65,90
				g.drawImage(portal, 530, 79, 110, 101, null);
			}
			else {
				g.drawImage(portal, 15, 440, 110, 101, null);
				g.drawImage(portal, 580, 65, 110, 101, null);
			}
			g.drawImage(caveman, caveX, caveY, 40, 40, null);
			g.setColor(Color.WHITE);
			cave = new Rectangle(caveX+5, caveY+2, 30, 36);
			
		}
		else {
			cl.show(pan, "loseP"); 
		}
		
		g.setColor(Color.GRAY);
		if ((lev<4 || lev>6) && lev!=10) {
			for (int i = 0; i<recArrayLength; i++) { //draw moving recs
				if (recs[i].getWidth()==10) {
					recs[i] = new Rectangle(vXcoors[i], vYcoors[i], 10, 50);
					g.drawImage(vKnife, vXcoors[i]-7, vYcoors[i], 25, 50, null);
				}
				else {
					recs[i] = new Rectangle(hXcoors[i], hYcoors[i], 50, 10);
					g.fillRect(hXcoors[i], hYcoors[i], 50, 10);
					g.drawImage(hSpikes, hXcoors[i], hYcoors[i]-4, 50, 20, null);
				}
			}
		}
		
		if (lev==1 || lev==4 || lev==7) {
			Color pG = new Color(193, 225, 193);
			g.setColor(pG);
		}
		else if (lev==2 || lev==5 || lev==8) {
			Color pY = new Color(255, 250, 160);
			g.setColor(pY);
		}
		else {
			Color pR = new Color(139, 0, 0);
			g.setColor(pR);
		}
		
		if (lev<10) {
			r1 = new Rectangle(150,100,410,10); //up
			g.fillRect(150,100,410,10);
			r2 = new Rectangle(550,150,10,360); //right
			g.fillRect(550,150,10,360);
			r3 = new Rectangle(150,500,410,10); //bottom
			g.fillRect(150,500,410,10);
			r4 = new Rectangle(150,100,10,360); //left
			g.fillRect(150,100,10,360);
		}
		else {
			r1 = new Rectangle(100,80,510,10); //up
			g.fillRect(100,80,510,10);
			r2 = new Rectangle(600,150,10,379); //right
			g.fillRect(600,150,10,379);
			r3 = new Rectangle(100,519,510,10); //bottom
			g.fillRect(100,519,510,10);
			r4 = new Rectangle(100,80,10,380); //left
			g.fillRect(100,80,10,380);
			r5 = new Rectangle(170,223,440,10); //higher line
			g.fillRect(170,223,440,10);
			r6 = new Rectangle(100,366,440,10); //lower line
			g.fillRect(100,366,440,10);
		}
		
		vTimer.start();
		hTimer.start();
		timer.start();
		if (hit==0 && lev==1) {
			g.drawImage(pressSpace, 50, 250, 600, 51, null);
		}
	}
 	
	public int randXY() { //returns random number between 170 and 450 as coordinates for different components
		int num = (int)(Math.random()*280+170);
		return num;
	}
	
	public int randFireXY() { //returns random number between 170 and 530 as coordinates for fire
		int num = (int)(Math.random()*360+170);
		return num;
	}
	
	public int randFX3() { //returns random number between 120 and 580 as coordinates for fire 
		int num = (int)(Math.random()*460+120);
		return num;
	}
	
	public int randVH() { //returns 1 = vertical rectangle, returns 2 = horizontal rectangle
		int num = (int)(Math.random()*2+1);
		return num;
	}
	
	public int randMil() { //returns milliseconds for pig timers
		int num = (int)(Math.random()*10+10);
		return num;
	}
	
	public int randFireMil() { //returns milliseconds for pig timers
		int num;
		if (lev<6) {
			num = (int)(Math.random()*6+7);
		}
		else {
			num = (int)(Math.random()*6+9);
		}
		return num;
	}
	
	public void pickDirecs(int pg) { //chooses direction of every moving pig
		for (int i = 0; i<pMils.length; i++) {
			pxDirec[pg] = randVH();
			pyDirec[pg] = randVH();
		}
	}
	
	public void pickFireDirecs(int fr) {
		for (int i = 0; i<fMils.length; i++) {
			fxDirec[fr] = randVH();
			fyDirec[fr] = randVH();
		}
	}
	
	public void keyTyped(KeyEvent e) {} // runs if a key is typed

	public void keyPressed(KeyEvent e) { //left,right,up,down key functions, checks if character gets hit by rectangles
		int key = e.getKeyCode();
		if (lev<10) {
			if (key==38 && caveY>102) { // UP
				caveY-=8;
			}
			else if (key==40 && caveY<460) { // DOWN
				caveY+=8;
			}
			else if (key==39) { // RIGHT
				if (!cave.intersects(r2)) {
					caveX+=8;
				}
				else {
					caveX-=8;	
				}
			}
			else if (key==37) { // LEFT
				if (!cave.intersects(r4)) {
					caveX-=8;
				}
				else {
					caveX+=8;	
				}
			}
		}
		else {
			if (key==38) {  // UP
				if (!(cave.intersects(r5) || cave.intersects(r6) || cave.intersects(r1))) {
					caveY-=8;
				}
				else {
					caveY+=8;
				}
			}
			if (key==40) { // DOWN
				if (!(cave.intersects(r5) || cave.intersects(r6) || cave.intersects(r3))) {
					caveY+=8;
				}
				else {
					caveY-=8;
				}
			}
			if (key==39) { // RIGHT
				if (!cave.intersects(r2)) {
					caveX+=8;
				}
				else {
					caveX-=8;	
				}
			}
			else if (key==37) { // LEFT
				if (!cave.intersects(r4)) {
					caveX-=8;
				}
				else {
					caveX+=8;	
				}
			}
		}
		if (key==32) { // SPACE
			hit=1;
		}
		
		if (cave.intersects(r1)) {
			caveY+=2;
			repaint();
		}
		else if (cave.intersects(r2)) {
			caveX-=2;
			repaint();
		}
		else if (cave.intersects(r3)) {
			caveY-=2;
			repaint();
		}
		else if (cave.intersects(r4)) {
			caveX+=2;
			repaint();
		}
		for (int i = 0; i<pxcoors.length; i++) { //check if avatar intersects pigs
			if (precs[i].intersects(cave)) {
				grabbed[i] = true;
				if (i%2==0 && (lev==3 || lev==5 || lev==6 ||lev==9)) {
					pTimers[i/2].stop();
				}
				else if (lev==10) {
					pTimers[i].stop();
				}
			}
		}
		repaint();
	}
	
	public void keyReleased(KeyEvent e) { //changes to next level
		if (cave.intersects(end)) {
			GameProject.currentLevel++;
			GameProject.totalScore+=score;
			//System.out.println(GameProject.totalScore);
			GameProject.addToLeaderboard(GameProject.totalScore);
			
			if (GameProject.currentLevel<11) {
				cl.show(pan, levelNames[GameProject.currentLevel]);
			}
			else {
				cl.show(pan, "final");
			}
		}
		repaint();
	}
	
	public void mouseClicked(MouseEvent e) { //checks if pause button clicked
		int xMouse = e.getX();
		int yMouse = e.getY();
		if (ps.contains(xMouse,yMouse)) {
			timer.stop();
			cl.show(pan,"pause");
		}
	}
	
	public void mousePressed(MouseEvent e){} //need for prog to run
	public void mouseReleased(MouseEvent e){} //need for prog to run
	public void mouseEntered(MouseEvent e){} //need for prog to run
	public void mouseExited(MouseEvent e){} //need for prog to run
	public void mouseDragged(MouseEvent e){} //need for prog to run
	public void mouseMoved(MouseEvent e){} //need for prog to run
}
