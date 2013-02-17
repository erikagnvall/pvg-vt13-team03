package sorting;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

import racer.Racer;
import racer.RacerClass;

public class ResultWriter {
	private BufferedWriter writer;
	private Competition data;
	private String fileName;
	private String header;
	private Comparator<Racer> comp;
	
	// TODO: klass utan modifierbart tillstånd?
	public ResultWriter(Competition data, String filename, Comparator<Racer> comp) {
		this.data = data;
		this.fileName = filename;
		this.header = "StartNr; Namn; TotalTid; StartTider; Måltider";
		this.comp = comp;
	}
	
	/**
	 * Loads and sorts racers before printing them to a file. Actual formatting
	 * is found in Racer class.
	 */
	public void writeToFile(int laps) {
		Set<RacerClass> classes = data.getClassTypes();
		
		try {
			writer = new BufferedWriter(new FileWriter(fileName));
			
			for (RacerClass racerClass : classes) {
				writeClassTypeToFile(racerClass, laps);
			}
			
			writer.close();
		} catch (IOException e) {
			System.err.println("File " + fileName + " could not be written");
		}
	}

	private void writeClassTypeToFile(RacerClass racerClass, int laps) throws IOException {
		Set<Racer> racers = data.getRacers(racerClass, comp);
		
		if (racerClass.toString().length() > 0 ){
			writer.write(racerClass.toString());
			writer.newLine();
		}
		
		//Ser till så att den inte skriver ut fler varv än nödvändigt
		int maxLapCount=0;
		for (Racer racer : racers) {
			if(maxLapCount<racer.getNumberOfLaps()){
				maxLapCount=racer.getNumberOfLaps();				
			}
		}
		
		if(maxLapCount>0 && maxLapCount<laps){
			laps=maxLapCount;
		}
		
		if (laps < 2) {
			writer.write(header);
		}
		else {
			writer.write("StartNr; Namn; #Varv; TotalTid; ");
			
			for (int i = 1; i <= laps; i++) {
				writer.write("Varv" + i + "; ");
			}
			
			writer.write("Start; ");
			
			for (int i = 1; i < laps; i++) {
				writer.write("Varvning" + i + "; ");
			}
			
			writer.write("Mål");
		}
		
		writer.newLine();
		for (Racer racer : racers) {
			writer.write(racer.toString(laps));
			writer.newLine();
		}
	}
}