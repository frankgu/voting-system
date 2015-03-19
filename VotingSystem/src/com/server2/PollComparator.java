package com.server2;
import java.util.Comparator;

import com.object.Candidate;
public class PollComparator implements Comparator <Candidate>{
	public int compare(Candidate c1, Candidate c2){
		if (c1.getPolls()<c2.getPolls())
			return 1;
		else if (c1.getPolls()>c2.getPolls()){
			return -1;
		}
		else return 0;
	}
}
