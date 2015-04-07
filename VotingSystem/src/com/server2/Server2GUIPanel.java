package com.server2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.object.Candidate;

public class Server2GUIPanel extends JPanel {
	JFrame topFrame;
	private List<Candidate> candidateList;
	private ArrayList<Candidate> winner;
	private ArrayList<Color> colors;
	private ArrayList<String> districts;
	JComboBox<String> districtComboBox;
	private int districtNum;
	private String selectedDistrict;
	
	public Server2GUIPanel(){
		
		candidateList = new ArrayList<Candidate>();
		colors = new ArrayList<Color>();
		winner = new ArrayList<Candidate>();
		districts = new ArrayList<String>();
		districtNum = 0;
		districtComboBox = new JComboBox<String>();
        districtComboBox.addActionListener(
        	new ActionListener(){
        		public void actionPerformed(ActionEvent e){
                	JComboBox<String> combo = (JComboBox<String>)e.getSource();
                    selectedDistrict = (String)combo.getSelectedItem();
                }
        	} 
        	
        );
		
		add(districtComboBox);
		
		colors.add(Color.blue);
		colors.add(Color.green);
		colors.add(Color.yellow);
		colors.add(Color.orange);
		colors.add(Color.pink);
		colors.add(Color.red);
		colors.add(Color.DARK_GRAY);
		colors.add(Color.MAGENTA);
		colors.add(Color.BLACK);
		colors.add(Color.blue);
		colors.add(Color.pink);
		colors.add(Color.red);
	}
	
	public void setData(List<Candidate> candidateList){
		this.candidateList = candidateList;
	}
	
	public void setWinner(ArrayList<Candidate> winner){
		this.winner = winner;
	}
	
	public void setDistricts(ArrayList<String> districts){
		//only update the combo box if a new district has been created
		if(districts.size() > districtNum){
			districtNum = districts.size();
			this.districts = districts;
			districtComboBox.removeAllItems();
			districtComboBox.addItem("All Districts");
			for(int i = 0; i<districts.size(); i++){
				districtComboBox.addItem(districts.get(i));
			}
		}
	}
	
	public String getSelectedDistrict(){
		return selectedDistrict;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		//scale
		g.setColor(Color.black);
		for(int i = 0; i<5; i++){
			//the number
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g.drawString(""+i*50, 15, 350-i*50);
			//the line
			g.drawLine(70, 350-i*50, candidateList.size()*100+70, 350-i*50);
		}
		
		//title
		g.setColor(Color.black);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.drawString("Election Results", 5, 50);
		//display winner
		if (winner.size()>1){
			String leaders = "";
			for(int i = 0; i<winner.size(); i++){
				leaders = leaders + winner.get(i).getFirstName() + " " + winner.get(i).getLastName();
				if (!(i+1==winner.size()))
					leaders = leaders + ", ";
			}
			g.drawString("Poll Leaders: " + leaders, 5, 75);
		}	
		else if(winner.size() == 1) g.drawString("Poll Leader: " + winner.get(0).getFirstName() + " " + winner.get(0).getLastName(), 5, 75);
			
		
		//vote bar graph
		for(int i = 0; i<candidateList.size(); i++){
			int count = candidateList.get(i).getPolls();
			//the bar
			g.setColor(colors.get(i));
			g.fillRect(i*100 + 70, 350, 75, -count);
			//the number above the bar
			g.setColor(Color.black);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g.drawString(""+count, i*100 + 100, 350 - count - 5);
			//legend
			g.drawString(candidateList.get(i).getFirstName(), i*100 + 70, 400);
			g.drawString(candidateList.get(i).getLastName(), i*100 + 70, 425);
		}
		
		
		
		
	}
}
