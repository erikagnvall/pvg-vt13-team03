package sorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import racer.RacerFactory;
import racer.RacerRankingComparator;

/**
 * A class representing a sorter. It reads start.txt and finish.txt and outputs
 * result.txt but only for one driver.
 */
public class Sorter {
	private Competition racers;
	private SorterConfig config;

	public Sorter() throws IOException {
		initializeConfig();

		racers = new Competition(new RacerFactory(typeOfContest()));

		read();
		readNames();

		write();
	}

	/**
	 * Tries to load config from file, if file cannot be found we instead load
	 * the defaults and create a new file.
	 * 
	 * @throws IOException
	 */
	private void initializeConfig() throws IOException {
		config = new SorterConfig();

		try {
			config.load();
		} catch (FileNotFoundException e1) {
			config = new SorterConfig();
			config.setDefaults();
			config.store();
		} catch (EmptyConfigurationFileException e1) {
			config = new SorterConfig();
			config.setDefaults();
			config.store();
		}
	}

	private String typeOfContest() {
		return (String) config.get("ContestType");
	}

	private int laps() {
		return Integer.parseInt((String) config.get("NumberOfLaps"));
	}

	private String namefile() {
		return (String) config.get("Namefile");
	}

	/**
	 * Returns a list with the filenames that the sorter will read goal times
	 * from.
	 * 
	 * @return A list with the goal times.
	 */
	private ArrayList<String> finishFiles() {
		return getPropertyMultipleEntries("FinishFiles");
	}

	private ArrayList<String> startFiles() {
		return getPropertyMultipleEntries("StartFiles");
	}

	private ArrayList<String> getPropertyMultipleEntries(String propertyName) {
		ArrayList<String> properties = new ArrayList<String>();
		String propertiesString = (String) config.get(propertyName);
		propertiesString = propertiesString.replaceAll("\\s", ""); // strip
																	// whitespace
		String[] propertiesArray = propertiesString.split(",");
		for (int i = 0; i < propertiesArray.length; i++) {
			properties.add(propertiesArray[i]);
		}
		return properties;
	}

	public String resultfile() {
		return (String) config.get("ResultFile");
	}

	private void read() throws IOException {
		for (String fileName : startFiles()) {
			racers.setStartTimesFromFile(fileName);
		}

		File directory = new File(".");
		String[] finishFiles = directory.list(new FinishFileFilter());
		for (String fileName : finishFiles) {
			racers.setFinishTimesFromFile(fileName);
		}
	}

	// TODO Detta skall bort!!!
	private class FinishFileFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return name.startsWith("finish") && name.endsWith(".txt");
		}
	}

	/**
	 * @todo kolla vad första raden innehåller istället.
	 * @todo skicka in en Map<id, namn> till RacerMap istället
	 */
	private void readNames() throws IOException {
		Map<String, String> names = new NameReader()
				.readFromNameFile(namefile());

		names.remove("StartNr");
		racers.setNames(names);
	}

	private void write() {
		ArrayList<String> finishFiles = finishFiles();
		for (int i = 0; i < finishFiles.size(); i++){
			new ResultWriter(racers, resultfile(), null).writeToFile(laps());
			String timeStartIsOpen = (String) config.get("TimeStartIsOpen");
			new SortResultWriter(racers, (String) config.get("SortedResultFile"), new RacerRankingComparator(), timeStartIsOpen).writeToFile(laps());
		}
	}
}
