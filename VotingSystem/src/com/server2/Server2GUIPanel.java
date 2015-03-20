package com.server2;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.object.Candidate;

public class Server2GUIPanel extends JPanel {
	
	private List<Candidate> votes;
	private ArrayList<Candidate> winner;
	private ArrayList<Color> colors;
	
	public Server2GUIPanel(){
		votes = new ArrayList<Candidate>();
		colors = new ArrayList<Color>();
		winner = new ArrayList<Candidate>();
		colors.add(Color.blue);
		colors.add(Color.green);
		colors.add(Color.yellow);
		colors.add(Color.orange);
		colors.add(Color.pink);
		colors.add(Color.red);
		colors.add(Color.DARK_GRAY);
		colors.add(Color.MAGENTA);
	}
	
	public void setData(List<Candidate> votes){
		this.votes = votes;
	}
	
	public void setWinner(ArrayList<Candidate> winner){
		this.winner = winner;
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
			g.drawLine(70, 350-i*50, votes.size()*100+70, 350-i*50);
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
			
		else g.drawString("Poll Leader: " + winner.get(0).getFirstName() + " " + winner.get(0).getLastName(), 5, 75);
		
		
		//vote bar graph
		for(int i = 0; i<votes.size(); i++){
			int count = votes.get(i).getPolls();
			//the bar
			g.setColor(colors.get(i));
			g.fillRect(i*100 + 70, 350, 75, -count);
			//the number above the bar
			g.setColor(Color.black);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g.drawString(""+count, i*100 + 100, 350 - count - 5);
			//legend
			g.drawString(votes.get(i).getFirstName(), i*100 + 70, 400);
			g.drawString(votes.get(i).getLastName(), i*100 + 70, 425);
		}
		
		
		
		
	}
}
