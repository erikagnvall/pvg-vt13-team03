package racer;

import java.util.ArrayList;
import java.util.List;

public class Distance {
	private List<RacerTime> startTimes, finishTimes;
	private RacerTime minTime;

	public Distance() {
		startTimes = new ArrayList<RacerTime>();
		finishTimes = new ArrayList<RacerTime>();
		minTime = new RacerTime("00.15.00");
	}
	
	public String toString(){
		String start = getStartTime();
		String finish = getFinishTime();
		
		return startTimes.get(0).getDifferenceTo(finishTimes.get(0)) + "; " + start + "; " + finish;	
	}

	public String getStartTime() {
		int startTimesSize = startTimes.size();
		if (startTimesSize == 0){
			return "Start?";
		} else {
			return startTimes.get(0).toString();
		}
	}
	public String getFinishTime() {
		int finishTimesSize = finishTimes.size();
		if (finishTimesSize == 0){
			return "Slut?";
		} else {
			return finishTimes.get(0).toString();
		}
	}
	public void addStartTime(String string) {
		startTimes.add(new RacerTime(string));
	}
	
	public void addFinishTime(String string) {
		finishTimes.add(new RacerTime(string));
	}

	public RacerTime getLapTime(){
		RacerTime lapTime = startTimes.get(0).computeLapTime(finishTimes.get(0));
		return lapTime;
	}
	
	public String possibleMultipleStartTimes(){
		StringBuilder sb = new StringBuilder();
		if (startTimes.size() > 1) {
			sb.append("; Flera starttider?");
			for (int i = 1; i < startTimes.size(); i++) {
				sb.append(" ");
				sb.append(startTimes.get(i));
			}
		}
		return sb.toString();
	}
	
	public String possibleMultipleFinishTimes(){
		StringBuilder sb = new StringBuilder();
		if (finishTimes.size() > 1) {
			sb.append("; Flera måltider?");
			for (int i = 1; i < finishTimes.size(); i++) {
				sb.append(" ");
				sb.append(finishTimes.get(i));
			}
		}
		return sb.toString();
	}
	/** @return Error messag if finishTime is "--.--.--" */
	public String possibleImpossibleTotalTime(){
		RacerTime lapTime = getLapTime();
		if(!lapTime.equals("--.--.--") && (lapTime).compareTo(minTime)<0){
			return("; Omöjlig Totaltid?");
		}
		return "";
	}
}
