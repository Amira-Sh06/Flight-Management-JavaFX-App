/**
 * The main1 class serves as the entry point for a JavaFX application
 * designed to manage airport flight operations. It provides a user interface
 * for interacting with flight data, including options to add, remove, sort,
 * filter, and display flights in both departure and arrival queues.
 */

 import java.io.*;
 import java.net.Socket;
 import java.time.LocalDate;
 import java.time.LocalTime;
 import java.util.ArrayList;
 import java.util.Comparator;
 import javafx.application.Application;
 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.event.ActionEvent;
 import javafx.event.EventHandler;
 import javafx.geometry.Insets;
 import javafx.scene.Scene;
 import javafx.scene.control.*;
 import javafx.scene.layout.*;
 import javafx.scene.text.Font;
 import javafx.stage.FileChooser;
 import javafx.stage.Stage;
 import java.time.LocalDateTime;
 import java.time.format.DateTimeFormatter;
 import java.util.List;
 
 
 public class main3 extends Application {
     // Observable lists to manage departure and arrival flight queues.
     private ObservableList<Flight> dpq = FXCollections.observableArrayList();
     private ObservableList<Flight> arq = FXCollections.observableArrayList();
 
     // TableView objects for displaying departure and arrival flight queues.
     private TableView<Flight> dpt = new TableView<>();
     private TableView<Flight> at = new TableView<>();
 
     @Override
     public void start(Stage stage) {
         // Set selection mode for both tables to allow selecting a single item at a time.
         dpt.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
         at.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
 
         // Main layout container for the UI, using BorderPane to organize elements.
         BorderPane mainLayout = new BorderPane();
         // Left panel (VBox) for the main menu with buttons and a title.
         VBox v1 = new VBox(20);
         v1.setPadding(new Insets(20));
 
         Label l1 = new Label("Main Menu");
 
         // Buttons for various actions in the system.
         Button b1 = new Button("Add Flight");
         Button b2 = new Button("Remove Flight");
         Button b3 = new Button("Display Queues");
         Button b4 = new Button("Sort and Filter");
         Button b5 = new Button("Load Data");
         Button b6 = new Button("Requeue Late Departures");
         Button b7 = new Button("Exit");
         v1.getChildren().addAll(l1, b1, b2, b3, b4, b5, b6, b7);  // Add all buttons and the label to the VBox.
 
         // Labels for the departure and arrival queues.
         Label l2 = new Label("Departure Queue:");
         Label l3 = new Label("Arrival Queue:");
         // Configure the TableView components for departure and arrival queues.
         configureTableView(dpt, "Departure");
         configureTableView(at, "Arrival");
 // VBox for the center section, containing the labels and the tables.
         VBox v2 = new VBox(10, l2, dpt, l3, at);
         v2.setPadding(new Insets(10));
 
         mainLayout.setLeft(v1);
         mainLayout.setCenter(v2);
 
         // Create a Scene with the main layout and set its dimensions.
         Scene scene = new Scene(mainLayout, 1000, 600);
         stage.setTitle("Airport Management System");
         stage.setScene(scene);
         stage.show();
 
         b1.setOnAction(e -> openAddFlightForm());
         b2.setOnAction(e -> removeflights());
         b3.setOnAction(e -> displayQueues());
         b4.setOnAction(e -> openSortAndFilterDialog());
         b5.setOnAction(e -> loadingData());
         b6.setOnAction(e -> requeueLateDepartures());
         b7.setOnAction(e -> stage.close());
     }
 
     private void openAddFlightForm() {
         Stage addFlightStage = new Stage();
         GridPane gp = new GridPane();
         gp.setHgap(10);
         gp.setVgap(10);
         gp.setPadding(new Insets(10));
 
         // Flight Information Panel
         Label flightLabel = new Label("Add Flight Information");
         flightLabel.setFont(new Font("Arial", 27));
         gp.add(flightLabel, 0, 0, 3, 1);
 
         // Flight number
         Label flightNumberLabel = new Label("Flight Number:");
         gp.add(flightNumberLabel, 0, 1);
         TextField flightNumberField = new TextField();
         gp.add(flightNumberField, 1, 1);
 
         // Airplane make
         Label airplaneMakeLabel = new Label("Airplane Make:");
         gp.add(airplaneMakeLabel, 0, 2);
         TextField airplaneMakeField = new TextField();
         gp.add(airplaneMakeField, 1, 2);
 
         // Departure Time
         Label departureLabel = new Label("Departure Time:");
         gp.add(departureLabel, 0, 3);
         TextField departureTimeField = new TextField();
         gp.add(departureTimeField, 1, 3);
 
         // Landing Time
         Label landingLabel = new Label("Landing Time:");
         gp.add(landingLabel, 0, 4);
         TextField landingTimeField = new TextField();
         gp.add(landingTimeField, 1, 4);
 
         // Flight Duration
         Label durationLabel = new Label("Flight Duration:");
         gp.add(durationLabel, 0, 5);
         TextField flightDurationField = new TextField();
         gp.add(flightDurationField, 1, 5);
 
         // Flight Type selection
         Label flightTypeLabel = new Label("Flight Type:");
         gp.add(flightTypeLabel, 0, 6);
         ToggleGroup flightTypeGroup = new ToggleGroup();
         RadioButton cargoRadio = new RadioButton("Cargo");
         cargoRadio.setToggleGroup(flightTypeGroup);
         RadioButton privateRadio = new RadioButton("Private");
         privateRadio.setToggleGroup(flightTypeGroup);
         RadioButton commercialRadio = new RadioButton("Commercial");
         commercialRadio.setToggleGroup(flightTypeGroup);
         gp.add(cargoRadio, 1, 6);
         gp.add(privateRadio, 1, 7);
         gp.add(commercialRadio, 1, 8);
 
         // Dynamic Fields
         Label l3 = new Label("Additional Information:");
         gp.add(l3, 0, 9);
 
         TextField tx1 = createTextField("Cargo Weight (kg)");
         TextField tx2 = createTextField("Owner's Name");
         TextField tx3 = createTextField("Passenger Count");
         gp.add(tx1, 1, 10);
         gp.add(tx2, 1, 11);
         gp.add(tx3, 1, 12);
         tx1.setVisible(false);
         tx2.setVisible(false);
         tx3.setVisible(false);
 
         flightTypeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
             if (newValue == cargoRadio) {
                 tx1.setVisible(true);
                 tx2.setVisible(false);
                 tx3.setVisible(false);
             } else if (newValue == privateRadio) {
                 tx1.setVisible(false);
                 tx2.setVisible(true);
                 tx3.setVisible(false);
             } else if (newValue == commercialRadio) {
                 tx1.setVisible(false);
                 tx2.setVisible(false);
                 tx3.setVisible(true);
             }
         });
 
         Button b8 = new Button("Add Flight");
         b8.setOnAction(e -> {
             if (flightNumberField.getText().isEmpty() ||
                     airplaneMakeField.getText().isEmpty() ||
                     departureTimeField.getText().isEmpty() ||
                     landingTimeField.getText().isEmpty() ||
                     flightDurationField.getText().isEmpty()) {
                 showAlert(Alert.AlertType.ERROR, "Error", "Empty flight", "All fields must be filled.");
                 return;
             }
             try {
                 Double.parseDouble(flightDurationField.getText());
             } catch (NumberFormatException a) {
                 showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "Duration time must be numeric.");
                 return;
             }
             String flightNumber = flightNumberField.getText();
             String airplaneMake = airplaneMakeField.getText();
             String flightType = ((RadioButton) flightTypeGroup.getSelectedToggle()).getText();
             String additionalInfo = cargoRadio.isSelected()?  "Cargo Weight: " + tx1.getText() :
                     privateRadio.isSelected() ? "Owner's Name: " + tx2.getText() :
                             "Passenger Count: " + tx3.getText();
 
             String departureTime = departureTimeField.getText();
             String landingTime = landingTimeField.getText();
             String flightDuration = flightDurationField.getText();
             Flight flight = new Flight(flightNumber, airplaneMake, flightType, additionalInfo, departureTime, landingTime, flightDuration);
             dpq.add(flight);
             addFlightStage.close();
         });
 
         gp.add(b8, 0, 13, 2, 1);
 
         Scene formScene = new Scene(gp, 600, 500);
         addFlightStage.setScene(formScene);
         addFlightStage.setTitle("Add Flight");
         addFlightStage.show();
     }
 
     private TextField createTextField(String prompt) {
         TextField textField = new TextField();
         textField.setPromptText(prompt);
         return textField;
     }
 
 
     private void displayQueues() {
         Stage queueStage = new Stage();
         queueStage.setTitle("Display Queues");
 
         VBox layout = new VBox(10);
         layout.setPadding(new Insets(10));
 
         Label departureLabel = new Label("Departure Queue:");
         TableView<Flight> departureTable = new TableView<>(dpq);
         configureTableView(departureTable, "Departure");
 
         Label arrivalLabel = new Label("Arrival Queue:");
         TableView<Flight> arrivalTable = new TableView<>(arq);
         configureTableView(arrivalTable, "Arrival");
 
         layout.getChildren().addAll(departureLabel, departureTable, arrivalLabel, arrivalTable);
 
         Scene queueScene = new Scene(layout, 800, 600);
         queueStage.setScene(queueScene);
         queueStage.show();
     }
 
     private void openSortAndFilterDialog() {
         Stage dialogStage = new Stage();
         VBox dialogLayout = new VBox(10);
         dialogLayout.setPadding(new Insets(10));
 
         // Create ComboBoxes for sorting and filtering
         ComboBox<String> sortComboBox = createComboBox("Sort By:",
                 "Flight Number", "Departure Time", "Landing Time", "Flight Duration");
         ComboBox<String> filterTypeComboBox = createComboBox("Filter By Type:",
                 "All", "Cargo", "Private", "Commercial");
 
         // Create buttons for applying and resetting filters
         Button applyButton = new Button("Apply");
         applyButton.setOnAction(e -> {
             applySortAndFilter(sortComboBox.getValue(), filterTypeComboBox.getValue());
             showAlert(Alert.AlertType.INFORMATION, "Success", "Sort/Filter Applied", "The flights have been successfully sorted/filtered.");
         });
 
 
         Button resetButton = new Button("Reset");
         resetButton.setOnAction(e -> resetFilters());
 
         dialogLayout.getChildren().addAll(sortComboBox, filterTypeComboBox, applyButton, resetButton);
 
         Scene dialogScene = new Scene(dialogLayout, 300, 200);
         dialogStage.setScene(dialogScene);
         dialogStage.setTitle("Sort and Filter");
         dialogStage.show();
     }
 
     private ComboBox<String> createComboBox(String label, String... items) {
         ComboBox<String> comboBox = new ComboBox<>();
         comboBox.getItems().addAll(items);
         comboBox.setValue(items[0]); // Set default value to the first item
         return comboBox;
     }
 
     private void applySortAndFilter(String sortBy, String filterType) {
         ObservableList<Flight> filteredDpq = FXCollections.observableArrayList(dpq);
         ObservableList<Flight> filteredArq = FXCollections.observableArrayList(arq);
 
         // Filter by flight type
         if (!filterType.equals("All")) {
             filteredDpq.removeIf(flight -> !flight.typeProperty().get().equals(filterType));
             filteredArq.removeIf(flight -> !flight.typeProperty().get().equals(filterType));
         }
 
         // Sort based on selected criteria
         Comparator<Flight> comparator = getComparator(sortBy);
         if (comparator != null) {
             filteredDpq.sort(comparator);
             filteredArq.sort(comparator);
         }
 
         // Update the TableView
         dpt.setItems(filteredDpq);
         at.setItems(filteredArq);
     }
 
     // Shows an alert with the specified parameters
     private void showAlert(Alert.AlertType type, String title, String header, String content) {
         Alert alert = new Alert(type);
         alert.setTitle(title);
         alert.setHeaderText(header);
         alert.setContentText(content);
         alert.showAndWait();
     }
 
     // Method for loading data from CSV files
     private void loadingData() {
         FileChooser fileChooser = new FileChooser();
         fileChooser.setTitle("Open Flight Data File");
         fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
 
         // Prompt user to select a file for the departure queue
         Stage fileStage = new Stage();
         boolean success = false;
 
         while(!success) {
             fileChooser.setInitialFileName("departure.csv");
             File departureFile = fileChooser.showOpenDialog(fileStage);
             // If a file is selected, proceed to load departure flights
             if (departureFile != null) {
                 try {
                     List<Flight> departureFlights = parseCSV(departureFile.getAbsolutePath());
                     dpq.addAll(departureFlights);
                     success = true;
                     showAlert(Alert.AlertType.INFORMATION, "Success", "Data Loaded", "Departure file successfully loaded.");
                 } catch (IOException e) {
                     showAlert(Alert.AlertType.ERROR, "Error", "File Read Error", "Could not read departure file.");
                 } catch (IllegalArgumentException e) {
                     showAlert(Alert.AlertType.ERROR, "Error", "Invalid File Format", "Please, select a SCV file.");
                 }
             } else {
                 showAlert(Alert.AlertType.ERROR, "Error", "No File Selected", "Please select a valid file.");
             }
         }
 
         success = false;
 
         while(!success) {
             // Prompt user to select a file for the arrival queue
             fileChooser.setInitialFileName("landing.csv");
             File arrivalFile = fileChooser.showOpenDialog(fileStage);
 
             // If a file is selected, proceed to load arrival flights
             if (arrivalFile != null) {
                 try {
                     List<Flight> arrivalFlights = parseCSV(arrivalFile.getAbsolutePath());
                     arq.addAll(arrivalFlights);
                     success = true;
                     showAlert(Alert.AlertType.INFORMATION, "Success", "Data Loaded", "Arrival file successfully loaded.");
                 } catch (IOException e) {
                     showAlert(Alert.AlertType.ERROR, "Error", "File Read Error", "Could not read arrival file.");
                 } catch (IllegalArgumentException e) {
                     showAlert(Alert.AlertType.ERROR, "Error", "Invalid File Format", "Please, select a CSV file.");
                 }
             } else {
                 showAlert(Alert.AlertType.ERROR, "Error", "No File Selected", "Please select a valid file.");
             }
         }
 
         // Refresh the TableViews after loading
         dpt.refresh();
         at.refresh();
 
         // Notify the user about successful loading
 
     }
 
     // Parses a CSV file and returns a list of flights
     private List<Flight> parseCSV(String filePath) throws IOException {
         List<Flight> flights = new ArrayList<>();
         try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
             String line;
             while ((line = br.readLine()) != null) {
                 String[] data = line.split(",");
                 if (data.length < 4) {
                     throw new IllegalArgumentException("Invalid file format. Each line must have at least 4 values.");
                 }
                 String flightNumber = data[0].trim();
                 String airplaneMake = data[1].trim();
                 String type = data[2].trim();
                 String additionalInfo = data[3].trim();
                 String departureTime = data[4].trim();
                 String landingTime = data[5].trim();
                 String flightDuration = data[6].trim();
 
                 // Create a Flight object and add it to the list
                 Flight flight = new Flight(flightNumber, airplaneMake, type, additionalInfo, departureTime, landingTime, flightDuration);
                 flights.add(flight);
             }
         }
         return flights;
     }
 
     private void requeueLateDepartures(){
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[H:mm][HH:mm]");
         ObservableList<Flight> lateFlights = FXCollections.observableArrayList();
 
         processLateFlights(dpq, lateFlights, formatter, "Departure Queue");
 
         if (!lateFlights.isEmpty()) {
             for (Flight flight : lateFlights) {
                 flight.setAdditionalInfo("Late: " + flight.getAdditionalInfo());
             }
 
             dpt.setRowFactory(tv -> new TableRow<>() {
                 @Override
                 protected void updateItem(Flight flight, boolean empty) {
                     super.updateItem(flight, empty);
                     if (flight == null || empty) {
                         setStyle("");
                     } else if (flight.getAdditionalInfo().startsWith("Late")) {
                         setStyle("-fx-background-color: lightcoral;");
                     } else {
                         setStyle("");
                     }
                 }
             });
 
             dpt.refresh();
 
             // Notify user about requeued flights
             showAlert(Alert.AlertType.INFORMATION, "Late Departures Requeued", "Requeued Late Departures", "null");
 
         } else {
             showAlert(Alert.AlertType.INFORMATION, "No Late Departures", null, "No flights in the departure queue are late.");
         }
 
         try {
             Socket s = new Socket("localhost", 5000);
             BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter pr = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
                 if(!dpq.isEmpty()) { 
                     pr.println("Dep reschedule");
                     String responce  = br.readLine();
                     if (responce.contains("Departure reschedules:")) {
                         showAlert(Alert.AlertType.INFORMATION, "Server Response", "Update Successful", responce);
                     }
                 } else if (!arq.isEmpty()) {
                     pr.println("Land schedule");
                     String responce  = br.readLine();
                     if (responce.contains("Landing reschedules:")) {
                         showAlert(Alert.AlertType.INFORMATION, "Server Response", "Update Successful", responce);
                     }
                 } else {
                     showAlert(Alert.AlertType.INFORMATION, "No late flights", null, null);
                 }

         br.close();
         pr.close();
         s.close();
     } catch (IOException e) {
         showAlert(Alert.AlertType.ERROR, "Connection error.", "Could not read a server", "Check connection with server.");}
     }
    
 
 private void processLateFlights(ObservableList<Flight> queue, ObservableList<Flight> lateFlights, DateTimeFormatter formatter, String queueName) {
     for (Flight flight : queue) {
         try {
             String time = flight.departureTimeProperty().get().trim();
             LocalDateTime departureTime = LocalDateTime.of(LocalDate.now(),
                     LocalTime.parse(time, formatter));
 
             if (departureTime.isBefore(LocalDateTime.now())) {
                 lateFlights.add(flight);
             }
         } catch (Exception e) {
             System.err.println("Invalid departure time format for flight (" + queueName + "): " + flight.flightNumberProperty().get());
         }
     }
 
     // Перемещение поздних рейсов в конец очереди
     if (!lateFlights.isEmpty()) {
         queue.removeAll(lateFlights);
         queue.addAll(lateFlights);
     }
 }
 
 
 private void removeflights() {
     // Check if a flight is selected in either queue
     Flight selectedDepartureFlight = dpt.getSelectionModel().getSelectedItem();
     Flight selectedArrivalFlight = at.getSelectionModel().getSelectedItem();
 
     if (selectedDepartureFlight != null) {
         // Confirm removal from the departure queue
         showRemoveConfirmation(selectedDepartureFlight, dpq);
     } else if (selectedArrivalFlight != null) {
         // Confirm removal from the arrival queue
         showRemoveConfirmation(selectedArrivalFlight, arq);
     } else {
         // No flight selected, show an error message
         showAlert(Alert.AlertType.ERROR, "No Flight Selected", "Please select a flight to remove.", "Select a flight from either the Departure or Landing queue.");
     }
 }
 
 
 private void showRemoveConfirmation(Flight flight, ObservableList<Flight> queue) {
     Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
     confirmationDialog.setTitle("Remove Flight");
     confirmationDialog.setHeaderText("Are you sure you want to remove this flight?");
     confirmationDialog.setContentText("Flight Number: " + flight.flightNumberProperty().get() +
             "\nAirplane Make: " + flight.airplaneMakeProperty().get());
 
     confirmationDialog.showAndWait().ifPresent(response -> {
         if (response == ButtonType.OK) {
             queue.remove(flight); // Remove the flight from the queue
         }
     });
 }
 
 
 private Comparator<Flight> getComparator(String sortBy) {
     if (sortBy.equals("Flight Number")) {
         return Comparator.comparing(flight -> flight.flightNumberProperty().get());
     } else if (sortBy.equals("Departure Time")) {
         return Comparator.comparing(flight -> flight.departureTimeProperty().get());
     } else if (sortBy.equals("Landing Time")) {
         return Comparator.comparing(flight -> flight.landingTimeProperty().get());
     } else if (sortBy.equals("Flight Duration")) {
         return Comparator.comparing(flight -> flight.flightDurationProperty().get());
     }
     return null; // Default case
 }
 
 private void resetFilters() {
     dpt.setItems(dpq);
     at.setItems(arq);
 }
 
 private void configureTableView(TableView<Flight> tableView, String type) {
     TableColumn<Flight, String> flightnum = new TableColumn<>("Flight Number");
     flightnum.setCellValueFactory(cell -> cell.getValue().flightNumberProperty());
 
     TableColumn<Flight, String> airplanemake = new TableColumn<>("Airplane Make");
     airplanemake.setCellValueFactory(cell -> cell.getValue().airplaneMakeProperty());
 
     TableColumn<Flight, String> Type = new TableColumn<>("Type");
     Type.setCellValueFactory(cell -> cell.getValue().typeProperty());
 
     TableColumn<Flight, String> additionalinfo = new TableColumn<>("Additional Info");
     additionalinfo.setCellValueFactory(cell -> cell.getValue().additionalInfoProperty());
 
     TableColumn<Flight, Void> remove = new TableColumn<>("Action");
     remove.setCellFactory(param -> new TableCell<>() {
         private final Button remove = new Button("Remove");
     });
 
     tableView.getColumns().addAll(flightnum, airplanemake, Type, additionalinfo);
     tableView.setItems(type.equals("Departure") ? dpq : arq);
 }
 
 
 
 public static void main(String[] args) {
     launch(args);
 
 }
 }