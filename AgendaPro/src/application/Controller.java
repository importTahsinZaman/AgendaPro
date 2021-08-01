package application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

//This is the controller that controls all the logic inside of the GUI (linked through the fxml file)

public class Controller {

	public static int weekDay = 0; // 0 = Friday, 1 = Saturday, 2 = Sunday, 3 = Monday, 4 = Tuesday, 5 = Wednesday,
	// 6 = Thursday
	public static String month = "";
	public static int monthDay = 1;
	public static String[] months = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };
	static String[] weekDays = { "Friday", "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday" };
	public static int[] daysInMonths = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	@FXML
	public TextArea txt1;
	@FXML
	public TextArea txt2;
	@FXML
	public TextArea txt3;

	@FXML
	public Label label1;
	@FXML
	public Label label2;
	@FXML
	public Label label3;

	public Controller() {
		txt1 = new TextArea();
		txt2 = new TextArea();
		txt3 = new TextArea();

		label1 = new Label();
		label2 = new Label();
		label3 = new Label();
	}

	public static int findWeekDay(int monthDay, int monthNumber) {
		LocalDate localDate = LocalDate.of(2021, monthNumber + 1, monthDay);
		java.time.DayOfWeek dayOfWeek = localDate.getDayOfWeek();

		String firstLetter = dayOfWeek.toString().substring(0, 1);
		String remainingLetters = (dayOfWeek.toString().substring(1, dayOfWeek.toString().length())).toLowerCase();
		return java.util.Arrays.asList(weekDays).indexOf(firstLetter + remainingLetters);
	}

	//Checks if the current value of monthDay exceeds the maximum number of days in the current month
	//If yes, goes to next month
	public static void checkMonthDaysMax() {
		if (monthDay > daysInMonths[java.util.Arrays.asList(months).indexOf(month)]) {
			month = months[(java.util.Arrays.asList(months).indexOf(month)) + 1];
			monthDay = 1;
			weekDay = findWeekDay(monthDay, (java.util.Arrays.asList(months).indexOf(month)));
		}
	}
	
	//Checks if the current value of monthDay is less than 0
	//If no, goes to previous month
	public static void checkMonthDaysMin() {
		if (monthDay <= 0) {
			month = months[(java.util.Arrays.asList(months).indexOf(month)) - 1];
			monthDay = daysInMonths[(java.util.Arrays.asList(months).indexOf(month))];
			weekDay = findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month));
		}
	}

	//Reads the text document where the agenda data is stored and displays text accordinly to the text areas
	//First line (DO NOT REMOVE LINE) is used as a place holder for adding new lines later on
	// "#~#" is used as a custom new line "character" to differentiate between new lines in the text area and new lines in the text file
	public void agendaRead() throws FileNotFoundException {
		try {
			Path filePath = Paths.get("testDocument.txt");
			Scanner fileScanner;
			try {
				Path filePath2 = Files.createFile(filePath);
				fileScanner = new Scanner(filePath2);
				List<String> fileContent = new ArrayList<>();
				fileContent.add("DO NOT REMOVE LINE\r\n" + "");
				Files.write(filePath2, fileContent, StandardCharsets.UTF_8);

			} catch (FileAlreadyExistsException e) {
				fileScanner = new Scanner(filePath);
			}

			List<String> lines = new ArrayList<String>();

			while (fileScanner.hasNextLine()) {
				lines.add(fileScanner.nextLine());
			}

			String label1checker = label1.getText().substring(label1.getText().indexOf(',') + 2,
					label1.getText().length());
			String label2checker = label2.getText().substring(label2.getText().indexOf(',') + 2,
					label2.getText().length());
			String label3checker = label3.getText().substring(label3.getText().indexOf(',') + 2,
					label3.getText().length());

			for (int i = 0; i < lines.size(); i++) {
				if (lines.get(i).contains(label1checker + " ")) {
					int subLength1 = (label1checker + " ").length();
					txt1.setText((lines.get(i).substring(subLength1, lines.get(i).length())).replace("#~#", "\n"));
				} else if (lines.get(i).contains(label2checker + " ")) {
					int subLength1 = (label2checker + " ").length();
					txt2.setText((lines.get(i).substring(subLength1, lines.get(i).length())).replace("#~#", "\n"));
				} else if (lines.get(i).contains(label3checker + " ")) {
					int subLength1 = (label3checker + " ").length();
					txt3.setText((lines.get(i).substring(subLength1, lines.get(i).length())).replace("#~#", "\n"));
				}
			}

			fileScanner.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//This is called whenever a new character has been written in one of the text areas.
	//Looks through the file for the correct Month and month day and saves text accordingly.
	public void checkAgendaWrite(int p) throws IOException {
		Path filePath = Paths.get("testDocument.txt");
		Scanner fileScanner = new Scanner(filePath);

		List<String> lines = new ArrayList<String>();

		while (fileScanner.hasNextLine()) {
			lines.add(fileScanner.nextLine());
		}

		fileScanner.close();

		Label[] labels = new Label[] { label1, label2, label3 };
		TextArea[] textAreas = new TextArea[] { txt1, txt2, txt3 };
		String text = "";
		
		String checker = labels[p].getText().substring(labels[p].getText().indexOf(',') + 2, labels[p].getText().length());
		System.out.println(checker);

		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).contains(checker + " ")) {
				text = textAreas[p].getText();
				lines.set(i, checker + " " + text.replace("\n", "#~#"));
				Files.write(filePath, lines, StandardCharsets.UTF_8);
				return;
			} else if (i == lines.size() - 1) {
				text = textAreas[p].getText();
				lines.add(checker + " " + text.replace("\n", "#~#"));
				Files.write(filePath, lines, StandardCharsets.UTF_8);
				return;
			}
		}
	}

	//txt1write, txt2write, txt3write all call checkAgendaWrite with their corresponding array indexes, 0,1,2
	//The arrays these numbers correspond to are (in checkAgendaWrite) labels and textAreas.s
	public void txt1write() throws IOException {
		checkAgendaWrite(0);
	}

	public void txt2write() throws IOException {
		checkAgendaWrite(1);
	}

	public void txt3write() throws IOException {
		checkAgendaWrite(2);
	}
	
	//Run on initialization of the application
	//Goes to the current day in the app
	@FXML
	public void initialize() throws IOException {
		pressMonthButton(months[Calendar.getInstance().get(Calendar.MONTH)], true);

		for (int i = 0; i < Calendar.getInstance().get(Calendar.DAY_OF_MONTH) / 3; i++) {
			monthDay++;

			checkMonthDaysMax();

			label1.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", "
					+ month + " " + monthDay);

			monthDay++;

			checkMonthDaysMax();

			label2.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", "
					+ month + " " + monthDay);

			monthDay++;

			checkMonthDaysMax();

			label3.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", "
					+ month + " " + monthDay);
		}
		String checkDay = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		
		if (!label3.getText().contains(checkDay) && !label2.getText().contains(checkDay) && !label1.getText().contains(checkDay)) {
			monthDay -= 3;

			checkMonthDaysMin();

			label3.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", " + month
					+ " " + monthDay);

			monthDay--;

			checkMonthDaysMin();

			label2.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", " + month
					+ " " + monthDay);

			monthDay--;

			checkMonthDaysMin();

			label1.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", " + month
					+ " " + monthDay);

			monthDay += 2;
		}
		
		if (!label3.getText().contains(checkDay) && !label2.getText().contains(checkDay) && !label1.getText().contains(checkDay)) {
			for (int q = 0; q<2; q++) {
				monthDay++;

				checkMonthDaysMax();

				label1.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", "
						+ month + " " + monthDay);

				monthDay++;

				checkMonthDaysMax();

				label2.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", "
						+ month + " " + monthDay);

				monthDay++;

				checkMonthDaysMax();

				label3.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", "
						+ month + " " + monthDay);
			}
		}
		agendaRead();
	}

	//Changes the month based on whichever month button is clicked
	//Month button methods are below (January to December)
	public void pressMonthButton(String tempMonth, boolean initialize) throws IOException {
		if (initialize) {
			month = tempMonth;
			monthDay = 1;

			label1.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", "
					+ month + " " + monthDay);

			monthDay++;

			label2.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", "
					+ month + " " + monthDay);

			monthDay++;

			label3.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", "
					+ month + " " + monthDay);

			agendaRead();
		} else {
			for (int i = 0; i < 2; i++) {

				txt1.clear();
				txt2.clear();
				txt3.clear();

				month = tempMonth;
				monthDay = 1;

				label1.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", "
						+ month + " " + monthDay);

				monthDay++;

				label2.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", "
						+ month + " " + monthDay);

				monthDay++;

				label3.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", "
						+ month + " " + monthDay);

				agendaRead();
			}
		}
	}

	@FXML
	public void pressJanuary(ActionEvent e) throws IOException {
		pressMonthButton("January", false);
	}

	public void pressFebruary(ActionEvent e) throws IOException {
		pressMonthButton("February", false);
	}

	public void pressMarch(ActionEvent e) throws IOException {
		pressMonthButton("March", false);
	}

	public void pressApril(ActionEvent e) throws IOException {
		pressMonthButton("April", false);
	}

	public void pressMay(ActionEvent e) throws IOException {
		pressMonthButton("May", false);
	}

	public void pressJune(ActionEvent e) throws IOException {
		pressMonthButton("June", false);
	}

	public void pressJuly(ActionEvent e) throws IOException {
		pressMonthButton("July", false);
	}

	public void pressAugust(ActionEvent e) throws IOException {
		pressMonthButton("August", false);
	}

	public void pressSeptember(ActionEvent e) throws IOException {
		pressMonthButton("September", false);
	}

	public void pressOctober(ActionEvent e) throws IOException {
		pressMonthButton("October", false);
	}

	public void pressNovember(ActionEvent e) throws IOException {
		pressMonthButton("November", false);
	}

	public void pressDecember(ActionEvent e) throws IOException {
		pressMonthButton("December", false);
	}

	//Shows the next three days of the month when the next button is clicked
	public void nextButton(ActionEvent e) throws IOException {

		txt1.clear();
		txt2.clear();
		txt3.clear();

		monthDay++;

		checkMonthDaysMax();

		label1.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", " + month
				+ " " + monthDay);

		monthDay++;

		checkMonthDaysMax();

		label2.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", " + month
				+ " " + monthDay);

		monthDay++;

		checkMonthDaysMax();

		label3.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", " + month
				+ " " + monthDay);

		agendaRead();
	}

	//Shows the previous three days when the previous button is clicked
	public void previousButton(ActionEvent e) throws IOException {

		txt1.clear();
		txt2.clear();
		txt3.clear();

		monthDay -= 3;

		checkMonthDaysMin();

		label3.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", " + month
				+ " " + monthDay);

		monthDay--;

		checkMonthDaysMin();

		label2.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", " + month
				+ " " + monthDay);

		monthDay--;

		checkMonthDaysMin();

		label1.setText(weekDays[findWeekDay(monthDay, java.util.Arrays.asList(months).indexOf(month))] + ", " + month
				+ " " + monthDay);

		monthDay += 2;

		agendaRead();
	}
}